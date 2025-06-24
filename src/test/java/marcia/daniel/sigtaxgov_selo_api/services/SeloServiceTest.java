package marcia.daniel.sigtaxgov_selo_api.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.AssertTrue;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloRequestDto;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloResponseDto;
import marcia.daniel.sigtaxgov_selo_api.repositories.SeloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static marcia.daniel.sigtaxgov_selo_api.enums.Status.EXPIRADO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;

@ExtendWith(MockitoExtension.class)
class SeloServiceTest {

    @Mock
    private SeloRepository seloRepository;

    @InjectMocks
    private SeloService seloService;

    @Nested
    class getAll {

        @Test
        void shouldListAll() {
            // Arrange
            Selo selo = new Selo();
            selo.setCodigo("SEL-2025-0001");
            selo.setTipoProduto("tabaco");
            selo.setFabricante("Tabacos Angola Ltda");
            selo.setDataEmissao(LocalDate.of(2025, 6, 15));
            selo.setStatus(Status.EXPIRADO);

            when(seloRepository.findAll()).thenReturn(List.of(selo));

            // Act
            List<SeloResponseDto> output = seloService.getAll();

            // Assert
            assertEquals(1, output.size());
            assertEquals("SEL-2025-0001", output.get(0).codigo());
            assertEquals("tabaco", output.get(0).tipoProduto());
            assertEquals("Tabacos Angola Ltda", output.get(0).fabricante());
            assertEquals(LocalDate.of(2025, 6, 15), output.get(0).dataEmissao());
            assertEquals(EXPIRADO, output.get(0).status());
        }

        @Test
        void shouldReturnEmptyList(){
            // Arrange
            when(seloRepository.findAll()).thenReturn(List.of());
            // Act
            List<SeloResponseDto> output = seloService.getAll();
            //Assert
            assertThat(output).isEmpty();
            verify(seloRepository).findAll();
        }
   
    }

    @Nested
    class getByStatus{
        @Test
        void shouldReturnListOfSelosWithGivenStatus() {
            // Arrange
            Status status = Status.VALIDO;

            Selo selo = new Selo();
            selo.setCodigo("SEL-2025-0002");
            selo.setTipoProduto("bebida");
            selo.setFabricante("Cervejas Angola");
            selo.setDataEmissao(LocalDate.of(2025, 6, 20));
            selo.setStatus(status);

            when(seloRepository.findByStatus(status)).thenReturn(List.of(selo));

            // Act
            List<SeloResponseDto> output = seloService.getByStatus(status);

            // Assert
            assertEquals(1, output.size());
            assertEquals("SEL-2025-0002", output.get(0).codigo());
            assertEquals(Status.VALIDO, output.get(0).status());
            verify(seloRepository).findByStatus(status);
        }

        @Test
        void shouldReturnEmptyListWhenNoSelosHaveTheGivenStatus() {
            // Arrange
            Status status = Status.VALIDO;
            when(seloRepository.findByStatus(status)).thenReturn(List.of());

            // Act
            List<SeloResponseDto> output = seloService.getByStatus(status);

            // Assert
            assertThat(output).isEmpty();
            verify(seloRepository).findByStatus(status);
        }

        @Test
        void shouldThrowExceptionWhenRepositoryFails() {
            // Arrange
            Status status = Status.EXPIRADO;
            when(seloRepository.findByStatus(status)).thenThrow(new RuntimeException("Erro ao acessar o banco"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> seloService.getByStatus(status));
            verify(seloRepository).findByStatus(status);
        }
    }

    @Nested
    class GetByCodigo {

        @Test
        void shouldReturnSeloWhenCodigoExists() {
            // Arrange
            String codigo = "SEL-2025-0003";

            Selo selo = new Selo();
            selo.setCodigo(codigo);
            selo.setTipoProduto("bebida");
            selo.setFabricante("Águas Angola");
            selo.setDataEmissao(LocalDate.of(2025, 5, 10));
            selo.setStatus(Status.VALIDO);

            when(seloRepository.findByCodigo(codigo)).thenReturn(Optional.of(selo));

            // Act
            var output = seloService.getByCodigo(codigo);

            // Assert
            assertTrue(output.isPresent());

            verify(seloRepository).findByCodigo(codigo);
        }


        @Test
        void shouldReturnEmptyOptionalWhenCodigoDoesNotExist() {
            // Arrange
            String codigo = "SEL-0000-9999";
            when(seloRepository.findByCodigo(codigo)).thenReturn(Optional.empty());

            // Act
            Optional<Selo> result = seloService.getByCodigo(codigo);

            // Assert
            assertThat(result).isEmpty(); // ou assertFalse(result.isPresent());
            verify(seloRepository).findByCodigo(codigo);
        }

        @Test
        void shouldThrowExceptionWhenRepositoryFails() {
            // Arrange
            String codigo = "SEL-ERRO-0001";
            when(seloRepository.findByCodigo(codigo)).thenThrow(new RuntimeException("Erro inesperado"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> seloService.getByCodigo(codigo));
            verify(seloRepository).findByCodigo(codigo);
        }
    }

    @Nested
    class Save{
        @Test
        void shouldCreateSelo() {
            // Arrange
            var seloRequestDto = new SeloRequestDto("bebida", "Águas Angola", Status.CANCELADO);

            // Mocka o save para retornar o próprio selo
            when(seloRepository.save(any(Selo.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            var output = seloService.save(seloRequestDto);

            // Assert
            assertNotNull(output);
            assertEquals("bebida", output.tipoProduto());
            assertEquals("Águas Angola", output.fabricante());
            assertEquals(Status.CANCELADO, output.status());

            String expectedCodigo = "SEL-" + LocalDate.now().getYear() + "-0001";
            assertEquals(expectedCodigo, output.codigo());

            assertEquals(LocalDate.now(ZoneId.of("UTC")), output.dataEmissao());

            verify(seloRepository).save(any(Selo.class));
        }

        @Test
        void shouldThrowExceptionWhenSaveFails() {
            var dto = new SeloRequestDto("tabaco", "Tabacos SA", Status.VALIDO);
            when(seloRepository.findAll()).thenReturn(List.of());
            when(seloRepository.save(any())).thenThrow(new RuntimeException("Falha ao salvar"));

            assertThrows(RuntimeException.class, () -> seloService.save(dto));
        }


    }

}
package marcia.daniel.sigtaxgov_selo_api.services;

import lombok.RequiredArgsConstructor;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloRequestDto;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloResponseDto;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;
import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import marcia.daniel.sigtaxgov_selo_api.repositories.SeloRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeloService {

    private final SeloRepository seloRepository;

    public List<SeloResponseDto> getAll(){
        List<Selo> selos = seloRepository.findAll();

        return selos.stream().map(this::toDto).collect(Collectors.toList());
    }
    
    public List<SeloResponseDto> getByStatus(Status status){
            List<Selo> selos = seloRepository.findByStatus(status);

        return selos.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<Selo> getByCodigo(String codigo){
        return seloRepository.findByCodigo(codigo);
    }

    public SeloResponseDto save(SeloRequestDto seloRequestDto){
        var selo = new Selo();
        BeanUtils.copyProperties(seloRequestDto, selo);
        selo.setDataEmissao(LocalDate.now(ZoneId.of("UTC")));
        selo.setCodigo(this.gerarCodigo());
        return this.toDto(seloRepository.save(selo));
    }



    public SeloResponseDto toDto(Selo selo) {
        return new SeloResponseDto(
                selo.getCodigo(),
                selo.getTipoProduto(),
                selo.getFabricante(),
                selo.getDataEmissao(),
                selo.getStatus()
        );
    }

    public String gerarCodigo() {
        int ano = LocalDate.now().getYear();
        List<Selo> existentes = seloRepository.findAll();
        int proximaSequencia = existentes.size() + 1;
        return String.format("%s-%d-%04d", "SEL", ano, proximaSequencia);
    }


}

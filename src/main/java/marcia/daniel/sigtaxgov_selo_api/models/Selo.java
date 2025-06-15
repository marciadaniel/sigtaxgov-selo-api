package marcia.daniel.sigtaxgov_selo_api.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "selo_fiscal")
public class Selo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String codigo;

    @Column(length = 50, nullable = false)
    private String tipoProduto;

    @Column(length = 100, nullable = false)
    private String fabricante;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}

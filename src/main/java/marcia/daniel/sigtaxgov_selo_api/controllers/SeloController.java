package marcia.daniel.sigtaxgov_selo_api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloRequestDto;
import marcia.daniel.sigtaxgov_selo_api.dtos.SeloResponseDto;
import marcia.daniel.sigtaxgov_selo_api.enums.Status;
import marcia.daniel.sigtaxgov_selo_api.models.Selo;
import marcia.daniel.sigtaxgov_selo_api.services.RedisService;
import marcia.daniel.sigtaxgov_selo_api.services.SeloService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SeloController {

    private final SeloService seloService;
    private final RedisService redisService;

    @GetMapping("/api/v1/selos")
    public ResponseEntity<List<SeloResponseDto>> getAll(@RequestParam(required = false) Status status){

        List<SeloResponseDto> selos;

        if(status != null){
            selos = seloService.getByStatus(status);
           return selos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(selos);
        }

        selos = seloService.getAll();
        return ResponseEntity.ok(selos);
    }

    @GetMapping("/api/v1/selos/{codigo}")
    public ResponseEntity<SeloResponseDto> getByCodigo(@PathVariable @NotNull @NotBlank String codigo){

        var seloOptional = seloService.getByCodigo(codigo);

        if(seloOptional.isEmpty())
            return ResponseEntity.notFound().build();

        var selo = seloOptional.get();

        redisService.addSeloRecente(selo);

        return ResponseEntity.ok(seloService.toDto(selo));
    }

    @GetMapping("/api/v1/selos/recentes")
    public ResponseEntity<List<SeloResponseDto>> getSelosRecentes(){
        List<Selo> selos = redisService.getSelosRecentes();

        var selosDto = selos.stream().map(seloService::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(selosDto);
    }

    @PostMapping("/api/v1/selos")
    public ResponseEntity<SeloResponseDto>
    save(@RequestBody @Valid SeloRequestDto seloRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seloService.save(seloRequestDto));
    }



}

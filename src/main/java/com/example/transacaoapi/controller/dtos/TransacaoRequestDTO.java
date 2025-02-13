package com.example.transacaoapi.controller.dtos;

import java.time.OffsetDateTime;

public record TransacaoRequestDTO(Double valor, OffsetDateTime dataHora) {

}

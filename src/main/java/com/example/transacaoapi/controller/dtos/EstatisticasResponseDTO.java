package com.example.transacaoapi.controller.dtos;

public record EstatisticasResponseDTO(Long count,
                                      Double sum,
                                      Double avg,
                                      Double min,
                                      Double max) {

}
package com.example.transacaoapi.business.service;

import com.example.transacaoapi.business.services.EstatisticasService;
import com.example.transacaoapi.business.services.TransacaoService;
import com.example.transacaoapi.controller.dtos.EstatisticasResponseDTO;
import com.example.transacaoapi.controller.dtos.TransacaoRequestDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstatisticaServiceTest {

    @InjectMocks
    EstatisticasService estatisticasService;

    @Mock
    TransacaoService transacaoService;

    TransacaoRequestDTO transacaoRequestDTO;

    EstatisticasResponseDTO estatisticasResponseDTO;

    @BeforeEach
    void setUp() {
        transacaoRequestDTO = new TransacaoRequestDTO(20.0, OffsetDateTime.now());
        estatisticasResponseDTO = new EstatisticasResponseDTO(1L, 20.0,20.0,20.0,20.0);
    }

    @Test
    void calcularEstatisticaComSucesso() {
        when(transacaoService.buscarTransacoes(60))
                .thenReturn(Collections.singletonList(transacaoRequestDTO));

        EstatisticasResponseDTO resultado = estatisticasService.calcularEstatisticasTransacoes(60);

        verify(transacaoService, times(1)).buscarTransacoes(60);
        Assertions.assertThat(resultado).usingRecursiveComparison().isEqualTo(estatisticasResponseDTO);
    }

    @Test
    void calcularEstatisticaQuandoListaVazia() {

        EstatisticasResponseDTO estatisticaEsperada = new EstatisticasResponseDTO(0L, 0.0,0.0,0.0,0.0);
        when(transacaoService.buscarTransacoes(60))
                .thenReturn(Collections.emptyList());

        EstatisticasResponseDTO resultado = estatisticasService.calcularEstatisticasTransacoes(60);

        verify(transacaoService, times(1)).buscarTransacoes(60);
        Assertions.assertThat(resultado).usingRecursiveComparison().isEqualTo(estatisticaEsperada);
    }
}

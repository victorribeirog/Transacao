 package com.example.transacaoapi.business.service;

import com.example.transacaoapi.business.services.EstatisticasService;
import com.example.transacaoapi.business.services.TransacaoService;
import com.example.transacaoapi.controller.dtos.EstatisticasResponseDTO;
import com.example.transacaoapi.controller.dtos.TransacaoRequestDTO;
import com.example.transacaoapi.infrastructure.exceptions.UnprocessableEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    TransacaoService transacaoService;

    TransacaoRequestDTO transacaoRequestDTO;

    EstatisticasResponseDTO estatisticasResponseDTO;

    @BeforeEach
    void setUp() {
        transacaoRequestDTO = new TransacaoRequestDTO(20.0, OffsetDateTime.now());
        estatisticasResponseDTO = new EstatisticasResponseDTO(1L, 20.0,20.0,20.0,20.0);
    }

    @Test
    void deveAdicionarTransacaoComSucesso() {

        transacaoService.adicionarTransacoes(transacaoRequestDTO);

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);

        assertTrue(transacoes.contains(transacaoRequestDTO));
    }

    @Test
    void deveLancarExcecaoCasoValorNegativo() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTransacoes(new TransacaoRequestDTO(-20.0, OffsetDateTime.now())));

        assertEquals("Valor não pode ser menor que 0", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoCasoDataouHoraMaiorQueAtual() {

        UnprocessableEntity exception = assertThrows(UnprocessableEntity.class,
                () -> transacaoService.adicionarTransacoes(new TransacaoRequestDTO(20.0, OffsetDateTime.now().plusDays(1))));

        assertEquals("Data e hora maiores que a data e hora atuais", exception.getMessage());
    }

    @Test
    void deveLimparTransacaoComSucesso() {

        transacaoService.limparTransacoes();

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(5000);

        assertTrue(transacoes.isEmpty());
    }

    @Test
    void deveBuscarTransacoesDentroDoIntervalo() {

        TransacaoRequestDTO dto = new TransacaoRequestDTO(20.0, OffsetDateTime.now().minusHours(1));

        transacaoService.adicionarTransacoes(transacaoRequestDTO);
        transacaoService.adicionarTransacoes(dto);

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(60);

        assertTrue(transacoes.contains(transacaoRequestDTO));
        assertFalse(transacoes.contains(dto));
    }
}

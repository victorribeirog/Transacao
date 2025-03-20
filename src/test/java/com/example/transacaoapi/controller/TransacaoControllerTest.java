package com.example.transacaoapi.controller;

import com.example.transacaoapi.business.services.TransacaoService;
import com.example.transacaoapi.controller.dtos.TransacaoRequestDTO;
import com.example.transacaoapi.infrastructure.exceptions.UnprocessableEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoControllerTest {

    @InjectMocks
    TransacaoController transacaoController;

    @Mock
    TransacaoService transacaoService;

    TransacaoRequestDTO transacao;

    MockMvc mockMvc;

    @Autowired
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
        transacao = new TransacaoRequestDTO(20.0, OffsetDateTime.of(2025,3,20,19,43,0,0, ZoneOffset.UTC));

    }

    @Test
    void DeveAdicionarTransacaoComSucesso() throws Exception {

        doNothing().when(transacaoService).adicionarTransacoes(transacao);

        mockMvc.perform(post("/transacao")
                .content(objectMapper.writeValueAsString(transacao))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void deveGerarExcecaoAoAdicionarTransacao() throws Exception {
        doThrow(new UnprocessableEntity("Erro de requisição")).when(transacaoService).adicionarTransacoes(transacao);
        mockMvc.perform(post("/transacao")
                        .content(objectMapper.writeValueAsString(transacao))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deveDeletarTransacoesComSucesso() throws Exception {
        doNothing().when(transacaoService).limparTransacoes();
        mockMvc.perform(delete("/transacao"))
                .andExpect(status().isOk());
    }
}

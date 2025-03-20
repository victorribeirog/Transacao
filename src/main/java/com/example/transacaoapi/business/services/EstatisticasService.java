package com.example.transacaoapi.business.services;

import com.example.transacaoapi.controller.dtos.EstatisticasResponseDTO;
import com.example.transacaoapi.controller.dtos.TransacaoRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticasService {

    public final TransacaoService transacaoService;


    public EstatisticasResponseDTO calcularEstatisticasTransacoes(Integer intervaloBusca) {

        log.info("Iniciada busca de estatísticas de transações pelo período de tempo " + intervaloBusca);

        long startTime = System.currentTimeMillis();

        List<TransacaoRequestDTO> transacoes = transacaoService.buscarTransacoes(intervaloBusca);

        if(transacoes.isEmpty()){
            return new EstatisticasResponseDTO(0L, 0.0, 0.0, 0.0, 0.0);
        }

        DoubleSummaryStatistics estatisticasTransacoes = transacoes.stream()
                .mapToDouble(TransacaoRequestDTO::valor).summaryStatistics();

        long finish = System.currentTimeMillis();

        System.out.println("Tempo de requisição: " + (finish - startTime) + " ms");

        log.info("Estatisticas retornadas com sucesso");
        return new EstatisticasResponseDTO(estatisticasTransacoes.getCount(),
                estatisticasTransacoes.getSum(),
                estatisticasTransacoes.getAverage(),
                estatisticasTransacoes.getMin(),
                estatisticasTransacoes.getMax());
    }

}
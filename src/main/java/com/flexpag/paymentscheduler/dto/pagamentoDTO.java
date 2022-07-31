package com.flexpag.paymentscheduler.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.flexpag.paymentscheduler.enumStatus.Status;
import com.flexpag.paymentscheduler.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class pagamentoDTO implements Serializable {


    private Long id;

    private String descricao;

    private Double valor;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dataHoraPagamento;

    @Enumerated(EnumType.STRING)
    private Status statusPagamento;

    public static pagamentoDTO converterParaDto(Pagamento pag){

        return pagamentoDTO.builder()
                .id(pag.getId())
                .descricao(pag.getDescricao())
                .statusPagamento(pag.getStatusPagamento())
                .dataHoraPagamento(pag.getDataHoraPagamento())
                .valor(pag.getValor())
                .build();
    }

}

package com.flexpag.paymentscheduler.service;

import com.flexpag.paymentscheduler.advice.NotFoundException;
import com.flexpag.paymentscheduler.dto.pagamentoDTO;
import com.flexpag.paymentscheduler.enumStatus.Status;
import com.flexpag.paymentscheduler.entity.Pagamento;
import com.flexpag.paymentscheduler.repository.pagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class pagamentoService {

    @Autowired
    private final pagamentoRepository repository;


    public void escolherStatus(Pagamento pag){

        pag.setStatusPagamento(Status.pending);

        log.info("O pagamento com a descricao = {} mudou", pag.getDescricao());
        repository.save(pag);
    }

    public List<Pagamento> buscarTodos(){

        log.info("Buscando todos os pagamentos registrados");
        return repository.findAll();
    }

    public Optional<Pagamento> buscarPorId(Long id){

        Pagamento pag = repository.findById(id).orElse(null);
        log.info("Buscando pagamento pelo id {}", pag.getId());
        return repository.findById(id);
    }

    public Pagamento mudancaStatus(Pagamento pag){

        pag.setStatusPagamento(Status.paid);
        log.info("O pagamento foi realizado com sucesso");
        return repository.save(pag);

    }

    private Pagamento construindoPagamento(pagamentoDTO pagamentoDto){
        return Pagamento.builder()
                .id(pagamentoDto.getId())
                .descricao(pagamentoDto.getDescricao())
                .statusPagamento(Status.pending)
                .valor(pagamentoDto.getValor())
                .build();
    }

    public pagamentoDTO criandoPagamento(pagamentoDTO pagDTO){
        Pagamento pag = repository.save(construindoPagamento(pagDTO));
        return pagDTO;

    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public Pagamento atualizarPagamento(Pagamento pag){

        LocalDateTime dateTime = pag.getDataHoraPagamento();

        if (pag.getStatusPagamento().equals(Status.valueOf("paid"))){
            throw new RuntimeException("O pagamento já foi realizado e não pode ser editado");
        } else {
            pag.setDataHoraPagamento(dateTime);
        }
        pag.setDataHoraPagamento(LocalDateTime.now());
        return repository.save(pag);
    }


    public void deletePagamento(Long id){
        Pagamento pag = buscarPorId(id).orElseThrow(()-> new NotFoundException("Não foi possivel encontrar o ID referente a essa busca: " + id));
        
        if (pag.getStatusPagamento().equals(Status.valueOf("pending"))){
            deletePagamento(id);
        } else {
            throw new RuntimeException("O pagamento já foi realizado e não pode ser excluido ou editados");

        }
        log.info("Apagando o pagamento referente ao Id = {}", pag.getId());
        repository.deleteById(id);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public pagamentoDTO criandoPayment(pagamentoDTO pagDTO){
        Pagamento pagamento = repository.save(construindoPagamento(pagDTO));
        return pagDTO;
    }

}

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
                .dataHoraPagamento(pagamentoDto.getDataHoraPagamento())
                .statusPagamento(Status.pending)
                .valor(pagamentoDto.getValor())
                .build();
    }


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public Pagamento atualizarPagamento(Pagamento pag){
        log.info("Atualizando o pagamento referente ao Id = {}", pag.getId(), pag.getDescricao());

        LocalDateTime dateTime = pag.getDataHoraPagamento();

        if (pag.getStatusPagamento().equals(Status.valueOf("paid"))){
            throw new RuntimeException("O pagamento já foi realizado e não pode ser editado ou excluído");
        }

        pag.setDataHoraPagamento(dateTime);
        pag.setStatusPagamento(Status.paid);
        return repository.save(pag);
    }


    public void deletePagamento(Long id){
        Pagamento pag = buscarPorId(id).orElseThrow(()-> new NotFoundException("Não foi possivel encontrar o ID referente a essa busca: " + id));
        Status status = pag.getStatusPagamento();

        if (pag.getStatusPagamento().equals(Status.paid)){
            throw new RuntimeException("O pagamento já foi realizado e não pode ser excluido");
        }
        log.info("Apagando o pagamento referente ao Id = ", pag.getId());
        repository.deleteById(id);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public pagamentoDTO criandoPayment(pagamentoDTO pagDTO){
        log.info("Criando o pagamento referente ao id = " + pagDTO.getId() + pagDTO.getDescricao());
        Pagamento pagamento = repository.save(construindoPagamento(pagDTO));
        return pagDTO;
    }

    public List<Pagamento> buscandoPorStatus(String pagStatus){
        log.info("Filtro por Status..." + pagStatus);
        List<Pagamento> pagList = repository.findByStatusPagamento(pagStatus);
        return pagList;
    }

}

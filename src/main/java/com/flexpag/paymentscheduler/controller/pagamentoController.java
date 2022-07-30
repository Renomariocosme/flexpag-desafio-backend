package com.flexpag.paymentscheduler.controller;


import com.flexpag.paymentscheduler.dto.pagamentoDTO;
import com.flexpag.paymentscheduler.enumStatus.Status;
import com.flexpag.paymentscheduler.entity.Pagamento;
import com.flexpag.paymentscheduler.service.pagamentoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/pagamento")
@Slf4j
@CrossOrigin("*")
public class pagamentoController {

    @Autowired
    private pagamentoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Pagamento> todosPagamentos(){
        return service.buscarTodos();
    }


    @GetMapping("/{id}")
    public Pagamento buscarPagamentoPorId(@PathVariable Long id){
        log.info("Pagamento referente ao id não foi encontrado = {}" + (id));
        return service.buscarPorId(id).orElseThrow(()-> new RuntimeException("Pagamento referente ao agendamento foi não encontrado"));
    }


    @PostMapping("/criar")
    public ResponseEntity<pagamentoDTO> salvarPagamento(@Valid @RequestBody pagamentoDTO pagDTO){
        pagDTO.setStatusPagamento(Status.pending);
        return new ResponseEntity<>(service.criandoPayment(pagDTO), HttpStatus.CREATED);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Pagamento> atualizarPagamento(@Valid @RequestBody Pagamento pag){

        log.info("Atualizando o pagamento conforme permita o status: {}", pag.getDescricao());
        return new ResponseEntity<>(service.atualizarPagamento(pag), HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletandoPagamento(@PathVariable Long id){
        log.info("Excluindo o pagamento referente ao id: {}", id);
        service.deletePagamento(id);
    }


}

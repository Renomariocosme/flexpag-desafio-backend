package com.flexpag.paymentscheduler.repository;

import com.flexpag.paymentscheduler.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface pagamentoRepository extends JpaRepository <Pagamento, Long> {

    Optional<Pagamento> findById(Long id);
}

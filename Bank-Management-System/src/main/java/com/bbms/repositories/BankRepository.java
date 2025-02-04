package com.bbms.repositories;

import com.bbms.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    public Optional<Bank> findBankByBankName(String bankName);
}

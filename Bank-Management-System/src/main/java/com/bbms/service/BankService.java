package com.bbms.service;

import com.bbms.entities.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankService {
    public Bank findBankById(Long bankId);
    public Bank findBankByName(String bankName);
    public List<Bank> findAllBanks();
    public Page<Bank> findAllBanksPage(Pageable page);
    public Bank saveBank(Bank newBank);
    public Bank updateBank(Long bankId, Bank updateBank );
    public void deleteBank(Long bankId);
}

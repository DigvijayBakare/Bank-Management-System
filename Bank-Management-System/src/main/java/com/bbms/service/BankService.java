package com.bbms.service;

import com.bbms.entities.Bank;

import java.util.List;

public interface BankService {
    public Bank findBankById(Long bankId);
    public Bank findBankByName(String bankName);
    public List<Bank> findAllBanks();
    public Bank saveBank(Bank newBank);
    public Bank updateBank(Long bankId, Bank updateBank );
    public void deleteBank(Long bankId);
}

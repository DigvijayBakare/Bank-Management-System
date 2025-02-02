package com.bbms.service.impl;

import com.bbms.custom_exception.BankNotFoundException;
import com.bbms.entities.Bank;
import com.bbms.repositories.BankRepository;
import com.bbms.service.BankService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public Bank findBankById(Long bankId) {
        return bankRepository.findById(bankId)
                .orElseThrow(() -> new BankNotFoundException("Bank with id " + bankId + " does not exists!"));
    }

    @Override
    public Bank findBankByName(String bankName) {
        return bankRepository.findBankByBankName(bankName)
                .orElseThrow(() -> new BankNotFoundException("Bank with name " + bankName + " does not exists!"));
    }

    @Override
    public List<Bank> findAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    public Bank saveBank(Bank newBank) {
        return bankRepository.save(newBank);
    }

    @Override
    public Bank updateBank(Long bankId, @Valid Bank updateBank) {
        Bank bank = bankRepository.findById(bankId).
                orElseThrow(() -> new BankNotFoundException("Bank with id: " + bankId + " not found!!"));

        bank.setBankId(bankId);
        bank.setBankContact(updateBank.getBankContact());
        bank.setActive(updateBank.isActive());
        bank.setBankName(updateBank.getBankName());
        bank.setBankEstablishedOn(updateBank.getBankEstablishedOn());
        bank.setBankEmail(updateBank.getBankEmail());

        bankRepository.save(bank);

        return bank;
    }

    @Override
    public void deleteBank(Long bankId) {
        bankRepository.findById(bankId).orElseThrow(() ->
                new BankNotFoundException("Bank with id: " + bankId + " not found!"));
        bankRepository.deleteById(bankId);
    }
}

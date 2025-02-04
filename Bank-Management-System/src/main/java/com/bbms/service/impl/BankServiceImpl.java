package com.bbms.service.impl;

import com.bbms.custom_exception.BankNotFoundException;
import com.bbms.entities.Bank;
import com.bbms.repositories.BankRepository;
import com.bbms.service.BankService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private MessageSource messageSource;

    public BankServiceImpl(BankRepository bankRepository, MessageSource messageSource) {
        this.bankRepository = bankRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Bank findBankById(Long bankId) {
        return bankRepository.findById(bankId)
                .orElseThrow(() -> new BankNotFoundException(
                        messageSource.getMessage("ge.handleBankWithIdNotFoundException",
                                new Object[]{bankId}, LocaleContextHolder.getLocale())));
    }

    @Override
    public Bank findBankByName(String bankName) {
        return bankRepository.findBankByBankName(bankName)
                .orElseThrow(() -> new BankNotFoundException(
                        messageSource.getMessage("ge.handleBankWithNameNotFoundException",
                                new Object[]{bankName}, LocaleContextHolder.getLocale())));
    }

    @Override
    public Page<Bank> findAllBanksPage(Pageable page) {
        Page<Bank> bankPage = this.bankRepository.findAll(page);
        if (bankPage.isEmpty()) {
            logger.error("No bank details found on desired page!!");
            throw new BankNotFoundException(messageSource.getMessage("ge.handleBankPageNotFoundException",
                    null, LocaleContextHolder.getLocale()));
        }
        logger.info("Page returned successfully!");
        return bankPage;
    }

    @Override
    public List<Bank> findAllBanks() {
        List<Bank> bankList = bankRepository.findAll();
        if (bankList.isEmpty()) {
            logger.error("No data available!");
            throw new BankNotFoundException(messageSource.getMessage("ge.handleBankNotFoundException",
                    null, LocaleContextHolder.getLocale()));
        }
        logger.info("List of all banks returned successfully!");
        return bankList;
    }

    @Override
    public Bank saveBank(Bank newBank) {
        return bankRepository.save(newBank);
    }

    @Override
    public Bank updateBank(Long bankId, @Valid Bank updateBank) {
        Bank bank = bankRepository.findById(bankId).
                orElseThrow(() -> new BankNotFoundException(
                        messageSource.getMessage("ge.handleBankWithIdNotFoundException",
                                new Object[]{bankId}, LocaleContextHolder.getLocale())));

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
                new BankNotFoundException(
                        messageSource.getMessage("ge.handleBankWithIdNotFoundException",
                                new Object[]{bankId}, LocaleContextHolder.getLocale())));
        bankRepository.deleteById(bankId);
    }
}

package com.bbms.service.impl;

import com.bbms.custom_exception.BankNotFoundException;
import com.bbms.entities.Bank;
import com.bbms.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class BankServiceImplTest {
    @Mock
    private BankRepository bankRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BankServiceImpl bankService;

    Optional<Bank> bank = Optional.of(new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null));

    private List<Bank> banks;

    private Long existingBankId = 1L;
    private Long nonExistingBankId = 5L;
    private Bank existingBank;
    private Bank updateBank;
    private Pageable page;

    @BeforeEach
    void setUp() {
        this.bankService = new BankServiceImpl(this.bankRepository, this.messageSource);

        // Create sample bank data
        banks = Arrays.asList(
                new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null),
                new Bank(2L, "Bank2", new Date(), "0987654321", "bank2@gmail.com", true, null)
        );

        page = PageRequest.of(0, 10);

        when(bankRepository.findById(1L)).thenReturn(bank);
        when(bankRepository.findBankByBankName("Bank1")).thenReturn(bank);
        when(bankRepository.findAll()).thenReturn(banks);
        when(bankRepository.save(bank.get())).thenReturn(bank.get());

        // Create an existing bank object
        existingBank = new Bank(existingBankId, "Old Bank", new Date(), "1234567890", "oldbank@gmail.com", true, null);

        // Create an update bank object with new values
        updateBank = new Bank(null, "Updated Bank", new Date(), "0987654321", "updatedbank@gmail.com", false, null);
    }

    @Test
    void findBankByIdExists() {
        Bank bankById = bankService.findBankById(1L);
        assertEquals(bank.get(), bankById);
    }

    @Test
    void findBankByIdNotExists() {
        when(this.messageSource.getMessage(eq("ge.handleBankWithIdNotFoundException"),
                any(), any())).thenReturn("Bank with id " + nonExistingBankId + " does not exist!");

        assertThrows(BankNotFoundException.class, () -> bankService.findBankById(5L));
    }

    @Test
    void findBankByNameExist() {
        Bank bankByName = bankService.findBankByName("Bank1");
        assertEquals(bank.get(), bankByName);
    }

    @Test
    void findBankByNameNotExist() {
        String nonExistingBankName = "Bank12";
        when(this.messageSource.getMessage(eq("ge.handleBankWithNameNotFoundException"),
                any(), any())).thenReturn("Bank with id " + nonExistingBankName + " does not exist!");

        assertThrows(BankNotFoundException.class, () -> bankService.findBankByName(nonExistingBankName));
    }

    @Test
    void findAllBanks() {
        List<Bank> bankList = bankService.findAllBanks();
        assertEquals(2, bankList.size());
        assertEquals(banks, bankList);
    }

    @Test
    void findAllBankIfListIsEmpty() {
        when(bankService.findAllBanks()).thenReturn(new ArrayList<>());
        when(this.messageSource.getMessage(eq("ge.handleBankNotFoundException"),
                any(), any())).thenReturn("Bank data does not exist!");
        assertThrows(BankNotFoundException.class, () -> bankService.findAllBanks());
    }

    @Test
    void findAllBankWithPages() {
        Page<Bank> bankPage = new PageImpl<>(banks, page, banks.size());
        when(bankRepository.findAll(page)).thenReturn(bankPage);
        Page<Bank> allBanksPage = bankService.findAllBanksPage(page);
        assertNotNull(allBanksPage);
        assertEquals(bankPage, allBanksPage);
    }

    @Test
    void findAllBanksByPageThrowsException() {
        when(this.messageSource.getMessage(eq("ge.handleBankPageNotFoundException"),
                any(), any())).thenReturn("Bank data does not exist!");
        when(bankRepository.findAll(page)).thenReturn(Page.empty());
        assertThrows(BankNotFoundException.class, () -> bankService.findAllBanksPage(page));
    }

    @Test
    void saveBank() {
        Bank savedBank = bankService.saveBank(bank.get());
        assertEquals(savedBank, bank.get());
    }

    @Test
    void updateBankIdExists() {
        Mockito.when(bankRepository.findById(existingBankId)).thenReturn(Optional.of(existingBank));
        bankService.updateBank(existingBankId, updateBank);

        Mockito.verify(bankRepository).save(existingBank);
        assertEquals("Updated Bank", existingBank.getBankName());
        assertEquals("0987654321", existingBank.getBankContact());
        assertFalse(existingBank.isActive());
        assertEquals("updatedbank@gmail.com", existingBank.getBankEmail());
    }

    @Test
    void updateBankIdNotExists() {
        when(this.messageSource.getMessage(eq("ge.handleBankWithIdNotFoundException"),
                any(), any())).thenReturn("Bank with id " + nonExistingBankId + " does not exist!");

        when(bankRepository.findById(nonExistingBankId)).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> bankService.updateBank(nonExistingBankId, updateBank));
    }

    @Test
    void deleteBankWhenBankExists() {
        bankService.deleteBank(existingBankId);
    }

    @Test
    void deleteBankWhenBankDoesNotExists() {
        when(this.messageSource.getMessage(eq("ge.handleBankWithIdNotFoundException"),
                any(), any())).thenReturn("Bank with id " + nonExistingBankId + " does not exist!");

        assertThrows(BankNotFoundException.class,
                () -> bankService.deleteBank(nonExistingBankId));
    }
}
package com.bbms.service.impl;

import com.bbms.custom_exception.BankNotFoundException;
import com.bbms.entities.Bank;
import com.bbms.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankServiceImplTest {
    @Autowired
    private BankServiceImpl bankService;

    @Mock
    private BankRepository bankRepository;

    Optional<Bank> bank = Optional.of(new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null));

    private List<Bank> banks;

    private Long existingBankId = 1L;
    private Long nonExistingBankId = 5L;
    private Bank existingBank;
    private Bank updateBank;

    @BeforeEach
    void setUp() {
        this.bankService = new BankServiceImpl(this.bankRepository);

        // Create sample bank data
        banks = Arrays.asList(
                new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null),
                new Bank(2L, "Bank2", new Date(), "0987654321", "bank2@gmail.com", true, null)
        );

        Mockito.when(bankRepository.findById(1L)).thenReturn(bank);
        Mockito.when(bankRepository.findBankByBankName("Bank1")).thenReturn(bank);
        Mockito.when(bankRepository.findAll()).thenReturn(banks);
        Mockito.when(bankRepository.save(bank.get())).thenReturn(bank.get());

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
        assertThrows(BankNotFoundException.class, () -> bankService.findBankById(5L));
    }

    @Test
    void findBankByNameExist() {
        Bank bankByName = bankService.findBankByName("Bank1");
        assertEquals(bank.get(), bankByName);
    }

    @Test
    void findBankByNameNotExist() {
        assertThrows(BankNotFoundException.class, ()-> bankService.findBankByName("Bank12"));
    }

    @Test
    void findAllBanks() {
        List<Bank> bankList = bankService.findAllBanks();
        assertEquals(2, bankList.size());
        assertEquals(banks, bankList);
    }

    @Test
    void saveBank() {
        Bank savedBank = bankService.saveBank(bank.get());
        assertEquals(savedBank, bank.get());
    }

    @Test
    void updateBankIdExists() {
        Mockito.when(bankRepository.findById(existingBankId)).thenReturn(Optional.of(existingBank));
        bankService.updateBank(existingBankId,updateBank);

        Mockito.verify(bankRepository).save(existingBank);
        assertEquals("Updated Bank", existingBank.getBankName());
        assertEquals("0987654321", existingBank.getBankContact());
        assertFalse(existingBank.isActive());
        assertEquals("updatedbank@gmail.com", existingBank.getBankEmail());
    }

    @Test
    void updateBankIdNotExists() {
        Mockito.when(bankRepository.findById(nonExistingBankId)).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> bankService.updateBank(nonExistingBankId, updateBank));
    }

    @Test
    void deleteBankWhenBankExists() {
        bankService.deleteBank(existingBankId);
    }

    @Test
    void deleteBankWhenBankDoesNotExists() {
        assertThrows(BankNotFoundException.class,
                () -> bankService.deleteBank(nonExistingBankId));
    }
}
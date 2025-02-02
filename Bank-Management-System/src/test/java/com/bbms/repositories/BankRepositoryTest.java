package com.bbms.repositories;

import com.bbms.entities.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BankRepositoryTest {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    Bank bankPersist = new Bank(1L, "Bank1", new Date(), "1236547890", "bank1@gmail.com", true, null);

    @BeforeEach
    void setUp() {
        this.testEntityManager.merge(bankPersist);
    }

    @Test
    void findBankByBankName() {
        Bank bank = bankRepository.findBankByBankName("Bank1").get();
//        assertEquals(bankPersist, bank);
        assertEquals(bankPersist.getBankId(), bank.getBankId());
        assertEquals(bankPersist.getBankName(), bank.getBankName());
        assertEquals(bankPersist.getBankContact(), bank.getBankContact());
        assertEquals(bankPersist.getBankEmail(), bank.getBankEmail());
        assertEquals(bankPersist.isActive(), bank.isActive());

        // Compare dates up to seconds
        assertEquals(bankPersist.getBankEstablishedOn().toInstant().truncatedTo(ChronoUnit.SECONDS),
                bank.getBankEstablishedOn().toInstant().truncatedTo(ChronoUnit.SECONDS));
    }
}
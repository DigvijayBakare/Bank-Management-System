package com.bbms.custom_exception;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankNotFoundExceptionTest {
    @Test
    void testBankNotFoundExceptionMessage() {
        Long bankId = 123L;
        String messageId = "Bank with ID {bankId} not found.";
        String expectedId = "Bank with ID 123 not found.";
        BankNotFoundException exceptionId = new BankNotFoundException(messageId, bankId);
        assertEquals(expectedId, exceptionId.getMessage());
    }

    @Test
    void testBankNotFoundExceptionForName() {
        String bankName = "SBI";
        String messageName = "Bank with name {bankName} not found.";
        String expectedName = "Bank with name SBI not found.";
        BankNotFoundException exceptionName = new BankNotFoundException(messageName, bankName);
        assertEquals(expectedName, exceptionName.getMessage());
    }
}
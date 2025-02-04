package com.bbms.custom_exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BranchNotFoundExceptionTest {
    @Test
    void testBranchNotFoundExceptionMessage() {
        Long bankId = 123L;
        String messageId = "Branch with ID {branchId} not found.";
        String expectedId = "Branch with ID 123 not found.";
        BranchNotFoundException exceptionId = new BranchNotFoundException(messageId, bankId);
        assertEquals(expectedId, exceptionId.getMessage());
    }

    @Test
    void testBranchNotFoundExceptionForName() {
        String bankName = "SBI";
        String messageName = "Branch with name {branchName} not found.";
        String expectedName = "Branch with name SBI not found.";
        BranchNotFoundException exceptionName = new BranchNotFoundException(messageName, bankName);
        assertEquals(expectedName, exceptionName.getMessage());
    }
}
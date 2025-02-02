package com.bbms.custom_exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExceptionHandlerHelperTest {
    private ExceptionHandlerHelper exceptionHandlerHelper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandlerHelper = new ExceptionHandlerHelper(); // Instantiate your exception handler
    }

    @Test
    void handleConstraintViolationException() {
        // Create a mock ConstraintViolation
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
//        Mockito.when(violation.getRootBeanClass()).thenReturn(Object.class);
        Mockito.doReturn(Object.class).when(violation).getRootBeanClass();
        Mockito.when(violation.getMessage()).thenReturn("must not be null");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        // Create the ConstraintViolationException
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Call the exception handler
        ResponseEntity<?> responseEntity = exceptionHandlerHelper.handleConstraintViolationException(exception);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        // Assuming Error class has a proper toString or getter for errors
        Error errorResponse = (Error) responseEntity.getBody();
        assertEquals("java.lang.Error: [java.lang.Object: must not be null]", errorResponse.toString());
    }

    @Test
    void handleBankNotFoundException() {
        // Create a mock BankNotFoundException
        String errorMessage = "Bank with ID 1 not found";
        BankNotFoundException exception = new BankNotFoundException(errorMessage);

        // Call the exception handler
        ResponseEntity<?> responseEntity = exceptionHandlerHelper.handleBankNotFoundException(exception);

        // Assert the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("bank not found in the record!!\n" + errorMessage, responseEntity.getBody());
    }

    @Test
    void handleBranchNotFoundException() {
        // Create an instance of BranchNotFoundException with a specific error message
        String errorMessage = "Branch with ID 1 not found";
        BranchNotFoundException exception = new BranchNotFoundException(errorMessage);

        // Call the exception handler
        ResponseEntity<String> responseEntity = exceptionHandlerHelper.handleBranchNotFoundException(exception);

        // Assert the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Branch is not found in records!!\n" + errorMessage, responseEntity.getBody());
    }
}
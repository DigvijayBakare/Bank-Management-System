package com.bbms.custom_exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionHandlerHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerHelper.class);

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + ": " + violation.getMessage());
        }
        Error response = new Error(String.valueOf(errors));
        return new ResponseEntity<Object>(response, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(value = BankNotFoundException.class)
    public ResponseEntity<?> handleBankNotFoundException(BankNotFoundException bnf) {
        String message = bnf.getMessage();
        logger.error("bank is not found in the record!!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("bank not found in the record!!\n" + message);
    }

    @ExceptionHandler(value = BranchNotFoundException.class)
    public ResponseEntity<String> handleBranchNotFoundException(BranchNotFoundException bnf){
        logger.error("branch is not present in the record!!!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bnf.getMessage());
    }
}


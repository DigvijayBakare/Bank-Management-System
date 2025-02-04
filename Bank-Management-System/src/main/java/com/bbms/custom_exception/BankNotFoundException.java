package com.bbms.custom_exception;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(String message) {
        super(message);
    }

    public BankNotFoundException(String message, Long bankId) {
        super(message.replace("{bankId}", String.valueOf(bankId)));
    }

    public BankNotFoundException(String message, String bankName) {
        super(message.replace("{bankName}", String.valueOf(bankName)));
    }
}
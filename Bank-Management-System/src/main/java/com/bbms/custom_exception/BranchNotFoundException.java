package com.bbms.custom_exception;

public class BranchNotFoundException extends RuntimeException{
    public BranchNotFoundException(String message){
        super(message);
    }
    public BranchNotFoundException(String message, Long branchId) {
        super(message.replace("{branchId}", String.valueOf(branchId)));
    }
}
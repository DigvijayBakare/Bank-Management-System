package com.bbms.custom_exception;

public class BranchNotFoundException extends RuntimeException{
    public BranchNotFoundException(String message){
        super(message);
    }
}
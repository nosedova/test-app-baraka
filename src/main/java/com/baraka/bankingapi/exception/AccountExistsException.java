package com.baraka.bankingapi.exception;

public class AccountExistsException extends RuntimeException {

    public AccountExistsException(String message) {
        super(message);
    }
}

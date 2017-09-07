package com.revolut.etransfer.domain.exception;

import static java.lang.String.format;

public class AccountNotFoundException extends RuntimeException {
    private static final String ACCOUNT_DOES_NOT_EXIST = "Account %s does not exist";

    public AccountNotFoundException(long accountNumber) {
        super(format(ACCOUNT_DOES_NOT_EXIST, accountNumber));
    }
}

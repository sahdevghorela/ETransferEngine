package com.revolut.etransfer.domain.exception;

import static java.lang.String.format;

public class NotEnoughBalanceException extends RuntimeException{
    private static final String NOT_ENOUGH_BALANCE_MESSAGE = "Not enough balance in account %s";

    public NotEnoughBalanceException(long accountNumber){
        super(format(NOT_ENOUGH_BALANCE_MESSAGE,accountNumber));
    }
}

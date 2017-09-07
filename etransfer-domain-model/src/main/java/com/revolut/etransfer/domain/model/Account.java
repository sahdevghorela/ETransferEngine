package com.revolut.etransfer.domain.model;

import java.math.BigDecimal;

public class Account {
    private long accountNumber;
    private BigDecimal balance;

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

package com.revolut.etransfer.domain.model;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class Account {
    private long accountNumber;
    private BigDecimal balance;

    public Account(long accountNumber){
        this(accountNumber, ZERO);
    }

    public Account(long accountNumber, BigDecimal withBalance) {
        this.accountNumber = accountNumber;
        this.balance = withBalance;
    }

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

    public boolean isBalanceBelow(BigDecimal amount) {
        return balance.compareTo(amount) < 0;
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}

package com.revolut.etransfer.service.impl;

import com.revolut.etransfer.dao.AccountManagementDao;
import com.revolut.etransfer.domain.model.Account;
import com.revolut.etransfer.service.AccountManagementService;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static java.util.Optional.ofNullable;

public class AccountManagementServiceImpl implements AccountManagementService {

    private AccountManagementDao accountDao;

    public AccountManagementServiceImpl(AccountManagementDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Account create() {
        return create(ZERO);
    }

    @Override
    public Account create(BigDecimal withBalance) {
        return accountDao.create(withBalance);
    }

    @Override
    public Optional<Account> find(long accountNumber) {
        return ofNullable(accountDao.find(accountNumber));
    }

    @Override
    public void delete(long accountNumber) {
        accountDao.delete(accountNumber);
    }

    @Override
    public void transfer(long fromAccount, long toAccount, BigDecimal amount) {
        validateAmount(amount);
        validateAccounts(fromAccount, toAccount);
        accountDao.transfer(fromAccount, toAccount, amount);
    }

    private void validateAccounts(long fromAccount, long toAccount) {
        if (fromAccount == toAccount) {
            throw new IllegalArgumentException("Source and Destination accounts can't be same");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || ZERO.compareTo(amount) >= 0) {
            throw new IllegalArgumentException("Transfer amount must be non null and positive");
        }
    }
}

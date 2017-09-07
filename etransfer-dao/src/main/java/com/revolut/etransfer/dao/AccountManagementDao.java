package com.revolut.etransfer.dao;

import com.revolut.etransfer.domain.model.Account;

import java.math.BigDecimal;

public interface AccountManagementDao {
    Account create(BigDecimal withBalance);
    Account find(long accountNumber);
    void delete(long accountNumber);
    void transfer(long fromAccountNumber, long toAccountNumber, BigDecimal amount);
}

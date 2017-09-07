package com.revolut.etransfer.service;

import com.revolut.etransfer.domain.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountManagementService {
    Account create();

    Account create(BigDecimal withBalance);

    Optional<Account> find(long accountNumber);

    void delete(long accountNumber);

    void transfer(long fromAccountNumber, long toAccountNumber, BigDecimal amount);
}

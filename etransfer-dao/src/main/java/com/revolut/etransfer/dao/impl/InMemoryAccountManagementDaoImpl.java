package com.revolut.etransfer.dao.impl;

import com.revolut.etransfer.dao.AccountManagementDao;
import com.revolut.etransfer.domain.exception.AccountNotFoundException;
import com.revolut.etransfer.domain.exception.NotEnoughBalanceException;
import com.revolut.etransfer.domain.model.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryAccountManagementDaoImpl implements AccountManagementDao {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<Long, Account>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final AtomicLong accountNumberGenerator = new AtomicLong(10);

    public Account create(BigDecimal withBalance) {
        long accountNumber = accountNumberGenerator.incrementAndGet();
        Account account = new Account(accountNumber, withBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    public void delete(long accountNumber) {
        try {
            lock.writeLock().lock();
            if (!accounts.containsKey(accountNumber)) {
                throw new AccountNotFoundException(accountNumber);
            }
            accounts.remove(accountNumber);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Account find(long accountNumber) {
        try {
            lock.readLock().lock();
            return accounts.getOrDefault(accountNumber, null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void transfer(long fromAccount, long toAccount, BigDecimal amount) {
        try {
            lock.writeLock().lock();
            if (!accounts.containsKey(fromAccount)) {
                throw new AccountNotFoundException(fromAccount);
            }
            if (!accounts.containsKey(toAccount)) {
                throw new AccountNotFoundException(toAccount);
            }
            Account sourceAccount = accounts.get(fromAccount);
            if (sourceAccount.isBalanceBelow(amount)) {
                throw new NotEnoughBalanceException(fromAccount);
            }
            Account destinationAccount = accounts.get(toAccount);
            sourceAccount.debit(amount);
            destinationAccount.credit(amount);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

package com.revolut.etransfer.dao.impl;

import com.revolut.etransfer.dao.AccountManagementDao;
import com.revolut.etransfer.domain.exception.AccountNotFoundException;
import com.revolut.etransfer.domain.exception.NotEnoughBalanceException;
import com.revolut.etransfer.domain.model.Account;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InMemoryAccountManagementDaoImplTest {

    private AccountManagementDao accountManagementDao;

    @Before
    public void setUp(){
        accountManagementDao = new InMemoryAccountManagementDaoImpl();
    }

    @Test
    public void shouldStartCreatingAccountFrom11AndSoOn(){
        Account account1 = accountManagementDao.create(ZERO);
        Account account2 = accountManagementDao.create(ZERO);
        Account account3 = accountManagementDao.create(ZERO);

        assertEquals(11,account1.getAccountNumber());
        assertEquals(12,account2.getAccountNumber());
        assertEquals(13,account3.getAccountNumber());
    }

    @Test
    public void shouldCreateAccountWithWithGivenBalance(){
        Account account = accountManagementDao.create(TEN);
        assertEquals(11,account.getAccountNumber());
        assertEquals(TEN,account.getBalance());
    }

    @Test
    public void shouldReturnNullWhenAccountNotFound(){
        Account shouldBeNull = accountManagementDao.find(11);
        assertNull(shouldBeNull);
    }

    @Test
    public void shouldFindAccountWhenCreatedPrior(){
        Account created = accountManagementDao.create(ZERO);
        Account found = accountManagementDao.find(created.getAccountNumber());
        assertNotNull(found);
        assertEquals(created.getAccountNumber(),found.getAccountNumber());
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowExceptionWhenAccountToBeDeletedNotFound(){
        accountManagementDao.delete(11);
    }

    @Test
    public void shouldNotFindAccountWhenDeleted(){
        Account created = accountManagementDao.create(TEN);
        assertNotNull(created);
        accountManagementDao.delete(created.getAccountNumber());
        Account found = accountManagementDao.find(created.getAccountNumber());
        assertNull(found);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowExceptionWhenSourceAccountToTransferNotFound(){
        Account destination = accountManagementDao.create(TEN);
        accountManagementDao.transfer(12,destination.getAccountNumber(),TEN);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowExceptionWhenTargetAccountToTransferNotFound(){
        Account source = accountManagementDao.create(TEN);
        accountManagementDao.transfer(source.getAccountNumber(),12,TEN);
    }

    @Test(expected = NotEnoughBalanceException.class)
    public void shouldThrowExceptionWhenSourceAccountBalanceIsNotGoodEnough(){
        Account source = accountManagementDao.create(TEN);
        Account target = accountManagementDao.create(ZERO);
        accountManagementDao.transfer(source.getAccountNumber(),target.getAccountNumber(),new BigDecimal("11"));
    }

    @Test
    public void shouldTransferBlanceFromSourceToTarget(){
        Account source = accountManagementDao.create(TEN);
        Account target = accountManagementDao.create(ZERO);
        accountManagementDao.transfer(source.getAccountNumber(),target.getAccountNumber(),new BigDecimal("5"));

        assertEquals(5,source.getBalance().intValue());
        assertEquals(5,target.getBalance().intValue());
    }
}
package com.revolut.etransfer.service.impl;

import com.revolut.etransfer.dao.AccountManagementDao;
import com.revolut.etransfer.domain.model.Account;
import com.revolut.etransfer.service.AccountManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagementServiceImplTest {

    @Mock
    private AccountManagementDao accountManagementDao;

    @InjectMocks
    private AccountManagementService accountManagementService = new AccountManagementServiceImpl(accountManagementDao);

    @Test
    public void shouldInvokeAccountDaoWithZeroBalanceWhenCreatedEmpty() {
        //Given When
        accountManagementService.create();

        //Then
        verify(accountManagementDao).create(ZERO);
    }

    @Test
    public void shouldInvokeAccountDaoWithBalanceWhenCreatedNotEmpty() {
        //Given When
        accountManagementService.create(TEN);

        //Then
        verify(accountManagementDao).create(TEN);
    }

    @Test
    public void shouldWrapAccountInOptionalWhenAccountFound() {
        //Given
        Account account = new Account(100, ONE);
        given(accountManagementDao.find(100)).willReturn(account);

        //When
        Optional<Account> found = accountManagementService.find(100);

        //Then
        assertTrue(found.isPresent());
        assertEquals(100, found.get().getAccountNumber());
        assertEquals(ONE, found.get().getBalance());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenAccountNotFound() {
        //Given
        given(accountManagementDao.find(100)).willReturn(null);

        //When
        Optional<Account> found = accountManagementService.find(100);

        //Then
        assertFalse(found.isPresent());
    }

    @Test
    public void shouldInvokeDaoWhenAccountNeedToBeDeleted() {
        //Give When
        accountManagementService.delete(100);

        //Then
        verify(accountManagementDao).delete(100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAmountToTransferIsNegative() {
        //Given When
        accountManagementService.transfer(100, 200, new BigDecimal("-1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAmountToTransferIsZero() {
        accountManagementService.transfer(100, 200, ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAmountToTransferIsNull() {
        accountManagementService.transfer(100, 200, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSourceAndTargetAccountsAreSame() {
        accountManagementService.transfer(100, 100, TEN);
    }

    @Test
    public void sholdInvokeAccountDaoWhenTransferWithValidValues() {
        //Given When
        accountManagementService.transfer(100, 200, TEN);

        //Then
        verify(accountManagementDao).transfer(100, 200, TEN);
    }

}
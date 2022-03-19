package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.AccountExistsException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.NewAccountDto;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.repository.AccountRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    public static final String DEFAULT_ACCOUNT_ID = "123";
    private final AccountRepository repository = new AccountRepositoryImpl();
    private final AccountService service = new AccountServiceImpl(repository);

    @Test
    void createAccount_Ok() {
        NewAccountDto newEntity = new NewAccountDto();
        newEntity.setAccountId("123");
        newEntity.setOwner("Mr. Big");
        ExistingAccountDto account = service.createAccount(newEntity);
        Assertions.assertNotNull(account.getId());

    }

    @Test
    void createAccount_ThisRowsAlreadyExist() {
        NewAccountDto newEntity = new NewAccountDto();
        newEntity.setAccountId("123");
        newEntity.setOwner("Mr. Big");
        service.createAccount(newEntity);
        assertThrows(AccountExistsException.class, () -> service.createAccount(newEntity));
    }

    @Test
    void getAccount_Ok() {
        addAccount();
        ExistingAccountDto account = service.getAccount(DEFAULT_ACCOUNT_ID);
        assertEquals(DEFAULT_ACCOUNT_ID, account.getAccountId());
    }

    @Test
    void getAccount_NotFound() {
        assertThrows(NotFoundException.class, () -> service.getAccount(DEFAULT_ACCOUNT_ID));
    }

    @Test
    void getAllAccounts_Ok() {
        addAccount();
        assertEquals(1, service.getAllAccounts().size());
    }

    @Test
    void deleteAccount_Ok() {
        addAccount();
        assertNotNull(service.deleteAccount(DEFAULT_ACCOUNT_ID));
        assertTrue(service.getAllAccounts().isEmpty());
    }

    @Test
    void deleteAccount_NotFound() {
        assertThrows(NotFoundException.class, () -> service.deleteAccount(DEFAULT_ACCOUNT_ID));
    }

    private void addAccount() {
        NewAccountDto newEntity = new NewAccountDto();
        newEntity.setAccountId(DEFAULT_ACCOUNT_ID);
        newEntity.setOwner("Mr. Big");
        service.createAccount(newEntity);
    }

}
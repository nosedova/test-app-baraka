package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.AccountExistsException;
import com.baraka.bankingapi.exception.NotEnoughMoneyException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.model.*;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.repository.AccountRepositoryImpl;
import com.baraka.bankingapi.view.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    public static final String DEFAULT_ACCOUNT_ID = "123";
    public static final String DEFAULT_ACCOUNT_ID2 = "321";
    private final AccountRepository repository = new AccountRepositoryImpl();
    private final CurrencyConverter currencyConverter = new CurrencyConverterImpl();
    private final AccountService service = new AccountServiceImpl(repository, currencyConverter);

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
        addAccount(DEFAULT_ACCOUNT_ID);
        ExistingAccountDto account = service.getAccount(DEFAULT_ACCOUNT_ID);
        assertEquals(DEFAULT_ACCOUNT_ID, account.getAccountId());
    }

    @Test
    void getAccount_NotFound() {
        assertThrows(NotFoundException.class, () -> service.getAccount(DEFAULT_ACCOUNT_ID));
    }

    @Test
    void getAllAccounts_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        assertEquals(1, service.getAllAccounts().size());
    }

    @Test
    void deleteAccount_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        assertNotNull(service.deleteAccount(DEFAULT_ACCOUNT_ID));
        assertTrue(service.getAllAccounts().isEmpty());
    }

    @Test
    void deleteAccount_NotFound() {
        assertThrows(NotFoundException.class, () -> service.deleteAccount(DEFAULT_ACCOUNT_ID));
    }

    @Test
    void deposit_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        MoneyOperatingDto operatingDto = MoneyOperatingDto.builder().amount(BigDecimal.TEN).accountId(DEFAULT_ACCOUNT_ID).build();
        assertEquals(BigDecimal.TEN, service.deposit(operatingDto).getBalance());
    }

    @Test
    void withdrawal_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        deposit(DEFAULT_ACCOUNT_ID, BigDecimal.valueOf(20));
        MoneyOperatingDto operatingDto = MoneyOperatingDto.builder().amount(BigDecimal.TEN).accountId(DEFAULT_ACCOUNT_ID).build();
        assertEquals(BigDecimal.TEN, service.withdrawal(operatingDto).getBalance());
    }

    @Test
    void withdrawal_NotEnoughMoney() {
        addAccount(DEFAULT_ACCOUNT_ID);
        MoneyOperatingDto operatingDto = MoneyOperatingDto.builder().amount(BigDecimal.TEN).accountId(DEFAULT_ACCOUNT_ID).build();
        assertThrows(NotEnoughMoneyException.class, () -> service.withdrawal(operatingDto));
    }

    @Test
    void transfer_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        deposit(DEFAULT_ACCOUNT_ID, BigDecimal.valueOf(20));
        addAccount(DEFAULT_ACCOUNT_ID2);

        MoneyTransferDto transferDto = MoneyTransferDto.builder().fromAccountId(DEFAULT_ACCOUNT_ID).toAccountId(DEFAULT_ACCOUNT_ID2).amount(BigDecimal.TEN).build();

        service.transfer(transferDto);
        assertEquals(BigDecimal.TEN, service.getAccount(DEFAULT_ACCOUNT_ID).getBalance());
        assertEquals(BigDecimal.TEN, service.getAccount(DEFAULT_ACCOUNT_ID2).getBalance());

    }

    @Test
    void transfer_SecondAccountDidntFound() {
        addAccount(DEFAULT_ACCOUNT_ID);
        deposit(DEFAULT_ACCOUNT_ID, BigDecimal.valueOf(20));

        MoneyTransferDto transferDto = MoneyTransferDto.builder().fromAccountId(DEFAULT_ACCOUNT_ID).toAccountId(DEFAULT_ACCOUNT_ID2).amount(BigDecimal.TEN).build();
        assertThrows(NotFoundException.class, () -> service.transfer(transferDto));
    }

    @Test
    void internationalTransfer_Ok() {
        addAccount(DEFAULT_ACCOUNT_ID);
        deposit(DEFAULT_ACCOUNT_ID, BigDecimal.valueOf(20));
        addAccount(DEFAULT_ACCOUNT_ID2);

        InternationalMoneyTransferDto transferDto = InternationalMoneyTransferDto.internationalBuilder()
                .fromAccountId(DEFAULT_ACCOUNT_ID)
                .toAccountId(DEFAULT_ACCOUNT_ID2)
                .amount(BigDecimal.TEN)
                .currency(Currency.EUR)
                .build();

        BigDecimal expectedAmount = BigDecimal.TEN.multiply(BigDecimal.valueOf(1.1));

        service.internationalTransfer(transferDto);
        assertEquals(BigDecimal.valueOf(20).subtract(expectedAmount), service.getAccount(DEFAULT_ACCOUNT_ID).getBalance());
        assertEquals(expectedAmount, service.getAccount(DEFAULT_ACCOUNT_ID2).getBalance());

    }

    private void addAccount(String accountId) {
        NewAccountDto newEntity = new NewAccountDto();
        newEntity.setAccountId(accountId);
        newEntity.setOwner("Mr. Big");
        service.createAccount(newEntity);
    }

    private void deposit(String accountId, BigDecimal amount) {
        service.deposit(MoneyOperatingDto.builder().accountId(accountId).amount(amount).build());
    }
}
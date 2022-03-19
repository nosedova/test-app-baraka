package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.NotEnoughMoneyException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.model.InternationalMoneyTransferDto;
import com.baraka.bankingapi.model.MoneyOperatingDto;
import com.baraka.bankingapi.model.MoneyTransferDto;
import com.baraka.bankingapi.model.NewAccountDto;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.repository.AccountRepositoryImpl;
import com.baraka.bankingapi.repository.TransactionRepository;
import com.baraka.bankingapi.repository.TransactionRepositoryImpl;
import com.baraka.bankingapi.view.Currency;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceImplTest {

    public static final String DEFAULT_ACCOUNT_ID = "123";
    public static final String DEFAULT_ACCOUNT_ID2 = "321";
    private final AccountRepository repository = new AccountRepositoryImpl();
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();
    private final CurrencyConverter currencyConverter = new CurrencyConverterImpl();
    private final TransactionService service = new TransactionServiceImpl(repository, transactionRepository, currencyConverter);
    private final AccountService accountService = new AccountServiceImpl(repository);

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
        assertEquals(BigDecimal.TEN, accountService.getAccount(DEFAULT_ACCOUNT_ID).getBalance());
        assertEquals(BigDecimal.TEN, accountService.getAccount(DEFAULT_ACCOUNT_ID2).getBalance());

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
        assertEquals(BigDecimal.valueOf(20).subtract(expectedAmount), accountService.getAccount(DEFAULT_ACCOUNT_ID).getBalance());
        assertEquals(expectedAmount, accountService.getAccount(DEFAULT_ACCOUNT_ID2).getBalance());

    }

    private void addAccount(String accountId) {
        NewAccountDto newEntity = new NewAccountDto();
        newEntity.setAccountId(accountId);
        newEntity.setOwner("Mr. Big");
        accountService.createAccount(newEntity);
    }

    private void deposit(String accountId, BigDecimal amount) {
        service.deposit(MoneyOperatingDto.builder().accountId(accountId).amount(amount).build());
    }
}
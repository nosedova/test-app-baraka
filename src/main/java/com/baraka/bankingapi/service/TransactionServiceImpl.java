package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.NotEnoughMoneyException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.mapper.AccountMapper;
import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.InternationalMoneyTransferDto;
import com.baraka.bankingapi.model.MoneyOperatingDto;
import com.baraka.bankingapi.model.MoneyTransferDto;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.repository.TransactionRepository;
import com.baraka.bankingapi.view.Account;
import com.baraka.bankingapi.view.Currency;
import com.baraka.bankingapi.view.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository repository;
    private final TransactionRepository transactionRepository;
    private final CurrencyConverter currencyConverter;

    @Autowired
    public TransactionServiceImpl(AccountRepository data, TransactionRepository transactionRepository, CurrencyConverter currencyConverter) {
        this.repository = data;
        this.transactionRepository = transactionRepository;
        this.currencyConverter = currencyConverter;
    }

    private Account getAccountModel(String accountId) {
        Account getAccount = repository.getByAccountId(accountId);
        if (getAccount == null) {
            throw new NotFoundException("Account not found");
        }
        return getAccount;
    }

    public ExistingAccountDto deposit(MoneyOperatingDto moneyOperation) {
        Account account = getAccountModel(moneyOperation.getAccountId());
        synchronized (account) {
            account.setBalance(account.getBalance().add(moneyOperation.getAmount()));
        }
        saveTransaction(moneyOperation.getAccountId(), moneyOperation.getAmount());

        return AccountMapper.INSTANCE.map(account);
    }

    public ExistingAccountDto withdrawal(MoneyOperatingDto moneyOperation) {
        Account account = getAccountModel(moneyOperation.getAccountId());
        synchronized (account) {
            if (account.getBalance().compareTo(moneyOperation.getAmount()) < 0) {
                throw new NotEnoughMoneyException("There is not enough money");
            }
            account.setBalance(account.getBalance().subtract(moneyOperation.getAmount()));
        }
        saveTransaction(moneyOperation.getAccountId(), moneyOperation.getAmount().negate());

        return AccountMapper.INSTANCE.map(account);
    }

    public void transfer(MoneyTransferDto moneyTransfer) {
        Account accountFrom = getAccountModel(moneyTransfer.getFromAccountId());
        Account accountTo = getAccountModel(moneyTransfer.getToAccountId());

        //find lock position to avoid deadlock
        Account accountLock1 = accountFrom.compareTo(accountTo) > 0 ? accountFrom : accountTo;
        Account accountLock2 = accountLock1 == accountFrom ? accountTo : accountFrom;
        if (accountFrom.getBalance().compareTo(moneyTransfer.getAmount()) < 0) {
            throw new NotEnoughMoneyException("There is not enough money");
        }
        synchronized (accountLock1) {
            synchronized (accountLock2) {
                accountFrom.setBalance(accountFrom.getBalance().subtract(moneyTransfer.getAmount()));
                accountTo.setBalance(accountTo.getBalance().add(moneyTransfer.getAmount()));
            }
        }

        saveTransaction(moneyTransfer.getFromAccountId(), moneyTransfer.getAmount().negate());
        saveTransaction(moneyTransfer.getToAccountId(), moneyTransfer.getAmount());


    }

    public void internationalTransfer(InternationalMoneyTransferDto moneyTransfer) {
        BigDecimal convertedSum = moneyTransfer.getAmount().multiply(currencyConverter.getExchangeRate(moneyTransfer.getCurrency()));
        MoneyTransferDto moneyTransferDto = MoneyTransferDto.builder()
                .fromAccountId(moneyTransfer.getFromAccountId())
                .toAccountId(moneyTransfer.getToAccountId())
                .amount(convertedSum)
                .build();
        transfer(moneyTransferDto);

        saveTransactionWithCurrency(moneyTransfer.getFromAccountId(), moneyTransfer.getAmount().negate(), moneyTransfer.getCurrency());
        saveTransactionWithCurrency(moneyTransfer.getToAccountId(), moneyTransfer.getAmount(), moneyTransfer.getCurrency());
    }


    private void saveTransaction(String accountId, BigDecimal amount) {
        saveTransactionWithCurrency(accountId, amount, Currency.USD);
    }

    private void saveTransactionWithCurrency(String accountId, BigDecimal amount, Currency currency) {
        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID().toString())
                .time(Instant.now())
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .build()
        );
    }
}

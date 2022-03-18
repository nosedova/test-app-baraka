package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.AccountExistsException;
import com.baraka.bankingapi.exception.NotEnoughMoneyException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.mapper.AccountMapper;
import com.baraka.bankingapi.model.*;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.view.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final CurrencyConverter currencyConverter;

    @Autowired
    public AccountServiceImpl(AccountRepository data, CurrencyConverter currencyConverter) {
        this.repository = data;
        this.currencyConverter = currencyConverter;
    }

    public ExistingAccountDto createAccount(NewAccountDto account) {
        if (repository.getByAccountId(account.getAccountId()) != null) {
            throw new AccountExistsException("Account already exists");
        }

        Account newAccount = AccountMapper.INSTANCE.map(account);
        return AccountMapper.INSTANCE.map(repository.save(newAccount));
    }

    public ExistingAccountDto getAccount(String accountId) {
        Account getAccount = getAccountModel(accountId);
        return AccountMapper.INSTANCE.map(getAccount);
    }

    private Account getAccountModel(String accountId) {
        Account getAccount = repository.getByAccountId(accountId);
        if (getAccount == null) {
            throw new NotFoundException("Account not found");
        }
        return getAccount;
    }

    public List<ExistingAccountDto> getAllAccounts() {
        return repository.getAllAccounts().stream().map(AccountMapper.INSTANCE::map).collect(Collectors.toList());

    }

    public ExistingAccountDto deleteAccount(String accountId) {
        Account deletedAccount = repository.delete(accountId);
        if (deletedAccount == null) {
            throw new NotFoundException("Account not found for delete");
        }
        return AccountMapper.INSTANCE.map(deletedAccount);
    }

    public ExistingAccountDto deposit(MoneyOperatingDto moneyOperation) {
        Account account = getAccountModel(moneyOperation.getAccountId());
        account.setBalance(account.getBalance().add(moneyOperation.getAmount()));
        return AccountMapper.INSTANCE.map(account);
    }

    public ExistingAccountDto withdrawal(MoneyOperatingDto moneyOperation) {
        Account account = getAccountModel(moneyOperation.getAccountId());

        if (account.getBalance().compareTo(moneyOperation.getAmount()) < 0) {
            throw new NotEnoughMoneyException("There is not enough money");
        }
        account.setBalance(account.getBalance().subtract(moneyOperation.getAmount()));
        return AccountMapper.INSTANCE.map(account);
    }

    public void transfer(MoneyTransferDto moneyTransfer) {
        MoneyOperatingDto moneyWithdrawal = MoneyOperatingDto.builder()
                .accountId(moneyTransfer.getFromAccountId())
                .amount(moneyTransfer.getAmount())
                .build();
        withdrawal(moneyWithdrawal);

        MoneyOperatingDto moneyDeposit = MoneyOperatingDto.builder()
                .accountId(moneyTransfer.getToAccountId())
                .amount(moneyTransfer.getAmount())
                .build();
        deposit(moneyDeposit);

    }

    public void internationalTransfer(InternationalMoneyTransferDto moneyTransfer) {
        BigDecimal convertedSum = moneyTransfer.getAmount().multiply(currencyConverter.getExchangeRate(moneyTransfer.getCurrency()));
        MoneyTransferDto moneyTransferDto = MoneyTransferDto.builder()
                .fromAccountId(moneyTransfer.getFromAccountId())
                .toAccountId(moneyTransfer.getToAccountId())
                .amount(convertedSum)
                .build();
        transfer(moneyTransferDto);
    }

}

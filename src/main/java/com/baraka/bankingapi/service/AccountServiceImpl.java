package com.baraka.bankingapi.service;

import com.baraka.bankingapi.exception.AccountExistsException;
import com.baraka.bankingapi.exception.NotFoundException;
import com.baraka.bankingapi.mapper.AccountMapper;
import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.NewAccountDto;
import com.baraka.bankingapi.repository.AccountRepository;
import com.baraka.bankingapi.view.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Autowired
    public AccountServiceImpl(AccountRepository data) {
        this.repository = data;
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
}

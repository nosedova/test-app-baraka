package com.baraka.bankingapi.repository;

import com.baraka.bankingapi.view.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Built-in store
 */
@Component
public class AccountRepositoryImpl implements AccountRepository {
    /**
     * primary key generator
     */
    private Long lastId = 0L;

    /**
     * Account list.
     * Account ID is a key
     */
    private final HashMap<String, Account> data = new HashMap<>();

    @Override
    public Account save(Account account) {
        account.setId(++lastId);
        data.put(account.getAccountId(), account);
        return account;
    }

    @Override
    public Account delete(String accountId) {
        return data.remove(accountId);
    }

    @Override
    public Account getByAccountId(String accountId) {
        return data.get(accountId);
    }

    @Override
    public List<Account> getAllAccounts() {
        return new ArrayList<>(data.values());
    }
}

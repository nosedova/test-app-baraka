package com.baraka.bankingapi.repository;

import com.baraka.bankingapi.view.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Built-in store
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {
    /**
     * primary key generator
     */
    private Long lastId = 0L;

    /**
     * Account map
     * Account ID is a key
     */
    private final Map<String, Account> data = new ConcurrentHashMap<>();

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

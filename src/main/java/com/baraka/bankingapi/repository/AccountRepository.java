package com.baraka.bankingapi.repository;

import com.baraka.bankingapi.view.Account;

import java.util.List;

public interface AccountRepository {
    Account save(Account account);

    Account delete(String accountId);

    Account getByAccountId(String accountId);

    List<Account> getAllAccounts();
}

package com.baraka.bankingapi.service;

import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.NewAccountDto;

import java.util.List;

/**
 * Account service
 */
public interface AccountService {
    /**
     * Create a new account
     *
     * @param account {@link ExistingAccountDto}
     * @return new account, AccountExistsException if account exists
     */
    ExistingAccountDto createAccount(NewAccountDto account);

    /**
     * Get and account
     *
     * @param accountId accountId
     * @return account, NotFoundException if account doesn't exist
     */
    ExistingAccountDto getAccount(String accountId);

    /**
     * Get all accounts
     *
     * @return List of {@link ExistingAccountDto}
     */
    List<ExistingAccountDto> getAllAccounts();

    /**
     * Delete an account
     *
     * @param accountId account Id
     * @return deleted account, NotFoundException if account doesn't exist
     */
    ExistingAccountDto deleteAccount(String accountId);
}

package com.baraka.bankingapi.service;

import com.baraka.bankingapi.model.*;

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

    /**
     * Add money to account balance
     *
     * @param moneyOperation accountId+amount
     * @return Account dto, NotFoundException if account doesn't exist
     */
    ExistingAccountDto deposit(MoneyOperatingDto moneyOperation);

    /**
     * Substruct money to account balance
     *
     * @param moneyOperation accountId+amount
     * @return {@link ExistingAccountDto}, NotFoundException if account doesn't exist, NotEnoughMoneyException if amount is larger than balance
     */
    ExistingAccountDto withdrawal(MoneyOperatingDto moneyOperation);

    /**
     * Transfers money between 2 accounts
     *
     * @param moneyTransfer fromAccountId+toAccountId+amount
     *                      throws NotFoundException if account doesn't exist, NotEnoughMoneyException if amount is larger than fromAccount balance
     */
    void transfer(MoneyTransferDto moneyTransfer);

    /**
     * Transfers money in different currency between 2 accounts
     *
     * @param moneyTransfer fromAccountId+toAccountId+amount
     *                      throws NotFoundException if account doesn't exist, NotEnoughMoneyException if amount is larger than fromAccount balance
     */
    void internationalTransfer(InternationalMoneyTransferDto moneyTransfer);


}

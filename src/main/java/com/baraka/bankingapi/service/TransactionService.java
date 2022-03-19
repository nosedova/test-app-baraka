package com.baraka.bankingapi.service;

import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.InternationalMoneyTransferDto;
import com.baraka.bankingapi.model.MoneyOperatingDto;
import com.baraka.bankingapi.model.MoneyTransferDto;

/**
 * Transaction service
 */
public interface TransactionService {
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

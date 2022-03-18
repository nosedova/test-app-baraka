package com.baraka.bankingapi.controller;

import com.baraka.bankingapi.model.*;
import com.baraka.bankingapi.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class ApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    private final AccountService accountService;

    @Autowired
    public ApiController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ExistingAccountDto createAccount(@Valid @RequestBody NewAccountDto record) {
        LOGGER.info("start saving account {}", record);
        ExistingAccountDto saved = accountService.createAccount(record);
        LOGGER.info("record saved: {}", saved);

        return saved;
    }

    @DeleteMapping(value = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ExistingAccountDto delete(@PathVariable String id) {
        LOGGER.info("start deleting account {}", id);
        ExistingAccountDto deleted = accountService.deleteAccount(id);
        LOGGER.info("record deleted: {}", deleted);

        return deleted;
    }

    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ExistingAccountDto> getAccounts() {
        List<ExistingAccountDto> allAccounts = accountService.getAllAccounts();
        LOGGER.info("getAllAccounts, {} rows found", allAccounts.size());
        return allAccounts;
    }

    @GetMapping(value = "/accounts/{id}/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    BigDecimal getBalanceByAccountId(@PathVariable String id) {
        BigDecimal balance = accountService.getAccount(id).getBalance();
        LOGGER.info("getByAccountId, row found by id {}", id);
        return balance;
    }

    @PostMapping(value = "/accounts/{id}/deposit/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    ExistingAccountDto deposit(@PathVariable String id, @PathVariable BigDecimal amount) {
        checkAmount(amount);
        LOGGER.info("deposit to accountId={}, amount= {}", id, amount);
        ExistingAccountDto account = accountService.deposit(MoneyOperatingDto.builder().accountId(id).amount(amount).build());
        LOGGER.info("deposit to accountId={}, amount= {} succeed", id, amount);

        return account;
    }

    @PostMapping(value = "/accounts/{id}/withdrawal/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    ExistingAccountDto withdrawal(@PathVariable String id, @PathVariable BigDecimal amount) {
        checkAmount(amount);
        LOGGER.info("withdrawal to accountId={}, amount= {}", id, amount);
        ExistingAccountDto account = accountService.withdrawal(MoneyOperatingDto.builder().accountId(id).amount(amount).build());
        LOGGER.info("withdrawal to accountId={}, amount= {} succeed", id, amount);

        return account;
    }

    @PostMapping(value = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void transfer(@Valid @RequestBody MoneyTransferDto transferDto) {
        checkAmount(transferDto.getAmount());
        LOGGER.info("transfer:{}", transferDto);
        accountService.transfer(transferDto);
        LOGGER.info("transfer {} succeed", transferDto);

    }

    @PostMapping(value = "/internationalTransfer", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void internationalTransfer(@Valid @RequestBody InternationalMoneyTransferDto intTransfer) {
        checkAmount(intTransfer.getAmount());
        LOGGER.info("internationalTransfer: {}", intTransfer);
        accountService.internationalTransfer(intTransfer);
        LOGGER.info("internationalTransfer {} succeed", intTransfer);

    }

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount must be positive");
        }
    }
}

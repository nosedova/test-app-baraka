package com.baraka.bankingapi.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Account
 */
@Getter
@Setter
@ToString
public class Account implements Comparable<Account> {

    /**
     * Row identification, primary key for database support
     */
    private Long id;

    /**
     * Account ID, must be unique
     */
    private String accountId;

    /**
     * Account owner
     */
    @NotBlank(message = "Account owner name is required")
    private String owner;

    /**
     * Account balance
     */
    private BigDecimal balance = BigDecimal.ZERO;

    private Currency currency = Currency.USD;

    @Override
    public int compareTo(Account o) {
        if (o == null) {
            return 1;
        }
        return this.getAccountId().compareTo(o.getAccountId());
    }
}

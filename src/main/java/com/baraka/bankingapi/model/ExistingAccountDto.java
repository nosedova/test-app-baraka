package com.baraka.bankingapi.model;

import com.baraka.bankingapi.view.Currency;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Existing account
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ExistingAccountDto extends NewAccountDto {

    /**
     * Row identification, primary key for database support
     */
    private Long id;

    /**
     * Account balance
     */
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Account currency
     */
    private Currency currency = Currency.USD;
}

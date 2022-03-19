package com.baraka.bankingapi.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Account
 */
@Getter
@Setter
@ToString
@Builder
public class Transaction {
    /**
     * transaction UUID
     */
    private String id;
    /**
     * Account ID
     */
    private String accountId;
    /**
     * Amount, + for deposit, - for withdrawal
     */
    private BigDecimal amount;

    /**
     * Currency
     */
    private Currency currency;

    /**
     * Transaction time
     */
    private Instant time;

}

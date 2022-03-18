package com.baraka.bankingapi.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for deposit/withdrawal operations
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoneyOperatingDto {

    /**
     * Account ID, IBAN
     */
    @Max(34)
    @NotNull
    private String accountId;

    /**
     * Deposit/withdrawal amount
     */
    private BigDecimal amount;
}

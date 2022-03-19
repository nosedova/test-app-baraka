package com.baraka.bankingapi.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 34)
    @NotNull
    private String accountId;

    /**
     * Deposit/withdrawal amount
     */
    private BigDecimal amount;
}

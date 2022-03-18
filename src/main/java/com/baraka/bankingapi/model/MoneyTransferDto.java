package com.baraka.bankingapi.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO fot money transfer
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferDto {

    /**
     * Transfer from account ID, IBAN
     */
    @Max(34)
    @NotNull
    private String fromAccountId;

    /**
     * Transfer to account ID, IBAN
     */
    @Max(34)
    @NotNull
    private String toAccountId;

    /**
     * Amount
     */
    private BigDecimal amount;
}

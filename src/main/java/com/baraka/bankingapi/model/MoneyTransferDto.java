package com.baraka.bankingapi.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 34)
    @NotNull
    private String fromAccountId;

    /**
     * Transfer to account ID, IBAN
     */
    @Size(max = 34)
    @NotNull
    private String toAccountId;

    /**
     * Amount
     */
    private BigDecimal amount;
}

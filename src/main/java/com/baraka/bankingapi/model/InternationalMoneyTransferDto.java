package com.baraka.bankingapi.model;

import com.baraka.bankingapi.view.Currency;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for international transfer money
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class InternationalMoneyTransferDto extends MoneyTransferDto {

    /**
     * Currency
     */
    private Currency currency;


    @Builder(builderMethodName = "internationalBuilder")
    public InternationalMoneyTransferDto(@Max(34) @NotNull String fromAccountId, @Max(34) @NotNull String toAccountId, BigDecimal amount, Currency currency) {
        super(fromAccountId, toAccountId, amount);
        this.currency = currency;
    }
}

package com.baraka.bankingapi.service;

import com.baraka.bankingapi.view.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyConverterImpl implements CurrencyConverter {

    @Override
    public BigDecimal getExchangeRate(Currency currency) {
        switch (currency) {
            case CNY:
                return BigDecimal.valueOf(0.16);
            case UAE:
                return BigDecimal.valueOf(0.27);
            case USD:
                return BigDecimal.ONE;
            case EUR:
                return BigDecimal.valueOf(1.1);
            case RUB:
                return BigDecimal.valueOf(0.0093);
            default:
                throw new IllegalArgumentException("Unknown currency");
        }
    }

}

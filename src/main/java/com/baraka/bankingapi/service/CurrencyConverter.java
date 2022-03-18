package com.baraka.bankingapi.service;

import com.baraka.bankingapi.view.Currency;

import java.math.BigDecimal;

public interface CurrencyConverter {
    BigDecimal getExchangeRate(Currency currency);
}

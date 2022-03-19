package com.baraka.bankingapi.repository;

import com.baraka.bankingapi.view.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
}

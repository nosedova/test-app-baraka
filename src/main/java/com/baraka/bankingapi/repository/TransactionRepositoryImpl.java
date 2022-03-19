package com.baraka.bankingapi.repository;

import com.baraka.bankingapi.view.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final List<Transaction> transactionList = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactionList.add(transaction);
    }
}

package com.techbank.account.query.api.queries.queries;

import com.techbank.account.query.api.queries.dto.EqualityType;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.domain.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountQueryHandler implements QueryHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        Optional<BankAccount> bankAccountOptional = accountRepository.findById(query.getId());
        if(bankAccountOptional.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccountOptional.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        var bankAccountOptional = accountRepository.findByAccountHolder(query.getAccountHolder());
        if(bankAccountOptional.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccountOptional.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountsWithBalanceQuery query) {
        List<BaseEntity> bankAccounts = null;
        if(EqualityType.GREATER_THAN == query.getEqualityType()) {
            bankAccounts = accountRepository.findByBalanceGreaterThan(query.getBalance());
        }
        if(EqualityType.LESS_THAN == query.getEqualityType()) {
            bankAccounts = accountRepository.findByBalanceLessThan(query.getBalance());
        }
        return bankAccounts;
    }
}

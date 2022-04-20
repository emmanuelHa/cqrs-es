package com.techbank.account.query.infra.handlers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        var bankAccountOptional = accountRepository.findById(event.getId());
        if(bankAccountOptional.isEmpty()) {
            return;
        }
        BankAccount bankAccount = bankAccountOptional.get();
        bankAccount.setBalance(bankAccount.getBalance() + event.getAmount());
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        var bankAccountOptional = accountRepository.findById(event.getId());
        if(bankAccountOptional.isEmpty()) {
            return;
        }
        BankAccount bankAccount = bankAccountOptional.get();
        bankAccount.setBalance(bankAccount.getBalance() - event.getAmount());
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }
}

package com.techbank.account.command.api.commands;

import com.techbank.account.command.domain.AccountAggregate;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler {

    @Autowired
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand openAccountCommand) {
        var aggregate = new AccountAggregate(openAccountCommand);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommands command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.depositFund(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(RestoreReadDbCommand command) {
        eventSourcingHandler.republishEvents();
    }
}

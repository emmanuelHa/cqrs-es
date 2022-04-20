package com.techbank.account.command.api.commands;

public interface CommandHandler {
    void handle(OpenAccountCommand command);
    void handle(DepositFundsCommands command);
    void handle(WithdrawFundsCommand command);
    void handle(CloseAccountCommand command);
    void handle(RestoreReadDbCommand command);
}

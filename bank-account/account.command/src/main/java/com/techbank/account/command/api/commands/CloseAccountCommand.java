package com.techbank.account.command.api.commands;

import com.techbank.cqrs.core.commands.BaseCommand;

public class CloseAccountCommand extends BaseCommand {

    public CloseAccountCommand(String id) {
        super(id);
    }
}

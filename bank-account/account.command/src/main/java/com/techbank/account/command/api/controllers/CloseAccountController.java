package com.techbank.account.command.api.controllers;

import com.techbank.account.command.api.commands.CloseAccountCommand;
import com.techbank.account.command.api.commands.DepositFundsCommands;
import com.techbank.account.command.api.dto.DepositFundsResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/V1/closeBankAccount")
public class CloseAccountController {

    private final Logger logger = Logger.getLogger(CloseAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable(value = "id") String id) {
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity(new BaseResponse("Close account request completed successfully"), HttpStatus.OK);
        }
        catch (IllegalStateException |AggregateNotFoundException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to close bank account " +
                    " with id - {0}", id);

            logger.log(Level.SEVERE, MessageFormat.format("Client made a bad request - {0}", safeErrorMessage));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

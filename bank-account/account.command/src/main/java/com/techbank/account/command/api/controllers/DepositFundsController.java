package com.techbank.account.command.api.controllers;

import com.techbank.account.command.api.commands.DepositFundsCommands;
import com.techbank.account.command.api.dto.DepositFundsResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/V1/depositFunds")
public class DepositFundsController {

    private final Logger logger = Logger.getLogger(DepositFundsController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String id,
                                              @RequestBody DepositFundsCommands depositFundsCommands) {
        depositFundsCommands.setId(id);
        try {
            commandDispatcher.send(depositFundsCommands);
            DepositFundsResponse depositFundsResponse = new DepositFundsResponse(id, "Deposit funds request completed successfully");
            return new ResponseEntity(depositFundsResponse, HttpStatus.OK);
        }
        catch (IllegalStateException ise) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", ise.toString()));
            return new ResponseEntity<>(new BaseResponse(ise.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to deposit funds " +
                    " with bank account id {0}", id);

            logger.log(Level.SEVERE, MessageFormat.format("Client made a bad request - {0}", safeErrorMessage));
            return new ResponseEntity<>(new DepositFundsResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

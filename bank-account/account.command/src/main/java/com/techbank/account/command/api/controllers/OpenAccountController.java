package com.techbank.account.command.api.controllers;

import com.techbank.account.command.api.commands.OpenAccountCommand;
import com.techbank.account.command.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/V1/openBankAccount")
public class OpenAccountController {

    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand) {
        var id = UUID.randomUUID().toString();
        openAccountCommand.setId(id);
        try {
            commandDispatcher.send(openAccountCommand);
            OpenAccountResponse openAccountResponse = new OpenAccountResponse(id, "Bank account created successfully");
            return new ResponseEntity(openAccountResponse, HttpStatus.CREATED);
        }
        catch (IllegalStateException ise) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", ise.toString()));
            return new ResponseEntity<>(new BaseResponse(ise.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to open " +
                    "a new bank account for id {0}", id);

            logger.log(Level.SEVERE, MessageFormat.format("Client made a bad request - {0}", safeErrorMessage));
            return new ResponseEntity<>(new OpenAccountResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

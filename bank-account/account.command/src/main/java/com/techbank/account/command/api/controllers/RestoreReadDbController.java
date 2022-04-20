package com.techbank.account.command.api.controllers;

import com.techbank.account.command.api.commands.RestoreReadDbCommand;
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
@RequestMapping(path = "/api/V1/restoreReadDb")
public class RestoreReadDbController {

    private final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    ResponseEntity<BaseResponse> restoreReadDb() {
        try {
            RestoreReadDbCommand restoreReadDbCommand = new RestoreReadDbCommand();
            commandDispatcher.send(restoreReadDbCommand);
            return new ResponseEntity(new BaseResponse("Restoring read Db"), HttpStatus.CREATED);
        }
        catch (IllegalStateException ise) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", ise.toString()));
            return new ResponseEntity<>(new BaseResponse(ise.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            var safeErrorMessage = "Error while processing request to restore read Db";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

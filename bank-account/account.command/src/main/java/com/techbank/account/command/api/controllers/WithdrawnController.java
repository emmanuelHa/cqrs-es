package com.techbank.account.command.api.controllers;

import com.techbank.account.command.api.commands.DepositFundsCommands;
import com.techbank.account.command.api.commands.WithdrawFundsCommand;
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
@RequestMapping(path = "/api/V1/withdrawnFunds")
public class WithdrawnController {

    private final Logger logger = Logger.getLogger(WithdrawnController.class.getName());

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    ResponseEntity<BaseResponse> withdrawnFunds(@PathVariable(value = "id") String id,
                                              @RequestBody WithdrawFundsCommand withdrawFundsCommand) {
        withdrawFundsCommand.setId(id);
        try {
            commandDispatcher.send(withdrawFundsCommand);
            return new ResponseEntity(new BaseResponse("Deposit funds request completed successfully"),
                    HttpStatus.OK);
        }
        catch (IllegalStateException ise) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", ise.toString()));
            return new ResponseEntity<>(new BaseResponse(ise.toString()), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            var safeErrorMessage = MessageFormat.format("Error while processing request to withdrawn funds " +
                    " with bank account id {0}", id);

            logger.log(Level.SEVERE, MessageFormat.format("Client made a bad request - {0}", safeErrorMessage));
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

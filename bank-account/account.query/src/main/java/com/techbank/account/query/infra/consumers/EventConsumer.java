package com.techbank.account.query.infra.consumers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consumer(@Payload AccountOpenedEvent event, Acknowledgment ack);
    void consumer(@Payload FundsDepositedEvent event, Acknowledgment ack);
    void consumer(@Payload FundsWithdrawnEvent event, Acknowledgment ack);
    void consumer(@Payload AccountClosedEvent event, Acknowledgment ack);
}

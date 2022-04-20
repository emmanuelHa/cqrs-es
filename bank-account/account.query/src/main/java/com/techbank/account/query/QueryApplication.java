package com.techbank.account.query;

import com.techbank.account.query.api.queries.queries.*;
import com.techbank.account.query.infra.AccountQueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	@Autowired
	private AccountQueryDispatcher accountQueryDispatcher;

	@Autowired
	private QueryHandler accountQueryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		accountQueryDispatcher.registerHandler(FindAllAccountsQuery.class, accountQueryHandler::handle);
		accountQueryDispatcher.registerHandler(FindAccountByIdQuery.class, accountQueryHandler::handle);
		accountQueryDispatcher.registerHandler(FindAccountByHolderQuery.class, accountQueryHandler::handle);
		accountQueryDispatcher.registerHandler(FindAccountsWithBalanceQuery.class, accountQueryHandler::handle);
	}

}

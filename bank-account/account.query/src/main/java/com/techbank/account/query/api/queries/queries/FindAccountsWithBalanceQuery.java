package com.techbank.account.query.api.queries.queries;

import com.techbank.account.query.api.queries.dto.EqualityType;
import com.techbank.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountsWithBalanceQuery extends BaseQuery {
    private EqualityType equalityType;
    private double balance;
}

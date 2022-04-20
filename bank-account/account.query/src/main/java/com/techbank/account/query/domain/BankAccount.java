package com.techbank.account.query.domain;

import com.techbank.account.common.dto.AccountType;
import com.techbank.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BankAccount extends BaseEntity {
    @javax.persistence.Id
    private String id;

    private String accountHolder;
    private Date creationDate;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private double balance;



}
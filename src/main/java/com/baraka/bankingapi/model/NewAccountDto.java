package com.baraka.bankingapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Account
 */
@Getter
@Setter
@ToString
public class NewAccountDto {

    /**
     * Account ID, IBAN
     */
    @Max(34)
    @NotNull
    private String accountId;

    /**
     * Account owner
     */
    @NotNull
    private String owner;
}

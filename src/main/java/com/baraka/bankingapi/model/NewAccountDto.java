package com.baraka.bankingapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(max = 34)
    @NotNull
    private String accountId;

    /**
     * Account owner
     */
    @NotNull
    private String owner;
}

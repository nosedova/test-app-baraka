package com.baraka.bankingapi.mapper;

import com.baraka.bankingapi.model.ExistingAccountDto;
import com.baraka.bankingapi.model.NewAccountDto;
import com.baraka.bankingapi.view.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * View-model and vice-versa converter
 */
@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account map(NewAccountDto dto);

    ExistingAccountDto map(Account model);


}

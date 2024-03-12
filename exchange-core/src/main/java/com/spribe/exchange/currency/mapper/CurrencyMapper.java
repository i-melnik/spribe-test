package com.spribe.exchange.currency.mapper;

import com.spribe.exchange.currency.dto.CurrencyDto;
import com.spribe.exchange.currency.entity.Currency;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper()
public interface CurrencyMapper {

    @Mapping(target = "description", source = "name")
    CurrencyDto map(Currency currency);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "description")
    @Mapping(target = "code", source = "code")
    Currency map(CurrencyDto dto);
}

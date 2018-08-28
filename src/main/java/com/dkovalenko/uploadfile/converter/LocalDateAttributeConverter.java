package com.dkovalenko.uploadfile.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class LocalDateAttributeConverter implements Converter<LocalDate, Date> {

    @Override
    public Date convert(LocalDate localDate) {
        return localDate == null
                ? null
                : Date.valueOf(localDate);
    }
}

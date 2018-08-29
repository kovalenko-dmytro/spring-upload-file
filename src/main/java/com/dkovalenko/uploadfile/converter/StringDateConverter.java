package com.dkovalenko.uploadfile.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StringDateConverter implements Converter<LocalDate, String> {

    @Override
    public String convert(LocalDate localDate) {
        return localDate.toString();
    }
}

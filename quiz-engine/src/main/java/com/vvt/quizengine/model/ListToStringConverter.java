package com.vvt.quizengine.model;

import antlr.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListToStringConverter implements AttributeConverter<List<Long>, String> {
    @Override
    public String convertToDatabaseColumn(List<Long> attribute) {
        return String.join(",", attribute.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    @Override
    public List<Long> convertToEntityAttribute(String dbData) {
        return dbData == null ? Collections.emptyList() :
                Arrays.asList(dbData.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
    }
}

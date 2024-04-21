package com.onetuks.goguma_bookstore.book.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.goguma_bookstore.global.error.ErrorCode;
import com.onetuks.goguma_bookstore.global.vo.file.CustomFile;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class CustomFileListToJsonConverter implements AttributeConverter<List<CustomFile>, String> {

  private final ObjectMapper objectMapper;

  public CustomFileListToJsonConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(List<CustomFile> customFiles) {
    try {
      return objectMapper.writeValueAsString(customFiles.stream().map(CustomFile::getUri).toList());
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(ErrorCode.JSON_CONVERT_ERROR.getMessage(), e);
    }
  }

  @Override
  public List<CustomFile> convertToEntityAttribute(String s) {
    try {
      return objectMapper.readValue(s, new TypeReference<List<String>>() {}).stream()
          .map(CustomFile::new)
          .toList();
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(ErrorCode.JSON_CONVERT_ERROR.getMessage(), e);
    }
  }
}

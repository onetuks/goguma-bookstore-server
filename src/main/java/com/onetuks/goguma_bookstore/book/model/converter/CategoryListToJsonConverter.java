package com.onetuks.goguma_bookstore.book.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetuks.goguma_bookstore.book.vo.Category;
import com.onetuks.goguma_bookstore.global.error.ErrorCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class CategoryListToJsonConverter implements AttributeConverter<List<Category>, String> {

  private final ObjectMapper objectMapper;

  public CategoryListToJsonConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public String convertToDatabaseColumn(List<Category> categories) {
    try {
      return objectMapper.writeValueAsString(categories);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(ErrorCode.JSON_CONVERT_ERROR.getMessage(), e);
    }
  }

  @Override
  public List<Category> convertToEntityAttribute(String s) {
    try {
      return objectMapper.readValue(
          s, objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(ErrorCode.JSON_CONVERT_ERROR.getMessage(), e);
    }
  }
}

package com.onetuks.goguma_bookstore.global.error;

import static com.onetuks.goguma_bookstore.global.error.ErrorCode.DUPLICATED_COLUMN_VALUE;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.FILE_NOT_FOUND;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.ILLEGAL_ARGUMENT_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.ILLEGAL_STATE_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.INVALID_INPUT_VALUE_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.INVALID_METHOD_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.NOT_FOUND_ENTITY;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.ONLY_FOR_ADMIN_METHOD;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.REQUEST_BODY_MISSING_ERROR;
import static com.onetuks.goguma_bookstore.global.error.ErrorCode.REQUEST_PARAM_MISSING_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.UncheckedIOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class GlobalExceptionRestHandler {

  /** 유니크 컬럼에 중복된 값을 넣으려고 하는 경우 - members, authors nickname col */
  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(DUPLICATED_COLUMN_VALUE, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** 관리자 혹은 작가만 접근 가능한 API에 다른 권한으로 접근한 경우 */
  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(ONLY_FOR_ADMIN_METHOD, e.getMessage());

    return ResponseEntity.status(UNAUTHORIZED).body(response);
  }

  /** S3 버킷에 찾고자 하는 파일이 없는 경우 */
  @ExceptionHandler(NoSuchKeyException.class)
  protected ResponseEntity<ErrorResponse> handleNoSuchKeyException(NoSuchKeyException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(FILE_NOT_FOUND, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** S3 버킷의 파일을 읽을때 발생하는 IOException Unchecking */
  @ExceptionHandler(UncheckedIOException.class)
  protected ResponseEntity<ErrorResponse> handleUncheckedIOException(UncheckedIOException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(FILE_NOT_FOUND, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** 객체 혹은 파라미터의 데이터 값이 유효하지 않은 경우 */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(INVALID_METHOD_ERROR, e.getBindingResult());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** 클라이언트에서 request의 '파라미터로' 데이터가 넘어오지 않았을 경우 */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
      MissingServletRequestParameterException ex) {
    logging(ex);

    final ErrorResponse response = ErrorResponse.of(REQUEST_PARAM_MISSING_ERROR, ex.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /**
   * enum type 일치하지 않아 binding 못할 경우<br>
   * 주로 @RequestParam enum으로 binding 못했을 경우 발생
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE_ERROR, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** com.fasterxml.jackson.core 내에 Exception 발생하는 경우 */
  @ExceptionHandler(JsonProcessingException.class)
  protected ResponseEntity<ErrorResponse> handleJsonProcessingException(
      JsonProcessingException ex) {
    logging(ex);

    final ErrorResponse response = ErrorResponse.of(REQUEST_BODY_MISSING_ERROR, ex.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /**
   * @ModelAttribute 으로 binding error 발생할 경우
   */
  @ExceptionHandler(BindException.class)
  protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
    logging(e);

    final ErrorResponse response =
        ErrorResponse.of(INVALID_INPUT_VALUE_ERROR, e.getBindingResult());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** ContentType이 적절하지 않은 경우 */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handleHttpMediaTypeException(
      HttpMediaTypeNotSupportedException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(INVALID_INPUT_VALUE_ERROR, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** 자원이 존재하지 않는 경우 */
  @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleNotFoundException(
      ChangeSetPersister.NotFoundException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(NOT_FOUND_ENTITY, e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleAllException(IllegalArgumentException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(ILLEGAL_ARGUMENT_ERROR, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleAllException(IllegalStateException e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(ILLEGAL_STATE_ERROR, e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(response);
  }

  /** 서버에 정의되지 않은 모든 예외 */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllException(Exception e) {
    logging(e);

    final ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR, e.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private void logging(Exception e) {
    log.warn("Handle {} : {}", e.getClass(), e.getMessage());
  }
}

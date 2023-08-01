package com.vti.configuration.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Log4j2
@ControllerAdvice //Thêm annotation này cho handler
public class ErrorHandler extends ResponseEntityExceptionHandler
implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(
                code, args, "Internal server error.",
                LocaleContextHolder.getLocale()
        );
    }
    //thêm get message để dịch các message của các lỗi


    @ExceptionHandler(Exception.class) //Bắt các lỗi chung mà chưa bắt được ở bên dưới
    public ResponseEntity<Object> handleException(Exception exception) {
        String message = getMessage("Exception.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(1, message, detailMessage);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override // Lỗi truy cập sai đường dẫn trên server, 404 not found
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(
                "NoHandlerFoundException.message",
                exception.getHttpMethod(), exception.getRequestURL()
        );
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(2, message, detailMessage);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override // Lỗi không hỗ trợ phương thức
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(
                "HttpRequestMethodNotSupportedException.message",
                exception.getMethod()
        );
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(3, message, detailMessage);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override // Lỗi không hỗ trợ định dạng này
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(
                "HttpMediaTypeNotSupportedException.message",
                exception.getContentType()
        );
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(4, message, detailMessage);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override // lỗi không thỏa mãn điều kiện lọc dữ liệu
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        String message = getMessage("MethodArgumentNotValidException.message");
        String detailMessage = exception.getLocalizedMessage(); //lấy chi tiết lỗi trong exception
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getFieldErrors()) {
            String key = error.getField();
            String value = error.getDefaultMessage();
            errors.put(key, value);
        }
        ErrorResponse response = new ErrorResponse(5, message, detailMessage, errors);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, headers, status);
        // respone: các thông tin lỗi cần in ra
        // headers: lấy ở trên
        // status: trạng thái lấy ở trên
    }

    @ExceptionHandler(ConstraintViolationException.class) //lỗi xung đột ràng buộc
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        String message = getMessage("ConstraintViolationException.message");
        String detailMessage = exception.getLocalizedMessage();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String key = violation.getPropertyPath().toString();
            String value = violation.getMessage();
            errors.put(key, value);
        }
        ErrorResponse response = new ErrorResponse(6, message, detailMessage, errors);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override //lỗi không truyền tham số yêu cầu
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = getMessage(
                "MissingServletRequestParameterException.message",
                exception.getParameterName(),
                exception.getParameterType()
        );
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(7, message, detailMessage);
        log.error(detailMessage, exception);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override //lỗi truyền vào dữ liệu nhưng dữ liệu đó lại không hợp lệ
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException exception,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (exception instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException ex = (MethodArgumentTypeMismatchException) exception;
            Class<?> requiredType = ex.getRequiredType();
            String message = getMessage(
                    "MethodArgumentTypeMismatchException.message",
                    ex.getName(),
                    requiredType == null ? "null" : requiredType.getName()
            );
            String detailMessage = exception.getLocalizedMessage();
            ErrorResponse response = new ErrorResponse(8, message, detailMessage);
            log.error(detailMessage, exception);
            return new ResponseEntity<>(response, headers, status);
        }
        return super.handleTypeMismatch(exception, headers, status, request);
    }

    @Override //lỗi về xác thực, gọi khi đăng nhập thất bại
    public void commence(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        String message = getMessage("AuthenticationException.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse errorResponse = new ErrorResponse(9, message, detailMessage);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String output = writer.writeValueAsString(errorResponse);
        log.error(detailMessage, exception);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(output);
    }

    @Override //lỗi phân quyền, không có quyền truy cập
    public void handle(
            HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException exception) throws IOException {
        String message = getMessage("AccessDeniedException.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse errorResponse = new ErrorResponse(10, message, detailMessage);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        String output = writer.writeValueAsString(errorResponse);
        log.error(detailMessage, exception);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(output);
    }
}

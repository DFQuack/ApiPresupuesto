package sv.edu.udb.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import sv.edu.udb.config.web.ApiError;
import sv.edu.udb.config.web.ApiErrorWrapper;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    // Para @Valid
    @Override
    protected final ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                        final HttpHeaders headers,
                                                                        final HttpStatusCode status,
                                                                        final WebRequest request) {
        final ApiErrorWrapper apiErrorWrapper = processErrors(ex.getBindingResult().getAllErrors());
        return handleExceptionInternal(ex, apiErrorWrapper, headers, HttpStatus.BAD_REQUEST, request);
    }

    // Errores 4xx
    @ExceptionHandler({HttpClientErrorException.class})
    protected ResponseEntity<Object> handleHttpClientError(final HttpClientErrorException ex, final WebRequest request) {
        return createResponseEntity(ex, new HttpHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected final ResponseEntity<Object> handleValidation(final ValidationException ex, final WebRequest request) {
        final ApiErrorWrapper apiErrors = message(HttpStatus.BAD_REQUEST, ex);
        return handleExceptionInternal(ex, apiErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Error 403
    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDenied(final AccessDeniedException ex, final WebRequest request) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // Error 404
    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // Error 409
    @ExceptionHandler({DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccess(final DataAccessException ex, final WebRequest request) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // Error 415
    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleInvalidMimeType(final IllegalArgumentException ex, final WebRequest request) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Error 500
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handle500Exception(final Exception ex, final WebRequest request) {
        return handleExceptionInternal(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Utilidades
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if (Objects.isNull(body)) {
            final ApiErrorWrapper apiErrors = message((HttpStatus) status, ex);
            return new ResponseEntity<>(apiErrors, headers, status);
        }
        return new ResponseEntity<>(body, headers, status);
    }

    protected ApiErrorWrapper message(final HttpStatus httpStatus, final Exception ex) {
        return message(buildApiError(httpStatus, ex));
    }

    protected ApiErrorWrapper message(final ApiError error) {
        final ApiErrorWrapper errors = new ApiErrorWrapper();
        errors.addApiError(error);
        return errors;
    }

    protected ApiErrorWrapper processErrors(final List<ObjectError> errors) {
        final ApiErrorWrapper dto = new ApiErrorWrapper();
        errors.forEach(objError -> {
            if (isFieldError(objError)) {
                FieldError fieldError = (FieldError) objError;
                final String localizedErrorMessage = fieldError.getDefaultMessage();
                dto.addFieldError(fieldError.getClass().getSimpleName(), "Invalid Attribute", fieldError.getField(), localizedErrorMessage);
            } else {
                final String localizedErrorMessage = objError.getDefaultMessage();
                dto.addFieldError(objError.getClass().getSimpleName(), "Invalid Attribute", "base", localizedErrorMessage);
            }
        });
        return dto;
    }

    private ApiError buildApiError(final HttpStatus httpStatus, final Exception ex) {
        final String typeException = ex.getClass().getSimpleName();
        final String description = StringUtils.defaultIfBlank(ex.getMessage(), ex.getClass().getSimpleName());

        String source;
        if (isMissingRequestParameterException(ex)) {
            MissingServletRequestParameterException missingParamEx = (MissingServletRequestParameterException) ex;
            source = missingParamEx.getParameterName();
        } else if (isMissingPathVariableException(ex)) {
            MissingPathVariableException missingPathEx = (MissingPathVariableException) ex;
            source = missingPathEx.getVariableName();
        } else {
            source = "base";
        }
        return ApiError.builder()
                .status(httpStatus.value())
                .type(typeException)
                .title(httpStatus.getReasonPhrase())
                .description(description)
                .source(source)
                .build();
    }

    private boolean isMissingPathVariableException(final Exception ex) {
        return ex instanceof MissingPathVariableException;
    }
    private boolean isMissingRequestParameterException(final Exception ex) {
        return ex instanceof MissingServletRequestParameterException;
    }
    private boolean isFieldError(ObjectError objError) {
        return objError instanceof FieldError;
    }
}
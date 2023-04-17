package com.example.demologging.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * The Custom request body advice adapter type.
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

    /**
     * The Logging service.
     */
    private final LoggingService loggingService;

    /**
     * The Http servlet request.
     */
    private final HttpServletRequest httpServletRequest;

    /**
     * Supports boolean.
     *
     * @param methodParameter the method parameter
     * @param type            the type
     * @param aClass          the a class
     * @return the boolean
     */
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type type,
                            @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * After body read object.
     *
     * @param body          the body
     * @param inputMessage  the input message
     * @param parameter     the parameter
     * @param targetType    the target type
     * @param converterType the converter type
     * @return the object
     */
    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage,
                                @NonNull MethodParameter parameter, @NonNull Type targetType,
                                @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        
        String str = "";
        try {
            str = new ObjectMapper().writeValueAsString(body);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        final String bodyStr = str;

        Optional.of(body).ifPresentOrElse(value ->
                        loggingService.logRequest(httpServletRequest, bodyStr),
                () -> loggingService.logRequest(httpServletRequest, body));

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

}

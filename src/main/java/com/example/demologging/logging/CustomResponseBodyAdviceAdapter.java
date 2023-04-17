package com.example.demologging.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

/**
 * The Custom response body advice adapter type.
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    /**
     * The Logging service.
     */
    private final LoggingService loggingService;

    /**
     * Supports boolean.
     *
     * @param returnType    the return type
     * @param converterType the converter type
     * @return the boolean
     */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        return true;
    }

    /**
     * Before body write object.
     *
     * @param body                  the body
     * @param returnType            the return type
     * @param selectedContentType   the selected content type
     * @param selectedConverterType the selected converter type
     * @param request               the request
     * @param response              the response
     * @return the object
     */
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType, @NonNull Class selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
            String str = "";
            try {
                str = new ObjectMapper().writeValueAsString(body);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            final String bodyStr = str;
            Optional.ofNullable(body).ifPresentOrElse(value ->
                            loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
                                    ((ServletServerHttpResponse) response).getServletResponse(), bodyStr),
                    () -> loggingService.logResponse(((ServletServerHttpRequest) request).getServletRequest(),
                            ((ServletServerHttpResponse) response).getServletResponse(), body));
        }
        return body;
    }
}

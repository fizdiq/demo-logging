package com.example.demologging.logging;

import com.example.demologging.constant.RestConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * The Logging service type.
 */
@Slf4j
@Component
public class LoggingServiceImpl implements LoggingService {
    /**
     * Log request.
     *
     * @param httpServletRequest the http servlet request
     * @param body               the body
     */
    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameters = buildParametersMap(httpServletRequest);

        MDC.clear();
        Map<String, String> headers = buildHeadersMap(httpServletRequest);
        if(generateIdHeader(httpServletRequest, RestConstants.HEADER_NAME.REQUEST_ID))
            headers.put(RestConstants.HEADER_NAME.REQUEST_ID, MDC.get(RestConstants.HEADER_NAME.REQUEST_ID));
        if(generateIdHeader(httpServletRequest,RestConstants.HEADER_NAME.CORRELATION_ID))
            headers.put(RestConstants.HEADER_NAME.CORRELATION_ID, MDC.get(RestConstants.HEADER_NAME.CORRELATION_ID));

//        httpServletRequest;
        stringBuilder.append("REQUEST ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("body=[").append(body).append("]");
        }

        log.info(stringBuilder.toString());
    }

    /**
     * Log response.
     *
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param body                the body
     */
    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        Map<String, String> headers = buildHeadersMap(httpServletResponse);
        if(!StringUtils.hasLength(httpServletResponse.getHeader(RestConstants.HEADER_NAME.REQUEST_ID)))
            headers.put(RestConstants.HEADER_NAME.REQUEST_ID, MDC.get(RestConstants.HEADER_NAME.REQUEST_ID));
        if(!StringUtils.hasLength(httpServletResponse.getHeader(RestConstants.HEADER_NAME.CORRELATION_ID)))
            headers.put(RestConstants.HEADER_NAME.CORRELATION_ID, MDC.get(RestConstants.HEADER_NAME.CORRELATION_ID));

        stringBuilder.append("RESPONSE ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
        stringBuilder.append("responseBody=[").append(body).append("] ");

        log.info(stringBuilder.toString());
    }

    /**
     * Build parameters map map.
     *
     * @param httpServletRequest the http servlet request
     * @return the map
     */
    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    /**
     * Build headers map map.
     *
     * @param request the request
     * @return the map
     */
    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
            if (key.equalsIgnoreCase("authorization")) {
                map.remove("authorization");
            }
        }

        return map;
    }

    /**
     * Generate id header boolean.
     *
     * @param request the request
     * @param key     the key
     * @return the boolean
     */
    private boolean generateIdHeader(HttpServletRequest request, String key) {
        boolean result = false;
        String id = request.getHeader(key);
        if (!StringUtils.hasLength(id)) {
            id = UUID.randomUUID().toString();
            result = true;
        }
        MDC.put(key, id);
        return result;
    }

    /**
     * Build headers map map.
     *
     * @param response the response
     * @return the map
     */
    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}

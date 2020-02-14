package org.nipu.pc.gateway.fallback;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductFallbackProvider implements FallbackProvider {

    @Value("${zuul.fallback.message}")
    private String fallbackMessage;

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        log.warn("can't process request to: {}", route, cause);

        if (cause instanceof HystrixTimeoutException) {
            return new GatewayClientHttpResponse(HttpStatus.GATEWAY_TIMEOUT, fallbackMessage);
        } else {
            return new GatewayClientHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, fallbackMessage);
        }
    }
}

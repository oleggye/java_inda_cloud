package org.nipu.pc.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(
        stubs = "classpath:integration-test/mappings",
        files = "classpath:integration-test")
@TestPropertySource(locations = "classpath:integration-test/test.properties")
class ApiGatewayIntegrationTest {

    @Value("${zuul.fallback.message}")
    private String fallbackMessage;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetCatalog() {
        ResponseEntity<String> entity = restTemplate.getForEntity("/catalog", String.class);

        assertAll(() -> {
            assertEquals(HttpStatus.OK, entity.getStatusCode());
            assertNotNull(entity.getBody());
            assertFalse(entity.getBody().contains(fallbackMessage));
        });
        verify(1, getRequestedFor(urlEqualTo("/catalog")));
    }

    @Test
    public void shouldGetDelayedResponseWithGwTimeoutMessageFromHystrix() {
        ResponseEntity<String> entity = restTemplate.getForEntity("/catalog?delayed=true", String.class);

        assertAll(() -> {
            assertEquals(HttpStatus.GATEWAY_TIMEOUT, entity.getStatusCode());
            assertEquals(fallbackMessage, entity.getBody());
        });
        verify(1, getRequestedFor(urlEqualTo("/catalog?delayed=true")));
    }

    @Test
    public void shouldGetNotFoundResponse() {
        ResponseEntity<String> entity = restTemplate.getForEntity("/bad_path", String.class);

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
    }

    @Test
    public void shouldPostCatalog() {
        String bodyMessage = "{\n\t\"name\": \"Wi-fi\",\n\t\"price\": 300\n}";
        String expectedResponseBody =
                "{\n" +
                        "  \"name\": \"Wi-fi\",\n" +
                        "  \"price\": 300,\n" +
                        "  \"_links\": {\n" +
                        "    \"self\": {\n" +
                        "      \"href\": \"http://172.19.0.6:8081/catalog/5e470e6971885a4373ba1516\"\n" +
                        "    },\n" +
                        "    \"productSpecification\": {\n" +
                        "      \"href\": \"http://172.19.0.6:8081/catalog/5e470e6971885a4373ba1516\"\n" +
                        "    }\n" +
                        "  }\n" +
                 "}";
        ResponseEntity<String> entity = restTemplate.postForEntity("/catalog", bodyMessage, String.class);

        assertAll(() -> {
            assertEquals(HttpStatus.OK, entity.getStatusCode());
            assertEquals(expectedResponseBody, entity.getBody());
        });
        verify(1, postRequestedFor(urlEqualTo("/catalog")));
    }

    @Test
    public void shouldPutCatalog() {
        String expectedBody =
                "{\n" +
                        "  \"id\": \"anyId\",\n" +
                        "  \"specificationId\": \"specificationId\",\n" +
                        "  \"quantity\": 1\n" +
                 "}";

        ResponseEntity<String> entity = restTemplate.exchange(
                "/catalog/anyId/order", HttpMethod.PUT, null, String.class);

        assertAll(() -> {
            assertEquals(HttpStatus.OK, entity.getStatusCode());
            assertEquals(expectedBody, entity.getBody());
        });
        verify(1, putRequestedFor(urlEqualTo("/catalog/anyId/order")));
    }
}
package com.example;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import io.micronaut.context.ApplicationContext;
import io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction;
import io.micronaut.http.MediaType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class ExampleServerlessControllerTest {

    private static final String TEXT = "français";

    @Inject
    private ApplicationContext applicationContext;

    private ApiGatewayProxyRequestEventFunction requestHandler;

    @BeforeEach
    void init() {
        requestHandler = new ApiGatewayProxyRequestEventFunction(this.applicationContext);
    }

    @Test
    void testStringServerlessControllerEncodesCorrectCharacters() {
        testServerlessControllerEncodesCorrectCharacters("/string");
    }

    @Test
    void testWritableServerlessControllerEncodesCorrectCharacters() {
        testServerlessControllerEncodesCorrectCharacters("/writable");
    }

    private void testServerlessControllerEncodesCorrectCharacters(String path) {
        var requestEvent = new APIGatewayProxyRequestEvent();
        requestEvent.setHttpMethod("GET");
        requestEvent.setPath(path);

        var responseEvent = requestHandler.handleRequest(requestEvent, null);

        var mediaType = MediaType.of(responseEvent.getHeaders().get(io.micronaut.http.HttpHeaders.CONTENT_TYPE));
        assertTrue(responseEvent.getIsBase64Encoded());
        assertTrue(mediaType.getCharset().isPresent());
        assertEquals(StandardCharsets.ISO_8859_1, mediaType.getCharset().get());
        assertEquals(TEXT, decodeBase64(responseEvent.getBody(), mediaType.getCharset().get()));
    }

    private static String decodeBase64(String body, Charset charset) {
        return new String(Base64.getMimeDecoder().decode(body.getBytes()), charset);
    }
}

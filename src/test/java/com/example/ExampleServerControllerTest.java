package com.example;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class ExampleServerControllerTest {

    private static final String TEXT = "français";

    @Inject
    @Client("/")
    private HttpClient client;

    @Test
    void testStringServerControllerEncodesCorrectCharacters() {
        testServerControllerEncodesCorrectCharacters("/string");
    }

    @Test
    void testWritableServerControllerEncodesCorrectCharacters() {
        testServerControllerEncodesCorrectCharacters("/writable");
    }

    private void testServerControllerEncodesCorrectCharacters(String path) {
        var response = client.toBlocking().exchange(path);

        var mediaType = MediaType.of(response.getHeaders().get(io.micronaut.http.HttpHeaders.CONTENT_TYPE));
        assertTrue(mediaType.getCharset().isPresent());
        assertEquals(StandardCharsets.ISO_8859_1, mediaType.getCharset().get());
        assertTrue(response.getBody(String.class).isPresent());
        
        assertEquals(TEXT, response.getBody(String.class).get());
    }
}

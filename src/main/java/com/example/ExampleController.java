package com.example;


import java.nio.charset.StandardCharsets;
import io.micronaut.core.io.Writable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller
public class ExampleController {

    private static final String TEXT = "français";

    @Get("/writable")
    public HttpResponse<Writable> writable() {
        return HttpResponse.<Writable>ok(writer -> writer.write(TEXT))
            .contentType(MediaType.TEXT_PLAIN_TYPE)
            .characterEncoding(StandardCharsets.ISO_8859_1);
    }

    @Get("/string")
    public HttpResponse<String> string() {
        return HttpResponse.ok(TEXT)
            .contentType(MediaType.TEXT_PLAIN_TYPE)
            .characterEncoding(StandardCharsets.ISO_8859_1);
    }
}

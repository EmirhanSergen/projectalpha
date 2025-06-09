package com.projectalpha.util;

import com.projectalpha.config.thirdparty.SupabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class SupabaseHttpHelper {
    private final SupabaseConfig config;
    private final HttpClient httpClient;

    @Autowired
    public SupabaseHttpHelper(SupabaseConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newHttpClient();
    }

    private HttpRequest.Builder baseBuilder(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", config.getSupabaseApiKey())
                .header("Authorization", "Bearer " + config.getSupabaseSecretKey())
                .header("Content-Type", "application/json");
    }

    public String get(String url) throws Exception {
        HttpRequest request = baseBuilder(url).GET().build();
        HttpResponse<String> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400) {
            throw new RuntimeException("Request failed: " + resp.body());
        }
        return resp.body();
    }

    public String post(String url, String body, String prefer) throws Exception {
        HttpRequest.Builder builder = baseBuilder(url)
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (prefer != null) {
           builder.header("Prefer", prefer);
        }
        HttpResponse<String> resp = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400 && resp.statusCode() != 201) {
            throw new RuntimeException("Request failed: " + resp.body());
        }
        return resp.body();
    }

    public String patch(String url, String body, String prefer) throws Exception {
        HttpRequest.Builder builder = baseBuilder(url)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body));
        if (prefer != null) {
            builder.header("Prefer", prefer);
        }
        HttpResponse<String> resp = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() >= 400 && resp.statusCode() != 201) {
            throw new RuntimeException("Request failed: " + resp.body());
        }
        return resp.body();
    }

    public String delete(String url, String prefer) throws Exception {
        HttpRequest.Builder builder = baseBuilder(url).DELETE();
        if (prefer != null) {
            builder.header("Prefer", prefer);
        }
       HttpResponse<String> resp = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
       if (resp.statusCode() >= 400 && resp.statusCode() != 204) {
            throw new RuntimeException("Request failed: " + resp.body());
        }
        return resp.body();
    }
}

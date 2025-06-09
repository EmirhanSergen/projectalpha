package com.projectalpha.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for interacting with Supabase via HTTP.
 * Provides common request construction and JSON serialization support.
 */
@Component
public class SupabaseHttpHelper {

    private final SupabaseConfig supabaseConfig;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    @Autowired
    public SupabaseHttpHelper(SupabaseConfig supabaseConfig) {
        this.supabaseConfig = supabaseConfig;
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    private HttpRequest.Builder baseRequest(String path) {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", supabaseConfig.getSupabaseApiKey())
                .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                .header("Content-Type", "application/json");
    }

    /**
     * Fetch a list of objects from Supabase.
     *
     * @param path relative API path
     * @param type class of the list items
     * @return list of parsed objects
     */
    public <T> List<T> fetchList(String path, Class<T> type) {
        try {
            HttpRequest request = baseRequest(path).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new RuntimeException("Request failed [" + path + "]: " + response.body());
            }
            JsonNode root = mapper.readTree(response.body());
            List<T> list = new ArrayList<>();
            for (JsonNode node : root) {
                list.add(mapper.treeToValue(node, type));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    private <T> T sendWithBody(String method, String path, Object body, Class<T> type) {
        try {
            HttpRequest.BodyPublisher publisher = body == null ?
                    HttpRequest.BodyPublishers.noBody() :
                    HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body));

            HttpRequest request = baseRequest(path)
                    .header("Prefer", "return=representation")
                    .method(method, publisher)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new RuntimeException(method + " request failed [" + path + "]: " + response.body());
            }
            if (type == Void.class) {
                return null;
            }
            return mapper.readValue(response.body(), type);
        } catch (Exception e) {
            throw new RuntimeException("Error during " + method + " " + path, e);
        }
    }

    public <T> T post(String path, Object body, Class<T> type) {
        return sendWithBody("POST", path, body, type);
    }

    public <T> T put(String path, Object body, Class<T> type) {
        return sendWithBody("PUT", path, body, type);
    }

    public <T> T patch(String path, Object body, Class<T> type) {
        return sendWithBody("PATCH", path, body, type);
    }

    public void delete(String path) {
        sendWithBody("DELETE", path, null, Void.class);
    }
}

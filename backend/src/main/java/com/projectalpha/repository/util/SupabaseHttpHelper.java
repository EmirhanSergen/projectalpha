package com.projectalpha.repository.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectalpha.config.thirdparty.SupabaseConfig;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility component for making HTTP requests to Supabase and mapping the
 * JSON response to DTO classes.
 */
@Component
public class SupabaseHttpHelper {
    private final SupabaseConfig supabaseConfig;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SupabaseHttpHelper(SupabaseConfig supabaseConfig) {
        this.supabaseConfig = supabaseConfig;
    }

    /**
     * Fetches a list of DTOs from the given path under the Supabase REST API.
     *
     * @param path  the path relative to <code>/rest/v1/</code>
     * @param clazz the DTO class
     * @return a list of mapped DTO objects
     */
    public <T> List<T> fetchList(String path, Class<T> clazz) {
        try {
            String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/" + path;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", supabaseConfig.getSupabaseApiKey())
                    .header("Authorization", "Bearer " + supabaseConfig.getSupabaseSecretKey())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new RuntimeException("Request failed [" + path + "]: " + response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());
            List<T> result = new ArrayList<>();
            for (JsonNode node : root) {
                result.add(objectMapper.treeToValue(node, clazz));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching " + path, e);
        }
    }

    /**
     * Fetches a single DTO from the given path under the Supabase REST API.
     */
    public <T> T fetchSingle(String path, Class<T> clazz) {
        List<T> list = fetchList(path, clazz);
        return list.isEmpty() ? null : list.get(0);
    }
}

package com.projectalpha.config.thirdparty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupabaseConfig {

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_API_KEY}")
    private String supabaseApiKey;

    @Value("${SUPABASE_SECRET_KEY}")
    private String supabaseSecretKey;

    public String getSupabaseUrl() {
        return supabaseUrl;
    }

    public String getSupabaseSecretKey(){return supabaseSecretKey;}

    public String getSupabaseApiKey() {
        return supabaseApiKey;
    }
}

package com.aptitudeapp.backend.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", "dqlwe5rro");
        config.put("api_key", "622549753596479");
        config.put("api_secret", "1FjNuFALNaRAVYoBbtiRKuuVy2E");

        return new Cloudinary(config);
    }
}

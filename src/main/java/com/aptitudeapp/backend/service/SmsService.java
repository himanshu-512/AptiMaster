package com.aptitudeapp.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${ninzasms.api.key}")
    private String apiKey;

    private final String apiUrl = "https://ninzasms.in.net/auth/send_sms";

    public void sendOtpSms(String phone, String otp) {

        try {
            System.out.println("📩 Sending OTP via NinzaSMS...");
            System.out.println("Original Phone: " + phone);
            System.out.println("OTP: " + otp);

//            // ✅ Convert to EXACT 10-digit format (same as Node project)
//            phone = phone.replace("+91", "")
//                    .replace("91", "")
//                    .replace("+", "")
//                    .trim();

            // If still longer than 10 digits, keep last 10
            if (phone.length() > 10) {
                phone = phone.substring(phone.length() - 10);
            }

            System.out.println("Final Phone Sent: " + phone);

            // ✅ Build JSON manually (exactly like Node)
            String jsonBody = "{"
                    + "\"sender_id\":\"15723\","
                    + "\"variables_values\":\"" + otp + "\","
                    + "\"numbers\":\"" + phone + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("authorization", apiKey.trim())
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP");
        }
    }
}
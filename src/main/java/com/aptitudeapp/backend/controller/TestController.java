package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PhoneUtil phoneUtil;

    @GetMapping("/encrypt-test")
    public String testEncrypt() throws Exception {

        String phone = "9876543210";

        String hash = phoneUtil.hash(phone);
        String encrypted = phoneUtil.encrypt(phone);

        return "Hash: " + hash + "\nEncrypted: " + encrypted;
    }
}
package com.aptitudeapp.backend.service;

import com.aptitudeapp.backend.dto.AuthResponse;
import com.aptitudeapp.backend.model.Otp;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.OtpRepository;
import com.aptitudeapp.backend.security.JwtTokenProvider;
import com.aptitudeapp.backend.util.OtpGenerator;
import com.aptitudeapp.backend.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final AuthService authService;
    private final SmsService smsService;
    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;
    private final PhoneUtil phoneUtil;
    private final JwtTokenProvider jwtTokenProvider;

    // 🔹 SEND OTP
    public void sendOtp(String phone) {

        phone = phoneUtil.normalize(phone);

        String phoneHash = phoneUtil.hash(phone);
        String otp = otpGenerator.generateOtp();

        otpRepository.deleteByPhoneHash(phoneHash);

        Otp entity = new Otp();
        entity.setPhoneHash(phoneHash);
        entity.setOtp(otp);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

        smsService.sendOtpSms(phone, otp);
    }

    // 🔹 VERIFY OTP
    public AuthResponse verifyOtp(String phone, String inputOtp) {

        phone = phoneUtil.normalize(phone);
        System.out.println("PHONE FROM FRONTEND: " + phone);
        System.out.println("NORMALIZED PHONE: " + phoneUtil.normalize(phone));

        String phoneHash = phoneUtil.hash(phone);
        System.out.println("PHONE HASH: " + phoneHash);
        Otp otpEntity = otpRepository.findByPhoneHash(phoneHash)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP not found")
                );

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired");
        }

        if (!otpEntity.getOtp().equals(inputOtp)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        otpRepository.deleteByPhoneHash(phoneHash);

        User user = authService.createOrGetUser(phone);

        String token = jwtTokenProvider.generateToken(user.getId());

        return new AuthResponse(
                token,
                user.getId(),
                "Login successful",
                user.isProfileComplete(),
                user.isFirstLogin()
        );
    }
}

package com.aptitudeapp.backend.controller;

import com.aptitudeapp.backend.dto.ProfileResponse;
import com.aptitudeapp.backend.dto.ProfileUpdateRequest;
import com.aptitudeapp.backend.model.User;
import com.aptitudeapp.backend.repository.UserRepository;
import com.aptitudeapp.backend.service.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
//import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ProfileResponse getProfile() {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userService.getProfile(userId);
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(
            @RequestBody ProfileUpdateRequest request) {

        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userService.updateProfile(userId, request);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("avatar") MultipartFile file
    ) throws IOException {

        // upload image to cloudinary
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        String imageUrl = uploadResult.get("secure_url").toString();

        // get logged-in userId from JWT
        String userId = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // save avatar url
        user.setAvatar(imageUrl);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "message", "Avatar uploaded successfully",
                "avatar", imageUrl
        ));
    }
}
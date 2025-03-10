package com.xiang.multipurposeTracker.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.xiang.multipurposeTracker.DTO.JwtDTO;
import com.xiang.multipurposeTracker.entities.UserCredentials;
import com.xiang.multipurposeTracker.entities.UserDetails;
import com.xiang.multipurposeTracker.repository.UserCredentialsRepository;
import com.xiang.multipurposeTracker.repository.UserDetailsRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class UserCredentialService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Transactional
    public String registrationValidation(String username, String password, String name, String email, String phoneNumber) {
        String response = null;
        // Validate the user credentials -- Username
        UserCredentials userCredential = userCredentialsRepository.findByUsername(username);

        if (userCredential == null) {
            try {
                // generate Useruid
                String useruid = UUID.randomUUID().toString();

                while (userCredentialsRepository.existsByUserUID(useruid)) {
                    useruid = UUID.randomUUID().toString();
                }

                // Insert user into User_Details table
                UserDetails newUserDetails = new UserDetails();
                newUserDetails.setUserUID(useruid);
                newUserDetails.setEmail(email);
                newUserDetails.setName(name);
                newUserDetails.setPhone(phoneNumber);
                userDetailsRepository.save(newUserDetails);

                // Insert user into User_Credentials table
                UserCredentials newUserCredentials = new UserCredentials();
                newUserCredentials.setUsername(username);
                newUserCredentials.setPassword(password);
                newUserCredentials.setUserUID(useruid);
                userCredentialsRepository.save(newUserCredentials);

                response = "Registration successful. Please proceed to login.";

            } catch (Exception e) {
                e.printStackTrace();
                response = "Registration failed: " + e.getMessage();
            }
        } else {
            response = "Username taken.";
        }
        return response;
    }

    public JwtDTO loginValidation(String username, String password) {
        String response = null;
        String token = null;

        // Validate the login
        UserCredentials userCredential = userCredentialsRepository.findByUsernameAndPassword(username, password);
        if (userCredential != null && userCredential.getPassword().equals(password)) {
            try {
                token = generateJwtToken(userCredential);
                response = "Login Successful";

            } catch (Exception e) {
                response = "JWT generation error";
                throw new RuntimeException(e);
            }

        } else {
            response = "Invalid username or password";
        }

        return new JwtDTO(response, token);
    }

    private String generateJwtToken(UserCredentials userCredentials) throws NoSuchAlgorithmException {
        long expirationTime = 1000 * 60 * 60;
        long expirationTimestamp = System.currentTimeMillis() + expirationTime;
        ZonedDateTime expirationDateUtc = Instant.ofEpochMilli(expirationTimestamp).atZone(ZoneOffset.UTC);

        SecretKey key = generateSecretKey();

        return Jwts.builder()
                .setSubject(userCredentials.getUserUID())
                .setIssuedAt(Date.from(ZonedDateTime.now(ZoneOffset.UTC).toInstant()))
                .setExpiration(Date.from(expirationDateUtc.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hashedBtyes = sha256.digest(jwtSecret.getBytes());

        return new SecretKeySpec(hashedBtyes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String getUserName(String userUID) {
        // Retrieve UserCredentials by userUID
        UserCredentials userCredentials = userCredentialsRepository.findByUserUID(userUID);

        // Check if userCredentials is null
        if (userCredentials != null) {
            System.out.println(userCredentials.getUsername());
            return userCredentials.getUsername();
        } else {
            // You can return a default value or throw an exception as needed
            throw new RuntimeException("User credentials not found for userUID: " + userUID);
        }
    }

    public void updatePassword(String userUID, String newPassword) throws Exception {
        // Find the user by userUID
        UserCredentials user = userCredentialsRepository.findByUserUID(userUID);

        if (user == null) {
            throw new Exception("User not found");
        }

        // Update the password with the already encrypted password
        user.setPassword(newPassword);

        // Save the updated user entity
        userCredentialsRepository.save(user);
    }
}

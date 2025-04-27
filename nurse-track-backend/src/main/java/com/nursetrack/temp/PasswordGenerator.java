package com.nursetrack.temp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=== CONTRASEÑAS CODIFICADAS ===");
        System.out.println("admin1: " + encoder.encode("admin1234"));
        System.out.println("super1: " + encoder.encode("super1234"));
        System.out.println("nurse1: " + encoder.encode("nurse1234"));
    }
}
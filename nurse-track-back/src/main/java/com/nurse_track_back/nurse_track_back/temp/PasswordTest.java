package com.nurse_track_back.nurse_track_back.temp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
        public static void main(String[] args) {
                BCryptPasswordEncoder encoderDefault = new BCryptPasswordEncoder(10);

                BCryptPasswordEncoder encoderCost12 = new BCryptPasswordEncoder(12);

                System.out.println("--- Generated Bcrypt Hashes ---");
                System.out.println("-------------------------------");

                // Admin
                String adminUsername = "admin1";
                String adminPassword = adminUsername + "1234"; // admin11234
                String adminHashedPassword = encoderDefault.encode(adminPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", adminUsername, adminPassword,
                                adminHashedPassword);

                // Supervisors (Active)
                String super1Username = "super1";
                String super1Password = super1Username + "1234"; // super11234
                String super1HashedPassword = encoderDefault.encode(super1Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", super1Username, super1Password,
                                super1HashedPassword);

                String superAnaUsername = "super_ana";
                String superAnaPassword = superAnaUsername + "1234"; // super_ana1234
                // Use the encoder with cost factor 12 if your backend uses it for this specific
                // user/role
                String superAnaHashedPassword = encoderCost12.encode(superAnaPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", superAnaUsername, superAnaPassword,
                                superAnaHashedPassword);

                String superDavidUsername = "super_david";
                String superDavidPassword = superDavidUsername + "1234"; // super_david1234
                String superDavidHashedPassword = encoderDefault.encode(superDavidPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", superDavidUsername, superDavidPassword,
                                superDavidHashedPassword);

                // Supervisor (Inactive)
                String superCarlosUsername = "super_carlos";
                String superCarlosPassword = superCarlosUsername + "1234"; // super_carlos1234
                String superCarlosHashedPassword = encoderDefault.encode(superCarlosPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", superCarlosUsername, superCarlosPassword,
                                superCarlosHashedPassword);

                // Nurses (Active and assigned)
                String nurse1Username = "nurse1";
                String nurse1Password = nurse1Username + "1234"; // nurse11234
                String nurse1HashedPassword = encoderDefault.encode(nurse1Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse1Username, nurse1Password,
                                nurse1HashedPassword);

                String nurse2Username = "nurse2";
                String nurse2Password = nurse2Username + "1234"; // nurse21234
                String nurse2HashedPassword = encoderDefault.encode(nurse2Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse2Username, nurse2Password,
                                nurse2HashedPassword);

                String nurse3Username = "nurse3";
                String nurse3Password = nurse3Username + "1234"; // nurse31234
                String nurse3HashedPassword = encoderDefault.encode(nurse3Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse3Username, nurse3Password,
                                nurse3HashedPassword);

                String nurse5Username = "nurse5";
                String nurse5Password = nurse5Username + "1234"; // nurse51234
                String nurse5HashedPassword = encoderDefault.encode(nurse5Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse5Username, nurse5Password,
                                nurse5HashedPassword);

                String nurse6Username = "nurse6";
                String nurse6Password = nurse6Username + "1234"; // nurse61234
                String nurse6HashedPassword = encoderDefault.encode(nurse6Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse6Username, nurse6Password,
                                nurse6HashedPassword);

                String nurse7Username = "nurse7";
                String nurse7Password = nurse7Username + "1234"; // nurse71234
                String nurse7HashedPassword = encoderDefault.encode(nurse7Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse7Username, nurse7Password,
                                nurse7HashedPassword);

                String nurse8Username = "nurse8";
                String nurse8Password = nurse8Username + "1234"; // nurse81234
                String nurse8HashedPassword = encoderDefault.encode(nurse8Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse8Username, nurse8Password,
                                nurse8HashedPassword);

                String nurse9Username = "nurse9";
                String nurse9Password = nurse9Username + "1234"; // nurse91234
                String nurse9HashedPassword = encoderDefault.encode(nurse9Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse9Username, nurse9Password,
                                nurse9HashedPassword);

                // Nurses (Inactive)
                String nurse4Username = "nurse4";
                String nurse4Password = nurse4Username + "1234"; // nurse41234
                String nurse4HashedPassword = encoderDefault.encode(nurse4Password);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurse4Username, nurse4Password,
                                nurse4HashedPassword);

                String nurseClarkUsername = "nurse_clark";
                String nurseClarkPassword = nurseClarkUsername + "1234"; // nurse_clark1234
                String nurseClarkHashedPassword = encoderDefault.encode(nurseClarkPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurseClarkUsername, nurseClarkPassword,
                                nurseClarkHashedPassword);

                // Nurses (Active, pero sin asignar inicialmente a ningún departamento)
                String nurseGreenUsername = "nurse_green";
                String nurseGreenPassword = nurseGreenUsername + "1234"; // nurse_green1234
                String nurseGreenHashedPassword = encoderDefault.encode(nurseGreenPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurseGreenUsername, nurseGreenPassword,
                                nurseGreenHashedPassword);

                String nurseHallUsername = "nurse_hall";
                String nurseHallPassword = nurseHallUsername + "1234"; // nurse_hall1234
                String nurseHallHashedPassword = encoderDefault.encode(nurseHallPassword);
                System.out.printf("User: %-15s | Plain: %-15s | Hash: %s%n", nurseHallUsername, nurseHallPassword,
                                nurseHallHashedPassword);
        }
}

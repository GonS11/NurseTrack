-- 1) Usuarios (username + 1234)
INSERT INTO users (
    firstname, lastname, username, email, password, role, license_number, is_active
) VALUES
    -- Admin
    ('Admin', 'Main', 'admin1',    'admin@example.com',    '$2a$10$aLVtkA4FTO4pHTdE45MF5.teVmxp.3nieCLW/ywL7at7R6D2m21Eu', 'ROLE_ADMIN',      NULL,        TRUE),

    -- Supervisors (Active)
    ('Supervisor', 'Boss',        'super1',        'super@example.com',  '$2a$10$QfrBQ301CwrcY9TaR.v8d.f2rDBxK3D4UOdJvmV8F/c4Nqkrm8mve',      'ROLE_SUPERVISOR', '12345678', TRUE), -- Password: super1
    ('Supervisora','Ana',        'super_ana',    'ana.g@example.com',  '$2a$12$0xiVzHeykdcSXYue6IoYTOSMIWgznFKxe4GXk1BHzQ6e1T2iehLlC', 'ROLE_SUPERVISOR', '87654321', TRUE), -- Password: super_ana
    ('Supervisor', 'David',        'super_david',    'david.d@example.com','$2a$10$aayyRI0bmeezFwtuNJRWKOaxBrFB2p75lKdJkoyTs/6pnlYjQN0Ke','ROLE_SUPERVISOR', '24681357', TRUE), -- Password: super_david

    -- Supervisor (Inactive)
    ('Supervisor', 'Carlos',    'super_carlos', 'carlos.l@example.com','$2a$10$6fUsAvVBFaExux.Zw6UX1.lqe1XX08sU2RmkHlcJtCbuOVShidOTS','ROLE_SUPERVISOR', '13579246', FALSE), -- Password: super_carlos

    -- Nurses (Active and assigned)
    ('Nurse', 'Smith',        'nurse1',        'nurse@example.com',    '$2a$10$.zqEsRkk26TaPS4KG/Mo..cUn0SK.OJh5RJtycCVY3Tkf.mSkprRW',      'ROLE_NURSE',    '11111111', TRUE), -- Password: nurse1
    ('Nurse', 'Johnson',    'nurse2',        'nurse2@example.com', '$2a$10$LQYbAV2VkT4fct34d3MR/OKBWdDyTT93DdocOx9sMukB.IfFa60WO',      'ROLE_NURSE',    '22222222', TRUE), -- Password: nurse2
    ('Nurse', 'Williams', 'nurse3',        'nurse3@example.com', '$2a$10$tzekhJ1FE4gsqh3CB5IUeuBem7472Uo5MhfPKZaZFLzjWuPi3LaGe','ROLE_NURSE',    '33333333', TRUE), -- Password: nurse3
    ('Nurse', 'Davis',        'nurse5',        'nurse5@example.com', '$2a$10$KD2X9H.V/Ed3PP.EUoknj.9DKrD8iiIGQEh1SOKuTxDPDxXRVnMsi',    'ROLE_NURSE',    '55555555', TRUE), -- Password: nurse5
    ('Nurse', 'Miller',     'nurse6',        'nurse6@example.com', '$2a$10$HUSeR/vrtKvAxnzNjCmoL.npwgwnufWiVJ//Rfez7tYly7IlN9XMK',    'ROLE_NURSE',    '66666666', TRUE), -- Password: nurse6
    ('Nurse', 'Wilson',     'nurse7',        'nurse7@example.com', '$2a$10$Xv22kNdM1wkxwPS6yqZjseg1zUCDqukmCGgSHxaGNvNjP8CJE/ukC',    'ROLE_NURSE',    '77777777', TRUE), -- Password: nurse7
    ('Nurse', 'Moore',        'nurse8',        'nurse8@example.com', '$2a$10$H5tPZgUnihvjRmGM8S8aW.LNz3OLkTWt1hVoR0OVPhvWLbK.4uGaq',    'ROLE_NURSE',    '88888888', TRUE), -- Password: nurse8
    ('Nurse', 'Taylor',     'nurse9',        'nurse9@example.com', '$2a$10$AF0M8NDMrqGyFRblY7EPvunSLBWqTIh18zUg0JePMLLbquTzo5FQG',    'ROLE_NURSE',    '99999999', TRUE), -- Password: nurse9

    -- Nurses (Inactive)
    ('Nurse', 'Brown',        'nurse4',        'nurse4@example.com', '$2a$10$fbv6cf0wmX3dSO89BMgvjOHf2YQogUT9buSRaJYW0M5YvagzPpDHS',    'ROLE_NURSE',    '44444444', FALSE), -- Password: nurse4
    ('Nurse', 'Clark',        'nurse_clark',    'clark.c@example.com', '$2a$10$RX2XJhQHBLQoNxGqCN5ByOaakc2X.yM84AGnV1w1aecry5qOcqZ42',       'ROLE_NURSE',    '10101010', FALSE), -- Password: nurse_clark

    -- Nurses (Active, pero sin asignar inicialmente a ningún departamento)
    ('Nurse', 'Green',        'nurse_green',    'green.g@example.com',    '$2a$10$XLJdAbvHUKV758nNxyI8S.xKb6rdQRIuc8cYtsmezdj4Vpg9yrl6i','ROLE_NURSE','12121212', TRUE), -- Password: nurse_green
    ('Nurse', 'Hall',         'nurse_hall',    'hall.h@example.com',    '$2a$10$Z8WRCYCAPVT90bgmBtJQ0uspidiCFGMKHJdJvpUI93nNlWdTtqgiK','ROLE_NURSE','13131313', TRUE); -- Password: nurse_hall


-- 2) Departamentos
INSERT INTO departments (name, location, is_active)
VALUES
    ('Cardiology',       'Floor 1, East Wing',             TRUE),
    ('Neurology',        'Floor 1, West Wing',             TRUE),
    ('Emergency Room',   'Ground Floor, Main Entrance',    TRUE),
    ('Oncology',         'Floor 3, South Building',        TRUE),
    ('ICU',              'Floor 2, Critical Care Unit',    TRUE),
    ('Maternity',        'Floor 5, North Tower',           TRUE),
    -- Inactive Departments
    ('Pediatrics',       'Floor 2, North Block',           FALSE),
    ('Surgery',          'Floor 4, Operating Theatres',    FALSE),
    ('Physical Therapy','Ground Floor, Annex Building',    FALSE);


-- 3) Plantillas de turno (SE ELIMINARON las que duplicaban el enum `type`)
INSERT INTO shift_templates (name, start_time, end_time, type)
VALUES
    ('Morning Shift',      '08:00:00', '15:00:00', 'MORNING'),
    ('Afternoon Shift',    '15:00:00', '22:00:00', 'AFTERNOON'),
    ('Night Shift',        '22:00:00', '08:00:00', 'NIGHT'),
    ('12h Day',            '08:00:00', '20:00:00', 'HALF_MORNING'), -- CORREGIDO: “Short Morning” eliminada
    ('12h Night',          '20:00:00', '08:00:00', 'HALF_NIGHT');
    -- (Se quitaron “Short Morning” y “Late Evening” para no duplicar el campo `type` con valores ya existentes)


-- 4) supervisors_departments – Asignación de Supervisores a Departamentos
INSERT INTO supervisors_departments (supervisor_id, department_id, assigned_at)
VALUES
    ((SELECT id FROM users WHERE username = 'super1'),
     (SELECT id FROM departments WHERE name = 'Cardiology'),
     NOW()
    ),
    ((SELECT id FROM users WHERE username = 'super1'),
     (SELECT id FROM departments WHERE name = 'Emergency Room'),
     NOW()
    ),
    ((SELECT id FROM users WHERE username = 'super_ana'),
     (SELECT id FROM departments WHERE name = 'Neurology'),
     NOW()
    ),

    ((SELECT id FROM users WHERE username = 'super_ana'),
     (SELECT id FROM departments WHERE name = 'Maternity'),
     NOW()
    ),
    ((SELECT id FROM users WHERE username = 'super_david'),
     (SELECT id FROM departments WHERE name = 'Oncology'),
     NOW()
    ),
    ((SELECT id FROM users WHERE username = 'super_david'),
     (SELECT id FROM departments WHERE name = 'ICU'),
     NOW()
    ),
    ((SELECT id FROM users WHERE username = 'super_carlos'),
     (SELECT id FROM departments WHERE name = 'Pediatrics'),
     NOW()
    );


-- 5) nurses_departments – Asignación de Enfermeras a Departamentos
INSERT INTO nurses_departments (nurse_id, department_id, assigned_at) -- <--- ADD assigned_at here
VALUES
    -- Cardiology Nurses
    ((SELECT id FROM users WHERE username = 'nurse1'), (SELECT id FROM departments WHERE name = 'Cardiology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse2'), (SELECT id FROM departments WHERE name = 'Cardiology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse3'), (SELECT id FROM departments WHERE name = 'Cardiology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse4'), (SELECT id FROM departments WHERE name = 'Cardiology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse6'), (SELECT id FROM departments WHERE name = 'Cardiology'), NOW()),

    -- Neurology Nurses
    ((SELECT id FROM users WHERE username = 'nurse1'), (SELECT id FROM departments WHERE name = 'Neurology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse5'), (SELECT id FROM departments WHERE name = 'Neurology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse7'), (SELECT id FROM departments WHERE name = 'Neurology'), NOW()),

    -- Emergency Room Nurses
    ((SELECT id FROM users WHERE username = 'nurse2'), (SELECT id FROM departments WHERE name = 'Emergency Room'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse8'), (SELECT id FROM departments WHERE name = 'Emergency Room'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse9'), (SELECT id FROM departments WHERE name = 'Emergency Room'), NOW()),

    -- Oncology Nurses
    ((SELECT id FROM users WHERE username = 'nurse9'), (SELECT id FROM departments WHERE name = 'Oncology'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse5'), (SELECT id FROM departments WHERE name = 'Oncology'), NOW()),

    -- ICU Nurses
    ((SELECT id FROM users WHERE username = 'nurse3'), (SELECT id FROM departments WHERE name = 'ICU'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse8'), (SELECT id FROM departments WHERE name = 'ICU'), NOW()),

    -- Maternity Nurses
    ((SELECT id FROM users WHERE username = 'nurse6'), (SELECT id FROM departments WHERE name = 'Maternity'), NOW()),
    ((SELECT id FROM users WHERE username = 'nurse7'), (SELECT id FROM departments WHERE name = 'Maternity'), NOW());


-- 6) Shifts
INSERT INTO shifts (
    nurse_id, department_id, shift_template_id,
    shift_date, status, notes, created_by_id
) VALUES
    -- SHIFTS FOR CARDIOLOGY (super1 gestiona esto)
    -- Pasado: Completados
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      DATE_SUB(CURDATE(), INTERVAL 5 DAY), -- 5 días atrás
      'COMPLETED',
      'Morning rounds done, all vitals stable.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'),
      DATE_SUB(CURDATE(), INTERVAL 4 DAY), -- 4 días atrás
      'COMPLETED',
      'Discharged patient X, admitted patient Y.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Pasado: Cancelado
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Night Shift'),
      DATE_SUB(CURDATE(), INTERVAL 3 DAY), -- 3 días atrás
      'CANCELLED',
      'Nurse fell ill, shift covered by agency.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Hoy: Scheduled
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      CURDATE(), -- Hoy
      'SCHEDULED',
      'Today: Special attention to Room 301.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'),
      CURDATE(), -- Hoy
      'SCHEDULED',
      'Today: New admission expected at 16:00.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = '12h Night'),
      CURDATE(), -- Hoy, se extiende a mañana
      'SCHEDULED',
      'Today: Check inventory of emergency supplies.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Futuro: Scheduled
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Night Shift'),
      DATE_ADD(CURDATE(), INTERVAL 1 DAY), -- Mañana
      'SCHEDULED',
      'Tomorrow: Nurse 1 on night duty.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = '12h Day'),
      DATE_ADD(CURDATE(), INTERVAL 2 DAY), -- En 2 días
      'SCHEDULED',
      'Upcoming: Long shift, ensure proper handover.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      DATE_ADD(CURDATE(), INTERVAL 7 DAY), -- La próxima semana
      'SCHEDULED',
      'Next week: Regular morning shift.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Turno asignado a enfermera inactiva (para test)
    (
      (SELECT id FROM users WHERE username = 'nurse4'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'),
      DATE_ADD(CURDATE(), INTERVAL 10 DAY),
      'SCHEDULED',
      'Assigned to inactive nurse, awaiting reassignment.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Turno 11: Offered for swap (Nurse1)
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      DATE_ADD(CURDATE(), INTERVAL 4 DAY),
      'SCHEDULED',
      'Offered for swap due to personal appointment.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    -- Turno 12: Desired for swap (Nurse2)
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'),
      DATE_ADD(CURDATE(), INTERVAL 5 DAY),
      'SCHEDULED',
      'Desired for swap, looking for a morning shift.',
      (SELECT id FROM users WHERE username = 'super1')
    ),

    -- SHIFTS FOR NEUROLOGY (super_ana)
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      (SELECT id FROM departments WHERE name = 'Neurology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      CURDATE(),
      'SCHEDULED',
      'Neurology morning check-ups.',
      (SELECT id FROM users WHERE username = 'super_ana')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse6'),
      (SELECT id FROM departments WHERE name = 'Neurology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'),
      DATE_ADD(CURDATE(), INTERVAL 1 DAY),
      'SCHEDULED',
      'Neurology evening shift, patient assessments.',
      (SELECT id FROM users WHERE username = 'super_ana')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse1'), -- Nurse1 también en Neurology
      (SELECT id FROM departments WHERE name = 'Neurology'),
      (SELECT id FROM shift_templates WHERE name = 'Night Shift'),
      DATE_ADD(CURDATE(), INTERVAL 3 DAY),
      'SCHEDULED',
      'Neurology night duty, monitoring critical cases.',
      (SELECT id FROM users WHERE username = 'super_ana')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse7'),
      (SELECT id FROM departments WHERE name = 'Neurology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'), -- CORREGIDO: antes era 'Short Morning'
      DATE_ADD(CURDATE(), INTERVAL 8 DAY),
      'SCHEDULED',
      'Short shift in Neurology.',
      (SELECT id FROM users WHERE username = 'super_ana')
    ),

    -- SHIFTS FOR EMERGENCY ROOM (super1)
    (
      (SELECT id FROM users WHERE username = 'nurse7'),
      (SELECT id FROM departments WHERE name = 'Emergency Room'),
      (SELECT id FROM shift_templates WHERE name = '12h Day'),
      CURDATE(),
      'SCHEDULED',
      'ER shift, expect high volume.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse8'),
      (SELECT id FROM departments WHERE name = 'Emergency Room'),
      (SELECT id FROM shift_templates WHERE name = '12h Night'),
      DATE_ADD(CURDATE(), INTERVAL 1 DAY),
      'SCHEDULED',
      'ER night shift, prepare for traumas.',
      (SELECT id FROM users WHERE username = 'super1')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse9'),
      (SELECT id FROM departments WHERE name = 'Emergency Room'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      DATE_ADD(CURDATE(), INTERVAL 6 DAY),
      'SCHEDULED',
      'Regular morning ER shift.',
      (SELECT id FROM users WHERE username = 'super1')
    ),

    -- SHIFTS FOR ONCOLOGY (super_david)
    (
      (SELECT id FROM users WHERE username = 'nurse9'),
      (SELECT id FROM departments WHERE name = 'Oncology'),
      (SELECT id FROM shift_templates WHERE name = 'Morning Shift'),
      CURDATE(),
      'SCHEDULED',
      'Oncology morning shift, patient comfort.',
      (SELECT id FROM users WHERE username = 'super_david')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      (SELECT id FROM departments WHERE name = 'Oncology'),
      (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift'), -- CORREGIDO: antes era 'Late Evening'
      DATE_ADD(CURDATE(), INTERVAL 2 DAY),
      'SCHEDULED',
      'Afternoon for oncology patients.',
      (SELECT id FROM users WHERE username = 'super_david')
    ),

    -- SHIFTS FOR ICU (super_david)
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      (SELECT id FROM departments WHERE name = 'ICU'),
      (SELECT id FROM shift_templates WHERE name = 'Night Shift'),
      CURDATE(),
      'SCHEDULED',
      'Critical care monitoring.',
      (SELECT id FROM users WHERE username = 'super_david')
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse8'),
      (SELECT id FROM departments WHERE name = 'ICU'),
      (SELECT id FROM shift_templates WHERE name = '12h Day'),
      DATE_ADD(CURDATE(), INTERVAL 1 DAY),
      'SCHEDULED',
      'Long day in ICU.',
      (SELECT id FROM users WHERE username = 'super_david')
    );


-- 7) ShiftChangeRequests
INSERT INTO shift_change_requests (
    requester_id, offered_shift_id, recipient_id,
    desired_shift_id, reason, reviewed_notes,
    status, is_interchange, reviewed_by_id, reviewed_at
) VALUES
    -- 7.1 Pending Interchange (Nurse1 ofrece Morning(4) por Afternoon(5))
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      -- ID del turno “Morning Shift” de hoy en Cardiology para nurse1
      (SELECT id FROM shifts
          WHERE shift_date = CURDATE()
            AND nurse_id = (SELECT id FROM users WHERE username = 'nurse1')
            AND department_id = (SELECT id FROM departments WHERE name = 'Cardiology')
            AND shift_template_id = (SELECT id FROM shift_templates WHERE name = 'Morning Shift')
          LIMIT 1
      ),
      (SELECT id FROM users WHERE username = 'nurse2'),
      -- ID del turno “Afternoon Shift” de hoy en Cardiology para nurse2
      (SELECT id FROM shifts
          WHERE shift_date = CURDATE()
            AND nurse_id = (SELECT id FROM users WHERE username = 'nurse2')
            AND department_id = (SELECT id FROM departments WHERE name = 'Cardiology')
            AND shift_template_id = (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift')
          LIMIT 1
      ),
      'Emergency family matter, need to swap shifts today.',
      NULL,
      'PENDING',
      TRUE,
      NULL,
      NULL
    ),
    -- 7.2 Approved Swap (Turno #3 de Nurse3 por Turno #1 de Nurse1)
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      (SELECT id FROM shifts WHERE id = 3), -- Cancelled shift
      (SELECT id FROM users WHERE username = 'nurse1'),
      (SELECT id FROM shifts WHERE id = 1), -- Completed shift
      'Previous commitment came up, swap needed.',
      'Approved, confirmed cover.',
      'APPROVED',
      FALSE,
      (SELECT id FROM users WHERE username = 'super1'),
      DATE_SUB(NOW(), INTERVAL 2 DAY)
    ),
    -- 7.3 Rejected Request (Nurse5 ofrece Neurology(13), Nurse6 quiere Afternoon)
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      (SELECT id FROM shifts WHERE id = 13), -- Shift #13
      (SELECT id FROM users WHERE username = 'nurse6'),
      -- ID del turno “Afternoon Shift” en Neurology para nurse6 (mañana)
      (SELECT id FROM shifts
          WHERE shift_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY)
            AND nurse_id = (SELECT id FROM users WHERE username = 'nurse6')
            AND department_id = (SELECT id FROM departments WHERE name = 'Neurology')
            AND shift_template_id = (SELECT id FROM shift_templates WHERE name = 'Afternoon Shift')
          LIMIT 1
      ),
      'Feeling unwell, cannot make it.',
      'Rejected due to insufficient notice and no replacement found.',
      'REJECTED',
      FALSE,
      (SELECT id FROM users WHERE username = 'super_ana'),
      DATE_SUB(NOW(), INTERVAL 1 DAY)
    ),
    -- 7.4 Pending Interchange (Nurse7 ofrece ER(17), Recipient Nurse8 con 12h Night)
    (
      (SELECT id FROM users WHERE username = 'nurse7'),
      (SELECT id FROM shifts WHERE id = 17),
      (SELECT id FROM users WHERE username = 'nurse8'),
      -- ID del turno “12h Night” en ER mañana para nurse8
      (SELECT id FROM shifts
          WHERE shift_date = DATE_ADD(CURDATE(), INTERVAL 1 DAY)
            AND nurse_id = (SELECT id FROM users WHERE username = 'nurse8')
            AND department_id = (SELECT id FROM departments WHERE name = 'Emergency Room')
            AND shift_template_id = (SELECT id FROM shift_templates WHERE name = '12h Night')
          LIMIT 1
      ),
      'Unexpected appointment, need to find coverage.',
      NULL,
      'PENDING',
      TRUE,
      NULL,
      NULL
    ),
    -- 7.5 Pending Interchange (Nurse9 ofrece Oncology(20) por Oncology(21))
    (
      (SELECT id FROM users WHERE username = 'nurse9'),
      (SELECT id FROM shifts WHERE id = 20),
      (SELECT id FROM users WHERE username = 'nurse5'),
      (SELECT id FROM shifts WHERE id = 21),
      'Need to switch shifts for a personal event.',
      NULL,
      'PENDING',
      TRUE,
      NULL,
      NULL
    );


-- 8) VacationRequests
INSERT INTO vacation_requests (
    requester_id, start_date, end_date,
    reason, reviewed_notes, status,
    reviewed_by_id, reviewed_at
) VALUES
    -- 8.1 Pending Request (Nurse1, dentro de 30 días)
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      DATE_ADD(CURDATE(), INTERVAL 30 DAY),
      DATE_ADD(CURDATE(), INTERVAL 37 DAY),
      'Annual family holiday to the beach.',
      NULL,
      'PENDING',
      NULL,
      NULL
    ),
    -- 8.2 Approved Request (Nurse5, dentro de 60 días)
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      DATE_ADD(CURDATE(), INTERVAL 60 DAY),
      DATE_ADD(CURDATE(), INTERVAL 67 DAY),
      'Trip to mountains.',
      'Approved, confirmed coverage in Neurology.',
      'APPROVED',
      (SELECT id FROM users WHERE username = 'super_ana'),
      DATE_SUB(NOW(), INTERVAL 10 DAY)
    ),
    -- 8.3 Rejected Request (Nurse2, la próxima semana)
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      DATE_ADD(CURDATE(), INTERVAL 7 DAY),
      DATE_ADD(CURDATE(), INTERVAL 9 DAY),
      'Short break, personal reasons.',
      'Rejected: Too many staff off this week, cannot approve.',
      'REJECTED',
      (SELECT id FROM users WHERE username = 'super1'),
      DATE_SUB(NOW(), INTERVAL 2 DAY)
    ),
    -- 8.4 Past Approved Request (Nurse8, ER)
    (
      (SELECT id FROM users WHERE username = 'nurse8'),
      DATE_SUB(CURDATE(), INTERVAL 15 DAY),
      DATE_SUB(CURDATE(), INTERVAL 10 DAY),
      'Short vacation after intense ER period.',
      'Approved quickly.',
      'APPROVED',
      (SELECT id FROM users WHERE username = 'super1'),
      DATE_SUB(NOW(), INTERVAL 14 DAY)
    ),
    -- 8.5 Another Pending Request (Nurse6, Maternity)
    (
      (SELECT id FROM users WHERE username = 'nurse6'),
      DATE_ADD(CURDATE(), INTERVAL 45 DAY),
      DATE_ADD(CURDATE(), INTERVAL 50 DAY),
      'Wedding attendance.',
      NULL,
      'PENDING',
      NULL,
      NULL
    );


-- 9) Notifications
INSERT INTO notifications (
    user_id, type, title, message, is_read
) VALUES
    -- Recordatorios Generales
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      'GENERAL',
      'Shift Reminder',
      'Your Morning Shift in Cardiology is today at 08:00.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      'GENERAL',
      'Holiday Alert',
      'Don''t forget your upcoming vacation starts in 60 days!',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      'GENERAL',
      'ICU Shift Reminder',
      'Your Night Shift in ICU is today at 22:00. Be prepared.',
      FALSE
    ),

    -- Notificaciones de Cambio de Turno
    (
      (SELECT id FROM users WHERE username = 'super1'),
      'SHIFT_CHANGE',
      'New Shift Change Request (Cardiology)',
      'Nurse Smith (nurse1) has requested a shift swap for today.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      'SHIFT_CHANGE',
      'Shift Swap Offer Received',
      'Nurse Smith (nurse1) wants to swap shifts with you for today.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse3'),
      'SHIFT_CHANGE',
      'Your Shift Swap was Approved',
      'Your shift swap request has been approved by Supervisor Boss.',
      TRUE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      'SHIFT_CHANGE',
      'Your Shift Change Request was Rejected',
      'Your request to change your Neurology shift was rejected.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'super_david'),
      'SHIFT_CHANGE',
      'New Shift Change Request (Oncology)',
      'Nurse Taylor (nurse9) has requested a shift swap.',
      FALSE
    ),

    -- Notificaciones de Vacaciones
    (
      (SELECT id FROM users WHERE username = 'super_ana'),
      'VACATION_REQUEST',
      'New Vacation Request (Maternity)',
      'Nurse Miller (nurse6) has submitted a vacation request.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse2'),
      'VACATION_REQUEST',
      'Your Vacation Request was Rejected',
      'Your vacation request for next week was rejected due to staffing.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'nurse5'),
      'VACATION_REQUEST',
      'Vacation Approved',
      'Your vacation request for next month has been approved.',
      TRUE
    ),

    -- Notificaciones de Sistema
    (
      (SELECT id FROM users WHERE username = 'nurse1'),
      'SYSTEM',
      'System Update Scheduled',
      'Important system maintenance on Sunday, June 10th at 02:00 AM.',
      FALSE
    ),
    (
      (SELECT id FROM users WHERE username = 'super1'),
      'SYSTEM',
      'Security Alert',
      'Unusual login activity detected on your account. Please review.',
      TRUE
    ),
    (
      (SELECT id FROM users WHERE username = 'admin1'),
      'SYSTEM',
      'New Department Added',
      'A new department, "Maternity," has been added to the system.',
      TRUE
    );
-- 1) Usuarios
INSERT INTO users (
    firstname, lastname, username, email, password, role, license_number, is_active
) VALUES
    ('Admin',      'Admin',   'admin1',   'admin@example.com', '$2a$10$BallFZIuncRKa2pTbXdlyuJIJBe1E1HgwWFfz644o.PILvXbkpUyC', 'ROLE_ADMIN',      NULL,   TRUE),
    ('Supervisor', 'Super',   'super1',   'super@example.com', '$2a$10$mxUbp36Dl3xHRlxS5ERfoud7ogjktmZV85.iMblVUh0vtWZDZAngi', 'ROLE_SUPERVISOR','12345678', TRUE),
    ('Nurse',      'Nurse',    'nurse1',   'nurse@example.com', '$2a$10$FwM.zUecoItXHTfrF6qbSeHjEiKtVnXImUAccr3Ob8IBYxiMBVDC2', 'ROLE_NURSE',     '11111111',TRUE),
    ('Nurse2',     'Nurse2',   'nurse2',   'nurse2@example.com','$2a$10$4d24W7ELHF02eRMyTHGBreh/DroQv5FeCnEwEtYbp4tYggU/FoZ0G', 'ROLE_NURSE',     '22222222',TRUE);

-- 2) Departamentos
INSERT INTO departments (name, location, is_active)
VALUES
    ('Cardiology','Floor 1',TRUE),
    ('Neurology', 'Floor 1',TRUE),
    ('Pediatrics','Floor 2',FALSE);

-- 3) Plantillas de turno
INSERT INTO shift_templates (name, start_time, end_time, type)
VALUES
    ('Morning Shift',   '08:00:00','15:00:00','MORNING'),
    ('Afternoon Shift', '15:00:00','22:00:00','AFTERNOON'),
    ('Night Shift',     '22:00:00','08:00:00','NIGHT'),
    ('12h Morning',     '08:00:00','20:00:00','HALF_MORNING'),
    ('12h Night',       '20:00:00','08:00:00','HALF_NIGHT');

-- 4) SupervisorDepartment
INSERT INTO supervisors_departments (supervisor_id, department_id)
VALUES (
    (SELECT id FROM users WHERE username='super1'),
    (SELECT id FROM departments WHERE name='Cardiology')
);

-- 5) NurseDepartment
INSERT INTO nurses_departments (nurse_id, department_id)
VALUES
    ((SELECT id FROM users WHERE username='nurse1'), (SELECT id FROM departments WHERE name='Neurology')),
    ((SELECT id FROM users WHERE username='nurse1'), (SELECT id FROM departments WHERE name='Cardiology'));

-- 6) Shifts
INSERT INTO shifts (
    nurse_id, department_id, shift_template_id,
    shift_date, status, notes, created_by_id
) VALUES
    -- 6.1 Pasados
    (
      (SELECT id FROM users WHERE username='nurse1'),
      (SELECT id FROM departments WHERE name='Cardiology'),
      (SELECT id FROM shift_templates WHERE type='MORNING'),
      DATE_SUB(CURDATE(), INTERVAL 2 DAY),
      'COMPLETED',
      'Everything went smoothly',
      (SELECT id FROM users WHERE username='super1')
    ),
    (
      (SELECT id FROM users WHERE username='nurse1'),
      (SELECT id FROM departments WHERE name='Cardiology'),
      (SELECT id FROM shift_templates WHERE type='AFTERNOON'),
      DATE_SUB(CURDATE(), INTERVAL 1 DAY),
      'COMPLETED',
      'No incidents reported',
      (SELECT id FROM users WHERE username='super1')
    ),
    -- 6.2 Hoy
    (
      (SELECT id FROM users WHERE username='nurse1'),
      (SELECT id FROM departments WHERE name='Cardiology'),
      (SELECT id FROM shift_templates WHERE type='NIGHT'),
      CURDATE(),
      'SCHEDULED',
      'Check ICU patients',
      (SELECT id FROM users WHERE username='super1')
    ),
    -- 6.3 Próximo
    (
      (SELECT id FROM users WHERE username='nurse1'),
      (SELECT id FROM departments WHERE name='Neurology'),
      (SELECT id FROM shift_templates WHERE type='MORNING'),
      DATE_ADD(CURDATE(), INTERVAL 1 DAY),
      'SCHEDULED',
      'Prepare ward rounds',
      (SELECT id FROM users WHERE username='super1')
    ),

   -- 6.4 Turno id=5, 4 días en el futuro (2025-05-22)
   (
     (SELECT id FROM users WHERE username='nurse1'),
     (SELECT id FROM departments WHERE name='Cardiology'),
     (SELECT id FROM shift_templates WHERE type='MORNING'),
     DATE_ADD(CURDATE(), INTERVAL 4 DAY),  -- Será 2025-05-22
     'SCHEDULED',
     'Offered for swap',
     (SELECT id FROM users WHERE username='super1')
   ),

   -- 6.5 Turno id=6, 5 días en el futuro (2025-05-23)
   (
     (SELECT id FROM users WHERE username='nurse1'),
     (SELECT id FROM departments WHERE name='Cardiology'),
     (SELECT id FROM shift_templates WHERE type='AFTERNOON'),
     DATE_ADD(CURDATE(), INTERVAL 5 DAY),  -- Será 2025-05-23
     'SCHEDULED',
     'Desired for swap',
     (SELECT id FROM users WHERE username='super1')
   );
-- 7) ShiftChangeRequests
INSERT INTO shift_change_requests (
    requester_id, offered_shift_id, recipient_id,
    desired_shift_id, reason, reviewed_notes,
    status, is_interchange, reviewed_by_id, reviewed_at
) VALUES
    -- 7.1 Pendiente de intercambio
    (
      (SELECT id FROM users WHERE username='nurse1'),
      3,
      (SELECT id FROM users WHERE username='nurse2'),
      4,
      'I need to attend a medical appointment',
      '',
      'PENDING',
      TRUE,
      NULL,
      NULL
    ),
    -- 7.2 Aprobada reemplazo
    (
      (SELECT id FROM users WHERE username='nurse1'),
      1,
      (SELECT id FROM users WHERE username='nurse2'),
      2,
      'Personal reasons',
      'Reviewed and approved',
      'APPROVED',
      FALSE,
      (SELECT id FROM users WHERE username='super1'),
      DATE_SUB(NOW(), INTERVAL 1 DAY)
    );

-- 8) VacationRequests
INSERT INTO vacation_requests (
    requester_id, start_date, end_date,
    reason, reviewed_notes, status,
    reviewed_by_id, reviewed_at
) VALUES
    -- 8.1 Pendientes
    (
      (SELECT id FROM users WHERE username='nurse1'),
      DATE_ADD(CURDATE(), INTERVAL 7 DAY),
      DATE_ADD(CURDATE(), INTERVAL 14 DAY),
      'Family vacation',
      NULL,
      'PENDING',
      NULL,
      NULL
    ),
    -- 8.2 Aprobadas
    (
      (SELECT id FROM users WHERE username='nurse1'),
      DATE_SUB(CURDATE(), INTERVAL 30 DAY),
      DATE_SUB(CURDATE(), INTERVAL 25 DAY),
      'Annual leave',
      'Approved by supervisor',
      'APPROVED',
      (SELECT id FROM users WHERE username='super1'),
      DATE_SUB(NOW(), INTERVAL 28 DAY)
    );

-- 9) Notifications
INSERT INTO notifications (
    user_id, type, title, message, is_read
) VALUES
    -- 9.1 Shift Reminder
    (
      (SELECT id FROM users WHERE username='nurse1'),
      'GENERAL',
      'Shift Reminder',
      'Your Night Shift is tomorrow at 22:00.',
      FALSE
    ),
    -- 9.2 Vacation Request Submitted
    (
      (SELECT id FROM users WHERE username='nurse1'),
      'VACATION_REQUEST',
      'Vacation Request Submitted',
      CONCAT(
        'Your vacation request from ',
        DATE_ADD(CURDATE(), INTERVAL 7 DAY),
        ' to ',
        DATE_ADD(CURDATE(), INTERVAL 14 DAY),
        ' is pending.'
      ),
      FALSE
    ),
    -- 9.3 New Shift Change Request
    (
      (SELECT id FROM users WHERE username='super1'),
      'SHIFT_CHANGE',
      'New Shift Change Request',
      'You have a pending shift change request.',
      FALSE
    ),
    -- 9.4 Scheduled Maintenance
    (
      (SELECT id FROM users WHERE username='nurse1'),
      'SYSTEM',
      'Scheduled Maintenance',
      'The system will undergo maintenance next Sunday.',
      TRUE
    );

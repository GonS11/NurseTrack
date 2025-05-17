-- Insert users
INSERT INTO users (firstname, lastname, username, email, password, role, license_number, is_active)
VALUES
    ('Admin',      'Admin',  'admin1',  'admin@example.com', '$2a$10$BallFZIuncRKa2pTbXdlyuJIJBe1E1HgwWFfz644o.PILvXbkpUyC', 'ADMIN',      NULL,   TRUE),
    ('Supervisor', 'Super',  'super1',  'super@example.com', '$2a$10$mxUbp36Dl3xHRlxS5ERfoud7ogjktmZV85.iMblVUh0vtWZDZAngi', 'SUPERVISOR','12345678', TRUE),
    ('Nurse',      'Nurse',  'nurse1',  'nurse@example.com', '$2a$10$FwM.zUecoItXHTfrF6qbSeHjEiKtVnXImUAccr3Ob8IBYxiMBVDC2', 'NURSE',     '11111111',TRUE),
    ('Nurse2',      'Nurse2',  'nurse2',  'nurse2@example.com', '$2a$10$4d24W7ELHF02eRMyTHGBreh/DroQv5FeCnEwEtYbp4tYggU/FoZ0G', 'NURSE',     '22222222',TRUE);

-- Insert departments
INSERT INTO departments (name, location, is_active)
VALUES
    ('Cardiology', 'Floor 1', TRUE),
    ('Neurology',  'Floor 1', TRUE),
    ('Pediatrics', 'Floor 2', FALSE);

-- Insert shift templates
INSERT INTO shift_templates (name, start_time, end_time, type)
VALUES
    ('Morning Shift',   '08:00:00', '15:00:00', 'MORNING'),
    ('Afternoon Shift', '15:00:00', '22:00:00', 'AFTERNOON'),
    ('Night Shift',     '22:00:00', '08:00:00', 'NIGHT'),
    ('12h Morning',     '08:00:00', '20:00:00', 'HALF_MORNING'),
    ('12h Night',       '20:00:00', '08:00:00', 'HALF_NIGHT');

-- Assign supervisor to department (uses CURRENT_TIMESTAMP)
INSERT INTO supervisors_departments (supervisor_id, department_id, assigned_at)
VALUES
    (
      (SELECT id FROM users WHERE username = 'super1'),
      (SELECT id FROM departments WHERE name = 'Cardiology'),
      CURRENT_TIMESTAMP
    );

-- 1) Insert nurse-department assignments (fecha fija o CURRENT_TIMESTAMP - días no siempre funciona en H2)
INSERT INTO nurses_departments (nurse_id, department_id, assigned_at)
VALUES
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Neurology'),
    CURRENT_TIMESTAMP
  ),
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    CURRENT_TIMESTAMP
  );

-- 2) Insert shifts (usa literales de fecha para evitar INTERVAL):
INSERT INTO shifts (nurse_id, department_id, shift_template_id, shift_date, status, notes, created_by_id)
VALUES
  -- Completed shifts for nurse1 in Cardiology
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    (SELECT id FROM shift_templates WHERE type = 'MORNING'),
    DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY),
    'COMPLETED',
    'Everything went smoothly',
    (SELECT id FROM users WHERE username = 'super1')
  ),
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    (SELECT id FROM shift_templates WHERE type = 'AFTERNOON'),
    DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY),
    'COMPLETED',
    'No incidents reported',
    (SELECT id FROM users WHERE username = 'super1')
  ),
  -- Scheduled shift today
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    (SELECT id FROM shift_templates WHERE type = 'NIGHT'),
    CURRENT_DATE,
    'SCHEDULED',
    'Check ICU patients',
    (SELECT id FROM users WHERE username = 'super1')
  ),
  -- Upcoming shift in Neurology
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Neurology'),
    (SELECT id FROM shift_templates WHERE type = 'MORNING'),
    DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY),
    'SCHEDULED',
    'Prepare ward rounds',
    (SELECT id FROM users WHERE username = 'super1')
  );

-- 3) Insert shift change requests
INSERT INTO shift_change_requests (
  requester_id, offered_shift_id, recipient_id, requested_shift_id,
  reason, reviewed_notes, status, is_interchange, reviewed_by_id, reviewed_at
) VALUES
  -- Pending interchange
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    3,    -- offered_shift_id
    (SELECT id FROM users WHERE username = 'nurse2'),
    4,    -- requested_shift_id
    'I need to attend a medical appointment',
    'Not reviewed',
    'PENDING',
    TRUE,
    NULL,
    NULL
  ),
  -- Approved replacement
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    1,
    (SELECT id FROM users WHERE username = 'nurse2'),  -- Recipient válido
    2,
    'Personal reasons',
    'Reviewed',
    'APPROVED',
    FALSE,
    (SELECT id FROM users WHERE username = 'super1'),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY)
  );

-- 4) Insert vacation requests
INSERT INTO vacation_requests (
  requester_id, start_date, end_date, reason,
  reviewed_notes, status, reviewed_by_id, reviewed_at
) VALUES
  -- Pending vacation
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY),
    DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY),
    'Family vacation',
    NULL,
    'PENDING',
    NULL,
    NULL
  ),
  -- Approved vacation
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY),
    DATE_SUB(CURRENT_DATE, INTERVAL 25 DAY),
    'Annual leave',
    'Approved by supervisor',
    'APPROVED',
    (SELECT id FROM users WHERE username = 'super1'),
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 28 DAY)
  );

-- 5) Insert notifications
INSERT INTO notifications (user_id, type, title, message, is_read)
VALUES
  -- For nurse1
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    'GENERAL',
    'Shift Reminder',
    'Your Night Shift is tomorrow at 22:00.',
    FALSE
  ),
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    'VACATION_REQUEST',
    'Vacation Request Submitted',
    CONCAT(
      'Your vacation request from ',
      DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY),
      ' to ',
      DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY),
      ' is pending.'
    ),
    FALSE
  ),
  -- For supervisor
  (
    (SELECT id FROM users WHERE username = 'super1'),
    'SHIFT_CHANGE',
    'New Shift Change Request',
    'You have a pending shift change request.',
    FALSE
  ),
  -- Already read
  (
    (SELECT id FROM users WHERE username = 'nurse1'),
    'SYSTEM',
    'Scheduled Maintenance',
    'The system will undergo maintenance next Sunday.',
    TRUE
  );

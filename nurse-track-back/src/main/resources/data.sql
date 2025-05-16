-- Insertar usuarios
INSERT INTO users (firstname, lastname, username, email, password, role, license_number, is_active) VALUES
('Admin', 'Admin', 'admin1', 'admin@example.com', '$2a$10$BallFZIuncRKa2pTbXdlyuJIJBe1E1HgwWFfz644o.PILvXbkpUyC', 'ADMIN', NULL, true),
('Supervisor', 'Super', 'super1', 'super@example.com', '$2a$10$mxUbp36Dl3xHRlxS5ERfoud7ogjktmZV85.iMblVUh0vtWZDZAngi', 'SUPERVISOR', '12345678', true),
('Nurse', 'Nurse', 'nurse1', 'nurse@example.com', '$2a$10$FwM.zUecoItXHTfrF6qbSeHjEiKtVnXImUAccr3Ob8IBYxiMBVDC2', 'NURSE', '11111111', true);

-- Insertar departamentos
INSERT INTO departments (name, location, is_active) VALUES
('Cardiology', 'Floor 1', true),
('Neurology', 'Floor 1', true),
('Pediatrics', 'Floor 2', false);

-- Insertar plantillas de turnos
INSERT INTO shift_templates (name, start_time, end_time, type) VALUES
('Morning Shift', '08:00:00', '15:00:00', 'MORNING'),
('Afternoon Shift', '15:00:00', '22:00:00', 'AFTERNOON'),
('Night Shift', '22:00:00', '08:00:00', 'NIGHT'),
('12h Morning', '08:00:00', '20:00:00', 'HALF_MORNING'),
('12h Night', '20:00:00', '08:00:00', 'HALF_NIGHT');

-- Asignar supervisor a departamento
INSERT INTO supervisors_departments (supervisor_id, department_id, assigned_at)
VALUES (
    (SELECT id FROM users WHERE username = 'super1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    NOW()
);

-- Asignar enfermero a departamento
INSERT INTO nurses_departments (nurse_id, department_id, assigned_at)
VALUES (
    (SELECT id FROM users WHERE username = 'nurse1'),
    (SELECT id FROM departments WHERE name = 'Cardiology'),
    NOW()
);
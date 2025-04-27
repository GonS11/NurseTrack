CREATE DATABASE IF NOT EXISTS nurse_track;
USE nurse_track;


CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'SUPERVISOR', 'NURSE') DEFAULT 'NURSE',
    license_number VARCHAR(50) UNIQUE DEFAULT NULL,
    is_active Boolean DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_users_role_active (role, is_active)
);

INSERT INTO users (first_name, last_name, username, email, password, role, license_number)
VALUES
    ('Admin','Admin','admin1','admin@example.com','<HASHED_PASSWORD>','ADMIN',NULL),
    ('Supervisor','Super','super1','super@example.com','<HASHED_PASSWORD>','SUPERVISOR','12345678'),
    ('Nurse','Nurse','nurse1','nurse@example.com','<HASHED_PASSWORD>','NURSE','11111111');


CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL,
    is_active Boolean DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO departments (name, location, is_active)
VALUES
    ('Cardiology','Floor 1',true),
    ('Neurology','Floor 1',true),
    ('Pediatrics','Floor 2',false);


CREATE TABLE shift_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    type ENUM('MORNING', 'AFTERNOON', 'NIGHT', 'HALF_MORNING', 'HALF_NIGHT') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE supervisors_departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    supervisor_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (supervisor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,

    UNIQUE (supervisor_id, department_id),

    INDEX idx_supervisors_departments (department_id, supervisor_id)
);


CREATE TABLE nurses_departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nurse_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (nurse_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,

    INDEX idx_nurses_departments (department_id, nurse_id)
);


CREATE TABLE shifts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nurse_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    shift_template_id BIGINT NOT NULL,
    shift_date DATE NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED') DEFAULT 'SCHEDULED',
    notes TEXT,
    created_by_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (nurse_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE RESTRICT,
    FOREIGN KEY (shift_template_id) REFERENCES shift_templates(id) ON DELETE RESTRICT,
    FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE RESTRICT,

    INDEX idx_shifts_coverage (department_id, shift_date, status),
    INDEX idx_shifts_nurse_date (nurse_id, shift_date)
);


CREATE TABLE shift_change_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    requester_id BIGINT NOT NULL,
    requested_shift_id BIGINT NOT NULL,
    offered_shift_id BIGINT NOT NULL,
    reason TEXT,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by_id BIGINT,
    reviewed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (requested_shift_id) REFERENCES shifts(id) ON DELETE RESTRICT,
    FOREIGN KEY (offered_shift_id) REFERENCES shifts(id) ON DELETE RESTRICT,
    FOREIGN KEY (reviewed_by_id) REFERENCES users(id) ON DELETE RESTRICT,

    INDEX idx_shift_change_requester (requester_id, status),
    INDEX idx_shift_change_timestamps (created_at, reviewed_at)
);


CREATE TABLE vacation_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    requester_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by_id BIGINT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by_id) REFERENCES users(id) ON DELETE SET NULL,

    UNIQUE (requester_id, start_date, end_date),

    INDEX idx_vacation_dates (start_date, end_date),
    INDEX idx_vacation_requester_status (requester_id, status),

    CHECK (end_date >= start_date)
);


CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type ENUM('SHIFT_CHANGE', 'VACATION_REQUEST', 'GENERAL', 'SYSTEM', 'EMERGENCY') DEFAULT 'GENERAL',
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    is_read Boolean DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    INDEX idx_notifications_user (user_id, is_read),
    INDEX idx_notifications_type (type)
);

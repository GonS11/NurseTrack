CREATE DATABASE IF NOT EXISTS `nurse-track`;
USE `nurse-track`;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'SUPERVISOR', 'NURSE') NOT NULL DEFAULT 'NURSE',
    license_number VARCHAR(50) DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_users_role_active (role, is_active)
) ENGINE=InnoDB;

INSERT INTO users (first_name, last_name, email, password, role, license_number)
VALUES
    ('Admin','Admin','admin@example.com','admin1234','ADMIN',null),
    ('Supervisor','Super','super@example.com','super1234','SUPERVISOR','12345678'),
    ('Nurse','Nurse','nurse@example.com','nurse1234','NURSE','11111111');

CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

INSERT INTO departments (name, location, is_active)
VALUES
    ('Cardiology','Floor 1',true),
    ('Neurology','Floor 1',true),
    ('Pediatrics','Floor 2',false);

CREATE TABLE shift_templates (
    shift_template_id INT PRIMARY KEY AUTO_INCREMENT,
    department_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    shift_start_time TIME NOT NULL,
    shift_end_time TIME NOT NULL,
    shift_type ENUM('MORNING', 'AFTERNOON', 'NIGHT', 'HALF_MORNING', 'HALF_NIGHT') NOT NULL,
    display_color VARCHAR(7) DEFAULT '#3498db',
    created_by_user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE RESTRICT,

    UNIQUE (department_id, name),

    INDEX idx_shift_templates_creator (created_by_user_id),

    CONSTRAINT chk_shift_time CHECK (shift_end_time > shift_start_time)
) ENGINE=InnoDB;

INSERT INTO shift_templates(department_id, name, shift_start_time, shift_end_time, shift_type, display_color, created_by_user_id)
VALUES
    (1, 'Morning Shift', '08:00:00', '15:00:00', 'MORNING', '#FF5733', 2),
    (1, 'Afternoon Shift', '15:00:00', '22:00:00', 'AFTERNOON', '#33FF57', 2);

CREATE TABLE supervisors_departments (
    supervisor_department_id INT PRIMARY KEY AUTO_INCREMENT,
    supervisor_user_id INT NOT NULL,
    department_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (supervisor_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,

    UNIQUE (supervisor_user_id, department_id),

    INDEX idx_supervisors_departments (department_id, supervisor_user_id)
) ENGINE=InnoDB;

CREATE TABLE nurses_departments (
    nurse_department_id INT PRIMARY KEY AUTO_INCREMENT,
    nurse_user_id INT NOT NULL,
    department_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (nurse_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,

    INDEX idx_nurses_departments (department_id, nurse_user_id)
) ENGINE=InnoDB;

CREATE TABLE shifts (
    shift_id INT PRIMARY KEY AUTO_INCREMENT,
    nurse_user_id INT,
    department_id INT,
    shift_template_id INT,
    shift_date DATE NOT NULL,
    shift_start_datetime DATETIME NOT NULL,
    shift_end_datetime DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED') DEFAULT 'SCHEDULED',
    notes TEXT,
    created_by_user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (nurse_user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE RESTRICT,
    FOREIGN KEY (shift_template_id) REFERENCES shift_templates(shift_template_id) ON DELETE SET NULL,
    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE RESTRICT,

    INDEX idx_shifts_coverage (department_id, shift_date, status),
    INDEX idx_shifts_nurse_date (nurse_user_id, shift_date),

    CONSTRAINT chk_shift_duration CHECK (shift_end_datetime > shift_start_datetime)
) ENGINE=InnoDB;

CREATE TABLE shift_change_requests (
    shift_change_request_id INT PRIMARY KEY AUTO_INCREMENT,
    requester_user_id INT NOT NULL,
    requested_shift_id INT NOT NULL,
    offered_shift_id INT,
    reason TEXT,
    status ENUM("SCHEDULED", "COMPLETED", "CANCELLED", "PENDING", "APPROVED", "REJECTED") DEFAULT 'PENDING',
    reviewed_by_user_id INT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (requested_shift_id) REFERENCES shifts(shift_id) ON DELETE CASCADE,
    FOREIGN KEY (offered_shift_id) REFERENCES shifts(shift_id) ON DELETE SET NULL,
    FOREIGN KEY (reviewed_by_user_id) REFERENCES users(user_id) ON DELETE SET NULL,

    INDEX idx_shift_change_requester (requester_user_id, status),
    INDEX idx_shift_change_timestamps (created_at, reviewed_at)
) ENGINE=InnoDB;

CREATE TABLE vacation_requests (
    vacation_request_id INT PRIMARY KEY AUTO_INCREMENT,
    requester_user_id INT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by_user_id INT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (requester_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by_user_id) REFERENCES users(user_id) ON DELETE SET NULL,

    UNIQUE (requester_user_id, start_date, end_date),

    INDEX idx_vacation_dates (start_date, end_date),
    INDEX idx_vacation_requester_status (requester_user_id, status),

    CHECK (end_date >= start_date)
) ENGINE=InnoDB;

CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    notification_type ENUM('SHIFT_CHANGE', 'VACATION_REQUEST', 'GENERAL', 'SYSTEM') DEFAULT 'GENERAL',
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,

    INDEX idx_notifications_user (user_id, is_read),
    INDEX idx_notifications_type (notification_type)
) ENGINE=InnoDB;
-- PostgreSQL Schema for Student Tutoring System

-- Create UUID extension if not exists
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USER table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    avatar TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- STUDENT table
CREATE TABLE IF NOT EXISTS students (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    student_id VARCHAR(50) UNIQUE NOT NULL,
    major VARCHAR(255),
    year INTEGER,
    enrollment_date DATE,
    academic_status VARCHAR(50),
    minor VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUBJECT table
CREATE TABLE IF NOT EXISTS subjects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    credits INTEGER,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ENROLLMENT table
CREATE TABLE IF NOT EXISTS enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES students(id),
    subject_id UUID REFERENCES subjects(id),
    enrollment_status VARCHAR(50),
    enrollment_date DATE,
    drop_date DATE,
    academic_term VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUPPORT_NEEDS table
CREATE TABLE IF NOT EXISTS support_needs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES students(id),
    requests_scholarship BOOLEAN DEFAULT FALSE,
    requests_advising BOOLEAN DEFAULT FALSE,
    requests_academic_support BOOLEAN DEFAULT FALSE,
    additional_needs TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TUTOR table
CREATE TABLE IF NOT EXISTS tutors (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    rating DECIMAL(3, 2) DEFAULT 0.00,
    name VARCHAR(255),
    file_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ADS table
CREATE TABLE IF NOT EXISTS ads (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    department VARCHAR(255),
    position_title VARCHAR(255),
    office_location VARCHAR(255),
    specialization TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- OAA table
CREATE TABLE IF NOT EXISTS oaa (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    department VARCHAR(255),
    position_title VARCHAR(255),
    office_location VARCHAR(255),
    responsibilities TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- OSA table
CREATE TABLE IF NOT EXISTS osa (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    department VARCHAR(255),
    position_title VARCHAR(255),
    office_location VARCHAR(255),
    specialization TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TIME_SLOT table
CREATE TABLE IF NOT EXISTS time_slots (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tutor_id UUID REFERENCES tutors(id),
    day VARCHAR(10),
    start_time TIME,
    end_time TIME,
    mode VARCHAR(20), -- ONLINE, OFFLINE, BOTH
    location VARCHAR(255),
    zoom_link TEXT,
    capacity INTEGER DEFAULT 1,
    is_published BOOLEAN DEFAULT FALSE,
    requires_approval BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- BLACKOUT_DATE table
CREATE TABLE IF NOT EXISTS blackout_dates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tutor_id UUID REFERENCES tutors(id),
    date DATE,
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TUTORING_SESSION table
CREATE TABLE IF NOT EXISTS tutoring_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES students(id),
    tutor_id UUID REFERENCES tutors(id),
    subject_id UUID REFERENCES subjects(id),
    status VARCHAR(50), -- ACTIVE, CANCELLED, COMPLETED
    max_students INTEGER DEFAULT 1,
    current_students INTEGER DEFAULT 0,
    date DATE,
    time TIME,
    duration INTEGER, -- in minutes
    zoom_link TEXT,
    attended BOOLEAN DEFAULT FALSE,
    session_notes TEXT,
    meeting_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FEEDBACK table
CREATE TABLE IF NOT EXISTS feedback (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES students(id),
    tutor_id UUID REFERENCES tutors(id),
    session_id UUID REFERENCES tutoring_sessions(id),
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    recommended BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SESSION_MATERIAL table
CREATE TABLE IF NOT EXISTS session_materials (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID REFERENCES tutoring_sessions(id),
    name VARCHAR(255),
    type VARCHAR(50), -- FILE, LIBRARY_LINK
    file_type VARCHAR(50),
    file_size INTEGER,
    url TEXT,
    description TEXT,
    visibility VARCHAR(20), -- PRIVATE, SHARED
    tags TEXT,
    uploaded_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MESSAGE table
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_id UUID REFERENCES users(id),
    receiver_id UUID REFERENCES users(id),
    content TEXT,
    read BOOLEAN DEFAULT FALSE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_students_student_id ON students(student_id);
CREATE INDEX IF NOT EXISTS idx_subjects_code ON subjects(code);
CREATE INDEX IF NOT EXISTS idx_tutors_rating ON tutors(rating);
CREATE INDEX IF NOT EXISTS idx_time_slots_tutor ON time_slots(tutor_id);
CREATE INDEX IF NOT EXISTS idx_tutoring_sessions_date ON tutoring_sessions(date);
CREATE INDEX IF NOT EXISTS idx_feedback_tutor ON feedback(tutor_id);
CREATE INDEX IF NOT EXISTS idx_messages_sender ON messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_messages_receiver ON messages(receiver_id);

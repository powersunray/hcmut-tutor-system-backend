-- JPA-Friendly PostgreSQL Schema for Student Tutoring System
-- Uses SINGLE_TABLE inheritance strategy for User hierarchy

-- Create UUID extension if not exists
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Drop existing tables if they exist (in correct order to handle dependencies)
DROP TABLE IF EXISTS feedback CASCADE;
DROP TABLE IF EXISTS session_materials CASCADE;
DROP TABLE IF EXISTS meeting_notes CASCADE;
DROP TABLE IF EXISTS evaluations CASCADE;
DROP TABLE IF EXISTS tutoring_sessions CASCADE;
DROP TABLE IF EXISTS availabilities CASCADE;
DROP TABLE IF EXISTS support_needs CASCADE;
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS subjects CASCADE;
DROP TABLE IF EXISTS profiles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- PROFILES table (separate from users for clean separation)
CREATE TABLE profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    profile_id VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    campus VARCHAR(100),
    address TEXT,
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- USERS table with SINGLE_TABLE inheritance (includes Student, Tutor, Staff)
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_type VARCHAR(31) NOT NULL, -- Discriminator column: STUDENT, TUTOR, STAFF

    -- Base User fields
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL, -- STUDENT, TUTOR, STAFF
    profile_id UUID REFERENCES profiles(id),

    -- Student-specific fields (NULL for non-students)
    student_id VARCHAR(50) UNIQUE,
    faculty VARCHAR(255),
    major VARCHAR(255),

    -- Tutor-specific fields (NULL for non-tutors)
    tutor_id VARCHAR(50) UNIQUE,
    bio TEXT,
    expertise_areas TEXT, -- Stored as comma-separated or JSON
    average_rating DECIMAL(3, 2) DEFAULT 0.00,
    rating_count INTEGER DEFAULT 0,

    -- Staff-specific fields (NULL for non-staff)
    staff_id VARCHAR(50) UNIQUE,
    staff_role VARCHAR(50), -- MANAGER, COORDINATOR, etc.
    department VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUBJECTS table
CREATE TABLE subjects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    credits INTEGER,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ENROLLMENTS table
CREATE TABLE enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES users(id),
    subject_id UUID REFERENCES subjects(id),
    course_code VARCHAR(20),
    semester VARCHAR(50),
    grade VARCHAR(10),
    enrollment_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUPPORT_NEEDS table
CREATE TABLE support_needs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID REFERENCES users(id),
    support_type VARCHAR(50) NOT NULL, -- TUTORING, ADVISING, SCHOLARSHIP, etc.
    description TEXT,
    status VARCHAR(50), -- PENDING, FULFILLED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- AVAILABILITIES table (tutor time slots)
CREATE TABLE availabilities (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    availability_id VARCHAR(100) UNIQUE NOT NULL,
    tutor_id UUID REFERENCES users(id),
    day_of_week VARCHAR(20) NOT NULL, -- MONDAY, TUESDAY, etc.
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    mode VARCHAR(20) NOT NULL, -- ONLINE, OFFLINE, HYBRID
    location_or_link VARCHAR(500),
    capacity INTEGER DEFAULT 1,
    published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- TUTORING_SESSIONS table
CREATE TABLE tutoring_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id VARCHAR(100) UNIQUE NOT NULL,
    tutor_id UUID REFERENCES users(id),
    student_id UUID REFERENCES users(id),
    title VARCHAR(255),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    mode VARCHAR(20) NOT NULL, -- ONLINE, OFFLINE, HYBRID
    location_or_link VARCHAR(500),
    status VARCHAR(50) NOT NULL, -- PENDING, CONFIRMED, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MEETING_NOTES table
CREATE TABLE meeting_notes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    note_id VARCHAR(100) UNIQUE NOT NULL,
    session_id UUID REFERENCES tutoring_sessions(id),
    content TEXT,
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SESSION_MATERIALS table
CREATE TABLE session_materials (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    material_id VARCHAR(100) UNIQUE NOT NULL,
    session_id UUID REFERENCES tutoring_sessions(id),
    name VARCHAR(255) NOT NULL,
    source_type VARCHAR(50), -- FILE, LIBRARY_LINK, URL
    content_url TEXT,
    visibility VARCHAR(20), -- PRIVATE, SHARED_WITH_STUDENT, PUBLIC
    uploaded_by UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FEEDBACK table (student feedback on sessions)
CREATE TABLE feedback (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    feedback_id VARCHAR(100) UNIQUE NOT NULL,
    session_id UUID REFERENCES tutoring_sessions(id),
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- EVALUATIONS table (tutor/staff evaluation of sessions)
CREATE TABLE evaluations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    evaluation_id VARCHAR(100) UNIQUE NOT NULL,
    session_id UUID REFERENCES tutoring_sessions(id),
    content TEXT,
    evaluator_id UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_type ON users(user_type);
CREATE INDEX idx_users_student_id ON users(student_id);
CREATE INDEX idx_users_tutor_id ON users(tutor_id);
CREATE INDEX idx_users_staff_id ON users(staff_id);
CREATE INDEX idx_subjects_code ON subjects(code);
CREATE INDEX idx_availabilities_tutor ON availabilities(tutor_id);
CREATE INDEX idx_tutoring_sessions_student ON tutoring_sessions(student_id);
CREATE INDEX idx_tutoring_sessions_tutor ON tutoring_sessions(tutor_id);
CREATE INDEX idx_tutoring_sessions_status ON tutoring_sessions(status);
CREATE INDEX idx_feedback_session ON feedback(session_id);

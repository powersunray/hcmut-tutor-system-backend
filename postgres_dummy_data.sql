-- PostgreSQL Dummy Data for Student Tutoring System

-- Insert dummy users
INSERT INTO users (id, name, email, role, phone, avatar) VALUES
(uuid_generate_v4(), 'John Smith', 'john.smith@hcmut.edu.vn', 'STUDENT', '0987654321', NULL),
(uuid_generate_v4(), 'Sarah Johnson', 'sarah.johnson@hcmut.edu.vn', 'TUTOR', '0912345678', NULL),
(uuid_generate_v4(), 'Michael Chen', 'michael.chen@hcmut.edu.vn', 'ADS', '0923456789', NULL),
(uuid_generate_v4(), 'Emma Wilson', 'emma.wilson@hcmut.edu.vn', 'OAA', '0934567890', NULL),
(uuid_generate_v4(), 'David Brown', 'david.brown@hcmut.edu.vn', 'OSA', '0945678901', NULL),
(uuid_generate_v4(), 'Lisa Davis', 'lisa.davis@hcmut.edu.vn', 'STUDENT', '0956789012', NULL),
(uuid_generate_v4(), 'Robert Taylor', 'robert.taylor@hcmut.edu.vn', 'TUTOR', '0967890123', NULL),
(uuid_generate_v4(), 'Maria Garcia', 'maria.garcia@hcmut.edu.vn', 'STUDENT', '0978901234', NULL);

-- Insert dummy students
INSERT INTO students (id, user_id, student_id, major, year, enrollment_date, academic_status) 
SELECT uuid_generate_v4(), id, 
    CASE 
        WHEN name = 'John Smith' THEN '2350123'
        WHEN name = 'Lisa Davis' THEN '2452345'
        WHEN name = 'Maria Garcia' THEN '2256789'
    END,
    CASE 
        WHEN name = 'John Smith' THEN 'Computer Science'
        WHEN name = 'Lisa Davis' THEN 'Electrical Engineering'
        WHEN name = 'Maria Garcia' THEN 'Chemistry Engineering'
    END,
    CASE 
        WHEN name = 'John Smith' THEN 3
        WHEN name = 'Lisa Davis' THEN 2
        WHEN name = 'Maria Garcia' THEN 4
    END,
    '2023-09-01',
    'ACTIVE'
FROM users 
WHERE name IN ('John Smith', 'Lisa Davis', 'Maria Garcia');

-- Insert dummy subjects
INSERT INTO subjects (id, code, name, credits, description) VALUES
(uuid_generate_v4(), 'CO3015', 'Software Testing', 3,
 'Principles and techniques for software testing, including test design, automation, and quality assurance.'),

(uuid_generate_v4(), 'CO3020', 'Principles of Programming Languages', 4,
 'Concepts of programming languages: syntax, semantics, type systems, scoping, and functional paradigms.'),

(uuid_generate_v4(), 'CO3085', 'Natural Language Processing', 3,
 'Models and algorithms for processing human language, including parsing, semantics, and machine learning approaches.'),

(uuid_generate_v4(), 'MT1005', 'Probabilities and Statistics', 4,
 'Introduction to probability, random variables, distributions, and statistical inference.'),

(uuid_generate_v4(), 'MT1003', 'Calculus 1', 4,
 'Single-variable calculus: limits, derivatives, integrals, and applications.');

-- Insert dummy tutors
INSERT INTO tutors (id, user_id, rating, name) 
SELECT uuid_generate_v4(), id, 
    CASE 
        WHEN name = 'Sarah Johnson' THEN 4.5
        WHEN name = 'Robert Taylor' THEN 4.2
    END,
    name
FROM users 
WHERE name IN ('Sarah Johnson', 'Robert Taylor');

-- Insert dummy time slots
INSERT INTO time_slots (id, tutor_id, day, start_time, end_time, mode, location, capacity, is_published) 
SELECT uuid_generate_v4(), 
    (SELECT id FROM tutors WHERE name = 'Sarah Johnson'), 
    'Monday', '09:00', '11:00', 'BOTH', 'Room A101', 5, TRUE;

INSERT INTO time_slots (id, tutor_id, day, start_time, end_time, mode, location, capacity, is_published) 
SELECT uuid_generate_v4(), 
    (SELECT id FROM tutors WHERE name = 'Sarah Johnson'), 
    'Wednesday', '14:00', '16:00', 'ONLINE', 'Zoom Meeting', 3, TRUE;

INSERT INTO time_slots (id, tutor_id, day, start_time, end_time, mode, location, capacity, is_published) 
SELECT uuid_generate_v4(), 
    (SELECT id FROM tutors WHERE name = 'Robert Taylor'), 
    'Tuesday', '10:00', '12:00', 'OFFLINE', 'Room B205', 4, TRUE;

-- Insert dummy tutoring sessions
INSERT INTO tutoring_sessions (id, student_id, tutor_id, subject_id, status, date, time, duration, attended)
SELECT uuid_generate_v4(),
    (SELECT id FROM students WHERE student_id = '2350123'),
    (SELECT id FROM tutors WHERE name = 'Sarah Johnson'),
    (SELECT id FROM subjects WHERE code = 'CO3015'),
    'COMPLETED', '2024-10-15', '09:00', 120, TRUE;

INSERT INTO tutoring_sessions (id, student_id, tutor_id, subject_id, status, date, time, duration, attended)
SELECT uuid_generate_v4(),
    (SELECT id FROM students WHERE student_id = '2452345'),
    (SELECT id FROM tutors WHERE name = 'Robert Taylor'),
    (SELECT id FROM subjects WHERE code = 'CO3020'),
    'ACTIVE', '2024-10-20', '10:30', 90, FALSE;

INSERT INTO tutoring_sessions (id, student_id, tutor_id, subject_id, status, date, time, duration, attended)
SELECT uuid_generate_v4(),
    (SELECT id FROM students WHERE student_id = '2256789'),
    (SELECT id FROM tutors WHERE name = 'Sarah Johnson'),
    (SELECT id FROM subjects WHERE code = 'MT1003'),
    'SCHEDULED', '2024-10-25', '14:00', 60, FALSE;

-- Insert dummy feedback
INSERT INTO feedback (id, student_id, tutor_id, session_id, rating, comment, recommended)
SELECT uuid_generate_v4(),
    (SELECT id FROM students WHERE student_id = '2350123'),
    (SELECT id FROM tutors WHERE name = 'Sarah Johnson'),
    (SELECT id FROM tutoring_sessions WHERE status = 'COMPLETED'),
    4, 'Very helpful session, explained concepts clearly', TRUE;

-- Insert dummy enrollments
INSERT INTO enrollments (id, student_id, subject_id, enrollment_status, enrollment_date, academic_term)
SELECT uuid_generate_v4(),
    s.id,
    sub.id,
    'ENROLLED',
    '2024-09-01',
    'Fall 2024'
FROM students s, subjects sub
WHERE s.student_id = '2350123' AND sub.code = 'CO3015';

INSERT INTO enrollments (id, student_id, subject_id, enrollment_status, enrollment_date, academic_term)
SELECT uuid_generate_v4(),
    s.id,
    sub.id,
    'ENROLLED',
    '2024-09-01',
    'Fall 2024'
FROM students s, subjects sub
WHERE s.student_id = '2452345' AND sub.code = 'CO3020';

INSERT INTO enrollments (id, student_id, subject_id, enrollment_status, enrollment_date, academic_term)
SELECT uuid_generate_v4(),
    s.id,
    sub.id,
    'ENROLLED',
    '2024-09-01',
    'Fall 2024'
FROM students s, subjects sub
WHERE s.student_id = '2256789' AND sub.code = 'MT1003';

-- Insert dummy messages
INSERT INTO messages (id, sender_id, receiver_id, content)
SELECT uuid_generate_v4(),
    (SELECT id FROM users WHERE name = 'John Smith'),
    (SELECT id FROM users WHERE name = 'Sarah Johnson'),
    'Hi Sarah, I would like to book a tutoring session for CO3015.';

INSERT INTO messages (id, sender_id, receiver_id, content)
SELECT uuid_generate_v4(),
    (SELECT id FROM users WHERE name = 'Sarah Johnson'),
    (SELECT id FROM users WHERE name = 'John Smith'),
    'Sure, I have availability on Monday from 9-11 AM. Is that good for you?';

-- Insert dummy support needs
INSERT INTO support_needs (id, student_id, requests_academic_support, additional_needs)
SELECT uuid_generate_v4(),
    s.id,
    TRUE,
    'Need help with programming assignments'
FROM students s
WHERE s.student_id = '2350123';

-- Insert dummy session materials
INSERT INTO session_materials (id, session_id, name, type, file_type, file_size, url, description, visibility, uploaded_by)
SELECT uuid_generate_v4(),
    ts.id,
    'Java Basics Slides',
    'FILE',
    'PDF',
    1024000,
    'https://example.com/java_basics.pdf',
    'Presentation slides for Java programming basics',
    'SHARED',
    u.id
FROM tutoring_sessions ts, users u
WHERE ts.status = 'COMPLETED' AND u.name = 'Sarah Johnson';

-- Insert dummy blackout dates
INSERT INTO blackout_dates (id, tutor_id, date, reason)
SELECT uuid_generate_v4(),
    t.id,
    '2024-11-05',
    'Conference'
FROM tutors t
WHERE t.name = 'Sarah Johnson';

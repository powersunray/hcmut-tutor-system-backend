UPDATE enrollments
SET grade =
    CASE grade
        WHEN 'A'  THEN 9
        WHEN 'A-' THEN 8.5
        WHEN 'B+' THEN 8
        WHEN 'B'  THEN 7
        WHEN 'B-' THEN 6.5
        WHEN 'C+' THEN 6
        WHEN 'C'  THEN 5
        WHEN 'C-' THEN 4.5
        WHEN 'D'  THEN 4
        WHEN 'F'  THEN 0
        ELSE NULL
    END;

ALTER TABLE enrollments
ALTER COLUMN grade TYPE numeric(3,2)
USING grade::numeric;

ALTER TABLE enrollments
ADD CONSTRAINT grade_range CHECK (grade >= 0 AND grade < 10);

INSERT INTO subjects (id, code, name, credits, description) VALUES
(uuid_generate_v4(), 'MT2013', 'Linear Algebra', 3, 'Introduction to vector spaces, matrices and linear transformations'),
(uuid_generate_v4(), 'MT1003', 'Probability Theory', 3, 'Basic concepts in probability and random variables'),
(uuid_generate_v4(), 'PH1003', 'Physics I', 4, 'Classical mechanics and fundamental physics principles'),
(uuid_generate_v4(), 'CH1003', 'General Chemistry', 3, 'Basic chemical principles and molecular structure'),
(uuid_generate_v4(), 'CO2004', 'Computer Networks', 3, 'Principles of network architecture and communication protocols'),
(uuid_generate_v4(), 'CO3005', 'Database Systems', 3, 'Design and implementation of relational database systems'),
(uuid_generate_v4(), 'CO3021', 'Artificial Intelligence', 3, 'Introduction to AI concepts and machine learning');


ALTER TABLE tutoring_sessions
ADD COLUMN subject_id uuid;

ALTER TABLE tutoring_sessions
ADD CONSTRAINT fk_tutoring_sessions_subject
FOREIGN KEY (subject_id)
REFERENCES subjects(id);

INSERT INTO tutoring_sessions
(id, session_id, tutor_id, student_id, subject_id,
 title, start_time, end_time, mode, location_or_link, status)
VALUES
(
    uuid_generate_v4(),
    'session-2',
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    'cccccccc-cccc-cccc-cccc-cccccccccccc',
    'd65709b3-4a4f-4c03-b8cd-19353e9974e5', 
    'CO3001 Homework Help',
    '2024-11-10 14:00:00',
    '2024-11-10 15:30:00',
    'ONLINE',
    'https://zoom.us/homework',
    'SCHEDULED'
);

-- JPA-Friendly Dummy Data for Student Tutoring System

-- Insert profiles first
INSERT INTO profiles (id, profile_id, phone_number, campus, address, gender) VALUES
('11111111-1111-1111-1111-111111111111', 'profile-t1', '0909999999', 'Campus 01', NULL, NULL),
('22222222-2222-2222-2222-222222222222', 'profile-t2', '0911111111', 'Campus 02', NULL, NULL),
('33333333-3333-3333-3333-333333333333', 'profile-s1', '0922222222', 'Campus 01', NULL, NULL),
('44444444-4444-4444-4444-444444444444', 'profile-s2', '0933333333', 'Campus 02', NULL, NULL);

-- Insert tutors
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, tutor_id, bio, expertise_areas, average_rating, rating_count) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'TUTOR', 'Hieu', 'Tran', 'hieu.tran@hcmut.edu.vn', 'TUTOR',
 '11111111-1111-1111-1111-111111111111', 'tutor-1', 'Experienced software engineering tutor',
 'CO3001,Software Engineering', 4.8, 12),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'TUTOR', 'Tam', 'Nguyen', 'tam.nguyen@hcmut.edu.vn', 'TUTOR',
 '22222222-2222-2222-2222-222222222222', 'tutor-2', 'Math and discrete math specialist',
 'CO2013,Discrete Math', 4.2, 7);

-- Insert students
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, student_id, faculty, major) VALUES
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'STUDENT', 'John', 'Doe', 'john.doe@hcmut.edu.vn', 'STUDENT',
 '33333333-3333-3333-3333-333333333333', 'student-1', 'Computer Science', 'Software Engineering'),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'STUDENT', 'Jane', 'Smith', 'jane.smith@hcmut.edu.vn', 'STUDENT',
 '44444444-4444-4444-4444-444444444444', 'student-2', 'Electrical Engineering', 'Electronics');

-- Insert subjects
INSERT INTO subjects (id, code, name, credits, description) VALUES
(uuid_generate_v4(), 'CO3001', 'Software Engineering', 4, 'Principles and practices of software engineering'),
(uuid_generate_v4(), 'CO2013', 'Operating Systems', 4, 'Introduction to operating systems concepts'),
(uuid_generate_v4(), 'CO3015', 'Software Testing', 3, 'Software testing methodologies and practices'),
(uuid_generate_v4(), 'MT1005', 'Calculus', 4, 'Single and multivariable calculus');

-- Insert availabilities for tutors
INSERT INTO availabilities (id, availability_id, tutor_id, day_of_week, start_time, end_time, mode, location_or_link, capacity, published) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'slot-1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'MONDAY', '09:00:00', '10:00:00', 'OFFLINE', 'Room C6-403', 3, true),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'slot-2', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'WEDNESDAY', '19:00:00', '20:00:00', 'ONLINE', 'https://zoom.us/tutor1', 5, true),
('12121212-1212-1212-1212-121212121212', 'slot-3', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'FRIDAY', '14:00:00', '15:00:00', 'HYBRID', 'Room B1-209 & Zoom', 4, true),
('13131313-1313-1313-1313-131313131313', 'slot-4', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'TUESDAY', '08:00:00', '09:00:00', 'OFFLINE', 'Room H1-302', 2, true);

-- Insert a completed session
INSERT INTO tutoring_sessions (id, session_id, tutor_id, student_id, title, start_time, end_time, mode, location_or_link, status) VALUES
('14141414-1414-1414-1414-141414141414', 'session-1', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'CO3001 Midterm Review',
 '2024-11-01 09:00:00', '2024-11-01 10:00:00', 'OFFLINE', 'Room C6-403', 'COMPLETED');

-- Insert feedback for the completed session
INSERT INTO feedback (id, feedback_id, session_id, rating, comment) VALUES
('15151515-1515-1515-1515-151515151515', 'feedback-1', '14141414-1414-1414-1414-141414141414',
 5, 'Excellent session! Very helpful explanations.');

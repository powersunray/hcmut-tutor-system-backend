-- JPA-Friendly Dummy Data for Student Tutoring System

-- Insert profiles with SAME UUID as corresponding users
INSERT INTO profiles (id, profile_id, phone_number, campus, address, gender) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'profile-t1', '0909999999', 'Campus 01', NULL, NULL),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'profile-t2', '0911111111', 'Campus 02', NULL, NULL),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'profile-s1', '0922222222', 'Campus 01', NULL, NULL),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'profile-s2', '0933333333', 'Campus 02', NULL, NULL);

-- Insert tutors
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, tutor_id, bio, expertise_areas, average_rating, rating_count) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'TUTOR', 'Hieu', 'Tran', 'hieu.tran@hcmut.edu.vn', 'TUTOR',
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'tutor-1', 'Experienced software engineering tutor',
 'CO3001,Software Engineering', 4.8, 12),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'TUTOR', 'Tam', 'Nguyen', 'tam.nguyen@hcmut.edu.vn', 'TUTOR',
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'tutor-2', 'Math and discrete math specialist',
 'CO2013,Discrete Math', 4.2, 7);

-- Insert students (student_id must be 7 digits per ValidationUtil)
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, student_id, faculty, major) VALUES
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'STUDENT', 'John', 'Doe', 'john.doe@hcmut.edu.vn', 'STUDENT',
 'cccccccc-cccc-cccc-cccc-cccccccccccc', '2110001', 'Computer Science', 'Software Engineering'),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'STUDENT', 'Jane', 'Smith', 'jane.smith@hcmut.edu.vn', 'STUDENT',
 'dddddddd-dddd-dddd-dddd-dddddddddddd', '2110002', 'Electrical Engineering', 'Electronics');

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

-- ============================================
-- ADDITIONAL TEST DATA FOR ENROLLMENT, SUPPORT NEEDS, AND STAFF
-- ============================================

-- Insert additional profiles for more students and staff (SAME UUID as users)
INSERT INTO profiles (id, profile_id, phone_number, campus, address, gender) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'profile-s3', '0944444444', 'Campus 01', '123 Le Loi St, District 1', 'MALE'),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'profile-s4', '0955555555', 'Campus 02', '456 Nguyen Hue St, District 1', 'FEMALE'),
('10101010-1010-1010-1010-101010101010', 'profile-s5', '0966666666', 'Campus 01', '789 Tran Hung Dao St, District 5', 'MALE'),
('20202020-2020-2020-2020-202020202020', 'profile-staff1', '0977777777', 'Campus 01', NULL, 'FEMALE'),
('30303030-3030-3030-3030-303030303030', 'profile-staff2', '0988888888', 'Campus 02', NULL, 'MALE'),
('40404040-4040-4040-4040-404040404040', 'profile-staff3', '0999999999', 'Campus 01', NULL, 'FEMALE');

-- Insert additional students (total 5 students for testing)
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, student_id, faculty, major) VALUES
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'STUDENT', 'Michael', 'Johnson', 'michael.johnson@hcmut.edu.vn', 'STUDENT',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '2110003', 'Computer Science', 'Software Engineering'),
('ffffffff-ffff-ffff-ffff-ffffffffffff', 'STUDENT', 'Emily', 'Williams', 'emily.williams@hcmut.edu.vn', 'STUDENT',
 'ffffffff-ffff-ffff-ffff-ffffffffffff', '2110004', 'Electrical Engineering', 'Computer Engineering'),
('10101010-1010-1010-1010-101010101010', 'STUDENT', 'David', 'Brown', 'david.brown@hcmut.edu.vn', 'STUDENT',
 '10101010-1010-1010-1010-101010101010', '2110005', 'Mechanical Engineering', 'Mechatronics');

-- Insert staff members (3 staff with different roles)
INSERT INTO users (id, user_type, first_name, last_name, email, role, profile_id, staff_id, staff_role, department) VALUES
('20202020-2020-2020-2020-202020202020', 'STAFF', 'Sarah', 'Martinez', 'sarah.martinez@hcmut.edu.vn', 'STAFF',
 '20202020-2020-2020-2020-202020202020', 'staff-001', 'ADS', 'Academic Development Services'),
('30303030-3030-3030-3030-303030303030', 'STAFF', 'Robert', 'Garcia', 'robert.garcia@hcmut.edu.vn', 'STAFF',
 '30303030-3030-3030-3030-303030303030', 'staff-002', 'OAA', 'Office of Academic Affairs'),
('40404040-4040-4040-4040-404040404040', 'STAFF', 'Linda', 'Rodriguez', 'linda.rodriguez@hcmut.edu.vn', 'STAFF',
 '40404040-4040-4040-4040-404040404040', 'staff-003', 'OSA', 'Office of Student Affairs');

-- Insert enrollments (testing various scenarios)
-- Note: Semester format is YYS where YY=year last 2 digits, S=1,2,3
-- Course code format: 2 letters + 4 digits (e.g., CO3001)
INSERT INTO enrollments (id, student_id, subject_id, course_code, semester, grade, enrollment_status) VALUES
-- Student 2110001 (John Doe) enrollments
('50505050-5050-5050-5050-505050505050',
 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 (SELECT id FROM subjects WHERE code = 'CO3001'),
 'CO3001', '241', 'A', 'COMPLETED'),

('51515151-5151-5151-5151-515151515151',
 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 (SELECT id FROM subjects WHERE code = 'CO2013'),
 'CO2013', '242', 'B+', 'COMPLETED'),

('52525252-5252-5252-5252-525252525252',
 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 (SELECT id FROM subjects WHERE code = 'CO3015'),
 'CO3015', '251', NULL, 'ACTIVE'),

-- Student 2110002 (Jane Smith) enrollments
('53535353-5353-5353-5353-535353535353',
 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 (SELECT id FROM subjects WHERE code = 'CO2013'),
 'CO2013', '241', 'A', 'COMPLETED'),

('54545454-5454-5454-5454-545454545454',
 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 (SELECT id FROM subjects WHERE code = 'MT1005'),
 'MT1005', '242', 'B', 'COMPLETED'),

('55555555-5555-5555-5555-000000000001',
 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 (SELECT id FROM subjects WHERE code = 'CO3001'),
 'CO3001', '251', NULL, 'ACTIVE'),

-- Student 2110003 (Michael Johnson) enrollments
('56565656-5656-5656-5656-565656565656',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
 (SELECT id FROM subjects WHERE code = 'CO3001'),
 'CO3001', '241', 'C+', 'COMPLETED'),

('57575757-5757-5757-5757-575757575757',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
 (SELECT id FROM subjects WHERE code = 'CO3015'),
 'CO3015', '251', NULL, 'ACTIVE'),

-- Student 2110004 (Emily Williams) enrollments
('58585858-5858-5858-5858-585858585858',
 'ffffffff-ffff-ffff-ffff-ffffffffffff',
 (SELECT id FROM subjects WHERE code = 'MT1005'),
 'MT1005', '241', 'A', 'COMPLETED'),

('59595959-5959-5959-5959-595959595959',
 'ffffffff-ffff-ffff-ffff-ffffffffffff',
 (SELECT id FROM subjects WHERE code = 'CO2013'),
 'CO2013', '251', NULL, 'ACTIVE'),

-- Student 2110005 (David Brown) enrollments
('60606060-6060-6060-6060-606060606060',
 '10101010-1010-1010-1010-101010101010',
 (SELECT id FROM subjects WHERE code = 'MT1005'),
 'MT1005', '242', 'B+', 'COMPLETED'),

('61616161-6161-6161-6161-616161616161',
 '10101010-1010-1010-1010-101010101010',
 (SELECT id FROM subjects WHERE code = 'CO3001'),
 'CO3001', '251', NULL, 'ACTIVE');

-- Insert support needs (testing various types and statuses)
INSERT INTO support_needs (id, student_id, support_type, description, status) VALUES
-- PENDING support needs
('70707070-7070-7070-7070-707070707070',
 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 'ACADEMIC_HELP',
 'Need help understanding advanced algorithms and data structures for CO3001',
 'PENDING'),

('71717171-7171-7171-7171-717171717171',
 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 'SCHOLARSHIP',
 'Applying for merit-based scholarship for excellent academic performance',
 'PENDING'),

('72727272-7272-7272-7272-727272727272',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
 'ADVISING',
 'Need guidance on course selection for next semester and career planning',
 'PENDING'),

-- FULFILLED support needs
('73737373-7373-7373-7373-737373737373',
 'ffffffff-ffff-ffff-ffff-ffffffffffff',
 'ACADEMIC_HELP',
 'Requested tutoring for Calculus - successfully matched with a tutor',
 'FULFILLED'),

('74747474-7474-7474-7474-747474747474',
 '10101010-1010-1010-1010-101010101010',
 'ADVISING',
 'Consultation about switching majors - met with academic advisor',
 'FULFILLED'),

-- CANCELLED support needs
('75757575-7575-7575-7575-757575757575',
 'cccccccc-cccc-cccc-cccc-cccccccccccc',
 'SCHOLARSHIP',
 'Financial aid application - no longer needed due to family support',
 'CANCELLED'),

-- Additional PENDING support needs for testing
('76767676-7676-7676-7676-767676767676',
 'dddddddd-dddd-dddd-dddd-dddddddddddd',
 'ACADEMIC_HELP',
 'Struggling with Operating Systems concepts, need tutoring sessions',
 'PENDING'),

('77777777-7777-7777-7777-000000000001',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
 'SCHOLARSHIP',
 'Applying for need-based financial assistance for tuition fees',
 'PENDING'),

('78787878-7878-7878-7878-787878787878',
 'ffffffff-ffff-ffff-ffff-ffffffffffff',
 'ADVISING',
 'Want to discuss internship opportunities and industry connections',
 'PENDING');

-- ============================================
-- TEST DATA SUMMARY
-- ============================================
-- PROFILES: 9 total (2 tutors, 5 students, 3 staff)
-- USERS:
--   - Tutors: 2 (tutor-1, tutor-2)
--   - Students: 5 (student IDs: 2110001, 2110002, 2110003, 2110004, 2110005)
--   - Staff: 3 (staff-001: ADS, staff-002: OAA, staff-003: OSA)
-- SUBJECTS: 4 (CO3001, CO2013, CO3015, MT1005)
-- ENROLLMENTS: 12 total
--   - COMPLETED: 6 (with grades)
--   - ACTIVE: 6 (current semester, no grades yet)
--   - Semesters: 241, 242, 251 (testing multiple semesters)
-- SUPPORT NEEDS: 9 total
--   - PENDING: 6 (3x ACADEMIC_HELP, 2x SCHOLARSHIP, 1x ADVISING)
--   - FULFILLED: 2
--   - CANCELLED: 1
--   - Types: ACADEMIC_HELP, SCHOLARSHIP, ADVISING
-- AVAILABILITIES: 4 (for tutor-1 and tutor-2)
-- TUTORING SESSIONS: 1 COMPLETED session
-- FEEDBACK: 1 (for the completed session)

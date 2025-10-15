sql

SELECT
    Student.name,
    Student.age,
    Faculty.name AS faculty_name,
    Faculty.color AS faculty_color
FROM Student
INNER JOIN Faculty ON Student.faculty_id = Faculty.id;

SELECT
    Student.name,
    Student.age,
    Faculty.name AS faculty_name,
    Faculty.color AS faculty_color
FROM Student
INNER JOIN Faculty ON Student.faculty_id = Faculty.id
WHERE Student.avatar IS NOT NULL;
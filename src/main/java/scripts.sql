sql

SELECT * FROM Student WHERE age BETWEEN 10 AND 20;
SELECT name FROM Student;
SELECT * FROM Student WHERE LOWER(name) LIKE '%о%';
SELECT * FROM Student WHERE age < id;
SELECT * FROM Student ORDER BY age;
SELECT COUNT(*) FROM Student;
SELECT AVG(age) from Student;
SELECT * FROM Student ORDER BY id DESC LIMIT 5;


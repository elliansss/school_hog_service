sql

ALTER TABLE Faculty DROP COLUMN colour;

ALTER TABLE Student
ADD CONSTRAINT age_constraint CHECK (age >= 16);

ALTER TABLE Student
ALTER COLUMN name SET NOT NULL;

ALTER TABLE Student
ADD CONSTRAINT unique_name UNIQUE (name);

ALTER TABLE Student
ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE Faculty
ADD CONSTRAINT faculty_name_color UNIQUE (name, color);
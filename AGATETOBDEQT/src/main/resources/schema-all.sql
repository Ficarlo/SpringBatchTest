--Spring Boot runs schema-@@platform@@.sql automatically during startup. -all is the default for all platforms.

DROP TABLE people IF EXISTS;

CREATE TABLE people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

INSERT INTO people (person_id,first_name,last_name) values(1,'Samuel','FUCHS');
INSERT INTO people (person_id,first_name,last_name) values(2,'Solenn','PACK');
INSERT INTO people (person_id,first_name,last_name) values(3,'John','DOE');


DROP TABLE log IF EXISTS;

CREATE TABLE log (
log_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
person_id BIGINT NOT NULL
);

DROP DATABASE IF EXISTS dbms_proj;

CREATE DATABASE IF NOT EXISTS dbms_proj;
USE dbms_proj;

-- USER TABLE
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    username varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    phone varchar(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- DATA FOR USER TABLE
INSERT INTO user (username, password, first_name, last_name, email, phone)
VALUES 
('abcd','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','abcd','efgh','abcd@gmail.com', '3434332323'),
('xyz','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','xyz','zyx','xyz@gmail.com', '3234234233'),
('mnop','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','mnop','qrst','mnop@gmail.com', '6345344432');

-- TABLE FOR USER ROLES
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- DATA FOR ROLES
INSERT INTO role (name)
VALUES
('ROLE_USER'), ('ROLE_ADMIN');

-- USER_ROLES TABLE CONNECTING ROLES AND USERS (MANY TO MANY)
DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role (
    user_id int NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    KEY FK_ROLE_idx (role_id),
    
    CONSTRAINT FK_USER_05 FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
    REFERENCES role (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- SET FOREIGN_KEY_CHECKS = 1;

-- DATA FOR user_role TABLE
INSERT INTO user_role (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1)
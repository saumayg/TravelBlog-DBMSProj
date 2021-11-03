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
    id int NOT NULL UNIQUE AUTO_INCREMENT,
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

-- DATA FOR user_role TABLE
INSERT INTO user_role (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1);

-- POST TABLE 
-- (CONNECTED TO USER BY A MANY TO ONE RELATIONSHIP (MANY POSTS UNDER ONE USER))
CREATE TABLE post (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    body text NOT NULL,
    user_id int DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by int NOT NULL,
    updated_by int NOT NULL,
    PRIMARY KEY (id),
    KEY FK_USER_POST (user_id),

    CONSTRAINT FK_USER_POST FOREIGN KEY (user_id) 
    REFERENCES user (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- DATA FOR post TABLE
INSERT INTO post (title, body, user_id, created_at, updated_at, created_by, updated_by)
VALUES
('title1', 'body1', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 3),
('title2', 'body2', 2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 2),
('title3', 'body3', 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 3),
('title4', 'body4', 3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 1),
('title5', 'body5', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 1);

-- TAGS TABLE
CREATE TABLE tag (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    description text DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by int NOT NULL,
    updated_by int NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- DATA FOR tag TABLE
INSERT INTO tag (name, description, created_at, updated_at, created_by, updated_by)
VALUES
('tag1', 'description1', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 1),
('tag2', 'description2', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 1),
('tag3', 'description3', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 3);

-- POST TAG TABLE CONNECTING POST AND TAG
-- (MANY TO MANY RELATIONSHIP)
CREATE TABLE post_tag (
    post_id int NOT NULL,
    tag_id int NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    KEY FK_TAG_idx (tag_id),
    
    CONSTRAINT FK_POST_05 FOREIGN KEY (post_id)
    REFERENCES post (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT FK_TAG FOREIGN KEY (tag_id)
    REFERENCES tag (id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- DATA for post_tag TABLE
INSERT INTO post_tag (post_id, tag_id) 
VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2),
(2, 3),
(3, 2),
(4, 2),
(4, 3),
(5, 1);
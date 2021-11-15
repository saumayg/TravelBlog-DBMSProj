-- DROP DATABASE IF EXISTS dbms_proj;

-- CREATE DATABASE IF NOT EXISTS dbms_proj;
USE heroku_01022fbe580f378;

SET FOREIGN_KEY_CHECKS = 0;

-- Album table
DROP TABLE IF EXISTS album;

CREATE TABLE album (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    name varchar(64) DEFAULT NULL,
    description text DEFAULT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    user_id int DEFAULT NULL,
    post_id int DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Image table
DROP TABLE IF EXISTS photo;

CREATE TABLE photo (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    name varchar(64) DEFAULT NULL,
    album_id int DEFAULT NULL, -- many to one relationship (many photos for one album)
    user_id int DEFAULT NULL, -- one to one relationship
    PRIMARY KEY (id),
    KEY FK_PHOTO_ALBUM (album_id),

    CONSTRAINT FK_PHOTO_ALBUM FOREIGN KEY (album_id)
    REFERENCES album (id)
    ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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

-- Adding foreign key references
ALTER TABLE album ADD FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE photo ADD FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

-- TABLE FOR USER ROLES
DROP TABLE IF EXISTS role;

CREATE TABLE role (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    name varchar(255) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- USER_ROLES TABLE CONNECTING ROLES AND USERS (MANY TO MANY)
DROP TABLE IF EXISTS user_role;

CREATE TABLE user_role (
    user_id int NOT NULL, -- many to many relationship
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    KEY FK_ROLE_idx (role_id),
    
    CONSTRAINT FK_USER_05 FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE CASCADE ON UPDATE NO ACTION,

    CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
    REFERENCES role (id)
    ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- POST TABLE 
-- (CONNECTED TO USER BY A MANY TO ONE RELATIONSHIP (MANY POSTS UNDER ONE USER))
DROP TABLE IF EXISTS post;

CREATE TABLE post (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    description text NOT NULL,
    body text NOT NULL,
    user_id int DEFAULT NULL, -- many to one relationship (many post for one user)
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY FK_POST_USER (user_id),

    CONSTRAINT FK_POST_USER FOREIGN KEY (user_id) 
    REFERENCES user (id)
    ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Adding foreign key references
ALTER TABLE album ADD FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE;

-- TAGS TABLE
DROP TABLE IF EXISTS tag;

CREATE TABLE tag (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    description text DEFAULT NULL,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- POST TAG TABLE CONNECTING POST AND TAG
-- (MANY TO MANY RELATIONSHIP)
DROP TABLE IF EXISTS post_tag;

CREATE TABLE post_tag (
    post_id int NOT NULL,
    tag_id int NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    KEY FK_TAG_idx (tag_id),
    
    CONSTRAINT FK_POST_05 FOREIGN KEY (post_id)
    REFERENCES post (id)
    ON DELETE CASCADE ON UPDATE NO ACTION,

    CONSTRAINT FK_TAG FOREIGN KEY (tag_id)
    REFERENCES tag (id)
    ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Comment table
DROP TABLE IF EXISTS comment;

CREATE TABLE comment (
    id int NOT NULL UNIQUE AUTO_INCREMENT,
    body text NOT NULL,
    post_id int DEFAULT NULL, -- many to one relationship (many comments for one post)
    user_id int DEFAULT NULL, -- many to one relationship (many comments for one user)
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY Key (id),
    KEY FK_COMMENT_POST (post_id),
    KEY FK_COMMENT_USER (user_id),

    CONSTRAINT FK_COMMENT_POST FOREIGN KEY (post_id)
    REFERENCES post (id)
    ON DELETE CASCADE,
    CONSTRAINT FK_COMMENT_USER FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
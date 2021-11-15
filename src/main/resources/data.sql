-- USE heroku_01022fbe580f378;

-- DATA FOR USER TABLE
DELETE FROM user;

INSERT INTO user (username, password, first_name, last_name, email, phone)
VALUES 
('abcd','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','abcd','efgh','abcd@gmail.com', '3434332323'),
('saumayg','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','Saumay','Gupta','saumayg@gmail.com', '3234234233'),
('mnop','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','mnop','qrst','mnop@gmail.com', '6345344432');

-- DATA FOR post TABLE
DELETE FROM post;

INSERT INTO post (title, description, body, user_id, created_at)
VALUES
('title1', 'description1', 'body1', 2, CURRENT_TIMESTAMP()),
('title2', 'description2', 'body2', 2, CURRENT_TIMESTAMP()),
('title3', 'description3', 'body3', 3, CURRENT_TIMESTAMP()),
('title4', 'description4', 'body4', 3, CURRENT_TIMESTAMP()),
('title5', 'description5', 'body5', 1, CURRENT_TIMESTAMP());

-- DATA FOR ALBUM TABLE
DELETE FROM album;

INSERT INTO album (name, description, created_at, user_id, post_id)
VALUES
('Album1', 'Description1', CURRENT_TIMESTAMP(), 1, 5),
('Album2', 'Description2', CURRENT_TIMESTAMP(), 2, 1),
('Album3', 'Description3', CURRENT_TIMESTAMP(), 3, 3),
('Album4', 'Description4', CURRENT_TIMESTAMP(), 1, NULL),
('Album5', 'Description5', CURRENT_TIMESTAMP(), 2, 2),
('Album6', 'Description6', CURRENT_TIMESTAMP(), 3, 4),
('Album7', 'Description7', CURRENT_TIMESTAMP(), 1, NULL),
('Album8', 'Description8', CURRENT_TIMESTAMP(), 2, NULL),
('Album9', 'Description9', CURRENT_TIMESTAMP(), 3, NULL),
('Album10', 'Description10', CURRENT_TIMESTAMP(), 1, NULL);

-- DATA FOR ROLES
DELETE FROM role;

INSERT INTO role (name)
VALUES
('ROLE_USER'), ('ROLE_ADMIN');

-- DATA FOR user_role TABLE
DELETE FROM user_role;

INSERT INTO user_role (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1);

-- DATA FOR tag TABLE
DELETE FROM tag;

INSERT INTO tag (name, description, created_at)
VALUES
('tag1', 'description1', CURRENT_TIMESTAMP()),
('tag2', 'description2', CURRENT_TIMESTAMP()),
('tag3', 'description3', CURRENT_TIMESTAMP());

-- DATA for post_tag TABLE
DELETE FROM post_tag;

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

-- DATA FOR comment TABLE
DELETE FROM comment;

INSERT INTO comment (body, post_id, user_id, created_at)
VALUES
('bod1', 1, 1, CURRENT_TIMESTAMP()),
('bod2', 2, 1, CURRENT_TIMESTAMP()),
('bod3', 3, 1, CURRENT_TIMESTAMP()),
('bod4', 4, 1, CURRENT_TIMESTAMP()),
('bod5', 5, 1, CURRENT_TIMESTAMP()),
('bod6', 2, 2, CURRENT_TIMESTAMP()),
('bod7', 4, 2, CURRENT_TIMESTAMP()),
('bod8', 3, 2, CURRENT_TIMESTAMP()),
('bod9', 1, 3, CURRENT_TIMESTAMP()),
('bod10', 2, 3, CURRENT_TIMESTAMP()),
('bod11', 5, 3, CURRENT_TIMESTAMP());
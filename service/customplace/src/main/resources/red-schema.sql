CREATE TABLE markers (
                         id VARCHAR(255) NOT NULL PRIMARY KEY,
                         user_id VARCHAR(255),
                         title VARCHAR(255),
                         address VARCHAR(255),
                         description LONGTEXT,
                         date DATETIME,
                         latitude DOUBLE,
                         longitude DOUBLE,
                         color VARCHAR(255),
                         score INT
);


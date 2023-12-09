DROP TABLE IF EXISTS `user` CASCADE;
CREATE TABLE user (
                      id bigint(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255),
                      username VARCHAR(255),
                      password VARCHAR(255),
                      userimagepath VARCHAR(1023) DEFAULT '/uploads/autouser.jpg',

                    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS `food`;
CREATE TABLE food (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      foodname VARCHAR(255),
                      description VARCHAR(255),
                      imagepath VARCHAR(255) DEFAULT '/uploads/autofood.jpg',
                      userid BIGINT default 0,
                      FOREIGN KEY (userid) REFERENCES user(id)
);

DROP TABLE IF EXISTS `message`;
CREATE TABLE message (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      content VARCHAR(1023),
                      time VARCHAR(255),
                      sendid BIGINT default 0,
                      sendname VARCHAR(255),
                      acceptid BIGINT default 0,
                      acceptname VARCHAR(255)
);
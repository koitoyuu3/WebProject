DROP TABLE IF EXISTS `user` CASCADE;
CREATE TABLE user (
                      id bigint(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255),
                      username VARCHAR(255),
                      password VARCHAR(255),
                      userimagepath VARCHAR(1023) DEFAULT '00000',

                    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS `food`;
CREATE TABLE food (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      foodname VARCHAR(255),
                      description VARCHAR(255),
                      imagepath VARCHAR(255) DEFAULT '00000',
                      userid BIGINT default 0,
                      FOREIGN KEY (userid) REFERENCES user(id)
);

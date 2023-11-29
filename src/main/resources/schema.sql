DROP TABLE IF EXISTS `user` CASCADE;
CREATE TABLE user (
                      id bigint(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255),
                      area VARCHAR(255),
                      username VARCHAR(255),
                      password VARCHAR(255),
                    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS `food`;
CREATE TABLE food (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      foodName VARCHAR(255),
                      description VARCHAR(255),
                      userId BIGINT,
                      FOREIGN KEY (userId) REFERENCES user(id)
);

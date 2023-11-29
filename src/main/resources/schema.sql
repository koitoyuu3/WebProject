DROP TABLE IF EXISTS `user` CASCADE;
CREATE TABLE user (
                      id bigint(20) NOT NULL AUTO_INCREMENT,
                      name VARCHAR(255),
                      username VARCHAR(255),
                      password VARCHAR(255),
                      user_image_path VARCHAR(255) DEFAULT '00000',

                    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS `food`;
CREATE TABLE food (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      food_name VARCHAR(255),
                      description VARCHAR(255),
                      image_path VARCHAR(255) DEFAULT NULL,
                      user_id BIGINT,
                      FOREIGN KEY (user_id) REFERENCES user(id)
);

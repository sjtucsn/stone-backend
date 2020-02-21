CREATE DATABASE IF NOT EXISTS fanggu;
USE fanggu;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS uploaded_content;
DROP TABLE IF EXISTS published_content;

CREATE TABLE user (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(20) NOT NULL,
    user_tel CHAR(11) NOT NULL,
    user_avatar VARCHAR(30),
    user_address VARCHAR(30),
    user_gender TINYINT(1) UNSIGNED, /* 1 男 0 女 */
    password CHAR(64), /* SHA-256算法加密的密码 */
    user_type TINYINT(1) UNSIGNED, /* 1 方古管理员 0 普通用户 */
    create_time DATETIME DEFAULT NOW(),
    update_time TIMESTAMP
);

INSERT INTO user(user_name, user_tel, user_gender, password, user_type) VALUES ('admin', '18202121916', 1, SHA2('123456', 256), 1);

CREATE TABLE category (
    category_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(20) NOT NULL,
    create_time DATETIME DEFAULT NOW(),
    update_time TIMESTAMP,
    memo VARCHAR(255)
);

CREATE TABLE uploaded_content (
    uploaded_content_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_id INT UNSIGNED NOT NULL,
    uploader_id INT UNSIGNED NOT NULL,
    title VARCHAR(30),
    content VARCHAR(256),
    image_path VARCHAR(30),
    position VARCHAR(30),
    create_time DATETIME DEFAULT NOW(),
    update_time TIMESTAMP,
    INDEX(category_id),
    INDEX(uploader_id)
);

CREATE TABLE published_content (
    published_content_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    category_id INT UNSIGNED NOT NULL,
    publisher_id INT UNSIGNED NOT NULL,
    title VARCHAR(30),
    content TEXT,
    image_path VARCHAR(30),
    position VARCHAR(30),
    create_time DATETIME DEFAULT NOW(),
    update_time TIMESTAMP,
    INDEX(category_id),
    INDEX(publisher_id)
)
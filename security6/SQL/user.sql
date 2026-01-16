-- Active: 1767915726149@@127.0.0.1@3306@aloha
-- 데이터베이스 생성

CREATE DATABASE IF NOT EXISTS `aloha`;

USE `aloha`;

-- 외래키 무시
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `user`;
-- 게시판 테이블 생성
CREATE TABLE `user` (
  `NO` bigint NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(100) NOT NULL,
  `PASSWORD` varchar(200) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `EMAIL` varchar(200) DEFAULT NULL,
  `CREATED_AT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_AT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ENABLED` int DEFAULT 1,
  PRIMARY KEY (`NO`)
) COMMENT='회원';

-- 샘플 데이터
TRUNCATE Table `user`;

INSERT INTO `user` (USERNAME, PASSWORD, NAME, EMAIL)
VALUES 
  ( '회원1', '000001', '김가갸', 'user1@test.com'),
  ( '회원2', '000002', '김거겨', 'user2@test.com'),
  ( '회원3', '000003', '김고교', 'user3@test.com'),
  ( '회원4', '000004', '김구규', 'user4@test.com'),
  ( '회원5', '000005', '김그기', 'user5@test.com')
;

-- 외래키 활성화
SET FOREIGN_KEY_CHECKS=1;

SELECT * FROM `user`;
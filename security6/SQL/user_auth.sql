-- Active: 1767915726149@@127.0.0.1@3306@aloha
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS `aloha`;

-- 외래키 무시
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `user`;

-- 게시판 테이블 생성
CREATE TABLE `user_auth` (
      no bigint NOT NULL AUTO_INCREMENT       -- 권한번호
    , username varchar(100) NOT NULL             -- 아이디
    , auth varchar(100) NOT NULL                -- 권한 (ROLE_USER, ROLE_ADMIN, ...)
    , PRIMARY KEY(no)                      
);

SET FOREIGN_KEY_CHECKS=1;

SELECT * FROM `user_auth`;
package com.aloha.security6.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aloha.security6.dto.UserAuth;
import com.aloha.security6.dto.Users;

@Mapper
public interface UserMapper {

    // 회원 조회
    public Users select(@Param("username") String username) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

}
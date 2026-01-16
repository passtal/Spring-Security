package com.aloha.security6.service;

import com.aloha.security6.dto.UserAuth;
import com.aloha.security6.dto.Users;

public interface UserService {
    
    // 조회
    public Users select(String username) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

}
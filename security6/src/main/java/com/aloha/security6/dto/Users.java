package com.aloha.security6.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Users {
    private Long no;
    private String username;
    private String password;
    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private int enabled;

    // ⭐ XML의 collection property="authList"와 매핑될 필드 추가
    private List<UserAuth> authList; 
}
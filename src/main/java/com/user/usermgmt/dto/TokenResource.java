package com.user.usermgmt.dto;

import lombok.Data;

@Data
public class TokenResource {
    public boolean upgraded;
    public String access_token;
    private Integer expires_in;
    private Integer refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private Integer notBeforePolicy;
}

package com.user.usermgmt.service;

import com.user.usermgmt.dto.TokenResource;
import org.json.JSONObject;

public interface IKeycloak {
    TokenResource getTokenWithResourceServer(String accessToken, String clientId, String audience);
    JSONObject parseAccessToken(String accessToken);
}

package com.user.usermgmt.service;

import com.user.usermgmt.dto.TokenResource;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
public class KeycloakService implements IKeycloak {

    @Override
    public TokenResource getTokenWithResourceServer(String accessToken, String clientId, String audience) {
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket");
        formData.add("audience", audience);

        return restClient.post()
                .uri("http://localhost:8080/realms/nizar-realm/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(formData)
                .retrieve()
                .body(TokenResource.class);
    }

    @Override
    public JSONObject parseAccessToken(String accessToken) {
        String[] parts = accessToken.split("\\.");
        JSONObject payload = new JSONObject(new String(Base64.getUrlDecoder().decode(parts[1])));
        String preferredUsername = payload.getString("preferred_username");
        System.out.println("preferredUsername:"+preferredUsername);
        return null;
    }
}

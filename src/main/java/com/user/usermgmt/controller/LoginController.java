package com.user.usermgmt.controller;

import com.user.usermgmt.dto.Token;
import com.user.usermgmt.dto.TokenResource;
import com.user.usermgmt.service.KeycloakService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final KeycloakService keycloakService;

    public LoginController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResource> login(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("client_id") String clientId,
                                       @RequestParam("grant_type") String grantType,
                                       @RequestParam("client_secret") String clientSecret) {
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);
        formData.add("client_id", clientId);
        formData.add("grant_type", grantType);
        formData.add("client_secret", clientSecret);

        Token tokenResult = restClient.post()
                .uri("http://localhost:8080/realms/nizar-realm/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Token.class);
        TokenResource tokenWithResourceServer = keycloakService.getTokenWithResourceServer(tokenResult.getAccess_token(), clientId, clientId);

        return new ResponseEntity<>(tokenWithResourceServer, HttpStatus.OK);
    }

    @GetMapping("/getRoleAndPermissions")
    public ResponseEntity<TokenResource> getRoleAndPermissions(@RequestParam("accessToken") String accessToken){
        keycloakService.parseAccessToken(accessToken);
        return null;
    }
}

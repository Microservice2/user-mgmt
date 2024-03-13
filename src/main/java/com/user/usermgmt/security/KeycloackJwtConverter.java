package com.user.usermgmt.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import com.user.usermgmt.constants.KeycloakConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloackJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtConverterProperties properties;

    public KeycloackJwtConverter(JwtConverterProperties properties) {
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        LinkedTreeMap<String, List<String>> resourceAccess = jwt.getClaim("realm_access");
        String preferredUsername = jwt.getClaim("preferred_username");
        LinkedTreeMap<String, List<LinkedTreeMap<String, Object>>> authorization = jwt.getClaim("authorization");
        List<LinkedTreeMap<String, Object>> permissions = authorization.get("permissions");
        Set<SimpleGrantedAuthority> rolesPrivileges = new HashSet<>();

        permissions.forEach(stringObjectLinkedTreeMap -> {
            if (stringObjectLinkedTreeMap.get("rsname") instanceof String rsName) {
                List<String> scopes = (List<String>) stringObjectLinkedTreeMap.get("scopes");

                if (rsName.startsWith("admin") && preferredUsername.equals(KeycloakConstant.USER_ADMIN)) {
                    scopes.forEach((scope) -> {
                        rolesPrivileges.add(new SimpleGrantedAuthority(KeycloakConstant.SCOPE_ADMIN + scope));
                    });
                }else if (rsName.startsWith("normal") && preferredUsername.equals(KeycloakConstant.USER_NORMAL)) {
                    if (scopes != null) {
                        scopes.forEach((scope) -> {
                            rolesPrivileges.add(new SimpleGrantedAuthority(KeycloakConstant.SCOPE_NORMAL + scope));
                        });
                    }
                }
            }

        });

        if (jwt.getClaim("realm_access") == null) {
            return Set.of();
        }

        Map<String, Object> resource;
        List<String> resourceAccessRoles = resourceAccess.get("roles");
        resourceAccessRoles.forEach(role -> rolesPrivileges.add(new SimpleGrantedAuthority(KeycloakConstant.ROLE + role)));

        /*resourceAccessRoles.stream()
                .map(role -> new SimpleGrantedAuthority(KeycloakConstant.ROLE + role))
                .collect(Collectors.toSet());*/


        return rolesPrivileges;
    }
}

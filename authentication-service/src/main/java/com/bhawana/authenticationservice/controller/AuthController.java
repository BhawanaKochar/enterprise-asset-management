package com.bhawana.authenticationservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/auth/success")
    public Map<String, Object> loginSuccess(Authentication authentication) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("authenticated", authentication.isAuthenticated());
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());

        if (authentication.getPrincipal() instanceof Saml2AuthenticatedPrincipal principal) {
            response.put("attributes", principal.getAttributes());
        }

        return response;
    }
}
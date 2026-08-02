package com.bhawana.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Configuration
public class SamlRelyingPartyConfig {

    private static final String REGISTRATION_ID = "authentication-service";
    private static final String KEYSTORE_PATH = "saml/saml-keystore.p12";
    private static final String METADATA_PATH = "classpath:saml/idp-metadata.xml";
    private static final String KEY_ALIAS = "authentication-service";
    private static final char[] STORE_PASSWORD = "changeit".toCharArray();
    private static final char[] KEY_PASSWORD = "changeit".toCharArray();

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository()
            throws Exception {

        Saml2X509Credential signingCredential = loadSigningCredential();

        RelyingPartyRegistration registration =
                RelyingPartyRegistrations
                        .fromMetadataLocation(METADATA_PATH)
                        .registrationId(REGISTRATION_ID)
                        .entityId("authentication-service")
                        .assertionConsumerServiceLocation(
                                "{baseUrl}/login/saml2/sso/{registrationId}")
                        .signingX509Credentials(credentials ->
                                credentials.add(signingCredential))
                        .build();

        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    private Saml2X509Credential loadSigningCredential() throws Exception {

        ClassPathResource keystoreResource =
                new ClassPathResource(KEYSTORE_PATH);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (InputStream inputStream = keystoreResource.getInputStream()) {
            keyStore.load(inputStream, STORE_PASSWORD);
        }

        Key key = keyStore.getKey(KEY_ALIAS, KEY_PASSWORD);

        if (!(key instanceof PrivateKey privateKey)) {
            throw new IllegalStateException(
                    "Private key not found for alias: " + KEY_ALIAS);
        }

        X509Certificate certificate =
                (X509Certificate) keyStore.getCertificate(KEY_ALIAS);

        if (certificate == null) {
            throw new IllegalStateException(
                    "Certificate not found for alias: " + KEY_ALIAS);
        }

        return Saml2X509Credential.signing(privateKey, certificate);
    }
}
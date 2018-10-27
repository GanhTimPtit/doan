package com.ptit.edu.store.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.edu.store.auth.models.data.JWTTokenPayload;

import java.io.IOException;

public class AccessTokenUtil {
    public static JWTTokenPayload decodeJWTAccessTokenPayload(String accessToken) throws IOException {
        String tokenPayloadBase64Encoded = accessToken.split("\\.")[1];
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(Base64Utils.decode(tokenPayloadBase64Encoded), JWTTokenPayload.class);
    }
}

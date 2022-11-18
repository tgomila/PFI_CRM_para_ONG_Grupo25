package com.pfi.crm.constant;

public class JWTConstants {

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30*24*60*60;
    public static final String SIGNING_KEY = "SuperSecretKey";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}

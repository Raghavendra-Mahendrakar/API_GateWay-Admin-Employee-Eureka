package com.pws.employee.utility;

public final class SwaggerLogsConstants {
    public static final String Authenticate_200_SUCCESS = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MTIzQGdtYWlsLmNvbSIsImV4cCI6MTY3NDY2MDQzNywiaWF0IjoxNjc0NjI0NDM3fQ.hThyGT49vC50NPOiIqT1i42DhbNwJ8UhMozOQylavV4\r\n"
            + "";
    public static final String Authenticate_400_Failure = "{\r\n" + "  \"timestamp\": \"25-01-2023 11:07:39\",\r\n"
            + "  \"status\": 400,\r\n" + "  \"error\": \"BAD_REQUEST\",\r\n"
            + "  \"message\": \"inavalid username/password\",\r\n" + "  \"debugMessage\": null,\r\n"
            + "  \"subErrors\": null\r\n" + "}";
    public static final String SIGNUP_201_SUCCESS="{\n" +
            "  \"timestamp\": \"26-01-2023 07:10:06\",\n" +
            "  \"status\": \"CREATED\",\n" +
            "  \"data\": null\n" +
            "}";

    public static final String SIGNUP_400_FAILURE="{\n" +
            "  \"timestamp\": \"26-01-2023 07:11:04\",\n" +
            "  \"status\": 400,\n" +
            "  \"error\": \"BAD_REQUEST\",\n" +
            "  \"message\": \"User Already Exist with Email : ram123@gmail.com\",\n" +
            "  \"debugMessage\": null,\n" +
            "  \"subErrors\": null\n" +
            "}";
    public static final String UPDATE_USER_PASSWORD_200_SUCCESS="{\n" +
            "  \"timestamp\": \"26-01-2023 07:20:16\",\n" +
            "  \"status\": \"OK\",\n" +
            "  \"data\": null\n" +
            "}";
    public static final String UPDATE_USER_PASSWORD_400_FAILURE="{\n" +
            "  \"timestamp\": \"26-01-2023 07:16:44\",\n" +
            "  \"status\": 400,\n" +
            "  \"error\": \"BAD_REQUEST\",\n" +
            "  \"message\": \"new password and confirm password doesnot match \",\n" +
            "  \"debugMessage\": null,\n" +
            "  \"subErrors\": null\n" +
            "}";
}
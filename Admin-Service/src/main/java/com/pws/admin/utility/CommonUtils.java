package com.pws.admin.utility;

import com.pws.admin.ApiSuccess;
import org.springframework.http.ResponseEntity;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
public class CommonUtils {

    public static ResponseEntity<Object> buildResponseEntity(ApiSuccess apiSuccess) {
        return new ResponseEntity<>(apiSuccess, apiSuccess.getStatus());
    }

}
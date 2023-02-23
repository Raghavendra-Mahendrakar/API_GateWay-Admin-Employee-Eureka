package com.pws.admin.exception.config;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
public class PWSException extends  Exception{

    private static final long serialVersionUID = 1L;

    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }


    public PWSException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}

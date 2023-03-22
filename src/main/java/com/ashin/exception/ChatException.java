package com.ashin.exception;

/**
 * 聊天异常
 *
 * @author kuanghuan
 */
public class ChatException extends Exception {

    public ChatException(String message) {
        super(message);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
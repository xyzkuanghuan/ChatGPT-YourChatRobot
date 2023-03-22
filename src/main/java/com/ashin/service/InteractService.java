package com.ashin.service;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;

/**
 * 交互服务
 *
 * @author kuanghuan
 */
public interface InteractService {

    /**
     * 聊天
     *
     * @param chatBO
     * @return
     * @throws ChatException
     */
    String chat(ChatBO chatBO) throws ChatException;
}

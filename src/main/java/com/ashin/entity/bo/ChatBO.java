package com.ashin.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 聊天BO
 *
 * @author kuanghuan
 */
@Data
@AllArgsConstructor
public class ChatBO {

    /**
     * 会话id
     */
    private String sessionId;

    /**
     * 问题
     */
    private String prompt;
}

package com.ashin.handler;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import com.alibaba.fastjson.JSON;
import com.ashin.constants.ChatConstants;
import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 微信消息处理程序
 *
 * @author kuanghuan
 */
@Component
@Slf4j
public class WechatMessageHandler implements IMsgHandlerFace {

    @Resource
    private InteractService interactService;

    @Override
    public String textMsgHandle(BaseMsg baseMsg) {
        String prompt = baseMsg.getText();
        if (baseMsg.isGroupMsg()) {
            if (!prompt.contains("@" + Core.getInstance().getNickName())) {
                return null;
            }
            prompt = prompt.replace("@" + Core.getInstance().getNickName(), "").trim();
        }
        if (ChatConstants.RESET_WORD.equals(prompt)) {
            BotUtil.resetPrompt(baseMsg.getFromUserName());
            return ChatConstants.RESET_SESSION_MESSAGE_SUCCESS;
        }
        log.info("sessionId = {}, prompt = {}, baseMsg = {}", baseMsg.getFromUserName(), prompt, JSON.toJSONString(baseMsg));
        ChatBO chatBO = new ChatBO(baseMsg.getFromUserName(), prompt);
        String response;
        try {
            response = interactService.chat(chatBO);
        } catch (ChatException e) {
            response = e.getMessage();
            if (response.contains(ChatConstants.MAXIMUM_KEY_WORD)) {
                BotUtil.resetPrompt(chatBO.getSessionId());
                return ChatConstants.RESET_SESSION_MESSAGE;
            }
        }
        return response;
    }

    @Override
    public String picMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String voiceMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String viedoMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String nameCardMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public void sysMsgHandle(BaseMsg baseMsg) {

    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg baseMsg) {
        return null;
    }

    @Override
    public String mediaMsgHandle(BaseMsg baseMsg) {
        return null;
    }
}

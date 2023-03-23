package com.ashin.handler;

import com.ashin.constants.ChatConstants;
import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.MessageTooLargeException;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * QQ消息处理程序
 *
 * @author kuanghuan
 */
@Component
@Slf4j
public class QqMessageHandler implements ListenerHost {

    @Resource
    private InteractService interactService;

    /**
     * 监听消息并把ChatGPT的回答发送到对应qq/群
     * 注：如果是在群聊则需@
     *
     * @param event 事件 ps:此处是MessageEvent 故所有的消息事件都会被监听
     */
    @EventHandler
    public void onMessage(@NotNull MessageEvent event) {
        String prompt = event.getMessage().contentToString().trim();
        if (event.getBot().getGroups().contains(event.getSubject().getId()) && event.getMessage().contains(new At(event.getBot().getId()))) {
            prompt = prompt.replace("@" + event.getBot().getId(), "").trim();
        }
        response(event, new ChatBO(String.valueOf(event.getSubject().getId()), prompt));
    }

    private void response(@NotNull MessageEvent event, ChatBO chatBO) {
        String prompt = chatBO.getPrompt();
        if (ChatConstants.RESET_WORD.equals(prompt)) {
            resetSession(event, chatBO.getSessionId(), ChatConstants.RESET_SESSION_MESSAGE_SUCCESS);
            return;
        }
        log.info("sessionId = {}, prompt = {}", chatBO.getSessionId(), prompt);
        String response;
        try {
            chatBO.setPrompt(prompt);
            response = interactService.chat(chatBO);
        } catch (Exception e) {
            response = e.getMessage();
            if (response.contains(ChatConstants.MAXIMUM_KEY_WORD)) {
                resetSession(event, chatBO.getSessionId(), ChatConstants.RESET_SESSION_MESSAGE);
            }
            return;
        }
        try {
            MessageChain messages = new MessageChainBuilder()
                    .append(new QuoteReply(event.getMessage()))
                    .append(response)
                    .build();
            event.getSubject().sendMessage(messages);
        } catch (MessageTooLargeException e) {
            event.getSubject().sendMessage(response);
        }
    }

    private void resetSession(MessageEvent event, String sessionId, String message) {
        BotUtil.resetPrompt(sessionId);
        event.getSubject().sendMessage(message);
    }

}
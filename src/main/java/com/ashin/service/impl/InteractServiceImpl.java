package com.ashin.service.impl;

import com.ashin.entity.bo.ChatBO;
import com.ashin.exception.ChatException;
import com.ashin.service.InteractService;
import com.ashin.util.BotUtil;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import java.util.List;

/**
 * 交互服务impl
 *
 * @author kuanghuan
 */
@Service
@Slf4j
public class InteractServiceImpl implements InteractService {

    @Override
    public String chat(ChatBO chatBO) throws ChatException {

        List<ChatMessage> prompt = BotUtil.getPrompt(chatBO.getSessionId(), chatBO.getPrompt());

        //向gpt提问
        OpenAiService openAiService = BotUtil.getOpenAiService();
        ChatCompletionRequest.ChatCompletionRequestBuilder completionRequestBuilder = BotUtil.getCompletionRequestBuilder();

        ChatCompletionRequest completionRequest = completionRequestBuilder.messages(prompt).build();
        ChatMessage answer = null;
        try {
            answer = openAiService.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
        } catch (HttpException e) {
            log.error("向gpt提问失败，提问内容：{}，原因：{}", chatBO.getPrompt(), e.getMessage(), e);
            if (500 == e.code() || 503 == e.code() || 429 == e.code()) {
                log.info("尝试重新发送");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    log.error("进程休眠失败，原因：{}", ex.getMessage(), ex);
                    throw new RuntimeException(ex);
                }
                return chat(chatBO);
            }
        }
        if (null == answer) {
            throw new ChatException("GPT可能暂时不想理你");
        }

        prompt.add(answer);
        BotUtil.updatePrompt(chatBO.getSessionId(), prompt);

        return answer.getContent().trim();
    }

}
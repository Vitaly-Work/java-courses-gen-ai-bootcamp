package com.epam.training.gen.ai.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.epam.training.gen.ai.config.ApplicationProperties;
import com.epam.training.gen.ai.config.DeploymentsSettings;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatMessageContent;
import com.microsoft.semantickernel.contextvariables.ContextVariable;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for interacting with the AI kernel, maintaining chat history.
 * <p>
 * This service provides a method to process user prompts while preserving chat history.
 * It uses the {@link Kernel} to invoke AI responses based on the user's input and the
 * previous chat context. The conversation history is updated after each interaction.
 */
@Slf4j
@AllArgsConstructor
@Builder
public class PromptWithHistoryService {

    private final ApplicationProperties applicationProperties;
    private final Kernel kernel;
    private final DeploymentsSettings deploymentsSettings;
    private final InvocationContext invocationContext;
    @Builder.Default
    private volatile ChatHistory chatHistory = new ChatHistory();

    @SneakyThrows
    public List<String> chatWithHistory(String prompt) {
        log.info("Request>>>>>>>> \n {}", prompt);

        chatHistory.addSystemMessage("""
            You are a helpful assistant. On each user question you will try to find related information.
            Always act by this scenario:
            1. Retrieve related information based on user query, if no related information found - answer that you don't know.
            2. Consider the probability score of the retrieved information parts and select only high relevant information parts.
            3. From selected information parts take info values and concatenate in one text.
            4. Analyze resulting text and use it as a context to prepare answer to the user question.
            5. If you don't find answer in the context text then just say that you don't have information.
            Never put score of id values of found related information parts into your answers.
            """);
        chatHistory.addUserMessage(prompt);

        final var chatCompletionService = kernel.getService(ChatCompletionService.class);
        final var modelId = chatCompletionService.getModelId();
        log.info("Invoke kernel using modelId: {}", modelId);

        List<ChatMessageContent<?>> messages = chatCompletionService
            .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
            .block();

        if (messages != null) {
            log.info("BEGIN Protocol of prompt =========================================== \n");
            final var promptResult = getPromptResult(messages);
            log.info("END   Protocol of prompt =========================================== \n");
            return promptResult;
        }

        return List.of();
    }

    private ArrayList<String> getPromptResult(List<ChatMessageContent<?>> messages) {
        final var result = new ArrayList<String>();

        for (ChatMessageContent<?> m : messages) {
            final var message = (OpenAIChatMessageContent<?>) m;
            switch (message.getAuthorRole()) {
                case ASSISTANT -> {
                    final var toolCalls = message.getToolCall();
                    if (toolCalls != null && !toolCalls.isEmpty()) {

                        final var toolCall = toolCalls.get(0);
                        final var arguments = Objects.requireNonNull(toolCall.getArguments())
                            .entrySet().stream()
                            .collect(Collectors.toMap(Entry::getKey, PromptWithHistoryService::getArgumentAsString));

                        log.info(
                            "Assistant requested tool: \n Plugin function: {}.{} \n with arguments: {}",
                            toolCall.getPluginName(), toolCall.getFunctionName(), arguments
                        );

                    } else if (message.getContent() != null) {
                        log.info("Response<<<<<<<< \n {}", message.getContent());
                        chatHistory.addMessage(message);
                        result.add(message.getContent());
                    }
                }
                case TOOL -> {
                    if (message.getContent() != null) {
                        log.info("Tool response: \n {}", message.getContent());
                    }
                }
            }
        }

        return result;
    }

    private static String getArgumentAsString(Entry<String, ContextVariable<?>> e) {
        final ContextVariable<?> value = e.getValue();
        return value.toPromptString();
    }

}

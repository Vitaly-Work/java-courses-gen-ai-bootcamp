package com.epam.training.gen.ai.service;

import java.util.Map;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for interacting with the AI kernel, maintaining chat history.
 * <p>
 * This service provides a method to process user prompts while preserving chat history.
 * It uses the {@link Kernel} to invoke AI responses based on the user's input and the
 * previous chat context. The conversation history is updated after each interaction.
 */
@Slf4j
@RequiredArgsConstructor
@Builder
public class PromptWithHistoryService {

    private final Kernel kernel;
    private final Map<String, PromptExecutionSettings> promptExecutionSettings;
    @Builder.Default
    private final ChatHistory chatHistory = new ChatHistory();

    public String chatWithHistory(String prompt) {
        log.info("Request>>>>>>>> \n {}", prompt);

        var response = kernel.invokeAsync(getChatKernelFunction())
            .withArguments(getKernelFunctionArguments(prompt, chatHistory))
            .block();

        chatHistory.addUserMessage(prompt);
        chatHistory.addAssistantMessage(response.getResult());

        log.info("Response<<<<<<<< \n {}", response.getResult());
        return response.getResult();
    }

    /**
     * Creates a kernel function for generating a chat response using a predefined prompt template.
     * <p>
     * The template includes the chat history and the user's message as variables.
     *
     * @return a {@link KernelFunction} for handling chat-based AI interactions
     */
    private KernelFunction<String> getChatKernelFunction() {
        return KernelFunction.<String>createFromPrompt("""
                {{$chatHistory}}
                <message role="user">{{$request}}</message>""")
            .withDefaultExecutionSettings(promptExecutionSettings.values().iterator().next())
            .build();
    }

    /**
     * Creates the kernel function arguments with the user prompt and chat history.
     *
     * @param prompt      the user's input
     * @param chatHistory the current chat history
     * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
     */
    private KernelFunctionArguments getKernelFunctionArguments(String prompt, ChatHistory chatHistory) {
        return KernelFunctionArguments.builder()
            .withVariable("request", prompt)
            .withVariable("chatHistory", chatHistory)
            .build();
    }

}

package com.epam.training.gen.ai.service;

import static com.epam.training.gen.ai.config.DeploymentsSettings.DEFAULT_DEPLOYMENT_SETTINGS_KEY;

import java.util.Optional;

import com.epam.training.gen.ai.config.ApplicationProperties;
import com.epam.training.gen.ai.config.DeploymentsSettings;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Builder.Default
    private volatile ChatHistory chatHistory = new ChatHistory();

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

    public void clearChatHistory() {
        chatHistory = new ChatHistory();
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
            .withDefaultExecutionSettings(getPromptExecutionSettings())
            .build();
    }

    /**
     * Returns the {@link PromptExecutionSettings} related to current deployment.
     *
     * @return a {@link PromptExecutionSettings}
     */
    private PromptExecutionSettings getPromptExecutionSettings() {
        final var name = applicationProperties.getDeploymentOrModelName();
        final var settingsMap = deploymentsSettings.getDeploymentSettings();

        return Optional.ofNullable(settingsMap.get(name))

            .map(entry -> PromptExecutionSettings.builder()
                .withModelId(name)
                .withTemperature(entry.temperature())
                .withMaxTokens(entry.maxTokens())
                .build())

            .orElseGet(() -> {
                final var deploymentSettings = deploymentsSettings.getDeploymentSettings().get(DEFAULT_DEPLOYMENT_SETTINGS_KEY);
                return PromptExecutionSettings.builder()
                    .withModelId(name)
                    .withTemperature(deploymentSettings.temperature())
                    .withMaxTokens(deploymentSettings.maxTokens())
                    .build();
            });
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

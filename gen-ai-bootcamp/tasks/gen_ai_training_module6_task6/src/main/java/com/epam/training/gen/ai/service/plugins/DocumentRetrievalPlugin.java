package com.epam.training.gen.ai.service.plugins;

import java.util.List;

import com.epam.training.gen.ai.service.SimpleDocumentService;
import com.epam.training.gen.ai.web.dto.EmbeddingsSearchResultDto;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DocumentRetrievalPlugin {

    private final SimpleDocumentService simpleDocumentService;

    @DefineKernelFunction(
        name = "retrieve_related_information",
        description = "Retrieves textual information related to user query. "
            + "Returned result is a list of structures (parts of found information). "
            + "Each part contains: id field with unique value, info field that contains information part, "
            + "score field that represents a float value of the probability that a given piece of information matches the user's request."
            + "If this function returns an empty list then there are no relevant information found.",
        returnType = "com.epam.training.gen.ai.service.plugins.RelatedInformationList")
    public RelatedInformationList retrieveRelatedInformation(
        @KernelFunctionParameter(name = "user_query", description = "The text of user question") String userQuery) {

        log.info("Function 'retrieve_related_information' called with arguments: {}", userQuery);

        List<EmbeddingsSearchResultDto> context = simpleDocumentService.searchContext(userQuery);

        return new RelatedInformationList(context);

    }

}

package com.epam.training.gen.ai.service.plugins;

import java.util.List;

import com.epam.training.gen.ai.web.dto.EmbeddingsSearchResultDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatedInformationList {
    private List<EmbeddingsSearchResultDto> informationParts;
}

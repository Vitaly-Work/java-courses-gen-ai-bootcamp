package com.epam.training.gen.ai.service;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.epam.training.gen.ai.config.ApplicationProperties;
import com.epam.training.gen.ai.config.QdrantProperties;
import com.epam.training.gen.ai.web.dto.EmbeddingsSearchResultDto;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.Embeddings;
import com.azure.ai.openai.models.EmbeddingsOptions;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.Collections.VectorParams;
import io.qdrant.client.grpc.Points.PointStruct;
import io.qdrant.client.grpc.Points.SearchPoints;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service class for processing text into embeddings and interacting with Qdrant for vector storage and retrieval.
 * <p>
 * This service converts text into embeddings using Azure OpenAI and saves these vectors in a Qdrant collection.
 * It also provides functionality to search for similar vectors based on input text.
 */

@Slf4j
@Service
@AllArgsConstructor
public class SimpleVectorService {

    private static final String PAYLOAD_INFO = "info";
    private final ApplicationProperties applicationProperties;
    private final OpenAIAsyncClient openAIAsyncClient;
    private final QdrantProperties qdrantProperties;
    private final QdrantClient qdrantClient;

    /**
     * Processes the input text into embeddings, transforms them into vector points,
     * and saves them in the Qdrant collection.
     *
     * @param text the text to be processed into embeddings
     */
    @SneakyThrows
    public void processAndSaveText(String text) {
        var embeddings = getEmbeddings(text);
        var points = new ArrayList<List<Float>>();
        embeddings.forEach(
            embeddingItem -> {
                var values = new ArrayList<>(embeddingItem.getEmbedding());
                points.add(values);
            });

        var pointStructs = new ArrayList<PointStruct>();
        points.forEach(point -> {
            var pointStruct = getPointStruct(point, text);
            pointStructs.add(pointStruct);
        });

        saveVector(pointStructs);
    }

    /**
     * Searches the Qdrant collection for vectors similar to the input text.
     * <p>
     * The input text is converted to embeddings, and a search is performed based on the vector similarity.
     *
     * @param text the text to search for similar vectors
     * @return a list of dto representing similar vectors
     */
    @SneakyThrows
    public List<EmbeddingsSearchResultDto> search(String text) {
        var embeddings = retrieveEmbeddings(text);
        var qe = new ArrayList<Float>();

        embeddings.block()
            .getData()
            .forEach(embeddingItem ->
                qe.addAll(embeddingItem.getEmbedding())
            );

        final var scoredPoints = qdrantClient
            .searchAsync(
                SearchPoints.newBuilder()
                    .setCollectionName(qdrantProperties.getCollectionName())
                    .setWithPayload(enable(true))
                    .addAllVector(qe)
                    .setLimit(qdrantProperties.getSearchResultLimit())
                    .build())
            .get();

        return scoredPoints.stream()
            .map(p -> new EmbeddingsSearchResultDto(
                p.getId().getUuid(),
                p.getPayloadMap().get(PAYLOAD_INFO).getStringValue(),
                p.getScore()
            ))
            .toList();
    }

    /**
     * Retrieves the embeddings for the given text using Azure OpenAI.
     *
     * @param text the text to be embedded
     * @return a list of {@link EmbeddingItem} representing the embeddings
     */
    public List<EmbeddingItem> getEmbeddings(String text) {
        var embeddings = retrieveEmbeddings(text);

        return embeddings.block().getData();
    }

    /**
     * Creates a new collection in Qdrant with specified vector parameters.
     *
     * @throws ExecutionException   if the collection creation operation fails
     * @throws InterruptedException if the thread is interrupted during execution
     */
    public void createCollection() throws ExecutionException, InterruptedException {
        log.info("Creating collection {}", qdrantProperties.getCollectionName());
        var result = qdrantClient.createCollectionAsync(
                qdrantProperties.getCollectionName(),
                VectorParams.newBuilder()
                    .setDistance(Collections.Distance.Cosine)
                    .setSize(1536)
                    .build()
            )
            .get();
        log.info("Collection was created: [{}]", result.getResult());
    }

    /**
     * Saves the list of point structures (vectors) to the Qdrant collection.
     *
     * @param pointStructs the list of vectors to be saved
     * @throws InterruptedException if the thread is interrupted during execution
     * @throws ExecutionException   if the saving operation fails
     */
    private void saveVector(ArrayList<PointStruct> pointStructs) throws InterruptedException, ExecutionException {
        final var collectionName = qdrantProperties.getCollectionName();
        try {

            qdrantClient.getCollectionInfoAsync(collectionName).get();

        } catch (ExecutionException exception) {
            if (exception.getCause() instanceof StatusRuntimeException ex) {
                if (ex.getStatus().getCode() == Status.Code.NOT_FOUND) {
                    createCollection();
                }
            }
        }
        var updateResult = qdrantClient.upsertAsync(collectionName, pointStructs).get();
        log.info("Upsert status: {}", updateResult.getStatus().name());
    }

    /**
     * Constructs a point structure from a list of float values representing a vector.
     *
     * @param point the vector values
     * @return a {@link PointStruct} object containing the vector and associated metadata
     */
    private PointStruct getPointStruct(List<Float> point, String info) {
        return PointStruct.newBuilder()
            .setId(id(UUID.randomUUID()))
            .setVectors(vectors(point))
            .putPayload(PAYLOAD_INFO, value(info))
            .build();
    }

    /**
     * Retrieves the embeddings for the given text asynchronously from Azure OpenAI.
     *
     * @param text the text to be embedded
     * @return a {@link Mono} of {@link Embeddings} representing the embeddings
     */
    private Mono<Embeddings> retrieveEmbeddings(String text) {
        var qembeddingsOptions = new EmbeddingsOptions(List.of(text));
        return openAIAsyncClient.getEmbeddings(
            applicationProperties.getEmbeddingsDeploymentName(),
            qembeddingsOptions
        );
    }
}

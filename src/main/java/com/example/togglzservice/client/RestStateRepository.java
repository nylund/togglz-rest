package com.example.togglzservice.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.togglz.core.Feature;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.StateRepository;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RestStateRepository implements StateRepository {

    private String url;
    private RestTemplateBuilder requestBuilder;

    public RestStateRepository(String url) {
        this(url, new RestTemplateBuilder());
    }

    public RestStateRepository(String url, RestTemplateBuilder requestBuilder) {
        this.url = url.endsWith("/") ? url : url + "/";
        this.requestBuilder = requestBuilder;
    }

    @Override
    public FeatureState getFeatureState(Feature feature) {
        return fetchFeature(feature).orElse(null);
    }

    @Override
    public void setFeatureState(FeatureState featureState) {
        throw new RuntimeException("Not implemented");
    }

    private Optional<FeatureState> fetchFeature(Feature feature) {
        RestTemplate restTemplate = requestBuilder.build();

        ResponseEntity<String> response = restTemplate.getForEntity(url + feature.name(), String.class);

        ObjectMapper mapper = new ObjectMapper();

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode jsonResponse = mapper.readTree(response.getBody());

                boolean enabled = false;
                if (jsonResponse.has("enabled")) {
                    enabled = jsonResponse.get("enabled").asBoolean();
                }

                FeatureState state = new FeatureState(feature, enabled);


                if (jsonResponse.has("strategyId")) {
                    JsonNode strategy = jsonResponse.get("strategyId");

                    if (!strategy.isNull()) {
                        state.setStrategyId(strategy.asText());
                    }
                }

                if (jsonResponse.has("parameterNames") &&
                    jsonResponse.has("parameterMap")) {

                    JsonNode parameterMap = jsonResponse.get("parameterMap");
                    for (JsonNode parameterName : jsonResponse.get("parameterNames")) {
                        String name = parameterName.asText();

                        state.setParameter(name, parameterMap.get(name).asText());
                    }
                }

                return Optional.of(state);
            } catch (IOException e) {
                log.debug("fetching feature failed", e);
            }
        }
        return Optional.empty();
    }
}

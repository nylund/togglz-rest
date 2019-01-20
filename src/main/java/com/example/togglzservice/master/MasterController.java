package com.example.togglzservice.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Profile("master")
public class MasterController {

    FeatureManager featureManager;

    @Autowired
    public MasterController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @GetMapping("/features-api/v1/feature")
    public Set<Feature> featureList() {
        return featureManager.getFeatures();
    }

    @GetMapping("/features-api/v1/state")
    public Set<FeatureState> featureStates() {
        return featureList().stream()
                .map(featureManager::getFeatureState)
                .collect(Collectors.toSet());
    }

    @GetMapping("/features-api/v1/state/{featureName}")
    public Optional<FeatureState> featureStates(@PathVariable("featureName") String featureName) {

        Optional<Feature> feature = featureManager.getFeatures().stream()
                .filter(f -> f.name().equals(featureName))
                .findFirst();

        return feature.map(feature1 -> featureManager.getFeatureState(feature1));
    }

}

package com.example.togglzservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.util.NamedFeature;

@RestController
public class Controller {

    private final FeatureManager manager;

    public static final Feature HELLO_WORLD = new NamedFeature("HELLO_WORLD");

    public Controller(FeatureManager manager) {
        this.manager = manager;
    }

    @RequestMapping("/")
    public ResponseEntity<?> staticClassFeature() {
        if (manager.isActive(HELLO_WORLD)) {
            return ResponseEntity.ok().body("Hello world");
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping("/enum")
    public ResponseEntity<?> enumFeature() {
        if (MyFeatures.HELLO_WORLD.isActive()) {
            return ResponseEntity.ok().body("Hello world");
        }
        return ResponseEntity.notFound().build();
    }
}

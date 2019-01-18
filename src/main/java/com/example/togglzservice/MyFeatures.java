package com.example.togglzservice;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum MyFeatures implements Feature {

    @Label("HELLO_WORLD")
    HELLO_WORLD;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}

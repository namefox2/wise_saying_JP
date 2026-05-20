package com.facebook.react;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ReactAppStubPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("react", ReactExtensionStub.class);
    }
}

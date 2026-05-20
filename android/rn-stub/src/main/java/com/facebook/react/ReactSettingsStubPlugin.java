package com.facebook.react;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

public class ReactSettingsStubPlugin implements Plugin<Settings> {
    @Override
    public void apply(Settings settings) {
        // no-op: used when node_modules is not installed
    }
}

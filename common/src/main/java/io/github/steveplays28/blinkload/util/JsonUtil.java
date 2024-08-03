package io.github.steveplays28.blinkload.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

public class JsonUtil {
    public static @NotNull Gson getGson() {
        return new Gson().newBuilder().setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
}

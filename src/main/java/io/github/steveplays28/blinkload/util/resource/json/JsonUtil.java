package io.github.steveplays28.blinkload.util.resource.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import io.github.steveplays28.blinkload.util.resource.json.adapter.NativeImageAdapter;
import net.minecraft.client.texture.NativeImage;
import org.jetbrains.annotations.NotNull;

public class JsonUtil {
	public static @NotNull Gson getGson() {
		return new Gson().newBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(
				NativeImage.class, new NativeImageAdapter()).create();
	}
}

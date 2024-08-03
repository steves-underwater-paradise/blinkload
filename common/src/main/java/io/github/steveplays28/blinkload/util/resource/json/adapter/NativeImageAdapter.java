package io.github.steveplays28.blinkload.util.resource.json.adapter;

import com.google.gson.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.NativeImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

@Environment(EnvType.CLIENT)
public class NativeImageAdapter implements JsonSerializer<NativeImage>, JsonDeserializer<NativeImage> {
	/**
	 * Gson invokes this call-back method during deserialization when it encounters a field of the
	 * specified type.
	 * <p>In the implementation of this call-back method, you should consider invoking
	 * {@link JsonDeserializationContext#deserialize(JsonElement, Type)} method to create objects
	 * for any non-trivial field of the returned object. However, you should never invoke it on the
	 * the same type passing {@code json} since that will cause an infinite loop (Gson will call your
	 * call-back method again).
	 *
	 * @param json    The JSON data that needs to be converted to a {@link NativeImage}.
	 * @param typeOfT The type of the {@link NativeImage} to deserialize to.
	 * @param context The {@link JsonDeserializationContext}.
	 * @return A deserialized {@link NativeImage} of the specified type typeOfT which is a subclass of {@code T}.
	 * @throws JsonParseException if json is not in the expected format of {@code typeofT}
	 */
	@Override
	public @NotNull NativeImage deserialize(@NotNull JsonElement json, Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
		byte[] nativeImageBytes = context.deserialize(json, byte[].class);

		try {
			return NativeImage.read(nativeImageBytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gson invokes this call-back method during serialization when it encounters a field of the
	 * specified type.
	 *
	 * <p>In the implementation of this call-back method, you should consider invoking
	 * {@link JsonSerializationContext#serialize(Object, Type)} method to create JsonElements for any
	 * non-trivial field of the {@code src} object. However, you should never invoke it on the
	 * {@code src} object itself since that will cause an infinite loop (Gson will call your
	 * call-back method again).</p>
	 *
	 * @param src       The {@link NativeImage} that needs to be converted to JSON.
	 * @param typeOfSrc The actual {@link Type} (fully genericized version) of the source object.
	 * @param context   The {@link JsonSerializationContext}.
	 * @return A {@link JsonElement} corresponding to the specified {@link NativeImage}.
	 */
	@Override
	public @NotNull JsonElement serialize(@Nullable NativeImage src, Type typeOfSrc, @NotNull JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		}

		byte[] nativeImageBytes;
		try {
			nativeImageBytes = src.getBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return context.serialize(nativeImageBytes, byte[].class);
	}
}

package io.github.steveplays28.blinkload.util.resource.json;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "unused"})
public class AtlasTextureIdentifier {
	private @Nullable Identifier identifier;
	private @Nullable Integer mipLevel;

	public AtlasTextureIdentifier(@Nullable Identifier identifier, @Nullable Integer mipLevel) {
		this.identifier = identifier;
		this.mipLevel = mipLevel;
	}

	public @Nullable Identifier getIdentifier() {
		return identifier;
	}

	public @Nullable Integer getMipLevel() {
		return mipLevel;
	}
}

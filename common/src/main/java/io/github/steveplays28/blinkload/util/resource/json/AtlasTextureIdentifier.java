package io.github.steveplays28.blinkload.util.resource.json;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof AtlasTextureIdentifier otherAtlasTextureIdentifier)) {
			return false;
		}

		return Objects.equals(getIdentifier(), otherAtlasTextureIdentifier.getIdentifier()) && Objects.equals(
				getMipLevel(), otherAtlasTextureIdentifier.getMipLevel());
	}

	@Override
	public int hashCode() {
		var identifierHashCode = 1;
		if (getIdentifier() != null) {
			identifierHashCode = getIdentifier().hashCode();
		}

		var mipLevelHashCode = 1;
		if (getMipLevel() != null) {
			mipLevelHashCode = getMipLevel().hashCode();
		}

		return identifierHashCode * mipLevelHashCode;
	}
}

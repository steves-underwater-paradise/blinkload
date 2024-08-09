package io.github.steveplays28.blinkload.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = Blocks.class, priority = 200)
public class BlocksMixin {
	@WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;initShapeCache()V"))
	private static void blinkload$cancelShapeCacheInitialization(BlockState instance, @NotNull Operation<Void> original) {
		// NO-OP
	}
}

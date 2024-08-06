package io.github.steveplays28.blinkload.mixin.server;

import net.minecraft.server.Main;
import net.minecraft.util.crash.CrashMemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
public class ServerMainMixin {
	@Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/crash/CrashReport;initCrashReport()V"))
	private static void blinkload$optimiseCrashReportInitialization() {
		CrashMemoryReserve.reserveMemory();
	}
}

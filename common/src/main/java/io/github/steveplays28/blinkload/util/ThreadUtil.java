package io.github.steveplays28.blinkload.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.steveplays28.blinkload.BlinkLoad;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadUtil {
	public static final @NotNull String BOOTSTRAPPER_THREAD_POOL_NAME = String.format("%s Bootstrap Initializer", BlinkLoad.MOD_NAME);

	private static final @NotNull Executor ATLAS_TEXTURE_LOADER_THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(
			1, new ThreadFactoryBuilder().setNameFormat(String.format("%s Atlas Texture Loader", BlinkLoad.MOD_NAME)).build());
	private static final @NotNull Executor BOOTSTRAP_INITIALIZER_THREAD_POOL_EXECUTOR = Executors.newFixedThreadPool(
			1, new ThreadFactoryBuilder().setNameFormat(BOOTSTRAPPER_THREAD_POOL_NAME).build());

	public static @NotNull Executor getAtlasTextureLoaderThreadPoolExecutor() {
		return ATLAS_TEXTURE_LOADER_THREAD_POOL_EXECUTOR;
	}

	public static @NotNull Executor getBootstrapInitializerThreadPoolExecutor() {
		return BOOTSTRAP_INITIALIZER_THREAD_POOL_EXECUTOR;
	}
}

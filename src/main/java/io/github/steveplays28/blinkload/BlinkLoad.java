package io.github.steveplays28.blinkload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlinkLoad {
	public static final String MOD_ID = "blinkload";
	public static final String MOD_NAME = "BlinkLoad";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void initialize() {
		LOGGER.info("Loading {}.", MOD_NAME);
	}
}

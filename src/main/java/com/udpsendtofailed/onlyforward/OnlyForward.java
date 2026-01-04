package com.udpsendtofailed.onlyforward;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlyForward implements ClientModInitializer {
    public static final String MOD_ID = "onlyforward";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Only Forward initialized. Don't look back.");
    }
}
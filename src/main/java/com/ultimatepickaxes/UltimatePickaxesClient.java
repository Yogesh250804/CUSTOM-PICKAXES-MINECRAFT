package com.ultimatepickaxes;

import com.ultimatepickaxes.client.ActionbarHudRenderer;
import com.ultimatepickaxes.client.ScreenShakeClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class UltimatePickaxesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // 1. Initialize Client Screen Shake Packet Receiver
        ScreenShakeClient.init();

        // 2. Register Client Tick Event for Screen Shake Updates
        ClientTickEvents.END_CLIENT_TICK.register(client -> ScreenShakeClient.tick());

        // 3. Initialize HUD Action bar Render Callback
        ActionbarHudRenderer.init();
    }
}

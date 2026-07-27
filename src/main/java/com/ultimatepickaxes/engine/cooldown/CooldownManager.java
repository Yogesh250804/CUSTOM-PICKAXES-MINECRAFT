package com.ultimatepickaxes.engine.cooldown;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private static final Map<UUID, Map<String, Long>> COOLDOWNS = new HashMap<>();

    public static void setCooldown(UUID playerUuid, String key, int durationTicks) {
        long expireTime = System.currentTimeMillis() + (durationTicks * 50L);
        COOLDOWNS.computeIfAbsent(playerUuid, u -> new HashMap<>()).put(key, expireTime);
    }

    public static boolean isOnCooldown(UUID playerUuid, String key) {
        return getRemainingTicks(playerUuid, key) > 0;
    }

    public static int getRemainingTicks(UUID playerUuid, String key) {
        Map<String, Long> playerMap = COOLDOWNS.get(playerUuid);
        if (playerMap == null) return 0;
        Long expireTime = playerMap.get(key);
        if (expireTime == null) return 0;

        long remainingMs = expireTime - System.currentTimeMillis();
        if (remainingMs <= 0) {
            playerMap.remove(key);
            return 0;
        }
        return (int) Math.ceil(remainingMs / 50.0);
    }

    public static float getCooldownProgress(UUID playerUuid, String key, int maxTicks) {
        int remaining = getRemainingTicks(playerUuid, key);
        if (remaining <= 0 || maxTicks <= 0) return 0.0f;
        return (float) remaining / (float) maxTicks;
    }
}

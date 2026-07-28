package com.ultimatepickaxes.engine.ability.components;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

public class UndeadAllyProtectionHandler {

    public static void init() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity player && source.getAttacker() instanceof LivingEntity attacker) {
                // Ignore self damage or damage from other Undead Allies
                if (attacker != player && !(attacker instanceof ZombieEntity && attacker.getCustomName() != null && "Undead Ally".equals(attacker.getCustomName().getString()))) {
                    // Retaliate: Find all nearby Undead Allies in 16 blocks and set target to attacker
                    Box searchBox = new Box(player.getBlockPos()).expand(16.0);
                    for (ZombieEntity zombie : player.getWorld().getEntitiesByClass(ZombieEntity.class, searchBox, z -> z.isAlive())) {
                        if (zombie.getCustomName() != null && "Undead Ally".equals(zombie.getCustomName().getString())) {
                            zombie.setTarget(attacker);
                        }
                    }
                }
            }
            return true; // Allow damage to proceed normally
        });
    }
}

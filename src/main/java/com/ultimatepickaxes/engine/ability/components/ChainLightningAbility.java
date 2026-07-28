package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChainLightningAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        int maxChains = params.has("chains") ? params.get("chains").getAsInt() : 5;
        double range = params.has("range") ? params.get("range").getAsDouble() : 8.0;
        float damage = params.has("damage") ? params.get("damage").getAsFloat() : 10.0f;

        Vec3d origin = context.getPlayer().getPos();
        Box searchArea = new Box(origin.add(-12, -6, -12), origin.add(12, 6, 12));

        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, searchArea, e -> e != context.getPlayer());
        if (targets.isEmpty()) return false;

        Set<LivingEntity> hitEntities = new HashSet<>();
        LivingEntity current = targets.get(0);

        for (int i = 0; i < maxChains && current != null; i++) {
            hitEntities.add(current);

            // Strike lightning
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            if (lightning != null) {
                lightning.refreshPositionAfterTeleport(current.getX(), current.getY(), current.getZ());
                lightning.setCosmetic(true);
                world.spawnEntity(lightning);
            }

            current.damage(world.getDamageSources().lightningBolt(), damage - (i * 1.5f));

            // Find next closest unhit entity
            LivingEntity next = null;
            double closestDistSq = range * range;
            Vec3d currentPos = current.getPos();

            Box searchBox = new Box(currentPos.x - range, currentPos.y - range, currentPos.z - range, currentPos.x + range, currentPos.y + range, currentPos.z + range);
            for (LivingEntity candidate : world.getEntitiesByClass(LivingEntity.class, searchBox, e -> e != context.getPlayer() && !hitEntities.contains(e))) {
                double distSq = candidate.squaredDistanceTo(current);
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    next = candidate;
                }
            }
            current = next;
        }

        return true;
    }
}

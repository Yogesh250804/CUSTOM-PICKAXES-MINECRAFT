package com.ultimatepickaxes.engine.ability.components;

import com.google.gson.JsonObject;
import com.ultimatepickaxes.engine.ability.AbilityComponent;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MobSwarmAbility implements AbilityComponent {

    @Override
    public boolean execute(JsonObject params, AbilityContext context) {
        if (!(context.getWorld() instanceof ServerWorld world) || context.getPlayer() == null) {
            return false;
        }

        BlockPos playerPos = context.getPlayer().getBlockPos();
        world.playSound(null, playerPos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.5f, 0.8f);

        int count = params.has("count") ? params.get("count").getAsInt() : 5;
        int durationSeconds = params.has("durationSeconds") ? params.get("durationSeconds").getAsInt() : 90;

        // Scoreboard team safety backup
        ServerScoreboard scoreboard = world.getScoreboard();
        Team team = scoreboard.getTeam("UndeadAllies");
        if (team == null) {
            team = scoreboard.addTeam("UndeadAllies");
            team.setFriendlyFireAllowed(false);
        }
        scoreboard.addScoreHolderToTeam(context.getPlayer().getNameForScoreboard(), team);

        for (int i = 0; i < count; i++) {
            double angle = (i / (double) count) * Math.PI * 2;
            int dx = (int) (Math.cos(angle) * 3);
            int dz = (int) (Math.sin(angle) * 3);
            BlockPos spawnPos = playerPos.add(dx, 0, dz);

            ZombieEntity zombie = EntityType.ZOMBIE.create(world);
            if (zombie != null) {
                zombie.refreshPositionAndAngles(spawnPos, 0, 0);
                zombie.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                zombie.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                zombie.setCustomName(Text.literal("Undead Ally"));
                zombie.setCustomNameVisible(true);

                // 1 & 2. Safely access targetSelector, clear default goals, and add HostileEntity target goal
                try {
                    GoalSelector targetSelector = getTargetSelector(zombie);
                    if (targetSelector != null) {
                        targetSelector.clear(g -> true); // Stops attacking player
                        targetSelector.add(1, new ActiveTargetGoal<>(
                                zombie,
                                HostileEntity.class,
                                10,
                                true,
                                false,
                                target -> target != null && !(target instanceof ZombieEntity)
                        ));
                    }
                } catch (Exception e) {
                    // Fallback target clear
                    zombie.setTarget(null);
                }

                // 3. Add to Scoreboard Team
                scoreboard.addScoreHolderToTeam(zombie.getNameForScoreboard(), team);

                world.spawnEntity(zombie);

                // 4. Automatic Despawn after duration
                final ZombieEntity finalZombie = zombie;
                CompletableFuture.delayedExecutor(durationSeconds, TimeUnit.SECONDS).execute(() -> {
                    if (world.getServer() != null) {
                        world.getServer().execute(() -> {
                            if (finalZombie.isAlive()) {
                                world.spawnParticles(ParticleTypes.POOF, finalZombie.getX(), finalZombie.getY() + 1.0, finalZombie.getZ(), 25, 0.4, 0.5, 0.4, 0.05);
                                finalZombie.discard();
                            }
                        });
                    }
                });
            }

            world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, spawnPos.getX() + 0.5, spawnPos.getY() + 1.0, spawnPos.getZ() + 0.5, 25, 0.4, 0.5, 0.4, 0.05);
        }

        return true;
    }

    private GoalSelector getTargetSelector(MobEntity mob) {
        int goalSelectorCount = 0;
        for (Field field : MobEntity.class.getDeclaredFields()) {
            if (GoalSelector.class.isAssignableFrom(field.getType())) {
                goalSelectorCount++;
                if (goalSelectorCount == 2) { // 2nd GoalSelector is targetSelector
                    field.setAccessible(true);
                    try {
                        return (GoalSelector) field.get(mob);
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}

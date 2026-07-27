package com.ultimatepickaxes.engine.ability;

import com.ultimatepickaxes.engine.trigger.TriggerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AbilityContext {
    private final World world;
    private final PlayerEntity player;
    private final ItemStack stack;
    private final BlockPos pos;
    private final Entity target;
    private final Direction side;
    private final HitResult hitResult;
    private final TriggerType triggerType;

    public AbilityContext(World world, PlayerEntity player, ItemStack stack, BlockPos pos, Entity target, Direction side, HitResult hitResult, TriggerType triggerType) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.pos = pos != null ? pos.toImmutable() : (player != null ? player.getBlockPos().toImmutable() : null);
        this.target = target;
        this.side = side;
        this.hitResult = hitResult;
        this.triggerType = triggerType;
    }

    public World getWorld() {
        return world;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Entity getTarget() {
        return target;
    }

    public Direction getSide() {
        return side;
    }

    public HitResult getHitResult() {
        return hitResult;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }
}

package com.ultimatepickaxes.engine.ability;

import com.ultimatepickaxes.engine.condition.ConditionEngine;
import com.ultimatepickaxes.engine.effect.EffectPipeline;

import java.util.List;

public class AbilityChainExecutor {

    public static boolean executeChain(List<AbilityConfig> chain, AbilityContext context) {
        if (chain == null || chain.isEmpty() || context == null) return false;

        boolean anyExecuted = false;
        for (AbilityConfig config : chain) {
            // 1. Evaluate conditions
            if (!ConditionEngine.evaluateAll(config.getConditions(), context)) {
                continue;
            }

            // 2. Resolve ability component
            AbilityComponent component = AbilityRegistry.get(config.getAbilityId());
            if (component != null) {
                boolean success = component.execute(config.getParams(), context);
                if (success) {
                    anyExecuted = true;
                    // 3. Trigger visual/audio effects
                    EffectPipeline.playEffects(config.getEffects(), context);
                }
            }
        }
        return anyExecuted;
    }
}

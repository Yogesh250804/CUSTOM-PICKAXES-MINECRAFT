package com.ultimatepickaxes.engine.trigger;

import com.ultimatepickaxes.engine.ability.AbilityChainExecutor;
import com.ultimatepickaxes.engine.ability.AbilityConfig;
import com.ultimatepickaxes.engine.ability.AbilityContext;
import com.ultimatepickaxes.engine.progression.ProgressionEngine;
import com.ultimatepickaxes.items.UltimatePickaxeItem;

import java.util.List;

public class TriggerDispatcher {

    public static boolean dispatch(TriggerType triggerType, AbilityContext context) {
        if (context == null || context.getStack() == null || !(context.getStack().getItem() instanceof UltimatePickaxeItem pickaxeItem)) {
            return false;
        }

        var definition = pickaxeItem.getDefinition();
        if (definition == null) return false;

        // Process XP progression on block break
        if (triggerType == TriggerType.ON_MINE) {
            ProgressionEngine.processBlockBreak(definition.getProgressionConfig(), context);
        }

        List<AbilityConfig> chain = definition.getTriggers().get(triggerType);
        if (chain != null && !chain.isEmpty()) {
            return AbilityChainExecutor.executeChain(chain, context);
        }
        return false;
    }
}

package com.briarshore.rule

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition

/**
 * Class: RuleFactory
 */
object RuleFactory {
    fun <T> getRule(ruleId: Int, ruleType: RuleType?, ruleName: String, description: String, conditions: Collection<Condition<T>>, actions: Collection<Action<T>>): Rule<T>? {
        return if (null == ruleType) null else RuleImpl(ruleId, ruleType, ruleName, conditions, actions, description)
    }
}

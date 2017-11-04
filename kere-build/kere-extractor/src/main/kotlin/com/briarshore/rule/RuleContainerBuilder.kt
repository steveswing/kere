package com.briarshore.rule

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition
import com.google.common.collect.Table
import java.util.Optional

/**
 * Class: RuleContainerBuilder
 */
abstract class RuleContainerBuilder<T>(protected val name: String, val ruleType: RuleType?, protected val values: Table<String, String, String>) {
    private var rowCount = 0

    abstract fun getRuleId(columns: Map<String, String>?): Int

    abstract fun getDescription(columns: Map<String, String>): String

    open fun makeRuleName(ruleId: Int): String {
        return String.format("%s-%03d", ruleType?.name ?: "undefined", ruleId)
    }

    open fun build(): RuleContainer<T> {
        values.rowKeySet().removeAll(keysHeaderRows)
        rowCount = Math.max(rowCount, values.rowMap().size)

        return makeRuleContainer(values.rowMap().filterValues { 0 < getRuleId(it) }.mapValues {
            val ruleId = getRuleId(it.value)
            RuleFactory.getRule<T>(ruleId, ruleType, makeRuleName(ruleId), getDescription(it.value), getConditions(it.value), getActions(it.value))
        })
    }

    protected fun makeRuleContainer(rulesById: Map<String, Rule<T>?>): RuleContainer<T> {
        val result = RuleContainer<T>(ruleType, name)
        result.addAll(rulesById.values)
        if (rowCount != result.ruleCount) {
            throw IllegalStateException(String.format("Expected %d rules (from %s worksheet rows) but only created %d.", rowCount, name, result.ruleCount))
        }
        return result
    }

    abstract fun getConditions(columns: Map<String, String>): Collection<Condition<T>>

    protected fun add(result: MutableCollection<Condition<T>>, condition: Condition<T>?) {
        if (null != condition) {
            result.add(condition)
        }
    }

    protected fun add(result: MutableCollection<Action<T>>, action: Action<T>?) {
        if (null != action) {
            result.add(action)
        }
    }

    abstract fun getActions(columns: Map<String, String>): Collection<Action<T>>

    companion object {
        val keysHeaderRows = listOf("0", "1", "2")
    }
}

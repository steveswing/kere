package com.briarshore.rule

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition
import com.google.common.collect.Table
import org.apache.commons.lang3.StringUtils
import org.apache.poi.ss.util.CellReference.convertNumToColString

/**
 * Class: StartDateRuleBuilder
 */
class SampleRuleContainerBuilder<T>(name: String, ruleType: RuleType?, values: Table<String, String, String>) : RuleContainerBuilder<T>(name, ruleType, values) {

    override fun getRuleId(columns: Map<String, String>?): Int {
        return Integer.valueOf(StringUtils.defaultIfBlank<String>(if (null == columns) "" else columns.getOrDefault(COLUMN_INDEX_A_RULE_ID, "-1"), "-1"))
    }

    override fun getDescription(columns: Map<String, String>): String {
        return StringUtils.trimToEmpty(columns[COLUMN_INDEX_B_DESCRIPTION])
    }

    override fun getConditions(columns: Map<String, String>): Collection<Condition<T>> {
        val result = mutableSetOf<Condition<T>>()
        add(result, ConditionFactory.effective(true, ConditionFactory.asDateTime(columns[COLUMN_INDEX_C_EFFECTIVE_DATE]), ConditionFactory.asDateTime(columns[COLUMN_INDEX_D_EXPIRATION_DATE])))
        return result
    }

    override fun getActions(columns: Map<String, String>): Collection<Action<T>> {
        return mutableSetOf()
    }

    companion object {
        private var index = 0

        private val COLUMN_INDEX_A_RULE_ID = convertNumToColString(index++)
        private val COLUMN_INDEX_B_DESCRIPTION = convertNumToColString(index++)
        private val COLUMN_INDEX_C_EFFECTIVE_DATE = convertNumToColString(index++)
        private val COLUMN_INDEX_D_EXPIRATION_DATE = convertNumToColString(index++)
    }
}

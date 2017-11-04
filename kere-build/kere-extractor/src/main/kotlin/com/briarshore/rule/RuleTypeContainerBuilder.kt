package com.briarshore.rule

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition
import com.google.common.collect.Table
import org.apache.commons.lang3.StringUtils
import org.apache.poi.ss.util.CellReference.convertNumToColString
import org.slf4j.LoggerFactory

/**
 * Class: RuleTypeBuilder
 */
class RuleTypeContainerBuilder(name: String, ruleType: RuleType?, values: Table<String, String, String>) : RuleContainerBuilder<RuleType>(name, ruleType, values) {
    private var rowCount: Int = 0
    override fun build(): RuleContainer<RuleType> {
        values.rowKeySet().removeAll(keysHeaderRows)
        rowCount = Math.max(rowCount, values.rowMap().size)

        values.rowMap().filterValues { 0 < getRuleId(it) && null != ruleType }.mapValues {
            val columns = it.value as Map<String, String>?
            if (null != columns && !columns.isEmpty()) {
                val ruleType = RuleType.Builder().setId(getRuleId(columns).toLong()).setName(columns.get(COLUMN_INDEX_B_RULE_TYPE_NAME)).setCategory(
                        columns.get(COLUMN_INDEX_C_CATEGORY)).setDescription(getDescription(columns)).setPriority(Integer.valueOf(columns.get(COLUMN_INDEX_E_PRIORITY))).build()
                log.debug("Created ruleType {}", ruleType)
            }
        }
        return RuleContainer()
    }

    override fun getRuleId(columns: Map<String, String>?): Int {
        return Integer.valueOf(StringUtils.defaultIfBlank<String>(columns!![COLUMN_INDEX_A_RULE_TYPE_ID], "-1"))
    }

    override fun makeRuleName(ruleId: Int): String {
        return String.format("%s-%03d", ruleType?.name ?: "undefined", ruleId)
    }

    override fun getConditions(columns: Map<String, String>): Collection<Condition<RuleType>> {
        return listOf()
    }

    override fun getActions(columns: Map<String, String>): Collection<Action<RuleType>> {
        return listOf()
    }

    override fun getDescription(columns: Map<String, String>): String {
        return columns[COLUMN_INDEX_D_DESCRIPTION] as String
    }

    companion object {
        private val log = LoggerFactory.getLogger(RuleTypeContainerBuilder::class.java)
        private var index = 0
        private val COLUMN_INDEX_A_RULE_TYPE_ID = convertNumToColString(index++)
        private val COLUMN_INDEX_B_RULE_TYPE_NAME = convertNumToColString(index++)
        private val COLUMN_INDEX_C_CATEGORY = convertNumToColString(index++)
        private val COLUMN_INDEX_D_DESCRIPTION = convertNumToColString(index++)
        private val COLUMN_INDEX_E_PRIORITY = convertNumToColString(index++)
    }
}

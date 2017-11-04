package com.briarshore.rule

import com.briarshore.util.Strings.nullSafeCaseInsensitiveComparator
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamOmitField
import org.slf4j.LoggerFactory
import java.util.Comparator
import java.util.SortedMap
import java.util.SortedSet

@XStreamAlias("rules") open class DefaultRuleEngine<T> : RuleEngine<T> {

    @Transient
    val log = LoggerFactory.getLogger(DefaultRuleEngine::class.java)
    @XStreamAlias("rule-types") protected val ruleTypes = sortedSetOf(RuleType.ruleTypePriorityComparator)
    @XStreamAlias("rule-containers") protected val ruleContainersByRuleType: SortedMap<RuleType, RuleContainer<T>> = mutableMapOf<RuleType, RuleContainer<T>>().toSortedMap(
            RuleType.ruleTypePriorityComparator)
    @XStreamOmitField protected val ruleContainersByCategory: SortedMap<String, SortedSet<RuleContainer<T>>> = mutableMapOf<String, SortedSet<RuleContainer<T>>>().toSortedMap(
            Comparator(nullSafeCaseInsensitiveComparator))

    override fun add(ruleType: RuleType?, rule: Rule<T>?) {
        if (null == ruleType || null == rule || hasNoConditions(rule) || hasNoActions(rule)) {
            return
        }

        getRuleContainer(ruleType).add(rule)
    }

    private fun hasNoConditions(rule: Rule<T>): Boolean {
        return rule.conditions.isEmpty()
    }

    private fun hasNoActions(rule: Rule<T>): Boolean {
        return rule.actions.isEmpty()
    }

    fun findRuleContainer(type: RuleType?): RuleContainer<T> {
        return ruleContainersByRuleType.computeIfAbsent(type, { RuleContainer<T>(it, it.name) })
    }

    protected fun getRuleContainer(type: RuleType?): RuleContainer<T> {
        return findRuleContainer(type)
    }

    fun getRuleTypes(): SortedSet<RuleType> {
        return ruleTypes
    }

    fun setRuleTypes(ruleTypes: Set<RuleType>?) {
        this.ruleTypes.clear()
        ruleTypes?.let { it.map { rt -> this.ruleTypes.add(rt) } }
        if (null != ruleTypes && !ruleTypes.isEmpty()) {
            for (r in ruleTypes) {
                // Kind of a hacky way around XStream not having a way to use the correct constructor with all the attributes.
                this.ruleTypes.add(RuleType.Builder().setId(r.id).setName(r.name).setCategory(r.category).setDescription(r.description).setPriority(r.priority).build())
            }
        }
    }

    fun setRuleContainersByRuleType(ruleContainers: SortedSet<RuleContainer<T>>?) {
        this.ruleContainersByRuleType.clear()
        ruleContainers?.let { addRuleContainers(it) }
    }

    override fun addRuleContainers(ruleContainers: Collection<RuleContainer<T>>?): RuleEngine<T> {
        ruleContainers?.filter({ null != it.ruleType })?.forEach({
            it.ruleType?.let { ruleType ->
                ruleTypes.add(ruleType)
                this.ruleContainersByRuleType.put(ruleType, it)
                this.ruleContainersByCategory.computeIfAbsent(ruleType.category, { sortedSetOf(RuleContainer.ruleContainerComparator) }).add(it)
            }
        })
        return this
    }

    override fun fireRules(category: String, t: T): Boolean {
        return !ruleContainersByCategory.getOrDefault(category, sortedSetOf(RuleContainer.ruleContainerComparator)).filter { it.fire(t) }.isEmpty()
    }

    override fun fireRules(t: T): Boolean {
        return !ruleContainersByRuleType.values.filter { it.fire(t) }.isEmpty()
    }

    companion object {
        private val serialVersionUID = 0L
    }
}

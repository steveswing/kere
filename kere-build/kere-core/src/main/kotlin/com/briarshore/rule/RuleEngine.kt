package com.briarshore.rule

import java.io.Serializable

interface RuleEngine<T> : Serializable {
    fun addRuleContainers(ruleContainers: Collection<RuleContainer<T>>?): RuleEngine<T>

    fun fireRules(t: T): Boolean

    fun fireRules(category: String, t: T): Boolean

    fun add(ruleType: RuleType?, rule: Rule<T>?)
}

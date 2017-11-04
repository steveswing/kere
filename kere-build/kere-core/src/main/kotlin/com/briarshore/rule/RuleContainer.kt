package com.briarshore.rule

import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.io.Serializable
import java.util.Comparator

open class RuleContainer<T> : Comparable<RuleContainer<T>>, Serializable {
    var ruleType: RuleType? = null

    @XStreamAsAttribute private var name: String? = null
    private val rules = mutableListOf<Rule<T>>()
    val ruleCount: Int
        get() = if (null == ruleType && "ruleTypes".equals(name, true)) RuleType.prioritizedRuleTypes.count() else rules.size

    constructor() {}

    constructor(ruleType: RuleType?, name: String?) {
        this.ruleType = ruleType
        this.name = name
    }

    fun add(rule: Rule<T>?): RuleContainer<T> {
        if (!rule?.conditions?.isEmpty()!! && !rule.actions.isEmpty()) {
            rules.add(rule)
        }
        return this
    }

    fun getRules(): Collection<Rule<T>> {
        return rules.toList()
    }

    fun setRules(rules: Collection<Rule<T>>?) {
        this.rules.clear()
        if (!rules?.isEmpty()!!) {
            addAll(rules)
        }
    }

    fun addAll(rules: Collection<Rule<T>?>?) {
        if (!rules?.isEmpty()!!) {
            rules.forEach { add(it) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleContainer<*>

        if (ruleType != other.ruleType) return false
        if (name != other.name) return false
        if (rules != other.rules) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ruleType?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + rules.hashCode()
        return result
    }

    fun fire(t: T): Boolean {
        return rules.any({ it.fire(t) })
    }

    override operator fun compareTo(other: RuleContainer<T>): Int {
        return if (this === other) 0 else RuleType.ruleTypePriorityComparator.compare(this.ruleType, other.ruleType)
    }

    override fun toString(): String {
        return name.orEmpty()
    }

    companion object {
        @Transient
        val ruleContainerComparator = { dis: RuleContainer<*>?, dat: RuleContainer<*>? ->
            if (dis === dat) 0
            else if (null != dis && null != dat) RuleType.ruleTypePriorityComparator.compare(dis.ruleType, dat.ruleType)
            else if (null == dis) -1 else 1
        } as Comparator<RuleContainer<*>>
        private val serialVersionUID = 0L
    }
}

package com.briarshore.rule

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition
import com.briarshore.util.Strings
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import java.util.Objects

@XStreamAlias("rule") open class RuleImpl<T> : Rule<T> {
    final override val ruleType: RuleType
    @XStreamAsAttribute final override val id: Int
    @XStreamAsAttribute final override val name: String
    @XStreamAsAttribute open var description: String? = null
    override val conditions = mutableSetOf<Condition<T>>()
    override val actions = mutableSetOf<Action<T>>()

    constructor(id: Int, ruleType: RuleType, name: String) {
        this.id = id
        this.ruleType = ruleType
        this.name = name
    }

    constructor(id: Int, ruleType: RuleType, name: String, conditions: Collection<Condition<T>>,
            actions: Collection<Action<T>>, description: String) {
        this.id = id
        this.ruleType = ruleType
        this.name = name
        if (!Strings.isBlank(description)) {
            this.description = description
        }

        addConditions(conditions)
        addActions(actions)
    }

    override fun add(c: Condition<T>): Rule<T> {
        if (!conditions.contains(c)) {
            conditions.add(c)
        }
        return this
    }

    override fun add(a: Action<T>): Rule<T> {
        if (!actions.contains(a)) {
            actions.add(a)
        }
        return this
    }

    override fun fire(t: T): Boolean {
        if (conditions.all { it.test(t) }) {
            actions.forEach { it.perform(this, t) }
            return true
        }
        return false
    }

    fun addConditions(conditions: Collection<Condition<T>>?) {
        if (null != conditions) {
            this.conditions.addAll(conditions)
        }
    }

    fun addActions(actions: Collection<Action<T>>?) {
        if (null != actions) {
            this.actions.addAll(actions)
        }
    }

    fun remove(c: Condition<T>?): Boolean {
        return null != c && conditions.contains(c) && conditions.remove(c)
    }

    fun remove(a: Action<T>?): Boolean {
        return null != a && actions.contains(a) && actions.remove(a)
    }

    override fun toString(): String {
        return Objects.toString(id, "")
    }

    override operator fun compareTo(other: Rule<T>): Int {
        var result = if (this === other) 0 else ruleType.compareTo(other.ruleType)
        if (0 == result) {
            result = id.compareTo(other.id)
            if (0 == result) {
                result = name.compareTo(other.name)
            }
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleImpl<*>

        if (ruleType != other.ruleType) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (conditions != other.conditions) return false
        if (actions != other.actions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ruleType.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + conditions.hashCode()
        result = 31 * result + actions.hashCode()
        return result
    }

    companion object {
        private val serialVersionUID = 0L
    }
}

package com.briarshore.rule

import java.io.Serializable

import com.briarshore.rule.actions.Action
import com.briarshore.rule.conditions.Condition

interface Rule<T> : Comparable<Rule<T>>, Serializable {
    val ruleType: RuleType

    val id: Int

    val name: String

    val conditions: Collection<Condition<T>>

    val actions: Collection<Action<T>>

    fun add(c: Condition<T>): Rule<T>

    fun add(a: Action<T>): Rule<T>

    fun fire(t: T): Boolean
}

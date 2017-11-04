package com.briarshore.rule.actions

import java.io.Serializable

import com.briarshore.rule.Rule

@FunctionalInterface
interface Action<T> : Serializable {
    fun perform(rule: Rule<T>, t: T)
}

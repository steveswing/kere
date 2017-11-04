package com.briarshore.rule.conditions

import java.io.Serializable

@FunctionalInterface
interface Condition<T> : Serializable {
    fun test(t: T?): Boolean
}

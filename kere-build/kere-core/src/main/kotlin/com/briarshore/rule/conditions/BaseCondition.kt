package com.briarshore.rule.conditions

open class BaseCondition<T> @JvmOverloads constructor(protected var affirmative: Boolean = true) : Condition<T> {

    override fun test(t: T?): Boolean {
        return !affirmative
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseCondition<*>

        return affirmative == other.affirmative
    }

    override fun hashCode(): Int {
        return affirmative.hashCode()
    }

    companion object {
        val serialVersionUID = 1L
    }
}

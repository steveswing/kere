package com.briarshore.rule.actions

import com.briarshore.rule.Rule

class BaseAction<T> : Action<T> {

    override fun perform(rule: Rule<T>, t: T) {
    }

    companion object {
        val serialVersionUID = 1L
    }
}

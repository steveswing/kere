package com.briarshore.rule

import com.briarshore.rule.conditions.Condition
import java.io.Serializable
import java.util.Collections

class DiagnosticsResult<T> : Serializable {
    private val results = linkedMapOf<Rule<T>, MutableMap<Boolean, MutableCollection<Condition<T>>>>()
    var subject: T? = null

    constructor() {}

    constructor(subject: T) {
        this.subject = subject
    }

    fun getResults(): Map<Rule<T>, Map<Boolean, Collection<Condition<T>>>> {
        return Collections.unmodifiableMap(results)
    }

    fun recordResult(rule: Rule<T>, diagnosticKey: Boolean, condition: Condition<T>) {
        val m: MutableMap<Boolean, MutableCollection<Condition<T>>> = results.computeIfAbsent(rule) { mutableMapOf() }
        m.computeIfAbsent(diagnosticKey) { mutableListOf() }.add(condition)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}

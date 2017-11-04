package com.briarshore.rule

class DiagnosticRuleImpl<T>(private var rule: Rule<T>?, diagnosticsResults: MutableMap<T, DiagnosticsResult<T>>) : RuleImpl<T>(rule?.id!!, rule.ruleType, rule.name) {
    private var diagnosticsResults: MutableMap<T, DiagnosticsResult<T>>? = diagnosticsResults

    override fun fire(t: T): Boolean {
        if (null != rule) {
            val diagnosticsResult = diagnosticsResults?.computeIfAbsent(t, { DiagnosticsResult(t) })
            val r = rule
            r!!.conditions.forEach { diagnosticsResult?.recordResult(r, it.test(t), it) }
            return r.fire(t)
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass !== other.javaClass) {
            return false
        }

        other as DiagnosticRuleImpl<*>?

        return !if (rule != null) rule != other.rule else other.rule != null
    }

    override fun hashCode(): Int {
        return rule?.hashCode() ?: 0
    }

    override fun toString(): String {
        return rule?.toString() ?: ""
    }

    companion object {
        private val serialVersionUID = 1L
    }
}

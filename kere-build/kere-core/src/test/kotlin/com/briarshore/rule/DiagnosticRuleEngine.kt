package com.briarshore.rule

import java.util.concurrent.ConcurrentHashMap

import com.thoughtworks.xstream.annotations.XStreamAlias

@XStreamAlias("rules")
class DiagnosticRuleEngine<T> : DefaultRuleEngine<T>() {
    private val diagnosticsResults = ConcurrentHashMap<T, DiagnosticsResult<T>>()

    @Synchronized override fun add(ruleType: RuleType?, rule: Rule<T>?) {
        super.add(ruleType, DiagnosticRuleImpl<T>(rule, diagnosticsResults) as Rule<T>)
    }
}

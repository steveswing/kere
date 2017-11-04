package com.briarshore.rule

class DiagnosticRuleEngineFactory<T> : RuleEngineFactory<T>() {
    override fun getRuleEngine() = DiagnosticRuleEngine<T>()
}

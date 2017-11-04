package com.briarshore.rule

/**
 * Class: DiagnosticRuleContainer
 */
class DiagnosticRuleContainer<T>(ruleType: RuleType, name: String) : RuleContainer<T>(ruleType, name) {

    fun diagnose(o: DiagnosticsResult<T>): Boolean {

        for (r in getRules()) {
            if (r is DiagnosticRuleImpl<*>) {
                //                ((DiagnosticRuleImpl<T>)r).diagnose(o);
            }
        }

        return fire(o.subject!!)
    }
}

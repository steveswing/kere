package com.briarshore.rule

import java.io.IOException
import java.io.InputStream

import org.dozer.DozerBeanMapper

/**
 * Class: RuleEngineFactory
 */
open class RuleEngineFactory<T> {
    protected open var ruleEngine: RuleEngine<T>? = null

    @Throws(IOException::class)
    @JvmOverloads open fun getRuleEngine(dozerBeanMapper: DozerBeanMapper, inputStream: InputStream? = null): RuleEngine<T> {
        if (null == ruleEngine && null != inputStream) {
            ruleEngine = RuntimeRuleLoader(dozerBeanMapper, getRuleEngine()).read(inputStream) as RuleEngine<T>
        }
        return ruleEngine as RuleEngine<T>
    }

    protected open fun getRuleEngine() = DefaultRuleEngine<T>()

    companion object {
        protected var ruleEngineFactory: RuleEngineFactory<*>? = null

        fun <T> getInstance(): RuleEngineFactory<T> {
            if (null == ruleEngineFactory) {
                ruleEngineFactory = RuleEngineFactory<T>()
            }
            return ruleEngineFactory as RuleEngineFactory<T>
        }
    }
}

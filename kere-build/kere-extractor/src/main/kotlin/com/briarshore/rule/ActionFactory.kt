package com.briarshore.rule

import org.apache.commons.lang3.BooleanUtils
import org.dozer.DozerBeanMapper
import org.slf4j.LoggerFactory

/**
 * Class: ActionFactory
 */
class ActionFactory<T> private constructor() {
    companion object {
        val log = LoggerFactory.getLogger(ActionFactory::class.java)
        var dozerBeanMapper: DozerBeanMapper? = null

        fun asRequired(s: String): Boolean {
            return BooleanUtils.toBoolean(s)
        }
    }
}

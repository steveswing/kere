package com.briarshore.rule

import com.google.common.collect.Maps
import com.google.common.collect.Table
import org.apache.commons.logging.LogFactory
import java.io.IOException
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.Properties
import java.util.TreeMap

/**
 * Class: RuleContainerBuilderFactory
 */
class RuleContainerBuilderFactory<T> {

    fun findRuleContainerBuilder(ruleType: RuleType): RuleContainerBuilder<T> {
        return buildersByRuleType[ruleType] as RuleContainerBuilder<T>
    }

    companion object {
        private val log = LogFactory.getLog(RuleContainerBuilderFactory::class.java)
        private val builderClasses = initializeBuilderClasses<Any>()
        private val buildersByString = TreeMap<String, RuleContainerBuilder<*>>()
        private val buildersByRuleType = TreeMap<RuleType, RuleContainerBuilder<*>>()

        fun <T> initializeBuilderClasses(): Map<String, Class<out RuleContainerBuilder<*>>> {
            try {
                val ruleBuilderProperties = Properties()
                ruleBuilderProperties.load(RuleContainerBuilderFactory::class.java.getResourceAsStream("/rule-builders.properties"))

                val ruleBuilderClassNames = Maps.fromProperties(ruleBuilderProperties)
                val result = TreeMap<String, Class<out RuleContainerBuilder<*>>>()
                result.putAll(ruleBuilderClassNames.mapValues { Class.forName(it.value) as Class<out RuleContainerBuilder<T>> });
                return result
/*
                Maps.transformValues(ruleBuilderClassNames, );
                result.putAll(Maps.filterValues(Maps.transformValues(ruleBuilderClassNames, Function<String, Class<out RuleContainerBuilder<T>>> { s ->
                    try {
                        return@Function extractClassFromName(s)
                    } catch (e: ClassNotFoundException) {
                        log.error("Invalid class name entry in rule-builders.properties", e)
                        return@Function null
                    }
                }), Predicate<Class<out RuleContainerBuilder<T>>> { Objects.nonNull(it) }))
*/
            } catch (e: IOException) {
                log.error("Error reading rule-builders.properties", e)
            }

            return sortedMapOf()
        }

        @Throws(ClassNotFoundException::class) private fun <T> extractClassFromName(className: String?): Class<out RuleContainerBuilder<T>> {
            return Class.forName(className) as Class<out RuleContainerBuilder<T>>
        }

        @Throws(IllegalAccessException::class, InstantiationException::class, InvocationTargetException::class, NoSuchMethodException::class)
        fun <T> getRuleContainerBuilder(name: String, values: Table<String, String, String>): RuleContainerBuilder<T>? {
            val constructor = extractConstructor<T>(name)
            val builder = constructor?.newInstance(name, RuleType.findByName(name), values)
            if (null != builder) {
                val ruleType = builder.ruleType
                if (null != ruleType) {
                    buildersByString.put(ruleType.name!!, builder)
                    buildersByRuleType.put(ruleType, builder)
                }
            }
            return builder
        }

        @Throws(NoSuchMethodException::class) protected fun <T> extractConstructor(name: String): Constructor<out RuleContainerBuilder<T>>? {
            val cls = builderClasses[name]
            return if (null == cls) null else cls.getConstructor(String::class.java, RuleType::class.java, Table::class.java) as Constructor<out RuleContainerBuilder<T>>
        }
    }
}

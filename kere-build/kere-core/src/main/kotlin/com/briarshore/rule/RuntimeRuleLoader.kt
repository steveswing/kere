package com.briarshore.rule

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.io.xml.Xpp3Driver
import org.dozer.DozerBeanMapper
import org.reflections.Reflections
import org.slf4j.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class RuntimeRuleLoader<T>(@field:Transient protected var dozerBeanMapper: DozerBeanMapper, private val engine: RuleEngine<T>?) {

    protected val xStream: XStream
        get() {
            val xstream = XStream(CustomPureJavaReflectionProvider(), Xpp3Driver())
            xstream.setMode(XStream.NO_REFERENCES)
            xstream.autodetectAnnotations(true)
            xstream.registerConverter(CustomTreeMapConverter(xstream.mapper), XStream.PRIORITY_VERY_HIGH)
            xstream.registerConverter(CustomTreeSetConverter(xstream.mapper), XStream.PRIORITY_VERY_HIGH)
            xstream.registerConverter(XStreamDozerBeanMapperConverter(dozerBeanMapper))
            xstream.registerConverter(XStreamLevelConverter())

            val reflections = Reflections(javaClass.`package`.name)
            val converters = reflections.getSubTypesOf(Converter::class.java)
            converters.remove(XStreamRuleTypeConverter::class.java)
            converters.remove(XStreamDozerBeanMapperConverter::class.java)
            for (cls in converters) {
                try {
                    xstream.registerConverter(cls.newInstance())
                } catch (e: InstantiationException) {
                    log.error("problem creating converter class \"{}\" to register.", cls.simpleName, e)
                } catch (e: IllegalAccessException) {
                    log.error("problem creating converter class \"{}\" to register.", cls.simpleName, e)
                }
            }

            val serializables = reflections.getSubTypesOf(Serializable::class.java)
            for (cls in serializables) {
                xstream.alias(cls.simpleName, cls)
            }

            xstream.useAttributeFor(String::class.java)
            xstream.useAttributeFor(Int::class.javaPrimitiveType)
            xstream.useAttributeFor(Long::class.javaPrimitiveType)
            xstream.useAttributeFor(Boolean::class.javaPrimitiveType)
            registerAliases(xstream)
            xstream.processAnnotations(javaClass)
            xstream.processAnnotations(RuleImpl::class.java)

            return xstream
        }

    fun initialize() {
        val xStream = xStream
        val ruleTypes = xStream.fromXML(javaClass.getResourceAsStream(RULE_TYPE_XML)) as Collection<RuleType>?
        if (null != ruleTypes && !ruleTypes.isEmpty()) {
            ruleTypes.flatMap {
                xStream.fromXML(javaClass.getResourceAsStream("/" + it.name + ".xml")) as Collection<Rule<T>>
            }.forEach { r -> engine!!.add(r.ruleType, r) }
        } else {
            throw RuntimeException(String.format("Failed to find %s to load rule types.", RULE_TYPE_XML))
        }
    }

    @Throws(IOException::class)
    fun read(ruleSource: InputStream?): RuleEngine<T>? {
        if (null != ruleSource) {
            xStream.fromXML(ruleSource, engine)
        }
        return engine
    }

    @Throws(IOException::class)
    fun write(ruleTarget: File) {
        if (null != engine) {
            val bufferedWriter = BufferedWriter(FileWriter(ruleTarget))
            try {
                bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
                bufferedWriter.write(xStream.toXML(engine))
            } finally {
                bufferedWriter.flush()
                bufferedWriter.close()
            }
        }
    }

    protected fun registerAliases(xstream: XStream) {
        xstream.aliasType(LocalTime::class.java.simpleName, LocalTime::class.java)
        xstream.aliasType(LocalDate::class.java.simpleName, LocalDate::class.java)
        xstream.aliasType(LocalDateTime::class.java.simpleName, LocalDateTime::class.java)
        xstream.aliasType("dozer-mapper", DozerBeanMapper::class.java)
        val ruleTypeConverter = XStreamRuleTypeConverter()
        xstream.registerLocalConverter(RuleContainer::class.java, "ruleType", ruleTypeConverter)
        xstream.registerLocalConverter(RuleImpl::class.java, "ruleType", ruleTypeConverter)
        xstream.useAttributeFor(RuleType::class.java, "id")
        xstream.useAttributeFor(RuleType::class.java, "name")
        xstream.useAttributeFor(RuleType::class.java, "description")
        xstream.useAttributeFor(RuleType::class.java, "priority")
    }

    companion object {
        private val RULE_TYPE_XML = "/RuleType.xml"
        private val log = LoggerFactory.getLogger(RuntimeRuleLoader::class.java)
    }
}

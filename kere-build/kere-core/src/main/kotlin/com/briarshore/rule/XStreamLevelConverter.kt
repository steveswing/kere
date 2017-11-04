package com.briarshore.rule

import java.util.HashMap

import org.apache.log4j.Level

import com.briarshore.util.Strings
import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter

/**
 * Class: XStreamLevelConverter
 */
class XStreamLevelConverter : Converter {

    override fun canConvert(type: Class<*>): Boolean {
        return Level::class.java.isAssignableFrom(type)
    }

    override fun marshal(source: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        writer.addAttribute(ATTRIBUTE_NAME, toString(source))
    }

    private fun toString(o: Any): String {
        return (o as? Level)?.toString() ?: Strings.EMPTY
    }

    override fun unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Any {
        return levelsByLevelName[reader.getAttribute(ATTRIBUTE_NAME)] as Any
    }

    companion object {
        val ATTRIBUTE_NAME = "value"
        @Transient private val levelNamesByLevel = initializeLevelNamesByLevel()
        @Transient private val levelsByLevelName = initializeLevelsByLevelName()

        protected fun initializeLevelNamesByLevel(): Map<Level, String> {
            val result = HashMap<Level, String>()
            result.put(Level.ALL, Level.ALL.toString())
            result.put(Level.TRACE, Level.TRACE.toString())
            result.put(Level.DEBUG, Level.DEBUG.toString())
            result.put(Level.INFO, Level.INFO.toString())
            result.put(Level.WARN, Level.WARN.toString())
            result.put(Level.ERROR, Level.ERROR.toString())
            result.put(Level.FATAL, Level.FATAL.toString())
            result.put(Level.OFF, Level.OFF.toString())
            return result
        }

        protected fun initializeLevelsByLevelName(): Map<String, Level> {
            val result = HashMap<String, Level>()
            levelNamesByLevel.entries.forEach { e -> result.put(e.value, e.key) }
            return result
        }
    }
}

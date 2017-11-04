package com.briarshore.rule

import com.briarshore.util.Strings
import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Class: XStreamDateTimeConverter
 */
class XStreamDateTimeConverter : Converter {

    override fun canConvert(cls: Class<*>?): Boolean {
        return null != cls && LocalDateTime::class.java.isAssignableFrom(cls)
    }

    fun toString(o: Any): String {
        return if (o is LocalDateTime) DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(o) else Strings.EMPTY
    }

    fun fromString(s: String): Any? {
        return if (Strings.isBlank(s)) null else DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(s)
    }

    override fun marshal(source: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        writer.addAttribute(ATTRIBUTE_NAME, toString(source))
    }

    override fun unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Any? {
        return fromString(reader.getAttribute(ATTRIBUTE_NAME))
    }

    companion object {
        val ATTRIBUTE_NAME = "value"
    }
}

package com.briarshore.rule

import com.briarshore.util.Strings
import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter

class XStreamRuleTypeConverter : Converter {

    fun fromString(s: String): Any {
        return RuleType.findByName(s) as Any
    }

    override fun canConvert(cls: Class<*>?): Boolean {
        return null != cls && RuleType::class.java.isAssignableFrom(cls)
    }

    fun toString(o: Any): String? {
        return if (o is RuleType) o.name else Strings.EMPTY
    }

    override fun marshal(source: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        writer.addAttribute(ATTRIBUTE_NAME, toString(source))
    }

    override fun unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Any {
        return fromString(reader.getAttribute(ATTRIBUTE_NAME))
    }

    companion object {
        val ATTRIBUTE_NAME = "name"
    }
}

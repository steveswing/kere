package com.briarshore.rule

import org.dozer.DozerBeanMapper

import com.thoughtworks.xstream.converters.Converter
import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter

/**
 * Class: XStreamDozerBeanMapperConverter
 */
class XStreamDozerBeanMapperConverter : Converter {

    constructor() {}

    constructor(dozerBeanMapper: DozerBeanMapper) {
        setDozerBeanMapper(dozerBeanMapper)
    }

    fun getDozerBeanMapper(): DozerBeanMapper? {
        return dozerBeanMapper
    }

    fun setDozerBeanMapper(dozerBeanMapper: DozerBeanMapper?) {
        if (null == XStreamDozerBeanMapperConverter.dozerBeanMapper && null != dozerBeanMapper) {
            XStreamDozerBeanMapperConverter.dozerBeanMapper = dozerBeanMapper
        }
    }

    fun toString(o: Any): String {
        return "defaultMapper"
    }

    fun fromString(s: String): Any? {
        return dozerBeanMapper
    }

    override fun canConvert(cls: Class<*>?): Boolean {
        return null != cls && DozerBeanMapper::class.java.isAssignableFrom(cls)
    }

    override fun marshal(source: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        writer.addAttribute(ATTRIBUTE_NAME, toString(source))
    }

    override fun unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Any? {
        return fromString(reader.getAttribute(ATTRIBUTE_NAME))
    }

    companion object {
        val ATTRIBUTE_NAME = "value"
        private var dozerBeanMapper: DozerBeanMapper? = null
    }
}

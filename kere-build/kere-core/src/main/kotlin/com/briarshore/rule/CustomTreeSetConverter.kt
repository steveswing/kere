package com.briarshore.rule

import java.util.TreeSet

import org.slf4j.LoggerFactory

import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.converters.collections.CollectionConverter
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter
import com.thoughtworks.xstream.mapper.Mapper

/**
 * Class: CustomTreeSetConverter
 */
class CustomTreeSetConverter(mapper: Mapper) : CollectionConverter(mapper, TreeSet::class.java) {

    init {
        logger.debug("CustomTreeSetConverter() constructor")
    }

    override fun marshal(source: Any, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        super.marshal(source, writer, context)
    }

    override fun unmarshal(reader: HierarchicalStreamReader, context: UnmarshallingContext): Any {
        return super.unmarshal(reader, context)
    }

    companion object {
        @Transient private val logger = LoggerFactory.getLogger(CustomTreeSetConverter::class.java)
    }
}

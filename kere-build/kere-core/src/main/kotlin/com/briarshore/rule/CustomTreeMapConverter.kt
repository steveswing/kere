package com.briarshore.rule

import com.thoughtworks.xstream.converters.MarshallingContext
import com.thoughtworks.xstream.converters.UnmarshallingContext
import com.thoughtworks.xstream.converters.collections.TreeMapConverter
import com.thoughtworks.xstream.io.HierarchicalStreamReader
import com.thoughtworks.xstream.io.HierarchicalStreamWriter
import com.thoughtworks.xstream.mapper.Mapper
import org.slf4j.LoggerFactory
import java.util.Comparator
import java.util.TreeMap

/**
 * Class: CustomTreeMapConverter
 */
class CustomTreeMapConverter(mapper: Mapper) : TreeMapConverter(mapper) {

    init {
        log.debug("CustomTreeMapConverter() constructor")
    }

    override fun marshalComparator(comparator: Comparator<*>, writer: HierarchicalStreamWriter, context: MarshallingContext) {
        log.debug("marshalComparator(): {}", context.toString())
    }

    override fun unmarshalComparator(reader: HierarchicalStreamReader, context: UnmarshallingContext, result: TreeMap<*, *>): Comparator<*>? {
        log.debug("unmarshalComparator()")
        return null
    }

    companion object {
        @Transient private val log = LoggerFactory.getLogger(CustomTreeMapConverter::class.java)
    }
}

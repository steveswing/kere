package com.briarshore.rule

import com.briarshore.util.Strings
import com.briarshore.util.Strings.nullSafeCaseInsensitiveComparator
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import org.jetbrains.annotations.Nullable
import java.io.Serializable
import java.util.Objects
import java.util.Optional
import java.util.SortedSet

class RuleType private constructor(@field:XStreamAsAttribute val id: Long, @field:XStreamAsAttribute val name: String?, @field:XStreamAsAttribute val category: String?, @field:XStreamAsAttribute
val description: String?, @field:XStreamAsAttribute val priority: Int?) :
        Comparable<RuleType>, Serializable {

    init {
        ruleTypesByName.put(name, this)
        ruleTypesByCategory.computeIfAbsent(category, { sortedSetOf(ruleTypePriorityComparator) }).add(this)
        prioritizedRuleTypes.add(this)
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, category, description, priority)
    }

    override fun compareTo(@Nullable other: RuleType): Int {
        return ruleTypePriorityComparator.compare(this, other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleType

        if (id != other.id) return false
        if (name != other.name) return false
        if (category != other.category) return false
        if (description != other.description) return false
        if (priority != other.priority) return false

        return true
    }

    class Builder {
        private var id: Long = 0
        private var name: String? = null
        private var category: String? = null
        private var description: String? = null
        private var priority: Int = 0

        fun getId(): Long {
            return id
        }

        fun setId(id: Long): Builder {
            this.id = id
            return this
        }

        fun getName(): String? {
            return name
        }

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun getCategory(): String? {
            return category
        }

        fun setCategory(category: String?): Builder {
            this.category = category
            return this
        }

        fun getDescription(): String? {
            return description
        }

        fun setDescription(description: String?): Builder {
            this.description = description
            return this
        }

        fun getPriority(): Int {
            return priority
        }

        fun setPriority(priority: Int?): Builder {
            this.priority = priority ?: 0
            return this
        }

        fun build(): RuleType {
            if (Strings.isBlank(name)) {
                throw IllegalStateException("name must be non-null, id must be > -1")
            }

            return RuleType(id, name, category, description, priority)
        }
    }

    companion object {
        @Transient internal val ruleTypePriorityComparator: Comparator<RuleType> = object : Comparator<RuleType> {
            override fun compare(rt1: RuleType?, rt2: RuleType?): Int {
                return if (rt1 === rt2) 0
                else if (null != rt1 && null != rt2) if (rt1.priority == rt2.priority) safeCompare(rt1.name, rt2.name) else safeCompare(rt1.priority, rt2.priority)
                else if (null != rt1) -1 else 1
            }

            private fun safeCompare(dis: String?, dat: String?): Int {
                return if (dis === dat) 0 else if (dis != null && dat != null) dis.compareTo(dat) else if (dis == null) -1 else 1
            }

            private fun safeCompare(dis: Int?, dat: Int?): Int {
                return if (dis == dat) 0 else if (dis != null && dat != null) dis.compareTo(dat) else if (dis == null) -1 else 1
            }
        }
        private val serialVersionUID = 0L
        private val ruleTypesByName = mutableMapOf<String, RuleType>().toSortedMap(Comparator(nullSafeCaseInsensitiveComparator))
        private val ruleTypesByCategory = mutableMapOf<String, SortedSet<RuleType>>().toSortedMap(Comparator(nullSafeCaseInsensitiveComparator))
        internal val prioritizedRuleTypes = mutableSetOf<RuleType>().toSortedSet(ruleTypePriorityComparator)


        fun findByName(name: String): RuleType? {
            return ruleTypesByName.get(name)
        }

        fun findByCategory(category: String): SortedSet<RuleType>? {
            return ruleTypesByCategory.get(category)
        }
    }
}

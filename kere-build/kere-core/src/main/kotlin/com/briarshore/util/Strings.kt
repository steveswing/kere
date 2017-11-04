package com.briarshore.util

object Strings {
    val EMPTY = ""
    val nullSafeCaseInsensitiveComparator: (String?, String?) -> Int = { s1: String?, s2: String? -> trimToEmpty(s1).compareTo(trimToEmpty(s2), true) }
    private fun trimToEmpty(s: String?): String {
        return if (null == s) EMPTY else s.trim()
    }

    fun isBlank(s: String?): Boolean {
        return 0 == trimToEmpty(s).length
    }

    fun newCaseInsensitiveSet(strings: Array<String>?): Set<String> {
        return if (null == strings || strings.isEmpty()) setOf() else sortedSetOf(nullSafeCaseInsensitiveComparator, strings) as Set<String>
    }
}

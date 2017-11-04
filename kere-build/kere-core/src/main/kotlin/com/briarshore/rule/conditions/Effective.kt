package com.briarshore.rule.conditions

import java.lang.String.format
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Effective<T> : BaseCondition<T> {
    private var clock: Clock? = Clock.systemDefaultZone()
        get() = field
    private var effectiveDate: LocalDateTime? = null
        get() = field
    private var expirationDate: LocalDateTime? = null
        get() = field

    constructor() : super(true) {}

    constructor(clock: Clock?) : super(true) {
        this.clock = clock
    }

    constructor(clock: Clock?, effectiveDate: LocalDateTime?, expirationDate: LocalDateTime?) : super(true) {
        this.clock = clock
        this.effectiveDate = effectiveDate
        this.expirationDate = expirationDate
    }

    constructor(effectiveDate: LocalDateTime?, expirationDate: LocalDateTime?) : super(true) {
        this.effectiveDate = effectiveDate
        this.expirationDate = expirationDate
    }

    constructor(affirmative: Boolean, effectiveDate: LocalDateTime?, expirationDate: LocalDateTime?) : super(affirmative) {
        this.effectiveDate = effectiveDate
        this.expirationDate = expirationDate
    }

    constructor(affirmative: Boolean, clock: Clock?, effectiveDate: LocalDateTime?, expirationDate: LocalDateTime?) : super(affirmative) {
        this.clock = clock
        this.effectiveDate = effectiveDate
        this.expirationDate = expirationDate
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Effective<*>

        return clock == other.clock && effectiveDate == other.effectiveDate && expirationDate == other.expirationDate
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (clock?.hashCode() ?: 0)
        result = 31 * result + (effectiveDate?.hashCode() ?: 0)
        result = 31 * result + (expirationDate?.hashCode() ?: 0)
        return result
    }

    override fun test(t: T?): Boolean {
        val now = LocalDateTime.now(clock).truncatedTo(ChronoUnit.DAYS)
        return if (null == effectiveDate && null == expirationDate) affirmative
        else ((null != effectiveDate && !effectiveDate!!.isAfter(now) && null != expirationDate && !expirationDate!!.isBefore(now)) && affirmative)
    }

    override fun toString(): String {
        val result = "now must "
        return if (effectiveDate != null && expirationDate != null) {
            if (effectiveDate!! == expirationDate) {
                format("%sbe %s%s", result, if (affirmative) "exactly " else "any date except ", effectiveDate)
            } else {
                format("%s%sbe between %s and %s (inclusive)", result, if (affirmative) "" else "not ", effectiveDate, expirationDate)
            }
        } else {
            if (effectiveDate == null && expirationDate == null) {
                format("current date may %sbe any value.", if (affirmative) "" else "not ")
            } else {
                format("%s%sbe on or %s%s", result, if (affirmative) "" else "not ", if (effectiveDate == null) "before " else "after ", if (effectiveDate == null) expirationDate else effectiveDate)
            }
        }
    }

    companion object {
        private val serialVersionUID = 1L
    }
}

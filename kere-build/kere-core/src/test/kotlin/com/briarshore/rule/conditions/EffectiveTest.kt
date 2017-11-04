package com.briarshore.rule.conditions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.lang.String.format
import java.time.Clock
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class EffectiveTest {
    private var _default: Condition<*>? = null
    private var _defaultClock: Condition<*>? = null
    private var affirmative: Condition<*>? = null
    private var negative: Condition<*>? = null
    private var affirmativeSameDate: Condition<*>? = null
    private var negativeSameDate: Condition<*>? = null
    private var affirmativeClockSameDate: Condition<*>? = null
    private var negativeClockSameDate: Condition<*>? = null
    private var affirmativeDoesNotExpire: Condition<*>? = null
    private var negativeAlwaysEffective: Condition<*>? = null
    private var effective: LocalDateTime? = null
    private var expires: LocalDateTime? = null
    private var midnight: LocalDateTime? = null
    private var midnightPlusOneDay: LocalDateTime? = null
    private var fixedClock: Clock? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        fixedClock = Clock.fixed(LocalDateTime.of(2016, Month.DECEMBER.value, 31, 0, 0, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC.normalized())
        midnight = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
        _default = Effective<Any>()
        _defaultClock = Effective<Any>(fixedClock!!)
        effective = midnight!!.minusDays(1L)
        expires = midnight!!.plusDays(1L)
        affirmative = Effective<Any>(effective!!, expires!!)
        negative = Effective<Any>(false, effective!!, expires!!)
        affirmativeSameDate = Effective<Any>(true, midnight!!, midnight!!)
        negativeSameDate = Effective<Any>(false, midnight!!, midnight!!)
        affirmativeClockSameDate = Effective<Any>(fixedClock!!, midnight!!, midnight!!)
        midnightPlusOneDay = midnight!!.plusDays(1L)
        negativeClockSameDate = Effective<Any>(false, fixedClock!!, midnight!!, midnightPlusOneDay!!)
        affirmativeDoesNotExpire = Effective<Any>(midnight!!, null)
        negativeAlwaysEffective = Effective<Any>(false, null, midnight)
    }

    @Test
    @Throws(Exception::class)
    fun testNull() {
        assertTrue("expected true", _default!!.test(null))
        assertTrue("expected true", _defaultClock!!.test(null))
        assertTrue("expected true", affirmative!!.test(null))
        assertFalse("expected false", negative!!.test(null))
        assertTrue("expected true", affirmativeSameDate!!.test(null))
        assertFalse("expected false", negativeSameDate!!.test(null))
    }

    @Test
    @Throws(Exception::class)
    fun testToString() {
        val expectedDefault = "current date may be any value."
        assertEquals("expected match", expectedDefault, _default!!.toString())
        val expectedAffirmative = format("now must be between %s and %s (inclusive)", effective, expires)
        assertEquals("expected match", expectedAffirmative, affirmative!!.toString())
        val expectedNegative = format("now must not be between %s and %s (inclusive)", effective, expires)
        assertEquals("expected match", expectedNegative, negative!!.toString())
        val expectedAffirmativeSameDate = format("now must be exactly %s", midnight)
        assertEquals("expected match", expectedAffirmativeSameDate, affirmativeSameDate!!.toString())
        val expectedNegativeSameDate = format("now must be any date except %s", midnight)
        assertEquals("expected match", expectedNegativeSameDate, negativeSameDate!!.toString())
        assertEquals("expected match", format("now must be on or after %s", midnight), affirmativeDoesNotExpire!!.toString())
        assertEquals("expected match", format("now must not be on or before %s", midnight), negativeAlwaysEffective!!.toString())
    }

    @Test
    @Throws(Exception::class)
    fun testEquals() {
        assertSame("expected same", _default, _default)
        assertEquals("expected equals", _default, _default)
        assertEquals("expected equals", affirmative, affirmative)
        assertNotEquals("expected not equals", _default, affirmative)
        assertNotEquals("expected not equals", _default, negative)
        assertNotEquals("expected not equals", _default, negativeSameDate)
        assertNotEquals("expected not equals", negativeSameDate, negativeClockSameDate)
    }

    @Ignore
    @Test
    @Throws(Exception::class)
    fun pojoTests() {
        val effective = _default as Effective<*>?
//        assertNotNull("expected non-null", effective!!.clock)
//        assertNull("expected null", effective.getClock())
//        effective.effectiveDate == null
//        assertNull("expected null", effective.getEffectiveDate())
//        effective.setExpirationDate(null)
//        assertNull("expected null", effective.getExpirationDate())
    }

    @Test
    @Throws(Exception::class)
    fun tCodes() {
        assertNotEquals("expected match", 0, _default!!.hashCode().toLong())
        assertNotEquals("expected match", 0, affirmative!!.hashCode().toLong())
        assertNotEquals("expected match", 0, affirmativeDoesNotExpire!!.hashCode().toLong())
        assertNotEquals("expected match", 0, negative!!.hashCode().toLong())
        assertNotEquals("expected match", 0, negativeAlwaysEffective!!.hashCode().toLong())
    }
}

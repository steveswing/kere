package com.briarshore.rule

import org.apache.commons.lang3.StringUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Class: ConditionFactoryTest
 */
class ConditionFactoryTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeNulls() {
        assertNull("expected null", ConditionFactory.asAffirmative(null))
        assertNull("expected null", ConditionFactory.asAffirmative(StringUtils.EMPTY))
        assertNull("expected null", ConditionFactory.asAffirmative(" "))
        assertNull("expected null", ConditionFactory.asAffirmative("other"))
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeLower() {
        val affirmativeFalse = ConditionFactory.asAffirmative("false")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("true")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeUpper() {
        val affirmativeFalse = ConditionFactory.asAffirmative("FALSE")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("TRUE")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeCapitalized() {
        val affirmativeFalse = ConditionFactory.asAffirmative("False")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("True")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeYesNoLower() {
        val affirmativeFalse = ConditionFactory.asAffirmative("no")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("yes")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeYesNoUpper() {
        val affirmativeFalse = ConditionFactory.asAffirmative("NO")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("YES")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsAffirmativeYesNoCapitalized() {
        val affirmativeFalse = ConditionFactory.asAffirmative("No")
        assertNotNull("expected non-null", affirmativeFalse)
        assertFalse("expected false", affirmativeFalse!!)
        val affirmativeTrue = ConditionFactory.asAffirmative("Yes")
        assertNotNull("expected non-null", affirmativeTrue)
        assertTrue("expected true", affirmativeTrue!!)
    }

    @Test
    @Throws(Exception::class)
    fun testAsSet() {
        assertNull("expected null from null", ConditionFactory.asSet(null))
        assertNull("expected null from StringUtils.EMPTY", ConditionFactory.asSet(StringUtils.EMPTY))
        val empty = ConditionFactory.asSet(",")
        assertNull("expected null from \",\"", empty)
        val one = ConditionFactory.asSet("one")
        assertNotNull("expected non-null from \"one\"", one)
        assertFalse("expected non-empty from \"one\"", one!!.isEmpty())
        val two = ConditionFactory.asSet("one,two")
        assertNotNull("expected non-null with \"one,two\"", two)
        assertFalse("expected non-empty with \"one,two\"", two!!.isEmpty())
        assertEquals("expected match", 2, two.size.toLong())
        val spacedThree = ConditionFactory.asSet("   one   ,  two   , three  ")
        assertNotNull("expected non-null with \"   one   ,  two   , three  \"", spacedThree)
        assertFalse("expected non-empty with \"   one   ,  two   , three  \"", spacedThree!!.isEmpty())
        assertEquals("expected match", 3, spacedThree.size.toLong())
        assertTrue("expected contains \"one\"", spacedThree.contains("one"))
        assertTrue("expected contains \"two\"", spacedThree.contains("two"))
        assertTrue("expected contains \"three\"", spacedThree.contains("three"))
        assertFalse("expected not contains \"four\"", spacedThree.contains("four"))
    }

    @Test
    @Throws(Exception::class)
    fun testAsFunctionCodes() {
    }

    @Test
    @Throws(Exception::class)
    fun testAsReasonCodes() {
    }

    @Test
    @Throws(Exception::class)
    fun testAsRemoteStatusCodes() {
    }

    @Test
    @Throws(Exception::class)
    fun testContainsCompletedSystem() {
    }

    @Test
    @Throws(Exception::class)
    fun testReasonCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testLeftReasonCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testRightReasonCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testSkip() {
    }

    @Test
    @Throws(Exception::class)
    fun testFunctionCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testLeftFunctionCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testRightFunctionCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testScheduledDateDefined() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateDefined() {
    }

    @Test
    @Throws(Exception::class)
    fun testScheduledAfterReceived() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedOnOrBeforeOperationalDayStart() {
    }

    @Test
    @Throws(Exception::class)
    fun testType() {
    }

    @Test
    @Throws(Exception::class)
    fun testDnpScheduledBeforeHoliday() {
    }

    @Test
    @Throws(Exception::class)
    fun testExpectedDateDefined() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateBeforeExpectedDate() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateBeforeEffectiveDate() {
    }

    @Test
    @Throws(Exception::class)
    fun testStartDateAfterCutoff() {
    }

    @Test
    @Throws(Exception::class)
    fun testEffective() {
    }

    @Test
    @Throws(Exception::class)
    fun testAsDateTime() {
    }

    @Test
    @Throws(Exception::class)
    fun testExpired() {
    }

    @Test
    @Throws(Exception::class)
    fun testCompleted() {
    }

    @Test
    @Throws(Exception::class)
    fun testOrderStatus() {
    }

    @Test
    @Throws(Exception::class)
    fun testTypeFunctionPriority() {
    }

    @Test
    @Throws(Exception::class)
    fun testSlaExpectedDateDefined() {
    }

    @Test
    @Throws(Exception::class)
    fun testRemoteStatusCode() {
    }

    @Test
    @Throws(Exception::class)
    fun testRemoteStatusCodeDefined() {
    }

    @Test
    @Throws(Exception::class)
    fun testRemoteStatusCodeBlank() {
    }

    @Test
    @Throws(Exception::class)
    fun testAsTimeZone() {
        assertNull("expected null", ConditionFactory.asTimeZone(null))
        assertNull("expected null", ConditionFactory.asTimeZone(StringUtils.EMPTY))
        assertNull("expected null", ConditionFactory.asTimeZone("Invalid"))
    }

    @Test
    @Throws(Exception::class)
    fun testAsDaysOfWeek() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateDayOfWeek() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateOnOrBeforeAmsOperationalDayStart2015() {
    }

    @Test
    @Throws(Exception::class)
    fun testReceivedDateAfterCutoff() {
    }
}

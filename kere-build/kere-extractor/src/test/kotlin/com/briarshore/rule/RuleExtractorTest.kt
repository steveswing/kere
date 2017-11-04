package com.briarshore.rule

import java.io.File

import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.google.common.collect.Table
import com.google.common.io.Files

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

/**
 * Class: RuleExtractorTest
 */
class RuleExtractorTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @Test
    @Throws(Exception::class)
    fun testExtractRules() {
        val ruleSourcePath = "/src/test/resources/rules.xlsx"
        val ruleSource = File(File("."), Files.simplifyPath(ruleSourcePath))
        //        System.out.println("ruleSource.getCanonicalPath() = " + ruleSource.getCanonicalPath());
        assertTrue(String.format("Expected rule source %s to exist", ruleSourcePath), ruleSource.exists())
        val rules = RuleExtractor.extractRules(ruleSource)
        assertNotNull("expected a non-null collection", rules)
        assertFalse("expected a non-empty collection", rules.isEmpty())
    }
}

package com.briarshore.utility

import org.apache.commons.lang3.StringUtils
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.XSSFCell
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Class: CellAsStringConverterTest
 */
class CellAsStringConverterTest {
    private var defaultConverter: CellAsStringConverter? = null
    private var converter: CellAsStringConverter? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        defaultConverter = CellAsStringConverter()
        converter = CellAsStringConverter(XSSFCell::class.java, String::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertToWithNulls() {
        val defaultActual = defaultConverter!!.convertTo(null, null)
        Assert.assertNull("expected null", defaultActual)

        val actual = converter!!.convertTo(null, null)
        Assert.assertNull("expected null", actual)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertToWithNullAndDefault() {
        val defaultExpected = "default"
        val defaultActual = converter!!.convertTo(null, defaultExpected)
        Assert.assertNotNull("expected non-null", defaultActual)
        Assert.assertEquals("expected non-null", defaultExpected, defaultActual)

        val expected = "default"
        val actual = converter!!.convertTo(null, expected)
        Assert.assertNotNull("expected non-null", actual)
        Assert.assertEquals("expected non-null", expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertFromNulls() {
        val xssfCell = converter!!.convertFrom(null, null)
        Assert.assertNull("expected null", xssfCell)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertFromStringAndNull() {
        val xssfCell = converter!!.convertFrom("expected", null)
        Assert.assertNull("expected null", xssfCell)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertToBlankCellToString() {
        val sxssfWorkbook = SXSSFWorkbook()
        val xssfWorkbook = sxssfWorkbook.xssfWorkbook
        val sheet = xssfWorkbook.createSheet()
        val row = sheet.createRow(1)
        val cell = row.createCell(1)
        cell.setCellType(CellType.BLANK)
        val defaultActual = defaultConverter!!.convertTo(cell)
        sxssfWorkbook.close()
        Assert.assertNotNull("expected non-null value", defaultActual)
        Assert.assertEquals("expected match", StringUtils.EMPTY, defaultActual)
    }

    @Test
    @Throws(Exception::class)
    fun testConvertFromEmptyStringToBlankCell() {
        val sxssfWorkbook = SXSSFWorkbook()
        val xssfWorkbook = sxssfWorkbook.xssfWorkbook
        val sheet = xssfWorkbook.createSheet()
        val row = sheet.createRow(1)
        val cell = row.createCell(1)
        cell.setCellType(CellType.BLANK)
        val defaultActual = defaultConverter!!.convertFrom(StringUtils.EMPTY, cell)
        sxssfWorkbook.close()
        Assert.assertNotNull("expected non-null value", defaultActual)
        Assert.assertEquals("expected match", CellType.BLANK, defaultActual!!.getCellTypeEnum())
    }

    @Test
    @Throws(Exception::class)
    fun testConvertFromNonEmptyStringToStringCell() {
        val sxssfWorkbook = SXSSFWorkbook()
        val xssfWorkbook = sxssfWorkbook.xssfWorkbook
        val sheet = xssfWorkbook.createSheet()
        val row = sheet.createRow(1)
        val cell = row.createCell(1)
        cell.setCellType(CellType.BLANK)
        val defaultActual = defaultConverter!!.convertFrom("true", cell)
        sxssfWorkbook.close()
        Assert.assertNotNull("expected non-null value", defaultActual)
        Assert.assertEquals("expected match", CellType.STRING, defaultActual!!.getCellTypeEnum())
    }
}

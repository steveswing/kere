package com.briarshore.rule

import com.google.common.collect.Maps
import com.google.common.collect.Table
import com.google.common.collect.Tables
import com.google.common.io.Files
import org.apache.commons.lang3.StringUtils
import org.apache.poi.hssf.util.CellReference
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.LinkedHashMap

/**
 * Class: RuleExtractor
 */
object RuleExtractor {

    @Throws(IOException::class, InvalidFormatException::class)
    fun extractRules(ruleSource: File): Map<String, Table<String, String, String>> {
        Files.asByteSource(ruleSource).openStream().use({ inputStream -> return extractRules(inputStream) })
    }

    @Throws(IOException::class, InvalidFormatException::class)
    fun extractRules(ruleSource: InputStream): Map<String, Table<String, String, String>> {
        val rules = LinkedHashMap<String, Table<String, String, String>>()
        WorkbookFactory.create(ruleSource).use { workbook ->
            if (workbook is XSSFWorkbook) {
                val xssfWorkbook = workbook
                val dataFormatter = DataFormatter(true)
                for (xssfSheet in xssfWorkbook) {
                    val sheetName = xssfSheet.sheetName
                    val ruleTable = Tables.newCustomTable(Maps.newLinkedHashMap<String, Map<String, String>>(), { Maps.newLinkedHashMap() })
                    rules.put(sheetName, ruleTable)
                    for (row in xssfSheet) {
                        for (cell in row) {
                            val rowKey = extractRowNbrString(cell.row)
                            val columnKey = extractColumnIndexString(cell)
                            val value = StringUtils.trimToEmpty(dataFormatter.formatCellValue(cell))
                            ruleTable.put(rowKey, columnKey, value)
                        }
                    }
                }
            }
        }
        return rules
    }

    fun extractRowNbrString(row: Row?): String {
        return row?.rowNum?.toString() ?: "undefined"
    }

    fun extractColumnIndexString(cell: Cell?): String {
        return if (null == cell) "undefined" else CellReference.convertNumToColString(cell.columnIndex)
    }
}

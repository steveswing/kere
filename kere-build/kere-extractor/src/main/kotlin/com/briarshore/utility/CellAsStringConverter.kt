package com.briarshore.utility

import com.briarshore.util.Strings
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.XSSFCell
import org.dozer.DozerConverter

/**
 * Class: CellAsStringConverter
 */
class CellAsStringConverter
/**
 * Defines two types, which will take part transformation.
 * As Dozer supports bi-directional mapping it is not known which of the classes is source and
 * which is destination. It will be decided in runtime.
 *
 * @param prototypeA type one
 * @param prototypeB type two
 */
@JvmOverloads constructor(prototypeA: Class<XSSFCell> = XSSFCell::class.java, prototypeB: Class<String> = String::class.java) : DozerConverter<XSSFCell, String>(prototypeA, prototypeB) {
    private val dataFormatter = DataFormatter(true)

    override fun convertTo(source: XSSFCell?, destination: String?): String? {
        return if (null == source) {
            destination
        } else dataFormatter.formatCellValue(source)
    }

    override fun convertFrom(source: String?, destination: XSSFCell?): XSSFCell? {
        if (null != destination) {
            if (Strings.isBlank(source)) {
                destination.setCellType(CellType.BLANK)
            } else {
                destination.setCellValue(source)
            }
        }

        return destination
    }
}

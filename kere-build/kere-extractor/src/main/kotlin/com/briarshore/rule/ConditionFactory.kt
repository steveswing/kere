package com.briarshore.rule

import com.briarshore.rule.conditions.Condition
import com.briarshore.rule.conditions.Effective
import com.briarshore.util.Strings
import com.briarshore.utility.Constants
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.tuple.MutablePair
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Class: ConditionFactory
 */
class ConditionFactory<T> private constructor() {
    companion object {
        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        fun asAffirmative(s: String?): Boolean? {
            return s?.let { BooleanUtils.toBooleanObject(it) }
        }

        fun asSet(s: String?): Set<String>? {
            return s?.split(",")?.filter { StringUtils.isNotBlank(it) }?.map { StringUtils.strip(it) }?.toSortedSet(
                    Comparator { s1, s2 -> StringUtils.defaultString(s1).compareTo(StringUtils.defaultString(s2), true) })?.let { if (it.isEmpty()) null else it }
        }

        fun <T> defined(affirmative: Boolean?): Condition<T>? {
            return affirmative?.let { null }
        }

        fun <T> containsCompletedSystem(systems: Set<String>?, affirmative: Boolean?): Condition<T>? {
            return affirmative?.let { systems?.let { if (it.isEmpty()) null else null } }
        }

        fun <T> reasonCode(affirmative: Boolean?, reasonCodes: Set<String>?): Condition<T>? {
            return if (null == affirmative || null == reasonCodes || reasonCodes.isEmpty()) null else null
        }

        fun <T> leftReasonCode(affirmative: Boolean?, reasonCodes: Set<String>?): Condition<MutablePair<T, T>>? {
            return if (null == affirmative || null == reasonCodes || reasonCodes.isEmpty()) null else null
        }

        fun <T> rightReasonCode(affirmative: Boolean?, reasonCodes: Set<String>?): Condition<MutablePair<T, T>>? {
            return if (null == affirmative || null == reasonCodes || reasonCodes.isEmpty()) null else null
        }

        fun <T> skip(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> functionCode(affirmative: Boolean?, functionCodes: Set<String>?): Condition<T>? {
            return if (null == affirmative || null == functionCodes || functionCodes.isEmpty()) null else null
        }

        fun <T> leftFunctionCode(affirmative: Boolean?, functionCodes: Set<String>?): Condition<MutablePair<T, T>>? {
            return if (null == affirmative || null == functionCodes || functionCodes.isEmpty()) null else null
        }

        fun <T> rightFunctionCode(affirmative: Boolean?, functionCodes: Set<String>?): Condition<MutablePair<T, T>>? {
            return if (null == affirmative || null == functionCodes || functionCodes.isEmpty()) null else null
        }

        fun <T> scheduledDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> receivedDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> scheduledAfterReceived(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> receivedOnOrBeforeOperationalDayStart(affirmative: Boolean?, amsOperationalDayStart: LocalTime?): Condition<T>? {
            return if (null == affirmative || null == amsOperationalDayStart) null else null
        }

        fun <T> type(affirmative: Boolean?, types: Set<String>): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> dnpScheduledBeforeHoliday(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> expectedDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> receivedDateBeforeExpectedDate(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> receivedDateBeforeEffectiveDate(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> startDateAfterCutoff(affirmative: Boolean?, cutoffTime: LocalTime): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> effective(affirmative: Boolean?, effective: LocalDateTime?, expires: LocalDateTime?): Condition<T>? {
            return if (null == affirmative || (null == effective && null == expires)) null else Effective(affirmative, effective, expires)
        }

        fun asDateTime(s: String?): LocalDateTime? {
            return if (Strings.isBlank(s)) null else dateTimeFormatter.parse(s) { temporal -> LocalDateTime.from(temporal) }
        }

        fun <T> expired(affirmative: Boolean?, expired: LocalDateTime?): Condition<T>? {
            return if (null == affirmative || null == expired) null else null
        }

        fun <T> completed(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> orderStatus(affirmative: Boolean?, statusCodes: Set<String>?): Condition<T>? {
            return if (null == affirmative || null == statusCodes || statusCodes.isEmpty()) null else null
        }

        fun <T> typeFunctionPriority(affirmative: Boolean?, values: Set<String>?): Condition<T>? {
            return if (null == affirmative || null == values || values.isEmpty()) null else null
        }

        fun <T> slaExpectedDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> remoteStatusCode(affirmative: Boolean?, remoteStatusCodes: Set<String>?): Condition<T>? {
            return if (null == affirmative || null == remoteStatusCodes || remoteStatusCodes.isEmpty()) null else null
        }

        fun <T> remoteStatusCodeDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> remoteStatusCodeBlank(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun asTimeZone(timeZone: String?): ZoneId? {
            return if (Strings.isBlank(timeZone) || !ZoneId.getAvailableZoneIds().contains(timeZone)) null else ZoneId.of(timeZone)
        }

        fun asLocalTimeWithZone(time: String, timeZone: ZoneId?): LocalTime? {
            return if (Strings.isBlank(time) || null == timeZone) null else LocalTime.parse(time).atDate(LocalDate.now()).atZone(Constants.easternTimeZone).toLocalTime()
        }

        fun asDaysOfWeek(s: String): Set<DayOfWeek>? {
            return StringUtils.split(s, ", ").map { DayOfWeek.valueOf(it) }.toSortedSet()
        }

        fun <T> receivedDateDayOfWeek(affirmative: Boolean?, daysOfWeek: Set<DayOfWeek>?): Condition<T>? {
            return if (null == affirmative || null == daysOfWeek || daysOfWeek.isEmpty()) null else null
        }

        fun <T> receivedDateOnOrBeforeAmsOperationalDayStart2015(affirmative: Boolean?, amsOperationalDayStart: LocalTime?): Condition<T>? {
            return if (null == affirmative || null == amsOperationalDayStart) null else null
        }

        fun <T> receivedDateAfterCutoff(affirmative: Boolean?, cutoffTime: LocalTime?): Condition<T>? {
            return if (null == affirmative || null == cutoffTime) null else null
        }

        fun <T> amsStartDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> endDateDefined(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> scheduledDateAfterCutoff(affirmative: Boolean?, cutoffTime: LocalTime?): Condition<T>? {
            return if (null == affirmative || cutoffTime == null) null else null
        }

        fun <T> endedOnScheduledDate(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }

        fun <T> endDateAfterCutoffTime(affirmative: Boolean?, cutoffTime: LocalTime?): Condition<T>? {
            return if (null == affirmative || null == cutoffTime) null else null
        }

        fun <T> endedBeforeScheduledDate(affirmative: Boolean?): Condition<T>? {
            return if (null == affirmative) null else null
        }
    }
}

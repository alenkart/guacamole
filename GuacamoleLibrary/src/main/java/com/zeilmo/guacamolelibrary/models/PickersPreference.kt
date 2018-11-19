package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R
import java.util.*

open class TimePickerPreference(key: Int): Preference(key) {

    var calendar: Calendar? = Calendar.getInstance()

    init {
        calendar?.clear()
        subTitle = "hh:mm"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_timepicker
}

class DatePickerPreference(key: Int): TimePickerPreference(key) {
    override fun getLayoutId(): Int = R.layout.cardview_preference_datepicker
}
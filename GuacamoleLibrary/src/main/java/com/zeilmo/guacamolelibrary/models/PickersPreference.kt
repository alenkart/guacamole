package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R
import java.util.*

class TimePickerPreference(key: String): BasicPreference(key) {

    var calendar: Calendar? = Calendar.getInstance()

    init {
        calendar?.clear()
        subTitle = "hh:mm"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_timepicker
}

class DatePickerPreference(key: String): BasicPreference(key){

    var calendar: Calendar? = Calendar.getInstance()

    init {
        calendar?.clear()
        subTitle = "YYYY/MM/dd"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_datepicker
}
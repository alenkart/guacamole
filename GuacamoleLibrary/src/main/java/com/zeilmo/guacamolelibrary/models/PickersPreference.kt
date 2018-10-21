package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R

class TimePickerPreference(key: String): BasicPreference(key) {

    var hours: Int = 0
    var minutes: Int = 0

    init {
        subTitle = "00:00"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_timepicker
}

class DatePickerPreference(key: String): BasicPreference(key) {

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    init {
        subTitle = "YYYY/MM/dd"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_datepicker
}
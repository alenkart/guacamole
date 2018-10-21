package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R

class SwitchPreference(key: String): BasicPreference(key)  {

    var status: Boolean = false
    var statusOn: String = "On"
    var statusOff: String = "Off"

    override fun getLayoutId(): Int = R.layout.cardview_preference_switch

    fun getStatusString() = if(status) statusOn else statusOff
}

class CheckBoxPreference(key: String): BasicPreference(key) {

    var status: Boolean = false

    override fun getLayoutId(): Int = R.layout.cardview_preference_checbox
}

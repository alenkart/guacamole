package com.zeilmo.guacamolelibrary.models

import android.text.InputType
import com.zeilmo.guacamolelibrary.R

abstract class AlertPreference(key: Int): Preference(key) {
    var alertTitle: String? = null
    var alertMessage: String? = null
    var alertButton: String? = null
}


class DescriptionPreference(key: Int): TextPreference(key)  {
    override fun getLayoutId(): Int = R.layout.cardview_preference_description
}

open class TextPreference(key: Int): AlertPreference(key)  {
    var inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

    override fun getLayoutId(): Int = R.layout.cardview_preference_text
}


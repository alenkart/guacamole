package com.zeilmo.guacamolelibrary.models

import android.graphics.drawable.Drawable
import com.zeilmo.guacamolelibrary.R

open class Preference(val key: Int) {

    var leftIcon: Drawable? = null
    var rightIcon: Drawable? = null
    var backgroundColor: Int? = null

    var title: String? = null
    var subTitle: String? = null

    var isClickable = true

    open fun getLayoutId(): Int = R.layout.cardview_preference_title
}




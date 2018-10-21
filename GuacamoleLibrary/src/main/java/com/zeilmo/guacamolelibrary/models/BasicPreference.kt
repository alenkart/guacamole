package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R

abstract class BasicPreference(val key: String) {

    var icon: Int? = null
    var title: String? = null
    var subTitle: String? = null

    abstract fun getLayoutId(): Int

}

class TitlePreference(key: String): AlertPreference(key)  {

    override fun getLayoutId(): Int = R.layout.cardview_preference_title
}


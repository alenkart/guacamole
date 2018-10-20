package com.zeilmo.guacamolelibrary.models

import com.zeilmo.guacamolelibrary.R

class SingleListPreference(key: String): AlertPreference(key)  {

    var itemList = arrayOf<String>()
    var selectedItem: Int = 0

    init {
        subTitle = "NO ITEM SELECTED"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_single_list
}

class MultiListPreference(key: String): AlertPreference(key)  {

    var itemList = hashMapOf<String, Boolean>()

    init {
        subTitle = "NO ITEM SELECTED"
    }

    override fun getLayoutId(): Int = R.layout.cardview_preference_multi_list
}


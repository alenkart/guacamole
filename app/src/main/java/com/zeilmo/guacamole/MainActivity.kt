package com.zeilmo.guacamole

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.zeilmo.guacamolelibrary.adapters.PreferenceRecyclerViewAdapter
import com.zeilmo.guacamolelibrary.models.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.HashMap

class MainActivity : AppCompatActivity(),
    PreferenceRecyclerViewAdapter.OnTextListener,
    PreferenceRecyclerViewAdapter.OnBooleanListener,
    PreferenceRecyclerViewAdapter.OnMultiListListener {

    private val prefs = mutableListOf<BasicPreference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpPrefs()
    }

    override fun onChangeData(key: String, list: HashMap<String, Boolean>): HashMap<String, Boolean> {
        return list
    }

    override fun onChangeData(key: String, text: String): String {
        return text
    }

    override fun onDataChange(key: String, status: Boolean): Boolean {
        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        return !status
    }

    private fun setUpPrefs() {

        val comment = resources.getString(R.string.lorem_ipsum_comment)
        val description = resources.getString(R.string.lorem_ipsum_description)

        val titlePref = TextPreference("titlePref")
        titlePref.title = "Title"
        titlePref.subTitle = comment
        titlePref.isClickable = false
        titlePref.backGroundColor = Color.parseColor("#dfe6e9")

        val descriptionPref = DescriptionPreference("descriptionPref")
        descriptionPref.title = "Description"
        descriptionPref.subTitle = description
        descriptionPref.leftIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_black_24dp)
        descriptionPref.rightIcon = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_right_black_24dp)

        val categoryPref = SingleListPreference("categoryPref")
        categoryPref.itemList = arrayOf("A", "B", "C", "D", "E", "F", "G")
        categoryPref.title = "Category"
        categoryPref.alertButton = "Save"
        categoryPref.selectedItem = 3
        categoryPref.leftIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_black_24dp)

        val weeksPref = MultiListPreference("categoryPref")
        weeksPref.title = "Working days"
        weeksPref.alertButton = "Save"
        weeksPref.itemList = hashMapOf(
            "Monday" to true,
            "Tuesday" to true,
            "Wednesday" to false,
            "Thursday" to false,
            "Friday" to false,
            "Saturday" to true,
            "Saturday" to false,
            "Sunday" to true
        )

        val datePickerPref = DatePickerPreference("datePickerPref")
        datePickerPref.title = "Date"

        val fromTimePickerPref = TimePickerPreference("timePickerPref")
        fromTimePickerPref.title = "From"

        val toTimePickerPref = TimePickerPreference("timePickerPref")
        toTimePickerPref.title = "To"

        val statusPref =  SwitchPreference("statusPref")
        statusPref.title = "Status"
        statusPref.subTitle = comment
        statusPref.statusOff = "Inactive"
        statusPref.statusOn = "Active"

        val checkPref =  CheckBoxPreference("statusPref")
        checkPref.title = "Status"
        checkPref.subTitle = comment

        val titleText = TitlePreference("title")
        titleText.title = "Text"

        val titleList = TitlePreference("title")
        titleList.title = "List"
        titleList.backGroundColor = Color.parseColor("#f1f1f1")

        val titleDate = TitlePreference("title")
        titleDate.title = "Date"
        titleDate.backGroundColor = Color.parseColor("#f1f1f1")

        val titleBoolean = TitlePreference("title")
        titleBoolean.title = "Boolean"
        titleBoolean.backGroundColor = Color.parseColor("#dddddd")

        val prefs = mutableListOf(
            titleText,
            titlePref,
            descriptionPref,
            titleList,
            categoryPref,
            weeksPref,
            titleDate,
            datePickerPref,
            fromTimePickerPref,
            toTimePickerPref,
            titleBoolean,
            statusPref,
            checkPref
        )

        this.prefs.addAll(prefs)

        val adapter = PreferenceRecyclerViewAdapter(prefs)
        adapter.fragmentManager = fragmentManager
        adapter.setListener(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}

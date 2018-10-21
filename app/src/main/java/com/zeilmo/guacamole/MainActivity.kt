package com.zeilmo.guacamole

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.zeilmo.guacamolelibrary.adapters.RecyclerViewAdapterPreference
import com.zeilmo.guacamolelibrary.models.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    RecyclerViewAdapterPreference.OnTextListener, RecyclerViewAdapterPreference.OnBooleanListener {

    private val prefs = mutableListOf<BasicPreference>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpPrefs()
    }

    override fun onChangeData(key: String, text: String) {
        Toast.makeText(this, "$key - $text", Toast.LENGTH_SHORT).show()
    }

    override fun onDataChange(key: String, value: Boolean) {
        Toast.makeText(this, " $key- $value", Toast.LENGTH_SHORT).show()
    }

    private fun setUpPrefs() {

        val comment = resources.getString(R.string.lorem_ipsum_comment)
        val description = resources.getString(R.string.lorem_ipsum_description)

        val titlePref = TextPreference("titlePref")
        titlePref.title = "Title"
        titlePref.subTitle = comment

        val categoryPref = SingleListPreference("categoryPref")
        categoryPref.itemList = arrayOf("A", "B", "C", "D", "E", "F", "G")
        categoryPref.title = "Category"
        categoryPref.alertButton = "Save"
        categoryPref.selectedItem = 3

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


        val descriptionPref = DescriptionPreference("descriptionPref")
        descriptionPref.title = "Description"
        descriptionPref.subTitle = description

        val datePickerPref = DatePickerPreference("datePickerPref")
        datePickerPref.title = "Date"
        datePickerPref.year = 50

        val fromTimePickerPref = TimePickerPreference("timePickerPref")
        fromTimePickerPref.title = "From"
        fromTimePickerPref.hours =

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

        val titleDate = TitlePreference("title")
        titleDate.title = "Date"

        val titleBoolean = TitlePreference("title")
        titleBoolean.title = "Boolean"


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

        val adapter = RecyclerViewAdapterPreference(prefs)
        adapter.fragmentManager = fragmentManager
        adapter.onTextListener = this
        adapter.onBooleanListener = this

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

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
    PreferenceRecyclerViewAdapter.TextListener,
    PreferenceRecyclerViewAdapter.BooleanListener,
    PreferenceRecyclerViewAdapter.MultiListListener,
    PreferenceRecyclerViewAdapter.Listener {

    private val prefs = mutableListOf<Preference>()

    enum class PreferenceKey {
        PREF_1,
        PREF_2,
        PREF_3,
        PREF_4,
        PREF_5,
        PREF_6,
        PREF_7,
        PREF_8,
        PREF_9,
        PREF_10,
        PREF_11,
        PREF_12,
        PREF_13,
        PREF_14
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpPrefs()
    }

    override fun onClick(key: Int) {
        Toast.makeText(this, "$key", Toast.LENGTH_SHORT).show()
    }

    override fun onChangeData(key: Int, list: HashMap<String, Boolean>): HashMap<String, Boolean> {
        return list
    }

    override fun onChangeData(key: Int, text: String): String {
        return text
    }

    override fun onDataChange(key: Int, status: Boolean): Boolean {
        Toast.makeText(this, "$status", Toast.LENGTH_SHORT).show()
        return !status
    }

    private fun setUpPrefs() {

        val comment = resources.getString(R.string.lorem_ipsum_comment)
        val description = resources.getString(R.string.lorem_ipsum_description)

        val titlePref = TextPreference(PreferenceKey.PREF_1.ordinal)
        titlePref.title = "Title"
        titlePref.subTitle = comment
        titlePref.isClickable = false
        titlePref.backgroundColor = Color.parseColor("#dfe6e9")

        val titlePref2 = TextPreference(PreferenceKey.PREF_2.ordinal)
        titlePref2.title = "Title"

        val descriptionPref = DescriptionPreference(PreferenceKey.PREF_3.ordinal)
        descriptionPref.title = "Description"
        descriptionPref.subTitle = description
        descriptionPref.leftIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_black_24dp)
        descriptionPref.rightIcon = ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_right_black_24dp)

        val categoryPref = SingleListPreference(PreferenceKey.PREF_4.ordinal)
        categoryPref.itemList = arrayOf("A", "B", "C", "D", "E", "F", "G")
        categoryPref.title = "Category"
        categoryPref.alertButton = "Save"
        categoryPref.selectedItem = 3
        categoryPref.leftIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit_black_24dp)

        val weeksPref = MultiListPreference(PreferenceKey.PREF_5.ordinal)
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

        val datePickerPref = DatePickerPreference(PreferenceKey.PREF_6.ordinal)
        datePickerPref.title = "Date"

        val fromTimePickerPref = TimePickerPreference(PreferenceKey.PREF_7.ordinal)
        fromTimePickerPref.title = "From"

        val toTimePickerPref = TimePickerPreference(PreferenceKey.PREF_8.ordinal)
        toTimePickerPref.title = "To"

        val statusPref =  SwitchPreference(PreferenceKey.PREF_9.ordinal)
        statusPref.title = "Status"
        statusPref.subTitle = comment
        statusPref.statusOff = "Inactive"
        statusPref.statusOn = "Active"

        val checkPref =  CheckBoxPreference(PreferenceKey.PREF_10.ordinal)
        checkPref.title = "Status"
        checkPref.subTitle = comment

        val titleText = Preference(PreferenceKey.PREF_11.ordinal)
        titleText.title = "Text"
        titlePref.isClickable = false

        val titleList = Preference(PreferenceKey.PREF_12.ordinal)
        titleList.title = "List"
        titleList.backgroundColor = Color.parseColor("#f1f1f1")

        val titleDate = Preference(PreferenceKey.PREF_13.ordinal)
        titleDate.title = "Date"
        titleDate.backgroundColor = Color.parseColor("#f1f1f1")

        val titleBoolean = Preference(PreferenceKey.PREF_14.ordinal)
        titleBoolean.title = "Boolean"
        titleBoolean.backgroundColor = Color.parseColor("#dddddd")

        val prefs = mutableListOf(
            titleText,
            titlePref,
            titlePref2,
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

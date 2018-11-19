package com.zeilmo.guacamolelibrary.adapters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.FragmentManager
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zeilmo.guacamolelibrary.R
import com.zeilmo.guacamolelibrary.fragments.DatePickerFragment
import com.zeilmo.guacamolelibrary.fragments.TimePickerFragment
import com.zeilmo.guacamolelibrary.models.*
import java.util.*

data class PreferenceRecyclerViewAdapter(
    val preferences: MutableList<Preference>
) : RecyclerView.Adapter<PreferenceRecyclerViewAdapter.ViewHolder>() {

    var fragmentManager: FragmentManager? = null

    var listener: Listener? = null
    var datePickerListener: DatePickerListener? = null
    var timePickerListener: TimePickerListener? = null
    var booleanListener: BooleanListener? = null
    var singleListListener: SingleListListener? = null
    var multiListListener: MultiListListener? = null
    var textListener: TextListener? = null

    interface TextListener {
        fun onChangeData(key: Int, text: String): String
    }

    interface SingleListListener {
        fun onChangeData(key: Int, selected: Int): Int
    }

    interface MultiListListener {
        fun onChangeData(key: Int, list: HashMap<String, Boolean>): HashMap<String, Boolean>
    }

    interface BooleanListener {
        fun onDataChange(key: Int, status: Boolean): Boolean
    }

    interface TimePickerListener {
        fun onDataChange(key: Int, hours: Int, minutes: Int): Calendar
    }

    interface DatePickerListener {
        fun onDataChange(key: Int, year: Int, month: Int, day: Int): Calendar
    }

    interface Listener {
        fun onClick(key: Int)
    }

    fun setListener(listener: Any) {

        if (listener is DatePickerListener) {
            this.datePickerListener = listener
        }

        if (listener is TimePickerListener) {
            this.timePickerListener = listener
        }

        if (listener is BooleanListener) {
            this.booleanListener = listener
        }

        if (listener is SingleListListener) {
            this.singleListListener = listener
        }

        if (listener is MultiListListener) {
            this.multiListListener = listener
        }

        if (listener is TextListener) {
            this.textListener = listener
        }

        if (listener is Listener) {
            this.listener = listener
        }

    }

    override fun getItemViewType(position: Int): Int {
        return preferences[position].getLayoutId()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.cardview_preference_datepicker -> DatePickerViewHolder(view)
            R.layout.cardview_preference_timepicker -> TimePickerViewHolder(view)
            R.layout.cardview_preference_switch -> SwitchViewHolder(view)
            R.layout.cardview_preference_checbox -> CheckBoxViewHolder(view)
            R.layout.cardview_preference_single_list -> SingleItemListViewHolder(view)
            R.layout.cardview_preference_multi_list -> MultiItemListViewHolder(view)

            R.layout.cardview_preference_text,
            R.layout.cardview_preference_description -> TextViewHolder(view)

            else -> ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val basicPreference = preferences[position]
        holder.updateData(basicPreference)
    }

    override fun getItemCount(): Int = preferences.size

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val leftIcon: ImageView = view.findViewById(R.id.simplePreferenceLeftIcon)
        val rightIcon: ImageView = view.findViewById(R.id.simplePreferenceRightIcon)
        val title: TextView = view.findViewById(R.id.simplePreferenceTitle)
        val subTitle: TextView = view.findViewById(R.id.simplePreferenceSubTitle)

        init {
            itemView.setOnClickListener(this)
        }

        open fun updateData(preference: Preference) {

            title.text = preference.title
            subTitle.text = preference.subTitle

            if (preference.leftIcon == null) {
                leftIcon.visibility = View.GONE
            } else {
                leftIcon.setImageDrawable(preference.leftIcon)
                leftIcon.visibility = View.VISIBLE
            }

            if (preference.rightIcon == null) {
                rightIcon.visibility = View.GONE
            } else {
                rightIcon.setImageDrawable(preference.rightIcon)
                rightIcon.visibility = View.VISIBLE
            }

            if(preference.backgroundColor != null ) {
                val color = preference.backgroundColor!!
                itemView.setBackgroundColor(color)
            }

            title.visibility = if (preference.title.isNullOrBlank())
                 View.GONE
             else
                 View.VISIBLE

            subTitle.visibility = if (preference.subTitle.isNullOrBlank())
                 View.GONE
            else
                 View.VISIBLE
        }

        fun getCurrentItem(): Preference = preferences[adapterPosition]

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            listener?.onClick(preference.key)

        }
    }

    abstract inner class AlertViewHolder(view: View) : ViewHolder(view) {

        open fun setAlertTitle(alert: AlertDialog.Builder, preference: AlertPreference) {

            val title = if (preference.alertTitle.isNullOrBlank()) {
                preference.title
            } else {
                preference.alertTitle
            }

            alert.setTitle(title)

        }

        open fun setAlertMessage(alert: AlertDialog.Builder, preference: AlertPreference) {
            alert.setMessage(preference.alertMessage)
        }

        open fun setAlertButton(alert: AlertDialog.Builder,
            preference: AlertPreference,
            listener: DialogInterface.OnClickListener?
        ) {

            val text = if (preference.alertButton.isNullOrBlank()) {
                "OK"
            } else {
                preference.alertButton
            }

            alert.setPositiveButton(text, listener)
        }
    }

    inner class TextViewHolder(view: View) : AlertViewHolder(view) {

        override fun onClick(p0: View?) {

            val preference = getCurrentItem() as AlertPreference

            if(!preference.isClickable) return

            if (preference is TextPreference) {

                val editText = EditText(itemView.context)
                val layout = FrameLayout(itemView.context)
                val alert = AlertDialog.Builder(itemView.context)

                editText.setText(preference.subTitle)
                editText.inputType = preference.inputType

                layout.addView(editText)
                layout.setPadding(45, 15, 45, 0)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)
                setAlertButton(alert, preference, DialogInterface.OnClickListener { _, _ ->

                    val result = if (textListener == null) {
                        editText.text.toString()
                    } else {
                        textListener?.onChangeData(preference.key, editText.text.toString())
                    }

                    result?.let {
                        preference.subTitle = result
                        notifyItemChanged(adapterPosition, preference)
                    }
                })

                alert.setView(layout)

                alert.show()
            }
        }
    }

    inner class SingleItemListViewHolder(view: View) : AlertViewHolder(view) {

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is SingleListPreference) {

                val alert = AlertDialog.Builder(itemView.context)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)
                setAlertButton(alert, preference, null)

                alert.setSingleChoiceItems(preference.itemList, preference.selectedItem) { dialog, selectedItem ->

                    val result = if (singleListListener == null)
                        selectedItem
                    else
                        singleListListener?.onChangeData(preference.key, selectedItem)

                    result?.let {
                        preference.selectedItem = result
                        notifyItemChanged(adapterPosition, preference)
                    }

                    dialog.dismiss()
                }

                val dialog = alert.create()
                val listView = dialog.listView
                listView.divider = ColorDrawable(Color.GRAY)
                listView.dividerHeight = 1

                dialog.show()
            }
        }

        override fun setAlertMessage(alert: AlertDialog.Builder, preference: AlertPreference) {}

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is SingleListPreference) {
                subTitle.text = getSelectItem(preference)
            }
        }

        private fun getSelectItem(preference: SingleListPreference) = preference.itemList[preference.selectedItem]
    }

    inner class MultiItemListViewHolder(view: View) : AlertViewHolder(view) {

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is MultiListPreference) {

                val alert = AlertDialog.Builder(itemView.context)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)

                val key = preference.itemList.keys.toTypedArray()
                val values = preference.itemList.values.toBooleanArray()

                alert.setMultiChoiceItems(key, values) { _: DialogInterface?, position: Int, checked: Boolean ->
                    val currentItem = key[position]
                    preference.itemList[currentItem] = checked
                }

                setAlertButton(alert, preference, DialogInterface.OnClickListener { dialog, _ ->

                    val result = if (multiListListener == null)
                        preference.itemList
                    else multiListListener?.onChangeData(preference.key, preference.itemList)

                    result?.let {
                        preference.itemList = result
                        notifyItemChanged(adapterPosition, preference)
                    }

                    dialog?.dismiss()
                })

                val dialog = alert.create()
                val listView = dialog.listView
                listView.divider = ColorDrawable(Color.GRAY)
                listView.dividerHeight = 1

                dialog.show()
            }
        }

        override fun setAlertMessage(alert: AlertDialog.Builder, preference: AlertPreference) {}

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is MultiListPreference) {
                subTitle.text = listToString(preference)
            }

        }

        private fun listToString(preference: MultiListPreference) =
            preference.itemList.filter { it.value }.keys.joinToString { it }
    }

    inner class CheckBoxViewHolder(view: View) : ViewHolder(view) {

        val checkBox: CheckBox = view.findViewById(R.id.simplePreferenceCheckBox)

        init {
            checkBox.isClickable = false
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is CheckBoxPreference) {

                val result = if (booleanListener == null)
                    !checkBox.isChecked
                else
                    booleanListener?.onDataChange(preference.key, checkBox.isChecked)

                result?.let {
                    preference.status = it
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is CheckBoxPreference) {
                checkBox.isChecked = preference.status
            }
        }
    }

    inner class SwitchViewHolder(view: View) : ViewHolder(view) {

        val switch: Switch = view.findViewById(R.id.simplePreferenceSwitch)
        val status: TextView = view.findViewById(R.id.simplePreferenceStatus)

        init {
            switch.isClickable = false
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is SwitchPreference) {

                val result = if (booleanListener == null)
                    !switch.isChecked
                else
                    booleanListener?.onDataChange(preference.key, switch.isChecked)

                result?.let {
                    preference.status = it
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is SwitchPreference) {
                switch.isChecked = preference.status
                status.text = preference.getStatusString()
            }
        }
    }

    inner class TimePickerViewHolder(view: View) : ViewHolder(view), TimePickerDialog.OnTimeSetListener {

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            val timePicker = TimePickerFragment()
            timePicker.listener = this
            timePicker.show(fragmentManager, "timePicker-${preference.key}")
        }

        override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {

            val preference = getCurrentItem()

            if (preference is TimePickerPreference) {

                val result = if (timePickerListener == null) {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.HOUR_OF_DAY, hours)
                    calendar.set(Calendar.MINUTE, minutes)
                    calendar
                } else {
                    timePickerListener?.onDataChange(preference.key, hours, minutes)
                }

                result?.let {
                    preference.calendar = result
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is TimePickerPreference) {

                val hours = preference.calendar?.get(Calendar.HOUR_OF_DAY)
                val minutes = preference.calendar?.get(Calendar.MINUTE)

                val time = "$hours:$minutes"

                subTitle.text = time
            }
        }
    }

    inner class DatePickerViewHolder(view: View) : ViewHolder(view), DatePickerDialog.OnDateSetListener {

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            val datePicker = DatePickerFragment()
            datePicker.listener = this
            datePicker.show(fragmentManager,"datePicker-${preference.key}")
        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

            val preference = getCurrentItem()

            if (preference is DatePickerPreference) {

                val result = if (datePickerListener == null) {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar
                } else {
                    datePickerListener?.onDataChange(preference.key, year, month, day)
                }

                result?.let {
                    preference.calendar = result
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(preference: Preference) {

            super.updateData(preference)

            if (preference is DatePickerPreference) {

                val year = preference.calendar?.get(Calendar.YEAR)
                val month = preference.calendar?.get(Calendar.MONTH)
                val day = preference.calendar?.get(Calendar.DAY_OF_MONTH)

                val date = "$year/$month/$day"

                subTitle.text = date
            }
        }
    }

}

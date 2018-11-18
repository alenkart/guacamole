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
    val preferences: MutableList<BasicPreference>
) : RecyclerView.Adapter<PreferenceRecyclerViewAdapter.ViewHolder>() {

    var fragmentManager: FragmentManager? = null

    var onDatePickerListener: OnDatePickerListener? = null
    var onTimePickerListener: OnTimePickerListener? = null
    var onBooleanListener: OnBooleanListener? = null
    var onSingleListListener: OnSingleListListener? = null
    var onMultiListListener: OnMultiListListener? = null
    var onTextListener: OnTextListener? = null

    interface OnTextListener {
        fun onChangeData(key: String, text: String): String
    }

    interface OnSingleListListener {
        fun onChangeData(key: String, selected: Int): Int
    }

    interface OnMultiListListener {
        fun onChangeData(key: String, list: HashMap<String, Boolean>): HashMap<String, Boolean>
    }

    interface OnBooleanListener {
        fun onDataChange(key: String, status: Boolean): Boolean
    }

    interface OnTimePickerListener {
        fun onDataChange(key: String, hours: Int, minutes: Int): Calendar
    }

    interface OnDatePickerListener {
        fun onDataChange(key: String, year: Int, month: Int, day: Int): Calendar
    }

    fun setListener(listener: Any) {

        if (listener is OnDatePickerListener) {
            this.onDatePickerListener = listener
        }

        if (listener is OnTimePickerListener) {
            this.onTimePickerListener = listener
        }

        if (listener is OnBooleanListener) {
            this.onBooleanListener = listener
        }

        if (listener is OnSingleListListener) {
            this.onSingleListListener = listener
        }

        if (listener is OnMultiListListener) {
            this.onMultiListListener = listener
        }

        if (listener is OnTextListener) {
            this.onTextListener = listener
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

            else -> TitleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val basicPreference = preferences[position]
        holder.updateData(basicPreference)
    }

    override fun getItemCount(): Int = preferences.size

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val leftIcon: ImageView = view.findViewById(R.id.simplePreferenceLeftIcon)
        val rightIcon: ImageView = view.findViewById(R.id.simplePreferenceRightIcon)
        val title: TextView = view.findViewById(R.id.simplePreferenceTitle)
        val subTitle: TextView = view.findViewById(R.id.simplePreferenceSubTitle)

        open fun updateData(basicPreference: BasicPreference) {

            if (basicPreference.leftIcon == null) {
                leftIcon.visibility = View.GONE
            } else {
                leftIcon.setImageDrawable(basicPreference.leftIcon)
                leftIcon.visibility = View.VISIBLE
            }

            if (basicPreference.rightIcon == null) {
                rightIcon.visibility = View.GONE
            } else {
                rightIcon.setImageDrawable(basicPreference.rightIcon)
                rightIcon.visibility = View.VISIBLE
            }

            if (basicPreference.title.isNullOrBlank()) {
                title.visibility = View.GONE
            } else {
                title.text = basicPreference.title
                title.visibility = View.VISIBLE
            }

            if (basicPreference.subTitle.isNullOrBlank()) {
                subTitle.visibility = View.GONE
            } else {
                subTitle.text = basicPreference.subTitle
                subTitle.visibility = View.VISIBLE
            }
        }

        fun getCurrentItem(): BasicPreference = preferences[adapterPosition]
    }

    inner class TitleViewHolder(view: View) : ViewHolder(view) {

        override fun onClick(p0: View?) {}
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

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {

            val preference = getCurrentItem() as AlertPreference

            if(!preference.isClickable) return

            if (preference is TextPreference) {

                val editText = EditText(itemView.context)
                val layout = FrameLayout(itemView.context)
                val alert = AlertDialog.Builder(itemView.context)

                editText.setText(subTitle.text)
                editText.inputType = preference.inputType

                layout.addView(editText)
                layout.setPadding(45, 15, 45, 0)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)
                setAlertButton(alert, preference, DialogInterface.OnClickListener { _, _ ->

                    val result = if (onTextListener == null) {
                        editText.text.toString()
                    } else {
                        onTextListener?.onChangeData(preference.key, editText.text.toString())
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

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is SingleListPreference) {

                val alert = AlertDialog.Builder(itemView.context)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)
                setAlertButton(alert, preference, null)

                alert.setSingleChoiceItems(preference.itemList, preference.selectedItem) { dialog, selectedItem ->

                    val result = if (onSingleListListener == null)
                        selectedItem
                    else
                        onSingleListListener?.onChangeData(preference.key, selectedItem)

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

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is SingleListPreference) {
                subTitle.text = getSelectItem(basicPreference)
            }
        }

        private fun getSelectItem(preference: SingleListPreference) = preference.itemList[preference.selectedItem]
    }

    inner class MultiItemListViewHolder(view: View) : AlertViewHolder(view) {

        init {
            itemView.setOnClickListener(this)
        }

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

                    val result = if (onMultiListListener == null)
                        preference.itemList
                    else onMultiListListener?.onChangeData(preference.key, preference.itemList)

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

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is MultiListPreference) {
                subTitle.text = listToString(basicPreference)
            }

        }

        private fun listToString(preference: MultiListPreference) =
            preference.itemList.filter { it.value }.keys.joinToString { it }
    }

    inner class CheckBoxViewHolder(view: View) : ViewHolder(view) {

        val checkBox: CheckBox = view.findViewById(R.id.simplePreferenceCheckBox)

        init {
            itemView.setOnClickListener(this)
            checkBox.isClickable = false
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is CheckBoxPreference) {

                val result = if (onBooleanListener == null)
                    !checkBox.isChecked
                else
                    onBooleanListener?.onDataChange(preference.key, checkBox.isChecked)

                result?.let {
                    preference.status = it
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is CheckBoxPreference) {
                checkBox.isChecked = basicPreference.status
            }
        }
    }

    inner class SwitchViewHolder(view: View) : ViewHolder(view) {

        val switch: Switch = view.findViewById(R.id.simplePreferenceSwitch)
        val status: TextView = view.findViewById(R.id.simplePreferenceStatus)

        init {
            itemView.setOnClickListener(this)
            switch.isClickable = false
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            if (preference is SwitchPreference) {

                val result = if (onBooleanListener == null)
                    !switch.isChecked
                else
                    onBooleanListener?.onDataChange(preference.key, switch.isChecked)

                result?.let {
                    preference.status = it
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is SwitchPreference) {
                switch.isChecked = basicPreference.status
                status.text = basicPreference.getStatusString()
            }
        }
    }

    inner class TimePickerViewHolder(view: View) : ViewHolder(view), TimePickerDialog.OnTimeSetListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            val timePicker = TimePickerFragment()
            timePicker.listener = this
            timePicker.show(fragmentManager, preference.key)
        }

        override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {

            val preference = getCurrentItem()

            if (preference is TimePickerPreference) {

                val result = if (onTimePickerListener == null) {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.HOUR_OF_DAY, hours)
                    calendar.set(Calendar.MINUTE, minutes)
                    calendar
                } else {
                    onTimePickerListener?.onDataChange(preference.key, hours, minutes)
                }

                result?.let {
                    preference.calendar = result
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is TimePickerPreference) {

                val hours = basicPreference.calendar?.get(Calendar.HOUR_OF_DAY)
                val minutes = basicPreference.calendar?.get(Calendar.MINUTE)

                val time = "$hours:$minutes"

                subTitle.text = time
            }
        }
    }


    inner class DatePickerViewHolder(view: View) : ViewHolder(view), DatePickerDialog.OnDateSetListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if(!preference.isClickable) return

            val datePicker = DatePickerFragment()
            datePicker.listener = this
            datePicker.show(fragmentManager, preference.key)
        }

        override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

            val preference = getCurrentItem()

            if (preference is DatePickerPreference) {

                val result = if (onDatePickerListener == null) {
                    val calendar = Calendar.getInstance()
                    calendar.clear()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    calendar
                } else {
                    onDatePickerListener?.onDataChange(preference.key, year, month, day)
                }

                result?.let {
                    preference.calendar = result
                    notifyItemChanged(adapterPosition, preference)
                }
            }
        }

        override fun updateData(basicPreference: BasicPreference) {

            super.updateData(basicPreference)

            if (basicPreference is DatePickerPreference) {

                val year = basicPreference.calendar?.get(Calendar.YEAR)
                val month = basicPreference.calendar?.get(Calendar.MONTH)
                val day = basicPreference.calendar?.get(Calendar.DAY_OF_MONTH)

                val date = "$year/${month}/${day}"

                subTitle.text = date
            }
        }
    }

}

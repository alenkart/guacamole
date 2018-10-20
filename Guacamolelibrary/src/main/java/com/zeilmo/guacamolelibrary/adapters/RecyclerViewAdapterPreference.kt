package com.zeilmo.guacamolelibrary.adapters

import android.app.*
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import com.zeilmo.guacamolelibrary.fragments.DatePickerFragment
import com.zeilmo.guacamolelibrary.fragments.TimePickerFragment
import com.zeilmo.guacamolelibrary.models.*
import com.zeilmo.guacamolelibrary.R
import java.util.*


data class RecyclerViewAdapterPreference(
        val preferences: MutableList<BasicPreference>
) : RecyclerView.Adapter<RecyclerViewAdapterPreference.ViewHolder>() {

    var fragmentManager: FragmentManager? = null

    var onDatePickerListener: OnDatePickerListener? = null
    var onTimePickerListener: OnTimePickerListener? = null
    var onBooleanListener: OnBooleanListener? = null
    var onMultiListListener: OnMultiListListener? = null
    var onTextListener: OnTextListener? = null

    interface OnTextListener {
        fun onChangeData(key: String, text: String)
    }

    interface OnSingleListListener {
        fun onChangeData(key: String, selected: Int)
    }

    interface OnMultiListListener {
        fun onChangeData(key: String, list: HashMap<String, Boolean>)
    }

    interface OnBooleanListener {
        fun onDataChange(key: String, value: Boolean)
    }

    interface OnTimePickerListener {
        fun onDataChange(key: String, hours: Int, minutes: Int)
    }

    interface OnDatePickerListener {
        fun onDataChange(key: String, year: Int, month: Int, day: Int)
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
            R.layout.cardview_preference_text, R.layout.cardview_preference_description -> TextViewHolder(view)
            else -> TitleViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val simplePreference = preferences[position]

        holder.title.text = preferences[position].title
        holder.subTitle.text = preferences[position].subTitle

        if (simplePreference.icon == null) {
            holder.icon.visibility = View.GONE
        } else {
            holder.icon.visibility = View.VISIBLE
        }

        if (simplePreference.title.isNullOrBlank()) {
            holder.title.visibility = View.GONE
        } else {
            holder.title.visibility = View.VISIBLE
        }

        if (simplePreference.subTitle.isNullOrBlank()) {
            holder.subTitle.visibility = View.GONE
        } else {
            holder.subTitle.visibility = View.VISIBLE
        }

        holder.updateData(simplePreference)

    }

    override fun getItemCount(): Int = preferences.size

    abstract inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val icon: ImageView = view.findViewById(R.id.simplePreferenceIcon)
        val title: TextView = view.findViewById(R.id.simplePreferenceTitle)
        val subTitle: TextView = view.findViewById(R.id.simplePreferenceSubTitle)

        abstract fun updateData(preference: BasicPreference)

        fun getCurrentItem(): BasicPreference = preferences[adapterPosition]
    }

    inner class TitleViewHolder(view: View) : ViewHolder(view) {

        override fun updateData(preference: BasicPreference) {}

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

        open fun setAlertButton(alert: AlertDialog.Builder, preference: AlertPreference, listener: DialogInterface.OnClickListener?) {

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

        override fun updateData(preference: BasicPreference) {}

        override fun onClick(view: View?) {

            val preference = getCurrentItem() as AlertPreference

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
                    preference.subTitle = editText.text.toString()
                    notifyItemChanged(adapterPosition, preference)
                    onTextListener?.onChangeData(preference.key, editText.text.toString())
                })

                alert.setView(layout)

                alert.show()
            }
        }
    }

    inner class SingleItemListViewHolder(view: View) : AlertViewHolder(view) {

        var listener: OnSingleListListener? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if (preference is SingleListPreference) {

                val alert = AlertDialog.Builder(itemView.context)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)
                setAlertButton(alert, preference, null)

                alert.setSingleChoiceItems(preference.itemList, preference.selectedItem) { dialog, selectedItem ->

                    preference.selectedItem = selectedItem

                    listener?.onChangeData(preference.key, selectedItem)

                    notifyItemChanged(adapterPosition, preference)

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

        override fun updateData(preference: BasicPreference) {

            if(preference is SingleListPreference) {
                subTitle.text = getSelectItem(preference)
            }
        }

        private fun getSelectItem(preference: SingleListPreference)
                = preference.itemList[preference.selectedItem]
    }

    inner class MultiItemListViewHolder(view: View) : AlertViewHolder(view) {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if (preference is MultiListPreference) {

                val alert = AlertDialog.Builder(itemView.context)

                setAlertTitle(alert, preference)
                setAlertMessage(alert, preference)

                val key  = preference.itemList.keys.toTypedArray()
                val values = preference.itemList.values.toBooleanArray()

                alert.setMultiChoiceItems(key ,values) { _: DialogInterface?, position: Int, checked: Boolean ->
                    val currentItem = key[position]
                    preference.itemList[currentItem] = checked
                }

                setAlertButton(alert, preference, DialogInterface.OnClickListener { dialog, _ ->
                    onMultiListListener?.onChangeData(preference.key,  preference.itemList)
                    notifyItemChanged(adapterPosition, preference)
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

        override fun updateData(preference: BasicPreference) {

            if(preference is MultiListPreference) {
                subTitle.text = listToString(preference)
            }

        }

        private fun listToString(preference: MultiListPreference)
                = preference.itemList.filter { it.value }.keys.joinToString { it }
    }

    inner class CheckBoxViewHolder(view: View) : ViewHolder(view) {

        val checkBox: CheckBox = view.findViewById(R.id.simplePreferenceCheckBox)

        init {
            itemView.setOnClickListener(this)
            checkBox.isClickable = false
        }

        override fun onClick(p0: View?) {

            val preference = getCurrentItem()

            if (preference is CheckBoxPreference) {

                preference.status =  !preference.status

                notifyItemChanged(adapterPosition, preference)

                onBooleanListener?.onDataChange(preference.key, checkBox.isChecked)

            }

        }

        override fun updateData(preference: BasicPreference) {

            if (preference is CheckBoxPreference) {
                checkBox.isChecked = preference.status
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

            if(preference is SwitchPreference) {

                preference.status = !preference.status

                updateData(preference)

                notifyItemChanged(adapterPosition, preference)

                onBooleanListener?.onDataChange(preference.key, switch.isChecked)
            }

        }

        override fun updateData(preference: BasicPreference) {

            if (preference is SwitchPreference) {
                switch.isChecked = preference.status
                status.text = preference.getStatusString()
            }
        }
    }

    inner class TimePickerViewHolder(view: View) : ViewHolder(view), TimePickerDialog.OnTimeSetListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {

            val timePicker = TimePickerFragment()
            timePicker.listener = this
            timePicker.show(fragmentManager, "timePicker")
        }

        override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {

            val preference = getCurrentItem()

            if(preference is TimePickerPreference) {

                preference.hours = hours
                preference.minutes = minutes

                updateData(preference)

                notifyItemChanged(adapterPosition, preference)

                onTimePickerListener?.onDataChange(preference.key, hours, minutes)
            }


        }

        override fun updateData(preference: BasicPreference) {
            if(preference is TimePickerPreference) {
                val time = "${preference.hours}:${preference.minutes}"
                subTitle.text = time
            }
        }
    }


    inner class DatePickerViewHolder(view: View) : ViewHolder(view), DatePickerDialog.OnDateSetListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val datePicker = DatePickerFragment()
            datePicker.listener = this
            datePicker.show(fragmentManager, "datePicker")
        }

        override fun onDateSet(p0: DatePicker?,  year: Int, month: Int, day: Int) {

            val preference = getCurrentItem()

            if(preference is DatePickerPreference) {

                preference.year = year
                preference.month = month
                preference.day = day

                updateData(preference)

                notifyItemChanged(adapterPosition, preference)

                onDatePickerListener?.onDataChange(preference.key, year, month, day)

            }

        }

        override fun updateData(preference: BasicPreference) {
            if(preference is DatePickerPreference) {
                val date = "${preference.year}/${preference.month}/${preference.day}"
                subTitle.text = date
            }
        }
    }

}

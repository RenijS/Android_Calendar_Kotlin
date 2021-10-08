package com.example.calandertest

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.calandertest.data.calEvent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarAdapter(private val context: Context, arr : ArrayList<String>, private val onItemClick: onItemClick): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private val daysOfMonths = arr

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val dayOfMonth: TextView = view.findViewById(R.id.cellDayText)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dayOfMonth.text = daysOfMonths[position]
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked ${holder.dayOfMonth.text}", Toast.LENGTH_SHORT).show()
            addEventDialog()
        }
    }

    override fun getItemCount(): Int {
        return daysOfMonths.size
    }

    private fun addEventDialog(){
        val dialogBuilder = Dialog(context)
        dialogBuilder.setCancelable(true)
        dialogBuilder.setContentView(R.layout.add_newevent_layout)
        dialogBuilder.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        val eventName = dialogBuilder.findViewById<EditText>(R.id.events_id)
        val eventTime = dialogBuilder.findViewById<TextView>(R.id.eventTime)
        val eventDesc =dialogBuilder.findViewById<TextView>(R.id.events_desc)
        val setTime = dialogBuilder.findViewById<ImageButton>(R.id.setEventTime)
        val addEvent = dialogBuilder.findViewById<Button>(R.id.addEvent)
        setTime.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val tpD = TimePickerDialog(dialogBuilder.context,R.style.ThemeOverlay_AppCompat_Dialog
                , TimePickerDialog.OnTimeSetListener { timePicker, hr, min ->
                    val c = Calendar.getInstance()
                    c.set(Calendar.HOUR_OF_DAY, hr)
                    c.set(Calendar.MINUTE, min)
                    c.timeZone = TimeZone.getDefault()
                    val hformat = SimpleDateFormat("K:mm a", Locale.ENGLISH)
                    val event_time = hformat.format(c.time)
                    eventTime.text = event_time
                },hour, minute, false)
            tpD.show()
        }
        addEvent.setOnClickListener {
            onItemClick.onClick(calEvent(eventName.text.toString(), eventTime.text.toString(), eventDesc.text.toString()))
            dialogBuilder.dismiss()
        }
        dialogBuilder.show()
    }
}
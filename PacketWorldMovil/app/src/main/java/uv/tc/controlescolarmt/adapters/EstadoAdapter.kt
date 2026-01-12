package uv.tc.controlescolarmt.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EstadoAdapter(
    context: Context,
    private val estados: List<String>
) : ArrayAdapter<String>(
    context,
    android.R.layout.simple_spinner_dropdown_item,
    estados
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = estados[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = estados[position]
        return view
    }

}

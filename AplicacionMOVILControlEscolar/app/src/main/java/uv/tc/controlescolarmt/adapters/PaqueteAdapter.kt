package uv.tc.controlescolarmt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import uv.tc.controlescolarmt.R
import uv.tc.controlescolarmt.listener.PaqueteListener
import uv.tc.controlescolarmt.poko.Paquete

class PaqueteAdapter(
    private val paquetes: MutableList<Paquete>,
    private val listener: PaqueteListener,
    gson: Gson
) : RecyclerView.Adapter<PaqueteAdapter.PaqueteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaqueteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paquete, parent, false)
        return PaqueteViewHolder(view)
    }

    override fun getItemCount(): Int = paquetes.size

    override fun onBindViewHolder(holder: PaqueteViewHolder, position: Int) {
        holder.bind(paquetes[position], position)
    }

    fun actualizarLista(nuevaLista: Array<Paquete>) {
        paquetes.clear()
        paquetes.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    inner class PaqueteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvPaqueteDescripcion: TextView =
            itemView.findViewById(R.id.tv_paquete_descripcion)
        private val tvPaquetePeso: TextView =
            itemView.findViewById(R.id.tv_paquete_peso)
        private val tvPaqueteDimensiones: TextView =
            itemView.findViewById(R.id.tv_paquete_dimensiones)
        private val tvNumeroPaquete: TextView =
            itemView.findViewById(R.id.tv_numero_paquete)

        fun bind(paquete: Paquete, position: Int) {
            tvNumeroPaquete.text = "Paquete No.: ${position + 1}"
            tvPaqueteDescripcion.text = "Descripción: ${paquete.descripcion}"
            tvPaquetePeso.text = "${paquete.peso} KG"
            tvPaqueteDimensiones.text =
                "Dimensiones: ${paquete.alto} x ${paquete.ancho} x ${paquete.profundidad}"

            itemView.setOnClickListener {
                listener.onVerDetalle(paquete)
            }
        }
    }
}

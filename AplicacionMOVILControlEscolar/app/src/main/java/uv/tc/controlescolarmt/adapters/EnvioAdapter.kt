package uv.tc.controlescolarmt.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import uv.tc.controlescolarmt.DetalleEnvioActivity
import uv.tc.controlescolarmt.R
import uv.tc.controlescolarmt.listener.EnvioListener
import uv.tc.controlescolarmt.poko.Envio
import uv.tc.controlescolarmt.util.Constantes

class EnvioAdapter(
    private val envios: MutableList<Envio>,
    private val mapaEstados: Map<Int, String>,
    private val mapaEstatus: Map<Int, String>,
    private val listener: EnvioListener
) : RecyclerView.Adapter<EnvioAdapter.EnvioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_envio, parent, false)
        return EnvioViewHolder(view)
    }

    override fun getItemCount(): Int = envios.size

    override fun onBindViewHolder(holder: EnvioViewHolder, position: Int) {
        holder.bind(envios[position])
    }

    fun actualizarLista(nuevaLista: List<Envio>) {
        envios.clear()
        envios.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    inner class EnvioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNumeroGuia: TextView =
            itemView.findViewById(R.id.tv_numero_guia)
        private val tvDireccionDestino: TextView =
            itemView.findViewById(R.id.tv_direccion_destino)
        private val tvEstatus: TextView =
            itemView.findViewById(R.id.tv_estatus)

        fun bind(envio: Envio) {
            tvNumeroGuia.text = "Guía: ${envio.numeroGuia}"

            val estado = mapaEstados[envio.idEstadoDestino] ?: "Desconocido"
            val estatus = mapaEstatus[envio.idEstadosEnvio] ?: "Desconocido"

            tvDireccionDestino.text =
                "${envio.calleDestino} ${envio.numeroDestino}, " +
                        "${envio.coloniaDestino}, ${envio.ciudadDestino}, $estado"

            tvEstatus.text = "Estatus: $estatus"

            itemView.setOnClickListener {
                listener.onVerDetalle(envio)
            }
        }
    }
}

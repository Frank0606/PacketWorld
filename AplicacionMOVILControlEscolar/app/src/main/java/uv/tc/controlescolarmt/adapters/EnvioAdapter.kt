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

/*class EnvioAdapter(
    private val envios: List<Envio>,
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

    inner class EnvioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvGuia: TextView = itemView.findViewById(R.id.tv_guia)
        private val tvCliente: TextView = itemView.findViewById(R.id.tv_contacto)
        private val tvEstado: TextView = itemView.findViewById(R.id.sp_estado_nuevo)
        private val btnDetalle: Button = itemView.findViewById(R.id.btn_perfil )
        private val btnCambiarEstado: Button = itemView.findViewById(R.id.btn_cambiar_estatus)

        fun bind(envio: Envio) {
            tvGuia.text = "Guía: ${envio.numeroGuia}"
            tvCliente.text = "Cliente: ${envio.nombreDestinatario}"
            tvEstado.text = "Estado: ${envio.estatus}"

            btnDetalle.setOnClickListener {
                listener.onVerDetalle(envio)
            }

            btnCambiarEstado.setOnClickListener {
                listener.onCambiarEstatus(envio)
            }
        }
    }
}*/

class EnvioAdapter(
    private val envios: MutableList<Envio>,
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

            tvDireccionDestino.text =
                "${envio.calleDestino} ${envio.numeroDestino}, " +
                        "${envio.coloniaDestino}, ${envio.ciudadDestino}, ${envio.estadoDestino}"

            tvEstatus.text = "Estatus: ${envio.estatus}"

            itemView.setOnClickListener {
                listener.onVerDetalle(envio)
            }
        }
    }
}

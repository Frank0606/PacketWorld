import { URL_WS } from "./constantes.js";

function getCookie(name) {
    const cookieValue = document.cookie.match('(^|[^;]+)\\s*' + name + '\\s*=\\s*([^;]+)');
    return cookieValue ? cookieValue.pop() : null;
}

document.addEventListener('DOMContentLoaded', () => {

    const numeroGuia = getCookie('noG')

    if (numeroGuia !== null && numeroGuia !== '0' && numeroGuia !== undefined && numeroGuia !== '') {

        document.getElementById('labelNumeroGuia').textContent = `Guia: ${numeroGuia}`
        buscarNumeroGuia(numeroGuia).then(envioJson => {

            const envio = envioJson[0]

            document.getElementById('labelOrigen').textContent = envio.origen
            document.getElementById('labelDestino').textContent = `Calle ${envio.calle} #${envio.numeroCasa}. ${envio.colonia}. ${envio.cp}. ${envio.ciudad}. ${envio.estado}`

            buscarPaquetesEnvio(envio.idEnvio).then(paquetes => {

                const contenedorPaquetes = document.getElementById('contenedorPaquetes')
                const divPaquete = document.createElement('div')
                const divDatos = document.createElement('div')
                const labelTitulo = document.createElement('label')
                const labelDatos = document.createElement('label')

                paquetes.forEach(paquete => {
                    const divPaqueteTemp = divPaquete.cloneNode(false)
                    divPaqueteTemp.classList.add('contenedorPaquete')

                    const divDatosIzq = divDatos.cloneNode(false)
                    divDatosIzq.classList.add('divDatosIzq')

                    const labelTituloTemp = labelTitulo.cloneNode(false)
                    labelTituloTemp.classList.add('labelTitulo')
                    labelTituloTemp.innerHTML = `<strong>ID:</strong> ${paquete.idPaquete}`
                    divDatosIzq.appendChild(labelTituloTemp)

                    const labelDatosPeso = labelDatos.cloneNode(false)
                    labelDatosPeso.classList.add('labelDatos')
                    labelDatosPeso.innerHTML = `<strong>Peso:</strong> ${paquete.peso} Kg`;
                    divDatosIzq.appendChild(labelDatosPeso)

                    const labelDatosDescripcion = labelDatos.cloneNode(false)
                    labelDatosDescripcion.classList.add('labelDatos')
                    labelDatosDescripcion.innerHTML = `<strong>Descripción:</strong> ${paquete.descripcion}`
                    divDatosIzq.appendChild(labelDatosDescripcion)

                    divPaqueteTemp.appendChild(divDatosIzq)
                    const divDatosDer = divDatos.cloneNode(false)
                    divDatosDer.classList.add('divDatosDer')

                    const labelDatosAlto = labelDatos.cloneNode(false)
                    labelDatosAlto.classList.add('labelDatos')
                    labelDatosAlto.innerHTML = `<strong>Alto:</strong> ${paquete.alto} cm`
                    divDatosDer.appendChild(labelDatosAlto)

                    const labelDatosAncho = labelDatos.cloneNode(false)
                    labelDatosAncho.classList.add('labelDatos')
                    labelDatosAncho.innerHTML = `<strong>Ancho:</strong> ${paquete.ancho} cm`
                    divDatosDer.appendChild(labelDatosAncho)

                    const labelDatosProfundidad = labelDatos.cloneNode(false)
                    labelDatosProfundidad.classList.add('labelDatos')
                    labelDatosProfundidad.innerHTML = `<strong>Profundidad:</strong> ${paquete.profundidad} cm`
                    divDatosDer.appendChild(labelDatosProfundidad)

                    divPaqueteTemp.appendChild(divDatosDer)
                    contenedorPaquetes.appendChild(divPaqueteTemp)
                })
            })

            ponerHistorialEstados(envio.historialEstados)
        })

    } else {
        window.location.href = 'index.html'
    }
})

async function buscarNumeroGuia(numeroGuia) {
    try {
        const response = await fetch(`${URL_WS}envio/numero-guia/${numeroGuia}`, {
            method: 'GET'
        });
        const data = await response.json();

        if (data.length > 0 && data[0].idEnvio !== -1) {
            return data;
        } else {
            return null;
        }
    } catch (error) {
        return null;
    }
}

async function buscarPaquetesEnvio(idEnvio) {
    try {
        const response = await fetch(`${URL_WS}paquete/id-envio/${idEnvio}`, {
            method: 'GET'
        })
        const data = await response.json()

        if (data.length > 0 && data[0].idEnvio !== -1) {
            return data
        } else {
            return null
        }
    } catch (error) {
        return null
    }
}

function ponerHistorialEstados(historialEstados) {
    const estados = historialEstados.split(',')
    estados.forEach(estado => {
        const estadoDatos = estado.split('_')
        const divEstado = document.createElement('div')
        divEstado.classList.add('divEstado')
        const labelEstado = document.createElement('label')
        labelEstado.classList.add('labelEstado')
        labelEstado.innerHTML = `<strong>Estado:</strong> <u>${estadoDatos[0]}</u><br/><span style="padding-left: 4rem;">Fecha: ${estadoDatos[1]} | ${estadoDatos[2]}</span>`
        divEstado.appendChild(labelEstado)
        document.getElementById('historialEstados').appendChild(divEstado)
    })
}
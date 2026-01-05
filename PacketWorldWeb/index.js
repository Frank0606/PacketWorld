import { URL_WS } from "./constantes.js";

document.getElementById("barraBusqueda").addEventListener('input', function () {
    document.getElementById("pError").style.display = 'none';
});

document.getElementById('btnBuscar').addEventListener('click', function () {
    document.getElementById("pError").style.display = 'none';

    if (document.getElementById("barraBusqueda").value == 0 || document.getElementById("barraBusqueda").value == null || document.getElementById("barraBusqueda").value == undefined) {
        document.getElementById("pError").textContent = 'Escriba un número de guia por favor.';
        document.getElementById("pError").style.display = 'block';
    } else {
        const numeroGuiaBuscar = document.getElementById("barraBusqueda").value;

        const contenedorPrincipal = document.getElementById('contenedorPrincipal');
        const contenedorCargando = document.querySelector('.contenedorCargando');

        contenedorPrincipal.style.animation = 'fadeOut 1s forwards';

        contenedorPrincipal.addEventListener('animationend', function () {
            contenedorPrincipal.style.display = 'none';
            contenedorCargando.style.display = 'flex';

            buscarNumeroGuia(numeroGuiaBuscar).then(esta => {

                setTimeout(function () {
                    contenedorCargando.style.display = 'none';
                    contenedorPrincipal.style.display = 'block';
                    contenedorPrincipal.style.animation = '';

                    if (esta) {
                        const date = new Date();
                        date.setTime(date.getTime() + (10 * 60 * 1000));
                        document.cookie = `noG=${numeroGuiaBuscar};expires=${date.toUTCString()};SameSite=Strict`;
                        window.location.href = 'principal.html';
                    } else {
                        document.getElementById("pError").textContent = 'No se encontro el número de guia.';
                        document.getElementById("pError").style.display = 'block';
                    }
                }, 4000);
            });
        }, { once: true });
    }
});

async function buscarNumeroGuia(numeroGuia) {
    try {
        const response = await fetch(`${URL_WS}envio/numero-guia/${numeroGuia}`, {
            method: 'GET'
        });
        const data = await response.json();

        if (data.length > 0 && data[0].idEnvio !== -1) {
            return true;
        } else {
            return false;
        }
    } catch (error) {
        return false;
    }
}
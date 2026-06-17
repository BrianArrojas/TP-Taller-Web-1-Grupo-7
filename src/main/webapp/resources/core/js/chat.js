const socket = new SockJS('/spring/chatConexion');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Conectado al chat de la mascota ' + idReporte);

    stompClient.subscribe('/sala/reporte/' + idReporte, function(mensaje) {
        const body = JSON.parse(mensaje.body);
        mostrarMensaje(body.remitente, body.contenido);
    });
});

document.getElementById('botonEnviar').addEventListener('click', enviarMensaje);
document.getElementById('campoMensaje').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') enviarMensaje();
});

function enviarMensaje() {
    const contenido = document.getElementById('campoMensaje').value.trim();
    if (!contenido) return;

    const chatDTO = {
        idReporte: idReporte,
        remitente: remitente,
        contenido: contenido
    };

    stompClient.send('/enviar/mensaje', {}, JSON.stringify(chatDTO));
    document.getElementById('campoMensaje').value = '';
}

function mostrarMensaje(remitente, contenido) {
    const divMensajes = document.getElementById('mensajes');
    const nuevoMensaje = document.createElement('p');
    nuevoMensaje.innerHTML = '<strong>' + remitente + ':</strong> ' + contenido;
    divMensajes.appendChild(nuevoMensaje);

    const ventana = document.getElementById('ventanaChat');
    ventana.scrollTop = ventana.scrollHeight;
}
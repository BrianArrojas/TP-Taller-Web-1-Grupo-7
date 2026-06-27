function conectarWebSocket() {
    var socket = new SockJS('/spring/chatConexion');
    window.stompClient = Stomp.over(socket);

    window.stompClient.connect({}, function(frame) {
        console.log('Conectado al chat privado ' + window.codigoChat);

        window.stompClient.subscribe('/chat/' + window.codigoChat, function(mensaje) {
            var body = JSON.parse(mensaje.body);
            mostrarMensaje(body.remitente, body.contenido);
        });
    });
}

document.getElementById('botonEnviar').addEventListener('click', enviarMensaje);
document.getElementById('campoMensaje').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') enviarMensaje();
});

// Enviar mensaje
function enviarMensaje() {
    if (!window.stompClient || !window.stompClient.connected) {
        console.warn('Aún no conectado, espera...');
        return;
    }
    var contenido = document.getElementById('campoMensaje').value.trim();
    if (!contenido) return;

    var chatDTO = {
        idReporte: window.idReporte,
        codigoChat: window.codigoChat,
        remitente: window.remitente,
        contenido: contenido
    };

    window.stompClient.send('/enviar/mensaje', {}, JSON.stringify(chatDTO));
    document.getElementById('campoMensaje').value = '';
}

function mostrarMensaje(remitente, contenido) {
    var divMensajes = document.getElementById('mensajes');
    var nuevoMensaje = document.createElement('p');
    nuevoMensaje.innerHTML = '<strong>' + remitente + ':</strong> ' + contenido;
    divMensajes.appendChild(nuevoMensaje);

    var ventana = document.getElementById('ventanaChat');
    ventana.scrollTop = ventana.scrollHeight;
}

conectarWebSocket();
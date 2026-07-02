(function() {
    var codigoChat = window.codigoChat;
    var idReporte = window.idReporte;
    var remitente = window.remitente || 'Visitante';

    var stompClient = null;

    function cargarHistorial(callback) {
        fetch('/spring/api/chats/' + codigoChat + '/historial')
            .then(function(response) {
                if (!response.ok) throw new Error('Error al cargar historial');
                return response.json();
            })
            .then(function(mensajes) {
                var contenedor = document.getElementById('mensajes');
                if (!contenedor) return;

                contenedor.innerHTML = '';
                mensajes.forEach(function(m) {
                    var fecha = m.fechaFormateada ? new Date(m.fechaFormateada).toLocaleString() : '';
                    var parrafo = document.createElement('p');
                    parrafo.innerHTML = '<strong>' + m.nombreRemitente + ':</strong> ' + m.texto +
                                        (fecha ? ' <small class="text-muted">(' + fecha + ')</small>' : '');
                    contenedor.appendChild(parrafo);
                });
            })
            .catch(function(error) {
                console.warn('No se pudo cargar el historial:', error);
            })
            .finally(function() {
                if (typeof callback === 'function') {
                    callback();
                }
            });
    }

    function conectarWebSocket() {
        var socket = new SockJS('/spring/chatConexion');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/chat/' + codigoChat, function(mensaje) {
                var body = JSON.parse(mensaje.body);
                mostrarMensaje(body.remitente, body.contenido, body.fechaFormateada);
            });
        });
    }

    document.getElementById('botonEnviar').addEventListener('click', enviarMensaje);
    document.getElementById('campoMensaje').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') enviarMensaje();
    });

    function enviarMensaje() {
        if (!stompClient || !stompClient.connected) return;
        var contenido = document.getElementById('campoMensaje').value.trim();
        if (!contenido) return;

        var mensaje = {
            idReporte: idReporte,
            codigoChat: codigoChat,
            remitente: remitente,
            contenido: contenido
        };

        stompClient.send('/enviar/mensaje', {}, JSON.stringify(mensaje));
        document.getElementById('campoMensaje').value = '';
    }

    function mostrarMensaje(remitente, contenido, fecha) {
        var divMensajes = document.getElementById('mensajes');
        var nuevoMensaje = document.createElement('p');
        var fechaStr = fecha ? new Date(fecha).toLocaleString() : '';
        nuevoMensaje.innerHTML = '<strong>' + remitente + ':</strong> ' + contenido +
                                (fechaStr ? ' <small class="text-muted">(' + fechaStr + ')</small>' : '');
        divMensajes.appendChild(nuevoMensaje);

        var ventana = document.getElementById('ventanaChat');
        ventana.scrollTop = ventana.scrollHeight;
    }

    cargarHistorial(conectarWebSocket);
})();
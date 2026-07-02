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
                    mostrarBurbuja(m.nombreRemitente, m.texto, m.fechaFormateada);
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
                mostrarBurbuja(body.remitente, body.contenido, body.fechaFormateada);
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

    function mostrarBurbuja(nombreRemitente, texto, fecha) {
        var divMensajes = document.getElementById('mensajes');
        if (!divMensajes) return;

        var esPropio = (nombreRemitente === remitente);

        var burbuja = document.createElement('div');
        burbuja.className = 'msg-bubble ' + (esPropio ? 'msg-sent' : 'msg-received');

        var fechaStr = fecha ? new Date(fecha).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : '';

        var nombreMostrado = esPropio ? 'Tú' : nombreRemitente;

        burbuja.innerHTML =
            '<div class="msg-header">' +
                '<span>' + nombreMostrado + '</span>' +
                (fechaStr ? '<span>' + fechaStr + '</span>' : '') +
            '</div>' +
            '<div class="msg-content">' + texto + '</div>';

        divMensajes.appendChild(burbuja);
        divMensajes.scrollTop = divMensajes.scrollHeight;
    }

    cargarHistorial(conectarWebSocket);
})();
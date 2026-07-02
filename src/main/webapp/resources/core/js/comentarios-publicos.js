(function() {
    var idReporte = document.body.getAttribute('data-id-reporte');
    if (!idReporte || isNaN(idReporte)) return;

    var stompClient = null;

    function cargarComentarios() {
        fetch('/spring/api/reportes/' + idReporte + '/comentarios')
            .then(function(response) {
                if (!response.ok) throw new Error('Error al cargar comentarios');
                return response.json();
            })
            .then(function(comentarios) {
                var contenedor = document.getElementById('comentarios-container');
                if (!contenedor) return;

                contenedor.innerHTML = '';
                if (comentarios.length === 0) {
                    contenedor.innerHTML = '<div class="text-center py-5 text-muted bg-white rounded-4 border border-dashed">'
                        + '<i class="bi bi-chat-left-text display-4 mb-3 d-block text-secondary"></i>'
                        + 'Aún no hay comentarios. ¡Sé el primero en comentar!</div>';
                    return;
                }

                comentarios.forEach(function(c) {
                    agregarBurbuja(c.nombreRemitente, c.texto, c.fechaFormateada);
                });
            })
            .catch(function(error) {
                console.warn('No se pudieron cargar los comentarios:', error);
            });
    }

    function agregarBurbuja(nombre, texto, fecha) {
        var contenedor = document.getElementById('comentarios-container');
        if (!contenedor) return;

        if (contenedor.querySelector('.text-center.py-5')) {
            contenedor.innerHTML = '';
        }

        var fechaStr = fecha ? new Date(fecha).toLocaleString() : '';
        var inicial = nombre ? nombre.charAt(0).toUpperCase() : 'U';

        var html =
            '<div class="comment-fb">' +
                '<div class="comment-fb-avatar">' + inicial + '</div>' +
                '<div class="comment-fb-body">' +
                    '<div class="comment-fb-name">' + nombre + '</div>' +
                    '<div class="comment-fb-text">' + texto + '</div>' +
                    (fechaStr ? '<div class="comment-fb-date">' + fechaStr + '</div>' : '') +
                '</div>' +
            '</div>';

        contenedor.insertAdjacentHTML('beforeend', html);
    }

    function conectarWebSocket() {
        if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;

        var socket = new SockJS('/spring/chatConexion');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/reporte/' + idReporte + '/foro', function(mensaje) {
                try {
                    var body = JSON.parse(mensaje.body);
                    agregarBurbuja(body.nombreRemitente, body.texto, body.fechaFormateada);
                } catch (e) {
                }
            });
        });
    }

    function enviarComentario() {
        var texto = document.getElementById('textoComentario').value.trim();
        if (!texto) return;

        if (!stompClient || !stompClient.connected) {
            console.warn('WebSocket no conectado aún');
            return;
        }

        var remitente = window.remitente || 'Visitante';
        var comentarioDTO = {
            idReporte: parseInt(idReporte),
            nombreRemitente: remitente,
            texto: texto
        };

        stompClient.send('/enviar/comentario', {}, JSON.stringify(comentarioDTO));
        document.getElementById('textoComentario').value = '';
    }

    document.addEventListener('DOMContentLoaded', function() {
        cargarComentarios();
        conectarWebSocket();

        var btnPublicar = document.getElementById('btn-publicar');
        if (btnPublicar) {
            btnPublicar.addEventListener('click', enviarComentario);
        }
    });
})();
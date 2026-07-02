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
                    contenedor.innerHTML = '<p class="text-muted">No hay comentarios aún.</p>';
                    return;
                }

                comentarios.forEach(function(c) {
                    var fecha = c.fechaFormateada ? new Date(c.fechaFormateada).toLocaleString() : '';
                    var parrafo = document.createElement('p');
                    parrafo.innerHTML = '<strong>' + c.nombreRemitente + ':</strong> ' + c.texto +
                                        (fecha ? ' <small class="text-muted">(' + fecha + ')</small>' : '');
                    contenedor.appendChild(parrafo);
                });
            })
            .catch(function(error) {
            });
    }
    
    function conectarWebSocket() {
        if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;

        var socket = new SockJS('/spring/chatConexion');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/reporte/' + idReporte + '/foro', function(mensaje) {
                try {
                    var body = JSON.parse(mensaje.body);
                    var fecha = body.fechaFormateada ? new Date(body.fechaFormateada).toLocaleString() : '';
                    var contenedor = document.getElementById('comentarios-container');
                    if (!contenedor) return;

                    var parrafo = document.createElement('p');
                    parrafo.innerHTML = '<strong>' + body.nombreRemitente + ':</strong> ' + body.texto +
                                        (fecha ? ' <small class="text-muted">(' + fecha + ')</small>' : '');
                    contenedor.appendChild(parrafo);
                } catch (e) {
                }
            });
        });
    }

    function enviarComentario() {
        var texto = document.getElementById('textoComentario').value.trim();
        if (!texto) return;

        var remitente = window.remitente || 'Visitante';
        var comentarioDTO = {
            idReporte: idReporte,
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
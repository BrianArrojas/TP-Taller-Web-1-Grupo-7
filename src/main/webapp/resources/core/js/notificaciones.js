(function() {
    var userId = window.userId;
    if (!userId) return;

    var stompClient = null;

    function crearContenedorToasts() {
        if (document.getElementById('toast-container')) return;
        var contenedor = document.createElement('div');
        contenedor.id = 'toast-container';
        contenedor.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        document.body.appendChild(contenedor);
    }

    function mostrarToast(titulo, mensaje, enlace) {
        crearContenedorToasts();

        var toastId = 'toast-' + new Date().getTime();
        var html =
            '<div id="' + toastId + '" class="toast align-items-center text-bg-primary border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="5000">' +
            '  <div class="d-flex">' +
            '    <div class="toast-body d-flex align-items-center gap-2">' +
            '      <i class="bi bi-chat-dots-fill fs-5"></i>' +
            '      <div>' +
            '        <strong>' + titulo + '</strong><br>' +
            '        <small>' + mensaje + '</small>' +
            '      </div>' +
            '    </div>' +
            '    <div class="d-flex align-items-center pe-2">' +
            '      <a href="' + enlace + '" class="btn btn-sm btn-light me-2">Ir</a>' +
            '      <button type="button" class="btn-close btn-close-white me-2" data-bs-dismiss="toast" aria-label="Cerrar"></button>' +
            '    </div>' +
            '  </div>' +
            '</div>';

        var contenedor = document.getElementById('toast-container');
        contenedor.insertAdjacentHTML('beforeend', html);

        var toastElement = document.getElementById(toastId);
        var toast = new bootstrap.Toast(toastElement);
        toast.show();

        toastElement.addEventListener('hidden.bs.toast', function() {
            toastElement.remove();
        });
    }

    function conectarNotificaciones() {
        if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;

        var socket = new SockJS('/spring/chatConexion');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function(frame) {
            stompClient.subscribe('/usuario/' + userId + '/chats', function(evento) {
                try {
                    var datos = JSON.parse(evento.body);
                    if (datos.action === 'new_message') {
                        var nombre = datos.nombreMascota || 'una mascota';
                        var enlace = '/spring/chat-privado?codigoChat=' + datos.codigoChat + '&idReporte=' + datos.idReporte;
                        mostrarToast(
                            'Nuevo mensaje',
                            'Has recibido un mensaje en el chat de <strong>' + nombre + '</strong>.',
                            enlace
                        );
                    }
                } catch (e) {
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function() {
        conectarNotificaciones();
    });
})();
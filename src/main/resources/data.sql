DELETE FROM foto;
DELETE FROM reporte_mascota;
DELETE FROM Usuario;

CREATE TABLE IF NOT EXISTS Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    nombre VARCHAR(100) DEFAULT '',
    apellido VARCHAR(100) DEFAULT '',
    telefono VARCHAR(20) DEFAULT '',
    rol VARCHAR(50),
    activo BOOLEAN
);

CREATE TABLE IF NOT EXISTS reporte_mascota (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_de_reporte VARCHAR(50),
    nombre VARCHAR(100),
    especie VARCHAR(50),
    raza VARCHAR(50),
    color VARCHAR(50),
    tamano VARCHAR(50),
    fecha DATE,
    ubicacion VARCHAR(255),
    descripcion TEXT,
    fecha_creacion_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registro_activo BOOLEAN DEFAULT TRUE,
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE IF NOT EXISTS foto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    img VARCHAR(255),
    reporte_mascota_id BIGINT,
    FOREIGN KEY (reporte_mascota_id) REFERENCES reporte_mascota(id)
);

INSERT INTO Usuario (id, email, password, nombre, apellido, telefono, rol, activo) VALUES
(1, 'test@unlam.edu.ar', 'test', 'Admin', 'Principal', '1112345678', 'ADMIN', true),
(2, 'juan.perez@unlam.edu.ar', '123', 'Juan', 'Pérez', '1123456789', 'USER', true);

INSERT INTO reporte_mascota (tipo_de_reporte, nombre, especie, raza, color, tamano, fecha, ubicacion, descripcion, registro_activo, usuario_id) VALUES
('Perdida', 'Firulais', 'Perro', 'Labrador', 'Negro', 'Grande', '2026-06-01', 'Parque Central', 'Lleva collar rojo.', true, 1),
('Encontrada', 'Michi', 'Gato', 'Siamés', 'Blanco', 'Pequeño', '2026-06-02', 'Avenida Siempre Viva', 'Es muy cariñoso.', true, 1),
('Perdida', 'Rocky', 'Perro', 'Bulldog', 'Marrón', 'Mediano', '2026-06-03', 'Plaza Mayor', 'Tiene una mancha blanca en el pecho.', true, 2);

INSERT INTO foto (img, reporte_mascota_id) VALUES
('default-pet.png', 1),
('default-pet.png', 2),
('default-pet.png', 3);
DELETE FROM Foto;
DELETE FROM reporte_mascota;
DELETE FROM Usuario;

INSERT INTO Usuario(id, email, password, rol, activo, nombre, apellido, telefono) VALUES(1, 'test@unlam.edu.ar', 'test', 'ADMIN', true, 'Admin', 'Principal', '1122334455');
INSERT INTO Usuario(id, email, password, rol, activo, nombre, apellido, telefono) VALUES(2, 'admin2@unlam.edu.ar', 'admin123', 'ADMIN', true, 'Maria', 'Gomez', '1166778899');
INSERT INTO Usuario(id, email, password, rol, activo, nombre, apellido, telefono) VALUES(3, 'usuario1@unlam.edu.ar', 'user123', 'USUARIO', true, 'Juan', 'Perez', '1144332211');
INSERT INTO Usuario(id, email, password, rol, activo, nombre, apellido, telefono) VALUES(4, 'usuario2@unlam.edu.ar', 'user123', 'USUARIO', true, 'Ana', 'Lopez', '1155667788');
INSERT INTO Usuario(id, email, password, rol, activo, nombre, apellido, telefono) VALUES(5, 'inactivo@unlam.edu.ar', 'user123', 'USUARIO', false, 'Pedro', 'Inactivo', '1199887766');

SELECT setval(pg_get_serial_sequence('usuario', 'id'), 5);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(1, 'Encontrado', NULL, 'Gato', '2023-01-15', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(1, 'img/0.jpg', 1);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(2, 'Perdido', 'Luna', 'Gato', '2023-02-20', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(2, 'img/1.jpg', 2);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(3, 'Encontrado', NULL, 'Gato', '2023-03-10', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(3, 'img/2.jpg', 3);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(4, 'Perdido', 'Simba', 'Gato', '2023-04-01', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(4, 'img/3.jpg', 4);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(5, 'Encontrado', NULL, 'Gato', '2023-05-12', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(5, 'img/4.jpg', 5);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(6, 'Perdido', 'Nala', 'Gato', '2023-06-22', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(6, 'img/5.jpg', 6);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(7, 'Encontrado', NULL, 'Gato', '2023-07-19', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(7, 'img/7.jpg', 7);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(8, 'Perdido', 'Milo', 'Gato', '2023-08-05', true, 3);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(8, 'img/8.jpg', 8);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(9, 'Encontrado', NULL, 'Gato', '2023-09-14', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(9, 'img/9.jpg', 9);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(10, 'Perdido', 'Leo', 'Gato', '2023-10-02', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(10, 'img/10.jpg', 10);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(11, 'Encontrado', NULL, 'Gato', '2023-11-25', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(11, 'img/11.jpg', 11);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(12, 'Perdido', 'Cleo', 'Gato', '2023-12-30', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(12, 'img/12.jpg', 12);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(13, 'Encontrado', NULL, 'Gato', '2024-01-08', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(13, 'img/13.jpg', 13);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(14, 'Perdido', 'Tom', 'Gato', '2024-02-16', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(14, 'img/14.jpg', 14);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(15, 'Encontrado', NULL, 'Perro', '2024-03-21', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(15, 'img/111.jpg', 15);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(16, 'Perdido', 'Max', 'Perro', '2024-04-03', true, 4);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(16, 'img/112.jpg', 16);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(17, 'Encontrado', NULL, 'Perro', '2024-05-09', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(17, 'img/113.jpg', 17);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(18, 'Perdido', 'Rocky', 'Perro', '2024-06-11', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(18, 'img/114.jpg', 18);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(19, 'Encontrado', NULL, 'Perro', '2024-07-15', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(19, 'img/115.jpg', 19);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(20, 'Perdido', 'Coco', 'Perro', '2024-08-20', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(20, 'img/116.jpg', 20);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(21, 'Encontrado', NULL, 'Perro', '2024-09-28', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(21, 'img/117.jpg', 21);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(22, 'Perdido', 'Toby', 'Perro', '2024-10-31', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(22, 'img/118.jpg', 22);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(23, 'Encontrado', NULL, 'Perro', '2024-11-05', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(23, 'img/119.jpg', 23);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(24, 'Perdido', 'Loki', 'Perro', '2024-12-10', true, 1);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(24, 'img/120.jpg', 24);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(25, 'Encontrado', NULL, 'Perro', '2025-01-15', true, 2);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(25, 'img/121.jpg', 25);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(26, 'Perdido', 'Zeus', 'Perro', '2025-02-20', true, 2);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(26, 'img/122.jpg', 26);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(27, 'Encontrado', NULL, 'Perro', '2025-03-25', true, 2);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(27, 'img/123.jpg', 27);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(28, 'Perdido', 'Apolo', 'Perro', '2025-04-30', true, 2);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(28, 'img/124.jpg', 28);

INSERT INTO reporte_mascota(id, tipo_de_reporte, nombre, especie, fecha, registro_activo, usuario_id) VALUES(29, 'Encontrado', NULL, 'Perro', '2025-05-05', true, 2);
INSERT INTO Foto(id, img, reporte_mascota_id) VALUES(29, 'img/125.jpg', 29);

SELECT setval(pg_get_serial_sequence('reporte_mascota', 'id'), 29);
SELECT setval(pg_get_serial_sequence('foto', 'id'), 29);
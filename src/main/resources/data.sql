DELETE FROM Usuario WHERE email = 'test@unlam.edu.ar';
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);
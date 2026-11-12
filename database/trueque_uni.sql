create database trueque_uni;

use trueque_uni;

create table usuarios (
    id_usuario int auto_increment primary key,
    nombre_alias varchar(100) not null,
    contrasena varchar(255) not null,
    programa varchar(100),
    rol varchar(20) default 'estudiante',
    reputacion decimal(2,1) default 5.0
);

create table categorias (
    id_categoria int auto_increment primary key,
    nombre_categoria varchar(50) not null
);


create table publicaciones (
    id_publicacion int auto_increment primary key,
    titulo varchar(100) not null,
    descripcion text,
    estado_fisico varchar(30) not null,
    estado_publicacion varchar(20) default 'activo',
    tipo_oferta varchar(20) not null,
    imagen varchar(255),
    fecha_publicacion date,
    id_usuario int not null,
    id_categoria int not null,

    foreign key (id_usuario) references usuarios(id_usuario),
    foreign key (id_categoria) references categorias(id_categoria)
);

create table solicitudes (
    id_solicitud int auto_increment primary key,
    tipo_solicitud varchar(20) not null,
    estado varchar(20) default 'pendiente',
    fecha_solicitud date,
    id_usuario int not null,
    id_publicacion int not null,

    foreign key (id_usuario) references usuarios(id_usuario),
    foreign key (id_publicacion) references publicaciones(id_publicacion)
);



create table mensajes (
    id_mensaje int auto_increment primary key,
    mensaje text not null,
    fecha_envio datetime default current_timestamp,
    id_remitente int not null,
    id_destinatario int not null,

    foreign key (id_remitente) references usuarios(id_usuario),
    foreign key (id_destinatario) references usuarios(id_usuario)
);

create table reportes (
    id_reporte int auto_increment primary key,
    motivo text not null,
    fecha_reporte datetime default current_timestamp,
    id_usuario int not null,
    id_publicacion int not null,

    foreign key (id_usuario) references usuarios(id_usuario),
    foreign key (id_publicacion) references publicaciones(id_publicacion)
);

-- pruebas


insert into usuarios
(nombre_alias, contrasena, programa, rol)
values
('Juan', 'clave123', 'Ingenieria de Sistemas', 'estudiante'),
('Laura', 'clave456', 'Fisioterapia', 'estudiante'),
('Carlos', 'clave789', 'Ingenieria Industrial', 'estudiante'),
('Sofia', 'clave321', 'Odontologia', 'estudiante'),
('Admin', 'admin123', 'Administracion de Empresas', 'administrador');

insert into categorias (nombre_categoria)
values
('Libros'),
('Ropa'),
('Tecnologia'),
('Articulos para el hogar');


insert into publicaciones
(titulo, descripcion, estado_fisico, estado_publicacion, tipo_oferta, imagen, fecha_publicacion, id_usuario, id_categoria)
values
('Libro de calculo', 'Libro de calculo en buen estado', 'Buen estado', 'activo', 'intercambio', 'imagenes/libro.jpg', '2026-11-12', 1, 1),
('Chaqueta deportiva', 'Chaqueta que ya no utilizo', 'Usado', 'activo', 'donacion', 'imagenes/chaqueta.jpg', '2026-11-12', 2, 2),
('Audifonos', 'Audifonos funcionando correctamente', 'Buen estado', 'activo', 'intercambio', 'imagenes/audifonos.jpg', '2026-11-12', 3, 3),
('Lampara de escritorio', 'Lampara en buen estado', 'Nuevo', 'activo', 'donacion', 'imagenes/lampara.jpg', '2026-11-12', 4, 4);


insert into solicitudes
(tipo_solicitud, estado, fecha_solicitud, id_usuario, id_publicacion)
values
('intercambio', 'pendiente', '2026-11-12', 2, 1),
('donacion', 'aceptada', '2026-11-12', 3, 2),
('intercambio', 'completada', '2026-11-12', 4, 3);



insert into mensajes
(mensaje, id_remitente, id_destinatario)
values
('Hola, ¿todavia tienes disponible el libro?', 2, 1),
('Hola, si, todavia lo tengo disponible.', 1, 2),
('¿Podemos acordar el intercambio mañana?', 3, 2),
('Claro, podemos encontrarnos en la universidad.', 2, 3);

insert into reportes
(motivo, id_usuario, id_publicacion)
values
('La publicacion tiene informacion incorrecta', 2, 1),
('La imagen no corresponde al articulo publicado', 3, 2);





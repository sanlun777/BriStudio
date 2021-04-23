
drop database if exists bristudio;
create database bristudio;
use bristudio;

create table usuarios(
usuario_id decimal(10) not null primary key,
usuario_nick nvarchar(20),
sexo boolean,
edad int(3),
correo nvarchar(254),
nombre nvarchar(30),
apellido_p nvarchar(15),
apellido_m nvarchar(15),
contrasena nvarchar(30),
unique (usuario_nick)
);

create table curso(
curso_id int not null primary key auto_increment,
curso nvarchar(30),
unique (curso)
);

create table permisos(
permisos_id int not null primary key auto_increment,
permiso nvarchar(20)
);

create table permiso_usuario(
permisosusu int not null primary key auto_increment,
permisos_id int,
usuario_id decimal(10),
foreign key (usuario_id) references usuarios(usuario_id) on delete cascade,
foreign key (permisos_id) references permisos(permisos_id));

create table usuario_curso(
curso_id int,
usuario_id decimal(10),
permisos_id int,
foreign key (usuario_id) references usuarios(usuario_id),
foreign key (permisos_id) references permisos(permisos_id),
foreign key (curso_id) references curso(curso_id) on delete cascade);

create table seccion(
seccion_id int not null primary key auto_increment,
seccion nvarchar(70),
curso_id int,
foreign key(curso_id) references curso(curso_id) on delete cascade
);

create table registro_curso(
registro_id int not null primary key auto_increment,
usuario_id decimal(10),
curso_id int,
permisos_id int,
unique (registro_id),
foreign key(usuario_id) references usuarios(usuario_id),
foreign key(curso_id) references curso(curso_id),
foreign key(permisos_id) references permisos(permisos_id)
);

create table tipo_contenido(
	tipo_contenido_id int not null primary key auto_increment,
    tipo_contenido nvarchar(10)
);

create table contenido(
contenido_id int not null primary key auto_increment,
curso_id int,
maestro_id decimal(10),
titulo_contenido nvarchar(30),
texto_descripcion text(5000),
tiempo_creacion datetime,
tiempo_limite datetime,
foreign key (curso_id) references curso(curso_id) on delete cascade,
foreign key (maestro_id) references usuarios(usuario_id));

create table pregunta(
pregunta_id int not null primary key auto_increment,
contenido_id int,
tipo_pregunta_id boolean,
texto_pregunta nvarchar(200),
foreign key (contenido_id) references contenido(contenido_id)
);

create table inciso(
inciso_id int not null primary key auto_increment,
pregunta_id int,
inciso nvarchar(100),
correcta boolean,
foreign key (pregunta_id) references pregunta(pregunta_id)
);

create table estado_actividad(
estado_actividad_id int not null primary key auto_increment,
estado_actividad nvarchar(20)
);

create table actividad_realizada(
realizada_id int not null primary key auto_increment,
alumno_id decimal(10),
contenido_id int,
estado_actividad_id int,
archivo_url nvarchar(260),
foreign key (alumno_id) references usuarios(usuario_id),
foreign key (contenido_id) references contenido(contenido_id),
foreign key (estado_actividad_id) references estado_actividad(estado_actividad_id)
);

create table inciso_respondido(
inciso_id int,
realizada_id int,
foreign key (inciso_id) references inciso(inciso_id),
foreign key (realizada_id) references actividad_realizada(realizada_id)
);

create table pregunta_respondida(
pregunta_id int not null primary key,
realizada_id int,
respuesta nvarchar(100),
foreign key (pregunta_id) references pregunta(pregunta_id),
foreign key (realizada_id) references actividad_realizada(realizada_id)
);

create table chat(
chat_id int not null primary key auto_increment,
chat nvarchar(255),
id_usuario_remitente decimal(10),
id_usuario_destinatario decimal(10),
estado_visualizacion boolean,
foreign key(id_usuario_remitente) references usuarios(usuario_id),
foreign key(id_usuario_destinatario) references usuarios(usuario_id)
);

create table faqPregunta(
preg_id int not null primary key auto_increment,
pregunta nvarchar(255),
respuesta nvarchar(511),
id_usuario_pregunta decimal(10) not null,
id_usuario_respuesta decimal(10),
foreign key(id_usuario_pregunta) references usuarios(usuario_id),
foreign key(id_usuario_pregunta) references usuarios(usuario_id));

insert into permisos values (1, 'crea_usuario');
insert into permisos values (2, 'auditoria');
insert into permisos values (3, 'crea_cursos');
insert into permisos values (4, 'permite_crea_usuario');
insert into permisos values (5, 'crea_contenido');
insert into permisos values (6, 'administrador_curso');
insert into usuarios values(1000000000,"administrador",1,15,'administrador@bristudio.com','Administrador','de','Bristudio','briadmin');
insert into permiso_usuario(permisos_id,usuario_id) values(1,1000000000);
insert into permiso_usuario(permisos_id,usuario_id) values(2,1000000000);
insert into permiso_usuario(permisos_id,usuario_id) values(3,1000000000);
insert into permiso_usuario(permisos_id,usuario_id) values(4,1000000000);
select*from usuario_curso;
select*from usuarios;
select*from curso;
select*from curso;
select curso from curso inner join usuario_curso using (curso_id) where usuario_curso.usuario_id = '1000000000';
select * from usuario_curso where 1000000000 = 'usuario_id';
select*from permiso_usuario;
select*from contenido;
select*from pregunta;
select*from inciso;

select last_insert_id();


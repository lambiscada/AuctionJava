-- Indexes for primary keys have been explicitly created.
--"Subastador" project.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

ALTER TABLE Producto DROP FOREIGN KEY ProductoIdPujasFK;
DROP TABLE Puja;
DROP TABLE Producto;
DROP TABLE Usuario;
DROP TABLE Categoria;
DROP TABLE TarjetaBanco;



-- ------------------------------ TarjetaBanco ----------------------------------

CREATE TABLE TarjetaBanco (idTarjeta BIGINT NOT NULL AUTO_INCREMENT,
    fechaExpiracion TIMESTAMP NOT NULL,
    numero BIGINT NOT NULL,
    CONSTRAINT TarjetaBancoPK PRIMARY KEY(idTarjeta),
    CONSTRAINT validNumero CHECK ( numero > 0 )) ENGINE = InnoDB;

CREATE INDEX TarjetaBancoIndexByIdTarjeta ON TarjetaBanco (idTarjeta);

-- ------------------------------ Categoria ----------------------------------

CREATE TABLE Categoria (idCategoria BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    CONSTRAINT CategoriaPK PRIMARY KEY(idCategoria)) ENGINE = InnoDB;

CREATE INDEX CatergoriaIndexByIdCategoria ON Categoria (idCategoria);


-- ------------------------------ Usuario ----------------------------------
CREATE TABLE Usuario (
    idUsuario BIGINT NOT NULL AUTO_INCREMENT,
    login VARCHAR(30) COLLATE latin1_bin NOT NULL,
    password VARCHAR(13) NOT NULL, 
    nombre VARCHAR(30) NOT NULL,
    apellidos VARCHAR(40) NOT NULL,
    email VARCHAR(60) NOT NULL,
    idTarjeta BIGINT,
    CONSTRAINT UsuarioIdTarjetaFK FOREIGN KEY(idTarjeta)
        REFERENCES TarjetaBanco (idTarjeta),
    CONSTRAINT Usuario_PK PRIMARY KEY (idUsuario),
    CONSTRAINT LoginUniqueKey UNIQUE (login),
    INDEX UsuarioIndexForFK (idTarjeta)) 
    ENGINE = InnoDB;

CREATE INDEX UsuarioIndexByIdUsuario ON Usuario (idUsuario);
CREATE INDEX UsuarioIndexByIdTarjeta ON Usuario (idUsuario, idTarjeta);

-- ------------------------------ Producto ----------------------------------
CREATE TABLE Producto (
    idProducto BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(150) NOT NULL,
    infoEnvio VARCHAR(100) NOT NULL,
    precioSalida DOUBLE NOT NULL,
    precioActual DOUBLE NOT NULL,
    fechaVencimiento TIMESTAMP NOT NULL,
    fechaAnuncio TIMESTAMP NOT NULL,
    idCategoria BIGINT NOT NULL,
    idPuja BIGINT,
    idUsuario BIGINT NOT NULL,
    version BIGINT,
    CONSTRAINT ProductoIdCategoriaFK FOREIGN KEY(idCategoria)
        REFERENCES Categoria (idCategoria),
    CONSTRAINT ProductoIdUsuarioFK FOREIGN KEY(idUsuario)
        REFERENCES Usuario (idUsuario),
    CONSTRAINT Producto_PK PRIMARY KEY (idProducto),
    INDEX ProductoIndexForCategoriaFK (idCategoria),INDEX ProductoIndexForUsuarioFK (idUsuario)) 
    ENGINE = InnoDB;

CREATE INDEX ProductoIndexByIdProducto ON Producto (idProducto);
CREATE INDEX ProductoIndexByNombre ON Producto (nombre);
CREATE INDEX ProductoIndexByNombreAndIdCategoria ON Producto (nombre, idCategoria);

-- ------------------------------ Puja ----------------------------------
CREATE TABLE Puja (
    idPuja BIGINT NOT NULL AUTO_INCREMENT,
    cantidadMaxima DOUBLE NOT NULL,
    fecha TIMESTAMP NOT NULL,
    idProducto BIGINT NOT NULL,
    idUsuario BIGINT NOT NULL,
    CONSTRAINT PujaIdProductoFK FOREIGN KEY(idProducto)
        REFERENCES Producto (idProducto),
    CONSTRAINT PujaIdUsuarioFK FOREIGN KEY(idUsuario)
        REFERENCES Usuario (idUsuario),
    CONSTRAINT Puja_PK PRIMARY KEY (idPuja),
    INDEX PujaIndexForProductoFK (idProducto),INDEX PujaIndexForUsuarioFK (idUsuario)) 
    ENGINE = InnoDB;

CREATE INDEX PujaIndexByIdPuja ON Puja (idPuja);
CREATE INDEX PujaIndexByFecha ON Puja (fecha);

ALTER TABLE Producto
ADD CONSTRAINT ProductoIdPujasFK FOREIGN KEY(idPuja)
        REFERENCES Puja (idPuja);
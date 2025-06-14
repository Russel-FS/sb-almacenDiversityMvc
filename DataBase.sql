CREATE DATABASE JC_Diversity;

USE JC_Diversity;

CREATE TABLE Categorias (
    ID_Categoria INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Categoria VARCHAR(50) NOT NULL,
    Descripcion TEXT
);

CREATE TABLE Productos (
    ID_Producto VARCHAR(10) PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    Categoria INT NOT NULL,
    Stock INT DEFAULT 0,
    Precio_Unitario DECIMAL(10, 2) NOT NULL,
    Fecha_Registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (Categoria) REFERENCES Categorias (ID_Categoria)
);

CREATE TABLE Entradas (
    ID_Entrada INT AUTO_INCREMENT PRIMARY KEY,
    Fecha_Entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    Costo_Total DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Detalle_Entrada (
    ID_Detalle_Entrada INT AUTO_INCREMENT PRIMARY KEY,
    ID_Entrada INT NOT NULL,
    ID_Producto VARCHAR(10) NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (ID_Entrada) REFERENCES Entradas (ID_Entrada),
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto)
);

CREATE TABLE Salidas (
    ID_Salida INT AUTO_INCREMENT PRIMARY KEY,
    Fecha_Salida DATETIME DEFAULT CURRENT_TIMESTAMP,
    Motivo_Salida VARCHAR(100)
);

CREATE TABLE Detalle_Salida (
    ID_Detalle_Salida INT AUTO_INCREMENT PRIMARY KEY,
    ID_Salida INT NOT NULL,
    ID_Producto VARCHAR(10) NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (ID_Salida) REFERENCES Salidas (ID_Salida),
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto)
);

CREATE TABLE Inventario (
    ID_Inventario INT AUTO_INCREMENT PRIMARY KEY,
    ID_Producto VARCHAR(10) NOT NULL,
    Stock_Inicial INT DEFAULT 0,
    Total_Entradas INT DEFAULT 0,
    Total_Salidas INT DEFAULT 0,
    Stock_Actual INT DEFAULT 0,
    Ultima_Actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto)
);

CREATE TABLE Usuarios (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Usuario VARCHAR(50) NOT NULL,
    Rol VARCHAR(50) NOT NULL,
    Contraseña VARCHAR(255) NOT NULL
);

CREATE TABLE Pedidos (
    ID_Pedido INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cliente INT,
    Fecha_Pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    Total_Pedido DECIMAL(10, 2) NOT NULL,
    Estado_Pedido VARCHAR(50) NOT NULL,
    ID_Usuario INT,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios (ID_Usuario)
);

CREATE TABLE Detalle_Pedido (
    ID_Detalle INT AUTO_INCREMENT PRIMARY KEY,
    ID_Pedido INT NOT NULL,
    ID_Producto VARCHAR(10) NOT NULL,
    Cantidad INT NOT NULL,
    Subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (ID_Pedido) REFERENCES Pedidos (ID_Pedido),
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto)
);

CREATE TABLE Clientes (
    ID_Cliente INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(15),
    Direccion VARCHAR(255)
);

select * from Pedidos;

select * from usuarios;
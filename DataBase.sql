DROP DATABASE IF EXISTS diversity_inventory_mvc;

CREATE DATABASE IF NOT EXISTS diversity_inventory_mvc;

USE diversity_inventory_mvc;

-- Tabla de Roles
CREATE TABLE Roles (
    ID_Rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Rol VARCHAR(50) NOT NULL UNIQUE,
    Descripcion TEXT,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Rol_Nombre UNIQUE (Nombre_Rol)
);

-- Tabla de Rubros
CREATE TABLE Rubros (
    ID_Rubro BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(80) NOT NULL,
    Code VARCHAR(50) NOT NULL,
    Descripcion VARCHAR(255),
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    PublicId VARCHAR(100),
    ImagenUrl VARCHAR(255),
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Rubros_Code_Nombre UNIQUE (Code, Nombre)
);

-- Tabla de Usuarios
CREATE TABLE Usuarios (
    ID_Usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre_Usuario VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Nombre_Completo VARCHAR(100) NOT NULL,
    Contraseña VARCHAR(255) NOT NULL,
    UrlImagen VARCHAR(255),
    PublicId VARCHAR(100),
    Estado ENUM(
        'Activo',
        'Inactivo',
        'Bloqueado'
    ) DEFAULT 'Activo',
    Ultimo_Acceso DATETIME,
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Usuario_Nombre UNIQUE (Nombre_Usuario),
    CONSTRAINT UQ_Usuario_Email UNIQUE (Email)
);
-- Tabla de asignación de rubros a usuarios
CREATE TABLE Usuario_Rubros (
    ID_Usuario_Rubro BIGINT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario BIGINT NOT NULL,
    ID_Rubro BIGINT NOT NULL,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    Fecha_Asignacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (ID_Usuario, ID_Rubro),
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (ID_Rubro) REFERENCES Rubros (ID_Rubro)
);

-- tabla de asignación de roles a usuarios
CREATE TABLE User_Roles (
    ID_User_Role BIGINT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario BIGINT NOT NULL,
    ID_Rol BIGINT NOT NULL,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    Fecha_Asignacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Usuario) REFERENCES Usuarios (ID_Usuario) ON DELETE CASCADE,
    FOREIGN KEY (ID_Rol) REFERENCES Roles (ID_Rol) ON DELETE CASCADE,
    CONSTRAINT UQ_User_Role UNIQUE (ID_Usuario, ID_Rol),
    CONSTRAINT CK_User_Role_Estado CHECK (
        Estado IN ('Activo', 'Inactivo')
    )
);

-- Tabla de Categorías
CREATE TABLE Categorias (
    ID_Categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    ID_Rubro BIGINT NOT NULL,
    Nombre_Categoria VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    Estado ENUM(
        'Activo',
        'Inactivo',
        'Eliminado'
    ) DEFAULT 'Activo',
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (ID_Rubro) REFERENCES Rubros (ID_Rubro),
    FOREIGN KEY (created_by) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (updated_by) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Categoria_Nombre_Rubro UNIQUE (Nombre_Categoria, ID_Rubro)
);

-- Tabla de Productos
CREATE TABLE Productos (
    ID_Producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    Codigo_Producto VARCHAR(50) NOT NULL,
    Nombre_Producto VARCHAR(100) NOT NULL,
    Descripcion TEXT,
    ID_Categoria BIGINT NOT NULL,
    Precio_Compra DECIMAL(10, 2) NOT NULL,
    Precio_Venta DECIMAL(10, 2) NOT NULL,
    Stock_Actual INT NOT NULL DEFAULT 0,
    Stock_Minimo INT NOT NULL DEFAULT 0,
    Stock_Maximo INT NOT NULL DEFAULT 0,
    url_imagen VARCHAR(255),
    public_id VARCHAR(100),
    Estado ENUM(
        'Activo',
        'Inactivo',
        'Agotado',
        'Eliminado'
    ) DEFAULT 'Activo',
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (ID_Categoria) REFERENCES Categorias (ID_Categoria),
    FOREIGN KEY (created_by) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (updated_by) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Producto_Codigo UNIQUE (Codigo_Producto)
);

-- Tabla de Proveedores
CREATE TABLE Proveedores (
    ID_Proveedor BIGINT AUTO_INCREMENT PRIMARY KEY,
    Razon_Social VARCHAR(100) NOT NULL,
    RUC VARCHAR(11) NOT NULL,
    Direccion TEXT,
    Telefono VARCHAR(20),
    Email VARCHAR(100),
    Estado ENUM(
        'Activo',
        'Inactivo',
        'Eliminado'
    ) DEFAULT 'Activo',
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Proveedor_RUC UNIQUE (RUC),
    CONSTRAINT UQ_Proveedor_Email UNIQUE (Email)
);

-- Tabla de Clientes
CREATE TABLE Clientes (
    ID_Cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    Tipo_Cliente ENUM('Persona_Natural', 'Empresa') NOT NULL,
    Nombre_Completo VARCHAR(100) NOT NULL,
    Razon_Social VARCHAR(100),
    RUC VARCHAR(11),
    DNI VARCHAR(8),
    Direccion TEXT,
    Telefono VARCHAR(20),
    Email VARCHAR(100),
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    Fecha_Creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (updated_by) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Cliente_RUC UNIQUE (RUC),
    CONSTRAINT UQ_Cliente_DNI UNIQUE (DNI),
    CONSTRAINT UQ_Cliente_Email UNIQUE (Email),
    CONSTRAINT CK_Cliente_Tipo CHECK (
        (
            Tipo_Cliente = 'Persona_Natural'
            AND Razon_Social IS NULL
        )
        OR (
            Tipo_Cliente = 'Empresa'
            AND RUC IS NOT NULL
            AND Razon_Social IS NOT NULL
        )
    )
);

-- Tabla de Entradas
CREATE TABLE Entradas (
    ID_Entrada BIGINT AUTO_INCREMENT PRIMARY KEY,
    Numero_Factura VARCHAR(50) NOT NULL,
    Tipo_Documento ENUM(
        'FACTURA',
        'BOLETA',
        'NOTA_CREDITO',
        'NOTA_DEBITO',
        'GUIA_REMISION'
    ) NOT NULL,
    ID_Proveedor BIGINT NOT NULL,
    Fecha_Entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    Costo_Total DECIMAL(10, 2) NOT NULL CHECK (Costo_Total >= 0),
    Estado ENUM(
        'Pendiente',
        'Completado',
        'Anulado'
    ) DEFAULT 'Pendiente',
    ID_Usuario_Registro BIGINT NOT NULL,
    ID_Usuario_Aprobacion BIGINT,
    Fecha_Aprobacion DATETIME,
    Observaciones TEXT,
    FOREIGN KEY (ID_Proveedor) REFERENCES Proveedores (ID_Proveedor),
    FOREIGN KEY (ID_Usuario_Registro) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (ID_Usuario_Aprobacion) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Entrada_Factura UNIQUE (Numero_Factura, ID_Proveedor)
);

-- Tabla de Detalle_Entrada
CREATE TABLE Detalle_Entrada (
    ID_Detalle_Entrada BIGINT AUTO_INCREMENT PRIMARY KEY,
    ID_Entrada BIGINT NOT NULL,
    ID_Producto BIGINT NOT NULL,
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    Precio_Unitario DECIMAL(10, 2) NOT NULL CHECK (Precio_Unitario >= 0),
    Subtotal DECIMAL(10, 2) NOT NULL CHECK (Subtotal >= 0),
    ID_Usuario_Registro BIGINT NOT NULL,
    Fecha_Registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado ENUM('Activo', 'Anulado') DEFAULT 'Activo',
    FOREIGN KEY (ID_Entrada) REFERENCES Entradas (ID_Entrada),
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto),
    FOREIGN KEY (ID_Usuario_Registro) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Detalle_Entrada_Producto UNIQUE (ID_Entrada, ID_Producto)
);

-- Tabla de Salidas
CREATE TABLE Salidas (
    ID_Salida BIGINT AUTO_INCREMENT PRIMARY KEY,
    Numero_Documento VARCHAR(50) NOT NULL,
    Tipo_Documento ENUM(
        'FACTURA',
        'BOLETA',
        'NOTA_CREDITO',
        'NOTA_DEBITO',
        'GUIA_REMISION'
    ) NOT NULL,
    ID_Cliente BIGINT NOT NULL,
    Fecha_Salida DATETIME DEFAULT CURRENT_TIMESTAMP,
    Motivo_Salida VARCHAR(100),
    Total_Venta DECIMAL(10, 2) NOT NULL CHECK (Total_Venta >= 0),
    Estado ENUM(
        'Pendiente',
        'Completado',
        'Anulado',
        'Autorizado'
    ) DEFAULT 'Pendiente',
    Codigo_Autorizacion VARCHAR(100),
    ID_Usuario_Registro BIGINT NOT NULL,
    ID_Usuario_Aprobacion BIGINT,
    Fecha_Aprobacion DATETIME,
    Observaciones TEXT,
    FOREIGN KEY (ID_Cliente) REFERENCES Clientes (ID_Cliente),
    FOREIGN KEY (ID_Usuario_Registro) REFERENCES Usuarios (ID_Usuario),
    FOREIGN KEY (ID_Usuario_Aprobacion) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Salida_Documento UNIQUE (
        Numero_Documento,
        Tipo_Documento
    )
);

-- Tabla de Detalle_Salida
CREATE TABLE Detalle_Salida (
    ID_Detalle_Salida BIGINT AUTO_INCREMENT PRIMARY KEY,
    ID_Salida BIGINT NOT NULL,
    ID_Producto BIGINT NOT NULL,
    Cantidad INT NOT NULL CHECK (Cantidad > 0),
    Precio_Unitario DECIMAL(10, 2) NOT NULL CHECK (Precio_Unitario >= 0),
    Subtotal DECIMAL(10, 2) NOT NULL CHECK (Subtotal >= 0),
    ID_Usuario_Registro BIGINT NOT NULL,
    Fecha_Registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado ENUM('Activo', 'Anulado') DEFAULT 'Activo',
    FOREIGN KEY (ID_Salida) REFERENCES Salidas (ID_Salida),
    FOREIGN KEY (ID_Producto) REFERENCES Productos (ID_Producto),
    FOREIGN KEY (ID_Usuario_Registro) REFERENCES Usuarios (ID_Usuario),
    CONSTRAINT UQ_Detalle_Salida_Producto UNIQUE (ID_Salida, ID_Producto)
);
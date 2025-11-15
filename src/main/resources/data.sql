-- Script SQL para crear usuarios de prueba
-- Ejecutar este script en la base de datos MySQL

USE Banco_de_Agricultura;

-- Insertar roles si no existen
INSERT IGNORE INTO roles (id_rol, nombre_rol, descripcion, created_at, updated_at) VALUES
(1, 'Cajero', 'Empleado que realiza transacciones y solicita préstamos', NOW(), NOW()),
(2, 'Dependiente', 'Empleado que realiza transacciones con comisión del 5%', NOW(), NOW()),
(3, 'Gerente', 'Gerente de sucursal que aprueba préstamos y gestiona personal', NOW(), NOW()),
(4, 'Gerente General', 'Gerente general que gestiona todo el sistema', NOW(), NOW());

-- Insertar empleados de prueba (Cajeros, Gerentes, Gerente General)
-- Nota: Ajusta los id_rol según los IDs reales en tu base de datos

-- Cajero 1
INSERT IGNORE INTO empleados (nombre, usuario, password, puesto, salario, estado, id_rol, created_at, updated_at) VALUES
('Juan Pérez', 'cajero1', '123456', 'Cajero', 500.00, 'activo', 1, NOW(), NOW());

-- Cajero 2
INSERT IGNORE INTO empleados (nombre, usuario, password, puesto, salario, estado, id_rol, created_at, updated_at) VALUES
('María González', 'cajero2', '123456', 'Cajero', 500.00, 'activo', 1, NOW(), NOW());

-- Gerente de Sucursal
INSERT IGNORE INTO empleados (nombre, usuario, password, puesto, salario, estado, id_rol, created_at, updated_at) VALUES
('Carlos Rodríguez', 'gerente1', '123456', 'Gerente de Sucursal', 1000.00, 'activo', 3, NOW(), NOW());

-- Gerente General
INSERT IGNORE INTO empleados (nombre, usuario, password, puesto, salario, estado, id_rol, created_at, updated_at) VALUES
('Ana Martínez', 'gerente_general', '123456', 'Gerente General', 2000.00, 'activo', 4, NOW(), NOW());

-- Insertar dependientes de prueba
INSERT IGNORE INTO dependientes (nombre, usuario, password, comision, estado, id_rol, created_at, updated_at) VALUES
('Luis Hernández', 'dependiente1', '123456', 5.00, 'activo', 2, NOW(), NOW()),
('Sofía López', 'dependiente2', '123456', 5.00, 'activo', 2, NOW(), NOW());

-- Crear una sucursal de prueba y asignar gerente
INSERT IGNORE INTO sucursales (nombre, direccion, id_gerente, created_at, updated_at) VALUES
('Sucursal Central', 'San Salvador, El Salvador', 
 (SELECT id_empleado FROM empleados WHERE usuario = 'gerente1' LIMIT 1), 
 NOW(), NOW());

-- Insertar clientes de prueba con acceso al sistema
INSERT IGNORE INTO clientes (nombre, dui, usuario, password, direccion, correo, salario, estado_civil, fecha_nacimiento, activo, created_at, updated_at) VALUES
('Pedro Gómez', '01234567-8', 'cliente1', '123456', 'San Salvador', 'pedro.gomez@example.com', 850.00, 'Soltero', '1990-05-10', TRUE, NOW(), NOW()),
('Laura Martínez', '98765432-1', 'cliente2', '123456', 'Santa Ana', 'laura.martinez@example.com', 950.00, 'Casado', '1988-03-22', TRUE, NOW(), NOW());

-- Cuentas bancarias asociadas a los clientes de prueba
INSERT IGNORE INTO cuentasbancarias (numero_cuenta, tipo_cuenta, saldo, fecha_apertura, activo, created_at, updated_at, id_cliente) VALUES
('BAC-100001', 'Cuenta Corriente', 1500.00, NOW(), TRUE, NOW(), NOW(), (SELECT id_cliente FROM clientes WHERE usuario = 'cliente1' LIMIT 1)),
('BAC-100002', 'Cuenta de Ahorro', 2750.50, NOW(), TRUE, NOW(), NOW(), (SELECT id_cliente FROM clientes WHERE usuario = 'cliente2' LIMIT 1));


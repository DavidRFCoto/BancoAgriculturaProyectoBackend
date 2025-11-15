# Usuarios de Prueba para Login

## Credenciales de Acceso

### Cajeros
- **Usuario:** `cajero1`
- **Contraseña:** `123456`
- **Rol:** Cajero

- **Usuario:** `cajero2`
- **Contraseña:** `123456`
- **Rol:** Cajero

### Dependientes
- **Usuario:** `dependiente1`
- **Contraseña:** `123456`
- **Rol:** Dependiente

- **Usuario:** `dependiente2`
- **Contraseña:** `123456`
- **Rol:** Dependiente

### Gerente de Sucursal
- **Usuario:** `gerente1`
- **Contraseña:** `123456`
- **Rol:** Gerente

### Gerente General
- **Usuario:** `gerente_general`
- **Contraseña:** `123456`
- **Rol:** Gerente General

### Clientes
- **Usuario:** `cliente1`
- **Contraseña:** `123456`
- **Rol:** Cliente

- **Usuario:** `cliente2`
- **Contraseña:** `123456`
- **Rol:** Cliente

## Instrucciones

1. Ejecuta el script `data.sql` en tu base de datos MySQL
2. Asegúrate de que los roles existan en la tabla `roles`
3. Si los IDs de roles son diferentes, ajusta el script SQL antes de ejecutarlo

## Nota de Seguridad

⚠️ **IMPORTANTE:** Estas contraseñas son solo para desarrollo/pruebas. En producción:
- Usa hash de contraseñas (BCrypt)
- Implementa políticas de contraseñas seguras
- No uses contraseñas en texto plano


# Sistema de Gestión Golden Center Gym - Aplicación web

Evidencia GA7-220501096-AA2-EV02 - Módulos de software codificados y probados.

## Módulos desarrollados
- Gestión de Clientes: registrar, consultar (por documento o nombre), actualizar, activar y desactivar clientes.
- Gestión de Membresías: asignar, consultar, actualizar, activar y desactivar membresías de clientes existentes.

Al desactivar un cliente, sus membresías activas pasan a inactivas.

## Tecnologías
Java 21, Maven, Servlets (Jakarta), JSP/JSTL, JDBC, MySQL 8.0 y Apache Tomcat 10.1.

## Estructura del proyecto
- src/main/java/com/goldencenter/config: conexión a MySQL (ConexionBD).
- src/main/java/com/goldencenter/model: clases Cliente y Membresia.
- src/main/java/com/goldencenter/dao y dao/impl: acceso a la base de datos (JDBC).
- src/main/java/com/goldencenter/service: reglas de negocio y validaciones.
- src/main/java/com/goldencenter/controller: servlets ClienteServlet y MembresiaServlet.
- src/main/webapp: página de inicio (index.jsp), estilos (css), imágenes (img) y pantallas JSP (WEB-INF/views).
- database: script SQL de la base de datos.

## Cómo ejecutarlo (PowerShell)
1. Ejecutar el script database/golden_center_gym.sql en MySQL.
2. Empaquetar el proyecto desde su carpeta:
   mvn clean package
3. Copiar target/golden-center-gym-web.war a la carpeta webapps de Tomcat.
4. Definir los datos de conexión y arrancar Tomcat desde su carpeta bin (reemplazar la contraseña):
   $env:GOLDEN_DB_URL='jdbc:mysql://localhost:3306/golden_center_gym'
   $env:GOLDEN_DB_USER='root'
   $env:GOLDEN_DB_PASSWORD='su_contraseña'
   .\startup.bat
5. Abrir en el navegador: http://localhost:8080/golden-center-gym-web/
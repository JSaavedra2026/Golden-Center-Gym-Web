<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>
  <c:choose>
    <c:when test="${modoEdicion}">Actualizar cliente</c:when>
    <c:otherwise>Registrar cliente</c:otherwise>
  </c:choose>
  · Sistema de Gestión Golden Center Gym
</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<a class="skip-link" href="#contenido-principal">Saltar al contenido</a>

<header class="app-header">
  <div class="brand">
    <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logotipo Golden Center Gym" class="brand__logo brand__logo--sm">
    <p class="brand__name">Sistema de Gestión Golden Center Gym</p>
  </div>
  <div class="app-header__user">
    <a href="${pageContext.request.contextPath}/" class="btn btn-outline btn-sm">Inicio</a>
  </div>
</header>

<nav class="app-nav" aria-label="Navegación principal">
  <a href="${pageContext.request.contextPath}/clientes" class="app-nav__link is-active" aria-current="page">Clientes</a>
  <a href="${pageContext.request.contextPath}/membresias" class="app-nav__link">Membresías</a>
</nav>

<main class="app-main" id="contenido-principal" tabindex="-1">
  <div class="modal__contenido modal__contenido--form" style="margin: 0 auto;">
    <h1 style="text-align:center; margin-top:0;">
      <c:choose>
        <c:when test="${modoEdicion}">Actualizar cliente</c:when>
        <c:otherwise>Registrar cliente</c:otherwise>
      </c:choose>
    </h1>

    <c:if test="${not empty error}">
      <p class="alert" role="alert"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/clientes" method="post">
      <c:choose>
        <c:when test="${modoEdicion}">
          <input type="hidden" name="accion" value="actualizar">
          <input type="hidden" name="idCliente" value="<c:out value='${idCliente}'/>">
        </c:when>
        <c:otherwise>
          <input type="hidden" name="accion" value="guardar">
        </c:otherwise>
      </c:choose>

      <div class="field">
        <label for="cli-documento">Documento</label>
        <div class="input-wrap">
          <input type="text" id="cli-documento" name="documento" maxlength="20"
                 inputmode="numeric" placeholder="Número de documento" required
                 value="<c:out value='${documento}'/>">
        </div>
      </div>

      <div class="field">
        <label for="cli-nombre">Nombre completo</label>
        <div class="input-wrap">
          <input type="text" id="cli-nombre" name="nombreCompleto" maxlength="100"
                 placeholder="Nombre y apellido" required
                 value="<c:out value='${nombreCompleto}'/>">
        </div>
      </div>

      <div class="field">
        <label for="cli-telefono">Teléfono</label>
        <div class="input-wrap">
          <input type="tel" id="cli-telefono" name="telefono" maxlength="15"
                 inputmode="numeric" placeholder="Ej: 3216549802" required
                 value="<c:out value='${telefono}'/>">
        </div>
      </div>

      <div class="field">
        <label for="cli-correo">Correo electrónico</label>
        <div class="input-wrap">
          <input type="email" id="cli-correo" name="correo" maxlength="100"
                 placeholder="usuario@correo.com" required
                 value="<c:out value='${correo}'/>">
        </div>
      </div>

      <div class="modal__botones">
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline">Cancelar</a>
      </div>
    </form>
  </div>
</main>
</body>
</html>
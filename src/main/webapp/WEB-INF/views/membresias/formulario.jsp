<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>
  <c:choose>
    <c:when test="${modoEdicion}">Actualizar membresía</c:when>
    <c:otherwise>Asignar membresía</c:otherwise>
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
  <a href="${pageContext.request.contextPath}/clientes" class="app-nav__link">Clientes</a>
  <a href="${pageContext.request.contextPath}/membresias" class="app-nav__link is-active" aria-current="page">Membresías</a>
</nav>

<main class="app-main" id="contenido-principal" tabindex="-1">
  <div class="modal__contenido modal__contenido--form" style="margin: 0 auto;">
    <h1 style="text-align:center; margin-top:0;">
      <c:choose>
        <c:when test="${modoEdicion}">Actualizar membresía</c:when>
        <c:otherwise>Asignar membresía a cliente</c:otherwise>
      </c:choose>
    </h1>

    <c:if test="${not empty error}">
      <p class="alert" role="alert"><c:out value="${error}"/></p>
    </c:if>

    <form action="${pageContext.request.contextPath}/membresias" method="post">
      <c:choose>
        <c:when test="${modoEdicion}">
          <input type="hidden" name="accion" value="actualizar">
          <input type="hidden" name="idMembresia" value="<c:out value='${idMembresia}'/>">
        </c:when>
        <c:otherwise>
          <input type="hidden" name="accion" value="guardar">
        </c:otherwise>
      </c:choose>

      <div class="field">
        <label for="mem-cliente">Cliente</label>
        <c:choose>
          <c:when test="${modoEdicion}">
            <div class="input-wrap">
              <input type="text" id="mem-cliente" value="<c:out value='${clienteNombre}'/>" readonly>
            </div>
          </c:when>
          <c:otherwise>
            <select id="mem-cliente" name="idCliente" class="select-input" required>
              <option value="">Seleccione un cliente</option>
              <c:forEach var="cliente" items="${clientes}">
                <option value="${cliente.idCliente}"
                  <c:if test="${idCliente eq cliente.idCliente}">selected</c:if>>
                  <c:out value="${cliente.nombreCompleto}"/> - <c:out value="${cliente.documento}"/>
                </option>
              </c:forEach>
            </select>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="field">
        <label for="mem-plan">Plan de membresía</label>
        <select id="mem-plan" name="tipoMembresia" class="select-input" required>
          <option value="">Seleccione un plan</option>
          <option value="Plan semanal" <c:if test="${tipoMembresia eq 'Plan semanal'}">selected</c:if>>Plan semanal - 7 días - $40.000</option>
          <option value="Plan quincenal" <c:if test="${tipoMembresia eq 'Plan quincenal'}">selected</c:if>>Plan quincenal - 15 días - $55.000</option>
          <option value="Plan mensual" <c:if test="${tipoMembresia eq 'Plan mensual'}">selected</c:if>>Plan mensual - 30 días - $65.000</option>
          <option value="Rutina por sesión" <c:if test="${tipoMembresia eq 'Rutina por sesión'}">selected</c:if>>Rutina por sesión - 1 día - $15.000</option>
        </select>
      </div>

      <div class="field">
        <label for="mem-inicio">Fecha de inicio</label>
        <div class="input-wrap">
          <input type="date" id="mem-inicio" name="fechaInicio" required
                 value="<c:out value='${fechaInicio}'/>">
        </div>
      </div>

      <div class="modal__botones">
        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="${pageContext.request.contextPath}/membresias" class="btn btn-outline">Cancelar</a>
      </div>
    </form>
  </div>
</main>
</body>
</html>
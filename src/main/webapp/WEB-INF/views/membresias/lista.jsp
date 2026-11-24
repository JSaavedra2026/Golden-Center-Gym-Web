<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="es_CO"/>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestión de membresías · Sistema de Gestión Golden Center Gym</title>
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
  <section aria-label="Gestión de membresías">
    <h1 class="page-title">Gestión de membresías</h1>

    <c:if test="${param.registro eq 'exitoso'}">
      <p class="alert" role="alert">La membresía se registró correctamente.</p>
    </c:if>
    <c:if test="${param.actualizacion eq 'exitosa'}">
      <p class="alert" role="alert">La membresía se actualizó correctamente.</p>
    </c:if>
    <c:if test="${param.desactivacion eq 'exitosa'}">
      <p class="alert" role="alert">La membresía se desactivó correctamente.</p>
    </c:if>
    <c:if test="${param.desactivacion eq 'error'}">
      <p class="alert" role="alert">No fue posible desactivar la membresía.</p>
    </c:if>
    <c:if test="${param.activacion eq 'exitosa'}">
      <p class="alert" role="alert">La membresía se activó correctamente.</p>
    </c:if>
    <c:if test="${param.activacion eq 'error'}">
      <p class="alert" role="alert">No fue posible activar la membresía. Verifique que el cliente esté activo.</p>
    </c:if>

    <div class="toolbar">
      <a href="${pageContext.request.contextPath}/membresias?accion=nuevo" class="btn btn-primary">Asignar membresía</a>
      <c:if test="${not empty filtroCliente}">
        <a href="${pageContext.request.contextPath}/membresias" class="btn btn-outline">Ver todas las membresías</a>
      </c:if>
    </div>

    <c:if test="${not empty filtroCliente}">
      <p>Mostrando las membresías del cliente con ID <c:out value="${filtroCliente}"/>.</p>
    </c:if>

    <div class="table-wrap">
      <table class="data-table" id="tabla-membresias">
        <thead>
          <tr>
            <th scope="col">Cliente</th>
            <th scope="col">Plan</th>
            <th scope="col">Duración</th>
            <th scope="col">Precio</th>
            <th scope="col">Inicio</th>
            <th scope="col">Vencimiento</th>
            <th scope="col">Estado</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="membresia" items="${membresias}">
            <tr>
              <td>
                <c:out value="${membresia.cliente.nombreCompleto}"/>
                (<c:out value="${membresia.cliente.documento}"/>)
              </td>
              <td><c:out value="${membresia.tipoMembresia}"/></td>
              <td><c:out value="${membresia.duracion}"/> día(s)</td>
              <td>$<fmt:formatNumber value="${membresia.costo}" pattern="#,##0"/></td>
              <td><c:out value="${membresia.fechaInicio}"/></td>
              <td><c:out value="${membresia.fechaVencimiento}"/></td>
              <td>
                <c:choose>
                  <c:when test="${membresia.estado eq 'Activa'}">
                    <span class="badge badge--active">Activa</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge badge--inactive"><c:out value="${membresia.estado}"/></span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:url var="urlEditar" value="/membresias">
                  <c:param name="accion" value="editar"/>
                  <c:param name="id" value="${membresia.idMembresia}"/>
                </c:url>
                <a href="${urlEditar}" class="action-link">Editar</a>
                <c:choose>
                  <c:when test="${membresia.estado ne 'Inactiva'}">
                    /
                    <form action="${pageContext.request.contextPath}/membresias" method="post" style="display:inline;margin:0;"
                          onsubmit="return confirm('¿Desea desactivar esta membresía?');">
                      <input type="hidden" name="accion" value="desactivar">
                      <input type="hidden" name="idMembresia" value="<c:out value='${membresia.idMembresia}'/>">
                      <button type="submit" class="action-link" style="background:none;border:none;padding:0;cursor:pointer;font:inherit;">Desactivar</button>
                    </form>
                  </c:when>
                  <c:otherwise>
                    /
                    <form action="${pageContext.request.contextPath}/membresias" method="post" style="display:inline;margin:0;"
                          onsubmit="return confirm('¿Desea activar esta membresía?');">
                      <input type="hidden" name="accion" value="activar">
                      <input type="hidden" name="idMembresia" value="<c:out value='${membresia.idMembresia}'/>">
                      <button type="submit" class="action-link" style="background:none;border:none;padding:0;cursor:pointer;font:inherit;">Activar</button>
                    </form>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty membresias}">
            <tr>
              <td colspan="8">No se encontraron membresías.</td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>

    <div class="bottom-bar bottom-bar--derecha">
      <a href="${pageContext.request.contextPath}/" class="btn btn-outline">Volver al inicio</a>
    </div>
  </section>
</main>
</body>
</html>
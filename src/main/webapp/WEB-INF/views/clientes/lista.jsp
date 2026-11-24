<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Gestión de clientes · Sistema de Gestión Golden Center Gym</title>
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
  <section aria-label="Gestión de clientes">
    <h1 class="page-title">Gestión de clientes</h1>

    <c:if test="${param.registro eq 'exitoso'}">
      <p class="alert" role="alert">El cliente se registró correctamente.</p>
    </c:if>
    <c:if test="${param.actualizacion eq 'exitosa'}">
      <p class="alert" role="alert">El cliente se actualizó correctamente.</p>
    </c:if>
    <c:if test="${param.desactivacion eq 'exitosa'}">
      <p class="alert" role="alert">El cliente se desactivó correctamente.</p>
    </c:if>
    <c:if test="${param.desactivacion eq 'error'}">
      <p class="alert" role="alert">No fue posible desactivar el cliente.</p>
    </c:if>
    <c:if test="${param.activacion eq 'exitosa'}">
      <p class="alert" role="alert">El cliente se activó correctamente.</p>
    </c:if>
    <c:if test="${param.activacion eq 'error'}">
      <p class="alert" role="alert">No fue posible activar el cliente.</p>
    </c:if>

    <div class="toolbar">
      <a href="${pageContext.request.contextPath}/clientes?accion=nuevo" class="btn btn-primary">Registrar cliente</a>
      <form class="search-box" role="search" action="${pageContext.request.contextPath}/clientes" method="get">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><circle cx="10.5" cy="10.5" r="6.5" stroke="currentColor" stroke-width="1.8"/><path d="m20 20-4.3-4.3" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/></svg>
        <label for="buscar-cliente" class="visually-hidden">Buscar cliente por documento o nombre</label>
        <input type="search" id="buscar-cliente" name="busqueda" value="<c:out value='${busqueda}'/>" placeholder="Buscar cliente por documento o nombre">
      </form>
      <c:if test="${not empty busqueda}">
        <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline">Ver todos</a>
      </c:if>
    </div>

    <div class="table-wrap">
      <table class="data-table" id="tabla-clientes">
        <thead>
          <tr>
            <th scope="col">Documento</th>
            <th scope="col">Nombre</th>
            <th scope="col">Teléfono</th>
            <th scope="col">Correo</th>
            <th scope="col">Estado</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="cliente" items="${clientes}">
            <tr>
              <td><c:out value="${cliente.documento}"/></td>
              <td><c:out value="${cliente.nombreCompleto}"/></td>
              <td><c:out value="${cliente.telefono}"/></td>
              <td><c:out value="${cliente.correo}"/></td>
              <td>
                <c:choose>
                  <c:when test="${cliente.estado eq 'Activo'}">
                    <span class="badge badge--active">Activo</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge badge--inactive"><c:out value="${cliente.estado}"/></span>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <c:url var="urlEditar" value="/clientes">
                  <c:param name="accion" value="editar"/>
                  <c:param name="id" value="${cliente.idCliente}"/>
                </c:url>
                <a href="${urlEditar}" class="action-link">Editar</a>
                <c:choose>
                  <c:when test="${cliente.estado eq 'Activo'}">
                    /
                    <form action="${pageContext.request.contextPath}/clientes" method="post" style="display:inline;margin:0;"
                          onsubmit="return confirm('¿Desea desactivar este cliente?');">
                      <input type="hidden" name="accion" value="desactivar">
                      <input type="hidden" name="idCliente" value="<c:out value='${cliente.idCliente}'/>">
                      <button type="submit" class="action-link" style="background:none;border:none;padding:0;cursor:pointer;font:inherit;">Desactivar</button>
                    </form>
                  </c:when>
                  <c:otherwise>
                    /
                    <form action="${pageContext.request.contextPath}/clientes" method="post" style="display:inline;margin:0;"
                          onsubmit="return confirm('¿Desea activar este cliente?');">
                      <input type="hidden" name="accion" value="activar">
                      <input type="hidden" name="idCliente" value="<c:out value='${cliente.idCliente}'/>">
                      <button type="submit" class="action-link" style="background:none;border:none;padding:0;cursor:pointer;font:inherit;">Activar</button>
                    </form>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty clientes}">
            <tr>
              <td colspan="6">No se encontraron clientes.</td>
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
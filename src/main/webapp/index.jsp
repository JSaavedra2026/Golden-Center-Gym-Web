<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Panel principal · Sistema de Gestión Golden Center Gym</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<a class="skip-link" href="#contenido-principal">Saltar al contenido</a>

<header class="app-header">
  <div class="brand">
    <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logotipo Golden Center Gym" class="brand__logo brand__logo--sm">
    <p class="brand__name">Sistema de Gestión Golden Center Gym</p>
  </div>
</header>

<nav class="app-nav" aria-label="Navegación principal">
  <a href="${pageContext.request.contextPath}/clientes" class="app-nav__link">Clientes</a>
  <a href="${pageContext.request.contextPath}/membresias" class="app-nav__link">Membresías</a>
</nav>

<main class="app-main" id="contenido-principal" tabindex="-1">
  <section class="panel-content" aria-label="Panel principal">
    <h1 class="page-title">Panel principal</h1>
    <div class="card-grid">
      <article>
        <a class="nav-card" href="${pageContext.request.contextPath}/clientes">
          <svg class="nav-card__icon" viewBox="0 0 24 24" fill="none" aria-hidden="true"><circle cx="9" cy="8" r="3" stroke="currentColor" stroke-width="1.6"/><path d="M3.5 19c.9-3.2 3-4.8 5.5-4.8s4.6 1.6 5.5 4.8" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/><circle cx="17" cy="9" r="2.3" stroke="currentColor" stroke-width="1.6"/><path d="M15.5 14.5c2 0 3.6 1.3 4.5 4" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/></svg>
          <span class="nav-card__label">Clientes</span>
        </a>
      </article>
      <article>
        <a class="nav-card" href="${pageContext.request.contextPath}/membresias">
          <svg class="nav-card__icon" viewBox="0 0 24 24" fill="none" aria-hidden="true"><rect x="3" y="5.5" width="18" height="13" rx="1.8" stroke="currentColor" stroke-width="1.6"/><circle cx="8.5" cy="11" r="1.8" stroke="currentColor" stroke-width="1.4"/><path d="M6 15.3c.5-1.4 1.5-2 2.5-2s2 .6 2.5 2M13.5 9.5h5M13.5 13h5" stroke="currentColor" stroke-width="1.4" stroke-linecap="round"/></svg>
          <span class="nav-card__label">Membresías</span>
        </a>
      </article>
    </div>
  </section>
</main>
</body>
</html>
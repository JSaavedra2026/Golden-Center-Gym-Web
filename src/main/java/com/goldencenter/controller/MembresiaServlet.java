package com.goldencenter.controller;

import com.goldencenter.model.Cliente;
import com.goldencenter.model.Membresia;
import com.goldencenter.service.ClienteService;
import com.goldencenter.service.MembresiaService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/membresias")
public class MembresiaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private MembresiaService membresiaService;
    private ClienteService clienteService;

    @Override
    public void init() throws ServletException {
        membresiaService = new MembresiaService();
        clienteService = new ClienteService();
    }

    // ---------------- GET: consultar y mostrar ----------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("nuevo".equals(accion)) {
            mostrarFormulario(request, response);
        } else if ("editar".equals(accion)) {
            editarMembresia(request, response);
        } else {
            listarMembresias(request, response);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Solo los clientes activos pueden recibir una membresía
            List<Cliente> activos = new ArrayList<>();
            for (Cliente c : clienteService.listar()) {
                if ("Activo".equals(c.getEstado())) {
                    activos.add(c);
                }
            }
            request.setAttribute("clientes", activos);
            request.getRequestDispatcher("/WEB-INF/views/membresias/formulario.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("Error al cargar clientes", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible cargar los clientes.");
        }
    }

    private void listarMembresias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filtroCliente = request.getParameter("idCliente");
        try {
            List<Membresia> membresias;
            if (filtroCliente != null && !filtroCliente.isBlank()) {
                int idCliente = Integer.parseInt(filtroCliente.trim());
                membresias = membresiaService.buscarPorCliente(idCliente);
                request.setAttribute("filtroCliente", idCliente);
            } else {
                membresias = membresiaService.listar();
            }
            request.setAttribute("membresias", membresias);
            request.getRequestDispatcher("/WEB-INF/views/membresias/lista.jsp")
                    .forward(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID del cliente no es válido.");
        } catch (SQLException e) {
            getServletContext().log("Error al consultar membresías", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible consultar las membresías.");
        }
    }

    private void editarMembresia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Membresia membresia = membresiaService.buscarPorId(id);
            if (membresia == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La membresía no existe.");
                return;
            }
            request.setAttribute("modoEdicion", true);
            request.setAttribute("idMembresia", membresia.getIdMembresia());
            request.setAttribute("idCliente", membresia.getCliente().getIdCliente());
            request.setAttribute("clienteNombre", membresia.getCliente().getNombreCompleto());
            request.setAttribute("tipoMembresia", membresia.getTipoMembresia());
            request.setAttribute("fechaInicio", membresia.getFechaInicio().toString());
            mostrarFormulario(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID de la membresía no es válido.");
        } catch (SQLException e) {
            getServletContext().log("Error al consultar membresía", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible consultar la membresía.");
        }
    }

    // ---------------- POST: guardar, actualizar, desactivar ----------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        getServletContext().log("MembresiaServlet - Acción recibida: " + accion);

        if ("guardar".equals(accion)) {
            guardarMembresia(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarMembresia(request, response);
        } else if ("desactivar".equals(accion)) {
            desactivarMembresia(request, response);
        } else if ("activar".equals(accion)) {
            activarMembresia(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Operación no válida: " + (accion == null ? "acción ausente" : accion));
        }
    }

    private void guardarMembresia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idClienteTexto = request.getParameter("idCliente");
        String tipoMembresia = request.getParameter("tipoMembresia");
        String fechaInicioTexto = request.getParameter("fechaInicio");

        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(Integer.parseInt(idClienteTexto));

            Membresia membresia = new Membresia();
            membresia.setCliente(cliente);
            membresia.setTipoMembresia(tipoMembresia);
            membresia.setFechaInicio(LocalDate.parse(fechaInicioTexto));

            membresiaService.registrar(membresia);
            response.sendRedirect(request.getContextPath() + "/membresias?registro=exitoso");
        } catch (NumberFormatException e) {
            devolverFormulario(request, response, false, 0,
                    idClienteTexto, tipoMembresia, fechaInicioTexto,
                    "Debe seleccionar un cliente.");
        } catch (DateTimeParseException | NullPointerException e) {
            devolverFormulario(request, response, false, 0,
                    idClienteTexto, tipoMembresia, fechaInicioTexto,
                    "La fecha de inicio no es válida.");
        } catch (IllegalArgumentException e) {
            devolverFormulario(request, response, false, 0,
                    idClienteTexto, tipoMembresia, fechaInicioTexto, e.getMessage());
        } catch (SQLException e) {
            getServletContext().log("Error al registrar membresía", e);
            devolverFormulario(request, response, false, 0,
                    idClienteTexto, tipoMembresia, fechaInicioTexto,
                    "No fue posible registrar la membresía. Verifique los datos ingresados.");
        }
    }

    private void actualizarMembresia(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("idMembresia");
        String tipoMembresia = request.getParameter("tipoMembresia");
        String fechaInicioTexto = request.getParameter("fechaInicio");

        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID de la membresía no es válido.");
            return;
        }

        try {
            Membresia existente = membresiaService.buscarPorId(id);
            if (existente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "La membresía no existe.");
                return;
            }
            existente.setTipoMembresia(tipoMembresia);
            existente.setFechaInicio(LocalDate.parse(fechaInicioTexto));

            membresiaService.actualizar(existente);
            response.sendRedirect(request.getContextPath() + "/membresias?actualizacion=exitosa");
        } catch (DateTimeParseException | NullPointerException e) {
            devolverFormulario(request, response, true, id, null,
                    tipoMembresia, fechaInicioTexto, "La fecha de inicio no es válida.");
        } catch (IllegalArgumentException e) {
            devolverFormulario(request, response, true, id, null,
                    tipoMembresia, fechaInicioTexto, e.getMessage());
        } catch (SQLException e) {
            getServletContext().log("Error al actualizar membresía", e);
            devolverFormulario(request, response, true, id, null,
                    tipoMembresia, fechaInicioTexto,
                    "No fue posible actualizar la membresía. Verifique los datos ingresados.");
        }
    }

    private void desactivarMembresia(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idTexto = request.getParameter("idMembresia");
        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID de la membresía no es válido.");
            return;
        }

        try {
            membresiaService.desactivar(id);
            response.sendRedirect(request.getContextPath() + "/membresias?desactivacion=exitosa");
        } catch (IllegalArgumentException e) {
            getServletContext().log("No fue posible desactivar la membresía", e);
            response.sendRedirect(request.getContextPath() + "/membresias?desactivacion=error");
        } catch (SQLException e) {
            getServletContext().log("Error SQL al desactivar la membresía", e);
            response.sendRedirect(request.getContextPath() + "/membresias?desactivacion=error");
        }
    }
    
    private void activarMembresia(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idTexto = request.getParameter("idMembresia");
        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID de la membresía no es válido.");
            return;
        }

        try {
            membresiaService.activar(id);
            response.sendRedirect(request.getContextPath() + "/membresias?activacion=exitosa");
        } catch (IllegalArgumentException e) {
            getServletContext().log("No fue posible activar la membresía", e);
            response.sendRedirect(request.getContextPath() + "/membresias?activacion=error");
        } catch (SQLException e) {
            getServletContext().log("Error SQL al activar la membresía", e);
            response.sendRedirect(request.getContextPath() + "/membresias?activacion=error");
        }
    }

    // Devuelve los datos al formulario cuando hay un error
    private void devolverFormulario(HttpServletRequest request, HttpServletResponse response,
                                    boolean edicion, int idMembresia, String idCliente,
                                    String tipoMembresia, String fechaInicio, String error)
            throws ServletException, IOException {
        if (edicion) {
            request.setAttribute("modoEdicion", true);
            request.setAttribute("idMembresia", idMembresia);
        }
        request.setAttribute("idCliente", idCliente);
        request.setAttribute("tipoMembresia", tipoMembresia);
        request.setAttribute("fechaInicio", fechaInicio);
        request.setAttribute("error", error);
        mostrarFormulario(request, response);
    }
}

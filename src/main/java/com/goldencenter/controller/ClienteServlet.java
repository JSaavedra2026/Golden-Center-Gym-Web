package com.goldencenter.controller;

import com.goldencenter.model.Cliente;
import com.goldencenter.service.ClienteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ClienteService clienteService;

    @Override
    public void init() throws ServletException {
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
            editarCliente(request, response);
        } else {
            listarClientes(request, response);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/clientes/formulario.jsp")
                .forward(request, response);
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String busqueda = request.getParameter("busqueda");
        try {
            List<Cliente> clientes;
            if (busqueda != null && !busqueda.isBlank()) {
                // Busca por documento y, si no hay resultado, por nombre
                clientes = new ArrayList<>();
                Cliente porDocumento = clienteService.buscarPorDocumento(busqueda);
                if (porDocumento != null) {
                    clientes.add(porDocumento);
                } else {
                    clientes = clienteService.buscarPorNombre(busqueda);
                }
                request.setAttribute("busqueda", busqueda.trim());
            } else {
                clientes = clienteService.listar();
            }
            request.setAttribute("clientes", clientes);
            request.getRequestDispatcher("/WEB-INF/views/clientes/lista.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("Error al consultar clientes", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible consultar los clientes.");
        }
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cliente cliente = clienteService.buscarPorId(id);
            if (cliente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "El cliente no existe.");
                return;
            }
            request.setAttribute("modoEdicion", true);
            request.setAttribute("idCliente", cliente.getIdCliente());
            request.setAttribute("nombreCompleto", cliente.getNombreCompleto());
            request.setAttribute("documento", cliente.getDocumento());
            request.setAttribute("telefono", cliente.getTelefono());
            request.setAttribute("correo", cliente.getCorreo());
            mostrarFormulario(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID del cliente no es válido.");
        } catch (SQLException e) {
            getServletContext().log("Error al consultar cliente", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No fue posible consultar el cliente.");
        }
    }

    // ---------------- POST: guardar, actualizar, desactivar ----------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        getServletContext().log("ClienteServlet - Acción recibida: " + accion);

        if ("guardar".equals(accion)) {
            guardarCliente(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarCliente(request, response);
        } else if ("desactivar".equals(accion)) {
            desactivarCliente(request, response);
        } else if ("activar".equals(accion)) {
            activarCliente(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Operación no válida: " + (accion == null ? "acción ausente" : accion));
        }
    }

    private void guardarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombreCompleto = request.getParameter("nombreCompleto");
        String documento = request.getParameter("documento");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        try {
            Cliente cliente = new Cliente();
            cliente.setNombreCompleto(nombreCompleto);
            cliente.setDocumento(documento);
            cliente.setTelefono(telefono);
            cliente.setCorreo(correo);

            clienteService.registrar(cliente);
            response.sendRedirect(request.getContextPath() + "/clientes?registro=exitoso");
        } catch (IllegalArgumentException e) {
            conservarDatos(request, nombreCompleto, documento, telefono, correo);
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
        } catch (SQLException e) {
            getServletContext().log("Error al registrar cliente", e);
            conservarDatos(request, nombreCompleto, documento, telefono, correo);
            request.setAttribute("error",
                    "No fue posible registrar el cliente. Verifique los datos ingresados.");
            mostrarFormulario(request, response);
        }
    }

    private void actualizarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idTexto = request.getParameter("idCliente");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String documento = request.getParameter("documento");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID del cliente no es válido.");
            return;
        }

        try {
            Cliente existente = clienteService.buscarPorId(id);
            if (existente == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "El cliente no existe.");
                return;
            }
            // Conserva el estado actual del cliente
            existente.setNombreCompleto(nombreCompleto);
            existente.setDocumento(documento);
            existente.setTelefono(telefono);
            existente.setCorreo(correo);

            clienteService.actualizar(existente);
            response.sendRedirect(request.getContextPath() + "/clientes?actualizacion=exitosa");
        } catch (IllegalArgumentException e) {
            request.setAttribute("modoEdicion", true);
            request.setAttribute("idCliente", id);
            conservarDatos(request, nombreCompleto, documento, telefono, correo);
            request.setAttribute("error", e.getMessage());
            mostrarFormulario(request, response);
        } catch (SQLException e) {
            getServletContext().log("Error al actualizar cliente", e);
            request.setAttribute("modoEdicion", true);
            request.setAttribute("idCliente", id);
            conservarDatos(request, nombreCompleto, documento, telefono, correo);
            request.setAttribute("error",
                    "No fue posible actualizar el cliente. Verifique los datos ingresados.");
            mostrarFormulario(request, response);
        }
    }

    private void desactivarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idTexto = request.getParameter("idCliente");
        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID del cliente no es válido.");
            return;
        }

        try {
            clienteService.desactivar(id);
            response.sendRedirect(request.getContextPath() + "/clientes?desactivacion=exitosa");
        } catch (IllegalArgumentException e) {
            getServletContext().log("No fue posible desactivar el cliente", e);
            response.sendRedirect(request.getContextPath() + "/clientes?desactivacion=error");
        } catch (SQLException e) {
            getServletContext().log("Error SQL al desactivar el cliente", e);
            response.sendRedirect(request.getContextPath() + "/clientes?desactivacion=error");
        }
    }

    private void activarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idTexto = request.getParameter("idCliente");
        int id;
        try {
            id = Integer.parseInt(idTexto);
            if (id <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "El ID del cliente no es válido.");
            return;
        }

        try {
            clienteService.activar(id);
            response.sendRedirect(request.getContextPath() + "/clientes?activacion=exitosa");
        } catch (IllegalArgumentException e) {
            getServletContext().log("No fue posible activar el cliente", e);
            response.sendRedirect(request.getContextPath() + "/clientes?activacion=error");
        } catch (SQLException e) {
            getServletContext().log("Error SQL al activar el cliente", e);
            response.sendRedirect(request.getContextPath() + "/clientes?activacion=error");
        }
    }

    // Devuelve los datos al formulario cuando hay un error
    private void conservarDatos(HttpServletRequest request, String nombreCompleto,
                                String documento, String telefono, String correo) {
        request.setAttribute("nombreCompleto", nombreCompleto);
        request.setAttribute("documento", documento);
        request.setAttribute("telefono", telefono);
        request.setAttribute("correo", correo);
    }
}
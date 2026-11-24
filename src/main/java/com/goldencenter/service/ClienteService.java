package com.goldencenter.service;

import com.goldencenter.dao.ClienteDAO;
import com.goldencenter.dao.impl.ClienteDAOImpl;
import com.goldencenter.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this(new ClienteDAOImpl());
    }

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // Registrar un cliente
    public void registrar(Cliente cliente) throws SQLException {
        validarCliente(cliente);
        if (clienteDAO.buscarPorDocumento(cliente.getDocumento()) != null) {
            throw new IllegalArgumentException(
                "Ya existe un cliente con ese número de documento.");
        }
        cliente.setEstado("Activo");
        clienteDAO.guardar(cliente);
    }

    // Consultar todos los clientes
    public List<Cliente> listar() throws SQLException {
        return clienteDAO.listar();
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }
        return clienteDAO.buscarPorId(id);
    }

    // Buscar clientes por nombre
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar un nombre para buscar.");
        }
        return clienteDAO.buscarPorNombre(nombre.trim());
    }

    // Buscar cliente por documento
    public Cliente buscarPorDocumento(String documento) throws SQLException {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("Debe ingresar un documento para buscar.");
        }
        return clienteDAO.buscarPorDocumento(documento.trim());
    }

    // Actualizar cliente
    public void actualizar(Cliente cliente) throws SQLException {
        validarCliente(cliente);
        if (cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente no es válido.");
        }
        if (clienteDAO.buscarPorId(cliente.getIdCliente()) == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        Cliente conMismoDocumento = clienteDAO.buscarPorDocumento(cliente.getDocumento());
        if (conMismoDocumento != null
                && conMismoDocumento.getIdCliente() != cliente.getIdCliente()) {
            throw new IllegalArgumentException(
                "Ya existe otro cliente con ese número de documento.");
        }
        clienteDAO.actualizar(cliente);
    }

    // Desactivar cliente (sin borrar su historial)
    public void desactivar(int id) throws SQLException {
        Cliente cliente = buscarPorId(id);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        if ("Inactivo".equals(cliente.getEstado())) {
            throw new IllegalArgumentException("El cliente ya está desactivado.");
        }
        clienteDAO.desactivar(id);
    }
    
    // Reactivar cliente
    public void activar(int id) throws SQLException {
        Cliente cliente = buscarPorId(id);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        if ("Activo".equals(cliente.getEstado())) {
            throw new IllegalArgumentException("El cliente ya está activo.");
        }
        clienteDAO.activar(id);
    }

    // Validaciones del negocio
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (cliente.getNombreCompleto() == null || cliente.getNombreCompleto().isBlank()) {
            throw new IllegalArgumentException("El nombre completo es obligatorio.");
        }
        if (cliente.getNombreCompleto().trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar 100 caracteres.");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
            throw new IllegalArgumentException("El documento es obligatorio.");
        }
        if (cliente.getDocumento().trim().length() > 20) {
            throw new IllegalArgumentException("El documento no puede superar 20 caracteres.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        if (cliente.getTelefono().trim().length() > 15) {
            throw new IllegalArgumentException("El teléfono no puede superar 15 caracteres.");
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (!cliente.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo no es válido.");
        }
        if (cliente.getCorreo().trim().length() > 100) {
            throw new IllegalArgumentException("El correo no puede superar 100 caracteres.");
        }
        cliente.setNombreCompleto(cliente.getNombreCompleto().trim());
        cliente.setDocumento(cliente.getDocumento().trim());
        cliente.setTelefono(cliente.getTelefono().trim());
        cliente.setCorreo(cliente.getCorreo().trim());
    }
}

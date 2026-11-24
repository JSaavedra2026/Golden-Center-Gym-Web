package com.goldencenter.service;

import com.goldencenter.dao.ClienteDAO;
import com.goldencenter.dao.MembresiaDAO;
import com.goldencenter.dao.impl.ClienteDAOImpl;
import com.goldencenter.dao.impl.MembresiaDAOImpl;
import com.goldencenter.model.Cliente;
import com.goldencenter.model.Membresia;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class MembresiaService {

    private final MembresiaDAO membresiaDAO;
    private final ClienteDAO clienteDAO;

    public MembresiaService() {
        this(new MembresiaDAOImpl(), new ClienteDAOImpl());
    }

    public MembresiaService(MembresiaDAO membresiaDAO, ClienteDAO clienteDAO) {
        this.membresiaDAO = membresiaDAO;
        this.clienteDAO = clienteDAO;
    }

    // Registrar una membresía para un cliente existente y activo
    public void registrar(Membresia membresia) throws SQLException {
        if (membresia == null || membresia.getCliente() == null
                || membresia.getCliente().getIdCliente() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente.");
        }
        Cliente cliente = clienteDAO.buscarPorId(membresia.getCliente().getIdCliente());
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        if (!"Activo".equals(cliente.getEstado())) {
            throw new IllegalArgumentException(
                "No se puede asignar una membresía a un cliente inactivo.");
        }
        aplicarPlan(membresia);
        membresia.setEstado("Activa");
        membresiaDAO.guardar(membresia);
    }

    // Consultar todas las membresías (primero marca las vencidas)
    public List<Membresia> listar() throws SQLException {
        membresiaDAO.actualizarVencidas();
        return membresiaDAO.listar();
    }

    // Buscar membresía por ID
    public Membresia buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }
        membresiaDAO.actualizarVencidas();
        return membresiaDAO.buscarPorId(id);
    }

    // Buscar las membresías de un cliente
    public List<Membresia> buscarPorCliente(int idCliente) throws SQLException {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser mayor que cero.");
        }
        membresiaDAO.actualizarVencidas();
        return membresiaDAO.buscarPorCliente(idCliente);
    }

    // Actualizar membresía (plan y fecha de inicio)
    public void actualizar(Membresia membresia) throws SQLException {
        if (membresia == null || membresia.getIdMembresia() <= 0) {
            throw new IllegalArgumentException("El ID de la membresía no es válido.");
        }
        if (membresiaDAO.buscarPorId(membresia.getIdMembresia()) == null) {
            throw new IllegalArgumentException("La membresía no existe.");
        }
        aplicarPlan(membresia);
        membresiaDAO.actualizar(membresia);
    }

    // Desactivar membresía (sin borrar su historial)
    public void desactivar(int id) throws SQLException {
        Membresia membresia = buscarPorId(id);
        if (membresia == null) {
            throw new IllegalArgumentException("La membresía no existe.");
        }
        if ("Inactiva".equals(membresia.getEstado())) {
            throw new IllegalArgumentException("La membresía ya está desactivada.");
        }
        membresiaDAO.desactivar(id);
    }
    
    // Reactivar membresía (solo si el cliente está activo)
    public void activar(int id) throws SQLException {
        Membresia membresia = buscarPorId(id);
        if (membresia == null) {
            throw new IllegalArgumentException("La membresía no existe.");
        }
        if (!"Inactiva".equals(membresia.getEstado())) {
            throw new IllegalArgumentException("La membresía no está desactivada.");
        }
        Cliente cliente = clienteDAO.buscarPorId(membresia.getCliente().getIdCliente());
        if (cliente == null || !"Activo".equals(cliente.getEstado())) {
            throw new IllegalArgumentException(
                "No se puede activar una membresía de un cliente inactivo.");
        }
        membresiaDAO.activar(id);
    }

    // Asigna duración y costo según el plan, y calcula la fecha de vencimiento
    private void aplicarPlan(Membresia membresia) {
        if (membresia.getTipoMembresia() == null || membresia.getTipoMembresia().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar un plan de membresía.");
        }
        if (membresia.getFechaInicio() == null) {
            throw new IllegalArgumentException("La fecha de inicio es obligatoria.");
        }
        switch (membresia.getTipoMembresia()) {
            case "Plan semanal":
                membresia.setDuracion(7);
                membresia.setCosto(new BigDecimal("40000"));
                break;
            case "Plan quincenal":
                membresia.setDuracion(15);
                membresia.setCosto(new BigDecimal("55000"));
                break;
            case "Plan mensual":
                membresia.setDuracion(30);
                membresia.setCosto(new BigDecimal("65000"));
                break;
            case "Rutina por sesión":
                membresia.setDuracion(1);
                membresia.setCosto(new BigDecimal("15000"));
                break;
            default:
                throw new IllegalArgumentException("El plan de membresía no es válido.");
        }
        membresia.setFechaVencimiento(
            membresia.getFechaInicio().plusDays(membresia.getDuracion()));
    }
}
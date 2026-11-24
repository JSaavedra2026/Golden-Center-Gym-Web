package com.goldencenter.dao;

import com.goldencenter.model.Membresia;

import java.sql.SQLException;
import java.util.List;

public interface MembresiaDAO {

    // CREATE: Registrar membresía
    void guardar(Membresia membresia) throws SQLException;

    // READ: Listar todas las membresías
    List<Membresia> listar() throws SQLException;

    // READ: Buscar membresía por ID
    Membresia buscarPorId(int id) throws SQLException;

    // READ: Buscar membresías de un cliente
    List<Membresia> buscarPorCliente(int idCliente) throws SQLException;

    // UPDATE: Actualizar membresía
    void actualizar(Membresia membresia) throws SQLException;

    // DELETE lógico: Desactivar membresía
    void desactivar(int id) throws SQLException;
    
    // Reactivar membresía
    void activar(int id) throws SQLException;

    // Validación: marcar como vencidas las membresías cuya fecha ya pasó
    int actualizarVencidas() throws SQLException;
}

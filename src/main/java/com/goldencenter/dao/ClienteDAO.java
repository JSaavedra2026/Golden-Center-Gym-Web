package com.goldencenter.dao;

import com.goldencenter.model.Cliente;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {

    // CREATE: Registrar cliente
    void guardar(Cliente cliente) throws SQLException;

    // READ: Listar todos los clientes
    List<Cliente> listar() throws SQLException;

    // READ: Buscar cliente por ID
    Cliente buscarPorId(int id) throws SQLException;

    // READ: Buscar cliente por documento
    Cliente buscarPorDocumento(String documento) throws SQLException;

    // READ: Buscar clientes por nombre
    List<Cliente> buscarPorNombre(String nombre) throws SQLException;

    // UPDATE: Actualizar cliente
    void actualizar(Cliente cliente) throws SQLException;

    // DELETE lógico: Desactivar cliente
    void desactivar(int id) throws SQLException;
    
    // Reactivar cliente
    void activar(int id) throws SQLException;
}
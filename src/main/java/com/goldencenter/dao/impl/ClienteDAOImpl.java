package com.goldencenter.dao.impl;

import com.goldencenter.config.ConexionBD;
import com.goldencenter.dao.ClienteDAO;
import com.goldencenter.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void guardar(Cliente cliente) throws SQLException {
        String sql = """
            INSERT INTO cliente
            (nombre_completo, documento, correo, telefono, estado)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEstado());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = """
            SELECT id_cliente, nombre_completo, documento,
                   telefono, correo, estado
            FROM cliente
            ORDER BY nombre_completo
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }

    @Override
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = """
            SELECT id_cliente, nombre_completo, documento,
                   telefono, correo, estado
            FROM cliente
            WHERE id_cliente = ?
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public Cliente buscarPorDocumento(String documento) throws SQLException {
        String sql = """
            SELECT id_cliente, nombre_completo, documento,
                   telefono, correo, estado
            FROM cliente
            WHERE documento = ?
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> buscarPorNombre(String nombre) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = """
            SELECT id_cliente, nombre_completo, documento,
                   telefono, correo, estado
            FROM cliente
            WHERE nombre_completo LIKE ?
            ORDER BY nombre_completo
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }
        return clientes;
    }

    @Override
    public void actualizar(Cliente cliente) throws SQLException {
        String sql = """
            UPDATE cliente
            SET nombre_completo = ?,
                documento = ?,
                correo = ?,
                telefono = ?
            WHERE id_cliente = ?
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setInt(5, cliente.getIdCliente());
            ps.executeUpdate();
        }
    }

    @Override
    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE cliente SET estado = 'Inactivo' WHERE id_cliente = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    @Override
    public void activar(int id) throws SQLException {
        String sql = "UPDATE cliente SET estado = 'Activo' WHERE id_cliente = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Convierte una fila de la base de datos en un objeto Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nombre_completo"),
                rs.getString("documento"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("estado"));
    }
}
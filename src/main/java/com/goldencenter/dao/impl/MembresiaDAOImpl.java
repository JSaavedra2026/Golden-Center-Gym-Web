package com.goldencenter.dao.impl;

import com.goldencenter.config.ConexionBD;
import com.goldencenter.dao.MembresiaDAO;
import com.goldencenter.model.Cliente;
import com.goldencenter.model.Membresia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembresiaDAOImpl implements MembresiaDAO {

    // Consulta base: une cada membresía con su cliente
    private static final String SQL_BASE = """
        SELECT m.id_membresia, m.tipo_membresia, m.duracion, m.costo,
               m.fecha_inicio, m.fecha_vencimiento, m.estado,
               c.id_cliente, c.nombre_completo, c.documento
        FROM membresia m
        INNER JOIN cliente c ON m.id_cliente = c.id_cliente
        """;

    @Override
    public void guardar(Membresia membresia) throws SQLException {
        String sql = """
            INSERT INTO membresia
            (fecha_inicio, fecha_vencimiento, duracion, costo,
             tipo_membresia, estado, id_cliente)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(2, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(3, membresia.getDuracion());
            ps.setBigDecimal(4, membresia.getCosto());
            ps.setString(5, membresia.getTipoMembresia());
            ps.setString(6, membresia.getEstado());
            ps.setInt(7, membresia.getCliente().getIdCliente());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Membresia> listar() throws SQLException {
        List<Membresia> membresias = new ArrayList<>();
        String sql = SQL_BASE + " ORDER BY m.id_membresia";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                membresias.add(mapearMembresia(rs));
            }
        }
        return membresias;
    }

    @Override
    public Membresia buscarPorId(int id) throws SQLException {
        String sql = SQL_BASE + " WHERE m.id_membresia = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMembresia(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Membresia> buscarPorCliente(int idCliente) throws SQLException {
        List<Membresia> membresias = new ArrayList<>();
        String sql = SQL_BASE + " WHERE m.id_cliente = ? ORDER BY m.id_membresia";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    membresias.add(mapearMembresia(rs));
                }
            }
        }
        return membresias;
    }

    @Override
    public void actualizar(Membresia membresia) throws SQLException {
        String sql = """
            UPDATE membresia
            SET tipo_membresia = ?,
                duracion = ?,
                costo = ?,
                fecha_inicio = ?,
                fecha_vencimiento = ?
            WHERE id_membresia = ?
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, membresia.getTipoMembresia());
            ps.setInt(2, membresia.getDuracion());
            ps.setBigDecimal(3, membresia.getCosto());
            ps.setDate(4, Date.valueOf(membresia.getFechaInicio()));
            ps.setDate(5, Date.valueOf(membresia.getFechaVencimiento()));
            ps.setInt(6, membresia.getIdMembresia());
            ps.executeUpdate();
        }
    }

    @Override
    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE membresia SET estado = 'Inactiva' WHERE id_membresia = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    @Override
    public void activar(int id) throws SQLException {
        String sql = """
            UPDATE membresia
            SET estado = CASE
                WHEN fecha_vencimiento < CURDATE() THEN 'Vencida'
                ELSE 'Activa'
            END
            WHERE id_membresia = ?
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public int actualizarVencidas() throws SQLException {
        String sql = """
            UPDATE membresia
            SET estado = 'Vencida'
            WHERE estado = 'Activa' AND fecha_vencimiento < CURDATE()
            """;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

    // Convierte una fila (membresía + cliente) en un objeto Membresia
    private Membresia mapearMembresia(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombreCompleto(rs.getString("nombre_completo"));
        cliente.setDocumento(rs.getString("documento"));

        Membresia membresia = new Membresia();
        membresia.setIdMembresia(rs.getInt("id_membresia"));
        membresia.setTipoMembresia(rs.getString("tipo_membresia"));
        membresia.setDuracion(rs.getInt("duracion"));
        membresia.setCosto(rs.getBigDecimal("costo"));
        membresia.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        membresia.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        membresia.setEstado(rs.getString("estado"));
        membresia.setCliente(cliente);
        return membresia;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author pbo2_2310010388
 */
public class tbl_perkara {
    private String namadb = "pbo2_2310010388"; // sesuai database di phpMyAdmin
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel untuk validasi data
    public String VAR_nama_perkara = null;
    public String VAR_uud_perkara = null;
    public boolean validasi = false;

    public tbl_perkara() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database pbo2_2310010388 (tbl_perkara)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // === SIMPAN DATA ===
    public void simpanPerkara(Integer id, String nama_perkara, String uud_perkara) {
        try {
            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM tbl_perkara WHERE id = ?";
            PreparedStatement check = koneksi.prepareStatement(cekPrimary);
            check.setInt(1, id);
            ResultSet data = check.executeQuery();

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID perkara sudah terdaftar!");
                this.VAR_nama_perkara = data.getString("nama_perkara");
                this.VAR_uud_perkara = data.getString("uud_perkara");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tbl_perkara (id, nama_perkara, uud_perkara) VALUES (?, ?, ?)";
                PreparedStatement perintah = koneksi.prepareStatement(sql);
                perintah.setInt(1, id);
                perintah.setString(2, nama_perkara);
                perintah.setString(3, uud_perkara);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data perkara berhasil disimpan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === UBAH DATA ===
    public void ubahPerkara(Integer id, String nama_perkara, String uud_perkara) {
        try {
            String sql = "UPDATE tbl_perkara SET nama_perkara=?, uud_perkara=? WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, nama_perkara);
            perintah.setString(2, uud_perkara);
            perintah.setInt(3, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data perkara berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === HAPUS DATA ===
    public void hapusPerkara(Integer id) {
        try {
                String sql = "DELETE FROM tbl_perkara WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data perkara berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
        

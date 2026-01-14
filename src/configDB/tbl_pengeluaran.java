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
import java.io.File;
import java.util.Set;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author pbo2_2310010388
 */
public class tbl_pengeluaran {
    private String namadb = "pbo2_2310010388"; // sesuai database di phpMyAdmin
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel untuk validasi data
    public String VAR_tanggal = null;
    public String VAR_nama = null;
    public String VAR_nominal = null;
    public String VAR_keterangan = null;
    public boolean validasi = false;

    public tbl_pengeluaran() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database pbo2_2310010388 (tbl_pengeluaran)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    public void simpanPengeluaran(Integer id, String tanggal, String nama, String nominal, String keterangan) {
        try {
            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM tbl_pengeluaran WHERE id = ?";
            PreparedStatement check = koneksi.prepareStatement(cekPrimary);
            check.setInt(1, id);
            ResultSet data = check.executeQuery();

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID pengeluaran sudah terdaftar!");
                this.VAR_tanggal = data.getString("tanggal_pengeluaran");
                this.VAR_nama = data.getString("nama_pengeluaran");
                this.VAR_nominal = data.getString("nominal");
                this.VAR_keterangan = data.getString("keterangan");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tbl_pengeluaran (id, tanggal_pengeluaran, nama_pengeluaran, nominal, keterangan) "
                           + "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement perintah = koneksi.prepareStatement(sql);
                perintah.setInt(1, id);
                perintah.setString(2, tanggal);
                perintah.setString(3, nama);
                perintah.setString(4, nominal);
                perintah.setString(5, keterangan);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data pengeluaran berhasil disimpan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === UBAH DATA ===
    public void ubahPengeluaran(Integer id, String tanggal, String nama, String nominal, String keterangan) {
        try {
            String sql = "UPDATE tbl_pengeluaran SET tanggal_pengeluaran=?, nama_pengeluaran=?, nominal=?, keterangan=? WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, tanggal);
            perintah.setString(2, nama);
            perintah.setString(3, nominal);
            perintah.setString(4, keterangan);
            perintah.setInt(5, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data pengeluaran berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === HAPUS DATA ===
    public void hapusPengeluaran(Integer id) {
        try {
            String sql = "DELETE FROM tbl_pengeluaran WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data pengeluaran berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }
     public void cetakLaporan(String fileLaporan, String SQL){
        try {
            File file = new File(fileLaporan);
            JasperDesign jasDes = JRXmlLoader.load(file);
            JRDesignQuery query = new JRDesignQuery();
            query.setText(SQL);
            jasDes.setQuery(query);
            JasperReport jr = JasperCompileManager.compileReport(jasDes);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, this.koneksi);
            JasperViewer.viewReport(jp);
            
        } catch (Exception e) {
        }
    }
     
     // === PENCARIAN DATA PENGELUARAN ===
public ResultSet cariPengeluaran(String keyword) {
    try {
        String sql = "SELECT * FROM tbl_pengeluaran WHERE "
                   + "id LIKE ? OR "
                   + "tanggal_pengeluaran LIKE ? OR "
                   + "nama_pengeluaran LIKE ? OR "
                   + "nominal LIKE ? OR "
                   + "keterangan LIKE ?";

        PreparedStatement pst = koneksi.prepareStatement(sql);
        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");
        pst.setString(4, "%" + keyword + "%");
        pst.setString(5, "%" + keyword + "%");

        return pst.executeQuery();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mencari data: " + e.getMessage());
        return null;
    }
}

}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
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
public class tbl_pemasukan {
    private String namadb = "pbo2_2310010388"; // sesuai database di phpMyAdmin
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel untuk validasi data
    public String VAR_tanggal = null;
    public String VAR_nominal = null;
    public String VAR_asal = null;
    public String VAR_keterangan = null;
    public boolean validasi = false;

    public tbl_pemasukan() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database pbo2_2310010388 (tbl_pemasukan)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // === SIMPAN DATA ===
    public void simpanPemasukan(Integer id, String tanggal, String nominal, String asal, String keterangan) {
        try {
            // cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM tbl_pemasukan WHERE id = ?";
            PreparedStatement check = koneksi.prepareStatement(cekPrimary);
            check.setInt(1, id);
            ResultSet data = check.executeQuery();

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID pemasukan sudah terdaftar!");
                this.VAR_tanggal = data.getString("tanggal_pemasukan");
                this.VAR_nominal = data.getString("nominal");
                this.VAR_asal = data.getString("asal_pemasukan");
                this.VAR_keterangan = data.getString("keterangan");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tbl_pemasukan (id, tanggal_pemasukan, nominal, asal_pemasukan, keterangan) "
                           + "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement perintah = koneksi.prepareStatement(sql);
                perintah.setInt(1, id);
                perintah.setString(2, tanggal);
                perintah.setString(3, nominal);
                perintah.setString(4, asal);
                perintah.setString(5, keterangan);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data pemasukan berhasil disimpan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === UBAH DATA ===
    public void ubahPemasukan(Integer id, String tanggal, String nominal, String asal, String keterangan) {
        try {
            String sql = "UPDATE tbl_pemasukan SET tanggal_pemasukan=?, nominal=?, asal_pemasukan=?, keterangan=? WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, tanggal);
            perintah.setString(2, nominal);
            perintah.setString(3, asal);
            perintah.setString(4, keterangan);
            perintah.setInt(5, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data pemasukan berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === HAPUS DATA ===
    public void hapusPemasukan(Integer id) {
        try {
            String sql = "DELETE FROM tbl_pemasukan WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data pemasukan berhasil dihapus!");
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
    
    // === PENCARIAN DATA PEMASUKAN ===
public ResultSet cariPemasukan(String keyword) {
    ResultSet rs = null;
    try {
        String sql = "SELECT * FROM tbl_pemasukan " +
                     "WHERE tanggal_pemasukan LIKE ? " +
                     "OR asal_pemasukan LIKE ? " +
                     "OR keterangan LIKE ?";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        ps.setString(2, "%" + keyword + "%");
        ps.setString(3, "%" + keyword + "%");

        rs = ps.executeQuery();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mencari data: " + e.getMessage());
    }
    return rs;
}

}

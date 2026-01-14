package configDB;

import java.sql.*;
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

public class tbl_surat_masuk {
    private String namadb = "pbo2_2310010388"; // sesuai database di phpMyAdmin
    private String url = "jdbc:mysql://localhost:3306/" + namadb;
    private String username = "root";
    private String password = "";
    private Connection koneksi;

    // Variabel untuk validasi data
    public String VAR_no_surat = null;
    public String VAR_perihal = null;
    public String VAR_isi = null;
    public String VAR_tanggal = null;
    public String VAR_perkara = null;
    public boolean validasi = false;

    public tbl_surat_masuk() {
        try {
            Driver mysqlDriver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);
            koneksi = DriverManager.getConnection(url, username, password);
            System.out.println("Berhasil dikoneksikan ke database pbo2_2310010388 (tbl_surat_masuk)");
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null, "Koneksi gagal: " + error.getMessage());
        }
    }

    // === SIMPAN DATA ===
    public void simpanSurat(Integer id, String no_surat, String perihal_surat, String isi_surat, Date tanggal_surat, String perkara) {
        try {
            // Cek apakah ID sudah ada
            String cekPrimary = "SELECT * FROM tbl_surat_masuk WHERE id = ?";
            PreparedStatement check = koneksi.prepareStatement(cekPrimary);
            check.setInt(1, id);
            ResultSet data = check.executeQuery();

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID surat sudah terdaftar!");
                this.VAR_no_surat = data.getString("no_surat");
                this.VAR_perihal = data.getString("perihal_surat");
                this.VAR_isi = data.getString("isi_surat");
                this.VAR_tanggal = data.getString("tanggal_surat");
                this.VAR_perkara = data.getString("perkara");
                this.validasi = true;
            } else {
                this.validasi = false;
                String sql = "INSERT INTO tbl_surat_masuk (id, no_surat, perihal_surat, isi_surat, tanggal_surat, perkara) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement perintah = koneksi.prepareStatement(sql);
                perintah.setInt(1, id);
                perintah.setString(2, no_surat);
                perintah.setString(3, perihal_surat);
                perintah.setString(4, isi_surat);
                perintah.setDate(5, tanggal_surat);
                perintah.setString(6, perkara);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data surat masuk berhasil disimpan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === UBAH DATA ===
    public void ubahSurat(Integer id, String no_surat, String perihal_surat, String isi_surat, Date tanggal_surat, String perkara) {
        try {
            String sql = "UPDATE tbl_surat_masuk SET no_surat=?, perihal_surat=?, isi_surat=?, tanggal_surat=?, perkara=? WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setString(1, no_surat);
            perintah.setString(2, perihal_surat);
            perintah.setString(3, isi_surat);
            perintah.setDate(4, tanggal_surat);
            perintah.setString(5, perkara);
            perintah.setInt(6, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data surat masuk berhasil diubah!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengubah: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // === HAPUS DATA ===
    public void hapusSurat(Integer id) {
        try {
            String sql = "DELETE FROM tbl_surat_masuk WHERE id=?";
            PreparedStatement perintah = koneksi.prepareStatement(sql);
            perintah.setInt(1, id);
            int hasil = perintah.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(null, "Data surat masuk berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "ID tidak ditemukan!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menghapus: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public ResultSet tampilSurat() {
    ResultSet rs = null;
    try {
        String sql = "SELECT * FROM tbl_surat_masuk ORDER BY id ASC";
        PreparedStatement pst = koneksi.prepareStatement(sql);
        rs = pst.executeQuery();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengambil data: " + e.getMessage());
    }
    return rs;
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
         
         public ResultSet cariSuratMasuk(String keyword) {
    try {
        String sql = "SELECT * FROM tbl_surat_masuk WHERE "
                   + "id LIKE ? OR "
                   + "no_surat LIKE ? OR "
                   + "perihal_surat LIKE ? OR "
                   + "isi_surat LIKE ? OR "
                   + "tanggal_surat LIKE ? OR "
                   + "perkara LIKE ?";

        PreparedStatement pst = koneksi.prepareStatement(sql);
        for (int i = 1; i <= 6; i++) {
            pst.setString(i, "%" + keyword + "%");
        }

        return pst.executeQuery();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Gagal mencari surat masuk: " + e.getMessage());
        return null;
    }
}
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgRujuk.java
 *
 * Created on 31 Mei 10, 20:19:56
 */
package rekammedis;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import simrskhanza.DlgCariPoli;
import laporan.DlgCariPenyakit;

/**
 *
 * @author perpustakaan
 */
public final class RMHemodialisa extends javax.swing.JDialog {

    private final DefaultTableModel tabDataHD, tabMonitoringHD, tabRencanaHD, tabLaporanHD, tabLayananHD;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private PreparedStatement ps;
    private ResultSet rs;
    private int i = 0, pilihan = 0;
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private DlgCariPenyakit penyakit = new DlgCariPenyakit(null, false);
    private DlgCariPoli poli=new DlgCariPoli(null,false);

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMHemodialisa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(628, 674);
        
        tabRencanaHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat",      // 0 - no_rawat
            "No.R.M.",       // 1 - no_rkm_medis  
            "Nama Pasien",   // 2 - nm_pasien
            "Tanggal Lahir", // 3 - tgl_lahir (dari query)
            "Tanggal",       // 4 - tanggal
            "Rencana",       // 5 - rencana
            "Kode Dokter",   // 6 - kd_dokter (hidden)
            "Dokter",        // 7 - nm_dokter
            "Kode Poli",     // 8 - kd_poli (hidden)
            "Poliklinik"     // 9 - nm_poli
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbRencanaHD.setModel(tabRencanaHD);

        tbRencanaHD.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbRencanaHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 10; i++) {
            TableColumn column = tbRencanaHD.getColumnModel().getColumn(i);  // <-- BENAR: tbRencanaHD
            if (i == 0) {
                column.setPreferredWidth(105);      // No.Rawat
            } else if (i == 1) {
                column.setPreferredWidth(65);       // No.R.M.
            } else if (i == 2) {
                column.setPreferredWidth(160);      // Nama Pasien
            } else if (i == 3) {
                column.setPreferredWidth(100);      // Tanggal Lahir
            } else if (i == 4) {
                column.setPreferredWidth(120);      // Tanggal
            } else if (i == 5) {
                column.setPreferredWidth(200);      // Rencana
            } else if (i == 6) {
                column.setMinWidth(0);              // Kode Dokter (hidden)
                column.setMaxWidth(0);
            } else if (i == 7) {
                column.setPreferredWidth(150);      // Dokter
            } else if (i == 8) {
                column.setMinWidth(0);              // Kode Poli (hidden)
                column.setMaxWidth(0);
            } else if (i == 9) {
                column.setPreferredWidth(150);      // Poliklinik
            }
        }
        tbRencanaHD.setDefaultRenderer(Object.class, new WarnaTable());

        tabDataHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.R.M.", "Nama Pasien", "Umur", "JK", "Tanggal",
            "Kode Dokter", "Dokter", "Kode Petugas", "Petugas", "Durasi",
            "BB Pre", "BB Lalu", "BB Naik", "BB Post", "Status Dialiser",
            "Penggunaan Dialiser", "Akses Vaskuler", "Jadwal", "Hari"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbDataHD.setModel(tabDataHD);

        tbDataHD.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDataHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 20; i++) {
            TableColumn column = tbDataHD.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(105);      // No.Rawat
            } else if (i == 1) {
                column.setPreferredWidth(65);       // No.R.M.
            } else if (i == 2) {
                column.setPreferredWidth(160);      // Nama Pasien
            } else if (i == 3) {
                column.setPreferredWidth(35);       // Umur
            } else if (i == 4) {
                column.setPreferredWidth(20);       // JK
            } else if (i == 5) {
                column.setPreferredWidth(120);      // Tanggal
            } else if (i == 6) {
                column.setMinWidth(0);              // Kode Dokter (hidden)
                column.setMaxWidth(0);
            } else if (i == 7) {
                column.setPreferredWidth(150);        // Dokter
            } else if (i == 8) {
                column.setMinWidth(0);              // Kode Petugas (hidden)
                column.setMaxWidth(0);
            } else if (i == 9) {
                column.setPreferredWidth(150);      // Petugas
            } else if (i == 10) {
                column.setPreferredWidth(50);       // Durasi
            } else if (i == 11) {
                column.setPreferredWidth(60);       // BB Pre
            } else if (i == 12) {
                column.setPreferredWidth(60);       // BB Lalu
            } else if (i == 13) {
                column.setPreferredWidth(60);       // BB Naik
            } else if (i == 14) {
                column.setPreferredWidth(60);       // BB Post
            } else if (i == 15) {
                column.setPreferredWidth(100);      // Status Dialiser
            } else if (i == 16) {
                column.setPreferredWidth(120);      // Penggunaan Dialiser
            } else if (i == 17) {
                column.setPreferredWidth(120);      // Akses Vaskuler
            } else if (i == 18) {
                column.setPreferredWidth(100);      // Jadwal
            } else if (i == 19) {
                column.setPreferredWidth(50);       // Hari
            }
        }
        tbDataHD.setDefaultRenderer(Object.class, new WarnaTable());

        tabLaporanHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat", 
            "No.R.M.", 
            "Nama Pasien", 
            "Tanggal Lahir", 
            "Tanggal",
            "Resep HD", 
            "Time HD", 
            "QB", 
            "QD", 
            "Ultrafiltration Goal", 
            "Ultrafiltration Rate",
            "Dialiser",
            "Na Start",
            "Conductivity",
            "Temperature",
            "Dosis Sirkulasi",
            "LMWH",
            "Dosis Awal",
            "Penyebab Tanpa Heparin",
            "Continous",
            "Intermitten",
            "Jenis Dial 1",
            "Ultrafiltration",
            "Asetat",
            "Bicarbonat",
            "Bilas",
            "UFH",
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Non-editable table
            }
        };
        tbLaporanHD.setModel(tabLaporanHD);

        tbLaporanHD.setPreferredScrollableViewportSize(new Dimension(800, 600));
        tbLaporanHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 27; i++) {
            TableColumn column = tbLaporanHD.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(105);  // No.Rawat
                    break;
                case 1:
                    column.setPreferredWidth(65);   // No.R.M.
                    break;
                case 2:
                    column.setPreferredWidth(160);  // Nama Pasien
                    break;
                case 3:
                    column.setPreferredWidth(100);  // Tanggal Lahir
                    break;
                case 4:
                    column.setPreferredWidth(100);  // Tanggal
                    break;
                case 5:
                    column.setPreferredWidth(100);  // Resep HD
                    break;
                case 6:
                    column.setPreferredWidth(80);   // Time HD
                    break;
                case 7:
                    column.setPreferredWidth(60);   // QB
                    break;
                case 8:
                    column.setPreferredWidth(60);   // QD
                    break;
                case 9:
                    column.setPreferredWidth(120);  // Ultrafiltration Goal
                    break;
                case 10:
                    column.setPreferredWidth(120);  // Ultrafiltration Rate
                    break;
                case 11:
                    column.setPreferredWidth(100);  // Dialiser
                    break;
                case 12:
                    column.setPreferredWidth(70);   // Na Start
                    break;
                case 13:
                    column.setPreferredWidth(90);   // Conductivity
                    break;
                case 14:
                    column.setPreferredWidth(90);   // Temperature
                    break;
                case 15:
                    column.setPreferredWidth(110);  // Dosis Sirkulasi
                    break;
                case 16:
                    column.setPreferredWidth(80);   // LMWH
                    break;
                case 17:
                    column.setPreferredWidth(90);   // Dosis Awal
                    break;
                case 18:
                    column.setPreferredWidth(150);  // Penyebab Tanpa Heparin
                    break;
                case 19:
                    column.setPreferredWidth(80);   // Continous
                    break;
                case 20:
                    column.setPreferredWidth(90);   // Intermitten
                    break;
                case 21:
                    column.setMinWidth(0);          
                    column.setMaxWidth(0);
                    break;
                case 22:
                    column.setMinWidth(0);          
                    column.setMaxWidth(0);
                    break;
                case 23:
                    column.setMinWidth(0);       
                    column.setMaxWidth(0);
                    break;
                case 24:
                    column.setMinWidth(0);  
                    column.setMaxWidth(0);
                    break;
                case 25:
                    column.setMinWidth(0);   
                    column.setMaxWidth(0);
                    break;
                case 26:
                    column.setMinWidth(0);   
                    column.setMaxWidth(0);
                    break;
                default:
                    column.setPreferredWidth(50);   // Default width
                    break;
            }
        }

        tbLaporanHD.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMonitoringHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat",
            "No.R.M.", 
            "Nama Pasien", 
            "Tanggal Lahir", 
            "Jam", 
            "Tekanan Vena", 
            "Qb", 
            "VP", 
            "TMP", 
            "Suhu",
            "UFR", 
            "Conductivity", 
            "Lain-lain",
            "NaCL",
            "Dextrose",
            "Makan/Minum",
            "Input Lain-lain",
            "UF Goal"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Non-editable table
            }
        };
        tbMonitoringHD.setModel(tabMonitoringHD);

        tbMonitoringHD.setPreferredScrollableViewportSize(new Dimension(800, 600));
        tbMonitoringHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < tbMonitoringHD.getColumnModel().getColumnCount(); i++) {
            TableColumn column = tbMonitoringHD.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(105);  // No.Rawat
                    break;
                case 1:
                    column.setPreferredWidth(65);   // No.R.M.
                    break;
                case 2:
                    column.setPreferredWidth(160);  // Nama Pasien
                    break;
                case 3:
                    column.setPreferredWidth(100);  // Tanggal Lahir
                    break;
                case 4:
                    column.setPreferredWidth(120);  // Jam
                    break;
                case 5:
                    column.setPreferredWidth(100);  // Tekanan Vena
                    break;
                case 6:
                    column.setPreferredWidth(60);   // Qb
                    break;
                case 7:
                    column.setPreferredWidth(60);   // VP
                    break;
                case 8:
                    column.setPreferredWidth(70);   // TMP
                    break;
                case 9:
                    column.setPreferredWidth(60);   // Suhu
                    break;
                case 10:
                    column.setPreferredWidth(60);   // UFR
                    break;
                case 11:
                    column.setPreferredWidth(90); // Conductivity
                    break;
                case 12:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                case 13:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                case 14:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                case 15:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                case 16:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                case 17:
                    column.setPreferredWidth(150);  // Lain-lain
                    break;
                default:
                    column.setPreferredWidth(50);   // Default width
                    break;
            }
        }

        tbMonitoringHD.setDefaultRenderer(Object.class, new WarnaTable());

        tabLayananHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat",
            "No.R.M.", 
            "Nama Pasien", 
            "Tanggal Lahir", 
            "Finger",
            "Tindakan HD",
            "Tindakan Reuse",
            "Dialiser",
            "Reuse",
            "Layanan Keperawatan",
            "Edukasi",
            "Keterangan Edukasi",
            "Lain-lain",
            "Keterangan Lain"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Non-editable table
            }
        };

        tbLayananHD.setModel(tabLayananHD);

        tbLayananHD.setPreferredScrollableViewportSize(new Dimension(800, 600));
        tbLayananHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < tbLayananHD.getColumnModel().getColumnCount(); i++) {
            TableColumn column = tbLayananHD.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(105);  // No.Rawat
                    break;
                case 1:
                    column.setPreferredWidth(65);   // No.R.M.
                    break;
                case 2:
                    column.setPreferredWidth(160);  // Nama Pasien
                    break;
                case 3:
                    column.setPreferredWidth(100);  // Tanggal Lahir
                    break;
                case 4:
                    column.setPreferredWidth(120);  // Tanggal
                    break;
                case 5:
                    column.setPreferredWidth(100);  // Lama HD
                    break;
                case 6:
                    column.setPreferredWidth(80);   // UFG
                    break;
                case 7:
                    column.setPreferredWidth(80);   // QB
                    break;
                case 8:
                    column.setPreferredWidth(100);  // Heparin
                    break;
                case 9:
                    column.setPreferredWidth(100);  // Total
                    break;
                case 10:
                    column.setPreferredWidth(120);  // IU Bolus Awal
                    break;
                case 11:
                    column.setPreferredWidth(100);  // Kontinyu
                    break;
                case 12:
                    column.setPreferredWidth(100);  // Lain-lain
                    break;
                case 13:
                    column.setPreferredWidth(140);  // Perubahan Obat Rutin
                    break;
                default:
                    column.setPreferredWidth(50);   // Default width for undefined columns

            }
        }

        tbLayananHD.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        kddok.setDocument(new batasInput((byte) 20).getKata(kddok));
        Durasi.setDocument(new batasInput((int) 10).getKata(Durasi));
        Dialiser.setDocument(new batasInput((int) 50).getKata(Dialiser));
        BB_Pre.setDocument(new batasInput((int) 10).getKata(BB_Pre));
        TCari.setDocument(new batasInput((int) 100).getKata(TCari));

        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilDataHD();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilDataHD();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilDataHD();
                    }
                }
            });
        }

        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (dokter.getTable().getSelectedRow() != -1) {
                    if (pilihan == 1) {
                        kddok.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        namadokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    } else if (pilihan == 2) {
                        kddok1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        namadokter1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    }
                }
                kddok.requestFocus();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        
        poli.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (poli.getTable().getSelectedRow() != -1) {
                    kdpoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 0).toString());
                    namapoli.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 1).toString());
                }
                kdpoli.requestFocus();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        penyakit.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (petugas.getTable().getSelectedRow() != -1) {
                    switch (TabRawat.getSelectedIndex()) {
                        case 0:
                            kdpoli.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                            namapoli.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                            break;
                        case 1:
                            kdpetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                            namapetugas.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
                            break;
                        default:
                    }

                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        ChkInput.setSelected(false);
        isForm();

        kddok.setText(Sequel.cariIsi("select set_pjlab.kd_dokterhemodialisa from set_pjlab"));
        namadokter.setText(dokter.tampil3(kddok.getText()));

        jam();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu2 = new javax.swing.JPopupMenu();
        MnCetakObservasiIntradialitik = new javax.swing.JMenuItem();
        JK = new widget.TextBox();
        Umur = new widget.TextBox();
        TanggalRegistrasi = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new widget.TabPane();
        internalFrame4 = new widget.InternalFrame();
        Scroll1 = new widget.ScrollPane();
        tbRencanaHD = new widget.Table();
        PanelInput1 = new javax.swing.JPanel();
        FormInput1 = new widget.PanelBiasa();
        jLabel23 = new widget.Label();
        kddok1 = new widget.TextBox();
        namadokter1 = new widget.TextBox();
        btnDokter1 = new widget.Button();
        jLabel45 = new widget.Label();
        jLabel47 = new widget.Label();
        Tanggal2 = new widget.Tanggal();
        jLabel49 = new widget.Label();
        jLabel50 = new widget.Label();
        kdpoli = new widget.TextBox();
        namapoli = new widget.TextBox();
        btnPoli = new widget.Button();
        scrollPane2 = new widget.ScrollPane();
        Rencana = new widget.TextArea();
        ChkInput1 = new widget.CekBox();
        internalFrame2 = new widget.InternalFrame();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel18 = new widget.Label();
        kddok = new widget.TextBox();
        namadokter = new widget.TextBox();
        btnDokter = new widget.Button();
        Durasi = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        Dialiser = new widget.TextBox();
        jLabel36 = new widget.Label();
        jLabel39 = new widget.Label();
        BB_Pre = new widget.TextBox();
        jLabel41 = new widget.Label();
        jLabel20 = new widget.Label();
        kdpetugas = new widget.TextBox();
        namapetugas = new widget.TextBox();
        btnPetugas2 = new widget.Button();
        jLabel43 = new widget.Label();
        BB_Lalu = new widget.TextBox();
        jLabel44 = new widget.Label();
        jLabel46 = new widget.Label();
        BB_Naik = new widget.TextBox();
        jLabel48 = new widget.Label();
        BB_Post = new widget.TextBox();
        jLabel103 = new widget.Label();
        jLabel136 = new widget.Label();
        jLabel137 = new widget.Label();
        StatusDialiser = new widget.ComboBox();
        jLabel138 = new widget.Label();
        Jadwal = new widget.TextBox();
        Vaskuler = new widget.TextBox();
        jLabel139 = new widget.Label();
        jLabel140 = new widget.Label();
        jLabel141 = new widget.Label();
        Hari = new widget.TextBox();
        ChkInput = new widget.CekBox();
        Scroll = new widget.ScrollPane();
        tbDataHD = new widget.Table();
        internalFrame5 = new widget.InternalFrame();
        Scroll3 = new widget.ScrollPane();
        tbLaporanHD = new widget.Table();
        PanelInput3 = new javax.swing.JPanel();
        FormInput3 = new widget.PanelBiasa();
        UR = new widget.TextBox();
        Time = new widget.TextBox();
        Natrium = new widget.TextBox();
        QB = new widget.TextBox();
        QD = new widget.TextBox();
        jLabel81 = new widget.Label();
        jLabel84 = new widget.Label();
        ResepHD = new widget.ComboBox();
        jLabel104 = new widget.Label();
        jLabel53 = new widget.Label();
        jLabel82 = new widget.Label();
        jLabel142 = new widget.Label();
        jLabel143 = new widget.Label();
        jLabel144 = new widget.Label();
        jLabel145 = new widget.Label();
        jLabel146 = new widget.Label();
        jLabel147 = new widget.Label();
        jLabel148 = new widget.Label();
        jLabel149 = new widget.Label();
        jLabel85 = new widget.Label();
        jCheckBox2 = new javax.swing.JCheckBox();
        UG = new widget.TextBox();
        jLabel89 = new widget.Label();
        jCheckBox3 = new javax.swing.JCheckBox();
        Ultrafiltration = new javax.swing.JCheckBox();
        Temperature = new widget.TextBox();
        jLabel150 = new widget.Label();
        jLabel90 = new widget.Label();
        Asetat = new javax.swing.JCheckBox();
        Bicarbonat = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        DialiserLain = new widget.TextBox();
        Intermitten = new widget.TextBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        Conductivity = new widget.TextBox();
        LMWH = new widget.TextBox();
        DosisSirkulasi = new widget.TextBox();
        DosisAwal = new widget.TextBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        TanpaHeparin = new widget.TextBox();
        Continous = new widget.TextBox();
        jLabel2 = new javax.swing.JLabel();
        ProgramBilas = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Poly = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        UFHCheck = new javax.swing.JCheckBox();
        UFH = new widget.TextBox();
        jLabel11 = new javax.swing.JLabel();
        ChkInput3 = new widget.CekBox();
        internalFrame3 = new widget.InternalFrame();
        Scroll2 = new widget.ScrollPane();
        tbMonitoringHD = new widget.Table();
        PanelInput2 = new javax.swing.JPanel();
        FormInput2 = new widget.PanelBiasa();
        jLabel14 = new widget.Label();
        jLabel57 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel58 = new widget.Label();
        TD = new widget.TextBox();
        jLabel59 = new widget.Label();
        jLabel60 = new widget.Label();
        jLabel67 = new widget.Label();
        Qb = new widget.TextBox();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        UFR = new widget.TextBox();
        jLabel70 = new widget.Label();
        jLabel71 = new widget.Label();
        VP = new widget.TextBox();
        jLabel72 = new widget.Label();
        jLabel73 = new widget.Label();
        TMP = new widget.TextBox();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        Conductivity2 = new widget.TextBox();
        jLabel77 = new widget.Label();
        Lainlain = new widget.TextBox();
        Jam1 = new widget.ComboBox();
        Menit1 = new widget.ComboBox();
        Detik1 = new widget.ComboBox();
        jLabel78 = new widget.Label();
        Dextrose = new widget.TextBox();
        jLabel80 = new widget.Label();
        MakMin = new widget.TextBox();
        jLabel86 = new widget.Label();
        InputLain = new widget.TextBox();
        jLabel91 = new widget.Label();
        NACL = new widget.TextBox();
        jLabel92 = new widget.Label();
        TMP5 = new widget.TextBox();
        ChkInput2 = new widget.CekBox();
        internalFrame6 = new widget.InternalFrame();
        Scroll4 = new widget.ScrollPane();
        tbLayananHD = new widget.Table();
        PanelInput4 = new javax.swing.JPanel();
        FormInput4 = new widget.PanelBiasa();
        jLabel88 = new widget.Label();
        finger = new javax.swing.JCheckBox();
        tindakan = new javax.swing.JCheckBox();
        PilihanReuse = new javax.swing.JCheckBox();
        Keperawatan = new javax.swing.JCheckBox();
        Edukasi = new javax.swing.JCheckBox();
        Lain = new javax.swing.JCheckBox();
        KeteranganLain = new widget.TextBox();
        jLabel66 = new widget.Label();
        jLabel76 = new widget.Label();
        Dialiser2 = new widget.TextBox();
        Reuse = new widget.TextBox();
        KeteranganEdukasi = new widget.TextBox();
        ChkInput4 = new widget.CekBox();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput6 = new javax.swing.JPanel();
        FormInput6 = new widget.PanelBiasa();
        TglLahir = new widget.TextBox();
        jLabel65 = new widget.Label();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TNoRw = new widget.TextBox();
        jLabel4 = new widget.Label();
        jLabel16 = new widget.Label();
        Tanggal = new widget.Tanggal();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        ChkKejadian = new widget.CekBox();

        jPopupMenu2.setName("jPopupMenu2"); // NOI18N

        MnCetakObservasiIntradialitik.setBackground(new java.awt.Color(255, 255, 254));
        MnCetakObservasiIntradialitik.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCetakObservasiIntradialitik.setForeground(new java.awt.Color(50, 50, 50));
        MnCetakObservasiIntradialitik.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCetakObservasiIntradialitik.setText("Cetak Observasi");
        MnCetakObservasiIntradialitik.setName("MnCetakObservasiIntradialitik"); // NOI18N
        MnCetakObservasiIntradialitik.setPreferredSize(new java.awt.Dimension(230, 26));
        MnCetakObservasiIntradialitik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCetakObservasiIntradialitikActionPerformed(evt);
            }
        });
        jPopupMenu2.add(MnCetakObservasiIntradialitik);

        JK.setHighlighter(null);
        JK.setName("JK"); // NOI18N

        Umur.setHighlighter(null);
        Umur.setName("Umur"); // NOI18N

        TanggalRegistrasi.setHighlighter(null);
        TanggalRegistrasi.setName("TanggalRegistrasi"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ RM Hemodialisa ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.setPreferredSize(new java.awt.Dimension(454, 530));

        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setLayout(new java.awt.BorderLayout());

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(452, 200));

        tbRencanaHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbRencanaHD.setName("tbRencanaHD"); // NOI18N
        tbRencanaHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRencanaHDMouseClicked(evt);
            }
        });
        tbRencanaHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbRencanaHDKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbRencanaHD);

        internalFrame4.add(Scroll1, java.awt.BorderLayout.CENTER);

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(192, 160));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput1.setBackground(new java.awt.Color(250, 255, 245));
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInput1.setLayout(null);

        jLabel23.setText("Dokter P.J. :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput1.add(jLabel23);
        jLabel23.setBounds(200, 10, 70, 23);

        kddok1.setEditable(false);
        kddok1.setHighlighter(null);
        kddok1.setName("kddok1"); // NOI18N
        kddok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kddok1KeyPressed(evt);
            }
        });
        FormInput1.add(kddok1);
        kddok1.setBounds(280, 10, 110, 23);

        namadokter1.setEditable(false);
        namadokter1.setName("namadokter1"); // NOI18N
        FormInput1.add(namadokter1);
        namadokter1.setBounds(395, 10, 220, 23);

        btnDokter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter1.setMnemonic('2');
        btnDokter1.setToolTipText("ALt+2");
        btnDokter1.setName("btnDokter1"); // NOI18N
        btnDokter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokter1ActionPerformed(evt);
            }
        });
        btnDokter1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokter1KeyPressed(evt);
            }
        });
        FormInput1.add(btnDokter1);
        btnDokter1.setBounds(620, 10, 20, 23);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Rencana HD :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput1.add(jLabel45);
        jLabel45.setBounds(20, 40, 70, 23);

        jLabel47.setText("Tanggal  :");
        jLabel47.setName("jLabel47"); // NOI18N
        jLabel47.setVerifyInputWhenFocusTarget(false);
        FormInput1.add(jLabel47);
        jLabel47.setBounds(40, 10, 50, 23);

        Tanggal2.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2026" }));
        Tanggal2.setDisplayFormat("dd-MM-yyyy");
        Tanggal2.setName("Tanggal2"); // NOI18N
        Tanggal2.setOpaque(false);
        Tanggal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tanggal2KeyPressed(evt);
            }
        });
        FormInput1.add(Tanggal2);
        Tanggal2.setBounds(100, 10, 100, 23);

        jLabel49.setText("Poliklinik :");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput1.add(jLabel49);
        jLabel49.setBounds(30, 100, 60, 23);

        jLabel50.setName("jLabel50"); // NOI18N
        FormInput1.add(jLabel50);
        jLabel50.setBounds(390, 140, 70, 23);

        kdpoli.setEditable(false);
        kdpoli.setHighlighter(null);
        kdpoli.setName("kdpoli"); // NOI18N
        kdpoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdpoliKeyPressed(evt);
            }
        });
        FormInput1.add(kdpoli);
        kdpoli.setBounds(100, 100, 94, 23);

        namapoli.setEditable(false);
        namapoli.setName("namapoli"); // NOI18N
        FormInput1.add(namapoli);
        namapoli.setBounds(200, 100, 185, 23);

        btnPoli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPoli.setMnemonic('2');
        btnPoli.setToolTipText("ALt+2");
        btnPoli.setName("btnPoli"); // NOI18N
        btnPoli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPoliActionPerformed(evt);
            }
        });
        btnPoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPoliKeyPressed(evt);
            }
        });
        FormInput1.add(btnPoli);
        btnPoli.setBounds(390, 100, 20, 23);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        Rencana.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Rencana.setColumns(20);
        Rencana.setRows(5);
        Rencana.setName("Rencana"); // NOI18N
        Rencana.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RencanaKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(Rencana);

        FormInput1.add(scrollPane2);
        scrollPane2.setBounds(100, 40, 610, 50);

        PanelInput1.add(FormInput1, java.awt.BorderLayout.CENTER);

        ChkInput1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setMnemonic('I');
        ChkInput1.setText(".: Input Data");
        ChkInput1.setToolTipText("Alt+I");
        ChkInput1.setBorderPainted(true);
        ChkInput1.setBorderPaintedFlat(true);
        ChkInput1.setFocusable(false);
        ChkInput1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput1.setName("ChkInput1"); // NOI18N
        ChkInput1.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput1ActionPerformed(evt);
            }
        });
        PanelInput1.add(ChkInput1, java.awt.BorderLayout.PAGE_END);

        internalFrame4.add(PanelInput1, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Perencanaan HD", internalFrame4);

        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout());

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 160));
        PanelInput.setLayout(new java.awt.BorderLayout());

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 140));
        FormInput.setLayout(null);

        jLabel18.setText("Dokter :");
        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N
        FormInput.add(jLabel18);
        jLabel18.setBounds(10, 10, 50, 23);

        kddok.setEditable(false);
        kddok.setHighlighter(null);
        kddok.setName("kddok"); // NOI18N
        kddok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kddokKeyPressed(evt);
            }
        });
        FormInput.add(kddok);
        kddok.setBounds(70, 10, 94, 23);

        namadokter.setEditable(false);
        namadokter.setName("namadokter"); // NOI18N
        FormInput.add(namadokter);
        namadokter.setBounds(170, 10, 185, 23);

        btnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter.setMnemonic('2');
        btnDokter.setToolTipText("ALt+2");
        btnDokter.setName("btnDokter"); // NOI18N
        btnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokterActionPerformed(evt);
            }
        });
        btnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokterKeyPressed(evt);
            }
        });
        FormInput.add(btnDokter);
        btnDokter.setBounds(360, 10, 20, 23);

        Durasi.setHighlighter(null);
        Durasi.setName("Durasi"); // NOI18N
        Durasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DurasiKeyPressed(evt);
            }
        });
        FormInput.add(Durasi);
        Durasi.setBounds(80, 40, 40, 23);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Durasi HD :");
        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(10, 40, 70, 23);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Jam");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(130, 40, 30, 23);

        Dialiser.setHighlighter(null);
        Dialiser.setName("Dialiser"); // NOI18N
        Dialiser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DialiserKeyPressed(evt);
            }
        });
        FormInput.add(Dialiser);
        Dialiser.setBounds(400, 70, 141, 23);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setText("Status Dialiser :");
        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(10, 70, 90, 23);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText("kg");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(300, 40, 20, 23);

        BB_Pre.setHighlighter(null);
        BB_Pre.setName("BB_Pre"); // NOI18N
        BB_Pre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BB_PreKeyPressed(evt);
            }
        });
        FormInput.add(BB_Pre);
        BB_Pre.setBounds(250, 40, 40, 23);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setText("BB Pre HD :");
        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput.add(jLabel41);
        jLabel41.setBounds(180, 40, 70, 23);

        jLabel20.setText("Petugas : ");
        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput.add(jLabel20);
        jLabel20.setBounds(410, 10, 60, 23);

        kdpetugas.setEditable(false);
        kdpetugas.setHighlighter(null);
        kdpetugas.setName("kdpetugas"); // NOI18N
        kdpetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdpetugasKeyPressed(evt);
            }
        });
        FormInput.add(kdpetugas);
        kdpetugas.setBounds(480, 10, 94, 23);

        namapetugas.setEditable(false);
        namapetugas.setName("namapetugas"); // NOI18N
        FormInput.add(namapetugas);
        namapetugas.setBounds(580, 10, 200, 23);

        btnPetugas2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnPetugas2.setMnemonic('2');
        btnPetugas2.setToolTipText("ALt+2");
        btnPetugas2.setName("btnPetugas2"); // NOI18N
        btnPetugas2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPetugas2ActionPerformed(evt);
            }
        });
        btnPetugas2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnPetugas2KeyPressed(evt);
            }
        });
        FormInput.add(btnPetugas2);
        btnPetugas2.setBounds(780, 10, 28, 23);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel43.setText("BB yang lalu :");
        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput.add(jLabel43);
        jLabel43.setBounds(330, 40, 80, 23);

        BB_Lalu.setHighlighter(null);
        BB_Lalu.setName("BB_Lalu"); // NOI18N
        BB_Lalu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BB_LaluKeyPressed(evt);
            }
        });
        FormInput.add(BB_Lalu);
        BB_Lalu.setBounds(415, 40, 40, 23);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("kg");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput.add(jLabel44);
        jLabel44.setBounds(460, 40, 20, 23);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("Kenaikan BB :");
        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput.add(jLabel46);
        jLabel46.setBounds(490, 40, 90, 23);

        BB_Naik.setHighlighter(null);
        BB_Naik.setName("BB_Naik"); // NOI18N
        BB_Naik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BB_NaikKeyPressed(evt);
            }
        });
        FormInput.add(BB_Naik);
        BB_Naik.setBounds(575, 40, 40, 23);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("kg");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput.add(jLabel48);
        jLabel48.setBounds(620, 40, 20, 23);

        BB_Post.setHighlighter(null);
        BB_Post.setName("BB_Post"); // NOI18N
        BB_Post.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BB_PostKeyPressed(evt);
            }
        });
        FormInput.add(BB_Post);
        BB_Post.setBounds(740, 40, 40, 23);

        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel103.setText("kg");
        jLabel103.setName("jLabel103"); // NOI18N
        FormInput.add(jLabel103);
        jLabel103.setBounds(785, 40, 20, 23);

        jLabel136.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel136.setText("BB Post HD :");
        jLabel136.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel136.setName("jLabel136"); // NOI18N
        FormInput.add(jLabel136);
        jLabel136.setBounds(660, 40, 80, 23);

        jLabel137.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel137.setText("Penggunaan Dialiser :");
        jLabel137.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel137.setName("jLabel137"); // NOI18N
        FormInput.add(jLabel137);
        jLabel137.setBounds(270, 70, 130, 23);

        StatusDialiser.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Single Use", "Reuse" }));
        StatusDialiser.setName("StatusDialiser"); // NOI18N
        FormInput.add(StatusDialiser);
        StatusDialiser.setBounds(110, 70, 140, 20);

        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel138.setText("x/minggu");
        jLabel138.setName("jLabel138"); // NOI18N
        FormInput.add(jLabel138);
        jLabel138.setBounds(250, 100, 50, 23);

        Jadwal.setHighlighter(null);
        Jadwal.setName("Jadwal"); // NOI18N
        Jadwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JadwalKeyPressed(evt);
            }
        });
        FormInput.add(Jadwal);
        Jadwal.setBounds(100, 100, 141, 23);

        Vaskuler.setHighlighter(null);
        Vaskuler.setName("Vaskuler"); // NOI18N
        Vaskuler.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VaskulerKeyPressed(evt);
            }
        });
        FormInput.add(Vaskuler);
        Vaskuler.setBounds(660, 70, 141, 23);

        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel139.setText("Akses Vaskuler :");
        jLabel139.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel139.setName("jLabel139"); // NOI18N
        FormInput.add(jLabel139);
        jLabel139.setBounds(560, 70, 100, 23);

        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel140.setText("Jadwal HD :");
        jLabel140.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel140.setName("jLabel140"); // NOI18N
        FormInput.add(jLabel140);
        jLabel140.setBounds(20, 100, 70, 23);

        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel141.setText("Hari :");
        jLabel141.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel141.setName("jLabel141"); // NOI18N
        FormInput.add(jLabel141);
        jLabel141.setBounds(330, 100, 40, 23);

        Hari.setHighlighter(null);
        Hari.setName("Hari"); // NOI18N
        Hari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                HariKeyPressed(evt);
            }
        });
        FormInput.add(Hari);
        Hari.setBounds(370, 100, 180, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.PAGE_START);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        internalFrame2.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 150));

        tbDataHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbDataHD.setComponentPopupMenu(jPopupMenu2);
        tbDataHD.setName("tbDataHD"); // NOI18N
        tbDataHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataHDMouseClicked(evt);
            }
        });
        tbDataHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDataHDKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbDataHD);

        internalFrame2.add(Scroll, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Data Hemodialisa", internalFrame2);

        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.BorderLayout());

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);
        Scroll3.setPreferredSize(new java.awt.Dimension(452, 200));
        Scroll3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Scroll3MouseClicked(evt);
            }
        });

        tbLaporanHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbLaporanHD.setName("tbLaporanHD"); // NOI18N
        tbLaporanHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLaporanHDMouseClicked(evt);
            }
        });
        tbLaporanHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbLaporanHDKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tbLaporanHD);

        internalFrame5.add(Scroll3, java.awt.BorderLayout.CENTER);

        PanelInput3.setName("PanelInput3"); // NOI18N
        PanelInput3.setOpaque(false);
        PanelInput3.setPreferredSize(new java.awt.Dimension(192, 280));
        PanelInput3.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput3.setBackground(new java.awt.Color(250, 255, 245));
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(100, 270));
        FormInput3.setLayout(null);

        UR.setName("UR"); // NOI18N
        FormInput3.add(UR);
        UR.setBounds(133, 177, 90, 24);

        Time.setHighlighter(null);
        Time.setName("Time"); // NOI18N
        Time.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TimeKeyPressed(evt);
            }
        });
        FormInput3.add(Time);
        Time.setBounds(80, 57, 90, 24);

        Natrium.setHighlighter(null);
        Natrium.setName("Natrium"); // NOI18N
        Natrium.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NatriumKeyPressed(evt);
            }
        });
        FormInput3.add(Natrium);
        Natrium.setBounds(383, 96, 130, 24);

        QB.setHighlighter(null);
        QB.setName("QB"); // NOI18N
        QB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QBKeyPressed(evt);
            }
        });
        FormInput3.add(QB);
        QB.setBounds(50, 87, 120, 24);

        QD.setHighlighter(null);
        QD.setName("QD"); // NOI18N
        QD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QDKeyPressed(evt);
            }
        });
        FormInput3.add(QD);
        QD.setBounds(50, 117, 120, 24);

        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel81.setText("Dialisat :");
        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel81.setName("jLabel81"); // NOI18N
        jLabel81.setVerifyInputWhenFocusTarget(false);
        FormInput3.add(jLabel81);
        jLabel81.setBounds(310, 150, 120, 14);

        jLabel84.setName("jLabel84"); // NOI18N
        FormInput3.add(jLabel84);
        jLabel84.setBounds(520, 440, 70, 23);

        ResepHD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rutin / Travelling HD", "HD Inisiasi", "Akut", "Pre-OP", "SLED" }));
        ResepHD.setName("ResepHD"); // NOI18N
        FormInput3.add(ResepHD);
        ResepHD.setBounds(20, 30, 140, 20);

        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setText("Heparinisasi :");
        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel104.setName("jLabel104"); // NOI18N
        FormInput3.add(jLabel104);
        jLabel104.setBounds(710, 10, 90, 23);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setText("Ultrafiltration Rate :");
        jLabel53.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput3.add(jLabel53);
        jLabel53.setBounds(20, 180, 120, 14);

        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel82.setText("liter/Jam");
        jLabel82.setName("jLabel82"); // NOI18N
        FormInput3.add(jLabel82);
        jLabel82.setBounds(230, 180, 50, 14);

        jLabel142.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel142.setText("QB :");
        jLabel142.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel142.setName("jLabel142"); // NOI18N
        FormInput3.add(jLabel142);
        jLabel142.setBounds(20, 90, 60, 14);

        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel143.setText("QD :");
        jLabel143.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel143.setName("jLabel143"); // NOI18N
        FormInput3.add(jLabel143);
        jLabel143.setBounds(20, 120, 60, 14);

        jLabel144.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel144.setText("Ultrafiltration Goal :");
        jLabel144.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel144.setName("jLabel144"); // NOI18N
        FormInput3.add(jLabel144);
        jLabel144.setBounds(20, 150, 120, 14);

        jLabel145.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel145.setText("Time HD :");
        jLabel145.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel145.setName("jLabel145"); // NOI18N
        FormInput3.add(jLabel145);
        jLabel145.setBounds(20, 60, 60, 14);

        jLabel146.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel146.setText("Jam");
        jLabel146.setName("jLabel146"); // NOI18N
        FormInput3.add(jLabel146);
        jLabel146.setBounds(180, 60, 30, 14);

        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel147.setText("mmol/liter");
        jLabel147.setName("jLabel147"); // NOI18N
        FormInput3.add(jLabel147);
        jLabel147.setBounds(520, 100, 60, 14);

        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel148.setText("ml/mnt");
        jLabel148.setName("jLabel148"); // NOI18N
        FormInput3.add(jLabel148);
        jLabel148.setBounds(180, 120, 40, 14);

        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel149.setText("liter");
        jLabel149.setName("jLabel149"); // NOI18N
        FormInput3.add(jLabel149);
        jLabel149.setBounds(230, 150, 30, 14);

        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel85.setText("Resep HD :");
        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel85.setName("jLabel85"); // NOI18N
        jLabel85.setVerifyInputWhenFocusTarget(false);
        FormInput3.add(jLabel85);
        jLabel85.setBounds(20, 10, 70, 14);

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox2.setText("Lainnya :");
        jCheckBox2.setName("jCheckBox2"); // NOI18N
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        FormInput3.add(jCheckBox2);
        jCheckBox2.setBounds(310, 50, 70, 19);

        UG.setHighlighter(null);
        UG.setName("UG"); // NOI18N
        UG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UGKeyPressed(evt);
            }
        });
        FormInput3.add(UG);
        UG.setBounds(133, 147, 90, 24);

        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel89.setText("Jenis Dialiser :");
        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel89.setName("jLabel89"); // NOI18N
        jLabel89.setVerifyInputWhenFocusTarget(false);
        FormInput3.add(jLabel89);
        jLabel89.setBounds(310, 10, 90, 14);

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox3.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox3.setText("Natrium :");
        jCheckBox3.setName("jCheckBox3"); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        FormInput3.add(jCheckBox3);
        jCheckBox3.setBounds(310, 97, 80, 19);

        Ultrafiltration.setBackground(new java.awt.Color(255, 255, 255));
        Ultrafiltration.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Ultrafiltration.setForeground(new java.awt.Color(50, 50, 50));
        Ultrafiltration.setText("Ultrafiltration ( Rate Otomatis dari Mesin HD )");
        Ultrafiltration.setName("Ultrafiltration"); // NOI18N
        FormInput3.add(Ultrafiltration);
        Ultrafiltration.setBounds(310, 122, 270, 19);

        Temperature.setHighlighter(null);
        Temperature.setName("Temperature"); // NOI18N
        Temperature.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TemperatureKeyPressed(evt);
            }
        });
        FormInput3.add(Temperature);
        Temperature.setBounds(410, 220, 90, 24);

        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel150.setText("ml/mnt");
        jLabel150.setName("jLabel150"); // NOI18N
        FormInput3.add(jLabel150);
        jLabel150.setBounds(180, 90, 40, 14);

        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel90.setText("Program Profiling :");
        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel90.setName("jLabel90"); // NOI18N
        jLabel90.setVerifyInputWhenFocusTarget(false);
        FormInput3.add(jLabel90);
        jLabel90.setBounds(310, 80, 120, 14);

        Asetat.setBackground(new java.awt.Color(255, 255, 255));
        Asetat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Asetat.setForeground(new java.awt.Color(50, 50, 50));
        Asetat.setText("Asetat");
        Asetat.setName("Asetat"); // NOI18N
        FormInput3.add(Asetat);
        Asetat.setBounds(310, 166, 70, 19);

        Bicarbonat.setBackground(new java.awt.Color(255, 255, 255));
        Bicarbonat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Bicarbonat.setForeground(new java.awt.Color(50, 50, 50));
        Bicarbonat.setText("Bicarbonat");
        Bicarbonat.setName("Bicarbonat"); // NOI18N
        FormInput3.add(Bicarbonat);
        Bicarbonat.setBounds(400, 166, 90, 19);

        jCheckBox7.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox7.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox7.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox7.setText("Conductivity :");
        jCheckBox7.setName("jCheckBox7"); // NOI18N
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });
        FormInput3.add(jCheckBox7);
        jCheckBox7.setBounds(310, 190, 100, 19);

        jCheckBox8.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox8.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox8.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox8.setText("Temperature :");
        jCheckBox8.setName("jCheckBox8"); // NOI18N
        FormInput3.add(jCheckBox8);
        jCheckBox8.setBounds(310, 222, 100, 19);

        DialiserLain.setHighlighter(null);
        DialiserLain.setName("DialiserLain"); // NOI18N
        DialiserLain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DialiserLainKeyPressed(evt);
            }
        });
        FormInput3.add(DialiserLain);
        DialiserLain.setBounds(382, 48, 260, 24);

        Intermitten.setHighlighter(null);
        Intermitten.setName("Intermitten"); // NOI18N
        Intermitten.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntermittenKeyPressed(evt);
            }
        });
        FormInput3.add(Intermitten);
        Intermitten.setBounds(820, 200, 90, 24);

        jCheckBox9.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox9.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox9.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox9.setText("Dosis Sirkulasi :");
        jCheckBox9.setName("jCheckBox9"); // NOI18N
        FormInput3.add(jCheckBox9);
        jCheckBox9.setBounds(710, 30, 110, 19);

        jCheckBox10.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox10.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox10.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox10.setText("LMWH :");
        jCheckBox10.setName("jCheckBox10"); // NOI18N
        FormInput3.add(jCheckBox10);
        jCheckBox10.setBounds(710, 60, 70, 19);

        jCheckBox11.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox11.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox11.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox11.setText("Dosis Awal :");
        jCheckBox11.setName("jCheckBox11"); // NOI18N
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });
        FormInput3.add(jCheckBox11);
        jCheckBox11.setBounds(710, 90, 100, 19);

        Conductivity.setHighlighter(null);
        Conductivity.setName("Conductivity"); // NOI18N
        Conductivity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ConductivityKeyPressed(evt);
            }
        });
        FormInput3.add(Conductivity);
        Conductivity.setBounds(410, 190, 90, 24);

        LMWH.setHighlighter(null);
        LMWH.setName("LMWH"); // NOI18N
        LMWH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LMWHKeyPressed(evt);
            }
        });
        FormInput3.add(LMWH);
        LMWH.setBounds(820, 60, 90, 24);

        DosisSirkulasi.setHighlighter(null);
        DosisSirkulasi.setName("DosisSirkulasi"); // NOI18N
        DosisSirkulasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DosisSirkulasiKeyPressed(evt);
            }
        });
        FormInput3.add(DosisSirkulasi);
        DosisSirkulasi.setBounds(820, 29, 90, 24);

        DosisAwal.setHighlighter(null);
        DosisAwal.setName("DosisAwal"); // NOI18N
        DosisAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DosisAwalKeyPressed(evt);
            }
        });
        FormInput3.add(DosisAwal);
        DosisAwal.setBounds(820, 90, 90, 24);

        jCheckBox12.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox12.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox12.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox12.setText("Tanpa Heparin :");
        jCheckBox12.setName("jCheckBox12"); // NOI18N
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });
        FormInput3.add(jCheckBox12);
        jCheckBox12.setBounds(710, 120, 110, 19);

        jCheckBox13.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox13.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jCheckBox13.setForeground(new java.awt.Color(50, 50, 50));
        jCheckBox13.setText("Dosis Maintenance :");
        jCheckBox13.setName("jCheckBox13"); // NOI18N
        FormInput3.add(jCheckBox13);
        jCheckBox13.setBounds(710, 150, 140, 19);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Intermitten :");
        jLabel1.setName("jLabel1"); // NOI18N
        FormInput3.add(jLabel1);
        jLabel1.setBounds(730, 203, 70, 14);

        TanpaHeparin.setHighlighter(null);
        TanpaHeparin.setName("TanpaHeparin"); // NOI18N
        TanpaHeparin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanpaHeparinKeyPressed(evt);
            }
        });
        FormInput3.add(TanpaHeparin);
        TanpaHeparin.setBounds(820, 120, 90, 24);

        Continous.setHighlighter(null);
        Continous.setName("Continous"); // NOI18N
        Continous.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ContinousKeyPressed(evt);
            }
        });
        FormInput3.add(Continous);
        Continous.setBounds(820, 170, 90, 24);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("lu/jam");
        jLabel2.setName("jLabel2"); // NOI18N
        FormInput3.add(jLabel2);
        jLabel2.setBounds(920, 203, 40, 14);

        ProgramBilas.setBackground(new java.awt.Color(255, 255, 255));
        ProgramBilas.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ProgramBilas.setForeground(new java.awt.Color(50, 50, 50));
        ProgramBilas.setText("Program Bilas Nac:l 0.9 % 100 cc/jam/30 mnt");
        ProgramBilas.setName("ProgramBilas"); // NOI18N
        FormInput3.add(ProgramBilas);
        ProgramBilas.setBounds(710, 230, 270, 19);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Continous :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormInput3.add(jLabel3);
        jLabel3.setBounds(730, 173, 70, 14);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("lu");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput3.add(jLabel5);
        jLabel5.setBounds(920, 65, 20, 14);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("lu");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput3.add(jLabel8);
        jLabel8.setBounds(920, 92, 20, 14);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("lu/jam, atau");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput3.add(jLabel9);
        jLabel9.setBounds(920, 172, 70, 14);

        Poly.setBackground(new java.awt.Color(255, 255, 255));
        Poly.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Poly.setForeground(new java.awt.Color(50, 50, 50));
        Poly.setText("Polyetehersulfone Hollow Fiber Hemodializer OCI-HD 180");
        Poly.setName("Poly"); // NOI18N
        FormInput3.add(Poly);
        Poly.setBounds(310, 26, 350, 19);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("lu");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput3.add(jLabel10);
        jLabel10.setBounds(920, 32, 20, 14);

        UFHCheck.setBackground(new java.awt.Color(255, 255, 255));
        UFHCheck.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        UFHCheck.setForeground(new java.awt.Color(50, 50, 50));
        UFHCheck.setText("UFH :");
        UFHCheck.setName("UFHCheck"); // NOI18N
        UFHCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UFHCheckActionPerformed(evt);
            }
        });
        FormInput3.add(UFHCheck);
        UFHCheck.setBounds(1020, 30, 60, 19);

        UFH.setHighlighter(null);
        UFH.setName("UFH"); // NOI18N
        UFH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UFHKeyPressed(evt);
            }
        });
        FormInput3.add(UFH);
        UFH.setBounds(1080, 26, 90, 24);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("lu");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput3.add(jLabel11);
        jLabel11.setBounds(1180, 30, 20, 14);

        PanelInput3.add(FormInput3, java.awt.BorderLayout.CENTER);

        ChkInput3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput3.setMnemonic('I');
        ChkInput3.setText(".: Input Data");
        ChkInput3.setToolTipText("Alt+I");
        ChkInput3.setBorderPainted(true);
        ChkInput3.setBorderPaintedFlat(true);
        ChkInput3.setFocusable(false);
        ChkInput3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput3.setName("ChkInput3"); // NOI18N
        ChkInput3.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput3.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput3ActionPerformed(evt);
            }
        });
        PanelInput3.add(ChkInput3, java.awt.BorderLayout.PAGE_END);

        internalFrame5.add(PanelInput3, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Laporan Tindakan Hemodialisa", internalFrame5);

        internalFrame3.setName("internalFrame3"); // NOI18N
        internalFrame3.setLayout(new java.awt.BorderLayout());

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);
        Scroll2.setPreferredSize(new java.awt.Dimension(452, 200));

        tbMonitoringHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbMonitoringHD.setComponentPopupMenu(jPopupMenu2);
        tbMonitoringHD.setInheritsPopupMenu(true);
        tbMonitoringHD.setName("tbMonitoringHD"); // NOI18N
        tbMonitoringHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbMonitoringHDMouseClicked(evt);
            }
        });
        tbMonitoringHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbMonitoringHDKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbMonitoringHD);

        internalFrame3.add(Scroll2, java.awt.BorderLayout.CENTER);

        PanelInput2.setName("PanelInput2"); // NOI18N
        PanelInput2.setOpaque(false);
        PanelInput2.setPreferredSize(new java.awt.Dimension(192, 160));
        PanelInput2.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput2.setBackground(new java.awt.Color(250, 255, 245));
        FormInput2.setName("FormInput2"); // NOI18N
        FormInput2.setPreferredSize(new java.awt.Dimension(100, 140));
        FormInput2.setLayout(null);

        jLabel14.setText("Monitoring pada Jam :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput2.add(jLabel14);
        jLabel14.setBounds(20, 10, 110, 23);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setText("Suhu :");
        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput2.add(jLabel57);
        jLabel57.setBounds(20, 70, 40, 23);

        Suhu.setFocusTraversalPolicyProvider(true);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput2.add(Suhu);
        Suhu.setBounds(70, 70, 40, 23);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("TD :");
        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput2.add(jLabel58);
        jLabel58.setBounds(20, 40, 30, 23);

        TD.setFocusTraversalPolicyProvider(true);
        TD.setName("TD"); // NOI18N
        TD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TDKeyPressed(evt);
            }
        });
        FormInput2.add(TD);
        TD.setBounds(50, 40, 70, 20);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel59.setText("°C");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput2.add(jLabel59);
        jLabel59.setBounds(120, 70, 30, 23);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel60.setText("mmHg");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput2.add(jLabel60);
        jLabel60.setBounds(130, 40, 40, 20);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("QB :");
        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput2.add(jLabel67);
        jLabel67.setBounds(190, 40, 30, 23);

        Qb.setFocusTraversalPolicyProvider(true);
        Qb.setName("Qb"); // NOI18N
        Qb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QbKeyPressed(evt);
            }
        });
        FormInput2.add(Qb);
        Qb.setBounds(220, 40, 70, 23);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel68.setText("ml/menit");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput2.add(jLabel68);
        jLabel68.setBounds(300, 40, 50, 23);

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel69.setText("UFR :");
        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel69.setName("jLabel69"); // NOI18N
        FormInput2.add(jLabel69);
        jLabel69.setBounds(340, 70, 40, 23);

        UFR.setFocusTraversalPolicyProvider(true);
        UFR.setName("UFR"); // NOI18N
        UFR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UFRKeyPressed(evt);
            }
        });
        FormInput2.add(UFR);
        UFR.setBounds(380, 70, 70, 23);

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel70.setText("Lt/jam");
        jLabel70.setName("jLabel70"); // NOI18N
        FormInput2.add(jLabel70);
        jLabel70.setBounds(460, 70, 50, 23);

        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel71.setText("VP :");
        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput2.add(jLabel71);
        jLabel71.setBounds(370, 40, 30, 23);

        VP.setFocusTraversalPolicyProvider(true);
        VP.setName("VP"); // NOI18N
        VP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                VPKeyPressed(evt);
            }
        });
        FormInput2.add(VP);
        VP.setBounds(406, 40, 64, 23);

        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel72.setText("mmHg");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput2.add(jLabel72);
        jLabel72.setBounds(480, 40, 40, 23);

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel73.setText("TMP :");
        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel73.setName("jLabel73"); // NOI18N
        FormInput2.add(jLabel73);
        jLabel73.setBounds(550, 40, 40, 23);

        TMP.setFocusTraversalPolicyProvider(true);
        TMP.setName("TMP"); // NOI18N
        TMP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TMPKeyPressed(evt);
            }
        });
        FormInput2.add(TMP);
        TMP.setBounds(590, 40, 70, 23);

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel74.setText("mmHg");
        jLabel74.setName("jLabel74"); // NOI18N
        FormInput2.add(jLabel74);
        jLabel74.setBounds(670, 40, 40, 23);

        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel75.setText("Conductivity : ");
        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput2.add(jLabel75);
        jLabel75.setBounds(160, 70, 90, 23);

        Conductivity2.setFocusTraversalPolicyProvider(true);
        Conductivity2.setName("Conductivity2"); // NOI18N
        Conductivity2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Conductivity2KeyPressed(evt);
            }
        });
        FormInput2.add(Conductivity2);
        Conductivity2.setBounds(250, 70, 60, 23);

        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel77.setText("Lain-lain : ");
        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput2.add(jLabel77);
        jLabel77.setBounds(20, 100, 60, 23);

        Lainlain.setFocusTraversalPolicyProvider(true);
        Lainlain.setName("Lainlain"); // NOI18N
        Lainlain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LainlainKeyPressed(evt);
            }
        });
        FormInput2.add(Lainlain);
        Lainlain.setBounds(90, 100, 620, 23);

        Jam1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam1.setName("Jam1"); // NOI18N
        Jam1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Jam1KeyPressed(evt);
            }
        });
        FormInput2.add(Jam1);
        Jam1.setBounds(140, 10, 62, 23);

        Menit1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit1.setName("Menit1"); // NOI18N
        Menit1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Menit1KeyPressed(evt);
            }
        });
        FormInput2.add(Menit1);
        Menit1.setBounds(210, 10, 62, 23);

        Detik1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik1.setName("Detik1"); // NOI18N
        Detik1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Detik1KeyPressed(evt);
            }
        });
        FormInput2.add(Detik1);
        Detik1.setBounds(280, 10, 62, 23);

        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel78.setText("Dextrose 40% :");
        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput2.add(jLabel78);
        jLabel78.setBounds(780, 40, 90, 23);

        Dextrose.setFocusTraversalPolicyProvider(true);
        Dextrose.setName("Dextrose"); // NOI18N
        Dextrose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DextroseKeyPressed(evt);
            }
        });
        FormInput2.add(Dextrose);
        Dextrose.setBounds(880, 40, 70, 23);

        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel80.setText("Makan/Minum :");
        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel80.setName("jLabel80"); // NOI18N
        FormInput2.add(jLabel80);
        jLabel80.setBounds(780, 70, 90, 23);

        MakMin.setFocusTraversalPolicyProvider(true);
        MakMin.setName("MakMin"); // NOI18N
        MakMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MakMinKeyPressed(evt);
            }
        });
        FormInput2.add(MakMin);
        MakMin.setBounds(880, 70, 70, 23);

        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel86.setText("Lain-lain :");
        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel86.setName("jLabel86"); // NOI18N
        FormInput2.add(jLabel86);
        jLabel86.setBounds(780, 100, 90, 23);

        InputLain.setFocusTraversalPolicyProvider(true);
        InputLain.setName("InputLain"); // NOI18N
        InputLain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InputLainKeyPressed(evt);
            }
        });
        FormInput2.add(InputLain);
        InputLain.setBounds(880, 100, 70, 23);

        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel91.setText("NaCL 0.9% : ");
        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel91.setName("jLabel91"); // NOI18N
        FormInput2.add(jLabel91);
        jLabel91.setBounds(780, 10, 90, 23);

        NACL.setFocusTraversalPolicyProvider(true);
        NACL.setName("NACL"); // NOI18N
        NACL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NACLKeyPressed(evt);
            }
        });
        FormInput2.add(NACL);
        NACL.setBounds(880, 10, 70, 23);

        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel92.setText("UF Goal : ");
        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel92.setName("jLabel92"); // NOI18N
        FormInput2.add(jLabel92);
        jLabel92.setBounds(1000, 10, 60, 23);

        TMP5.setFocusTraversalPolicyProvider(true);
        TMP5.setName("TMP5"); // NOI18N
        TMP5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TMP5KeyPressed(evt);
            }
        });
        FormInput2.add(TMP5);
        TMP5.setBounds(1070, 10, 70, 23);

        PanelInput2.add(FormInput2, java.awt.BorderLayout.CENTER);

        ChkInput2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setMnemonic('I');
        ChkInput2.setText(".: Input Data");
        ChkInput2.setToolTipText("Alt+I");
        ChkInput2.setBorderPainted(true);
        ChkInput2.setBorderPaintedFlat(true);
        ChkInput2.setFocusable(false);
        ChkInput2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput2.setName("ChkInput2"); // NOI18N
        ChkInput2.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput2ActionPerformed(evt);
            }
        });
        PanelInput2.add(ChkInput2, java.awt.BorderLayout.PAGE_END);

        internalFrame3.add(PanelInput2, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Monitoring", internalFrame3);

        internalFrame6.setName("internalFrame6"); // NOI18N
        internalFrame6.setLayout(new java.awt.BorderLayout());

        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);
        Scroll4.setPreferredSize(new java.awt.Dimension(452, 200));

        tbLayananHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbLayananHD.setName("tbLayananHD"); // NOI18N
        tbLayananHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLayananHDMouseClicked(evt);
            }
        });
        tbLayananHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbLayananHDKeyPressed(evt);
            }
        });
        Scroll4.setViewportView(tbLayananHD);

        internalFrame6.add(Scroll4, java.awt.BorderLayout.CENTER);

        PanelInput4.setName("PanelInput4"); // NOI18N
        PanelInput4.setOpaque(false);
        PanelInput4.setPreferredSize(new java.awt.Dimension(192, 270));
        PanelInput4.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput4.setBackground(new java.awt.Color(250, 255, 245));
        FormInput4.setName("FormInput4"); // NOI18N
        FormInput4.setPreferredSize(new java.awt.Dimension(100, 250));
        FormInput4.setLayout(null);

        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel88.setText("LAYANAN DAN INFORMASI YANG DIBERIKAN");
        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel88.setName("jLabel88"); // NOI18N
        FormInput4.add(jLabel88);
        jLabel88.setBounds(20, 10, 430, 23);

        finger.setBackground(new java.awt.Color(255, 255, 255));
        finger.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        finger.setForeground(new java.awt.Color(50, 50, 50));
        finger.setText("Finger / Sidik Jari");
        finger.setName("finger"); // NOI18N
        FormInput4.add(finger);
        finger.setBounds(20, 40, 120, 19);

        tindakan.setBackground(new java.awt.Color(255, 255, 255));
        tindakan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        tindakan.setForeground(new java.awt.Color(50, 50, 50));
        tindakan.setText("Penjelasan Tindakan HD (Inform Consent) meliputi Informasi Diagnosa Medis, Indikasi tindakan, tata cara, Tujuan, Prosedur Resiko dan komplikasi, keberhasilan, Alternatif, dsb.");
        tindakan.setName("tindakan"); // NOI18N
        FormInput4.add(tindakan);
        tindakan.setBounds(20, 70, 900, 19);

        PilihanReuse.setBackground(new java.awt.Color(255, 255, 255));
        PilihanReuse.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        PilihanReuse.setForeground(new java.awt.Color(50, 50, 50));
        PilihanReuse.setText("Penjelasan Tindakan Reuse (Inform Consent) imana HD saat ini menggunakan Dialiser");
        PilihanReuse.setName("PilihanReuse"); // NOI18N
        FormInput4.add(PilihanReuse);
        PilihanReuse.setBounds(20, 100, 460, 19);

        Keperawatan.setBackground(new java.awt.Color(255, 255, 255));
        Keperawatan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Keperawatan.setForeground(new java.awt.Color(50, 50, 50));
        Keperawatan.setText("Layanan Keperawatan Pre HD, Intra HD dan Post HD beserta Pelayanan Pemeriksaan Dokter, Obat Kronis dan lainnya sesuai kebutuhan berdasarkan indikasi.");
        Keperawatan.setName("Keperawatan"); // NOI18N
        FormInput4.add(Keperawatan);
        Keperawatan.setBounds(20, 150, 810, 19);

        Edukasi.setBackground(new java.awt.Color(255, 255, 255));
        Edukasi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Edukasi.setForeground(new java.awt.Color(50, 50, 50));
        Edukasi.setText("Patient family Edukasi tentang : ");
        Edukasi.setName("Edukasi"); // NOI18N
        FormInput4.add(Edukasi);
        Edukasi.setBounds(20, 180, 190, 19);

        Lain.setBackground(new java.awt.Color(255, 255, 255));
        Lain.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        Lain.setForeground(new java.awt.Color(50, 50, 50));
        Lain.setText("Lain - lain :");
        Lain.setName("Lain"); // NOI18N
        FormInput4.add(Lain);
        Lain.setBounds(20, 210, 85, 19);

        KeteranganLain.setHighlighter(null);
        KeteranganLain.setName("KeteranganLain"); // NOI18N
        FormInput4.add(KeteranganLain);
        KeteranganLain.setBounds(110, 207, 710, 23);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel66.setText("HD selanjutnya menggunakan Dialiser Baru/Reuse ke");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput4.add(jLabel66);
        jLabel66.setBounds(40, 125, 270, 14);

        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel76.setText("kemudian setelah selesai pelayanan dialiser akan dilakukan reprocessing sterilisasi / dibuang.");
        jLabel76.setName("jLabel76"); // NOI18N
        FormInput4.add(jLabel76);
        jLabel76.setBounds(590, 100, 470, 20);

        Dialiser2.setHighlighter(null);
        Dialiser2.setName("Dialiser2"); // NOI18N
        FormInput4.add(Dialiser2);
        Dialiser2.setBounds(480, 97, 100, 23);

        Reuse.setHighlighter(null);
        Reuse.setName("Reuse"); // NOI18N
        FormInput4.add(Reuse);
        Reuse.setBounds(310, 120, 100, 23);

        KeteranganEdukasi.setHighlighter(null);
        KeteranganEdukasi.setName("KeteranganEdukasi"); // NOI18N
        FormInput4.add(KeteranganEdukasi);
        KeteranganEdukasi.setBounds(210, 176, 610, 24);

        PanelInput4.add(FormInput4, java.awt.BorderLayout.CENTER);

        ChkInput4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput4.setMnemonic('I');
        ChkInput4.setText(".: Input Data");
        ChkInput4.setToolTipText("Alt+I");
        ChkInput4.setBorderPainted(true);
        ChkInput4.setBorderPaintedFlat(true);
        ChkInput4.setFocusable(false);
        ChkInput4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput4.setName("ChkInput4"); // NOI18N
        ChkInput4.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput4.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInput4ActionPerformed(evt);
            }
        });
        PanelInput4.add(ChkInput4, java.awt.BorderLayout.PAGE_END);

        internalFrame6.add(PanelInput4, java.awt.BorderLayout.PAGE_START);

        TabRawat.addTab("Bukti Layanan HD", internalFrame6);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput6.setName("PanelInput6"); // NOI18N
        PanelInput6.setOpaque(false);
        PanelInput6.setPreferredSize(new java.awt.Dimension(192, 50));
        PanelInput6.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput6.setBackground(new java.awt.Color(250, 255, 245));
        FormInput6.setName("FormInput6"); // NOI18N
        FormInput6.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInput6.setLayout(null);

        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput6.add(TglLahir);
        TglLahir.setBounds(630, 10, 100, 23);

        jLabel65.setText("Tgl.Lahir :");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput6.add(jLabel65);
        jLabel65.setBounds(560, 10, 60, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput6.add(TPasien);
        TPasien.setBounds(336, 10, 210, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput6.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput6.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        jLabel4.setText("No.Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput6.add(jLabel4);
        jLabel4.setBounds(0, 10, 75, 23);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput6.add(jLabel16);
        jLabel16.setBounds(730, 10, 75, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-06-2026" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput6.add(Tanggal);
        Tanggal.setBounds(810, 10, 90, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput6.add(Jam);
        Jam.setBounds(900, 10, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput6.add(Menit);
        Menit.setBounds(960, 10, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput6.add(Detik);
        Detik.setBounds(1020, 10, 62, 23);

        ChkKejadian.setBorder(null);
        ChkKejadian.setSelected(true);
        ChkKejadian.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkKejadian.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkKejadian.setName("ChkKejadian"); // NOI18N
        FormInput6.add(ChkKejadian);
        ChkKejadian.setBounds(1090, 10, 23, 23);

        PanelInput6.add(FormInput6, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput6, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            isRawat();
        } else {
            Valid.pindah(evt, TCari, Tanggal);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt, TCari, BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (kdpoli.getText().trim().equals("") || namapoli.getText().trim().equals("")) {
                    Valid.textKosong(kdpoli, "Poli");
                } else if (kddok1.getText().trim().equals("") || namadokter1.getText().trim().equals("")) {
                    Valid.textKosong(kddok1, "Dokter");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + "") + " " + Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem()) == true) {
                            simpan();
                        }
                    }
                }
                break;
            case 1:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (kddok.getText().trim().equals("") || namadokter.getText().trim().equals("")) {
                    Valid.textKosong(kddok, "Dokter P.J");
                }  else {
                    simpan();
                }
                break;
            case 2:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else {
                    simpan();
                }
                break;
            case 3:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (TD.getText().trim().equals("")) {
                    Valid.textKosong(TD, "Tensi");
                } else if (Suhu.getText().trim().equals("")) {
                    Valid.textKosong(Suhu, "Suhu");
                } else if (Qb.getText().trim().equals("")) {
                    Valid.textKosong(Qb, "Qb");
                } else if (UFR.getText().trim().equals("")) {
                    Valid.textKosong(UFR, "Qd");
                } else if (VP.getText().trim().equals("")) {
                    Valid.textKosong(VP, "TV");
                } else if (TMP.getText().trim().equals("")) {
                    Valid.textKosong(TMP, "TMP");
                } else if (Conductivity2.getText().trim().equals("")) {
                    Valid.textKosong(Conductivity2, "Conductivity");
                } else {
                    simpan();
                }
                break;
            case 4:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else {
                    if (akses.getkode().equals("Admin Utama")) {
                        simpan();
                    } else {
                        if (TanggalRegistrasi.getText().equals("")) {
                            TanggalRegistrasi.setText(Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat=?", TNoRw.getText()));
                        }
                        if (Sequel.cekTanggalRegistrasi(TanggalRegistrasi.getText(), Valid.SetTgl(Tanggal.getSelectedItem() + "") + " " + Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem()) == true) {
                            simpan();
                        }
                    }
                }

                break;
            default:

        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        BtnSimpanActionPerformed(null);
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            emptTeks();
        } else {
            Valid.pindah(evt, BtnSimpan, BtnHapus);
        }
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (tbRencanaHD.getSelectedRow() != -1) {
                    if (Sequel.queryu2tf("delete from rencana_hd where tanggal=? and no_rawat=?", 2, new String[]{
                        tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 4).toString(), tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 0).toString()
                    }) == true) {
                        tabRencanaHD.removeRow(tbRencanaHD.getSelectedRow());
                        LCount.setText("" + tabRencanaHD.getRowCount());
                        emptTeks();
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                    }
                }
                break;
            case 1:
                if (tbDataHD.getSelectedRow() != -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (kddok.getText().equals(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 20).toString())) {
                            if (Sequel.cekTanggal48jam(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 5).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }
                break;
            case 2:
                hapus();
                break;
            case 3: //ini sudah
                if (tbMonitoringHD.getSelectedRow() != -1) {
                    if (akses.getkode().equals("Admin Utama")) {
                        hapus();
                    } else {
                        if (kddok.getText().equals(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 20).toString())) {
                            if (Sequel.cekTanggal48jam(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 5).toString(), Sequel.ambiltanggalsekarang()) == true) {
                                hapus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Hanya bisa dihapus oleh petugas yang bersangkutan..!!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                }

                break;
            case 4:
                hapus();
                break;
            default:
        }
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnHapusActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (kdpoli.getText().trim().equals("") || namapoli.getText().trim().equals("")) {
                    Valid.textKosong(kdpoli, "Poli");
                } else if (kddok1.getText().trim().equals("") || namadokter1.getText().trim().equals("")) {
                    Valid.textKosong(kddok1, "Dokter");
                } else {
                    Sequel.mengedit("rencana_hd", "no_rawat=?", "tanggal=?,rencana=?,kd_dokter=?,kd_poli=?", 5, new String[]{
                        Valid.SetTgl(Tanggal2.getSelectedItem() + ""),
                        Rencana.getText(), 
                        kddok1.getText(), 
                        kdpoli.getText(),
                        TNoRw.getText()
                    });
                    if (tabRencanaHD.getRowCount() != 0) {
                        tampilRencanaHD();
                    }
                    emptTeks();
                }
                break;
            case 1:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (kddok.getText().trim().equals("") || namadokter.getText().trim().equals("")) {
                    Valid.textKosong(kddok, "Dokter P.J");
                } else if (Durasi.getText().trim().equals("")) {
                    Valid.textKosong(Durasi, "Lama Instruksi");
                } else if (Dialiser.getText().trim().equals("")) {
                    Valid.textKosong(Dialiser, "Dialist");
                } else if (BB_Pre.getText().trim().equals("")) {
                    Valid.textKosong(BB_Pre, "Penarikan Cairan");
                } else {
                    if (tbDataHD.getSelectedRow() > -1) {
                        ganti();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                    }
                }
                break;
            case 2:
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else {                    
                    String poly = Poly.isSelected() ? "true" : "false";
                    String ultra = Ultrafiltration.isSelected() ? "true" : "false";
                    String asetat = Asetat.isSelected() ? "true" : "false";
                    String bicarbonat = Bicarbonat.isSelected() ? "true" : "false";
                    String program_bilas = ProgramBilas.isSelected() ? "true" : "false";
                
                    Sequel.mengedit("laporan_tindakan_hemodialisa", "no_rawat=?", ""
                            + "tanggal=?,"
                            + "resep_hd=?,"
                            + "time_hd=?,"
                            + "qb=?,"
                            + "qd=?,"
                            + "ug=?,"
                            + "ur=?,"
                            + "jenis_dial1=?,"
                            + "jenis_dial2=?,"
                            + "na_start=?,"
                            + "ultrafiltration=?,"
                            + "asetat=?,"
                            + "bicarbonat=?,"
                            + "conductivity=?,"
                            + "temp=?,"
                            + "dosis_sirkulasi=?,"
                            + "lmwh=?,"
                            + "dosis_awal=?,"
                            + "heparin=?,"
                            + "dosis_continous=?,"
                            + "dosis_intermitten=?,"
                            + "bilas=?,"
                            + "ufh=?", 24, new String[]{

                        Valid.SetTgl(Tanggal.getSelectedItem() + ""),
                        ResepHD.getSelectedItem().toString(), 
                        Time.getText(), 
                        QB.getText(),

                        QD.getText(),
                        UG.getText(), 
                        UR.getText(),
                        poly,
                        DialiserLain.getText(),

                        Natrium.getText(), 
                        ultra,
                        asetat,
                        bicarbonat,
                        Conductivity.getText(),

                        Temperature.getText(),
                        DosisSirkulasi.getText(),
                        LMWH.getText(),
                        DosisAwal.getText(),
                        TanpaHeparin.getText(),

                        Continous.getText(),
                        Intermitten.getText(),
                        program_bilas,
                        UFH.getText(),
                        TNoRw.getText()
                    });
                    if (tabLaporanHD.getRowCount() != 0) {
                        tabLaporanHD.removeRow(tbLaporanHD.getSelectedRow());
                        LCount.setText("" + tabLaporanHD.getRowCount());
                        tampilLaporanHD();
                    }
                    emptTeks();
                }
                break;
            case 3: //sudah
                if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
                    Valid.textKosong(TNoRw, "pasien");
                } else if (TD.getText().trim().equals("")) {
                    Valid.textKosong(TD, "Tensi");
                } else if (Suhu.getText().trim().equals("")) {
                    Valid.textKosong(Suhu, "Suhu");
                } else if (Qb.getText().trim().equals("")) {
                    Valid.textKosong(Qb, "Qb");
                } else if (UFR.getText().trim().equals("")) {
                    Valid.textKosong(UFR, "UFR");
                } else if (VP.getText().trim().equals("")) {
                    Valid.textKosong(VP, "VP");
                } else if (TMP.getText().trim().equals("")) {
                    Valid.textKosong(TMP, "TMP");
                } else if (Conductivity2.getText().trim().equals("")) {
                    Valid.textKosong(Conductivity2, "Conductivity");
                } else {
                    if (tbMonitoringHD.getSelectedRow() > -1) {
                        ganti();
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Silahkan anda pilih data terlebih dahulu..!!");
                    }
                }
                break;
            case 4:
                ganti();
                break;
            default:

        }


}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnEditActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnKeluarActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, TCari);
        }
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (tabMonitoringHD.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                } else if (tabMonitoringHD.getRowCount() != 0) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars", akses.getnamars());
                    param.put("alamatrs", akses.getalamatrs());
                    param.put("kotars", akses.getkabupatenrs());
                    param.put("propinsirs", akses.getpropinsirs());
                    param.put("kontakrs", akses.getkontakrs());
                    param.put("emailrs", akses.getemailrs());
                    param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
                    if (TCari.getText().equals("")) {
                        Valid.MyReportqry("rptDataHemodialisa.jasper", "report", "::[ Data Hemodialis ]::",
                                "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                                + "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "
                                + "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "
                                + "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "
                                + "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "
                                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                                + "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "
                                + "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' order by hemodialisa.tanggal ", param);
                    } else {
                        Valid.MyReportqry("rptDataHemodialisa.jasper", "report", "::[ Data Hemodialis ]::",
                                "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                                + "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "
                                + "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "
                                + "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "
                                + "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "
                                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                                + "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "
                                + "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and reg_periksa.no_rawat like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.no_rkm_medis like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.nm_pasien like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.akses like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.dialist like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.lain like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and dokter.nm_dokter like '%" + TCari.getText().trim() + "%' "
                                + "order by hemodialisa.tanggal ", param);
                    }
                }
                this.setCursor(Cursor.getDefaultCursor());
                break;
            case 1:
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (tabMonitoringHD.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                } else if (tabMonitoringHD.getRowCount() != 0) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars", akses.getnamars());
                    param.put("alamatrs", akses.getalamatrs());
                    param.put("kotars", akses.getkabupatenrs());
                    param.put("propinsirs", akses.getpropinsirs());
                    param.put("kontakrs", akses.getkontakrs());
                    param.put("emailrs", akses.getemailrs());
                    param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
                    if (TCari.getText().equals("")) {
                        Valid.MyReportqry("rptDataObservasiIntradialitik.jasper", "report", "::[ Data Hemodialis ]::",
                                "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, observasi_intradialitik.tanggal,"
                                + "observasi_intradialitik.pemantauan, observasi_intradialitik.keluhan, observasi_intradialitik.bb,"
                                + "observasi_intradialitik.kesadaran, observasi_intradialitik.td, observasi_intradialitik.nadi,"
                                + "observasi_intradialitik.rr, observasi_intradialitik.suhu, observasi_intradialitik.qb,"
                                + "observasi_intradialitik.qd, observasi_intradialitik.tv, observasi_intradialitik.tmp,"
                                + "observasi_intradialitik.volume, observasi_intradialitik.asesmen, observasi_intradialitik.nip,"
                                + "petugas.nama FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                                + "INNER JOIN observasi_intradialitik ON reg_periksa.no_rawat = observasi_intradialitik.no_rawat "
                                + "INNER JOIN petugas ON observasi_intradialitik.nip = petugas.nip where "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' order by observasi_intradialitik.tanggal ", param);
                    } else {
                        Valid.MyReportqry("rptDataObservasiIntradialitik.jasper", "report", "::[ Data Hemodialis ]::",
                                "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, observasi_intradialitik.tanggal,"
                                + "observasi_intradialitik.pemantauan, observasi_intradialitik.keluhan, observasi_intradialitik.bb,"
                                + "observasi_intradialitik.kesadaran, observasi_intradialitik.td, observasi_intradialitik.nadi,"
                                + "observasi_intradialitik.rr, observasi_intradialitik.suhu, observasi_intradialitik.qb,"
                                + "observasi_intradialitik.qd, observasi_intradialitik.tv, observasi_intradialitik.tmp,"
                                + "observasi_intradialitik.volume, observasi_intradialitik.asesmen, observasi_intradialitik.nip,"
                                + "petugas.nama FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                                + "INNER JOIN observasi_intradialitik ON reg_periksa.no_rawat = observasi_intradialitik.no_rawat "
                                + "INNER JOIN petugas ON observasi_intradialitik.nip = petugas.nip where "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and reg_periksa.no_rawat like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.no_rkm_medis like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.nm_pasien like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and observasi_intradialitik.keluhan like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and observasi_intradialitik.asesmen like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and observasi_intradialitik.nip like '%" + TCari.getText().trim() + "%' or "
                                + "observasi_intradialitik.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and petugas.nama like '%" + TCari.getText().trim() + "%' "
                                + "order by observasi_intradialitik.tanggal ", param);
                    }
                }
                this.setCursor(Cursor.getDefaultCursor());
                break;
            case 2:
                break;
            case 3: //sudah
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (tabMonitoringHD.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
                    BtnBatal.requestFocus();
                } else if (tabMonitoringHD.getRowCount() != 0) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("namars", akses.getnamars());
                    param.put("alamatrs", akses.getalamatrs());
                    param.put("kotars", akses.getkabupatenrs());
                    param.put("propinsirs", akses.getpropinsirs());
                    param.put("kontakrs", akses.getkontakrs());
                    param.put("emailrs", akses.getemailrs());
                    param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
                    if (TCari.getText().equals("")) {
                        Valid.MyReportqry("rptDataHemodialisa.jasper", "report", "::[ Data Hemodialis ]::",
                                "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                                + "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "
                                + "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "
                                + "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "
                                + "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "
                                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                                + "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "
                                + "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' order by hemodialisa.tanggal ", param);
                    } else {
                        Valid.MyReportqry("rptDataHemodialisa.jasper", "report", "::[ Data Hemodialis ]::",
                                "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,reg_periksa.umurdaftar,reg_periksa.sttsumur,"
                                + "pasien.jk,hemodialisa.tanggal,hemodialisa.lama,hemodialisa.akses,hemodialisa.dialist,hemodialisa.transfusi,hemodialisa.penarikan, "
                                + "hemodialisa.qb,hemodialisa.qd,hemodialisa.ureum,hemodialisa.hb,hemodialisa.hbsag,creatinin,hemodialisa.hiv,hemodialisa.hcv,hemodialisa.lain, "
                                + "hemodialisa.kd_dokter,dokter.nm_dokter,hemodialisa.kd_penyakit,penyakit.nm_penyakit "
                                + "from hemodialisa inner join reg_periksa on hemodialisa.no_rawat=reg_periksa.no_rawat "
                                + "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                                + "inner join dokter on hemodialisa.kd_dokter=dokter.kd_dokter "
                                + "inner join penyakit on hemodialisa.kd_penyakit=penyakit.kd_penyakit where "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and reg_periksa.no_rawat like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.no_rkm_medis like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and pasien.nm_pasien like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.akses like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.dialist like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and hemodialisa.lain like '%" + TCari.getText().trim() + "%' or "
                                + "hemodialisa.tanggal between '" + Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00' and '" + Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59' and dokter.nm_dokter like '%" + TCari.getText().trim() + "%' "
                                + "order by hemodialisa.tanggal ", param);
                    }
                }
                this.setCursor(Cursor.getDefaultCursor());

                break;
            case 4:
                break;
            default:
        }
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnPrintActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                tampilRencanaHD();
                break;
            case 1:
                tampilDataHD();
                break;
            case 2:
                tampilLaporanHD();
                break;
            case 3: 
                tampilMonitoringHD();
                break;
            case 4:
                tampilLayananHD();
                break;
        }


}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampilDataHD();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            tampilDataHD();
            TCari.setText("");
        } else {
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TCari, Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbDataHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataHDMouseClicked
        if (tabDataHD.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbDataHDMouseClicked

    private void tbDataHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDataHDKeyPressed
        if (tabDataHD.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbDataHDKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt, Tanggal, Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt, Jam, Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt, Menit, btnDokter);
    }//GEN-LAST:event_DetikKeyPressed

    private void kddokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddokKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            namadokter.setText(dokter.tampil3(kddok.getText()));
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            Detik.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Durasi.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            btnDokterActionPerformed(null);
        }
    }//GEN-LAST:event_kddokKeyPressed

    private void btnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokterActionPerformed
        pilihan = 1;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokterActionPerformed

    private void DurasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DurasiKeyPressed

    }//GEN-LAST:event_DurasiKeyPressed

    private void btnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokterKeyPressed
        Valid.pindah(evt, Detik, Durasi);
    }//GEN-LAST:event_btnDokterKeyPressed

    private void DialiserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DialiserKeyPressed

    }//GEN-LAST:event_DialiserKeyPressed

    private void BB_PreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BB_PreKeyPressed

    }//GEN-LAST:event_BB_PreKeyPressed

    private void tbRencanaHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRencanaHDMouseClicked
        if (tabRencanaHD.getRowCount() != 0) {
            try {
                getDataRencanaHD();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbRencanaHDMouseClicked

    private void tbRencanaHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRencanaHDKeyPressed
        if (tabDataHD.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getDataRencanaHD();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRencanaHDKeyPressed

    private void kddok1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddok1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            namadokter.setText(dokter.tampil3(kddok.getText()));
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            Detik.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Durasi.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            btnDokterActionPerformed(null);
        }
    }//GEN-LAST:event_kddok1KeyPressed

    private void btnDokter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokter1ActionPerformed
        pilihan = 2;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_btnDokter1ActionPerformed

    private void btnDokter1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokter1KeyPressed
        Valid.pindah(evt, Detik, Durasi);
    }//GEN-LAST:event_btnDokter1KeyPressed

    private void Tanggal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal2KeyPressed

    private void kdpoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdpoliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdpoliKeyPressed

    private void btnPoliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPoliActionPerformed
        poli.emptTeks();
        poli.isCek();
        poli.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        poli.setLocationRelativeTo(internalFrame1);
        poli.setVisible(true);
    }//GEN-LAST:event_btnPoliActionPerformed

    private void btnPoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPoliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPoliKeyPressed

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInput1ActionPerformed

    private void tbMonitoringHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbMonitoringHDMouseClicked
        if (tabMonitoringHD.getRowCount() != 0) {
            try {
                getDataMonitoringHD();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbMonitoringHDMouseClicked

    private void tbMonitoringHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbMonitoringHDKeyPressed
        if (tabMonitoringHD.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getDataMonitoringHD();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbMonitoringHDKeyPressed

    private void ChkInput2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput2ActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInput2ActionPerformed

    private void tbLaporanHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLaporanHDMouseClicked
        if (tabLaporanHD.getRowCount() != 0) {
            try {
                getDataLaporanHD();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbLaporanHDMouseClicked

    private void tbLaporanHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbLaporanHDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbLaporanHDKeyPressed

    private void TimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TimeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TimeKeyPressed

    private void NatriumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NatriumKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NatriumKeyPressed

    private void QBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QBKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_QBKeyPressed

    private void QDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_QDKeyPressed

    private void ChkInput3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkInput3ActionPerformed

    private void tbLayananHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLayananHDMouseClicked
        if (tabLayananHD.getRowCount() != 0) {
            try {
                getDataLayananHD();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbLayananHDMouseClicked

    private void tbLayananHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbLayananHDKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbLayananHDKeyPressed

    private void ChkInput4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkInput4ActionPerformed

    private void MnCetakObservasiIntradialitikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCetakObservasiIntradialitikActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (tabMonitoringHD.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        } else if (tabMonitoringHD.getRowCount() != 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("namars", akses.getnamars());
            param.put("alamatrs", akses.getalamatrs());
            param.put("kotars", akses.getkabupatenrs());
            param.put("propinsirs", akses.getpropinsirs());
            param.put("kontakrs", akses.getkontakrs());
            param.put("emailrs", akses.getemailrs());
            param.put("logo", Sequel.cariGambar("select setting.logo from setting"));
            Valid.MyReportqry("rptDataObservasiIntradialitik.jasper", "report", "::[ Data Hemodialis ]::",
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, observasi_intradialitik.tanggal,"
                    + "observasi_intradialitik.pemantauan, observasi_intradialitik.keluhan, observasi_intradialitik.bb,"
                    + "observasi_intradialitik.kesadaran, observasi_intradialitik.td, observasi_intradialitik.nadi,"
                    + "observasi_intradialitik.rr, observasi_intradialitik.suhu, observasi_intradialitik.qb,"
                    + "observasi_intradialitik.qd, observasi_intradialitik.tv, observasi_intradialitik.tmp,"
                    + "observasi_intradialitik.volume, observasi_intradialitik.asesmen, observasi_intradialitik.nip,"
                    + "petugas.nama FROM reg_periksa INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN observasi_intradialitik ON reg_periksa.no_rawat = observasi_intradialitik.no_rawat "
                    + "INNER JOIN petugas ON observasi_intradialitik.nip = petugas.nip where "
                    + "reg_periksa.no_rawat = '" + TNoRw.getText() + "'"
                    + " order by observasi_intradialitik.tanggal ", param);

        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_MnCetakObservasiIntradialitikActionPerformed

    private void Scroll3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Scroll3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Scroll3MouseClicked

    private void RencanaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RencanaKeyPressed

    }//GEN-LAST:event_RencanaKeyPressed

    private void kdpetugasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdpetugasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdpetugasKeyPressed

    private void btnPetugas2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPetugas2ActionPerformed
        pilihan = 1;
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_btnPetugas2ActionPerformed

    private void btnPetugas2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnPetugas2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPetugas2KeyPressed

    private void BB_LaluKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BB_LaluKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BB_LaluKeyPressed

    private void BB_NaikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BB_NaikKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BB_NaikKeyPressed

    private void BB_PostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BB_PostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BB_PostKeyPressed

    private void JadwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JadwalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JadwalKeyPressed

    private void VaskulerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VaskulerKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_VaskulerKeyPressed

    private void HariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_HariKeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void UGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_UGKeyPressed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void TemperatureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TemperatureKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TemperatureKeyPressed

    private void DialiserLainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DialiserLainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DialiserLainKeyPressed

    private void IntermittenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntermittenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_IntermittenKeyPressed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox7ActionPerformed

    private void ConductivityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ConductivityKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ConductivityKeyPressed

    private void LMWHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LMWHKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_LMWHKeyPressed

    private void DosisSirkulasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DosisSirkulasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DosisSirkulasiKeyPressed

    private void DosisAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DosisAwalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DosisAwalKeyPressed

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox12ActionPerformed

    private void TanpaHeparinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanpaHeparinKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TanpaHeparinKeyPressed

    private void ContinousKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ContinousKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ContinousKeyPressed

    private void Detik1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Detik1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Detik1KeyPressed

    private void Menit1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Menit1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menit1KeyPressed

    private void Jam1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Jam1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jam1KeyPressed

    private void LainlainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LainlainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_LainlainKeyPressed

    private void Conductivity2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Conductivity2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Conductivity2KeyPressed

    private void TMPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TMPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TMPKeyPressed

    private void VPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_VPKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_VPKeyPressed

    private void UFRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UFRKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_UFRKeyPressed

    private void QbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QbKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_QbKeyPressed

    private void TDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TDKeyPressed

    }//GEN-LAST:event_TDKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed

    }//GEN-LAST:event_SuhuKeyPressed

    private void DextroseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DextroseKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DextroseKeyPressed

    private void MakMinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MakMinKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MakMinKeyPressed

    private void InputLainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InputLainKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputLainKeyPressed

    private void NACLKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NACLKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NACLKeyPressed

    private void TMP5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TMP5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TMP5KeyPressed

    private void UFHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UFHKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_UFHKeyPressed

    private void UFHCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UFHCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UFHCheckActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMHemodialisa dialog = new RMHemodialisa(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Asetat;
    private widget.TextBox BB_Lalu;
    private widget.TextBox BB_Naik;
    private widget.TextBox BB_Post;
    private widget.TextBox BB_Pre;
    private javax.swing.JCheckBox Bicarbonat;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkInput1;
    private widget.CekBox ChkInput2;
    private widget.CekBox ChkInput3;
    private widget.CekBox ChkInput4;
    private widget.CekBox ChkKejadian;
    private widget.TextBox Conductivity;
    private widget.TextBox Conductivity2;
    private widget.TextBox Continous;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.ComboBox Detik1;
    private widget.TextBox Dextrose;
    private widget.TextBox Dialiser;
    private widget.TextBox Dialiser2;
    private widget.TextBox DialiserLain;
    private widget.TextBox DosisAwal;
    private widget.TextBox DosisSirkulasi;
    private widget.TextBox Durasi;
    private javax.swing.JCheckBox Edukasi;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private widget.PanelBiasa FormInput4;
    private widget.PanelBiasa FormInput6;
    private widget.TextBox Hari;
    private widget.TextBox InputLain;
    private widget.TextBox Intermitten;
    private widget.TextBox JK;
    private widget.TextBox Jadwal;
    private widget.ComboBox Jam;
    private widget.ComboBox Jam1;
    private javax.swing.JCheckBox Keperawatan;
    private widget.TextBox KeteranganEdukasi;
    private widget.TextBox KeteranganLain;
    private widget.Label LCount;
    private widget.TextBox LMWH;
    private javax.swing.JCheckBox Lain;
    private widget.TextBox Lainlain;
    private widget.TextBox MakMin;
    private widget.ComboBox Menit;
    private widget.ComboBox Menit1;
    private javax.swing.JMenuItem MnCetakObservasiIntradialitik;
    private widget.TextBox NACL;
    private widget.TextBox Natrium;
    private javax.swing.JPanel PanelInput;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPanel PanelInput2;
    private javax.swing.JPanel PanelInput3;
    private javax.swing.JPanel PanelInput4;
    private javax.swing.JPanel PanelInput6;
    private javax.swing.JCheckBox PilihanReuse;
    private javax.swing.JCheckBox Poly;
    private javax.swing.JCheckBox ProgramBilas;
    private widget.TextBox QB;
    private widget.TextBox QD;
    private widget.TextBox Qb;
    private widget.TextArea Rencana;
    private widget.ComboBox ResepHD;
    private widget.TextBox Reuse;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll4;
    private widget.ComboBox StatusDialiser;
    private widget.TextBox Suhu;
    private widget.TextBox TCari;
    private widget.TextBox TD;
    private widget.TextBox TMP;
    private widget.TextBox TMP5;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TabPane TabRawat;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tanggal2;
    private widget.TextBox TanggalRegistrasi;
    private widget.TextBox TanpaHeparin;
    private widget.TextBox Temperature;
    private widget.TextBox TglLahir;
    private widget.TextBox Time;
    private widget.TextBox UFH;
    private javax.swing.JCheckBox UFHCheck;
    private widget.TextBox UFR;
    private widget.TextBox UG;
    private widget.TextBox UR;
    private javax.swing.JCheckBox Ultrafiltration;
    private widget.TextBox Umur;
    private widget.TextBox VP;
    private widget.TextBox Vaskuler;
    private widget.Button btnDokter;
    private widget.Button btnDokter1;
    private widget.Button btnPetugas2;
    private widget.Button btnPoli;
    private javax.swing.JCheckBox finger;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame3;
    private widget.InternalFrame internalFrame4;
    private widget.InternalFrame internalFrame5;
    private widget.InternalFrame internalFrame6;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private javax.swing.JLabel jLabel11;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel138;
    private widget.Label jLabel139;
    private widget.Label jLabel14;
    private widget.Label jLabel140;
    private widget.Label jLabel141;
    private widget.Label jLabel142;
    private widget.Label jLabel143;
    private widget.Label jLabel144;
    private widget.Label jLabel145;
    private widget.Label jLabel146;
    private widget.Label jLabel147;
    private widget.Label jLabel148;
    private widget.Label jLabel149;
    private widget.Label jLabel150;
    private widget.Label jLabel16;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private javax.swing.JLabel jLabel2;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel23;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private javax.swing.JLabel jLabel3;
    private widget.Label jLabel36;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel41;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private javax.swing.JLabel jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel53;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private javax.swing.JLabel jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private javax.swing.JLabel jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu2;
    private widget.TextBox kddok;
    private widget.TextBox kddok1;
    private widget.TextBox kdpetugas;
    private widget.TextBox kdpoli;
    private widget.TextBox namadokter;
    private widget.TextBox namadokter1;
    private widget.TextBox namapetugas;
    private widget.TextBox namapoli;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbDataHD;
    private widget.Table tbLaporanHD;
    private widget.Table tbLayananHD;
    private widget.Table tbMonitoringHD;
    private widget.Table tbRencanaHD;
    private javax.swing.JCheckBox tindakan;
    // End of variables declaration//GEN-END:variables

    public void tampilRencanaHD() {
        Valid.tabelKosong(tabRencanaHD);
        try {
            if (TCari.getText().toString().trim().equals("")) {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                    + "rencana_hd.tanggal, rencana_hd.rencana, rencana_hd.kd_dokter, dokter.nm_dokter, "
                    + "rencana_hd.kd_poli, poliklinik.nm_poli "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN rencana_hd ON reg_periksa.no_rawat = rencana_hd.no_rawat "
                    + "INNER JOIN dokter ON rencana_hd.kd_dokter = dokter.kd_dokter "
                    + "INNER JOIN poliklinik ON rencana_hd.kd_poli = poliklinik.kd_poli "
                    + "WHERE rencana_hd.tanggal BETWEEN ? AND ? "
                    + "ORDER BY rencana_hd.tanggal"
                );
            } else {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                    + "rencana_hd.tanggal, rencana_hd.rencana, rencana_hd.kd_dokter, dokter.nm_dokter, "
                    + "rencana_hd.kd_poli, poliklinik.nm_poli "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN rencana_hd ON reg_periksa.no_rawat = rencana_hd.no_rawat "
                    + "INNER JOIN dokter ON rencana_hd.kd_dokter = dokter.kd_dokter "
                    + "INNER JOIN poliklinik ON rencana_hd.kd_poli = poliklinik.kd_poli "
                    + "WHERE rencana_hd.tanggal BETWEEN ? AND ? "
                    + "AND ("
                    + "    reg_periksa.no_rawat LIKE ? "
                    + "    OR pasien.no_rkm_medis LIKE ? "
                    + "    OR pasien.nm_pasien LIKE ? "
                    + "    OR rencana_hd.rencana LIKE ? "
                    + "    OR dokter.nm_dokter LIKE ? "
                    + "    OR poliklinik.nm_poli LIKE ? "
                    + ") "
                    + "ORDER BY rencana_hd.tanggal"
                );
            }

            try {
                if (TCari.getText().toString().trim().equals("")) {
                    // Tanpa pencarian: 2 parameter (tanggal dari, tanggal sampai)
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                } else {
                    // Dengan pencarian: 8 parameter
                    // (tanggal dari, tanggal sampai, keyword × 6)
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, "%" + TCari.getText() + "%");
                    ps.setString(7, "%" + TCari.getText() + "%");
                    ps.setString(8, "%" + TCari.getText() + "%");
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabRencanaHD.addRow(new String[]{
                        rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), rs.getString("tgl_lahir"),
                        rs.getString("tanggal"), rs.getString("rencana"),rs.getString("kd_dokter"), rs.getString("nm_dokter"),
                        rs.getString("kd_poli"), rs.getString("nm_poli")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }
        int b = tabRencanaHD.getRowCount();
        LCount.setText("" + b);
    }
    
    public void tampilDataHD() {
        Valid.tabelKosong(tabDataHD);
        try {
            if (TCari.getText().toString().trim().equals("")) {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, "
                    + "reg_periksa.umurdaftar, reg_periksa.sttsumur, pasien.jk, "
                    + "hemodialisa.tanggal, hemodialisa.kd_dokter, dokter.nm_dokter, "
                    + "hemodialisa.kd_petugas, petugas.nama, hemodialisa.durasi, "
                    + "hemodialisa.bb_pre, hemodialisa.bb_lalu, hemodialisa.bb_naik, "
                    + "hemodialisa.bb_post, hemodialisa.status_dialiser, "
                    + "hemodialisa.penggunaan_dialiser, hemodialisa.akses_vaskuler, "
                    + "hemodialisa.jadwal, hemodialisa.hari "
                    + "FROM hemodialisa "
                    + "INNER JOIN reg_periksa ON hemodialisa.no_rawat = reg_periksa.no_rawat "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN dokter ON hemodialisa.kd_dokter = dokter.kd_dokter "
                    + "INNER JOIN petugas ON hemodialisa.kd_petugas = petugas.nip "
                    + "WHERE hemodialisa.tanggal BETWEEN ? AND ? "
                    + "ORDER BY hemodialisa.tanggal"
                );
            } else {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, "
                    + "reg_periksa.umurdaftar, reg_periksa.sttsumur, pasien.jk, "
                    + "hemodialisa.tanggal, hemodialisa.kd_dokter, dokter.nm_dokter, "
                    + "hemodialisa.kd_petugas, petugas.nama, hemodialisa.durasi, "
                    + "hemodialisa.bb_pre, hemodialisa.bb_lalu, hemodialisa.bb_naik, "
                    + "hemodialisa.bb_post, hemodialisa.status_dialiser, "
                    + "hemodialisa.penggunaan_dialiser, hemodialisa.akses_vaskuler, "
                    + "hemodialisa.jadwal, hemodialisa.hari "
                    + "FROM hemodialisa "
                    + "INNER JOIN reg_periksa ON hemodialisa.no_rawat = reg_periksa.no_rawat "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN dokter ON hemodialisa.kd_dokter = dokter.kd_dokter "
                    + "INNER JOIN petugas ON hemodialisa.kd_petugas = petugas.nip "
                    + "WHERE hemodialisa.tanggal BETWEEN ? AND ? "
                    + "AND ("
                    + "    reg_periksa.no_rawat LIKE ? "
                    + "    OR pasien.no_rkm_medis LIKE ? "
                    + "    OR pasien.nm_pasien LIKE ? "
                    + "    OR hemodialisa.akses_vaskuler LIKE ? "
                    + "    OR hemodialisa.status_dialiser LIKE ? "
                    + "    OR hemodialisa.penggunaan_dialiser LIKE ? "
                    + "    OR dokter.nm_dokter LIKE ? "
                    + "    OR petugas.nama LIKE ? "
                    + ") "
                    + "ORDER BY hemodialisa.tanggal"
                );
            }

            try {
                if (TCari.getText().toString().trim().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, "%" + TCari.getText() + "%");
                    ps.setString(7, "%" + TCari.getText() + "%");
                    ps.setString(8, "%" + TCari.getText() + "%");
                    ps.setString(9, "%" + TCari.getText() + "%");
                    ps.setString(10, "%" + TCari.getText() + "%");
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabDataHD.addRow(new String[]{
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("umurdaftar") + " " + rs.getString("sttsumur"),
                        rs.getString("jk"),
                        rs.getString("tanggal"),
                        rs.getString("kd_dokter"),
                        rs.getString("nm_dokter"),
                        rs.getString("kd_petugas"),
                        rs.getString("nama"),
                        rs.getString("durasi"),
                        rs.getString("bb_pre"),
                        rs.getString("bb_lalu"),
                        rs.getString("bb_naik"),
                        rs.getString("bb_post"),
                        rs.getString("status_dialiser"),
                        rs.getString("penggunaan_dialiser"),
                        rs.getString("akses_vaskuler"),
                        rs.getString("jadwal"),
                        rs.getString("hari")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }
        int b = tabDataHD.getRowCount();
        LCount.setText("" + b);
    }

    public void tampilMonitoringHD() {
        Valid.tabelKosong(tabMonitoringHD);
        String sql;

        if (TCari.getText().toString().trim().equals("")) {
            sql = "SELECT reg_periksa.no_rawat, "
                    + "pasien.no_rkm_medis, "
                    + "pasien.nm_pasien, "
                    + "pasien.tgl_lahir, "
                    + "monitoring_hd.jam, "
                    + "monitoring_hd.td, "
                    + "monitoring_hd.qb, "
                    + "monitoring_hd.vp, "
                    + "monitoring_hd.tmp, "
                    + "monitoring_hd.suhu, "
                    + "monitoring_hd.ufr, "
                    + "monitoring_hd.conductivity, "
                    + "monitoring_hd.lainlain, "
                    + "monitoring_hd.nacl, "
                    + "monitoring_hd.dextrose, "
                    + "monitoring_hd.makmin, "
                    + "monitoring_hd.inputlain, "
                    + "monitoring_hd.ufgoal "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN monitoring_hd ON reg_periksa.no_rawat = monitoring_hd.no_rawat "
                    + "WHERE reg_periksa.no_rawat LIKE ? "
                    + "ORDER BY monitoring_hd.jam";
        } else {
            sql = "SELECT reg_periksa.no_rawat, "
                    + "pasien.no_rkm_medis, "
                    + "pasien.nm_pasien, "
                    + "pasien.tgl_lahir, "
                    + "monitoring_hd.jam, "
                    + "monitoring_hd.td, "
                    + "monitoring_hd.qb, "
                    + "monitoring_hd.vp, "
                    + "monitoring_hd.tmp, "
                    + "monitoring_hd.suhu, "
                    + "monitoring_hd.ufr, "
                    + "monitoring_hd.conductivity, "
                    + "monitoring_hd.lainlain, "
                    + "monitoring_hd.nacl, "
                    + "monitoring_hd.dextrose, "
                    + "monitoring_hd.makmin, "
                    + "monitoring_hd.inputlain, "
                    + "monitoring_hd.ufgoal "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN monitoring_hd ON reg_periksa.no_rawat = monitoring_hd.no_rawat "
                    + "WHERE ("
                    + "reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR "
                    + "monitoring_hd.td LIKE ? OR monitoring_hd.qb LIKE ? OR "
                    + "monitoring_hd.conductivity LIKE ? OR monitoring_hd.lainlain LIKE ?) "
                    + "ORDER BY monitoring_hd.jam";
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, TNoRw.getText());

            if (TCari.getText().toString().trim().equals("")) {
                // Tanpa pencarian: 2 parameter (tanggal dari, tanggal sampai)
                ps.setString(1, "%" + TCari.getText() + "%");
            } else {
                // Dengan pencarian: 8 parameter
                // (tanggal dari, tanggal sampai, keyword × 6)
                ps.setString(1, "%" + TCari.getText() + "%");
                ps.setString(2, "%" + TCari.getText() + "%");
                ps.setString(3, "%" + TCari.getText() + "%");
                ps.setString(4, "%" + TCari.getText() + "%");
                ps.setString(5, "%" + TCari.getText() + "%");
                ps.setString(6, "%" + TCari.getText() + "%");
                ps.setString(7, "%" + TCari.getText() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tabMonitoringHD.addRow(new String[]{
                        rs.getString("no_rawat"), 
                        rs.getString("no_rkm_medis"), 
                        rs.getString("nm_pasien"), 
                        rs.getString("tgl_lahir"),
                        rs.getString("jam"), 
                        rs.getString("td"), 
                        rs.getString("qb"), 
                        rs.getString("vp"),
                        rs.getString("tmp"), 
                        rs.getString("suhu"), 
                        rs.getString("ufr"), 
                        rs.getString("conductivity"),
                        rs.getString("lainlain"),
                        rs.getString("nacl"),
                        rs.getString("dextrose"),
                        rs.getString("makmin"),
                        rs.getString("inputlain"),
                        rs.getString("ufgoal")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        LCount.setText(String.valueOf(tabMonitoringHD.getRowCount()));
    }

    public void tampilLaporanHD() {
        Valid.tabelKosong(tabLaporanHD);
        String tglAwal = Valid.SetTgl(DTPCari1.getSelectedItem() + "");
        String tglAkhir = Valid.SetTgl(DTPCari2.getSelectedItem() + "");

        try {
            String query = "";
            if (TCari.getText().toString().trim().isEmpty()) {
                query = "SELECT reg_periksa.no_rawat, "
                        + "pasien.no_rkm_medis, "
                        + "pasien.nm_pasien, "
                        + "pasien.tgl_lahir, "
                        + "laporan_tindakan_hemodialisa.tanggal, "
                        + "laporan_tindakan_hemodialisa.resep_hd, "
                        + "laporan_tindakan_hemodialisa.time_hd, "
                        + "laporan_tindakan_hemodialisa.qb, "
                        + "laporan_tindakan_hemodialisa.qd, "
                        + "laporan_tindakan_hemodialisa.ug, "
                        + "laporan_tindakan_hemodialisa.ur, "
                        + "laporan_tindakan_hemodialisa.jenis_dial2, "
                        + "laporan_tindakan_hemodialisa.na_start, "
                        + "laporan_tindakan_hemodialisa.conductivity, "
                        + "laporan_tindakan_hemodialisa.temp, "
                        + "laporan_tindakan_hemodialisa.dosis_sirkulasi, "
                        + "laporan_tindakan_hemodialisa.lmwh, "
                        + "laporan_tindakan_hemodialisa.dosis_awal, "
                        + "laporan_tindakan_hemodialisa.heparin, "
                        + "laporan_tindakan_hemodialisa.dosis_continous, "
                        + "laporan_tindakan_hemodialisa.dosis_intermitten "
                        + "laporan_tindakan_hemodialisa.jenis_dial1, "
                        + "laporan_tindakan_hemodialisa.ultrafiltration, "
                        + "laporan_tindakan_hemodialisa.asetat, "
                        + "laporan_tindakan_hemodialisa.bicarbonat, "
                        + "laporan_tindakan_hemodialisa.bilas, "
                        + "laporan_tindakan_hemodialisa.ufh "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN laporan_tindakan_hemodialisa ON reg_periksa.no_rawat = laporan_tindakan_hemodialisa.no_rawat "
                        + "WHERE laporan_tindakan_hemodialisa.tanggal BETWEEN ? AND ? "
                        + "ORDER BY laporan_tindakan_hemodialisa.tanggal";
            } else {
                query = "SELECT reg_periksa.no_rawat, "
                        + "pasien.no_rkm_medis, "
                        + "pasien.nm_pasien, "
                        + "pasien.tgl_lahir, "
                        + "laporan_tindakan_hemodialisa.tanggal, "
                        + "laporan_tindakan_hemodialisa.resep_hd, "
                        + "laporan_tindakan_hemodialisa.time_hd, "
                        + "laporan_tindakan_hemodialisa.qb, "
                        + "laporan_tindakan_hemodialisa.qd, "
                        + "laporan_tindakan_hemodialisa.ug, "
                        + "laporan_tindakan_hemodialisa.ur, "
                        + "laporan_tindakan_hemodialisa.jenis_dial2, "
                        + "laporan_tindakan_hemodialisa.na_start, "
                        + "laporan_tindakan_hemodialisa.conductivity, "
                        + "laporan_tindakan_hemodialisa.temp, "
                        + "laporan_tindakan_hemodialisa.dosis_sirkulasi, "
                        + "laporan_tindakan_hemodialisa.lmwh, "
                        + "laporan_tindakan_hemodialisa.dosis_awal, "
                        + "laporan_tindakan_hemodialisa.heparin, "
                        + "laporan_tindakan_hemodialisa.dosis_continous, "
                        + "laporan_tindakan_hemodialisa.dosis_intermitten, "
                        + "laporan_tindakan_hemodialisa.jenis_dial1, "
                        + "laporan_tindakan_hemodialisa.ultrafiltration, "
                        + "laporan_tindakan_hemodialisa.asetat, "
                        + "laporan_tindakan_hemodialisa.bicarbonat, "
                        + "laporan_tindakan_hemodialisa.bilas, "
                        + "laporan_tindakan_hemodialisa.ufh "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN laporan_tindakan_hemodialisa ON reg_periksa.no_rawat = laporan_tindakan_hemodialisa.no_rawat "
                        + "WHERE (laporan_tindakan_hemodialisa.tanggal BETWEEN ? AND ?) AND ("
                        + "reg_periksa.no_rawat LIKE ? OR "
                        + "pasien.no_rkm_medis LIKE ? OR "
                        + "pasien.nm_pasien LIKE ? OR "
                        + "laporan_tindakan_hemodialisa.resep_hd LIKE ? OR "
                        + "laporan_tindakan_hemodialisa.time_hd LIKE ? OR "
                        + "laporan_tindakan_hemodialisa.jenis_dial1 LIKE ? OR "
                        + "laporan_tindakan_hemodialisa.heparin LIKE ?) "
                        + "ORDER BY laporan_tindakan_hemodialisa.tanggal";
            }

            ps = koneksi.prepareStatement(query);
            ps.setString(1, tglAwal);
            ps.setString(2, tglAkhir);

            if (!TCari.getText().toString().trim().isEmpty()) {
                String cari = "%" + TCari.getText().trim() + "%";
                for (int i = 3; i <= 9; i++) {
                    ps.setString(i, cari);
                }
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                tabLaporanHD.addRow(new String[]{
                    rs.getString("no_rawat"), 
                    rs.getString("no_rkm_medis"), 
                    rs.getString("nm_pasien"), 
                    rs.getString("tgl_lahir"),
                    rs.getString("tanggal"), 
                    rs.getString("resep_hd"), 
                    rs.getString("time_hd"),
                    rs.getString("qb"), 
                    rs.getString("qd"), 
                    rs.getString("ug"), 
                    rs.getString("ur"),
                    rs.getString("jenis_dial2"), 
                    rs.getString("na_start"), 
                    rs.getString("conductivity"),
                    rs.getString("temp"), 
                    rs.getString("dosis_sirkulasi"), 
                    rs.getString("lmwh"),
                    rs.getString("dosis_awal"), 
                    rs.getString("heparin"), 
                    rs.getString("dosis_continous"),
                    rs.getString("dosis_intermitten"),
                    rs.getString("jenis_dial1"),
                    rs.getString("ultrafiltration"),
                    rs.getString("asetat"),
                    rs.getString("bicarbonat"),
                    rs.getString("bilas"),
                    rs.getString("ufh")
                });
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println("Notifikasi Penutupan : " + e.getMessage());
            }
        }

        LCount.setText(String.valueOf(tabLaporanHD.getRowCount()));
    }

    public void tampilLayananHD() {
        Valid.tabelKosong(tabLayananHD);

        try {
            String query = "";
            if (TCari.getText().toString().trim().isEmpty()) {
                query = "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                        + "bukti_layanan_hd.finger, bukti_layanan_hd.tindakan_hd, bukti_layanan_hd.tindakan_reuse, "
                        + "bukti_layanan_hd.dialiser, bukti_layanan_hd.reuse, bukti_layanan_hd.keperawatan, "
                        + "bukti_layanan_hd.edukasi, bukti_layanan_hd.keterangan_edukasi, bukti_layanan_hd.lain, "
                        + "bukti_layanan_hd.ketengan_edukasi "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN bukti_layanan_hd ON reg_periksa.no_rawat = bukti_layanan_hd.no_rawat "
                        + "WHERE reg_periksa.no_rawat = ?";  // <-- Filter by no_rawat, bukan tanggal
            } else {
                query = "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                        + "bukti_layanan_hd.finger, bukti_layanan_hd.tindakan_hd, bukti_layanan_hd.tindakan_reuse, "
                        + "bukti_layanan_hd.dialiser, bukti_layanan_hd.reuse, bukti_layanan_hd.keperawatan, "
                        + "bukti_layanan_hd.edukasi, bukti_layanan_hd.keterangan_edukasi, bukti_layanan_hd.lain, "
                        + "bukti_layanan_hd.ketengan_edukasi "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN bukti_layanan_hd ON reg_periksa.no_rawat = bukti_layanan_hd.no_rawat "
                        + "WHERE ("
                        + "reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR "
                        + "bukti_layanan_hd.dialiser LIKE ? OR bukti_layanan_hd.reuse LIKE ? OR "
                        + "bukti_layanan_hd.lain LIKE ? OR bukti_layanan_hd.keterangan_edukasi LIKE ?) ";
            }

            ps = koneksi.prepareStatement(query);

            if (TCari.getText().toString().trim().isEmpty()) {
                ps.setString(1, TNoRw.getText());  // <-- Filter by no_rawat pasien aktif
            } else {
                String cari = "%" + TCari.getText().trim() + "%";
                for (int i = 1; i <= 7; i++) {
                    ps.setString(i, cari);
                }
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                tabLayananHD.addRow(new String[]{
                    rs.getString("no_rawat"), 
                    rs.getString("no_rkm_medis"), 
                    rs.getString("nm_pasien"), 
                    rs.getString("tgl_lahir"),
                    rs.getString("finger"), 
                    rs.getString("tindakan_hd"), 
                    rs.getString("tindakan_reuse"),
                    rs.getString("dialiser"), 
                    rs.getString("reuse"), 
                    rs.getString("keperawatan"),
                    rs.getString("edukasi"), 
                    rs.getString("keterangan_edukasi"), 
                    rs.getString("lain"),
                    rs.getString("ketengan_edukasi")
                });
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                System.out.println("Notifikasi Penutupan : " + e.getMessage());
            }
        }

        LCount.setText(String.valueOf(tabLayananHD.getRowCount()));
    }

    public void emptTeks() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                kddok1.setText("");
                kdpoli.setText("");
                namadokter1.setText("");
                namapoli.setText("");
                Rencana.setText("");
                Tanggal2.setDate(new Date());
                break;
            case 1:
                kddok.setText("");
                namadokter.setText("");
                
                kdpetugas.setText("");
                namapetugas.setText("");
                
                Durasi.setText("");
                BB_Pre.setText("");
                BB_Lalu.setText("");
                BB_Naik.setText("");
                BB_Post.setText("");
                
                Dialiser.setText("");
                Vaskuler.setText("");
                Jadwal.setText("");
                Hari.setText("");
                Tanggal.setDate(new Date());
                break;
            case 2:
                ResepHD.setSelectedIndex(0);
                Time.setText("");
                QB.setText("");
                QD.setText("");
                UG.setText("");
                UR.setText("");
                DialiserLain.setText("");
                Natrium.setText("");
                Conductivity.setText("");
                Temperature.setText("");
                DosisSirkulasi.setText("");
                LMWH.setText("");
                DosisAwal.setText("");
                TanpaHeparin.setText("");
                Continous.setText("");
                Intermitten.setText("");
                UFH.setText("");
                
                jCheckBox2.setSelected(false);
                jCheckBox3.setSelected(false);
                jCheckBox7.setSelected(false);
                jCheckBox8.setSelected(false);
                jCheckBox9.setSelected(false);
                jCheckBox10.setSelected(false);
                jCheckBox11.setSelected(false);
                jCheckBox12.setSelected(false);
                jCheckBox13.setSelected(false);
                
                ProgramBilas.setSelected(false);
                Ultrafiltration.setSelected(false);
                Asetat.setSelected(false);
                Bicarbonat.setSelected(false);
                Poly.setSelected(false);
                UFHCheck.setSelected(false);
                
                break;
            case 3: //sudah
                TD.setText("");
                Suhu.setText("");
                Qb.setText("");
                UFR.setText("");
                VP.setText("");
                TMP.setText("");
                Conductivity2.setText("");
                Lainlain.setText("");
                
                NACL.setText("");
                Dextrose.setText("");
                MakMin.setText("");
                InputLain.setText("");
                TMP5.setText("");
                
                break;
            case 4:
                finger.setSelected(false);
                tindakan.setSelected(false);
                PilihanReuse.setSelected(false);
                Keperawatan.setSelected(false);
                Edukasi.setSelected(false);
                Lain.setSelected(false);
                
                KeteranganLain.setText("");
                Dialiser2.setText("");
                Reuse.setText("");
                KeteranganEdukasi.setText("");
                break;
            default:
        }

    }

    private void getData() {
        if (tbDataHD.getSelectedRow() != -1) {
            TNoRw.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 2).toString());
            Umur.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 3).toString());
            JK.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 4).toString());
            Valid.SetTgl(Tanggal, tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 5).toString());

            // Kolom 6 = Kode Dokter (hidden), kolom 7 = Nama Dokter
            // Kalau ada field KdDokter di form:
            kddok.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 6).toString());
            namadokter.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 7).toString());

            // Kolom 8 = Kode Petugas (hidden), kolom 9 = Nama Petugas
            kdpetugas.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 8).toString());
            namapetugas.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 9).toString());

            Durasi.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 10).toString());
            BB_Pre.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 11).toString());
            BB_Lalu.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 12).toString());
            BB_Naik.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 13).toString());
            BB_Post.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 14).toString());
            StatusDialiser.setSelectedItem(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 15).toString());
            Dialiser.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 16).toString());
            Vaskuler.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 17).toString());
            Jadwal.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 18).toString());
            Hari.setText(tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 19).toString());
        }
    }

    private void getDataMonitoringHD() {
        if (tbMonitoringHD.getSelectedRow() != -1) {
            TNoRw.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 3).toString());

            // Format TIME: 15:31:12
            Jam1.setSelectedItem(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 4).toString().substring(0, 2));   // 15
            Menit1.setSelectedItem(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 4).toString().substring(3, 5));  // 31
            Detik1.setSelectedItem(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 4).toString().substring(6, 8));  // 12

            // Kolom 5-12
            TD.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 5).toString());          // Tekanan Vena (td)
            Qb.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 6).toString());          // Qb
            VP.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 7).toString());          // VP
            TMP.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 8).toString());          // TMP
            Suhu.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 9).toString());         // Suhu
            UFR.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 10).toString());         // UFR
            Conductivity2.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 11).toString()); // Conductivity
            Lainlain.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 12).toString());    // Lain-lain
            
            NACL.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 13).toString());    // Lain-lain
            Dextrose.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 14).toString());    // Lain-lain
            MakMin.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 15).toString());    // Lain-lain
            InputLain.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 16).toString());    // Lain-lain
            TMP5.setText(tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 17).toString());    // Lain-lain
        }
    }

    private void getDataRencanaHD() {
        if (tbRencanaHD.getSelectedRow() != -1) {
            TNoRw.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 3).toString());
            Valid.SetTgl(Tanggal2, tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 4).toString());
            Rencana.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 5).toString());
            kddok1.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 6).toString());
            namadokter1.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 7).toString()); 
            kdpoli.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 8).toString());
            namapoli.setText(tbRencanaHD.getValueAt(tbRencanaHD.getSelectedRow(), 9).toString());

        }
    }

    private void getDataLaporanHD() {
        if (tbLaporanHD.getSelectedRow() != -1) {
            
            TNoRw.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 3).toString());

            // Kolom 4 = Tanggal (datetime)
            Valid.SetTgl(Tanggal, tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 4).toString());

            // Kolom 5-20
            ResepHD.setSelectedItem(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 5).toString());
            Time.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 6).toString());
            QB.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 7).toString());
            QD.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 8).toString());
            UG.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 9).toString());           // Ultrafiltration Goal
            UR.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 10).toString());          // Ultrafiltration Rate
            DialiserLain.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 11).toString());    // Dialiser (jenis_dial1)
            Natrium.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 12).toString());     // Na Start
            Conductivity.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 13).toString());
            Temperature.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 14).toString());
            DosisSirkulasi.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 15).toString());
            LMWH.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 16).toString());
            DosisAwal.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 17).toString());
            TanpaHeparin.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 18).toString()); // heparin
            Continous.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 19).toString());   // dosis_continous
            Intermitten.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 20).toString()); // dosis_intermitten
            UFH.setText(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 26).toString()); // dosis_intermitten
            
            if(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 21).toString().equals("true")){
                Poly.setSelected(true);
            }
            
            if(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 22).toString().equals("true")){
                Ultrafiltration.setSelected(true);
            }
            
            if(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 23).toString().equals("true")){
                Asetat.setSelected(true);
            }
            
            if(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 24).toString().equals("true")){
                Bicarbonat.setSelected(true);
            }
            
            if(tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 25).toString().equals("true")){
                ProgramBilas.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 11).toString().equals("")){
                jCheckBox2.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 12).toString().equals("")){
                jCheckBox3.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 13).toString().equals("")){
                jCheckBox7.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 14).toString().equals("")){
                jCheckBox8.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 15).toString().equals("")){
                jCheckBox9.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 16).toString().equals("")){
                jCheckBox10.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 17).toString().equals("")){
                jCheckBox11.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 18).toString().equals("")){
                jCheckBox12.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 19).toString().equals("")){
                jCheckBox13.setSelected(true);
            } else if (!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 20).toString().equals("")){
                jCheckBox13.setSelected(true);
            }
            
            if(!tbLaporanHD.getValueAt(tbLaporanHD.getSelectedRow(), 26).toString().equals("")){
                UFHCheck.setSelected(true);
            }
        }
    }
    
    private void getDataLayananHD() {
        if (tbLayananHD.getSelectedRow() != -1) {
            // Kolom 0-3: Data pasien
            TNoRw.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 3).toString());

            // Kolom 4-13: Data bukti layanan HD
            // Kolom 4 = Finger (checkbox)
            finger.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 4).toString()));

            // Kolom 5 = Tindakan HD (checkbox)
            tindakan.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 5).toString()));

            // Kolom 6 = Tindakan Reuse (checkbox)
            PilihanReuse.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 6).toString()));

            // Kolom 7 = Dialiser (text)
            Dialiser2.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 7).toString());

            // Kolom 8 = Reuse (text)
            Reuse.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 8).toString());

            // Kolom 9 = Layanan Keperawatan (checkbox)
            Keperawatan.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 9).toString()));

            // Kolom 10 = Edukasi (checkbox)
            Edukasi.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 10).toString()));

            // Kolom 11 = Keterangan Edukasi (text)
            KeteranganEdukasi.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 11).toString());

            // Kolom 12 = Lain-lain (checkbox)
            Lain.setSelected("true".equals(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 12).toString()));

            // Kolom 13 = Keterangan Lain (text)
            KeteranganLain.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 13).toString());
        }
    }

    private void isRawat() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.jk,reg_periksa.umurdaftar,reg_periksa.sttsumur,reg_periksa.tgl_registrasi,"
                    + "reg_periksa.jam_reg from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, TNoRw.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    JK.setText(rs.getString("jk"));
                    Umur.setText(rs.getString("umurdaftar") + " " + rs.getString("sttsumur"));
                    TanggalRegistrasi.setText(rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg"));
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void setNoRm(String norwt) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        isRawat();
        ChkInput.setSelected(true);
        isForm();
    }

    private void isForm() {
        if (ChkInput.isSelected() == true) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 160));
            FormInput.setVisible(true);
            ChkInput.setVisible(true);
        } else if (ChkInput.isSelected() == false) {
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH, 20));
            FormInput.setVisible(false);
            ChkInput.setVisible(true);
        }
    }

    public void isCek() {
        BtnSimpan.setEnabled(akses.gethemodialisa());
        BtnHapus.setEnabled(akses.gethemodialisa());
        BtnEdit.setEnabled(akses.gethemodialisa());
        BtnPrint.setEnabled(akses.gethemodialisa());
    }

    private void jam() {
        ActionListener taskPerformer = new ActionListener() {
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;

            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";

                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if (ChkKejadian.isSelected() == true) {
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                } else if (ChkKejadian.isSelected() == false) {
                    nilai_jam = Jam.getSelectedIndex();
                    nilai_menit = Menit.getSelectedIndex();
                    nilai_detik = Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }

    private void simpan() {
        switch (TabRawat.getSelectedIndex()) { 
            case 0:
                if (Sequel.menyimpantf("rencana_hd", "?,?,?,?,?,?", "Data", 6, new String[]{
                    TNoRw.getText(), 
                    TNoRM.getText(),
                    Valid.SetTgl(Tanggal.getSelectedItem() + ""),
                    Rencana.getText(), 
                    kddok1.getText(), 
                    kdpoli.getText()
                }) == true) {
                    tampilRencanaHD();
                    LCount.setText("" + tabRencanaHD.getRowCount());
                    emptTeks();
                }
                break;
            
            case 1:
                if (Sequel.menyimpantf("hemodialisa", "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?", "Data", 14, new String[]{
                    TNoRw.getText(), 
                    Valid.SetTgl(Tanggal.getSelectedItem() + ""),
                    kddok.getText(), 
                    kdpetugas.getText(), 
                    Durasi.getText(), 
                    BB_Pre.getText(), 
                    BB_Lalu.getText(), 
                    BB_Naik.getText(), 
                    BB_Post.getText(),
                    StatusDialiser.getSelectedItem().toString(), 
                    Dialiser.getText(), 
                    Vaskuler.getText(), 
                    Jadwal.getText(),
                    Hari.getText()
                }) == true) {
                    tampilDataHD();
                    emptTeks();
                }
                break;
            case 2:
                String poly = Poly.isSelected() ? "true" : "false";
                String ultra = Ultrafiltration.isSelected() ? "true" : "false";
                String asetat = Asetat.isSelected() ? "true" : "false";
                String bicarbonat = Bicarbonat.isSelected() ? "true" : "false";
                String program_bilas = ProgramBilas.isSelected() ? "true" : "false";
                
                if (Sequel.menyimpantf("laporan_tindakan_hemodialisa", "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?", "Data", 24, new String[]{
                    
                    TNoRw.getText(), 
                    Valid.SetTgl(Tanggal.getSelectedItem() + ""),
                    ResepHD.getSelectedItem().toString(), 
                    Time.getText(), 
                    QB.getText(),
                    
                    QD.getText(),
                    UG.getText(), 
                    UR.getText(),
                    poly,
                    DialiserLain.getText(),
                    
                    Natrium.getText(), 
                    ultra,
                    asetat,
                    bicarbonat,
                    Conductivity.getText(),
                    
                    Temperature.getText(),
                    DosisSirkulasi.getText(),
                    LMWH.getText(),
                    DosisAwal.getText(),
                    TanpaHeparin.getText(),
                    
                    Continous.getText(),
                    Intermitten.getText(),
                    program_bilas,
                    
                    UFH.getText()
                }) == true) {
                    tampilLaporanHD();
                    LCount.setText("" + tabLaporanHD.getRowCount());
                    emptTeks();
                }
                break;
            case 3: //sudah
                if (Sequel.menyimpantf("monitoring_hd", "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?", "Data", 15, new String[]{
                    TNoRw.getText(), 
                    Jam1.getSelectedItem() + ":" + Menit1.getSelectedItem() + ":" + Detik1.getSelectedItem(),
                    TD.getText(),
                    Qb.getText(),
                    VP.getText(),
                    TMP.getText(),
                    Suhu.getText(),
                    UFR.getText(),
                    Conductivity2.getText(), 
                    Lainlain.getText(),
                    NACL.getText(),
                    Dextrose.getText(),
                    MakMin.getText(),
                    InputLain.getText(),
                    TMP5.getText()
                }) == true) {
                    tampilMonitoringHD();
                    LCount.setText("" + tabMonitoringHD.getRowCount());
                    emptTeks();
                }
                break;
            case 4:  //udah bener
                String finger1 = finger.isSelected() ? "true" : "false";
                String tindakan2 = tindakan.isSelected() ? "true" : "false";
                String PilihanReuse3 = PilihanReuse.isSelected() ? "true" : "false";
                String Keperawatan4 = Keperawatan.isSelected() ? "true" : "false";
                String Edukasi5 = Edukasi.isSelected() ? "true" : "false";
                String Lain6 = Lain.isSelected() ? "true" : "false";
                
                if (Sequel.menyimpantf("bukti_layanan_hd", "?,?,?,?,?, ?,?,?,?,?, ?", "Data", 11, new String[]{
                    TNoRw.getText(),
                    finger1,
                    tindakan2,
                    PilihanReuse3,
                    Dialiser2.getText(),
                    Reuse.getText(),
                    Keperawatan4,
                    Edukasi5,
                    KeteranganEdukasi.getText(),
                    Lain6,
                    KeteranganLain.getText(),
                }) == true) {
                    tabLayananHD.addRow(new String[]{
                        TNoRw.getText(), 
                        TNoRM.getText(), 
                        TPasien.getText(), 
                        TglLahir.getText(), 
                        finger.getText(),
                        tindakan.getText(),
                        PilihanReuse.getText(),
                        Dialiser2.getText(),
                        Reuse.getText(),
                        Keperawatan.getText(),
                        Edukasi.getText(),
                        KeteranganEdukasi.getText(),
                        Lain.getText(),
                        KeteranganLain.getText()});
                    tampilLayananHD();
                    LCount.setText("" + tabLayananHD.getRowCount());
                    emptTeks();
                }
                break;
            default:
        }

    }

    private void hapus() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                if (Sequel.queryu2tf("delete from hemodialisa where tanggal=? and no_rawat=?", 2, new String[]{
                    tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 5).toString(), tbDataHD.getValueAt(tbDataHD.getSelectedRow(), 0).toString()
                }) == true) {
                    tabDataHD.removeRow(tbDataHD.getSelectedRow());
                    LCount.setText("" + tabDataHD.getRowCount());
                    emptTeks();
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 2:
                if (Sequel.queryu2tf("delete from laporan_tindakan_hemodialisa where no_rawat=?", 1, new String[]{
                    TNoRw.getText()
                }) == true) {
                    tabLaporanHD.removeRow(tbLaporanHD.getSelectedRow());
                    LCount.setText("" + tabLaporanHD.getRowCount());
                    emptTeks();
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 3:
                if (Sequel.queryu2tf("delete from monitoring_hd where no_rawat=?", 1, new String[]{
                    TNoRw.getText()
                }) == true) {
                    tabMonitoringHD.removeRow(tbMonitoringHD.getSelectedRow());
                    LCount.setText("" + tabMonitoringHD.getRowCount());
                    emptTeks();
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            case 4:
                 if (Sequel.queryu2tf("delete from bukti_layanan_hd where no_rawat=?", 1, new String[]{
                    TNoRw.getText()
                }) == true) {
                    tabLayananHD.removeRow(tbLayananHD.getSelectedRow());
                    LCount.setText("" + tabLayananHD.getRowCount());
                    emptTeks();
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                }
                break;
            default:

        }

    }

    private void ganti() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                if (Sequel.mengedittf("hemodialisa", "no_rawat=?", "tanggal=?,kd_dokter=?,kd_petugas=?,durasi=?,bb_pre=?,bb_lalu=?,bb_naik=?,bb_post=?,status_dialiser=?,penggunaan_dialiser=?,akses_vaskuler=?,jadwal=?,hari=?", 14, new String[]{
                    Valid.SetTgl(Tanggal.getSelectedItem() + ""),
                    kddok.getText(), 
                    kdpetugas.getText(), 
                    Durasi.getText(), 
                    BB_Pre.getText(), 
                    BB_Lalu.getText(), 
                    BB_Naik.getText(), 
                    BB_Post.getText(),
                    StatusDialiser.getSelectedItem().toString(), 
                    Dialiser.getText(), 
                    Vaskuler.getText(), 
                    Jadwal.getText(),
                    Hari.getText(),
                    TNoRw.getText()
                }) == true) {
                    tampilDataHD();
                    emptTeks();
                }
                break;
            case 2:
                break;
            case 3: //sudah
                Sequel.mengedit("monitoring_hd", "no_rawat=?", "jam=?,td=?,qb=?,vp=?,tmp=?,suhu=?,ufr=?,conductivity=?,lainlain=?,nacl=?,dextrose=?,makmin=?,inputlain=?,ufgoal=?", 15, new String[]{
                    Jam1.getSelectedItem() + ":" + Menit1.getSelectedItem() + ":" + Detik1.getSelectedItem(),
                    TD.getText(),
                    Qb.getText(), 
                    VP.getText(), 
                    TMP.getText(), 
                    Suhu.getText(),
                    UFR.getText(),
                    Conductivity2.getText(), 
                    Lainlain.getText(),
                    NACL.getText(),
                    Dextrose.getText(),
                    MakMin.getText(),
                    InputLain.getText(),
                    TMP5.getText(),
                    tbMonitoringHD.getValueAt(tbMonitoringHD.getSelectedRow(), 0).toString()
                });
                if (tabMonitoringHD.getRowCount() != 0) {
                    tampilMonitoringHD();
                }
                emptTeks();
                break;
            case 4:
                String finger1 = finger.isSelected() ? "true" : "false";
                String tindakan1 = tindakan.isSelected() ? "true" : "false";
                String PilihanReuse1 = PilihanReuse.isSelected() ? "true" : "false";
                String Keperawatan1 = Keperawatan.isSelected() ? "true" : "false";
                String Edukasi1 = Edukasi.isSelected() ? "true" : "false";
                String Lain1 = Lain.isSelected() ? "true" : "false";
                    
                Sequel.mengedit("bukti_layanan_hd", "no_rawat=?", "finger=?,tindakan_hd=?,tindakan_reuse=?,dialiser=?,reuse=?,keperawatan=?,edukasi=?,keterangan_edukasi=?,lain=?,ketengan_edukasi=?", 11, new String[]{
                    finger1,
                    tindakan1,
                    PilihanReuse1,
                    Dialiser2.getText(), 
                    Reuse.getText(), 
                    Keperawatan1,
                    Edukasi1,
                    KeteranganEdukasi.getText(), 
                    Lain1,
                    KeteranganLain.getText(),
                    TNoRw.getText()
                });
                if (tabLayananHD.getRowCount() != 0) {
                    tampilLayananHD();
                }
                emptTeks();
                break;
            default:
        }

    }

}

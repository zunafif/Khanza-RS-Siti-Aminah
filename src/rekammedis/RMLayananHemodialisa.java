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
import inventory.DlgCariAturanPakai;
import inventory.DlgResepObat;
import inventory.riwayatobat;
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
import simrskhanza.DlgCariBangsal;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.Document;
import javax.swing.event.HyperlinkEvent;
import java.awt.Desktop;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


/**
 *
 * @author perpustakaan
 */
public final class RMLayananHemodialisa extends javax.swing.JDialog {

    private final DefaultTableModel TabModeTindakan, tabMonitoringHD, tabDataLayananHD, tabLayananHD, tabModeDr, tabModePr, TabModeTindakan2, tabModeobat;
    private Connection koneksi = koneksiDB.condb();
    private boolean[] pilih,k; 
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private DlgCariPetugas petugas = new DlgCariPetugas(null, false);
    private PreparedStatement ps, ps2, psobat,pscarikapasitas,psstok;
    private String[] kode,nama,kategori;
    private boolean sukses=false;
    private ResultSet rs, rstindakan, rs2, rs3, rsobat, carikapasitas, rsstok;
    private int i = 0, pilihan = 0, jmlparsial=0,jml=0,index=0,tinggi=0, row=0, urut=0, urutdpjp=0,w=0,s=0,z=0,row2,r;
    private DlgCariDokter dokter = new DlgCariDokter(null, false);
    private DlgCariPenyakit penyakit = new DlgCariPenyakit(null, false);
    private DlgCariPoli poli=new DlgCariPoli(null,false);
    private DlgCariBangsal caribangsal=new DlgCariBangsal(null,false);
    public DlgCariAturanPakai aturanpakai=new DlgCariAturanPakai(null,false);
    private riwayatobat Trackobat=new riwayatobat();
    private double[] totaltnd,bagianrs,bhp,jmdokter,jmperawat,kso,menejemen;
    private PreparedStatement pstindakan;
    private String kodedokter="",VALIDASIULANGBERIOBAT="",namadokter="",kode_poli="",noresep="",kd_pj="",poli_ralan="No",cara_bayar_ralan="No",aktifkanparsial="no",dokterrujukan="",polirujukan="",aktifkanbatch="no",aktifpcare="no",sql="",hppfarmasi="",tampilkan_ppnobat_ralan;
    private double ttljmdokter=0,ttljmperawat=0,ttlkso=0,ttljasasarana=0,ttlbhp=0,ttlmenejemen=0,ttlpendapatan=0,
            embalase=Sequel.cariIsiAngka("select set_embalase.embalase_per_obat from set_embalase"),
            tuslah=Sequel.cariIsiAngka("select set_embalase.tuslah_per_obat from set_embalase"),
            kenaikan=0,stokbarang=0,ttl=0,ppnobat=0,ttlhpp,ttljual,y=0;
    private StringBuilder htmlContent;
    private double biayaperawatan=0;
    private GetMethod get;
    private HttpClient http = new HttpClient();
    private double[] jumlah,harga,eb,ts,stok,beli,kapasitas,kandungan;
    private String[] kodebarang,namabarang,kodesatuan,letakbarang,namajenis,aturan,industri,golongan,no,nobatch,nofaktur,kadaluarsa;

    /**
     * Creates new form DlgRujuk
     *
     * @param parent
     * @param modal
     */
    public RMLayananHemodialisa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(628, 674);
        
        try {
            VALIDASIULANGBERIOBAT=koneksiDB.VALIDASIULANGBERIOBAT();
        } catch (Exception e) {
            VALIDASIULANGBERIOBAT="no";
        }
        
        tampilkan_ppnobat_ralan=Sequel.cariIsi("select set_nota.tampilkan_ppnobat_ralan from set_nota"); 
        
        tabModeobat=new DefaultTableModel(null,new Object[]{
                "P","K","Jumlah","Kode Barang","Nama Barang","Satuan","Kandungan",
                "Harga(Rp)","Jenis Obat","Emb","Tsl","Stok","Aturan Pakai","I.F.",
                "H.Beli","Kategori","Golongan","No.Batch","No.Faktur","Kadaluarsa"
            }){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==0)||(colIndex==1)||(colIndex==2)||(colIndex==9)||(colIndex==10)||(colIndex==12)||(colIndex==17)||(colIndex==18)) {
                    a=true;
                }
                return a;
             }
            
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, 
                java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        
        tbObat.setModel(tabModeobat);
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 20; i++) {  // Dari 19 jadi 20 karena nambah 1 kolom
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);  // P - Pilihan (checkbox)
            }else if(i==1){
                column.setMinWidth(0);         // K (sebelumnya i==0)
                column.setMaxWidth(0);
            }else if(i==2){
                column.setPreferredWidth(45);  // Jumlah (sebelumnya i==1)
            }else if(i==3){
                column.setPreferredWidth(75);  // Kode Barang
            }else if(i==4){
                column.setPreferredWidth(200); // Nama Barang
            }else if(i==5){
                column.setPreferredWidth(45);  // Satuan
            }else if(i==6){
                column.setMinWidth(0);         // Kandungan
                column.setMaxWidth(0);
            }else if(i==7){
                column.setPreferredWidth(70);  // Harga(Rp)
            }else if(i==8){
                column.setPreferredWidth(85);  // Jenis Obat
            }else if(i==9){
                column.setPreferredWidth(40);  // Emb
            }else if(i==10){
                column.setPreferredWidth(40);  // Tsl
            }else if(i==11){
                column.setPreferredWidth(40);  // Stok
            }else if(i==12){
                column.setPreferredWidth(130); // Aturan Pakai
            }else if(i==13){
                column.setPreferredWidth(85);  // I.F.
            }else if(i==14){
                column.setMinWidth(0);         // H.Beli
                column.setMaxWidth(0);
            }else if(i==15){
                column.setPreferredWidth(85);  // Kategori
            }else if(i==16){
                column.setPreferredWidth(85);  // Golongan
            }else if(i==17){
                column.setPreferredWidth(70);  // No.Batch
            }else if(i==18){
                column.setPreferredWidth(100); // No.Faktur
            }else if(i==19){
                column.setPreferredWidth(65);  // Kadaluarsa
            }                 
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeTindakan=new DefaultTableModel(null,new Object[]{
            "P","Jumlah","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex == 0 || colIndex == 1) {
                    a=true;
                }
                return a;
             }
            Class[] types = new Class[] {
                java.lang.Boolean.class, // 0 - P
                java.lang.Object.class,  // 1 - Jumlah
                java.lang.Object.class,  // 2 - Kode
                java.lang.Object.class,  // 3 - Nama Perawatan
                java.lang.Object.class,  // 4 - Kategori Perawatan ← String, bukan Double!
                java.lang.Double.class,  // 5 - Tarif/Biaya
                java.lang.Double.class,  // 6 - Bagian RS
                java.lang.Double.class,  // 7 - BHP
                java.lang.Double.class,  // 8 - JM Dokter
                java.lang.Double.class,  // 9 - JM Perawat
                java.lang.Double.class,  // 10 - KSO
                java.lang.Double.class   // 11 - Menejemen ← TAMBAHKAN!
            };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan.setModel(TabModeTindakan);
        tbTindakan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            } else if(i==1){
                column.setPreferredWidth(90);
            } else if(i==2){
                column.setPreferredWidth(90);
            }else if(i==3){
                column.setPreferredWidth(420);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbTindakan.setDefaultRenderer(Object.class, new WarnaTable());
        
        TabModeTindakan2=new DefaultTableModel(null,new Object[]{"P","Jumlah","Kode","Nama Perawatan","Kategori Perawatan","Tarif/Biaya","Bagian RS","BHP","JM Dokter","JM Perawat","KSO","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex == 0 || colIndex == 1) {
                    a=true;
                }
                return a;
             }
             
            Class[] types = new Class[] {
                java.lang.Boolean.class,  // 0 - P
                java.lang.Integer.class,  // 1 - Jumlah ← Integer, user isi angka
                java.lang.Object.class,   // 2 - Kode
                java.lang.Object.class,   // 3 - Nama Perawatan
                java.lang.Object.class,   // 4 - Kategori Perawatan
                java.lang.Double.class,   // 5 - Tarif/Biaya
                java.lang.Double.class,   // 6 - Bagian RS
                java.lang.Double.class,   // 7 - BHP
                java.lang.Double.class,   // 8 - JM Dokter
                java.lang.Double.class,   // 9 - JM Perawat
                java.lang.Double.class,   // 10 - KSO
                java.lang.Double.class    // 11 - Menejemen
            };
            
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbTindakan2.setModel(TabModeTindakan2);
        tbTindakan2.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbTindakan2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbTindakan2.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            } else if(i==1){
                column.setPreferredWidth(50);
            } else if(i==2){
                column.setPreferredWidth(90);
            }else if(i==3){
                column.setPreferredWidth(420);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(150);
            }else if(i==6){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==7){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==8){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==9){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else{
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbTindakan2.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModeDr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","Kode Dokter","Dokter Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Dokter","KSO","Jasa Sarana","BHP","Menejemen"}){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatDr.setModel(tabModeDr);
        //tampilDr();

        tbRawatDr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatDr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbRawatDr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==14){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==15){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatDr.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabModePr=new DefaultTableModel(null,new Object[]{
            "P","No.Rawat","No.R.M.","Nama Pasien","Perawatan/Tindakan","NIP","Petugas Yg Menangani","Tgl.Rawat","Jam Rawat","Biaya","Kode","Tarif Perawat","KSO","Jasa Sarana","BHP","Menejemen"}){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbRawatPr.setModel(tabModePr);
        tbRawatPr.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbRawatPr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 16; i++) {
            TableColumn column = tbRawatPr.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(180);
            }else if(i==4){
                column.setPreferredWidth(180);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(180);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(75);
            }else if(i==9){
                column.setPreferredWidth(90);
            }else if(i==10){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==11){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==12){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==14){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==15){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbRawatPr.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabDataLayananHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat",
            "No.R.M.",
            "Nama Pasien",
            "Tanggal Lahir",
            "Tanggal",
            
            "Jam Mulai",
            "Jam Selesai",
            "Pelayanan HD",
            "Paket HD",
            "Akses Vaskuler",
            
            "Pelayanan",
            "Kode Dokter Spesialis",
            "Nama Dokter Spesialis",
            "Kode Dokter Umum",
            "Nama Dokter Umum"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbDataLayananHD.setModel(tabDataLayananHD);

        tbDataLayananHD.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDataLayananHD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 15; i++) {
            TableColumn column = tbDataLayananHD.getColumnModel().getColumn(i);  // <-- BENAR: tbDataLayananHD
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
               column.setPreferredWidth(150);
            } else if (i == 7) {
                column.setPreferredWidth(150);      // Dokter
            } else if (i == 8) {
                column.setPreferredWidth(150);
            } else if (i == 9) {
                column.setPreferredWidth(150);      // Poliklinik
            } else if (i == 10) {
                column.setPreferredWidth(150);      // Poliklinik
            } else if (i == 11) {
                column.setPreferredWidth(150);      // Poliklinik
            } else if (i == 12) {
                column.setPreferredWidth(150);      // Poliklinik
            } else if (i == 13) {
                column.setPreferredWidth(150);      // Poliklinik
            } else if (i == 14) {
                column.setPreferredWidth(150);      // Poliklinik
            }
        }
        tbDataLayananHD.setDefaultRenderer(Object.class, new WarnaTable());
        
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
            "Lain-lain"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; // Non-editable table
            }
        };

        tabLayananHD = new DefaultTableModel(null, new Object[]{
            "No.Rawat",
            "No.R.M.", 
            "Nama Pasien", 
            "Tanggal Lahir", 
            "Terapi Obat / Lain-lain"
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
                    column.setPreferredWidth(250);  // Tanggal
                    break;
                default:
                    column.setPreferredWidth(50);   // Default width for undefined columns

            }
        }

        tbLayananHD.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte) 17).getKata(TNoRw));
        TCari.setDocument(new batasInput((int) 100).getKata(TCari));

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
                        KdDok.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        TDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    } else if (pilihan == 2) {
                        kddok1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        namadokter1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    } else if (pilihan == 3) {
                        kddok2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        namadokter2.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    }  else if (pilihan == 4) {
                        KdDok1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 0).toString());
                        TDokter1.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
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
        
        poli.addWindowListener(new WindowListener() {
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
        
        caribangsal.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(caribangsal.getTable().getSelectedRow()!= -1){                   
                    kdgudang.setText(caribangsal.getTable().getValueAt(caribangsal.getTable().getSelectedRow(),0).toString());
                    nmgudang.setText(caribangsal.getTable().getValueAt(caribangsal.getTable().getSelectedRow(),1).toString());
                } 
                kdgudang.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
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
                    kdptg.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 0).toString());
                    TPerawat.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(), 1).toString());
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
        isForm();

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
        tbDataLayananHD = new widget.Table();
        PanelInput1 = new javax.swing.JPanel();
        FormInput1 = new widget.PanelBiasa();
        jLabel23 = new widget.Label();
        kddok1 = new widget.TextBox();
        namadokter1 = new widget.TextBox();
        btnDokter1 = new widget.Button();
        jLabel45 = new widget.Label();
        jLabel47 = new widget.Label();
        Tanggal2 = new widget.Tanggal();
        jLabel50 = new widget.Label();
        jLabel24 = new widget.Label();
        kddok2 = new widget.TextBox();
        namadokter2 = new widget.TextBox();
        btnDokter2 = new widget.Button();
        jLabel51 = new widget.Label();
        Jam2 = new widget.ComboBox();
        Menit2 = new widget.ComboBox();
        Detik2 = new widget.ComboBox();
        jLabel52 = new widget.Label();
        Detik3 = new widget.ComboBox();
        Menit3 = new widget.ComboBox();
        Jam3 = new widget.ComboBox();
        Pelayanan = new widget.ComboBox();
        jLabel54 = new widget.Label();
        PelayananHD = new widget.ComboBox();
        jLabel55 = new widget.Label();
        PaketHD = new widget.ComboBox();
        jLabel56 = new widget.Label();
        AksesVaskuler = new widget.ComboBox();
        ChkInput1 = new widget.CekBox();
        internalFrame2 = new widget.InternalFrame();
        panelGlass7 = new widget.panelisi();
        jLabel10 = new widget.Label();
        KdDok = new widget.TextBox();
        BtnSeekDokter = new widget.Button();
        TDokter = new widget.TextBox();
        jLabel11 = new widget.Label();
        KdDok1 = new widget.TextBox();
        TDokter1 = new widget.TextBox();
        BtnSeekDokter1 = new widget.Button();
        TabRawatTindakanDokter = new javax.swing.JTabbedPane();
        Scroll6 = new widget.ScrollPane();
        tbTindakan = new widget.Table();
        Scroll = new widget.ScrollPane();
        tbRawatDr = new widget.Table();
        internalFrame7 = new widget.InternalFrame();
        panelGlass10 = new widget.panelisi();
        jLabel13 = new widget.Label();
        kdptg = new widget.TextBox();
        TPerawat = new widget.TextBox();
        BtnSeekPetugas = new widget.Button();
        TabRawatTindakanPetugas = new javax.swing.JTabbedPane();
        Scroll7 = new widget.ScrollPane();
        tbTindakan2 = new widget.Table();
        Scroll8 = new widget.ScrollPane();
        tbRawatPr = new widget.Table();
        internalFrame8 = new widget.InternalFrame();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        TCari1 = new widget.TextBox();
        BtnCari1 = new widget.Button();
        BtnAll1 = new widget.Button();
        FormInput = new widget.PanelBiasa();
        label21 = new widget.Label();
        kdgudang = new widget.TextBox();
        nmgudang = new widget.TextBox();
        BtnGudang = new widget.Button();
        NoResep = new widget.TextBox();
        jLabel26 = new widget.Label();
        label12 = new widget.Label();
        Jeniskelas = new widget.ComboBox();
        ChkNoResep = new widget.CekBox();
        jLabel5 = new widget.Label();
        LTotal = new widget.Label();
        jLabel8 = new widget.Label();
        LPpn = new widget.Label();
        jLabel9 = new widget.Label();
        LTotalTagihan = new widget.Label();
        TabRawat1 = new javax.swing.JTabbedPane();
        Scroll5 = new widget.ScrollPane();
        tbObat = new widget.Table();
        internalFrame9 = new widget.InternalFrame();
        Scroll11 = new widget.ScrollPane();
        LoadHTMLKPO = new widget.editorpane();
        panelGlass5 = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        R4 = new widget.RadioButton();
        NoRawat = new widget.TextBox();
        BtnCari2 = new widget.Button();
        label19 = new widget.Label();
        BtnPrint1 = new widget.Button();
        BtnKeluar2 = new widget.Button();
        internalFrame6 = new widget.InternalFrame();
        Scroll4 = new widget.ScrollPane();
        tbLayananHD = new widget.Table();
        PanelInput4 = new javax.swing.JPanel();
        FormInput4 = new widget.PanelBiasa();
        jLabel1 = new javax.swing.JLabel();
        scrollPane2 = new widget.ScrollPane();
        TerapiLain = new widget.TextArea();
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
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setLayout(new java.awt.BorderLayout());

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);
        Scroll1.setPreferredSize(new java.awt.Dimension(452, 200));

        tbDataLayananHD.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbDataLayananHD.setName("tbDataLayananHD"); // NOI18N
        tbDataLayananHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataLayananHDMouseClicked(evt);
            }
        });
        tbDataLayananHD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDataLayananHDKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbDataLayananHD);

        internalFrame4.add(Scroll1, java.awt.BorderLayout.CENTER);

        PanelInput1.setName("PanelInput1"); // NOI18N
        PanelInput1.setOpaque(false);
        PanelInput1.setPreferredSize(new java.awt.Dimension(192, 150));
        PanelInput1.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput1.setBackground(new java.awt.Color(250, 255, 245));
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(100, 225));
        FormInput1.setLayout(null);

        jLabel23.setText("Dokter Umum :");
        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput1.add(jLabel23);
        jLabel23.setBounds(520, 90, 90, 23);

        kddok1.setEditable(false);
        kddok1.setHighlighter(null);
        kddok1.setName("kddok1"); // NOI18N
        kddok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kddok1KeyPressed(evt);
            }
        });
        FormInput1.add(kddok1);
        kddok1.setBounds(620, 90, 110, 23);

        namadokter1.setEditable(false);
        namadokter1.setName("namadokter1"); // NOI18N
        FormInput1.add(namadokter1);
        namadokter1.setBounds(730, 90, 220, 23);

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
        btnDokter1.setBounds(950, 90, 20, 23);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel45.setText("Pelayanan :");
        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel45.setName("jLabel45"); // NOI18N
        FormInput1.add(jLabel45);
        jLabel45.setBounds(20, 50, 70, 23);

        jLabel47.setText("s/d");
        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel47.setName("jLabel47"); // NOI18N
        jLabel47.setVerifyInputWhenFocusTarget(false);
        FormInput1.add(jLabel47);
        jLabel47.setBounds(500, 10, 20, 23);

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

        jLabel50.setName("jLabel50"); // NOI18N
        FormInput1.add(jLabel50);
        jLabel50.setBounds(390, 140, 70, 23);

        jLabel24.setText("Dokter Spesialis :");
        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput1.add(jLabel24);
        jLabel24.setBounds(20, 90, 100, 23);

        kddok2.setEditable(false);
        kddok2.setHighlighter(null);
        kddok2.setName("kddok2"); // NOI18N
        kddok2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kddok2KeyPressed(evt);
            }
        });
        FormInput1.add(kddok2);
        kddok2.setBounds(130, 90, 110, 23);

        namadokter2.setEditable(false);
        namadokter2.setName("namadokter2"); // NOI18N
        FormInput1.add(namadokter2);
        namadokter2.setBounds(240, 90, 220, 23);

        btnDokter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnDokter2.setMnemonic('2');
        btnDokter2.setToolTipText("ALt+2");
        btnDokter2.setName("btnDokter2"); // NOI18N
        btnDokter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDokter2ActionPerformed(evt);
            }
        });
        btnDokter2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDokter2KeyPressed(evt);
            }
        });
        FormInput1.add(btnDokter2);
        btnDokter2.setBounds(460, 90, 20, 23);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel51.setText("Tanggal  :");
        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel51.setName("jLabel51"); // NOI18N
        jLabel51.setVerifyInputWhenFocusTarget(false);
        FormInput1.add(jLabel51);
        jLabel51.setBounds(20, 10, 60, 23);

        Jam2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam2.setName("Jam2"); // NOI18N
        Jam2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Jam2KeyPressed(evt);
            }
        });
        FormInput1.add(Jam2);
        Jam2.setBounds(290, 10, 62, 23);

        Menit2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit2.setName("Menit2"); // NOI18N
        Menit2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Menit2KeyPressed(evt);
            }
        });
        FormInput1.add(Menit2);
        Menit2.setBounds(360, 10, 62, 23);

        Detik2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik2.setName("Detik2"); // NOI18N
        Detik2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Detik2KeyPressed(evt);
            }
        });
        FormInput1.add(Detik2);
        Detik2.setBounds(430, 10, 62, 23);

        jLabel52.setText("Pukul :");
        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel52.setName("jLabel52"); // NOI18N
        jLabel52.setVerifyInputWhenFocusTarget(false);
        FormInput1.add(jLabel52);
        jLabel52.setBounds(230, 10, 50, 23);

        Detik3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik3.setName("Detik3"); // NOI18N
        Detik3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Detik3KeyPressed(evt);
            }
        });
        FormInput1.add(Detik3);
        Detik3.setBounds(670, 10, 62, 23);

        Menit3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit3.setName("Menit3"); // NOI18N
        Menit3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Menit3KeyPressed(evt);
            }
        });
        FormInput1.add(Menit3);
        Menit3.setBounds(600, 10, 62, 23);

        Jam3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam3.setName("Jam3"); // NOI18N
        Jam3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Jam3KeyPressed(evt);
            }
        });
        FormInput1.add(Jam3);
        Jam3.setBounds(530, 10, 62, 23);

        Pelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rajal", "Cito" }));
        Pelayanan.setName("Pelayanan"); // NOI18N
        FormInput1.add(Pelayanan);
        Pelayanan.setBounds(90, 50, 70, 20);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel54.setText("Pelayanan HD :");
        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput1.add(jLabel54);
        jLabel54.setBounds(690, 50, 90, 23);

        PelayananHD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "P", "S", "M" }));
        PelayananHD.setName("PelayananHD"); // NOI18N
        FormInput1.add(PelayananHD);
        PelayananHD.setBounds(790, 50, 40, 20);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("Paket HD :");
        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput1.add(jLabel55);
        jLabel55.setBounds(200, 50, 60, 23);

        PaketHD.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Komplit", "Tidak Komplit" }));
        PaketHD.setName("PaketHD"); // NOI18N
        FormInput1.add(PaketHD);
        PaketHD.setBounds(270, 50, 100, 20);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel56.setText("Akses Vaskuler :");
        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput1.add(jLabel56);
        jLabel56.setBounds(420, 50, 100, 23);

        AksesVaskuler.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Double Lumen", "Av Shunt", "Femoral" }));
        AksesVaskuler.setName("AksesVaskuler"); // NOI18N
        FormInput1.add(AksesVaskuler);
        AksesVaskuler.setBounds(520, 50, 110, 20);

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

        TabRawat.addTab("Data Layanan HD", internalFrame4);

        internalFrame2.setName("internalFrame2"); // NOI18N
        internalFrame2.setLayout(new java.awt.BorderLayout());

        panelGlass7.setBorder(null);
        panelGlass7.setName("panelGlass7"); // NOI18N
        panelGlass7.setPreferredSize(new java.awt.Dimension(44, 80));
        panelGlass7.setLayout(null);

        jLabel10.setText("Dokter Spesialis:");
        jLabel10.setName("jLabel10"); // NOI18N
        panelGlass7.add(jLabel10);
        jLabel10.setBounds(20, 10, 90, 23);

        KdDok.setHighlighter(null);
        KdDok.setName("KdDok"); // NOI18N
        KdDok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDokKeyPressed(evt);
            }
        });
        panelGlass7.add(KdDok);
        KdDok.setBounds(130, 10, 146, 23);

        BtnSeekDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekDokter.setMnemonic('4');
        BtnSeekDokter.setToolTipText("ALt+4");
        BtnSeekDokter.setName("BtnSeekDokter"); // NOI18N
        BtnSeekDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekDokterActionPerformed(evt);
            }
        });
        panelGlass7.add(BtnSeekDokter);
        BtnSeekDokter.setBounds(820, 10, 28, 23);

        TDokter.setEditable(false);
        TDokter.setHighlighter(null);
        TDokter.setName("TDokter"); // NOI18N
        panelGlass7.add(TDokter);
        TDokter.setBounds(280, 10, 540, 23);

        jLabel11.setText("Dokter Umum :");
        jLabel11.setName("jLabel11"); // NOI18N
        panelGlass7.add(jLabel11);
        jLabel11.setBounds(20, 40, 90, 23);

        KdDok1.setHighlighter(null);
        KdDok1.setName("KdDok1"); // NOI18N
        KdDok1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdDok1KeyPressed(evt);
            }
        });
        panelGlass7.add(KdDok1);
        KdDok1.setBounds(130, 40, 146, 23);

        TDokter1.setEditable(false);
        TDokter1.setHighlighter(null);
        TDokter1.setName("TDokter1"); // NOI18N
        panelGlass7.add(TDokter1);
        TDokter1.setBounds(280, 40, 540, 23);

        BtnSeekDokter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekDokter1.setMnemonic('4');
        BtnSeekDokter1.setToolTipText("ALt+4");
        BtnSeekDokter1.setName("BtnSeekDokter1"); // NOI18N
        BtnSeekDokter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekDokter1ActionPerformed(evt);
            }
        });
        panelGlass7.add(BtnSeekDokter1);
        BtnSeekDokter1.setBounds(820, 40, 28, 23);

        internalFrame2.add(panelGlass7, java.awt.BorderLayout.PAGE_START);

        TabRawatTindakanDokter.setBackground(new java.awt.Color(255, 255, 253));
        TabRawatTindakanDokter.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatTindakanDokter.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawatTindakanDokter.setName("TabRawatTindakanDokter"); // NOI18N
        TabRawatTindakanDokter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatTindakanDokterMouseClicked(evt);
            }
        });

        Scroll6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbTindakan.setToolTipText("");
        tbTindakan.setName("tbTindakan"); // NOI18N
        tbTindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbTindakanKeyPressed(evt);
            }
        });
        Scroll6.setViewportView(tbTindakan);

        TabRawatTindakanDokter.addTab("Daftar Tindakan/Tagihan", Scroll6);

        Scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbRawatDr.setName("tbRawatDr"); // NOI18N
        tbRawatDr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRawatDrMouseClicked(evt);
            }
        });
        tbRawatDr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRawatDrKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbRawatDr);

        TabRawatTindakanDokter.addTab("Tindakan Dilakukan", Scroll);

        internalFrame2.add(TabRawatTindakanDokter, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Pemeriksaan Dokter", internalFrame2);

        internalFrame7.setName("internalFrame7"); // NOI18N
        internalFrame7.setLayout(new java.awt.BorderLayout());

        panelGlass10.setBorder(null);
        panelGlass10.setName("panelGlass10"); // NOI18N
        panelGlass10.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass10.setLayout(null);

        jLabel13.setText("Petugas :");
        jLabel13.setName("jLabel13"); // NOI18N
        panelGlass10.add(jLabel13);
        jLabel13.setBounds(0, 10, 63, 23);

        kdptg.setHighlighter(null);
        kdptg.setName("kdptg"); // NOI18N
        kdptg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdptgKeyPressed(evt);
            }
        });
        panelGlass10.add(kdptg);
        kdptg.setBounds(66, 10, 146, 23);

        TPerawat.setEditable(false);
        TPerawat.setBackground(new java.awt.Color(202, 202, 202));
        TPerawat.setHighlighter(null);
        TPerawat.setName("TPerawat"); // NOI18N
        panelGlass10.add(TPerawat);
        TPerawat.setBounds(214, 10, 532, 23);

        BtnSeekPetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPetugas.setMnemonic('5');
        BtnSeekPetugas.setToolTipText("ALt+5");
        BtnSeekPetugas.setName("BtnSeekPetugas"); // NOI18N
        BtnSeekPetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPetugasActionPerformed(evt);
            }
        });
        panelGlass10.add(BtnSeekPetugas);
        BtnSeekPetugas.setBounds(749, 10, 28, 23);

        internalFrame7.add(panelGlass10, java.awt.BorderLayout.PAGE_START);

        TabRawatTindakanPetugas.setBackground(new java.awt.Color(255, 255, 253));
        TabRawatTindakanPetugas.setForeground(new java.awt.Color(50, 50, 50));
        TabRawatTindakanPetugas.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawatTindakanPetugas.setName("TabRawatTindakanPetugas"); // NOI18N
        TabRawatTindakanPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatTindakanPetugasMouseClicked(evt);
            }
        });

        Scroll7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll7.setName("Scroll7"); // NOI18N
        Scroll7.setOpaque(true);

        tbTindakan2.setToolTipText("");
        tbTindakan2.setName("tbTindakan2"); // NOI18N
        tbTindakan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbTindakan2KeyPressed(evt);
            }
        });
        Scroll7.setViewportView(tbTindakan2);

        TabRawatTindakanPetugas.addTab("Daftar Tindakan/Tagihan", Scroll7);

        Scroll8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll8.setName("Scroll8"); // NOI18N
        Scroll8.setOpaque(true);

        tbRawatPr.setName("tbRawatPr"); // NOI18N
        tbRawatPr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRawatPrMouseClicked(evt);
            }
        });
        tbRawatPr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRawatPrKeyReleased(evt);
            }
        });
        Scroll8.setViewportView(tbRawatPr);

        TabRawatTindakanPetugas.addTab("Tindakan Dilakukan", Scroll8);

        internalFrame7.add(TabRawatTindakanPetugas, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Tindakan Perawat", internalFrame7);

        internalFrame8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame8.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label9.setText("Key Word Obat :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi3.add(label9);

        TCari1.setToolTipText("Alt+C");
        TCari1.setName("TCari1"); // NOI18N
        TCari1.setPreferredSize(new java.awt.Dimension(340, 23));
        TCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCari1KeyPressed(evt);
            }
        });
        panelisi3.add(TCari1);

        BtnCari1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari1.setMnemonic('1');
        BtnCari1.setToolTipText("Alt+1");
        BtnCari1.setName("BtnCari1"); // NOI18N
        BtnCari1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari1ActionPerformed(evt);
            }
        });
        BtnCari1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari1KeyPressed(evt);
            }
        });
        panelisi3.add(BtnCari1);

        BtnAll1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll1.setMnemonic('2');
        BtnAll1.setToolTipText("Alt+2");
        BtnAll1.setName("BtnAll1"); // NOI18N
        BtnAll1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAll1ActionPerformed(evt);
            }
        });
        BtnAll1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAll1KeyPressed(evt);
            }
        });
        panelisi3.add(BtnAll1);

        internalFrame8.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        FormInput.setBackground(new java.awt.Color(215, 225, 215));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 44));
        FormInput.setLayout(null);

        label21.setText("Depo :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label21);
        label21.setBounds(310, 10, 50, 23);

        kdgudang.setEditable(false);
        kdgudang.setName("kdgudang"); // NOI18N
        kdgudang.setPreferredSize(new java.awt.Dimension(80, 23));
        kdgudang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdgudangKeyPressed(evt);
            }
        });
        FormInput.add(kdgudang);
        kdgudang.setBounds(370, 10, 55, 23);

        nmgudang.setEditable(false);
        nmgudang.setName("nmgudang"); // NOI18N
        nmgudang.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(nmgudang);
        nmgudang.setBounds(430, 10, 150, 23);

        BtnGudang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnGudang.setMnemonic('2');
        BtnGudang.setToolTipText("Alt+2");
        BtnGudang.setName("BtnGudang"); // NOI18N
        BtnGudang.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnGudang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGudangActionPerformed(evt);
            }
        });
        FormInput.add(BtnGudang);
        BtnGudang.setBounds(580, 10, 28, 23);

        NoResep.setEditable(false);
        NoResep.setName("NoResep"); // NOI18N
        NoResep.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NoResep);
        NoResep.setBounds(90, 10, 110, 23);

        jLabel26.setText("No: Resep:");
        jLabel26.setName("jLabel26"); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(68, 23));
        FormInput.add(jLabel26);
        jLabel26.setBounds(20, 10, 60, 23);

        label12.setText("Tarif :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(label12);
        label12.setBounds(610, 10, 50, 20);

        Jeniskelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rawat Jalan", "Beli Luar", "Karyawan", "Utama/BPJS" }));
        Jeniskelas.setName("Jeniskelas"); // NOI18N
        Jeniskelas.setPreferredSize(new java.awt.Dimension(100, 23));
        Jeniskelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JeniskelasItemStateChanged(evt);
            }
        });
        Jeniskelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JeniskelasKeyPressed(evt);
            }
        });
        FormInput.add(Jeniskelas);
        Jeniskelas.setBounds(670, 10, 150, 20);

        ChkNoResep.setSelected(true);
        ChkNoResep.setText("No.Resep   ");
        ChkNoResep.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkNoResep.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkNoResep.setName("ChkNoResep"); // NOI18N
        ChkNoResep.setOpaque(false);
        ChkNoResep.setPreferredSize(new java.awt.Dimension(85, 23));
        ChkNoResep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkNoResepItemStateChanged(evt);
            }
        });
        FormInput.add(ChkNoResep);
        ChkNoResep.setBounds(210, 10, 80, 20);

        jLabel5.setText("Total :");
        jLabel5.setName("jLabel5"); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(45, 23));
        FormInput.add(jLabel5);
        jLabel5.setBounds(820, 10, 65, 23);

        LTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LTotal.setText("0");
        LTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LTotal.setName("LTotal"); // NOI18N
        LTotal.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(LTotal);
        LTotal.setBounds(890, 10, 80, 23);

        jLabel8.setText("PPN :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(35, 23));
        FormInput.add(jLabel8);
        jLabel8.setBounds(960, 10, 35, 23);

        LPpn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LPpn.setText("0");
        LPpn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LPpn.setName("LPpn"); // NOI18N
        LPpn.setPreferredSize(new java.awt.Dimension(65, 23));
        FormInput.add(LPpn);
        LPpn.setBounds(1000, 10, 65, 23);

        jLabel9.setText("Total+PPN :");
        jLabel9.setName("jLabel9"); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(65, 23));
        FormInput.add(jLabel9);
        jLabel9.setBounds(1070, 10, 65, 23);

        LTotalTagihan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LTotalTagihan.setText("0");
        LTotalTagihan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LTotalTagihan.setName("LTotalTagihan"); // NOI18N
        LTotalTagihan.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(LTotalTagihan);
        LTotalTagihan.setBounds(1140, 10, 70, 23);

        internalFrame8.add(FormInput, java.awt.BorderLayout.PAGE_START);

        TabRawat1.setBackground(new java.awt.Color(255, 255, 253));
        TabRawat1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat1.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat1.setName("TabRawat1"); // NOI18N
        TabRawat1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawat1MouseClicked(evt);
            }
        });

        Scroll5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbObatPropertyChange(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll5.setViewportView(tbObat);

        TabRawat1.addTab("Umum", Scroll5);

        internalFrame9.setBackground(new java.awt.Color(235, 255, 235));
        internalFrame9.setBorder(null);
        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll11.setName("Scroll11"); // NOI18N
        Scroll11.setOpaque(true);

        LoadHTMLKPO.setBorder(null);
        LoadHTMLKPO.setName("LoadHTMLKPO"); // NOI18N
        Scroll11.setViewportView(LoadHTMLKPO);

        internalFrame9.add(Scroll11, java.awt.BorderLayout.CENTER);

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        R1.setSelected(true);
        R1.setText("5 Riwayat Terakhir");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass5.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        R2.setText("Semua Riwayat");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(104, 23));
        panelGlass5.add(R2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        R3.setText("Tanggal :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass5.add(R3);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        R4.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        R4.setText("Nomor :");
        R4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass5.add(R4);

        NoRawat.setName("NoRawat"); // NOI18N
        NoRawat.setPreferredSize(new java.awt.Dimension(135, 23));
        NoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRawatKeyPressed(evt);
            }
        });
        panelGlass5.add(NoRawat);

        BtnCari2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari2.setMnemonic('2');
        BtnCari2.setToolTipText("Alt+2");
        BtnCari2.setName("BtnCari2"); // NOI18N
        BtnCari2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari2ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnCari2);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass5.add(label19);

        BtnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint1.setMnemonic('T');
        BtnPrint1.setToolTipText("Alt+T");
        BtnPrint1.setName("BtnPrint1"); // NOI18N
        BtnPrint1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrint1ActionPerformed(evt);
            }
        });
        panelGlass5.add(BtnPrint1);

        BtnKeluar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar2.setMnemonic('K');
        BtnKeluar2.setToolTipText("Alt+K");
        BtnKeluar2.setName("BtnKeluar2"); // NOI18N
        BtnKeluar2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKeluar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar2ActionPerformed(evt);
            }
        });
        BtnKeluar2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar2KeyPressed(evt);
            }
        });
        panelGlass5.add(BtnKeluar2);

        internalFrame9.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat1.addTab("Riwayat Pengobatan", internalFrame9);

        internalFrame8.add(TabRawat1, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Penggunaan BHP", internalFrame8);

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
        PanelInput4.setPreferredSize(new java.awt.Dimension(192, 170));
        PanelInput4.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput4.setBackground(new java.awt.Color(250, 255, 245));
        FormInput4.setName("FormInput4"); // NOI18N
        FormInput4.setPreferredSize(new java.awt.Dimension(100, 150));
        FormInput4.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Terapi Obat/Lain-lain : ");
        jLabel1.setName("jLabel1"); // NOI18N
        FormInput4.add(jLabel1);
        jLabel1.setBounds(30, 20, 220, 15);

        scrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane2.setName("scrollPane2"); // NOI18N

        TerapiLain.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TerapiLain.setColumns(20);
        TerapiLain.setRows(5);
        TerapiLain.setName("TerapiLain"); // NOI18N
        TerapiLain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TerapiLainKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(TerapiLain);

        FormInput4.add(scrollPane2);
        scrollPane2.setBounds(30, 40, 610, 90);

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

        TabRawat.addTab("Terapi Obat/Lain-lain", internalFrame6);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);
        TabRawat.getAccessibleContext().setAccessibleName("Terapi Obat Lain");

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
                } else {
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
                if (tbDataLayananHD.getSelectedRow() != -1) {
                    if (Sequel.queryu2tf("delete from pelayanan_hemodialisa where no_rawat=?", 1, new String[]{
                        tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 0).toString()
                    }) == true) {
                        tabDataLayananHD.removeRow(tbDataLayananHD.getSelectedRow());
                        LCount.setText("" + tabDataLayananHD.getRowCount());
                        emptTeks();
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
                    }
                }
                break;
            case 1:
                break;
            case 2:
                hapus();
                break;
            case 3: //ini sudah 
                hapus();
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
                } else if (kddok1.getText().trim().equals("") || namadokter1.getText().trim().equals("")) {
                    Valid.textKosong(kddok1, "Dokter");
                } else {
                    Sequel.mengedit("pelayanan_hemodialisa", "no_rawat=?", "tanggal=?,"
                            + "jam_mulai=?,"
                            + "jam_selesai=?,"
                            + "layanan_hd=?,"
                            + "paket=?,"
                            + "vakuler=?,"
                            + "pelayanan=?,"
                            + "kd_dokter_spesialis=?,"
                            + "kd_dokter_umum=?", 10, new String[]{
                        Valid.SetTgl(Tanggal2.getSelectedItem() + ""),
                        Jam2.getSelectedItem().toString() + ":" + Menit2.getSelectedItem().toString() + ":" + Detik2.getSelectedItem().toString(),
                        Jam3.getSelectedItem().toString() + ":" + Menit3.getSelectedItem().toString() + ":" + Detik3.getSelectedItem().toString(),
                        Pelayanan.getSelectedItem().toString(),
                        PaketHD.getSelectedItem().toString(),
                        AksesVaskuler.getSelectedItem().toString(),
                        PelayananHD.getSelectedItem().toString(),
                        kddok2.getText(), 
                        kddok1.getText(), 
                        TNoRw.getText()
                    });
                    if (tabDataLayananHD.getRowCount() != 0) {
                        tampilDataLayananHD();
                    }
                    emptTeks();
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3: //sudah
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
                tampilDataLayananHD();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                Valid.tabelKosong(tabModeobat);
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

}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed

}//GEN-LAST:event_BtnAllKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah(evt, TCari, Jam);
}//GEN-LAST:event_TanggalKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt, Tanggal, Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt, Jam, Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed

    }//GEN-LAST:event_DetikKeyPressed

    private void tbDataLayananHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataLayananHDMouseClicked
        if (tabDataLayananHD.getRowCount() != 0) {
            try {
                getDataPelayananHD();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbDataLayananHDMouseClicked

    private void tbDataLayananHDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDataLayananHDKeyPressed

    }//GEN-LAST:event_tbDataLayananHDKeyPressed

    private void kddok1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddok1KeyPressed

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

    }//GEN-LAST:event_btnDokter1KeyPressed

    private void Tanggal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tanggal2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Tanggal2KeyPressed

    private void ChkInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInput1ActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInput1ActionPerformed

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

    private void kddok2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kddok2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kddok2KeyPressed

    private void btnDokter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDokter2ActionPerformed
        pilihan = 3;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);        
    }//GEN-LAST:event_btnDokter2ActionPerformed

    private void btnDokter2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDokter2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDokter2KeyPressed

    private void Jam2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Jam2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jam2KeyPressed

    private void Menit2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Menit2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menit2KeyPressed

    private void Detik2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Detik2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Detik2KeyPressed

    private void Detik3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Detik3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Detik3KeyPressed

    private void Menit3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Menit3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Menit3KeyPressed

    private void Jam3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Jam3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Jam3KeyPressed

    private void KdDokKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDokKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            TDokter.setText(dokter.tampil3(KdDok.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekDokterActionPerformed(null);
        }else{
            Valid.pindah(evt,TNoRw,BtnSeekDokter);
        }
    }//GEN-LAST:event_KdDokKeyPressed

    private void BtnSeekDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokterActionPerformed
        pilihan = 1;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeekDokterActionPerformed

    private void tbTindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakanKeyPressed
        if(tbTindakan.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan.getSelectedRow()>-1){
                            tbTindakan.setValueAt(true,tbTindakan.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakanKeyPressed

    private void tbRawatDrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatDrMouseClicked

    }//GEN-LAST:event_tbRawatDrMouseClicked

    private void tbRawatDrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatDrKeyReleased

    }//GEN-LAST:event_tbRawatDrKeyReleased

    private void TabRawatTindakanDokterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatTindakanDokterMouseClicked
        if(TabRawatTindakanDokter.getSelectedIndex()==0){
            TCari.setText("");
        }else if(TabRawatTindakanDokter.getSelectedIndex()==1){
            TCari.setText("");
        }
        TCari.requestFocus();
        tampilkanPenangananDokter();
    }//GEN-LAST:event_TabRawatTindakanDokterMouseClicked

    private void KdDok1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdDok1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdDok1KeyPressed

    private void BtnSeekDokter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekDokter1ActionPerformed
        pilihan = 4;
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnSeekDokter1ActionPerformed

    private void tbTindakan2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbTindakan2KeyPressed
        if(tbTindakan2.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    i=tbTindakan2.getSelectedColumn();
                    if(i==1){
                        if(tbTindakan2.getSelectedRow()>-1){
                            tbTindakan2.setValueAt(true,tbTindakan2.getSelectedRow(),0);
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }
        }
    }//GEN-LAST:event_tbTindakan2KeyPressed

    private void tbRawatPrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRawatPrMouseClicked
        if(tabModePr.getRowCount()!=0){
            try {
                getDataPr();
            } catch (java.lang.NullPointerException e) {
            }

        }
    }//GEN-LAST:event_tbRawatPrMouseClicked

    private void tbRawatPrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRawatPrKeyReleased
        if(tabModePr.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataPr();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbRawatPrKeyReleased

    private void TabRawatTindakanPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatTindakanPetugasMouseClicked
        if(TabRawatTindakanPetugas.getSelectedIndex()==0){
            TCari.setText("");
        }else if(TabRawatTindakanPetugas.getSelectedIndex()==1){
            TCari.setText("");
            TCari.setText(TNoRM.getText());
        }
        TCari.requestFocus();
        tampilkanPenangananPetugas();
    }//GEN-LAST:event_TabRawatTindakanPetugasMouseClicked

    private void kdptgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdptgKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            TPerawat.setText(petugas.tampil3(kdptg.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPetugasActionPerformed(null);
        }else{
            Valid.pindah(evt,TNoRw,BtnSeekPetugas);
        }
    }//GEN-LAST:event_kdptgKeyPressed

    private void BtnSeekPetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPetugasActionPerformed
        akses.setform("DlgRawatJalan");
        petugas.emptTeks();
        petugas.isCek();
        petugas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        petugas.setLocationRelativeTo(internalFrame1);
        petugas.setVisible(true);
    }//GEN-LAST:event_BtnSeekPetugasActionPerformed

    private void TCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCari1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbObat.requestFocus();
        }
    }//GEN-LAST:event_TCari1KeyPressed

    private void BtnCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari1ActionPerformed
        tampilobat();
    }//GEN-LAST:event_BtnCari1ActionPerformed

    private void BtnCari1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCari1KeyPressed

    private void BtnAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAll1ActionPerformed
        TCari.setText("");
        BtnCariActionPerformed(evt);
    }//GEN-LAST:event_BtnAll1ActionPerformed

    private void BtnAll1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAll1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
    }//GEN-LAST:event_BtnAll1KeyPressed

    private void kdgudangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdgudangKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_PAGE_UP:
            TCari.requestFocus();
            break;
            case KeyEvent.VK_ENTER:
            Sequel.cariIsi("select bangsal.nm_bangsal from bangsal where bangsal.kd_bangsal=?",nmgudang,kdgudang.getText());
            BtnSimpan.requestFocus();
            break;
            case KeyEvent.VK_UP:
            BtnGudangActionPerformed(null);
            break;
        }
    }//GEN-LAST:event_kdgudangKeyPressed

    private void BtnGudangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGudangActionPerformed
        caribangsal.isCek();
        caribangsal.emptTeks();
        caribangsal.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        caribangsal.setLocationRelativeTo(internalFrame1);
        caribangsal.setAlwaysOnTop(false);
        caribangsal.setVisible(true);
    }//GEN-LAST:event_BtnGudangActionPerformed

    private void TabRawat1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawat1MouseClicked
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTMLKPO.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
        Document doc = kit.createDefaultDocument();

        LoadHTMLKPO.setDocument(doc);
        LoadHTMLKPO.setEditable(false);
        LoadHTMLKPO.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        if(TabRawat1.getSelectedIndex()==1){
            if(TNoRw.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
                TCari.requestFocus();
            }else if(TabRawat1.getSelectedIndex()==2){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                RMRiwayatPengobatan resume=new RMRiwayatPengobatan(null,true);
                resume.setNoRm(TNoRM.getText(),TPasien.getText());
                resume.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                resume.setLocationRelativeTo(internalFrame1);
                resume.setVisible(true);
                this.setCursor(Cursor.getDefaultCursor());
            }
        }
    }//GEN-LAST:event_TabRawat1MouseClicked

    private void BtnKeluar2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar2KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,TNoRM);}
    }//GEN-LAST:event_BtnKeluar2KeyPressed

    private void BtnKeluar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar2ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar2ActionPerformed

    private void BtnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrint1ActionPerformed
        if(TNoRM.getText().trim().equals("")||TPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }
    }//GEN-LAST:event_BtnPrint1ActionPerformed

    private void BtnCari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari2ActionPerformed
        if(TNoRM.getText().trim().equals("")||TPasien.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            tampilKPO();
        }
    }//GEN-LAST:event_BtnCari2ActionPerformed

    private void NoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRawatKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCari1ActionPerformed(null);
        }
    }//GEN-LAST:event_NoRawatKeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,TNoRM);
    }//GEN-LAST:event_Tgl2KeyPressed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tbObat.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    getDataobat(tbObat.getSelectedRow());
                    i=tbObat.getSelectedColumn();
                    if(i==9){  // Emb pindah ke index 9 (was 8)
                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0,0")) {
                                tbObat.setValueAt(embalase,tbObat.getSelectedRow(),9);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),9);
                        }
                    }else if(i==10){  // Tsl pindah ke index 10 (was 9)
                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0,0")) {
                                tbObat.setValueAt(tuslah,tbObat.getSelectedRow(),10);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),10);
                        }

                        TCari.setText("");
                        TCari.requestFocus();
                    }else if((i==11)||(i==3)){  // Stok index 11, Kode Barang index 3
                        hitungObat();
                        TCari.setText("");
                        TCari.requestFocus();
                    }else if(i==12){  // Aturan Pakai index 12 (was 11)
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataobat(tbObat.getSelectedRow());
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
                i=tbObat.getSelectedColumn();
                // HAPUS i==1 (K adalah Boolean, tidak bisa dihapus jadi "")
                // Jika ingin hapus K, set false bukan ""
                if((i==2)||(i==12)||(i==9)||(i==10)){  // Jumlah index 2, Aturan index 12, Emb index 9, Tsl index 10
                    if(tbObat.getSelectedRow()!= -1){
                        if(i==2){
                            tbObat.setValueAt("",tbObat.getSelectedRow(),i);  // Jumlah String, OK
                        }else if(i==12){
                            tbObat.setValueAt("",tbObat.getSelectedRow(),i);  // Aturan Pakai String, OK
                        }else{
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),i);  // Emb/Tsl Double, OK
                        }
                    }
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                i=tbObat.getSelectedColumn();
                if(i!=12){  // Aturan Pakai index 12
                    TCari.requestFocus();
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                i=tbObat.getSelectedColumn();
                if(i==3){  // Kode Barang index 3 (was 2)
                    try {
                        getDataobat(tbObat.getSelectedRow());

                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0,0")) {
                                tbObat.setValueAt(embalase,tbObat.getSelectedRow(),9);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),9);
                        }

                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),10).toString().equals("0,0")) {
                                tbObat.setValueAt(tuslah,tbObat.getSelectedRow(),10);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),10);
                        }
                    } catch (Exception e) {
                        tbObat.setValueAt(0,tbObat.getSelectedRow(),11);  // Stok index 11 (was 10)
                    }
                    hitungObat();
                }else if(i==12){  // Aturan Pakai index 12 (was 11)
                    akses.setform("DlgCariObat");
                    aturanpakai.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    aturanpakai.setLocationRelativeTo(internalFrame1);
                    aturanpakai.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_tbObatKeyPressed

    private void tbObatPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbObatPropertyChange
        if(this.isVisible()==true){
            getDataobat();
            hitungObat();
        }
    }//GEN-LAST:event_tbObatPropertyChange

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tbObat.getRowCount()!=0){
            try {
                getDataobat();
            } catch (java.lang.NullPointerException e) {
            }

            if(evt.getClickCount()==2){
                if(akses.getform().equals("DlgPemberianObat")){
                    dispose();
                }
            }
        }
    }//GEN-LAST:event_tbObatMouseClicked

    private void ChkNoResepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkNoResepItemStateChanged
        if(ChkNoResep.isSelected()==true){
            DlgResepObat resep=new DlgResepObat(null,false);
            resep.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
            resep.setLocationRelativeTo(internalFrame1);
            resep.emptTeks();
            resep.isCek();
            if(!namadokter.equals("")){
                resep.setNoRm(TNoRw.getText(),Tanggal.getDate(),Tanggal.getDate(),Jam.getSelectedItem().toString(),Menit.getSelectedItem().toString(),Detik.getSelectedItem().toString(),kodedokter,namadokter,"ralan");
            }else{
                resep.setNoRm(TNoRw.getText(),Tanggal.getDate(),Tanggal.getDate(),Jam.getSelectedItem().toString(),Menit.getSelectedItem().toString(),Detik.getSelectedItem().toString(),"ralan");
                resep.setDokterRalan();
            }
            resep.tampil();
            resep.setVisible(true);
        }
    }//GEN-LAST:event_ChkNoResepItemStateChanged

    private void JeniskelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JeniskelasKeyPressed
        Valid.pindah(evt, TCari,BtnKeluar);
    }//GEN-LAST:event_JeniskelasKeyPressed

    private void JeniskelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JeniskelasItemStateChanged
        tampilobat();
    }//GEN-LAST:event_JeniskelasItemStateChanged

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==1){
            if(TabRawatTindakanDokter.getSelectedIndex() < 0){
                TabRawatTindakanDokter.setSelectedIndex(0);
            }

            // Jalankan logika yang sama dengan TabRawatTindakanPetugasMouseClicked
            if(TabRawatTindakanDokter.getSelectedIndex()==0){
                TCari.setText("");
            }else if(TabRawatTindakanDokter.getSelectedIndex()==1){
                TCari.setText("");
            }
            TCari.requestFocus();
            tampilkanPenangananDokter();
        }else if(TabRawat.getSelectedIndex()==2){
            // Pastikan tab bersarang ter-set ke index 0 saat pertama dibuka
            if(TabRawatTindakanPetugas.getSelectedIndex() < 0){
                TabRawatTindakanPetugas.setSelectedIndex(0);
            }

            // Jalankan logika yang sama dengan TabRawatTindakanPetugasMouseClicked
            if(TabRawatTindakanPetugas.getSelectedIndex()==0){
                TCari.setText("");
            }else if(TabRawatTindakanPetugas.getSelectedIndex()==1){
                TCari.setText("");
                TCari.setText(TNoRM.getText());
            }
            TCari.requestFocus();
            tampilkanPenangananPetugas();
        }else if(TabRawat.getSelectedIndex()==3){
            // Pastikan tab bersarang ter-set ke index 0 saat pertama dibuka
            if(TabRawat1.getSelectedIndex() < 0){
                TabRawat1.setSelectedIndex(0);
            }

            // Jalankan logika yang sama dengan TabRawatTindakanPetugasMouseClicked
            if(TabRawat1.getSelectedIndex()==0){
                TCari.setText("");
            }else if(TabRawat1.getSelectedIndex()==1){
                TCari.setText("");
                TCari.setText(TNoRM.getText());
            }
            TCari.requestFocus();
            tampilobat();
            hitungObat();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void TerapiLainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TerapiLainKeyPressed

    }//GEN-LAST:event_TerapiLainKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMLayananHemodialisa dialog = new RMLayananHemodialisa(new javax.swing.JFrame(), true);
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
    private widget.ComboBox AksesVaskuler;
    private widget.Button BtnAll;
    private widget.Button BtnAll1;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCari1;
    private widget.Button BtnCari2;
    private widget.Button BtnEdit;
    private widget.Button BtnGudang;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnKeluar2;
    private widget.Button BtnPrint;
    private widget.Button BtnPrint1;
    private widget.Button BtnSeekDokter;
    private widget.Button BtnSeekDokter1;
    private widget.Button BtnSeekPetugas;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput1;
    private widget.CekBox ChkInput4;
    private widget.CekBox ChkKejadian;
    private widget.CekBox ChkNoResep;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.ComboBox Detik2;
    private widget.ComboBox Detik3;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput4;
    private widget.PanelBiasa FormInput6;
    private widget.TextBox JK;
    private widget.ComboBox Jam;
    private widget.ComboBox Jam2;
    private widget.ComboBox Jam3;
    private widget.ComboBox Jeniskelas;
    private widget.TextBox KdDok;
    private widget.TextBox KdDok1;
    private widget.Label LCount;
    private widget.Label LPpn;
    private widget.Label LTotal;
    private widget.Label LTotalTagihan;
    private widget.editorpane LoadHTMLKPO;
    private widget.ComboBox Menit;
    private widget.ComboBox Menit2;
    private widget.ComboBox Menit3;
    private javax.swing.JMenuItem MnCetakObservasiIntradialitik;
    private widget.TextBox NoRawat;
    private widget.TextBox NoResep;
    private widget.ComboBox PaketHD;
    private javax.swing.JPanel PanelInput1;
    private javax.swing.JPanel PanelInput4;
    private javax.swing.JPanel PanelInput6;
    private widget.ComboBox Pelayanan;
    private widget.ComboBox PelayananHD;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll11;
    private widget.ScrollPane Scroll4;
    private widget.ScrollPane Scroll5;
    private widget.ScrollPane Scroll6;
    private widget.ScrollPane Scroll7;
    private widget.ScrollPane Scroll8;
    private widget.TextBox TCari;
    private widget.TextBox TCari1;
    private widget.TextBox TDokter;
    private widget.TextBox TDokter1;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPerawat;
    private widget.TabPane TabRawat;
    private javax.swing.JTabbedPane TabRawat1;
    private javax.swing.JTabbedPane TabRawatTindakanDokter;
    private javax.swing.JTabbedPane TabRawatTindakanPetugas;
    private widget.Tanggal Tanggal;
    private widget.Tanggal Tanggal2;
    private widget.TextBox TanggalRegistrasi;
    private widget.TextArea TerapiLain;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private widget.TextBox TglLahir;
    private widget.TextBox Umur;
    private widget.Button btnDokter1;
    private widget.Button btnDokter2;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame2;
    private widget.InternalFrame internalFrame4;
    private widget.InternalFrame internalFrame6;
    private widget.InternalFrame internalFrame7;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private javax.swing.JLabel jLabel1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel13;
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel26;
    private widget.Label jLabel4;
    private widget.Label jLabel45;
    private widget.Label jLabel47;
    private widget.Label jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel65;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu2;
    private widget.TextBox kddok1;
    private widget.TextBox kddok2;
    private widget.TextBox kdgudang;
    private widget.TextBox kdptg;
    private widget.Label label12;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label21;
    private widget.Label label9;
    private widget.TextBox namadokter1;
    private widget.TextBox namadokter2;
    private widget.TextBox nmgudang;
    private widget.panelisi panelGlass10;
    private widget.panelisi panelGlass5;
    private widget.panelisi panelGlass7;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.panelisi panelisi3;
    private widget.ScrollPane scrollPane2;
    private widget.Table tbDataLayananHD;
    private widget.Table tbLayananHD;
    private widget.Table tbObat;
    private widget.Table tbRawatDr;
    private widget.Table tbRawatPr;
    private widget.Table tbTindakan;
    private widget.Table tbTindakan2;
    // End of variables declaration//GEN-END:variables

    public void tampilDataLayananHD() {
        Valid.tabelKosong(tabDataLayananHD);
        try {
            if (TCari.getText().toString().trim().equals("")) {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                    + "pelayanan_hemodialisa.tanggal, pelayanan_hemodialisa.jam_mulai, pelayanan_hemodialisa.jam_selesai, "
                    + "pelayanan_hemodialisa.layanan_hd, pelayanan_hemodialisa.paket, pelayanan_hemodialisa.vakuler, "
                    + "pelayanan_hemodialisa.pelayanan, pelayanan_hemodialisa.kd_dokter_spesialis, dokter_spesialis.nm_dokter AS nm_dokter_spesialis, "
                    + "pelayanan_hemodialisa.kd_dokter_umum, dokter_umum.nm_dokter AS nm_dokter_umum "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN pelayanan_hemodialisa ON reg_periksa.no_rawat = pelayanan_hemodialisa.no_rawat "
                    + "LEFT JOIN dokter AS dokter_spesialis ON pelayanan_hemodialisa.kd_dokter_spesialis = dokter_spesialis.kd_dokter "
                    + "LEFT JOIN dokter AS dokter_umum ON pelayanan_hemodialisa.kd_dokter_umum = dokter_umum.kd_dokter "
                    + "WHERE pelayanan_hemodialisa.tanggal BETWEEN ? AND ? "
                    + "ORDER BY pelayanan_hemodialisa.tanggal, pelayanan_hemodialisa.jam_mulai"
                );
            } else {
                ps = koneksi.prepareStatement(
                    "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                    + "pelayanan_hemodialisa.tanggal, pelayanan_hemodialisa.jam_mulai, pelayanan_hemodialisa.jam_selesai, "
                    + "pelayanan_hemodialisa.layanan_hd, pelayanan_hemodialisa.paket, pelayanan_hemodialisa.vakuler, "
                    + "pelayanan_hemodialisa.pelayanan, pelayanan_hemodialisa.kd_dokter_spesialis, dokter_spesialis.nm_dokter AS nm_dokter_spesialis, "
                    + "pelayanan_hemodialisa.kd_dokter_umum, dokter_umum.nm_dokter AS nm_dokter_umum "
                    + "FROM reg_periksa "
                    + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                    + "INNER JOIN pelayanan_hemodialisa ON reg_periksa.no_rawat = pelayanan_hemodialisa.no_rawat "
                    + "LEFT JOIN dokter AS dokter_spesialis ON pelayanan_hemodialisa.kd_dokter_spesialis = dokter_spesialis.kd_dokter "
                    + "LEFT JOIN dokter AS dokter_umum ON pelayanan_hemodialisa.kd_dokter_umum = dokter_umum.kd_dokter "
                    + "WHERE pelayanan_hemodialisa.tanggal BETWEEN ? AND ? "
                    + "AND ("
                    + "    reg_periksa.no_rawat LIKE ? "
                    + "    OR pasien.no_rkm_medis LIKE ? "
                    + "    OR pasien.nm_pasien LIKE ? "
                    + "    OR pelayanan_hemodialisa.layanan_hd LIKE ? "
                    + "    OR pelayanan_hemodialisa.paket LIKE ? "
                    + "    OR pelayanan_hemodialisa.pelayanan LIKE ? "
                    + "    OR dokter_spesialis.nm_dokter LIKE ? "
                    + "    OR dokter_umum.nm_dokter LIKE ? "
                    + ") "
                    + "ORDER BY pelayanan_hemodialisa.tanggal, pelayanan_hemodialisa.jam_mulai"
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
                    tabDataLayananHD.addRow(new String[]{
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("tgl_lahir"),
                        rs.getString("tanggal"),
                        rs.getString("jam_mulai"),
                        rs.getString("jam_selesai"),
                        rs.getString("layanan_hd"),
                        rs.getString("paket"),
                        rs.getString("vakuler"),
                        rs.getString("pelayanan"),
                        rs.getString("kd_dokter_spesialis"),
                        rs.getString("nm_dokter_spesialis"),
                        rs.getString("kd_dokter_umum"),
                        rs.getString("nm_dokter_umum")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }
        int b = tabDataLayananHD.getRowCount();
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
                    + "monitoring_hd.lainlain "
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
                    + "monitoring_hd.lainlain "
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
                        rs.getString("lainlain")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi : " + e);
        }

        LCount.setText(String.valueOf(tabMonitoringHD.getRowCount()));
    }
    
    private void tampilkanPenangananDokter() {
        if(TabRawatTindakanDokter.getSelectedIndex()==0){
            tampilTindakanDr();
        }else if(TabRawatTindakanDokter.getSelectedIndex()==1){
            tampilDr();
        }
    }
    
    private void tampilDr() {
        Valid.tabelKosong(tabModeDr);
        try{
            ps=koneksi.prepareStatement("select rawat_jl_dr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_dr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_dr.kd_dokter,dokter.nm_dokter,"+
                   "rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat,rawat_jl_dr.biaya_rawat,rawat_jl_dr.kd_jenis_prw, " +
                   "rawat_jl_dr.tarif_tindakandr,rawat_jl_dr.kso,rawat_jl_dr.material,rawat_jl_dr.bhp,rawat_jl_dr.menejemen "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "dokter inner join rawat_jl_dr "+
                   "on rawat_jl_dr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_dr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_dr.kd_dokter=dokter.kd_dokter "+
                   "where rawat_jl_dr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                   (TCari.getText().trim().equals("")?"":"and (rawat_jl_dr.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                   "jns_perawatan.nm_perawatan like ? or rawat_jl_dr.kd_dokter like ? or dokter.nm_dokter like ? )")+
                   " order by rawat_jl_dr.no_rawat,rawat_jl_dr.tgl_perawatan,rawat_jl_dr.jam_rawat desc");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps.setString(3,"%"+TCari.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps.setString(4,"%"+TCari.getText().trim()+"%");
                    ps.setString(5,"%"+TCari.getText().trim()+"%");
                    ps.setString(6,"%"+TCari.getText().trim()+"%");
                    ps.setString(7,"%"+TCari.getText().trim()+"%");
                    ps.setString(8,"%"+TCari.getText().trim()+"%");
                    ps.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps.executeQuery();
                while(rs.next()){
                    tabModeDr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getDouble(9),rs.getString("kd_jenis_prw"),rs.getString("tarif_tindakandr"),rs.getString("kso"),
                        rs.getString("material"),rs.getString("bhp"),rs.getString("menejemen")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }           
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModeDr.getRowCount());
    }
    
    private void tampilTindakanDr() {
        try{     
            int jml = 0;
            for(i=0;i<TabModeTindakan.getRowCount();i++){
                if(TabModeTindakan.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];    

            Valid.tabelKosong(TabModeTindakan);
            
            if (poli_ralan.equals("Yes") && cara_bayar_ralan.equals("Yes")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrdr > 0 and (paket_tindakan_hd.kategori = 'Umum' OR paket_tindakan_hd.kategori = 'Spesialis') and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and (jns_perawatan.kd_jenis_prw like ? or jns_perawatan.nm_perawatan like ? or kategori_perawatan.nm_kategori like ?) " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("No") && cara_bayar_ralan.equals("Yes")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrdr > 0 and (paket_tindakan_hd.kategori = 'Umum' OR paket_tindakan_hd.kategori = 'Spesialis') and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and (jns_perawatan.kd_jenis_prw like ? or jns_perawatan.nm_perawatan like ? or kategori_perawatan.nm_kategori like ?) " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("Yes") && cara_bayar_ralan.equals("No")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrdr > 0 and (paket_tindakan_hd.kategori = 'Umum' OR paket_tindakan_hd.kategori = 'Spesialis') and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and (jns_perawatan.kd_jenis_prw like ? or jns_perawatan.nm_perawatan like ? or kategori_perawatan.nm_kategori like ?) " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("No") && cara_bayar_ralan.equals("No")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrdr > 0 and (paket_tindakan_hd.kategori = 'Umum' OR paket_tindakan_hd.kategori = 'Spesialis') and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_jenis_prw like ? or jns_perawatan.nm_perawatan like ? or kategori_perawatan.nm_kategori like ?) " +
                    "order by jns_perawatan.nm_perawatan");
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while(rstindakan.next()){
                    TabModeTindakan.addRow(new Object[] {
                        true, "", rstindakan.getString(1), rstindakan.getString(2), rstindakan.getString(3),
                        rstindakan.getDouble("total_byrdr"), rstindakan.getDouble("material"),
                        rstindakan.getDouble("bhp"), rstindakan.getDouble("tarif_tindakandr"),
                        rstindakan.getDouble("tarif_tindakanpr"), rstindakan.getDouble("kso"),
                        rstindakan.getDouble("menejemen")
                    });
                }                   
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan.getRowCount());
    }
    
    private void tampilPr() {
        Valid.tabelKosong(tabModePr);
        try{  
            ps2=koneksi.prepareStatement("select rawat_jl_pr.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                   "concat(rawat_jl_pr.kd_jenis_prw,' ',jns_perawatan.nm_perawatan),rawat_jl_pr.nip,petugas.nama,"+
                   "rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat,rawat_jl_pr.biaya_rawat,rawat_jl_pr.kd_jenis_prw, " +
                   "rawat_jl_pr.tarif_tindakanpr,rawat_jl_pr.kso,rawat_jl_pr.material,rawat_jl_pr.bhp,rawat_jl_pr.menejemen "+
                   "from pasien inner join reg_periksa inner join jns_perawatan inner join "+
                   "petugas inner join rawat_jl_pr "+
                   "on rawat_jl_pr.no_rawat=reg_periksa.no_rawat "+
                   "and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                   "and rawat_jl_pr.kd_jenis_prw=jns_perawatan.kd_jenis_prw "+
                   "and rawat_jl_pr.nip=petugas.nip where  "+
                   "rawat_jl_pr.tgl_perawatan between ? and ? and reg_periksa.no_rkm_medis like ? "+
                   (TCari.getText().trim().equals("")?"":"and (rawat_jl_pr.no_rawat like ? or reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or "+
                   "jns_perawatan.nm_perawatan like ? or rawat_jl_pr.nip like ? or petugas.nama like ?) ")+
                   "order by rawat_jl_pr.no_rawat,rawat_jl_pr.tgl_perawatan,rawat_jl_pr.jam_rawat desc"); 
            try{
                ps2.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps2.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                ps2.setString(3,"%"+TCari.getText()+"%");
                if(!TCari.getText().trim().equals("")){
                    ps2.setString(4,"%"+TCari.getText().trim()+"%");
                    ps2.setString(5,"%"+TCari.getText().trim()+"%");
                    ps2.setString(6,"%"+TCari.getText().trim()+"%");
                    ps2.setString(7,"%"+TCari.getText().trim()+"%");
                    ps2.setString(8,"%"+TCari.getText().trim()+"%");
                    ps2.setString(9,"%"+TCari.getText().trim()+"%");
                }
                rs=ps2.executeQuery();
                while(rs.next()){
                    tabModePr.addRow(new Object[]{
                        false,rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6),
                        rs.getString(7),rs.getString(8),rs.getDouble(9),
                        rs.getString("kd_jenis_prw"),rs.getString("tarif_tindakanpr"),
                        rs.getString("kso"),rs.getString("material"),
                        rs.getString("bhp"),rs.getString("menejemen")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps2!=null){
                    ps2.close();
                }
            }                  
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabModePr.getRowCount());
    }
    
    private void tampilkanPenangananPetugas() {
        if(TabRawatTindakanPetugas.getSelectedIndex()==0){
            tampilTindakanPr();
        }else if(TabRawatTindakanPetugas.getSelectedIndex()==1){
            tampilPr();
        }
    }
    
    private void tampilKPO() {
        try {
            htmlContent = new StringBuilder();

            // Siapkan query utama berdasarkan filter
            if (R1.isSelected() == true) {
                ps = koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg," +
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj," +
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab " +
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter " +
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj " +
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? " +
                    "order by reg_periksa.tgl_registrasi desc limit 1");
            } else if (R2.isSelected() == true) {
                ps = koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg," +
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj," +
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab " +
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter " +
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj " +
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? " +
                    "order by reg_periksa.tgl_registrasi");
            } else if (R3.isSelected() == true) {
                ps = koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg," +
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj," +
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab " +
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter " +
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj " +
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and " +
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            } else if (R4.isSelected() == true) {
                ps = koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg," +
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj," +
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab " +
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter " +
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli " +
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj " +
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }

            // Set parameter query utama
            if (R1.isSelected() == true) {
                ps.setString(1, TNoRM.getText().trim());
            } else if (R2.isSelected() == true) {
                ps.setString(1, TNoRM.getText().trim());
            } else if (R3.isSelected() == true) {
                ps.setString(1, TNoRM.getText().trim());
                ps.setString(2, Valid.SetTgl(Tgl1.getSelectedItem() + ""));
                ps.setString(3, Valid.SetTgl(Tgl2.getSelectedItem() + ""));
            } else if (R4.isSelected() == true) {
                ps.setString(1, TNoRM.getText().trim());
                ps.setString(2, NoRawat.getText().trim());
            }

            // Siapkan PreparedStatement untuk query bersarang (dibuat sekali, digunakan berkali-kali)
            PreparedStatement psRujukan = koneksi.prepareStatement(
                "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli " +
                "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli " +
                "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter " +
                "where no_rawat=?");

            PreparedStatement psDpjpRanap = koneksi.prepareStatement(
                "select dokter.nm_dokter from dpjp_ranap " +
                "inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter " +
                "where dpjp_ranap.no_rawat=?");

            PreparedStatement psPemberianObat = koneksi.prepareStatement(
                "select detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,databarang.kode_sat, " +
                "detail_pemberian_obat.kode_brng,detail_pemberian_obat.jml,detail_pemberian_obat.total," +
                "databarang.nama_brng from detail_pemberian_obat inner join databarang " +
                "on detail_pemberian_obat.kode_brng=databarang.kode_brng " +
                "where detail_pemberian_obat.no_rawat=? " +
                "order by detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam");

            PreparedStatement psAturanPakai = koneksi.prepareStatement(
                "select aturan from aturan_pakai " +
                "where tgl_perawatan=? and jam=? and no_rawat=? and kode_brng=?");

            PreparedStatement psReturObat = koneksi.prepareStatement(
                "select databarang.kode_brng,databarang.nama_brng,detreturjual.kode_sat,detreturjual.h_retur, " +
                "(detreturjual.jml_retur * -1) as jumlah,(detreturjual.subtotal * -1) as total from detreturjual " +
                "inner join databarang on detreturjual.kode_brng=databarang.kode_brng " +
                "inner join returjual on returjual.no_retur_jual=detreturjual.no_retur_jual " +
                "where returjual.no_retur_jual like ? order by databarang.nama_brng");

            PreparedStatement psDpjpRanapTtd = koneksi.prepareStatement(
                "select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap " +
                "inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter " +
                "where dpjp_ranap.no_rawat=?");

            urut = 1;
            rs = ps.executeQuery();

            while (rs.next()) {
                // ============================================
                // EKSTRAK SEMUA DATA rs KE VARIABEL LOKAL
                // ============================================
                String noRawat = rs.getString("no_rawat");
                String noReg = rs.getString("no_reg");
                String tglRegistrasi = rs.getString("tgl_registrasi");
                String jamReg = rs.getString("jam_reg");
                String nmPoli = rs.getString("nm_poli");
                String nmDokter = rs.getString("nm_dokter");
                String pJawab = rs.getString("p_jawab");
                String almtPj = rs.getString("almt_pj");
                String hubunganPj = rs.getString("hubunganpj");
                double biayaReg = rs.getDouble("biaya_reg");
                String statusLanjut = rs.getString("status_lanjut");
                String pngJawab = rs.getString("png_jawab");
                String kdDokter = rs.getString("kd_dokter");

                // ============================================
                // QUERY 1: RUJUKAN INTERNAL POLI
                // ============================================
                dokterrujukan = "";
                polirujukan = "";

                try {
                    psRujukan.setString(1, noRawat);
                    ResultSet rsRujukan = psRujukan.executeQuery();
                    while (rsRujukan.next()) {
                        polirujukan = polirujukan + ", " + rsRujukan.getString("nm_poli");
                        dokterrujukan = dokterrujukan + ", " + rsRujukan.getString("nm_dokter");
                    }
                    rsRujukan.close();
                } catch (Exception e) {
                    System.out.println("Notif Rujukan : " + e);
                }

                // ============================================
                // BUILD HTML - INFORMASI REGISTRASI
                // ============================================
                htmlContent.append(
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'>" + urut + "</td>" +
                        "<td valign='top' width='18%'>No.Rawat</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + noRawat + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>No.Registrasi</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + noReg + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Tanggal Registrasi</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + tglRegistrasi + " " + jamReg + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Unit/Poliklinik</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + nmPoli + polirujukan + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Dokter Poli</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + nmDokter + dokterrujukan + "</td>" +
                    "</tr>"
                );

                // ============================================
                // QUERY 2: DPJP RANAP (jika status_lanjut = Ranap)
                // ============================================
                if (statusLanjut.equals("Ranap")) {
                    try {
                        psDpjpRanap.setString(1, noRawat);
                        ResultSet rsDpjp = psDpjpRanap.executeQuery();
                        if (rsDpjp.next()) {
                            htmlContent.append(
                                "<tr class='isi'>" +
                                    "<td valign='top' width='2%'></td>" +
                                    "<td valign='top' width='18%'>DPJP Ranap</td>" +
                                    "<td valign='top' width='1%' align='center'>:</td>" +
                                    "<td valign='top' width='79%'>"
                            );
                            rsDpjp.beforeFirst();
                            urutdpjp = 1;
                            while (rsDpjp.next()) {
                                htmlContent.append(urutdpjp + ". " + rsDpjp.getString("nm_dokter") + "&nbsp;&nbsp;");
                                urutdpjp++;
                            }
                            htmlContent.append("</td></tr>");
                        }
                        rsDpjp.close();
                    } catch (Exception e) {
                        System.out.println("Status Lanjut : " + e);
                    }
                }

                // ============================================
                // BUILD HTML - INFORMASI LAINNYA
                // ============================================
                htmlContent.append(
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Cara Bayar</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + pngJawab + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Penanggung Jawab</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + pJawab + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Alamat P.J.</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + almtPj + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Hubungan P.J.</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + hubunganPj + "</td>" +
                    "</tr>" +
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Status</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" + statusLanjut + "</td>" +
                    "</tr>"
                );

                urut++;

                // ============================================
                // BIAYA & PERAWATAN
                // ============================================
                biayaperawatan = biayaReg;

                htmlContent.append(
                    "<tr class='isi'>" +
                        "<td valign='top' width='2%'></td>" +
                        "<td valign='top' width='18%'>Biaya & Perawatan</td>" +
                        "<td valign='top' width='1%' align='center'>:</td>" +
                        "<td valign='top' width='79%'>" +
                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>" +
                                "<tr>" +
                                    "<td valign='top' width='89%'>Administrasi</td>" +
                                    "<td valign='top' width='1%' align='right'>:</td>" +
                                    "<td valign='top' width='10%' align='right'>" + Valid.SetAngka(biayaReg) + "</td>" +
                                "</tr>" +
                            "</table>"
                );

                // ============================================
                // QUERY 3: PEMBERIAN OBAT/BHP/ALKES
                // ============================================
                try {
                    psPemberianObat.setString(1, noRawat);
                    ResultSet rsObat = psPemberianObat.executeQuery();

                    if (rsObat.next()) {
                        htmlContent.append(
                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>" +
                                "<tr><td valign='top' colspan='5'>Pemberian Obat/BHP/Alkes</td><td valign='top' colspan='1' align='right'>:</td><td></td></tr>" +
                                "<tr align='center'>" +
                                    "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>" +
                                    "<td valign='top' width='15%' bgcolor='#FFFAF8'>Tanggal</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Kode</td>" +
                                    "<td valign='top' width='35%' bgcolor='#FFFAF8'>Nama Obat/BHP/Alkes</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Jumlah</td>" +
                                    "<td valign='top' width='16%' bgcolor='#FFFAF8'>Aturan Pakai</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Biaya</td>" +
                                "</tr>"
                        );

                        rsObat.beforeFirst();
                        w = 1;
                        while (rsObat.next()) {
                            // Ekstrak data rsObat ke variabel lokal
                            String tglPerawatan = rsObat.getString("tgl_perawatan");
                            String jam = rsObat.getString("jam");
                            String kodeSat = rsObat.getString("kode_sat");
                            String kodeBrng = rsObat.getString("kode_brng");
                            double jml = rsObat.getDouble("jml");
                            double total = rsObat.getDouble("total");
                            String namaBrng = rsObat.getString("nama_brng");

                            // Ambil aturan pakai dengan PreparedStatement terpisah
                            String aturan = "";
                            try {
                                psAturanPakai.setString(1, tglPerawatan);
                                psAturanPakai.setString(2, jam);
                                psAturanPakai.setString(3, noRawat);
                                psAturanPakai.setString(4, kodeBrng);
                                ResultSet rsAturan = psAturanPakai.executeQuery();
                                if (rsAturan.next()) {
                                    aturan = rsAturan.getString("aturan");
                                }
                                rsAturan.close();
                            } catch (Exception e) {
                                System.out.println("Notif Aturan : " + e);
                            }

                            htmlContent.append(
                                "<tr>" +
                                    "<td valign='top' align='center'>" + w + "</td>" +
                                    "<td valign='top'>" + tglPerawatan + " " + jam + "</td>" +
                                    "<td valign='top'>" + kodeBrng + "</td>" +
                                    "<td valign='top'>" + namaBrng + "</td>" +
                                    "<td valign='top'>" + jml + " " + kodeSat + "</td>" +
                                    "<td valign='top'>" + aturan + "</td>" +
                                    "<td valign='top' align='right'>" + Valid.SetAngka(total) + "</td>" +
                                "</tr>"
                            );
                            w++;
                            biayaperawatan = biayaperawatan + total;
                        }
                        htmlContent.append("</table>");
                    }
                    rsObat.close();
                } catch (Exception e) {
                    System.out.println("Notifikasi Obat : " + e);
                }

                // ============================================
                // QUERY 4: RETUR OBAT
                // ============================================
                try {
                    psReturObat.setString(1, "%" + noRawat + "%");
                    ResultSet rsRetur = psReturObat.executeQuery();

                    if (rsRetur.next()) {
                        htmlContent.append(
                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>" +
                                "<tr><td valign='top' colspan='3'>Retur Obat</td><td valign='top' colspan='1' align='right'>:</td><td></td></tr>" +
                                "<tr align='center'>" +
                                    "<td valign='top' width='4%' bgcolor='#FFFAF8'>No.</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Kode</td>" +
                                    "<td valign='top' width='66%' bgcolor='#FFFAF8'>Nama Obat/BHP/Alkes</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Jumlah</td>" +
                                    "<td valign='top' width='10%' bgcolor='#FFFAF8'>Biaya</td>" +
                                "</tr>"
                        );

                        rsRetur.beforeFirst();
                        w = 1;
                        while (rsRetur.next()) {
                            String kodeBrng = rsRetur.getString("kode_brng");
                            String namaBrng = rsRetur.getString("nama_brng");
                            String kodeSat = rsRetur.getString("kode_sat");
                            double jumlah = rsRetur.getDouble("jumlah");
                            double total = rsRetur.getDouble("total");

                            htmlContent.append(
                                "<tr>" +
                                    "<td valign='top' align='center'>" + w + "</td>" +
                                    "<td valign='top'>" + kodeBrng + "</td>" +
                                    "<td valign='top'>" + namaBrng + "</td>" +
                                    "<td valign='top'>" + jumlah + " " + kodeSat + "</td>" +
                                    "<td valign='top' align='right'>" + Valid.SetAngka(total) + "</td>" +
                                "</tr>"
                            );
                            w++;
                            biayaperawatan = biayaperawatan + total;
                        }
                        htmlContent.append("</table>");
                    }
                    rsRetur.close();
                } catch (Exception e) {
                    System.out.println("Notifikasi Retur : " + e);
                }

                // ============================================
                // TANDA TANGAN / VERIFIKASI (hanya jika R4 dipilih)
                // ============================================
                if (R4.isSelected() == true) {
                    if (statusLanjut.equals("Ralan")) {
                        get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + kdDokter.replace(" ", "_"));
                        http.executeMethod(get);

                        htmlContent.append(
                            "<tr class='isi'>" +
                                "<td valign='top' width='2%'></td>" +
                                "<td valign='middle' width='18%'>Tanda Tangan/Verifikasi</td>" +
                                "<td valign='middle' width='1%' align='center'>:</td>" +
                                "<td valign='middle' width='79%' align='center'>Dokter Poli<br><img width='90' height='90' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + kdDokter + ".png'/><br>" + nmDokter + "</td>" +
                            "</tr>"
                        );

                    } else if (statusLanjut.equals("Ranap")) {
                        try {
                            psDpjpRanapTtd.setString(1, noRawat);
                            ResultSet rsDpjpTtd = psDpjpRanapTtd.executeQuery();

                            if (rsDpjpTtd.next()) {
                                htmlContent.append(
                                    "<tr class='isi'>" +
                                        "<td valign='top' width='2%'></td>" +
                                        "<td valign='middle' width='18%'>Tanda Tangan/Verifikasi</td>" +
                                        "<td valign='middle' width='1%' align='center'>:</td>" +
                                        "<td valign='top' width='79%' align='center'>" +
                                            "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>" +
                                                "<tr class='isi'>"
                                );

                                rsDpjpTtd.beforeFirst();
                                urutdpjp = 1;
                                while (rsDpjpTtd.next()) {
                                    String kdDokterDpjp = rsDpjpTtd.getString("kd_dokter");
                                    String nmDokterDpjp = rsDpjpTtd.getString("nm_dokter");

                                    get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + kdDokterDpjp.replace(" ", "_"));
                                    http.executeMethod(get);

                                    htmlContent.append(
                                        "<td border='0' align='center'>Dokter DPJP " + urutdpjp + "<br><img width='90' height='90' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + kdDokterDpjp + ".png'/><br>" + nmDokterDpjp + "</td>"
                                    );
                                    urutdpjp++;
                                }
                                htmlContent.append(
                                            "</tr>" +
                                        "</table>" +
                                    "</td></tr>"
                                );

                            } else {
                                // Tidak ada DPJP, gunakan dokter poli
                                get = new GetMethod("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/generateqrcode.php?kodedokter=" + kdDokter.replace(" ", "_"));
                                http.executeMethod(get);

                                htmlContent.append(
                                    "<tr class='isi'>" +
                                        "<td valign='top' width='2%'></td>" +
                                        "<td valign='middle' width='18%'>Tanda Tangan/Verifikasi</td>" +
                                        "<td valign='middle' width='1%' align='center'>:</td>" +
                                        "<td valign='middle' width='79%' align='center'>Dokter DPJP<br><img width='90' height='90' src='http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/penggajian/temp/" + kdDokter + ".png'/><br>" + nmDokter + "</td>" +
                                    "</tr>"
                                );
                            }
                            rsDpjpTtd.close();
                        } catch (Exception e) {
                            System.out.println("Tanda Tangan IGD : " + e);
                        }
                    }
                }

                htmlContent.append(
                    "<tr class='isi'><td></td><td colspan='3' align='right'>&nbsp;</tr>"
                );
            }

            // Tutup PreparedStatement bersarang
            psRujukan.close();
            psDpjpRanap.close();
            psPemberianObat.close();
            psAturanPakai.close();
            psReturObat.close();
            psDpjpRanapTtd.close();

            // Set hasil ke komponen LoadHTML
            LoadHTMLKPO.setText(
                "<html>" +
                    "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>" +
                        htmlContent.toString() +
                    "</table>" +
                "</html>"
            );

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                System.out.println("Notifikasi Finally : " + e);
            }
        }
    }
    
    private void tampilTindakanPr() {
        try{     
            jml=0;
            for(i=0;i<TabModeTindakan2.getRowCount();i++){
                if(TabModeTindakan2.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }

            pilih=null;
            pilih=new boolean[jml]; 
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            kategori=null;
            kategori=new String[jml];
            totaltnd=null;
            totaltnd=new double[jml];  
            bagianrs=null;
            bagianrs=new double[jml];
            bhp=null;
            bhp=new double[jml];
            jmdokter=null;
            jmdokter=new double[jml];
            jmperawat=null;
            jmperawat=new double[jml];
            kso=null;
            kso=new double[jml];
            menejemen=null;
            menejemen=new double[jml];

            Valid.tabelKosong(TabModeTindakan2);
           
            if (poli_ralan.equals("Yes") && cara_bayar_ralan.equals("Yes")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and jns_perawatan.kd_jenis_prw like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and jns_perawatan.nm_perawatan like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and kategori_perawatan.nm_kategori like ? " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("No") && cara_bayar_ralan.equals("Yes")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and jns_perawatan.kd_jenis_prw like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and jns_perawatan.nm_perawatan like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_pj = ? or jns_perawatan.kd_pj = '-') " +
                    "and kategori_perawatan.nm_kategori like ? " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("Yes") && cara_bayar_ralan.equals("No")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and jns_perawatan.kd_jenis_prw like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and jns_perawatan.nm_perawatan like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and (jns_perawatan.kd_poli = ? or jns_perawatan.kd_poli = '-') " +
                    "and kategori_perawatan.nm_kategori like ? " +
                    "order by jns_perawatan.nm_perawatan");

            } else if (poli_ralan.equals("No") && cara_bayar_ralan.equals("No")) {
                pstindakan = koneksi.prepareStatement(
                    "select jns_perawatan.kd_jenis_prw, jns_perawatan.nm_perawatan, kategori_perawatan.nm_kategori, " +
                    "jns_perawatan.total_byrdr, jns_perawatan.total_byrpr, jns_perawatan.total_byrdrpr, jns_perawatan.bhp, jns_perawatan.material, " +
                    "jns_perawatan.tarif_tindakandr, jns_perawatan.tarif_tindakanpr, jns_perawatan.kso, jns_perawatan.menejemen, " +
                    "paket_tindakan_hd.kd_jenis_prw as is_paket " +
                    "from jns_perawatan " +
                    "inner join kategori_perawatan on jns_perawatan.kd_kategori = kategori_perawatan.kd_kategori " +
                    "inner join paket_tindakan_hd on jns_perawatan.kd_jenis_prw = paket_tindakan_hd.kd_jenis_prw " +
                    "where jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and jns_perawatan.kd_jenis_prw like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and jns_perawatan.nm_perawatan like ? " +
                    "or " +
                    "jns_perawatan.total_byrpr > 0 and paket_tindakan_hd.kategori = 'Perawat' and jns_perawatan.status = '1' " +
                    "and kategori_perawatan.nm_kategori like ? " +
                    "order by jns_perawatan.nm_perawatan");
            }
            
            try {
                if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,kode_poli.trim());
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(4,kd_pj.trim());
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(7,kd_pj.trim());
                    pstindakan.setString(8,kode_poli.trim());
                    pstindakan.setString(9,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("Yes")){
                    pstindakan.setString(1,kd_pj.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kd_pj.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kd_pj.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("Yes")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,kode_poli.trim());
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,kode_poli.trim());
                    pstindakan.setString(4,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(5,kode_poli.trim());
                    pstindakan.setString(6,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }else if(poli_ralan.equals("No")&&cara_bayar_ralan.equals("No")){
                    pstindakan.setString(1,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(2,"%"+TCari.getText().trim()+"%");
                    pstindakan.setString(3,"%"+TCari.getText().trim()+"%");
                    rstindakan=pstindakan.executeQuery();
                }
                
                while (rstindakan.next()) {
                    TabModeTindakan2.addRow(new Object[] {
                        true,                          // 0 - P (checkbox)
                        "",                             // 1 - Jumlah (default 1, user bisa ubah)
                        rstindakan.getString(1),       // 2 - Kode
                        rstindakan.getString(2),       // 3 - Nama Perawatan
                        rstindakan.getString(3),       // 4 - Kategori Perawatan
                        rstindakan.getDouble("total_byrpr"),   // 5 - Tarif/Biaya
                        rstindakan.getDouble("material"),      // 6 - Bagian RS
                        rstindakan.getDouble("bhp"),           // 7 - BHP
                        rstindakan.getDouble("tarif_tindakandr"),  // 8 - JM Dokter
                        rstindakan.getDouble("tarif_tindakanpr"),  // 9 - JM Perawat
                        rstindakan.getDouble("kso"),           // 10 - KSO
                        rstindakan.getDouble("menejemen")      // 11 - Menejemen
                    });
                }
                
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rstindakan != null){
                    rstindakan.close();
                }
                if(pstindakan != null){
                    pstindakan.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+TabModeTindakan2.getRowCount());
    }
    
    private void getDataPr() {
        if(tbRawatPr.getSelectedRow()!= -1){
            TNoRw.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),1).toString());
            TNoRM.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),2).toString());
            TPasien.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),3).toString());   
            kdptg.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),5).toString());
            TPerawat.setText(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),6).toString());
            Jam.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(0,2));
            Menit.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(3,5));
            Detik.setSelectedItem(tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),8).toString().substring(6,8));
            Valid.SetTgl(Tanggal,tbRawatPr.getValueAt(tbRawatPr.getSelectedRow(),7).toString());
        }
    }
    
    public void tampilobat() {   
            String obat_igd = Sequel.cariIsi("select obat_igd from setting_obat");
            String norwt = TNoRw.getText();

            if(obat_igd.equals("yes")){
                String kd_pj = Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",norwt);
                String kd_poli = Sequel.cariIsi("select reg_periksa.kd_poli from reg_periksa where reg_periksa.no_rawat=?",norwt);

                if(kd_poli.equals("IGDK") && !kd_pj.equals("BPJ")){
                    double igd=Sequel.cariIsiAngka("select (margin_igd/100) from setting_obat");

                    boolean isKhusus = Sequel.cariIsiBoolean(
                        "SELECT count(*)>0 FROM set_harga_obat_ralan WHERE kd_pj='" + kd_pj + "'"
                    );
                    if (!isKhusus) {
                        kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?","-");
                        kenaikan += igd;
                    } else {
                        kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?",kd_pj);
                        kenaikan += igd;
                    }
                }else{
                    boolean isKhusus = Sequel.cariIsiBoolean(
                        "SELECT count(*)>0 FROM set_harga_obat_ralan WHERE kd_pj='" + kd_pj + "'"
                    );
                    if (!isKhusus) {
                        kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?","-");
                    } else {
                        kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?",kd_pj);
                    }
                }
            }else{
                boolean isKhusus = Sequel.cariIsiBoolean(
                    "SELECT count(*)>0 FROM set_harga_obat_ralan WHERE kd_pj='" + kd_pj + "'"
                );
                if (!isKhusus) {
                    kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?","-");
                } else {
                    kenaikan=Sequel.cariIsiAngka("select (set_harga_obat_ralan.hargajual/100) from set_harga_obat_ralan where set_harga_obat_ralan.kd_pj=?",kd_pj);
                }
            }

            z=0;
            for(i=0;i<tbObat.getRowCount();i++){
                if(Valid.SetAngka(tbObat.getValueAt(i,2).toString())>0){  // Jumlah pindah ke index 2
                    z++;
                }
            }    

            pilih=null;
            pilih=new boolean[z]; 
            k=null;  // Tambah array untuk kolom K
            k=new boolean[z];
            jumlah=null;
            jumlah=new double[z];
            harga=null;
            harga=new double[z];
            eb=null;
            eb=new double[z];
            ts=null;
            ts=new double[z];
            stok=null;
            stok=new double[z];
            kodebarang=null;
            kodebarang=new String[z];
            namabarang=null;
            namabarang=new String[z];
            kodesatuan=null;
            kodesatuan=new String[z];
            letakbarang=null;
            letakbarang=new String[z];
            namajenis=null;
            namajenis=new String[z];                   
            aturan=null;
            aturan=new String[z];           
            industri=null;
            industri=new String[z];         
            beli=null;
            beli=new double[z]; 
            kategori=null;
            kategori=new String[z];
            golongan=null;
            golongan=new String[z];
            nobatch=new String[z];
            nofaktur=new String[z];
            kadaluarsa=new String[z];
            z=0;        
            for(i=0;i<tbObat.getRowCount();i++){
                if(Valid.SetAngka(tbObat.getValueAt(i,2).toString())>0){  // Jumlah index 2
                    pilih[z]=Boolean.parseBoolean(tbObat.getValueAt(i,0).toString());  // P (index 0)
                    k[z]=Boolean.parseBoolean(tbObat.getValueAt(i,1).toString());    // K (index 1) - TAMBAH
                    try {
                        jumlah[z]=Double.parseDouble(tbObat.getValueAt(i,2).toString());  // Jumlah index 2
                    } catch (Exception e) {
                        jumlah[z]=0;
                    }  
                    kodebarang[z]=tbObat.getValueAt(i,3).toString();   // Kode index 3 (was 2)
                    namabarang[z]=tbObat.getValueAt(i,4).toString();   // Nama index 4 (was 3)
                    kodesatuan[z]=tbObat.getValueAt(i,5).toString();   // Satuan index 5 (was 4)
                    letakbarang[z]=tbObat.getValueAt(i,6).toString();  // Letak index 6 (was 5)
                    try {
                        harga[z]=Double.parseDouble(tbObat.getValueAt(i,7).toString());  // Harga index 7 (was 6)
                    } catch (Exception e) {
                        harga[z]=0;
                    }                  
                    namajenis[z]=tbObat.getValueAt(i,8).toString();    // Jenis index 8 (was 7)
                    try {
                        eb[z]=Double.parseDouble(tbObat.getValueAt(i,9).toString());     // Emb index 9 (was 8)
                    } catch (Exception e) {
                        eb[z]=0;
                    }  
                    try {
                        ts[z]=Double.parseDouble(tbObat.getValueAt(i,10).toString());   // Tsl index 10 (was 9)
                    } catch (Exception e) {
                        ts[z]=0;
                    } 
                    try {
                        stok[z]=Double.parseDouble(tbObat.getValueAt(i,11).toString());  // Stok index 11 (was 10)
                    } catch (Exception e) {
                        stok[z]=0;
                    } 
                    aturan[z]=tbObat.getValueAt(i,12).toString();      // Aturan index 12 (was 11)
                    industri[z]=tbObat.getValueAt(i,13).toString();    // IF index 13 (was 12)
                    try {
                        beli[z]=Double.parseDouble(tbObat.getValueAt(i,14).toString());  // H.Beli index 14 (was 13)
                    } catch (Exception e) {
                        beli[z]=0;
                    } 
                    kategori[z]=tbObat.getValueAt(i,15).toString();    // Kategori index 15 (was 14)
                    golongan[z]=tbObat.getValueAt(i,16).toString();    // Golongan index 16 (was 15)
                    nobatch[z]=tbObat.getValueAt(i,17).toString();     // No.Batch index 17 (was 16)
                    nofaktur[z]=tbObat.getValueAt(i,18).toString();    // No.Faktur index 18 (was 17)
                    try {
                        kadaluarsa[z]=tbObat.getValueAt(i,19).toString();  // Kadaluarsa index 19 (was 18)
                    } catch (Exception e) {
                        kadaluarsa[z]="0000-00-00";
                    }

                    z++;
                }
            }

            Valid.tabelKosong(tabModeobat);             

            for(i=0;i<z;i++){
                tabModeobat.addRow(new Object[] {
                    pilih[i],k[i],jumlah[i],kodebarang[i],namabarang[i],kodesatuan[i],letakbarang[i],harga[i],namajenis[i],
                    eb[i],ts[i],stok[i],aturan[i],industri[i],beli[i],kategori[i],golongan[i],nobatch[i],nofaktur[i],kadaluarsa[i]
                });
            }

            try {
                if(kenaikan>0){
                    sql="SELECT " +
                        "    databarang.kode_brng, " +
                        "    databarang.nama_brng, " +
                        "    jenis.nama, " +
                        "    databarang.kode_sat, " +
                        "    CASE " +
                        "        WHEN setpenjualanperbarang.kode_brng IS NOT NULL THEN (databarang.h_beli + (databarang.h_beli * (setpenjualanperbarang.ralan/100))) " +
                        "        ELSE (databarang.h_beli + (databarang.h_beli * ?)) " +
                        "    END AS harga, " +
                        "    databarang.letak_barang, " +
                        "    industrifarmasi.nama_industri, " +
                        "    databarang.h_beli AS dasar, " +
                        "    kategori_barang.nama AS kategori, " +
                        "    gudangbarang.stok, " +
                        "    golongan_barang.nama AS golongan, " +
                        "    databarang.h_beli AS dasar, " +
                        "    databarang.kapasitas, " +
                        "    paket_bhp_hd.jumlah " +
                        "FROM " +
                        "    databarang " +
                        "INNER JOIN jenis ON databarang.kdjns = jenis.kdjns " +
                        "INNER JOIN industrifarmasi ON industrifarmasi.kode_industri = databarang.kode_industri " +
                        "INNER JOIN golongan_barang ON databarang.kode_golongan = golongan_barang.kode " +
                        "INNER JOIN kategori_barang ON databarang.kode_kategori = kategori_barang.kode " +
                        "INNER JOIN gudangbarang ON databarang.kode_brng = gudangbarang.kode_brng " +
                        "INNER JOIN paket_bhp_hd ON paket_bhp_hd.kode_brng = databarang.kode_brng " +
                        "LEFT JOIN setpenjualanperbarang ON setpenjualanperbarang.kode_brng = databarang.kode_brng";    

                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' "+
                        (TCari1.getText().trim().equals("")?"":" and (databarang.kode_brng like ? or databarang.nama_brng like ? or kategori_barang.nama like ? or "+
                        "golongan_barang.nama like ? or jenis.nama like ? or databarang.letak_barang like ?) ")+
                        "order by databarang.nama_brng");
                    try{
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        if(!TCari1.getText().trim().equals("")){
                            psobat.setString(3,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(4,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(5,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(6,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(7,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(8,"%"+TCari1.getText().trim()+"%");
                        }

                        rsobat=psobat.executeQuery();
                        while(rsobat.next()){
                            tabModeobat.addRow(new Object[] {
                                false,   // P (index 0)
                                false,   // K (index 1) - TAMBAH
                                rsobat.getString("jumlah"),     // Jumlah index 2
                                rsobat.getString("kode_brng"),  // Kode index 3
                                rsobat.getString("nama_brng"),  // Nama index 4
                                rsobat.getString("kode_sat"),   // Satuan index 5
                                rsobat.getString("letak_barang"), // Letak index 6
                                Valid.roundUp(rsobat.getDouble("harga"),100), // Harga index 7
                                rsobat.getString("nama"),       // Jenis index 8
                                0,                              // Emb index 9
                                0,                              // Tsl index 10
                                rsobat.getDouble("stok"),       // Stok index 11
                                "",                             // Aturan index 12
                                rsobat.getString("nama_industri"), // IF index 13
                                rsobat.getDouble("dasar"),      // H.Beli index 14
                                rsobat.getString("kategori"),   // Kategori index 15
                                rsobat.getString("golongan"),   // Golongan index 16
                                "",                             // No.Batch index 17
                                "",                             // No.Faktur index 18
                                ""                              // Kadaluarsa index 19
                            });          
                        }
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rsobat != null){
                            rsobat.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }         
                }else{
                    sql="SELECT " +
                        "    databarang.kode_brng, " +
                        "    databarang.nama_brng, " +
                        "    jenis.nama, " +
                        "    databarang.kode_sat, " +
                        "    CASE " +
                        "        WHEN setpenjualanperbarang.kode_brng IS NOT NULL THEN (databarang.h_beli + (databarang.h_beli * (setpenjualanperbarang.ralan/100))) " +
                        "        ELSE (databarang.h_beli + (databarang.h_beli * ?)) " +
                        "    END AS harga, " +
                        "    databarang.letak_barang, " +
                        "    industrifarmasi.nama_industri, " +
                        "    databarang.h_beli AS dasar, " +
                        "    kategori_barang.nama AS kategori, " +
                        "    gudangbarang.stok, " +
                        "    golongan_barang.nama AS golongan, " +
                        "    databarang.h_beli AS dasar, " +
                        "    databarang.kapasitas, " +
                        "    paket_bhp_hd.jumlah " +
                        "FROM " +
                        "    databarang " +
                        "INNER JOIN jenis ON databarang.kdjns = jenis.kdjns " +
                        "INNER JOIN industrifarmasi ON industrifarmasi.kode_industri = databarang.kode_industri " +
                        "INNER JOIN golongan_barang ON databarang.kode_golongan = golongan_barang.kode " +
                        "INNER JOIN kategori_barang ON databarang.kode_kategori = kategori_barang.kode " +
                        "INNER JOIN gudangbarang ON databarang.kode_brng = gudangbarang.kode_brng " +
                        "INNER JOIN paket_bhp_hd ON paket_bhp_hd.kode_brng = databarang.kode_brng " +
                        "LEFT JOIN setpenjualanperbarang ON setpenjualanperbarang.kode_brng = databarang.kode_brng";    

                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1'"+
                        (TCari1.getText().trim().equals("")?"":" and (databarang.kode_brng like ? or databarang.nama_brng like ? or kategori_barang.nama like ? or "+
                        "golongan_barang.nama like ? or jenis.nama like ? or databarang.letak_barang like ?)")+
                        " order by databarang.nama_brng");
                    try{
                        psobat.setString(1,kdgudang.getText());
                        if(!TCari1.getText().trim().equals("")){
                            psobat.setString(2,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(3,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(4,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(5,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(6,"%"+TCari1.getText().trim()+"%");
                            psobat.setString(7,"%"+TCari1.getText().trim()+"%");
                        }

                        rsobat=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rsobat.next()){
                                tabModeobat.addRow(new Object[] {
                                    false,   // P (index 0)
                                    false,   // K (index 1) - TAMBAH
                                    rsobat.getString("jumlah"),     // Jumlah index 2
                                    rsobat.getString("kode_brng"),  // Kode index 3
                                    rsobat.getString("nama_brng"),  // Nama index 4
                                    rsobat.getString("kode_sat"),   // Satuan index 5
                                    rsobat.getString("letak_barang"), // Letak index 6
                                    Valid.roundUp(rsobat.getDouble("karyawan"),100), // Harga index 7
                                    rsobat.getString("nama"),       // Jenis index 8
                                    0,                              // Emb index 9
                                    0,                              // Tsl index 10
                                    rsobat.getDouble("stok"),       // Stok index 11
                                    "",                             // Aturan index 12
                                    rsobat.getString("nama_industri"), // IF index 13
                                    rsobat.getDouble("dasar"),      // H.Beli index 14
                                    rsobat.getString("kategori"),   // Kategori index 15
                                    rsobat.getString("golongan"),   // Golongan index 16
                                    "",                             // No.Batch index 17
                                    "",                             // No.Faktur index 18
                                    ""                              // Kadaluarsa index 19
                                });     
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Rawat Jalan")){
                            while(rsobat.next()){
                                tabModeobat.addRow(new Object[] {
                                    false,   // P (index 0)
                                    false,   // K (index 1) - TAMBAH
                                    rsobat.getString("jumlah"),     // Jumlah index 2
                                    rsobat.getString("kode_brng"),  // Kode index 3
                                    rsobat.getString("nama_brng"),  // Nama index 4
                                    rsobat.getString("kode_sat"),   // Satuan index 5
                                    rsobat.getString("letak_barang"), // Letak index 6
                                    Valid.roundUp(rsobat.getDouble("ralan"),100), // Harga index 7
                                    rsobat.getString("nama"),       // Jenis index 8
                                    0,                              // Emb index 9
                                    0,                              // Tsl index 10
                                    rsobat.getDouble("stok"),       // Stok index 11
                                    "",                             // Aturan index 12
                                    rsobat.getString("nama_industri"), // IF index 13
                                    rsobat.getDouble("dasar"),      // H.Beli index 14
                                    rsobat.getString("kategori"),   // Kategori index 15
                                    rsobat.getString("golongan"),   // Golongan index 16
                                    "",                             // No.Batch index 17
                                    "",                             // No.Faktur index 18
                                    ""                              // Kadaluarsa index 19
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rsobat.next()){
                                tabModeobat.addRow(new Object[] {
                                    false,   // P (index 0)
                                    false,   // K (index 1) - TAMBAH
                                    rsobat.getString("jumlah"),     // Jumlah index 2
                                    rsobat.getString("kode_brng"),  // Kode index 3
                                    rsobat.getString("nama_brng"),  // Nama index 4
                                    rsobat.getString("kode_sat"),   // Satuan index 5
                                    rsobat.getString("letak_barang"), // Letak index 6
                                    Valid.roundUp(rsobat.getDouble("beliluar"),100), // Harga index 7
                                    rsobat.getString("nama"),       // Jenis index 8
                                    0,                              // Emb index 9
                                    0,                              // Tsl index 10
                                    rsobat.getDouble("stok"),       // Stok index 11
                                    "",                             // Aturan index 12
                                    rsobat.getString("nama_industri"), // IF index 13
                                    rsobat.getDouble("dasar"),      // H.Beli index 14
                                    rsobat.getString("kategori"),   // Kategori index 15
                                    rsobat.getString("golongan"),   // Golongan index 16
                                    "",                             // No.Batch index 17
                                    "",                             // No.Faktur index 18
                                    ""                              // Kadaluarsa index 19
                                });          
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama/BPJS")){
                            while(rsobat.next()){
                                tabModeobat.addRow(new Object[] {
                                    false,   // P (index 0)
                                    false,   // K (index 1) - TAMBAH
                                    rsobat.getString("jumlah"),     // Jumlah index 2
                                    rsobat.getString("kode_brng"),  // Kode index 3
                                    rsobat.getString("nama_brng"),  // Nama index 4
                                    rsobat.getString("kode_sat"),   // Satuan index 5
                                    rsobat.getString("letak_barang"), // Letak index 6
                                    Valid.roundUp(rsobat.getDouble("utama"),100), // Harga index 7
                                    rsobat.getString("nama"),       // Jenis index 8
                                    0,                              // Emb index 9
                                    0,                              // Tsl index 10
                                    rsobat.getDouble("stok"),       // Stok index 11
                                    "",                             // Aturan index 12
                                    rsobat.getString("nama_industri"), // IF index 13
                                    rsobat.getDouble("dasar"),      // H.Beli index 14
                                    rsobat.getString("kategori"),   // Kategori index 15
                                    rsobat.getString("golongan"),   // Golongan index 16
                                    "",                             // No.Batch index 17
                                    "",                             // No.Faktur index 18
                                    ""                              // Kadaluarsa index 19
                                });           
                            }
                        }   
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rsobat != null){
                            rsobat.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }                                       
                }      
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            }            
        }

    public void tampilLayananHD() {
        Valid.tabelKosong(tabLayananHD);

        try {
            String query = "";
            if (TCari.getText().toString().trim().isEmpty()) {
                query = "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                        + "pelayanan_hemodialisa_terapi.terapi_obat_lain "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN pelayanan_hemodialisa_terapi ON reg_periksa.no_rawat = pelayanan_hemodialisa_terapi.no_rawat "
                        + "WHERE reg_periksa.no_rawat = ?";  // <-- Filter by no_rawat, bukan tanggal
            } else {
                query = "SELECT reg_periksa.no_rawat, pasien.no_rkm_medis, pasien.nm_pasien, pasien.tgl_lahir, "
                        + "pelayanan_hemodialisa_terapi.terapi_obat_lain "
                        + "FROM reg_periksa "
                        + "INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis "
                        + "INNER JOIN pelayanan_hemodialisa_terapi ON reg_periksa.no_rawat = pelayanan_hemodialisa_terapi.no_rawat "
                        + "WHERE ("
                        + "reg_periksa.no_rawat LIKE ? OR pasien.no_rkm_medis LIKE ? OR pasien.nm_pasien LIKE ? OR "
                        + "pelayanan_hemodialisa_terapi.terapi_obat_lain LIKE ?) ";
            }

            ps = koneksi.prepareStatement(query);

            if (TCari.getText().toString().trim().isEmpty()) {
                ps.setString(1, TNoRw.getText());  // <-- Filter by no_rawat pasien aktif
            } else {
                String cari = "%" + TCari.getText().trim() + "%";
                for (int i = 1; i <= 4; i++) {
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
                    rs.getString("terapi_obat_lain")
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
                namadokter1.setText("");
                kddok2.setText("");
                namadokter2.setText("");
                Tanggal2.setDate(new Date());
                Jam2.setSelectedIndex(0);
                Menit2.setSelectedIndex(0);
                Detik2.setSelectedIndex(0);
                Jam3.setSelectedIndex(0);
                Menit3.setSelectedIndex(0);
                Detik3.setSelectedIndex(0);
                Pelayanan.setSelectedIndex(0);
                PaketHD.setSelectedIndex(0);
                AksesVaskuler.setSelectedIndex(0);
                PelayananHD.setSelectedIndex(0);
                break;
            case 1:
                Tanggal.setDate(new Date());
                break;
            case 2:
                break;
            case 3: //sudah
                break;
            case 4:
                TerapiLain.setText("");
                break;
            default:
        }
    }

    private void getDataPelayananHD() {
        if (tbDataLayananHD.getSelectedRow() != -1) {
            TNoRw.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 3).toString());
            Valid.SetTgl(Tanggal2, tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 4).toString());
            String jamMulai = tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 5).toString();
            String[] jm = jamMulai.split(":");
            Jam2.setSelectedItem(jm[0]);
            Menit2.setSelectedItem(jm[1]);
            Detik2.setSelectedItem(jm[2]);
            String jamSelesai = tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 6).toString();
            String[] js = jamSelesai.split(":");
            Jam3.setSelectedItem(js[0]);
            Menit3.setSelectedItem(js[1]);
            Detik3.setSelectedItem(js[2]);
            Pelayanan.setSelectedItem(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 7).toString());
            PaketHD.setSelectedItem(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 8).toString());
            AksesVaskuler.setSelectedItem(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 9).toString());
            PelayananHD.setSelectedItem(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 10).toString());
            kddok2.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 11).toString());
            namadokter2.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 12).toString());
            kddok1.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 13).toString());
            namadokter1.setText(tbDataLayananHD.getValueAt(tbDataLayananHD.getSelectedRow(), 14).toString());
        }
    }
    
    private void getDataLayananHD() {
        if (tbLayananHD.getSelectedRow() != -1) {
            // Kolom 0-3: Data pasien
            TNoRw.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 0).toString());
            TNoRM.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 1).toString());
            TPasien.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 2).toString());
            TglLahir.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 3).toString());
            TerapiLain.setText(tbLayananHD.getValueAt(tbLayananHD.getSelectedRow(), 4).toString());
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
        kdgudang.setText("Depo3");
        nmgudang.setText("Depo HD");
        isRawat();
        isForm();
    }

    private void isForm() {

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
    
    private void SimpanPenangananDokter() {
        try {
            Sequel.AutoComitFalse();
            sukses = true;
            ttljmdokter = 0;
            ttlkso = 0;
            ttlpendapatan = 0;
            ttljasasarana = 0;
            ttlbhp = 0;
            ttlmenejemen = 0;

            for (i = 0; i < tbTindakan.getRowCount(); i++) { 
                if (tbTindakan.getValueAt(i, 0).toString().equals("true")) {  

                    int jumlah = Integer.parseInt(tbTindakan.getValueAt(i, 1).toString());

                    for (int x = 0; x < jumlah; x++) {
                        // Buat jam unik dengan menambahkan x detik
                        String jam = Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem();

                        // Kalau jumlah > 1, tambahkan x detik untuk membuat jam unik
                        if (jumlah > 1) {
                            int detikAsli = Integer.parseInt(Detik.getSelectedItem().toString());
                            int detikBaru = detikAsli + x;
                            // Handle overflow detik (max 59)
                            int menitBaru = Integer.parseInt(Menit.getSelectedItem().toString());
                            int jamBaru = Integer.parseInt(Jam.getSelectedItem().toString());

                            if (detikBaru >= 60) {
                                detikBaru = detikBaru % 60;
                                menitBaru++;
                            }
                            if (menitBaru >= 60) {
                                menitBaru = menitBaru % 60;
                                jamBaru++;
                            }

                            jam = String.format("%02d:%02d:%02d", jamBaru, menitBaru, detikBaru);
                        }
                        
                        String jenis = Sequel.cariIsi("SELECT kategori FROM paket_tindakan_hd WHERE kd_jenis_prw = '"+tbTindakan.getValueAt(i, 2).toString()+"'");

                        String dokter = "";
                        if(jenis.equals("Spesialis")){
                            dokter = KdDok.getText();
                        }else{
                            dokter = KdDok1.getText();
                        }
                        
                        if (Sequel.menyimpantf("rawat_jl_dr", "?,?,?,?,?,?,?,?,?,?,?,'Belum'", "Tindakan", 11, new String[]{
                            TNoRw.getText(),                          // no_rawat
                            tbTindakan.getValueAt(i, 2).toString(),   // kd_jenis_prw (Kode)
                            dokter,                          // kd_dokter
                            Valid.SetTgl(Tanggal.getSelectedItem() + ""), // tgl_perawatan
                            jam,                                      // jam_rawat (UNIK per iterasi)
                            tbTindakan.getValueAt(i, 6).toString(),   // material (Bagian RS)
                            tbTindakan.getValueAt(i, 7).toString(),   // bhp
                            tbTindakan.getValueAt(i, 8).toString(),   // tarif_tindakandr (JM Dokter)
                            tbTindakan.getValueAt(i, 10).toString(),  // kso
                            tbTindakan.getValueAt(i, 11).toString(),  // menejemen
                            tbTindakan.getValueAt(i, 5).toString()   // biaya_rawat (Tarif/Biaya)
                        }) == true) {

                            ttljmdokter += Double.parseDouble(tbTindakan.getValueAt(i, 8).toString());
                            ttlkso += Double.parseDouble(tbTindakan.getValueAt(i, 10).toString());
                            ttlpendapatan += Double.parseDouble(tbTindakan.getValueAt(i, 5).toString());
                            ttljasasarana += Double.parseDouble(tbTindakan.getValueAt(i, 6).toString());
                            ttlbhp += Double.parseDouble(tbTindakan.getValueAt(i, 7).toString());
                            ttlmenejemen += Double.parseDouble(tbTindakan.getValueAt(i, 11).toString());

                        } else {
                            sukses = false;
                            break;
                        }
                    }
                }                           
            }

            if (sukses) {
                Sequel.Commit();
                for (i = 0; i < tbTindakan.getRowCount(); i++) { 
                    tbTindakan.setValueAt(false, i, 0);
                    tbTindakan.setValueAt(1, i, 1);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                Sequel.RollBack();
            }

            Sequel.AutoComitTrue();

        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }
    
    private void SimpanPenangananPetugas() {
        try {
            Sequel.AutoComitFalse();
            sukses = true;
            ttljmperawat = 0;
            ttlkso = 0;
            ttlpendapatan = 0;
            ttljasasarana = 0;
            ttlbhp = 0;
            ttlmenejemen = 0;

            for (i = 0; i < tbTindakan2.getRowCount(); i++) { 
                if (tbTindakan2.getValueAt(i, 0).toString().equals("true")) {  

                    // Ambil jumlah dari kolom 1
                    int jumlah = Integer.parseInt(tbTindakan2.getValueAt(i, 1).toString());

                    for (int x = 0; x < jumlah; x++) {
                        // Buat jam unik dengan menambahkan x detik
                        String jam = Jam.getSelectedItem() + ":" + Menit.getSelectedItem() + ":" + Detik.getSelectedItem();

                        // Kalau jumlah > 1, tambahkan x detik untuk membuat jam unik
                        if (jumlah > 1) {
                            int detikAsli = Integer.parseInt(Detik.getSelectedItem().toString());
                            int detikBaru = detikAsli + x;
                            int menitBaru = Integer.parseInt(Menit.getSelectedItem().toString());
                            int jamBaru = Integer.parseInt(Jam.getSelectedItem().toString());

                            if (detikBaru >= 60) {
                                detikBaru = detikBaru % 60;
                                menitBaru++;
                            }
                            if (menitBaru >= 60) {
                                menitBaru = menitBaru % 60;
                                jamBaru++;
                            }

                            jam = String.format("%02d:%02d:%02d", jamBaru, menitBaru, detikBaru);
                        }

                        if (Sequel.menyimpantf("rawat_jl_pr", "?,?,?,?,?,?,?,?,?,?,?,'Belum'", "Tindakan", 11, new String[]{
                            TNoRw.getText(),                           // no_rawat
                            tbTindakan2.getValueAt(i, 2).toString(),  // kd_jenis_prw (Kode)
                            kdptg.getText(),                          // nip (Petugas)
                            Valid.SetTgl(Tanggal.getSelectedItem() + ""), // tgl_perawatan
                            jam,                                      // jam_rawat (UNIK per iterasi)
                            tbTindakan2.getValueAt(i, 6).toString(),  // material (Bagian RS)
                            tbTindakan2.getValueAt(i, 7).toString(),  // bhp
                            tbTindakan2.getValueAt(i, 9).toString(),  // tarif_tindakanpr (JM Perawat)
                            tbTindakan2.getValueAt(i, 10).toString(), // kso
                            tbTindakan2.getValueAt(i, 11).toString(), // menejemen
                            tbTindakan2.getValueAt(i, 5).toString()   // biaya_rawat (Tarif/Biaya)
                        }) == true) {

                            ttljmperawat += Double.parseDouble(tbTindakan2.getValueAt(i, 9).toString());
                            ttlkso += Double.parseDouble(tbTindakan2.getValueAt(i, 10).toString());
                            ttlpendapatan += Double.parseDouble(tbTindakan2.getValueAt(i, 5).toString());
                            ttljasasarana += Double.parseDouble(tbTindakan2.getValueAt(i, 6).toString());
                            ttlbhp += Double.parseDouble(tbTindakan2.getValueAt(i, 7).toString());
                            ttlmenejemen += Double.parseDouble(tbTindakan2.getValueAt(i, 11).toString());

                        } else {
                            sukses = false;
                            break;
                        }
                    }
                }                           
            }

            if (sukses) {
                Sequel.Commit();
                // ⚠️ PERBAIKAN: Reset tbTindakan2, bukan tbTindakan
                for (i = 0; i < tbTindakan2.getRowCount(); i++) { 
                    tbTindakan2.setValueAt(false, i, 0);
                    tbTindakan2.setValueAt(1, i, 1); // Reset jumlah ke 1
                }
            } else {
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                Sequel.RollBack();
            }

            Sequel.AutoComitTrue();

        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void hitungObat() {
        ttl = 0;
        y = 0;
        row2 = tabModeobat.getRowCount();
        for (r = 0; r < row2; r++) { 
            try {
                // Cek kolom P (index 0) terpilih, atau K (index 1)
                // Sesuaikan logika sesuai kebutuhan
                if (Boolean.parseBoolean(tabModeobat.getValueAt(r, 0).toString())) {
                    if (Double.parseDouble(tabModeobat.getValueAt(r, 2).toString()) > 0) {
                        try {                
                            y = Math.round(
                                Double.parseDouble(tabModeobat.getValueAt(r, 2).toString()) *   // Jumlah (index 2)
                                Double.parseDouble(tabModeobat.getValueAt(r, 7).toString()) +   // Harga (index 7)
                                Double.parseDouble(tabModeobat.getValueAt(r, 9).toString()) +   // Emb (index 9)
                                Double.parseDouble(tabModeobat.getValueAt(r, 10).toString())    // Tsl (index 10)
                            );                                                
                        } catch (Exception e) {
                            y = 0;
                        }
                        ttl = ttl + y;
                    }
                }
            } catch (Exception e) {
            }                           
        }

        LTotal.setText(Valid.SetAngka(ttl));
        ppnobat = 0;
        if (tampilkan_ppnobat_ralan.equals("Yes")) {
            ppnobat = Math.round(ttl * 0.11);
            ttl = ttl + ppnobat;
            LPpn.setText(Valid.SetAngka(ppnobat));
            LTotalTagihan.setText(Valid.SetAngka(ttl));
        }
    }
    
    private void simpan() {
        switch (TabRawat.getSelectedIndex()) { 
            case 0:
                if (Sequel.menyimpantf("pelayanan_hemodialisa", "?,?,?,?,?, ?,?,?,?,?", "Data", 10, new String[]{
                    TNoRw.getText(), 
                    Valid.SetTgl(Tanggal2.getSelectedItem() + ""),
                    Jam2.getSelectedItem() + ":" + Menit2.getSelectedItem() + ":" + Detik2.getSelectedItem(),
                    Jam3.getSelectedItem() + ":" + Menit3.getSelectedItem() + ":" + Detik3.getSelectedItem(),
                    Pelayanan.getSelectedItem().toString(),
                    PaketHD.getSelectedItem().toString(),
                    AksesVaskuler.getSelectedItem().toString(),
                    PelayananHD.getSelectedItem().toString(),
                    kddok2.getText(), 
                    kddok1.getText(), 
                }) == true) {
                    tampilDataLayananHD();
                    LCount.setText("" + tabDataLayananHD.getRowCount());
                    emptTeks();
                }
                break;
            
            case 1:
                if(KdDok.getText().trim().equals("")||TDokter.getText().trim().equals("")){
                    Valid.textKosong(KdDok,"Dokter");
                }else{                        
                    try {
                        jmlparsial=0;
                        if(aktifkanparsial.equals("yes")){
                            jmlparsial=Sequel.cariInteger("select count(set_input_parsial.kd_pj) from set_input_parsial where set_input_parsial.kd_pj=?",Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
                        }
                        if(jmlparsial>0){    
                            SimpanPenangananDokter();
                        }else{
                            if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                TCari.requestFocus();
                            }else{
                                SimpanPenangananDokter();
                            }
                        } 
                    } catch (Exception e) {
                    }                      
                } 
                break;
            case 2:
                if(kdptg.getText().trim().equals("")||TPerawat.getText().trim().equals("")){
                        Valid.textKosong(kdptg,"Petugas");
                    }else{
                        try {
                            jmlparsial=0;
                            if(aktifkanparsial.equals("yes")){
                                jmlparsial=Sequel.cariInteger("select count(set_input_parsial.kd_pj) from set_input_parsial where set_input_parsial.kd_pj=?",Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
                            }
                            if(jmlparsial>0){ 
                                SimpanPenangananPetugas();
                            }else{
                                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                    TCari.requestFocus();
                                }else{
                                    SimpanPenangananPetugas();
                                }
                            } 
                        } catch (Exception e) {
                        } 
                    }  
                    break;
            case 3: //sudah
                
                if(VALIDASIULANGBERIOBAT.equals("yes")){
                    for(i=0;i<tbObat.getRowCount();i++){ 
                        if(Valid.SetAngka(tbObat.getValueAt(i,2).toString())>0){  // Jumlah index 2 (was 1)
                            getDataobat(i);
                        } 
                    }
                }

                if(TNoRw.getText().trim().equals("")){
                    Valid.textKosong(TCari,"Data");
                }else if(kdgudang.getText().equals("")){
                    Valid.textKosong(TCari,"Lokasi");
                }else if(ttl<=0){
                    JOptionPane.showMessageDialog(null,"Maaf, silahkan masukkan terlebih dahulu obat yang mau diberikan...!!!");
                    TCari.requestFocus();
                }else{
                    int reply = JOptionPane.showConfirmDialog(rootPane,"Eeiiiiiits, udah bener belum data yang mau disimpan..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        try {
                            Sequel.AutoComitFalse();
                            sukses=true;
                            ttlhpp=0;ttljual=0;
                            for(i=0;i<tbObat.getRowCount();i++){ 
                                // Cek: P (index 0) terpilih DAN Jumlah (index 2) > 0
                                if(tbObat.getValueAt(i,0).toString().equals("true") && Valid.SetAngka(tbObat.getValueAt(i,2).toString())>0){
                                    pscarikapasitas= koneksi.prepareStatement("select IFNULL(databarang.kapasitas,1) from databarang where databarang.kode_brng=?");                                      
                                    try {
                                        pscarikapasitas.setString(1,tbObat.getValueAt(i,3).toString());  // Kode Barang index 3 (was 2)
                                        carikapasitas=pscarikapasitas.executeQuery();
                                        if(carikapasitas.next()){ 
                                            // Simpan dengan kapasitas
                                            if(Sequel.menyimpantf2("detail_pemberian_obat","?,?,?,?,?,?,?,?,?,?,?,?,?,?","data",14,new String[]{
                                                Valid.SetTgl(Tanggal.getSelectedItem()+""),
                                                Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                                                TNoRw.getText(),
                                                tbObat.getValueAt(i,3).toString(),      // Kode Barang index 3
                                                tbObat.getValueAt(i,14).toString(),     // H.Beli index 14 (was 13)
                                                tbObat.getValueAt(i,7).toString(),      // Harga(Rp) index 7 (was 6)
                                                ""+(Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)),  // Jumlah index 2
                                                tbObat.getValueAt(i,9).toString(),      // Emb index 9 (was 8)
                                                tbObat.getValueAt(i,10).toString(),     // Tsl index 10 (was 9)
                                                ""+Math.round(Double.parseDouble(tbObat.getValueAt(i,9).toString())+    // Emb index 9
                                                    Double.parseDouble(tbObat.getValueAt(i,10).toString())+              // Tsl index 10
                                                    (Double.parseDouble(tbObat.getValueAt(i,7).toString())*            // Harga index 7
                                                    (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)))),  // Jumlah index 2
                                                "Ralan",
                                                kdgudang.getText(),
                                                tbObat.getValueAt(i,17).toString(),     // No.Batch index 17 (was 16)
                                                tbObat.getValueAt(i,18).toString()      // No.Faktur index 18 (was 17)
                                            })==true){
                                                ttljual=ttljual+Math.round(Double.parseDouble(tbObat.getValueAt(i,9).toString())+    // Emb index 9
                                                        Double.parseDouble(tbObat.getValueAt(i,10).toString())+                       // Tsl index 10
                                                        (Double.parseDouble(tbObat.getValueAt(i,7).toString())*                        // Harga index 7
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1))));  // Jumlah index 2
                                                ttlhpp=ttlhpp+Math.round(Double.parseDouble(tbObat.getValueAt(i,14).toString())*     // H.Beli index 14
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)));  // Jumlah index 2

                                                // Aturan Pakai index 12 (was 11)
                                                if(!tbObat.getValueAt(i,12).toString().equals("")){
                                                    Sequel.menyimpan("aturan_pakai","?,?,?,?,?",5,new String[]{
                                                        Valid.SetTgl(Tanggal.getSelectedItem()+""),
                                                        Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                                                        TNoRw.getText(),
                                                        tbObat.getValueAt(i,3).toString(),   // Kode Barang index 3
                                                        tbObat.getValueAt(i,12).toString()   // Aturan Pakai index 12
                                                    });  
                                                }                                            

                                                if(aktifkanbatch.equals("yes")){
                                                    // Batch & Faktur index 17, 18
                                                    Sequel.mengedit("data_batch","no_batch=? and kode_brng=? and no_faktur=?","sisa=sisa-?",4,new String[]{
                                                        ""+(Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)),
                                                        tbObat.getValueAt(i,17).toString(),
                                                        tbObat.getValueAt(i,3).toString(),
                                                        tbObat.getValueAt(i,18).toString()
                                                    });
                                                    Trackobat.catatRiwayat(tbObat.getValueAt(i,3).toString(),0,
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)),
                                                        "Pemberian Obat",akses.getkode(),kdgudang.getText(),"Simpan",
                                                        tbObat.getValueAt(i,17).toString(),
                                                        tbObat.getValueAt(i,18).toString(),
                                                        TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                                    Sequel.menyimpan("gudangbarang",
                                                        "'"+tbObat.getValueAt(i,3).toString()+"','"+kdgudang.getText()+"','-"+
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1))+"','"+
                                                        tbObat.getValueAt(i,17).toString()+"','"+tbObat.getValueAt(i,18).toString()+"'", 
                                                        "stok=stok-'"+(Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1))+"'",
                                                        "kode_brng='"+tbObat.getValueAt(i,3).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='"+tbObat.getValueAt(i,17).toString()+"' and no_faktur='"+tbObat.getValueAt(i,18).toString()+"'");
                                                }else{
                                                    Trackobat.catatRiwayat(tbObat.getValueAt(i,3).toString(),0,
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1)),
                                                        "Pemberian Obat",akses.getkode(),kdgudang.getText(),"Simpan","","",
                                                        TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                                    Sequel.menyimpan("gudangbarang",
                                                        "'"+tbObat.getValueAt(i,3).toString()+"','"+kdgudang.getText()+"','-"+
                                                        (Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1))+"','',''", 
                                                        "stok=stok-'"+(Double.parseDouble(tbObat.getValueAt(i,2).toString())/carikapasitas.getDouble(1))+"'",
                                                        "kode_brng='"+tbObat.getValueAt(i,3).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='' and no_faktur=''"); 
                                                }
                                            }else{
                                                sukses=false;
                                            }  
                                        }else{
                                            // Simpan tanpa kapasitas (kode_brng tidak punya kapasitas)
                                            if(Sequel.menyimpantf2("detail_pemberian_obat","?,?,?,?,?,?,?,?,?,?,?,?,?,?","data",14,new String[]{
                                                Valid.SetTgl(Tanggal.getSelectedItem()+""),
                                                Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                                                TNoRw.getText(),
                                                tbObat.getValueAt(i,3).toString(),      // Kode Barang index 3
                                                tbObat.getValueAt(i,14).toString(),     // H.Beli index 14
                                                tbObat.getValueAt(i,7).toString(),      // Harga(Rp) index 7
                                                ""+Double.parseDouble(tbObat.getValueAt(i,2).toString()),  // Jumlah index 2
                                                tbObat.getValueAt(i,9).toString(),      // Emb index 9
                                                tbObat.getValueAt(i,10).toString(),     // Tsl index 10
                                                ""+Math.round(Double.parseDouble(tbObat.getValueAt(i,9).toString())+
                                                    Double.parseDouble(tbObat.getValueAt(i,10).toString())+
                                                    (Double.parseDouble(tbObat.getValueAt(i,7).toString())*
                                                    Double.parseDouble(tbObat.getValueAt(i,2).toString()))),
                                                "Ralan",
                                                kdgudang.getText(),
                                                tbObat.getValueAt(i,17).toString(),     // No.Batch index 17
                                                tbObat.getValueAt(i,18).toString()      // No.Faktur index 18
                                            })==true){
                                                ttljual=ttljual+Math.round(Double.parseDouble(tbObat.getValueAt(i,9).toString())+
                                                        Double.parseDouble(tbObat.getValueAt(i,10).toString())+
                                                        (Double.parseDouble(tbObat.getValueAt(i,7).toString())*
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString())));
                                                ttlhpp=ttlhpp+Math.round(Double.parseDouble(tbObat.getValueAt(i,14).toString())*
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString()));

                                                // Aturan Pakai index 12
                                                if(!tbObat.getValueAt(i,12).toString().equals("")){
                                                    Sequel.menyimpan("aturan_pakai","?,?,?,?,?",5,new String[]{
                                                        Valid.SetTgl(Tanggal.getSelectedItem()+""),
                                                        Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),
                                                        TNoRw.getText(),
                                                        tbObat.getValueAt(i,3).toString(),
                                                        tbObat.getValueAt(i,12).toString()
                                                    });  
                                                }                                              

                                                if(aktifkanbatch.equals("yes")){
                                                    Sequel.mengedit("data_batch","no_batch=? and kode_brng=? and no_faktur=?","sisa=sisa-?",4,new String[]{
                                                        ""+(Double.parseDouble(tbObat.getValueAt(i,2).toString())),
                                                        tbObat.getValueAt(i,17).toString(),
                                                        tbObat.getValueAt(i,3).toString(),
                                                        tbObat.getValueAt(i,18).toString()
                                                    });
                                                    Trackobat.catatRiwayat(tbObat.getValueAt(i,3).toString(),0,
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString()),
                                                        "Pemberian Obat",akses.getkode(),kdgudang.getText(),"Simpan",
                                                        tbObat.getValueAt(i,17).toString(),
                                                        tbObat.getValueAt(i,18).toString(),
                                                        TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                                    Sequel.menyimpan("gudangbarang",
                                                        "'"+tbObat.getValueAt(i,3).toString()+"','"+kdgudang.getText()+"','-"+
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString())+"','"+
                                                        tbObat.getValueAt(i,17).toString()+"','"+tbObat.getValueAt(i,18).toString()+"'", 
                                                        "stok=stok-'"+Double.parseDouble(tbObat.getValueAt(i,2).toString())+"'",
                                                        "kode_brng='"+tbObat.getValueAt(i,3).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='"+tbObat.getValueAt(i,17).toString()+"' and no_faktur='"+tbObat.getValueAt(i,18).toString()+"'");   
                                                }else{ 
                                                    Trackobat.catatRiwayat(tbObat.getValueAt(i,3).toString(),0,
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString()),
                                                        "Pemberian Obat",akses.getkode(),kdgudang.getText(),"Simpan","","",
                                                        TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                                    Sequel.menyimpan("gudangbarang",
                                                        "'"+tbObat.getValueAt(i,3).toString()+"','"+kdgudang.getText()+"','-"+
                                                        Double.parseDouble(tbObat.getValueAt(i,2).toString())+"','',''", 
                                                        "stok=stok-'"+Double.parseDouble(tbObat.getValueAt(i,2).toString())+"'",
                                                        "kode_brng='"+tbObat.getValueAt(i,3).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='' and no_faktur=''");  
                                                }
                                            }else{
                                                sukses=false;
                                            }                                   
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Notifikasi Kapasitas : "+e);
                                    } finally{
                                        if(carikapasitas!=null){
                                            carikapasitas.close();
                                        }
                                        if(pscarikapasitas!=null){
                                            pscarikapasitas.close();
                                        }
                                    }
                                }
                                // Hapus blok ELSE yang simpan tanpa cek P - karena hanya yang dipilih yang disimpan
                            }

                            if(!noresep.equals("")){
                                Sequel.mengedit("resep_obat","no_resep='"+noresep+"'","tgl_perawatan='"+Valid.SetTgl(Tanggal.getSelectedItem()+"")+"',jam='"+Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem()+"'");
                            }

                            if(sukses==true){
                                Sequel.Commit();
                                for(i=0;i<tbObat.getRowCount();i++){
                                    tbObat.setValueAt(false,i,0);  // Reset P ke false
                                    tbObat.setValueAt(false,i,1);  // Reset K ke false (jika perlu)
                                }
                                LTotal.setText("0");
                                LPpn.setText("0");
                                LTotalTagihan.setText("0");

                                JOptionPane.showMessageDialog(null,"Data Berhasil disimpan.");
                            }else{
                                sukses=false;
                                JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                                Sequel.RollBack();
                            }

                            Sequel.AutoComitTrue();
                        } catch (Exception ex) {
                            System.out.println(ex);                
                        }
                    }                
                }
                break;
            default:
        }
    }
    
private void getDataobat(int data) {        
        try {            
            stokbarang=0;  
            if(aktifkanbatch.equals("yes")){
                psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch=? and gudangbarang.no_faktur=?");
                try {
                    psstok.setString(1,kdgudang.getText());
                    psstok.setString(2,tbObat.getValueAt(data,3).toString());   // Kode Barang index 3 (was 2)
                    psstok.setString(3,tbObat.getValueAt(data,17).toString());  // No.Batch index 17 (was 16)
                    psstok.setString(4,tbObat.getValueAt(data,18).toString());  // No.Faktur index 18 (was 17)
                    rsstok=psstok.executeQuery();
                    if(rsstok.next()){
                        stokbarang=rsstok.getDouble(1);
                    }                                
                } catch (Exception e) {
                    stokbarang=0;
                    System.out.println("Notifikasi : "+e);
                }finally{
                    if(rsstok != null){
                        rsstok.close();
                    }
                    if(psstok != null){
                        psstok.close();
                    }
                }
            }else{
                psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch='' and gudangbarang.no_faktur=''");
                try {
                    psstok.setString(1,kdgudang.getText());
                    psstok.setString(2,tbObat.getValueAt(data,3).toString());   // Kode Barang index 3 (was 2)
                    rsstok=psstok.executeQuery();
                    if(rsstok.next()){
                        stokbarang=rsstok.getDouble(1);
                    }                                
                } catch (Exception e) {
                    stokbarang=0;
                    System.out.println("Notifikasi : "+e);
                }finally{
                    if(rsstok != null){
                        rsstok.close();
                    }
                    if(psstok != null){
                        psstok.close();
                    }
                }
            }

            // Stok pindah ke index 11 (was 10)
            tbObat.setValueAt(stokbarang,data,11);

            y=0;
            try {
                // Cek kolom K (index 1) untuk kapasitas, bukan P (index 0)
                // Atau kalau memang cek P, biarkan saja index 0
                if(tbObat.getValueAt(data,1).toString().equals("true")){  // K (index 1) — sesuaikan kebutuhan
                    pscarikapasitas= koneksi.prepareStatement("select IFNULL(databarang.kapasitas,1) from databarang where databarang.kode_brng=?");                                      
                    try {
                        pscarikapasitas.setString(1,tbObat.getValueAt(data,3).toString());  // Kode Barang index 3
                        carikapasitas=pscarikapasitas.executeQuery();
                        if(carikapasitas.next()){ 
                            y=Double.parseDouble(tbObat.getValueAt(data,2).toString())/carikapasitas.getDouble(1);  // Jumlah index 2
                        }else{
                            y=Double.parseDouble(tbObat.getValueAt(data,2).toString());  // Jumlah index 2
                        }
                    } catch (Exception e) {
                        y=Double.parseDouble(tbObat.getValueAt(data,2).toString());  // Jumlah index 2
                        System.out.println("Kapasitasmu masih kosong broooh : "+e);
                    } finally{
                        if(carikapasitas!=null){
                            carikapasitas.close();
                        }
                        if(pscarikapasitas!=null){
                            pscarikapasitas.close();
                        }
                    }
                }else{
                    y=Double.parseDouble(tbObat.getValueAt(data,2).toString());  // Jumlah index 2
                }                        
            } catch (Exception e) {
                y=0;
            }
            if(stokbarang<y){
                JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
            }
        } catch (Exception e) {
            tbObat.setValueAt(0,data,11);  // Stok index 11 (was 10)
        } 
    }

    private void hapus() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                if(tabModeDr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    Sequel.AutoComitFalse();
                    sukses=true;
                    ttljmdokter=0;ttljmperawat=0;ttlkso=0;ttlpendapatan=0;ttljasasarana=0;ttlbhp=0;ttlmenejemen=0;
                    for(i=0;i<tbRawatDr.getRowCount();i++){
                        if(tbRawatDr.getValueAt(i,0).toString().equals("true")){                            
                            if(Sequel.cariRegistrasi(tbRawatDr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                tbRawatDr.setValueAt(false,i,0);
                                TCari.requestFocus();
                            }else{
                                if(Sequel.queryutf("delete from rawat_jl_dr where no_rawat='"+tbRawatDr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatDr.getValueAt(i,10)+
                                        "' and kd_dokter='"+tbRawatDr.getValueAt(i,5).toString()+
                                        "' and tgl_perawatan='"+tbRawatDr.getValueAt(i,7).toString()+
                                        "' and jam_rawat='"+tbRawatDr.getValueAt(i,8).toString()+"'")==true){
                                    ttljmdokter=ttljmdokter+Double.parseDouble(tbRawatDr.getValueAt(i,11).toString());
                                    ttlkso=ttlkso+Double.parseDouble(tbRawatDr.getValueAt(i,12).toString());
                                    ttlpendapatan=ttlpendapatan+Double.parseDouble(tbRawatDr.getValueAt(i,9).toString());
                                    ttljasasarana=ttljasasarana+Double.parseDouble(tbRawatDr.getValueAt(i,13).toString());
                                    ttlbhp=ttlbhp+Double.parseDouble(tbRawatDr.getValueAt(i,14).toString());
                                    ttlmenejemen=ttlmenejemen+Double.parseDouble(tbRawatDr.getValueAt(i,15).toString());
                                }else{
                                    sukses=false;
                                }
                            }
                        }                            
                    }
                      
                    if(sukses==true){
                        Sequel.Commit();
                        for(i=0;i<tbRawatDr.getRowCount();i++){
                            if(tbRawatDr.getValueAt(i,0).toString().equals("true")){ 
                                tabModeDr.removeRow(i);
                                i--;
                            }
                        }
                        LCount.setText(""+tabModeDr.getRowCount());
                    }else{
                        sukses=false;
                        JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                        Sequel.RollBack();
                    }
                    Sequel.AutoComitTrue();
                }   break;
            case 2:
                if(tabModePr.getRowCount()==0){
                    JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
                    TNoRw.requestFocus();
                }else{
                    Sequel.AutoComitFalse();
                    sukses=true;
                    ttljmdokter=0;ttljmperawat=0;ttlkso=0;ttlpendapatan=0;ttljasasarana=0;ttlbhp=0;ttlmenejemen=0;
                    for(i=0;i<tbRawatPr.getRowCount();i++){
                        if(tbRawatPr.getValueAt(i,0).toString().equals("true")){
                            if(Sequel.cariRegistrasi(tbRawatPr.getValueAt(i,1).toString())>0){
                                JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                                tbRawatPr.setValueAt(false,i,0);
                                TCari.requestFocus();
                            }else{
                                if(Sequel.queryutf("delete from rawat_jl_pr where no_rawat='"+tbRawatPr.getValueAt(i,1).toString()+
                                        "' and kd_jenis_prw='"+tbRawatPr.getValueAt(i,10)+
                                        "' and nip='"+tbRawatPr.getValueAt(i,5).toString()+
                                        "' and tgl_perawatan='"+tbRawatPr.getValueAt(i,7).toString()+
                                        "' and jam_rawat='"+tbRawatPr.getValueAt(i,8).toString()+"' ")==true){
                                    ttljmperawat=ttljmperawat+Double.parseDouble(tbRawatPr.getValueAt(i,11).toString());
                                    ttlkso=ttlkso+Double.parseDouble(tbRawatPr.getValueAt(i,12).toString());
                                    ttlpendapatan=ttlpendapatan+Double.parseDouble(tbRawatPr.getValueAt(i,9).toString());
                                    ttljasasarana=ttljasasarana+Double.parseDouble(tbRawatPr.getValueAt(i,13).toString());
                                    ttlbhp=ttlbhp+Double.parseDouble(tbRawatPr.getValueAt(i,14).toString());
                                    ttlmenejemen=ttlmenejemen+Double.parseDouble(tbRawatPr.getValueAt(i,15).toString());
                                }else{
                                    sukses=false;
                                }
                            }
                        }
                    }
                        
                    if(sukses==true){
                        Sequel.Commit();
                        for(i=0;i<tbRawatPr.getRowCount();i++){
                            if(tbRawatPr.getValueAt(i,0).toString().equals("true")){ 
                                tabModePr.removeRow(i);
                                i--;
                            }
                        }
                        LCount.setText(""+tabModePr.getRowCount());
                    }else{
                        sukses=false;
                        JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
                        Sequel.RollBack();
                    }
                    Sequel.AutoComitTrue();
                }   break;
            case 3:
                break;
            case 4:
                 if (Sequel.queryu2tf("delete from pelayanan_hemodialisa_terapi where no_rawat=?", 1, new String[]{
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

    private void getDataobat() {
        if(nmgudang.getText().trim().equals("")){
            Valid.textKosong(kdgudang,"Lokasi");
        }else{
            if(tbObat.getSelectedRow()!= -1){
                row=tbObat.getSelectedRow();
                if(!tbObat.getValueAt(row,1).toString().equals("")){
                    try {
                        if(Double.parseDouble(tbObat.getValueAt(row,1).toString())>0){
                            stokbarang=0;  
                            if(aktifkanbatch.equals("yes")){
                                psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch=? and gudangbarang.no_faktur=?");
                                try {
                                    psstok.setString(1,kdgudang.getText());
                                    psstok.setString(2,tbObat.getValueAt(row,2).toString());
                                    psstok.setString(3,tbObat.getValueAt(row,16).toString());
                                    psstok.setString(4,tbObat.getValueAt(row,17).toString());
                                    rsstok=psstok.executeQuery();
                                    if(rsstok.next()){
                                        stokbarang=rsstok.getDouble(1);
                                    }                                
                                } catch (Exception e) {
                                    stokbarang=0;
                                    System.out.println("Notifikasi : "+e);
                                }finally{
                                    if(rsstok != null){
                                        rsstok.close();
                                    }
                                    if(psstok != null){
                                        psstok.close();
                                    }
                                }
                            }else{
                                psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch='' and gudangbarang.no_faktur=''");
                                try {
                                    psstok.setString(1,kdgudang.getText());
                                    psstok.setString(2,tbObat.getValueAt(row,2).toString());
                                    rsstok=psstok.executeQuery();
                                    if(rsstok.next()){
                                        stokbarang=rsstok.getDouble(1);
                                    }                                
                                } catch (Exception e) {
                                    stokbarang=0;
                                    System.out.println("Notifikasi : "+e);
                                }finally{
                                    if(rsstok != null){
                                        rsstok.close();
                                    }
                                    if(psstok != null){
                                        psstok.close();
                                    }
                                }
                            }

                            tbObat.setValueAt(stokbarang,row,10);

                            y=0;
                            try {
                                if(tbObat.getValueAt(row,0).toString().equals("true")){
                                    pscarikapasitas= koneksi.prepareStatement("select IFNULL(databarang.kapasitas,1) from databarang where databarang.kode_brng=?");                                      
                                    try {
                                        pscarikapasitas.setString(1,tbObat.getValueAt(row,2).toString());
                                        carikapasitas=pscarikapasitas.executeQuery();
                                        if(carikapasitas.next()){ 
                                            y=Double.parseDouble(tbObat.getValueAt(row,1).toString())/carikapasitas.getDouble(1);
                                        }else{
                                            y=Double.parseDouble(tbObat.getValueAt(row,1).toString());
                                        }
                                    } catch (Exception e) {
                                        y=Double.parseDouble(tbObat.getValueAt(row,1).toString());
                                        System.out.println("Kapasitasmu masih kosong broooh : "+e);
                                    } finally{
                                        if(carikapasitas!=null){
                                            carikapasitas.close();
                                        }
                                        if(pscarikapasitas!=null){
                                            pscarikapasitas.close();
                                        }
                                    }
                                }else{
                                    y=Double.parseDouble(tbObat.getValueAt(row,1).toString());
                                }                        
                            } catch (Exception e) {
                                y=0;
                            }

                            if(stokbarang<y){
                                JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                tbObat.setValueAt("",row,1);
                            }
                        }
                    } catch (Exception e) {
                        tbObat.setValueAt("",row,1);
                        tbObat.setValueAt(0,row,10);
                    } 
                }
                if((tbObat.getSelectedColumn()==16)||(tbObat.getSelectedColumn()==17)){ 
                    hitungObat();
                }
            }
        }       
    }
    
    private void ganti() {
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:

                Sequel.mengedit("pelayanan_hemodialisa_terapi", "no_rawat=?", "terapi_obat_lain=?", 2, new String[]{
                    TerapiLain.getText(),TNoRw.getText()
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

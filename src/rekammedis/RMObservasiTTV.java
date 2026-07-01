/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package rekammedis;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;


/**
 *
 * @author perpustakaan
 */
public final class RMObservasiTTV extends javax.swing.JDialog {
    private Timer timerObservasi;
    private int idIntraAktif = 0;
    private int intervalKe = 0;
    private boolean isAnestesiAktif = false;
    private javax.swing.Timer timerCountdown;

    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i=0,pilihan=0;    
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String finger="",finger2="";
    private StringBuilder htmlContent;
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMObservasiTTV(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
            "ID TTV", "ID Intra", "No.Rawat", "Nama Pasien", 
            "Interval", "Waktu Obs", "TD Sys", "TD Dia", "MAP", 
            "Nadi", "RR", "SpO2", "Suhu", "EtCO2", "FiO2", 
            "Status", "Kejadian", "Tindakan", "Kode Petugas", "Petugas"
        }){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        // Lebar kolom
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 20; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if (i == 0) column.setPreferredWidth(50);      // ID TTV
            else if (i == 1) column.setPreferredWidth(50);  // ID Intra
            else if (i == 2) column.setPreferredWidth(100);  // No.Rawat
            else if (i == 3) column.setPreferredWidth(150); // Nama Pasien
            else if (i == 4) column.setPreferredWidth(50);  // Interval
            else if (i == 5) column.setPreferredWidth(120);  // Waktu Obs
            else if (i == 6) column.setPreferredWidth(50);  // TD Sys
            else if (i == 7) column.setPreferredWidth(50);  // TD Dia
            else if (i == 8) column.setPreferredWidth(45);  // MAP
            else if (i == 9) column.setPreferredWidth(45);  // Nadi
            else if (i == 10) column.setPreferredWidth(35); // RR
            else if (i == 11) column.setPreferredWidth(45); // SpO2
            else if (i == 12) column.setPreferredWidth(45); // Suhu
            else if (i == 13) column.setPreferredWidth(50); // EtCO2
            else if (i == 14) column.setPreferredWidth(45); // FiO2
            else if (i == 15) column.setPreferredWidth(70); // Status
            else if (i == 16) column.setPreferredWidth(150); // Kejadian
            else if (i == 17) column.setPreferredWidth(150); // Tindakan
            else if (i == 18) column.setPreferredWidth(100); // Kode Petugas
            else if (i == 19) column.setPreferredWidth(100); // Petugas
        }
        
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        petugas.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(petugas.getTable().getSelectedRow()!= -1){     
                    KdPetugasOK.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),0).toString());
                    NmPetugasOK.setText(petugas.getTable().getValueAt(petugas.getTable().getSelectedRow(),1).toString());
                }   
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
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        }); 
        
        ChkInput.setSelected(false);
        isForm();
        
        HTMLEditorKit kit = new HTMLEditorKit();
        LoadHTML.setEditable(true);
        LoadHTML.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
                ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTML.setDocument(doc);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnSignInSebelumAnestesi = new javax.swing.JMenuItem();
        MnObservasiTTV = new javax.swing.JMenuItem();
        LoadHTML = new widget.editorpane();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
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
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel26 = new widget.Label();
        KdPetugasOK = new widget.TextBox();
        NmPetugasOK = new widget.TextBox();
        jLabel5 = new widget.Label();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        Waktu = new widget.Tanggal();
        jLabel33 = new widget.Label();
        Interval = new widget.TextBox();
        Sistole = new widget.TextBox();
        jLabel35 = new widget.Label();
        Diastole = new widget.TextBox();
        StatusHemo = new widget.ComboBox();
        jLabel56 = new widget.Label();
        RR = new widget.TextBox();
        Oksigen = new widget.TextBox();
        jLabel60 = new widget.Label();
        jLabel66 = new widget.Label();
        FIO = new widget.TextBox();
        ETCO = new widget.TextBox();
        jLabel68 = new widget.Label();
        jLabel71 = new widget.Label();
        Suhu = new widget.TextBox();
        jLabel72 = new widget.Label();
        Map = new widget.TextBox();
        jLabel75 = new widget.Label();
        Nadi = new widget.TextBox();
        jLabel77 = new widget.Label();
        scrollPane3 = new widget.ScrollPane();
        KejadianKhusus = new widget.TextArea();
        jLabel78 = new widget.Label();
        jLabel79 = new widget.Label();
        scrollPane4 = new widget.ScrollPane();
        TindakanRespons = new widget.TextArea();
        jLabel31 = new widget.Label();
        jLabel32 = new widget.Label();
        jLabel34 = new widget.Label();
        jLabel36 = new widget.Label();
        jLabel37 = new widget.Label();
        jLabel38 = new widget.Label();
        jLabel39 = new widget.Label();
        jLabel40 = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnSignInSebelumAnestesi.setBackground(new java.awt.Color(255, 255, 254));
        MnSignInSebelumAnestesi.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnSignInSebelumAnestesi.setForeground(new java.awt.Color(50, 50, 50));
        MnSignInSebelumAnestesi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnSignInSebelumAnestesi.setText("Formulir Sign-In Sebelum Tindakan Anestesi");
        MnSignInSebelumAnestesi.setName("MnSignInSebelumAnestesi"); // NOI18N
        MnSignInSebelumAnestesi.setPreferredSize(new java.awt.Dimension(270, 26));
        MnSignInSebelumAnestesi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnSignInSebelumAnestesiActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnSignInSebelumAnestesi);

        MnObservasiTTV.setBackground(new java.awt.Color(255, 255, 254));
        MnObservasiTTV.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnObservasiTTV.setForeground(new java.awt.Color(50, 50, 50));
        MnObservasiTTV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnObservasiTTV.setText("Formulir Sign-In Sebelum Tindakan Anestesi");
        MnObservasiTTV.setName("MnObservasiTTV"); // NOI18N
        MnObservasiTTV.setPreferredSize(new java.awt.Dimension(270, 26));
        MnObservasiTTV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnObservasiTTVActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnObservasiTTV);

        LoadHTML.setBorder(null);
        LoadHTML.setName("LoadHTML"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Observasi TTV Intra Anestesi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

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
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-06-2026" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-06-2026" }));
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

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 336));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

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

        scrollInput.setName("scrollInput"); // NOI18N
        scrollInput.setPreferredSize(new java.awt.Dimension(102, 557));

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 280));
        FormInput.setLayout(null);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("No.Rawat");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(21, 10, 75, 23);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(79, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(336, 10, 460, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(222, 10, 112, 23);

        jLabel16.setText("Waktu Obs :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(0, 40, 75, 23);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setText("Perawat Kamar Operasi");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(230, 40, 130, 23);

        KdPetugasOK.setEditable(false);
        KdPetugasOK.setHighlighter(null);
        KdPetugasOK.setName("KdPetugasOK"); // NOI18N
        FormInput.add(KdPetugasOK);
        KdPetugasOK.setBounds(360, 40, 110, 23);

        NmPetugasOK.setEditable(false);
        NmPetugasOK.setName("NmPetugasOK"); // NOI18N
        NmPetugasOK.setPreferredSize(new java.awt.Dimension(64, 27));
        FormInput.add(NmPetugasOK);
        NmPetugasOK.setBounds(470, 40, 326, 23);

        jLabel5.setText(":");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 75, 23);

        jSeparator1.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator1.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator1.setName("jSeparator1"); // NOI18N
        FormInput.add(jSeparator1);
        jSeparator1.setBounds(0, 100, 810, 1);

        jSeparator2.setBackground(new java.awt.Color(239, 244, 234));
        jSeparator2.setForeground(new java.awt.Color(239, 244, 234));
        jSeparator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 244, 234)));
        jSeparator2.setName("jSeparator2"); // NOI18N
        FormInput.add(jSeparator2);
        jSeparator2.setBounds(0, 100, 810, 1);

        Waktu.setForeground(new java.awt.Color(50, 70, 50));
        Waktu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "17-06-2026 16:51:16" }));
        Waktu.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        Waktu.setName("Waktu"); // NOI18N
        Waktu.setOpaque(false);
        Waktu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WaktuKeyPressed(evt);
            }
        });
        FormInput.add(Waktu);
        Waktu.setBounds(80, 40, 130, 23);

        jLabel33.setText("Interval Ke : ");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput.add(jLabel33);
        jLabel33.setBounds(0, 70, 80, 23);

        Interval.setHighlighter(null);
        Interval.setName("Interval"); // NOI18N
        Interval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IntervalActionPerformed(evt);
            }
        });
        Interval.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                IntervalKeyPressed(evt);
            }
        });
        FormInput.add(Interval);
        Interval.setBounds(80, 70, 130, 23);

        Sistole.setHighlighter(null);
        Sistole.setName("Sistole"); // NOI18N
        Sistole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SistoleKeyPressed(evt);
            }
        });
        FormInput.add(Sistole);
        Sistole.setBounds(120, 110, 80, 23);

        jLabel35.setText("Diastole :");
        jLabel35.setName("jLabel35"); // NOI18N
        FormInput.add(jLabel35);
        jLabel35.setBounds(30, 140, 80, 23);

        Diastole.setHighlighter(null);
        Diastole.setName("Diastole"); // NOI18N
        Diastole.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DiastoleKeyPressed(evt);
            }
        });
        FormInput.add(Diastole);
        Diastole.setBounds(120, 140, 80, 23);

        StatusHemo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stabil", "Tidak Stabil", "Kritis" }));
        StatusHemo.setName("StatusHemo"); // NOI18N
        StatusHemo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                StatusHemoKeyPressed(evt);
            }
        });
        FormInput.add(StatusHemo);
        StatusHemo.setBounds(360, 70, 125, 23);

        jLabel56.setText("Respiratory Rate :");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput.add(jLabel56);
        jLabel56.setBounds(260, 110, 130, 23);

        RR.setHighlighter(null);
        RR.setName("RR"); // NOI18N
        RR.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                RRKeyPressed(evt);
            }
        });
        FormInput.add(RR);
        RR.setBounds(400, 110, 80, 23);

        Oksigen.setHighlighter(null);
        Oksigen.setName("Oksigen"); // NOI18N
        Oksigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OksigenKeyPressed(evt);
            }
        });
        FormInput.add(Oksigen);
        Oksigen.setBounds(400, 140, 80, 23);

        jLabel60.setText("Saturasi Oksigen :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(280, 140, 110, 23);

        jLabel66.setText("End-Tidal CO2 :");
        jLabel66.setName("jLabel66"); // NOI18N
        FormInput.add(jLabel66);
        jLabel66.setBounds(570, 110, 90, 23);

        FIO.setHighlighter(null);
        FIO.setName("FIO"); // NOI18N
        FIO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FIOKeyPressed(evt);
            }
        });
        FormInput.add(FIO);
        FIO.setBounds(670, 140, 80, 23);

        ETCO.setHighlighter(null);
        ETCO.setName("ETCO"); // NOI18N
        ETCO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ETCOKeyPressed(evt);
            }
        });
        FormInput.add(ETCO);
        ETCO.setBounds(670, 110, 80, 23);

        jLabel68.setText("Fraksi O2 :");
        jLabel68.setName("jLabel68"); // NOI18N
        FormInput.add(jLabel68);
        jLabel68.setBounds(580, 140, 80, 23);

        jLabel71.setText("Map :");
        jLabel71.setName("jLabel71"); // NOI18N
        FormInput.add(jLabel71);
        jLabel71.setBounds(30, 170, 80, 23);

        Suhu.setHighlighter(null);
        Suhu.setName("Suhu"); // NOI18N
        Suhu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SuhuKeyPressed(evt);
            }
        });
        FormInput.add(Suhu);
        Suhu.setBounds(400, 170, 80, 23);

        jLabel72.setText("Status Hemodinamik :");
        jLabel72.setName("jLabel72"); // NOI18N
        FormInput.add(jLabel72);
        jLabel72.setBounds(230, 70, 120, 23);

        Map.setHighlighter(null);
        Map.setName("Map"); // NOI18N
        Map.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MapKeyPressed(evt);
            }
        });
        FormInput.add(Map);
        Map.setBounds(120, 170, 80, 23);

        jLabel75.setText("Kejadian Khusus :");
        jLabel75.setName("jLabel75"); // NOI18N
        FormInput.add(jLabel75);
        jLabel75.setBounds(10, 210, 100, 23);

        Nadi.setHighlighter(null);
        Nadi.setName("Nadi"); // NOI18N
        Nadi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NadiKeyPressed(evt);
            }
        });
        FormInput.add(Nadi);
        Nadi.setBounds(670, 170, 80, 23);

        jLabel77.setText("Suhu tubuh :");
        jLabel77.setName("jLabel77"); // NOI18N
        FormInput.add(jLabel77);
        jLabel77.setBounds(310, 170, 80, 23);

        scrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane3.setName("scrollPane3"); // NOI18N

        KejadianKhusus.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        KejadianKhusus.setColumns(20);
        KejadianKhusus.setRows(5);
        KejadianKhusus.setName("KejadianKhusus"); // NOI18N
        KejadianKhusus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KejadianKhususKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(KejadianKhusus);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(120, 210, 260, 60);

        jLabel78.setText("Nadi :");
        jLabel78.setName("jLabel78"); // NOI18N
        FormInput.add(jLabel78);
        jLabel78.setBounds(580, 170, 80, 23);

        jLabel79.setText("Tindakan Respons : ");
        jLabel79.setName("jLabel79"); // NOI18N
        FormInput.add(jLabel79);
        jLabel79.setBounds(390, 210, 110, 23);

        scrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        scrollPane4.setName("scrollPane4"); // NOI18N

        TindakanRespons.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TindakanRespons.setColumns(20);
        TindakanRespons.setRows(5);
        TindakanRespons.setName("TindakanRespons"); // NOI18N
        TindakanRespons.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TindakanResponsKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(TindakanRespons);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(510, 210, 260, 60);

        jLabel31.setText("Sistole :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(30, 110, 80, 23);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText("mmHg");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(210, 110, 40, 23);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("mmHg");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput.add(jLabel34);
        jLabel34.setBounds(210, 140, 40, 23);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setText("/menit");
        jLabel36.setName("jLabel36"); // NOI18N
        FormInput.add(jLabel36);
        jLabel36.setBounds(760, 170, 40, 23);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel37.setText("%");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput.add(jLabel37);
        jLabel37.setBounds(490, 140, 40, 23);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel38.setText("°C");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput.add(jLabel38);
        jLabel38.setBounds(490, 170, 40, 23);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel39.setText("mmHg");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput.add(jLabel39);
        jLabel39.setBounds(760, 110, 40, 23);

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setText("/menit");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput.add(jLabel40);
        jLabel40.setBounds(490, 110, 40, 23);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed

        if (TNoRw.getText().trim().equals("") || TPasien.getText().trim().equals("")) {
            Valid.textKosong(TNoRw, "pasien");
            return;
        }

        // Ambil id_intra dari form intra (harus sudah di-set sebelumnya)
        if (idIntraAktif == 0) {
            JOptionPane.showMessageDialog(null, "ID Intra Anestesi belum di-set!");
            return;
        }

        int id_ttv = Sequel.cariInteger("SELECT id_ttv FROM ttv_observasi ORDER BY id_ttv DESC LIMIT 1") + 1;

        // Hitung MAP otomatis
        String map = hitungMAP(Sistole.getText(), Diastole.getText());

        // 17 parameter = 18 tanda tanya
        if (Sequel.menyimpantf("ttv_observasi",
            "?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?",
            "Data", 18,
            new String[]{
                TNoRw.getText(),
                String.valueOf(id_ttv),         // 1 id_ttv
                String.valueOf(idIntraAktif),   // 2 id_intra
                Valid.SetTgl(Waktu.getSelectedItem() + "") + " " + Waktu.getSelectedItem().toString().substring(11, 19), // 3 waktu_obs
                Interval.getText(),             // 4 interval_ke
                Sistole.getText(),              // 5 td_sistole
                Diastole.getText(),             // 6 td_diastole
                map,                            // 7 map (auto)
                Nadi.getText(),                 // 8 nadi
                RR.getText(),                   // 9 rr
                Oksigen.getText(),              // 10 spo2
                Suhu.getText(),                 // 11 suhu
                ETCO.getText(),                 // 12 etco2
                FIO.getText(),                  // 13 fio2
                StatusHemo.getSelectedItem().toString(), // 14 status_hemodinamik
                KejadianKhusus.getText(),       // 15 kejadian_khusus
                TindakanRespons.getText(),      // 16 tindakan_respons
                KdPetugasOK.getText()           // 17 kd_petugas
            }) == true) {

            tampil();
            emptTeks();
            JOptionPane.showMessageDialog(null, "Data TTV berhasil disimpan!");
        }
}

// Helper hitung MAP
private String hitungMAP(String sys, String dia) {
    try {
        int s = Integer.parseInt(sys);
        int d = Integer.parseInt(dia);
        return String.valueOf(d + (s - d) / 3);
    } catch (Exception e) {
        return "";
    }

}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        BtnSimpanActionPerformed(null);
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            hapus();
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }   
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().trim().equals("")||TPasien.getText().trim().equals("")){
            Valid.textKosong(TNoRw,"pasien");
        } else {  
            if(tbObat.getSelectedRow()>-1){
                ganti();
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        petugas.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            try{
                if(TCari.getText().trim().equals("")){
                    ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                        "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                        "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                        "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                        "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                        "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                        "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                        "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                        "where signin_sebelum_anestesi.tanggal between ? and ? order by signin_sebelum_anestesi.tanggal ");
                }else{
                    ps=koneksi.prepareStatement(
                        "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                        "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                        "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                        "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                        "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                        "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                        "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                        "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                        "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                        "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                        "where signin_sebelum_anestesi.tanggal between ? and ? and (reg_periksa.no_rawat like ? or pasien.no_rkm_medis like ? or "+
                        "pasien.nm_pasien like ? or dokterbedah.nm_dokter like ? or dokteranestesi.nm_dokter like ? or petugas.nama like ?) "+
                        "order by signin_sebelum_anestesi.tanggal ");
                }

                try {
                    if(TCari.getText().trim().equals("")){
                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                    }else{
                        ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+"")+" 00:00:00");
                        ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+"")+" 23:59:59");
                        ps.setString(3,"%"+TCari.getText()+"%");
                        ps.setString(4,"%"+TCari.getText()+"%");
                        ps.setString(5,"%"+TCari.getText()+"%");
                        ps.setString(6,"%"+TCari.getText()+"%");
                        ps.setString(7,"%"+TCari.getText()+"%");
                        ps.setString(8,"%"+TCari.getText()+"%");
                    }
                    rs=ps.executeQuery();
                    htmlContent = new StringBuilder();
                    htmlContent.append(                             
                        "<tr class='isi'>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.Rawat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>No.RM</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Pasien</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tgl.Lahir</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>J.K.</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tanggal</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>SN/CN</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Tindakan</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kode Dokter Bedah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Dokter Bedah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kode Dokter Anest</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Nama Dokter Anestesi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Identitas</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Alergi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Area Operasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Resiko Aspirasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Antisipasi Resiko Aspirasi</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Kehilangan Darah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Jalur IV Line</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Antisipasi Resiko Kehilangan Darah</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Alat & Obat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Rencana Antisipasi Ketidaklengkapan Alat & Obat</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>NIP OK</b></td>"+
                            "<td valign='middle' bgcolor='#FFFAF8' align='center'><b>Petugas Ruang OK</b></td>"+
                        "</tr>"
                    );
                    while(rs.next()){
                        htmlContent.append(
                            "<tr class='isi'>"+
                               "<td valign='top'>"+rs.getString("no_rawat")+"</td>"+
                               "<td valign='top'>"+rs.getString("no_rkm_medis")+"</td>"+
                               "<td valign='top'>"+rs.getString("nm_pasien")+"</td>"+
                               "<td valign='top'>"+rs.getString("tgl_lahir")+"</td>"+
                               "<td valign='top'>"+rs.getString("jk")+"</td>"+
                               "<td valign='top'>"+rs.getString("tanggal")+"</td>"+
                               "<td valign='top'>"+rs.getString("sncn")+"</td>"+
                               "<td valign='top'>"+rs.getString("tindakan")+"</td>"+
                               "<td valign='top'>"+rs.getString("kd_dokter_bedah")+"</td>"+
                               "<td valign='top'>"+rs.getString("dokterbedah")+"</td>"+
                               "<td valign='top'>"+rs.getString("kd_dokter_anestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("dokteranestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("identitas")+"</td>"+
                               "<td valign='top'>"+rs.getString("alergi")+"</td>"+
                               "<td valign='top'>"+rs.getString("penandaan_area_operasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_aspirasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_aspirasi_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah_line")+"</td>"+
                               "<td valign='top'>"+rs.getString("resiko_kehilangan_darah_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("kesiapan_alat_obat_anestesi")+"</td>"+
                               "<td valign='top'>"+rs.getString("kesiapan_alat_obat_anestesi_rencana_antisipasi")+"</td>"+
                               "<td valign='top'>"+rs.getString("nip_perawat_ok")+"</td>"+
                               "<td valign='top'>"+rs.getString("nama")+"</td>"+
                            "</tr>");
                    }
                    LoadHTML.setText(
                        "<html>"+
                          "<table width='2100px' border='0' align='center' cellpadding='1px' cellspacing='0' class='tbl_form'>"+
                           htmlContent.toString()+
                          "</table>"+
                        "</html>"
                    );

                    File g = new File("file2.css");            
                    BufferedWriter bg = new BufferedWriter(new FileWriter(g));
                    bg.write(
                        ".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi2 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#323232;}"+
                        ".isi3 td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi4 td{font: 11px tahoma;height:12px;border-top: 1px solid #e2e7dd;background: #ffffff;color:#323232;}"+
                        ".isi5 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#AA0000;}"+
                        ".isi6 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#FF0000;}"+
                        ".isi7 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#C8C800;}"+
                        ".isi8 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#00AA00;}"+
                        ".isi9 td{font: 8.5px tahoma;border:none;height:12px;background: #ffffff;color:#969696;}"
                    );
                    bg.close();

                    File f = new File("DataSignInSebelumAnestesi.html");            
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));            
                    bw.write(LoadHTML.getText().replaceAll("<head>","<head>"+
                                "<link href=\"file2.css\" rel=\"stylesheet\" type=\"text/css\" />"+
                                "<table width='2100px' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                    "<tr class='isi2'>"+
                                        "<td valign='top' align='center'>"+
                                            "<font size='4' face='Tahoma'>"+akses.getnamars()+"</font><br>"+
                                            akses.getalamatrs()+", "+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>"+
                                            akses.getkontakrs()+", E-mail : "+akses.getemailrs()+"<br><br>"+
                                            "<font size='2' face='Tahoma'>DATA SIGN-IN SEBELUM ANESTESI<br><br></font>"+        
                                        "</td>"+
                                   "</tr>"+
                                "</table>")
                    );
                    bw.close();                         
                    Desktop.getDesktop().browse(f.toURI());
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
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
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void MnSignInSebelumAnestesiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnSignInSebelumAnestesiActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),22).toString()); 
            finger2=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),10).toString()); 
            Valid.MyReportqry("rptFormulirSIgnInSebelumAnestesi.jasper","report","::[ Formulir Sign-In Sebelum Tindakan Anestesi ]::",
                    "select reg_periksa.no_rawat,pasien.no_rkm_medis,pasien.nm_pasien,pasien.tgl_lahir,pasien.jk,signin_sebelum_anestesi.tanggal,"+
                    "signin_sebelum_anestesi.sncn,signin_sebelum_anestesi.tindakan,signin_sebelum_anestesi.kd_dokter_bedah,dokterbedah.nm_dokter as dokterbedah,"+
                    "signin_sebelum_anestesi.kd_dokter_anestesi,dokteranestesi.nm_dokter as dokteranestesi,signin_sebelum_anestesi.identitas,signin_sebelum_anestesi.penandaan_area_operasi,"+
                    "signin_sebelum_anestesi.alergi,signin_sebelum_anestesi.resiko_aspirasi,signin_sebelum_anestesi.resiko_aspirasi_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.resiko_kehilangan_darah,signin_sebelum_anestesi.resiko_kehilangan_darah_line,signin_sebelum_anestesi.resiko_kehilangan_darah_rencana_antisipasi,"+
                    "signin_sebelum_anestesi.kesiapan_alat_obat_anestesi,signin_sebelum_anestesi.kesiapan_alat_obat_anestesi_rencana_antisipasi,signin_sebelum_anestesi.nip_perawat_ok,"+
                    "petugas.nama from signin_sebelum_anestesi inner join reg_periksa on signin_sebelum_anestesi.no_rawat=reg_periksa.no_rawat "+
                    "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter as dokterbedah on dokterbedah.kd_dokter=signin_sebelum_anestesi.kd_dokter_bedah "+
                    "inner join dokter as dokteranestesi on dokteranestesi.kd_dokter=signin_sebelum_anestesi.kd_dokter_anestesi "+
                    "inner join petugas on petugas.nip=signin_sebelum_anestesi.nip_perawat_ok "+
                    "where signin_sebelum_anestesi.no_rawat='"+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()+"' and signin_sebelum_anestesi.tanggal='"+tbObat.getValueAt(tbObat.getSelectedRow(),5).toString()+"' ",param);
        }
    }//GEN-LAST:event_MnSignInSebelumAnestesiActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void MnObservasiTTVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnObservasiTTVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MnObservasiTTVActionPerformed

    private void StatusHemoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_StatusHemoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_StatusHemoKeyPressed

    private void DiastoleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiastoleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DiastoleKeyPressed

    private void SistoleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SistoleKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SistoleKeyPressed

    private void IntervalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_IntervalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_IntervalKeyPressed

    private void IntervalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IntervalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IntervalActionPerformed

    private void WaktuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WaktuKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_WaktuKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
    }//GEN-LAST:event_TNoRMKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
    }//GEN-LAST:event_TPasienKeyPressed

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{
            //Valid.pindah(evt,TCari,Tanggal);
        }
    }//GEN-LAST:event_TNoRwKeyPressed

    private void RRKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RRKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_RRKeyPressed

    private void OksigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OksigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_OksigenKeyPressed

    private void FIOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FIOKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_FIOKeyPressed

    private void ETCOKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ETCOKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ETCOKeyPressed

    private void SuhuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SuhuKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SuhuKeyPressed

    private void MapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_MapKeyPressed

    private void NadiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NadiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NadiKeyPressed

    private void KejadianKhususKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KejadianKhususKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KejadianKhususKeyPressed

    private void TindakanResponsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakanResponsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TindakanResponsKeyPressed

    
    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMObservasiTTV dialog = new RMObservasiTTV(new javax.swing.JFrame(), true);
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
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.TextBox Diastole;
    private widget.TextBox ETCO;
    private widget.TextBox FIO;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Interval;
    private widget.TextBox KdPetugasOK;
    private widget.TextArea KejadianKhusus;
    private widget.Label LCount;
    private widget.editorpane LoadHTML;
    private widget.TextBox Map;
    private javax.swing.JMenuItem MnObservasiTTV;
    private javax.swing.JMenuItem MnSignInSebelumAnestesi;
    private widget.TextBox Nadi;
    private widget.TextBox NmPetugasOK;
    private widget.TextBox Oksigen;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox RR;
    private widget.ScrollPane Scroll;
    private widget.TextBox Sistole;
    private widget.ComboBox StatusHemo;
    private widget.TextBox Suhu;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextArea TindakanRespons;
    private widget.Tanggal Waktu;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel26;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel40;
    private widget.Label jLabel5;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel66;
    private widget.Label jLabel68;
    private widget.Label jLabel7;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel75;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables
    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            String sqlSelect = 
                "SELECT ttv.id_ttv, ttv.id_intra, reg.no_rawat, pas.nm_pasien, " +
                "ttv.interval_ke, ttv.waktu_obs, ttv.td_sistole, ttv.td_diastole, ttv.map, " +
                "ttv.nadi, ttv.rr, ttv.spo2, ttv.suhu, ttv.etco2, ttv.fio2, " +
                "ttv.status_hemodinamik, ttv.kejadian_khusus, ttv.tindakan_respons, " +
                "pet.nip, pet.nama as petugas ";

            String sqlFrom = 
                "FROM ttv_observasi ttv " +
                "INNER JOIN intra_anestesi ia ON ttv.id_intra = ia.id_intra " +
                "INNER JOIN reg_periksa reg ON ia.no_rawat = reg.no_rawat " +
                "INNER JOIN pasien pas ON reg.no_rkm_medis = pas.no_rkm_medis " +
                "LEFT JOIN petugas pet ON ttv.kd_petugas = pet.nip ";

            if (TCari.getText().trim().equals("")) {
                ps = koneksi.prepareStatement(
                    sqlSelect + sqlFrom +
                    "WHERE ttv.waktu_obs BETWEEN ? AND ? " +
                    "ORDER BY ttv.waktu_obs DESC"
                );
            } else {
                ps = koneksi.prepareStatement(
                    sqlSelect + sqlFrom +
                    "WHERE ttv.waktu_obs BETWEEN ? AND ? AND " +
                    "(reg.no_rawat LIKE ? OR pas.nm_pasien LIKE ? OR pet.nama LIKE ?) " +
                    "ORDER BY ttv.waktu_obs DESC"
                );
            }

            try {
                if (TCari.getText().trim().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new Object[]{
                        rs.getString("id_ttv"),         // 0
                        rs.getString("id_intra"),       // 1
                        rs.getString("no_rawat"),       // 2
                        rs.getString("nm_pasien"),      // 3
                        rs.getString("interval_ke"),    // 4
                        rs.getString("waktu_obs"),      // 5
                        rs.getString("td_sistole"),     // 6
                        rs.getString("td_diastole"),    // 7
                        rs.getString("map"),            // 8
                        rs.getString("nadi"),           // 9
                        rs.getString("rr"),             // 10
                        rs.getString("spo2"),           // 11
                        rs.getString("suhu"),           // 12
                        rs.getString("etco2"),          // 13
                        rs.getString("fio2"),           // 14
                        rs.getString("status_hemodinamik"), // 15
                        rs.getString("kejadian_khusus"),    // 16
                        rs.getString("tindakan_respons"),   // 17
                        rs.getString("nip"),         // 18
                        rs.getString("petugas")         // 19
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif tampil: " + e);
                e.printStackTrace();
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi tampil: " + e);
            e.printStackTrace();
        }
        LCount.setText("" + tabMode.getRowCount());
    }
    
    public void tampil2(String noRawat) {
        Valid.tabelKosong(tabMode);
        try {
            String sqlSelect = 
                "SELECT ttv.id_ttv, ttv.id_intra, reg.no_rawat, pas.nm_pasien, " +
                "ttv.interval_ke, ttv.waktu_obs, ttv.td_sistole, ttv.td_diastole, ttv.map, " +
                "ttv.nadi, ttv.rr, ttv.spo2, ttv.suhu, ttv.etco2, ttv.fio2, " +
                "ttv.status_hemodinamik, ttv.kejadian_khusus, ttv.tindakan_respons, " +
                "pet.nip, pet.nama as petugas ";

            String sqlFrom = 
                "FROM ttv_observasi ttv " +
                "INNER JOIN intra_anestesi ia ON ttv.id_intra = ia.id_intra " +
                "INNER JOIN reg_periksa reg ON ia.no_rawat = reg.no_rawat " +
                "INNER JOIN pasien pas ON reg.no_rkm_medis = pas.no_rkm_medis " +
                "LEFT JOIN petugas pet ON ttv.kd_petugas = pet.nip ";

            if (TCari.getText().trim().equals("")) {
                ps = koneksi.prepareStatement(
                    sqlSelect + sqlFrom +
                    "WHERE ttv.waktu_obs BETWEEN ? AND ? " +
                    "AND ttv.id_intra = ? " +
                    "ORDER BY ttv.waktu_obs DESC"
                );
            } else {
                ps = koneksi.prepareStatement(
                    sqlSelect + sqlFrom +
                    "WHERE ttv.waktu_obs BETWEEN ? AND ? AND " +
                    "(reg.no_rawat LIKE ? OR pas.nm_pasien LIKE ? OR pet.nama LIKE ?) " +
                    "AND ttv.id_intra = ? " +
                    "ORDER BY ttv.waktu_obs DESC"
                );
            }

            try {
                if (TCari.getText().trim().equals("")) {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    ps.setString(3, noRawat);
                } else {
                    ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + "") + " 00:00:00");
                    ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + "") + " 23:59:59");
                    ps.setString(3, "%" + TCari.getText() + "%");
                    ps.setString(4, "%" + TCari.getText() + "%");
                    ps.setString(5, "%" + TCari.getText() + "%");
                    ps.setString(6, noRawat);
                }

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new Object[]{
                        rs.getString("id_ttv"),         // 0
                        rs.getString("id_intra"),       // 1
                        rs.getString("no_rawat"),       // 2
                        rs.getString("nm_pasien"),      // 3
                        rs.getString("interval_ke"),    // 4
                        rs.getString("waktu_obs"),      // 5
                        rs.getString("td_sistole"),     // 6
                        rs.getString("td_diastole"),    // 7
                        rs.getString("map"),            // 8
                        rs.getString("nadi"),           // 9
                        rs.getString("rr"),             // 10
                        rs.getString("spo2"),           // 11
                        rs.getString("suhu"),           // 12
                        rs.getString("etco2"),          // 13
                        rs.getString("fio2"),           // 14
                        rs.getString("status_hemodinamik"), // 15
                        rs.getString("kejadian_khusus"),    // 16
                        rs.getString("tindakan_respons"),   // 17
                        rs.getString("nip"),         // 18
                        rs.getString("petugas")         // 19
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif tampil: " + e);
                e.printStackTrace();
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi tampil: " + e);
            e.printStackTrace();
        }
        LCount.setText("" + tabMode.getRowCount());
    }
   
    // Panggil ini dari RMIntraAnestesi saat buka form TTV
    public void setIdIntra(int idIntra, String noRawat, String namaPasien, String kodePerawat, String namaPerawat, String norm) {
        this.idIntraAktif = idIntra;
        TNoRw.setText(noRawat);
        TPasien.setText(namaPasien);
        TNoRM.setText(norm);
        
        TCari.setText(noRawat);
        
        KdPetugasOK.setText(kodePerawat);
        NmPetugasOK.setText(namaPerawat);

        // Ambil no_rm dan tgl_lahir
        isRawat();
        isPsien();

        // Set waktu sekarang
        Waktu.setDate(new Date());

        // Cari interval terakhir + 1
        int lastInterval = Sequel.cariInteger(
            "SELECT interval_ke FROM ttv_observasi WHERE id_intra=" + idIntra + 
            " ORDER BY interval_ke DESC LIMIT 1"
        );
        Interval.setText(String.valueOf(lastInterval + 1));
    }
    
    public void emptTeks() {
        Waktu.setDate(new Date());
        Interval.setText("");
        Sistole.setText("");
        Diastole.setText("");
        Map.setText("");
        Nadi.setText("");
        RR.setText("");
        Oksigen.setText("");
        Suhu.setText("");
        ETCO.setText("");
        FIO.setText("");
        StatusHemo.setSelectedIndex(0);
        KejadianKhusus.setText("");
        TindakanRespons.setText("");

        Sistole.requestFocus();
    }

    private void getData() {
        if (tbObat.getSelectedRow() == -1) return;

        // 0 = id_ttv (tidak ditampilkan di form)
        // 1 = id_intra (tidak ditampilkan di form)
        // 2 = no_rawat
        TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 2).toString());
        // 3 = nm_pasien
        TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 3).toString());
        // 4 = interval_ke
        Interval.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 4).toString());
        // 5 = waktu_obs
        Valid.SetTgl2(Waktu, tbObat.getValueAt(tbObat.getSelectedRow(), 5).toString());
        // 6 = td_sistole
        Sistole.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 6).toString());
        // 7 = td_diastole
        Diastole.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 7).toString());
        // 8 = map (auto, tidak di-edit manual)
        Map.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 8).toString());
        // 9 = nadi
        Nadi.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 9).toString());
        // 10 = rr
        RR.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 10).toString());
        // 11 = spo2
        Oksigen.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 11).toString());
        // 12 = suhu
        Suhu.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 12).toString());
        // 13 = etco2
        ETCO.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 13).toString());
        // 14 = fio2
        FIO.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 14).toString());
        // 15 = status_hemodinamik
        StatusHemo.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(), 15).toString());
        // 16 = kejadian_khusus
        KejadianKhusus.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 16).toString());
        // 17 = tindakan_respons
        TindakanRespons.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 17).toString());
        // 18 = petugas (kode)
        KdPetugasOK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 18).toString());
        // 19 = petugas (nama)
        NmPetugasOK.setText(tbObat.getValueAt(tbObat.getSelectedRow(), 19).toString());
    }
    
    private void isRawat() {
         Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select pasien.nm_pasien from pasien where pasien.no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
    }
    
    public void setNoRm2(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
    }
    
    public void setNoRm(String norwt, Date tgl2,String KodeDokter,String NamaDokter) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select reg_periksa.tgl_registrasi from reg_periksa where reg_periksa.no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);
        isRawat();
        isPsien();
        ChkInput.setSelected(true);
        isForm();
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            if(internalFrame1.getHeight()>508){
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,336));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }else{
                ChkInput.setVisible(false);
                PanelInput.setPreferredSize(new Dimension(WIDTH,internalFrame1.getHeight()-175));
                FormInput.setVisible(true);      
                ChkInput.setVisible(true);
            }
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnHapus.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnEdit.setEnabled(akses.getsignin_sebelum_anestesi());
        BtnPrint.setEnabled(akses.getsignin_sebelum_anestesi()); 
    }

    private void ganti() {
        if (tbObat.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data TTV yang akan diubah!");
            return;
        }

        String idTtv = tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString();

        // Hitung MAP otomatis
        String map = hitungMAP(Sistole.getText(), Diastole.getText());

        Sequel.mengedit("ttv_observasi",
            "id_ttv=?",
            "waktu_obs=?," +
            "interval_ke=?," +
            "td_sistole=?," +
            "td_diastole=?," +
            "map=?," +
            "nadi=?," +
            "rr=?," +
            "spo2=?," +
            "suhu=?," +
            "etco2=?," +
            "fio2=?," +
            "status_hemodinamik=?," +
            "kejadian_khusus=?," +
            "tindakan_respons=?," +
            "kd_petugas=?",
            16, // 15 SET + 1 WHERE
            new String[]{
                // SET
                Valid.SetTgl(Waktu.getSelectedItem() + "") + " " + Waktu.getSelectedItem().toString().substring(11, 19),
                Interval.getText(),
                Sistole.getText(),
                Diastole.getText(),
                map,
                Nadi.getText(),
                RR.getText(),
                Oksigen.getText(),
                Suhu.getText(),
                ETCO.getText(),
                FIO.getText(),
                StatusHemo.getSelectedItem().toString(),
                KejadianKhusus.getText(),
                TindakanRespons.getText(),
                KdPetugasOK.getText(),
                // WHERE
                idTtv
            }
        );

        if (tabMode.getRowCount() != 0) {
            tampil();
        }
        emptTeks();
    }

    private void hapus() {
        if (tbObat.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data TTV yang akan dihapus!");
            return;
        }

        String idTtv = tbObat.getValueAt(tbObat.getSelectedRow(), 0).toString();

        if (Sequel.queryu2tf("DELETE FROM ttv_observasi WHERE id_ttv=?", 1,
            new String[]{idTtv}) == true) {

            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText("" + tabMode.getRowCount());
            emptTeks();
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menghapus..!!");
        }
    }
}

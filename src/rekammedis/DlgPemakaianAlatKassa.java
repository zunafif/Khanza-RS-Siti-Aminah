/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPerawatan.java
 *
 * Created on May 23, 2010, 6:36:30 PM
 */

/* Modifikasi oleh dr Salim Mulyana
     * Tahun 2024
     * WARNING: use at your own risk
     * silahkan monggo ditambahkan atau diperbaiki fiturnya
*/

package rekammedis;

import kepegawaian.DlgCariDokter;
import kepegawaian.DlgCariPetugas;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgInstrumenOp;


/**
 *
 * @author dosen
 */
public final class DlgPemakaianAlatKassa extends javax.swing.JDialog {
    private DefaultTableModel tbModeInstrumen;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private DlgCariPetugas petugas=new DlgCariPetugas(null,false);
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private PreparedStatement ps,pspemeriksaan,psinstrumen;
    private ResultSet rs;
    private boolean[] pilih; 
    private String[] kode,nama,jmlawal,jmlakhir,keterangan,sisa;
    private int jml=0,i=0,index=0,jmlparsial=0,pilihan=0;
    private String kamar,status="",
            norawatibu="",aktifkanparsial="no";
   

    /** Creates new form DlgPerawatan
     * @param parent
     * @param modal */
  /*
    public DlgPemakaianAlatKassa(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Object[] row={"P","Kode","Alat/ Kassa","Persediaan ","Terpakai","Sisa","Keterangan"};
        tbModeInstrumen=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==0)||(colIndex==3)||(colIndex==4)||(colIndex==5)||(colIndex==6)) {
                    a=true;
                }
                return a;
             }
                
             Class[] types = new Class[] {
                java.lang.Boolean.class,java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbInstrumen.setModel(tbModeInstrumen);        
        
        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbInstrumen.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbInstrumen.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for(i = 0; i < 6; i++) {
            TableColumn column = tbInstrumen.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(50);
            }else if(i==2){
                column.setPreferredWidth(130);
            }else if(i==3){
                column.setPreferredWidth(130);
            }else if(i==4){
                column.setPreferredWidth(130);
            }else if(i==5){
                column.setPreferredWidth(300);
            }
        }
        tbInstrumen.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        Diagnosa.setDocument(new batasInput((byte)20).getKata(Diagnosa));        
        
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCariPeriksa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCariPeriksa.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCariPeriksa.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCariPeriksa.getText().length()>2){
                        tampil();
                    }
                }
            });
        }  
                
    }
    */

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /*
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents
/*
    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        petugas.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnCari,TCariPeriksa);}
}//GEN-LAST:event_BtnKeluarKeyPressed

private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));  
    DlgDaftarPemakaianAlatKassa form=new DlgDaftarPemakaianAlatKassa(null,false);
    form.isCek();
    form.setPasien(TNoRw.getText());
    form.setSize(this.getWidth(),this.getHeight());
    form.setLocationRelativeTo(this);
    form.setVisible(true);
    this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnCariActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void PenjabKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PenjabKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PenjabKeyPressed

    private void TCariPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariPeriksaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            btnCariPeriksaActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            btnCariPeriksa.requestFocus();
        }
    }//GEN-LAST:event_TCariPeriksaKeyPressed

    private void btnCariPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariPeriksaActionPerformed
        tampil();
    }//GEN-LAST:event_btnCariPeriksaActionPerformed

    private void btnCariPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCariPeriksaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
        }else{
            Valid.pindah(evt, TCariPeriksa, BtnAllPeriksa);
        }
    }//GEN-LAST:event_btnCariPeriksaKeyPressed

    private void BtnAllPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllPeriksaActionPerformed
        TCariPeriksa.setText("");
        tampil();
        
    }//GEN-LAST:event_BtnAllPeriksaActionPerformed

    private void BtnAllPeriksaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllPeriksaKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllPeriksaActionPerformed(null);
        }else{
            Valid.pindah(evt, btnCariPeriksa, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllPeriksaKeyPressed

    private void tbInstrumenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbInstrumenMouseClicked
        if(tbModeInstrumen.getRowCount()!=0){
            try {
               // getData2();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbInstrumenMouseClicked
/*
    private void tbInstrumenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbInstrumenKeyPressed
        if(tbInstrumen.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    int row=tbInstrumen.getSelectedColumn();
                    if((row!=0)||(row!=20)){
                        if(tbInstrumen.getSelectedRow()>-1){
                            tbInstrumen.setValueAt(true,tbInstrumen.getSelectedRow(),0);
                        }
                        TCariPeriksa.setText("");
                        TCariPeriksa.requestFocus();
                    }
                    //getData2();
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                   // getData2();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbInstrumenKeyPressed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }
    }//GEN-LAST:event_BtnSimpanKeyPressed
*/
    /*
    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        jml=0;
        for(i=0;i<tbInstrumen.getRowCount();i++){
            if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                jml++;
            }
        }
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(tbModeInstrumen.getRowCount()==0){
            Valid.textKosong(TCariPeriksa,"Data Permintaan");
        }else if(jml==0){
            Valid.textKosong(TCariPeriksa,"Data Permintaan");
        }else if(Diagnosa.getText().equals("")){
            Valid.textKosong(Diagnosa,"Diagnosa");
        }else if(Tindakan.getText().equals("")){
            Valid.textKosong(Tindakan,"Diagnosa");
        }else{
            jmlparsial=0;
            if(aktifkanparsial.equals("yes")){
                jmlparsial=Sequel.cariInteger("select count(set_input_parsial.kd_pj) from set_input_parsial where set_input_parsial.kd_pj=?",Penjab.getText());
            }
            if(jmlparsial>0){    
                simpan(); 
            }else{
                if(Sequel.cariRegistrasi(TNoRw.getText())>0){
                    JOptionPane.showMessageDialog(rootPane,"Data billing sudah terverifikasi, data tidak boleh dihapus.\nSilahkan hubungi bagian kasir/keuangan ..!!");
                    TCariPeriksa.requestFocus();
                }else{
                    simpan();              
                }
            }   
        } 
    }//GEN-LAST:event_BtnSimpanActionPerformed
*/
    /*
    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnBatalActionPerformed(null);
        }
    }//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt,TCariPeriksa,BtnKeluar);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void DiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaKeyPressed
    /*    if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            NmPerujuk.setText(dokter.tampil3(KodePerujuk.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnDokterActionPerformed(null);
        }else{            
            Valid.pindah(evt,TCariPeriksa,Tanggal);
        } */
    }//GEN-LAST:event_DiagnosaKeyPressed
/*
    private void TCariPeriksaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TCariPeriksaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariPeriksaActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        // Valid.pindah(evt,Rencana,Informasi);
    }//GEN-LAST:event_TanggalKeyPressed

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //bangsal.setModal(true);
        DlgAlatKassa instrumen=new DlgAlatKassa(null,false);
        instrumen.emptTeks();
        instrumen.isCek();
        instrumen.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        instrumen.setLocationRelativeTo(internalFrame1);
        instrumen.setAlwaysOnTop(false);
        instrumen.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());

    }//GEN-LAST:event_BtnTambahActionPerformed

    private void TindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TindakanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TindakanActionPerformed

    /**
    * @param args the command line arguments
    */
/*
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgPemakaianAlatKassa dialog = new DlgPemakaianAlatKassa(new javax.swing.JFrame(), true);
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
    // End of variables declaration//GEN-END:variables
    
    
    private void tampil() {         
        try{
            jml=0;
            for(i=0;i<tbInstrumen.getRowCount();i++){
                if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                    jml++;
                }
            }     
            pilih=null;
            pilih=new boolean[jml];
            kode=null;
            kode=new String[jml];
            nama=null;
            nama=new String[jml];
            jmlawal=null;
            jmlawal=new String[jml];
            jmlakhir=null;
            jmlakhir=new String[jml];
            sisa=null;
            sisa=new String[jml];
            keterangan=null;
            keterangan=new String[jml];
           

            index=0; 
            for(i=0;i<tbInstrumen.getRowCount();i++){
                if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                    pilih[index]=true;
                    kode[index]=tbInstrumen.getValueAt(i,1).toString();
                    nama[index]=tbInstrumen.getValueAt(i,2).toString();
                    jmlawal[index]=tbInstrumen.getValueAt(i,3).toString();
                    jmlakhir[index]=tbInstrumen.getValueAt(i,4).toString(); 
                    sisa[index]=tbInstrumen.getValueAt(i,5).toString();
                    keterangan[index]=tbInstrumen.getValueAt(i,6).toString();
                    index++;
                }
            }

            Valid.tabelKosong(tbModeInstrumen);
            for(i=0;i<jml;i++){
                tbModeInstrumen.addRow(new Object[] {pilih[i],kode[i],nama[i],jmlawal[i],jmlakhir[i],sisa[i],keterangan[i]});
            }       
                              //"P","Kode","Alat/ Kassa","Persediaan ","Terpakai","Sisa","Keterangan" 
            psinstrumen=koneksi.prepareStatement("select alatkassa_ok.kd_instrumen, alatkassa_ok.nm_instrumen from alatkassa_ok where "+
                    "alatkassa_ok.kd_instrumen like ? or "+
                    "alatkassa_ok.nm_instrumen like ? "+
                    "order by alatkassa_ok.kd_instrumen ");
            try {
                psinstrumen.setString(1,"%"+TCariPeriksa.getText().trim()+"%");
                psinstrumen.setString(2,"%"+TCariPeriksa.getText().trim()+"%");
                
                rs=psinstrumen.executeQuery();
                while(rs.next()){
                    tbModeInstrumen.addRow(new Object[]{false,rs.getString(1),rs.getString(2),"","","",""});
                } 
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(psinstrumen!=null){
                    psinstrumen.close();
                }
            }           
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }
    
    
    
    public void isReset(){
        jml=tbInstrumen.getRowCount();
        for(i=0;i<jml;i++){ 
            tbInstrumen.setValueAt(false,i,0);
        }
        Valid.tabelKosong(tbModeInstrumen);
        tampil();
    }
    
    public void emptTeks() {
        TCariPeriksa.setText("");
        
        
    }
    
    private void isRawat() {
         Sequel.cariIsi("select reg_periksa.no_rkm_medis from reg_periksa where reg_periksa.no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien(){
        try {
            pspemeriksaan=koneksi.prepareStatement(
                "select reg_periksa.no_rkm_medis,reg_periksa.kd_pj,reg_periksa.kd_dokter,dokter.nm_dokter,pasien.nm_pasien,pasien.jk,pasien.umur,"+
                "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat "+
                "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                "inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter where no_rawat=?");
            try {
                pspemeriksaan.setString(1,TNoRw.getText());
                rs=pspemeriksaan.executeQuery();
                while(rs.next()){
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    Penjab.setText(rs.getString("kd_pj"));
                    Diagnosa.setText(rs.getString("kd_dokter"));
                //    NmPerujuk.setText(rs.getString("nm_dokter"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    Umur.setText(rs.getString("umur"));
                    Alamat.setText(rs.getString("alamat"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(pspemeriksaan!=null){
                    pspemeriksaan.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    

    
    
    public void setNoRm(String norwt, String namapasien, String KodeDokter,String NamaDokter) {
        TNoRw.setText(norwt);
        //TNoRM.setText(norm);
        isRawat();
        TPasien.setText(namapasien);
        Diagnosa.setText(KodeDokter);
    //  NmPerujuk.setText(NamaDokter);
        
    }
    
    
    public void isCek(){        
        BtnSimpan.setEnabled(akses.getpermintaan_radiologi());
        //BtnPrint.setEnabled(akses.getpermintaan_radiologi());
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            FormInput.setPreferredSize(new Dimension(WIDTH,189));
            PanelInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            FormInput.setPreferredSize(new Dimension(WIDTH,20));
            PanelInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void setDokterPerujuk(String kodeperujuk,String namaperujuk){
        Diagnosa.setText(kodeperujuk);
      //  NmPerujuk.setText(namaperujuk);
    }

    
/*
    private void simpan() {
    int reply = JOptionPane.showConfirmDialog(rootPane,"Eeiiiiiits, udah bener belum data yang mau disimpan..??","Konfirmasi",JOptionPane.YES_NO_OPTION);
        if (reply ==NmPerujuk.setText(namaperujuk); JOptionPane.YES_OPTION) {
            //ChkJln.setSelected(false);
            try {                    
                koneksi.setAutoCommit(false);
                //autoNomor();
                if(Sequel.menyimpantf2("petugas_beri_alatkassa_ok","?,?,?,?,?,?","No.Rawat",6,new String[]{
                        TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),
                        KodePerujuk.getText(),KdPetugasRuangan1.getText(),KdPetugasRuangan.getText(),KdPetugasRuangan2.getText()
                    })==true){
                    for(i=0;i<tbInstrumen.getRowCount();i++){ 
                        if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("beri_alatkassa_ok","?,?,?,?,?,?","pemakaian instrumen",6,new String[]{
                                TNoRw.getText(),tbInstrumen.getValueAt(i,3).toString(),tbInstrumen.getValueAt(i,1).toString(),tbInstrumen.getValueAt(i,2).toString(),
                                tbInstrumen.getValueAt(i,5).toString(),tbInstrumen.getValueAt(i,6).toString()
                            });
                            
                        }                        
                    } 
                    isReset();
                    emptTeks();
                }else{
                    //autoNomor();
                    if(Sequel.menyimpantf2("petugas_beri_alatkassa_ok","?,?,?,?,?,?","No.Rawat",6,new String[]{
                        TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),
                        KodePerujuk.getText(),KdPetugasRuangan1.getText(),KdPetugasRuangan.getText(),KdPetugasRuangan2.getText()
                    })==true){
                    for(i=0;i<tbInstrumen.getRowCount();i++){ 
                        if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("beri_alatkassa_ok","?,?,?,?,?,?","pemakaian instrumen",6,new String[]{
                                TNoRw.getText(),tbInstrumen.getValueAt(i,3).toString(),tbInstrumen.getValueAt(i,1).toString(),tbInstrumen.getValueAt(i,2).toString(),
                                tbInstrumen.getValueAt(i,5).toString(),tbInstrumen.getValueAt(i,6).toString()
                            });
                            
                        }                        
                    } 
                        isReset();
                        emptTeks();
                    }else{
                        //autoNomor();
                        if(Sequel.menyimpantf2("petugas_beri_alatkassa_ok","?,?,?,?,?,?","No.Rawat",6,new String[]{
                        TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),
                        KodePerujuk.getText(),KdPetugasRuangan1.getText(),KdPetugasRuangan.getText(),KdPetugasRuangan2.getText()
                    })==true){
                    for(i=0;i<tbInstrumen.getRowCount();i++){ 
                        if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("beri_alatkassa_ok","?,?,?,?,?,?","pemakaian instrumen",6,new String[]{
                                TNoRw.getText(),tbInstrumen.getValueAt(i,3).toString(),tbInstrumen.getValueAt(i,1).toString(),tbInstrumen.getValueAt(i,2).toString(),
                                tbInstrumen.getValueAt(i,5).toString(),tbInstrumen.getValueAt(i,6).toString()
                            });
                            
                        }                        
                    } 
                            isReset();
                            emptTeks();
                        }else{
                            //autoNomor();
                            if(Sequel.menyimpantf2("petugas_beri_alatkassa_ok","?,?,?,?,?,?","No.Rawat",6,new String[]{
                        TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+"")+" "+Tanggal.getSelectedItem().toString().substring(11,19),
                        KodePerujuk.getText(),KdPetugasRuangan1.getText(),KdPetugasRuangan.getText(),KdPetugasRuangan2.getText()
                    })==true){
                    for(i=0;i<tbInstrumen.getRowCount();i++){ 
                        if(tbInstrumen.getValueAt(i,0).toString().equals("true")){
                            Sequel.menyimpan2("beri_alatkassa_ok","?,?,?,?,?,?","pemakaian instrumen",6,new String[]{
                                TNoRw.getText(),tbInstrumen.getValueAt(i,3).toString(),tbInstrumen.getValueAt(i,1).toString(),tbInstrumen.getValueAt(i,2).toString(),
                                tbInstrumen.getValueAt(i,5).toString(),tbInstrumen.getValueAt(i,6).toString()
                            });
                            
                        }                        
                    }
                                isReset();
                                emptTeks();
                            } 
                        } 
                    } 
                }   

                koneksi.setAutoCommit(true);                    
                JOptionPane.showMessageDialog(null,"Proses simpan selesai...!");
            } catch (Exception e) {
                System.out.println(e);
            }    
            //ChkJln.setSelected(true);            
        } 
    } */
/*
}
*/


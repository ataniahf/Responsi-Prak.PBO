package kereta_api;
/**
 *
 * @author ATANIA
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;


public class Kereta extends JFrame{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kereta_api";
    static final String USER = "root";
    static final String PASS = "";
    
    Connection koneksi;
    Statement statement;
    //layout
    JLabel judul = new JLabel("DATA NAMA KERETA API"); //membuat 
    JLabel lID= new JLabel("ID KERETA : ");
    JTextField tfID = new JTextField();
    JLabel lNama = new JLabel("Nama Kereta : ");
    JTextField tfNama = new JTextField();
    JLabel lKelas = new JLabel("Kelas : ");
    JTextField tfKelas = new JTextField();
    
    
    JButton btnCreatePanel = new JButton("Tambah");
    JButton btnEditPanel = new JButton("Edit");
    JButton btnDeletePanel = new JButton("Hapus");
    JButton btnExitPanel = new JButton("Batal");
    JButton btnRefreshPanel = new JButton("Refesh");
    JTable tabel;
    DefaultTableModel tabelModel;
    JScrollPane scrollPane;
    Object namaKolom[] = {"ID Kereta","Nama Kereta","Kelas"};
   
   public Kereta(){//method karyawan
       //exception (menangani error agar program tetap berjalan)
        try{//kalo bener
            Class.forName(JDBC_DRIVER);
            koneksi = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println("Koneksi Berhasil");
        }catch(ClassNotFoundException | SQLException ex) {//kalo error
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("Koneksi Gagal");
        }  
    
        tabelModel = new DefaultTableModel (namaKolom,0);
        tabel = new JTable(tabelModel);
        scrollPane = new JScrollPane(tabel);
       
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(850,580);
        setLocation(225,75);
        
        add(judul);
        add(lID);
        add(tfID);
        add(lNama);
        add(tfNama);
        add(lKelas);
        add(tfKelas);
        judul.setBounds(350, 10, 200, 20);
        judul.setFont(new Font("",Font.CENTER_BASELINE, 15));
        lID.setBounds(10,300,90,20);       
        tfID.setBounds(120,300,120,20);        
        lNama.setBounds(10,320,90,20);        
        tfNama.setBounds(120,320,120,20);        
        lKelas.setBounds(10,340,90,20);        
        tfKelas.setBounds(120,340,120,20);


        
        add(btnEditPanel);
        btnEditPanel.setBounds(400,300,100,50);
        add(btnCreatePanel);
        btnCreatePanel.setBounds(400,370,100,50);
        add(btnDeletePanel);
        btnDeletePanel.setBounds(600,300,100,50);
        add(btnExitPanel);
        btnExitPanel.setBounds(600,370,100,50);
        add(btnRefreshPanel);
        btnRefreshPanel.setBounds(500,440,100,50);
        
        add(scrollPane);
        scrollPane.setBounds(110,50,600,200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
   
if (this.getBanyakData() != 0) {
            String dataKereta[][] = this.readKereta();//this =kelas sendiri
            tabel.setModel((new JTable(dataKereta, namaKolom)).getModel());
        } else {
            JOptionPane.showMessageDialog(null, "Data Tidak Ada");
        }
btnCreatePanel.addActionListener((ActionEvent e) -> {
            if (tfID.getText().equals("") ) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                String id = tfID.getText();
                String nama = tfNama.getText();
                String kelas = tfKelas.getText();
                
   
                this.insertKereta(id, nama, kelas);
                String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
            }
        });


tabel.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e){ 
               int baris = tabel.getSelectedRow();
               int kolom = tabel.getSelectedColumn(); 
               String dataterpilih = tabel.getValueAt(baris, 0).toString();
               try {
        String query = "SELECT * FROM `kereta` WHERE `id_kereta` = '" + dataterpilih + "'";
        statement = koneksi.createStatement();
        ResultSet resultSet = statement.executeQuery(query); 
        while (resultSet.next()) { 
            tfID.setText(resultSet.getString("id_kereta"));
            tfNama.setText(resultSet.getString("nama_kereta"));
            tfKelas.setText(resultSet.getString("kelas_kereta"));
                        
        }
        
    } catch (SQLException sql) {
        System.out.println(sql.getMessage());
    }  
      

btnDeletePanel.addActionListener((ActionEvent f) -> {
                  deleteKereta(dataterpilih);
                  String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
                }); 
    
               
           }
        });
btnEditPanel.addActionListener((ActionEvent f) -> {
            if (tfID.getText().equals("") ) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                String ID = tfID.getText();//encaptulation
                String Nama = tfNama.getText();
                String Kelas = tfKelas.getText();
                this.updateKereta(ID, Nama, Kelas);
                String dataKereta[][] = readKereta();
                tabel.setModel(new JTable(dataKereta,namaKolom).getModel());
            }
        }); 
   

btnExitPanel.addActionListener((ActionEvent e) -> {
          Menu g = new Menu();
          dispose();
        });
btnRefreshPanel.addActionListener((ActionEvent e) -> {
          tfID.setText("");
          tfNama.setText("");
          tfKelas.setText("");
        });

}

int getBanyakData() {
        int jmlData = 0;
        
        try{
            statement = koneksi.createStatement();
            String query = "SELECT * from `kereta`";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                jmlData++;
            }
            return jmlData;
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return 0;
        }
    }

String[][] readKereta() {
        try{
            int jmlData = 0;
            String data[][]=new String[getBanyakData()][6];
            String query = "Select * from `kereta`";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                data[jmlData][0] = resultSet.getString("id_kereta");
                data[jmlData][1] = resultSet.getString("nama_kereta");
                data[jmlData][2] = resultSet.getString("kelas_kereta");

                
                jmlData++;
            }
            return data;
        }catch(SQLException e){//kalo salah
            System.out.println(e.getMessage());
            System.out.println("SQL error");
            return null;
        }
    }

public void insertKereta(String id, String nama, String kelas) {
        try{
        String query = "INSERT INTO `kereta`(`id_kereta`,`nama_kereta`,`kelas_kereta`) VALUES ('"+id+"','"+nama+"','"+kelas+"')";
        statement = (Statement) koneksi.createStatement();
        statement.executeUpdate(query);
        JOptionPane.showMessageDialog(null,"Data berhasil ditambahkan");
        }catch(Exception sql){
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }
        
    }

void deleteKereta(String ID) {
    
        try{
            String query = "DELETE FROM `kereta` WHERE `id_kereta` = '"+ID+"'";
            statement = koneksi.createStatement();
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(null, "berhasil dihapus" );
        }catch(SQLException sql){
            System.out.println(sql.getMessage());
        }
    }
public void updateKereta(String ID, String NamaK, String Kelas) {
    
        try{
            String query = "UPDATE kereta SET nama_kereta = '"+NamaK+"', kelas_kereta = '"+Kelas+"' where `id_kereta` = '"+ID+"'";        statement = (Statement) koneksi.createStatement();
        statement.executeUpdate(query);
        JOptionPane.showMessageDialog(null,"Data berhasil diedit");
        }catch(Exception sql){
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }   
    }

}


package kereta_api;
/**
 *
 * @author ATANIA
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class Tiket extends JFrame {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/kereta_api";
    static final String USER = "root";
    static final String PASS = "";
    
    Connection koneksi;
    Statement statement;
    
    private String Jk[] ={"-pilih-","Perempuan","Laki-laki"};
    private String Stasiun[] ={"-pilih-","Tugu Jogja","Lempuyangan","Maguwoharjo"};
   
    JLabel judul = new JLabel("DATA PEMBELIAN TIKET KERETA API");
    JLabel lNama = new JLabel("Nama :");
    JTextField tfNama = new JTextField();
    JLabel lJk = new JLabel("Jenis Kelamin :");    
    JLabel lStasiun = new JLabel("Stasiun Tujuan :");    
    JLabel lKereta = new JLabel("Kereta :");
    JComboBox cmbJk = new JComboBox(Jk);
    JComboBox cmbStasiun = new JComboBox(Stasiun);

    JButton btnCreatePanel = new JButton("Tambah");
    JButton btnExitPanel = new JButton("Batal");
  
    JTable tabel;
    DefaultTableModel tabelModel;
    JScrollPane scrollPane;
    Object namaKolom[] = {"Nama", "Jenis Kelamin", "Stasiun Tujuan", "Kereta"};

public Tiket() {
    try {
        Class.forName(JDBC_DRIVER);
        koneksi = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Koneksi Berhasil");
    } catch (ClassNotFoundException | SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
        System.out.println("Koneksi Gagal");
    }
        tabelModel = new DefaultTableModel(namaKolom, 0);
        tabel = new JTable(tabelModel);
        scrollPane = new JScrollPane(tabel);
            
        //VIEW
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(850, 580);
        setLocation(225, 75);

        add(judul);
        add(lNama);
        add(tfNama);
        add(lJk);
        add(cmbJk);
        add(lStasiun);
        add(cmbStasiun);
        judul.setBounds(350, 10, 200, 20);
        judul.setFont(new Font("", Font.CENTER_BASELINE, 15));
        lNama.setBounds(10, 300, 90, 20);        
        tfNama.setBounds(120, 300, 120, 20);        
        lJk.setBounds(10, 320, 90, 20);        
        cmbJk.setBounds(120, 320, 120, 20);        
        lStasiun.setBounds(10, 340, 90, 20);       
        cmbStasiun.setBounds(120, 340, 120, 20);
        //UNTUK COMBO BOX DATA NAMA KERETA
        String data[] = new String[dataKereta()];
        data = Kereta();
        JComboBox cmbKereta = new JComboBox(data);

        add(lKereta);
        add(cmbKereta);
        lKereta.setBounds(10, 360, 90, 20);        
        cmbKereta.setBounds(120, 360, 120, 20);
        //

        add(btnCreatePanel);
        add(btnExitPanel);
        
        btnCreatePanel.setBounds(400, 300, 100, 50);
        btnExitPanel.setBounds(400, 370, 100, 50);

        add(scrollPane);
        scrollPane.setBounds(110, 50, 600, 200);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    if (this.getBanyakData() != 0) {
        String dataTiket[][] = this.readTiket();
        tabel.setModel((new JTable(dataTiket, namaKolom)).getModel());
        } else {
        JOptionPane.showMessageDialog(null, "Data Tidak Ada");
        }
    btnCreatePanel.addActionListener((ActionEvent e) -> {
        if (tfNama.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
        } else {
            String Nama = tfNama.getText();//encaptulation
            String Jk = cmbJk.getSelectedItem().toString();
            String Stasiun = cmbStasiun.getSelectedItem().toString();
            String Kereta = cmbKereta.getSelectedItem().toString();

            this.insertTiket(Nama, Jk, Stasiun, Kereta);
            String dataTiket[][] = readTiket();
            tabel.setModel(new JTable(dataTiket, namaKolom).getModel());
        }
    });
    

    btnExitPanel.addActionListener((ActionEvent e) -> {
        Menu g = new Menu();
        dispose();
    });

}
//mengambil banyak data tiket pada tabel tiket dari database
int getBanyakData() {
    int jmlData = 0;
    try {
        statement = koneksi.createStatement();
        String query = "SELECT * from `tiket";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            jmlData++;
        }
        return jmlData;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println("SQL error");
        return 0;
    }
}

//menampilkan data tiket pada tabel tiket dari database
String[][] readTiket() {
    try {
        int jmlData         = 0;
        String data[][]     = new String[getBanyakData()][8];
        String query        = "Select * from `tiket`";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            data[jmlData][0] = resultSet.getString("nama");
            data[jmlData][1] = resultSet.getString("jenis");
            data[jmlData][2] = resultSet.getString("stasiun");
            data[jmlData][3] = resultSet.getString("kereta");

            jmlData++;
        }
        return data;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println("SQL error");
        return null;
    }
}
//memasukkan data input form tiket ke dalam database tabel tiket
public void insertTiket(String Nama, String Jk, String Stasiun, String Kereta) {

        try {

            String query = "SET FOREIGN_KEY_CHECKS=0;";
            statement.execute(query);
            query = "INSERT INTO `tiket`(`nama`,`jenis`,`stasiun`,`kereta`) VALUES ('" + Nama + "','" + Jk + "','" + Stasiun + "','" + Kereta + "')";
            statement.executeUpdate(query);
            query = "SET FOREIGN_KEY_CHECKS=1;";
            statement.execute(query);

            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (Exception sql) {
            System.out.println(sql.getMessage());
            JOptionPane.showMessageDialog(null, sql.getMessage());
        }
    }
//memanggil data kereta untuk combo box
int dataKereta() {
    int jmlData = 0;
    try {
        statement = koneksi.createStatement();
        String query = "SELECT * from `kereta";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            jmlData++;
        }
        return jmlData;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println("SQL error");
        return 0;
    }
}
//menampilkan nama kereta pada tabel kereta ke dalam combo box
String[] Kereta() {
    try {
        int jmlData = 0;
        String data[] = new String[dataKereta()];
        String query = "Select * from `kereta`";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
        data[jmlData] = resultSet.getString("nama_kereta");
        jmlData++;
        }
        return data;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
        System.out.println("SQL error");
        return null;
    }
}
}

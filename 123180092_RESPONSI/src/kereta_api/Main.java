package kereta_api;
/**
 *
 * @author ATANIA
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

class Main {
    public static void main(String[] args) {//method menu 
        Menu g = new Menu();//membuat object menu
    } 
}
//LAYOUT
class Menu extends JFrame {//inherritance
    JLabel menu = new JLabel("MENU UTAMA KERETA API");
    JButton tomboltiket = new JButton("DAFTAR TIKET");
    JButton tombolkereta = new JButton("DAFTAR KERETA");
    
    public Menu() {//method
    
        setTitle("MENU");//mengatur judul output
        setDefaultCloseOperation(3);//mengatur operasi default dalam container
        setSize(350,250);//mengatur ukuran output
        setLocation(500,275);//mengatur lokasi output
        setLayout(null);//mengatur tampilan
        
        add(menu);
        add(tomboltiket);
        add(tombolkereta);
        menu.setBounds(70,10,200,20);
        menu.setFont(new Font("",Font.CENTER_BASELINE, 15));
        
        tomboltiket.setBounds(70,70,200,40);
        tombolkereta.setBounds(70,120,200,40);

        
        tomboltiket.addActionListener((ActionEvent e) -> {
            Tiket a = new Tiket();
            dispose();
        });
        tombolkereta.addActionListener((ActionEvent e) -> {
            Kereta b = new Kereta();
            dispose();
        });
        
        
        setVisible(true);//berfungsi untuk mengatur output agar d tampilkan
    }
    
}
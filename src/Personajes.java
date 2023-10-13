import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Personajes extends JFrame{
    private JPanel panel;
    private JTextField idText;
    private JTextField nicknameText;
    private JTextField fuerzaText;
    private JButton consultarBtn;
    private JButton ingresarBtn;
    private JTextField destrezaText;
    private JTextField inteligenciaText;
    private JList lista;
    private JComboBox comboRol;
    private JButton modificarBtn;
    private JButton eliminarBtn;
    Connection con;
    PreparedStatement ps;
    Statement st;
    ResultSet result;
    DefaultListModel mod = new DefaultListModel();



public Personajes(){
    consultarBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                listar();
            }catch (SQLException ex){
                lista.setModel(mod);
                mod.removeAllElements();
                mod.addElement("¡Error al listar");
                throw new RuntimeException(ex);
            }
        }

    });
    ingresarBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        try{
            insertar();
        }catch (SQLException ex){
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("¡Error al registrar!");
            throw new RuntimeException(ex);
        }
        }
    });

    eliminarBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                eliminar();
            }catch (SQLException ex){
                lista.setModel(mod);
                mod.removeAllElements();
                mod.addElement("¡Error al eliminar!");
                throw new RuntimeException(ex);
            }
        }
    });

    modificarBtn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                modificar();
            }catch (SQLException ex){
                lista.setModel(mod);
                mod.removeAllElements();
                mod.addElement("¡Error al modificar!");

                throw new RuntimeException(ex);
            }
        }
    });
}


 public void listar() throws SQLException{
    conectar();
    lista.setModel(mod);
    st = con.createStatement();
    result  =st.executeQuery("Select id, nickname, nombreRol, fuerza,destreza, inteligencia from personaje");
    mod.removeAllElements();
    while (result.next()){
        mod.addElement(result.getString(1)+ "   " + result.getString(2)+"   "+ result.getString(3)+"   Frz "+ result.getInt(4)+"   Int "+ result.getInt(5)+"   Dex "+ result.getInt(6));
    }

 }
 public void insertar() throws SQLException {
    conectar();

    ps = con.prepareStatement("INSERT INTO personaje VALUES (?,?,?,?,?,?)");
    String combo;
    combo = comboRol.getSelectedItem().toString();
    ps.setInt(1,Integer.parseInt(idText.getText()));
    ps.setString(2, nicknameText.getText());
    ps.setString(3, combo);
    ps.setInt(4,Integer.parseInt(fuerzaText.getText()));
    ps.setInt(5,Integer.parseInt(destrezaText.getText()));
    ps.setInt(6, Integer.parseInt(inteligenciaText.getText()));
    if(ps.executeUpdate()>0){
        lista.setModel(mod);
        mod.removeAllElements();
        mod.addElement("¡Ingreso exitoso!");

        idText.setText("");
        nicknameText.setText("");
        comboRol.setSelectedIndex(0);
        fuerzaText.setText("");
        destrezaText.setText("");
        inteligenciaText.setText("");
    }
 }

    public void modificar() throws SQLException {
        conectar();
        ps = con.prepareStatement("UPDATE personaje SET nickname=?, nombreRol=?, fuerza=?, inteligencia=?, destreza=? WHERE id=?");

        ps.setString(1, nicknameText.getText());
        ps.setString(2, comboRol.getSelectedItem().toString());
        ps.setInt(3,Integer.parseInt(fuerzaText.getText()));
        ps.setInt(4,Integer.parseInt(destrezaText.getText()));
        ps.setInt(5, Integer.parseInt(inteligenciaText.getText()));
        ps.setInt(6,Integer.parseInt(idText.getText()));
        if(ps.executeUpdate()>0){
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("¡Modificación exitosa!");

            idText.setText("");
            nicknameText.setText("");
            comboRol.setSelectedIndex(0);
            fuerzaText.setText("");
            destrezaText.setText("");
            inteligenciaText.setText("");
        }
    }

    public void eliminar() throws SQLException {
        conectar();
        ps = con.prepareStatement("DELETE FROM personaje WHERE id=?");
        ps.setInt(1,Integer.parseInt(idText.getText()));
        if(ps.executeUpdate()>0){
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("¡Eliminado exitosamente!");

            idText.setText("");
        }
    }

public static void main (String[] args){
    Personajes g = new Personajes();
    g.setContentPane(new Personajes().panel);
    g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    g.setVisible(true);
    g.pack();


}
    public void conectar(){
        try {
            con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/juegorol","root","");
            System.out.println("Conectado");
//            cargarCombo(comboRol);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

//    private void cargarCombo(JComboBox c) {
//
//        DefaultComboBoxModel combo = new DefaultComboBoxModel();
//        c.setModel(combo);
//        Listado_Roles lr = new Listado_Roles();
//        try {
//            Statement st = con.createStatement();
//            ResultSet rs = st.executeQuery("select nombreRol from Rol");
//            while (rs.next()){
//                Rol ro = new Rol();
//                ro.setNombreRol(rs.getString(1));
//
//                lr.AgregarRol(ro);
//                combo.addElement(ro.getNombreRol());
//                    System.out.println("EXITO");
//            }
//        } catch (SQLException e) {
//            System.out.println("ERROR"+e);
//        }
//
//    }
}

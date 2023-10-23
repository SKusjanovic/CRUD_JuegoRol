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
    private JLabel label;
    private JComboBox comboRol;
    private JButton modificarBtn;
    private JButton eliminarBtn;
    private JLabel icono;
    Connection con;
    PreparedStatement ps;
    Statement st;
    ResultSet result;
    DefaultListModel mod = new DefaultListModel();



    public Personajes(){

        //Botón consultar
        consultarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    listar();
                }catch (SQLException ex){
                    lista.setModel(mod);
                    mod.removeAllElements();
                    mod.addElement("¡Error al listar!");
                    throw new RuntimeException(ex);
                }
            }

        });

        //Botón ingresar
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

        //Botón eliminar
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

        //Botón modificar
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



    //Listar personajes
     public void listar() throws SQLException{
        conectar();
        lista.setModel(mod);
        st = con.createStatement();
        result  =st.executeQuery("Select id, nickname, nombreRol, fuerza,destreza, inteligencia from personaje");
        mod.removeAllElements();
        while (result.next()){
            mod.addElement(result.getString(1)+ " | " + result.getString(2)+" | "+ result.getString(3)+" | Frz "+ result.getInt(4)+" | Int "+ result.getInt(5)+" | Des "+ result.getInt(6));
        }

     }

     //Registrar personaje
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

    //Modificar personaje
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

    //Eliminar personaje
    public void eliminar() throws SQLException {
        conectar();
        ps = con.prepareStatement("DELETE FROM personaje WHERE id=?");
        ps.setInt(1,Integer.parseInt(idText.getText()));
        if(ps.executeUpdate()>0){
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("¡Eliminado exitosamente!");

            idText.setText("");
        }else {
            lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("¡Error al eliminar!");

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

    //Conexión con BBDD
    public void conectar(){
        try {
            con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/juegorol","root","");
            System.out.println("Conectado");

        } catch (SQLException e) {
            System.out.println("No conectado");
            throw new RuntimeException(e);
        }

    }

}

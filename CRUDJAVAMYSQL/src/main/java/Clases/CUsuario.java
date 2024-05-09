
package Clases;

import com.toedter.calendar.JDateChooser;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class CUsuario {

    
    int idSexo;
    
    public void establecerIdSexo(int idSexo) {
        this.idSexo = idSexo;
    }
    
    public void MostrarSexoCombo(JComboBox comboSexo){
        
        Clases.CConexion objetoConexion = new Clases.CConexion();
        
        String sql = "select * from sexo";
        
        Statement st;
        
        try {
            st = objetoConexion.estableceConexion().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            comboSexo.removeAllItems();
            
            while (rs.next()) {
                String nombreSexo = rs.getString("sexo");
                this.establecerIdSexo(rs.getInt("id"));
                
                
                comboSexo.addItem(nombreSexo);
                comboSexo.putClientProperty(nombreSexo, idSexo);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al mostrar sexo"+e.toString());
        
        }
        finally{
            objetoConexion.cerrarConexion();
        }
        
    }
    
    
    public void AgregarUsuario(JTextField nombres, JTextField apellidos, JComboBox combosexo, JTextField edad, JDateChooser fnacimiento, File foto){
        
        CConexion objetoConexion = new CConexion();
        
        String consulta = "insert into usuarios (nombres, apellidos, fksexo, edad, fnacimiento,foto) values (?, ?, ?, ?, ?, ?);";
        
        
        try {
            FileInputStream fis = new FileInputStream(foto);
            
            CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
            cs.setString(1, nombres.getText());
            cs.setString(2, apellidos.getText());

            int idSexo = (int) combosexo.getClientProperty(combosexo.getSelectedItem());
            
            cs.setInt(3, idSexo);
            cs.setInt(4, Integer.parseInt(edad.getText())); //Asi se transforma un string en un int
            
            Date fechaSeleccionada = fnacimiento.getDate();
            
            java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());
            
            cs.setDate(5, fechaSQL);
            
            cs.setBinaryStream(6, fis,(int)foto.length());
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se guardó el usuario correctamente");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "error al guardar, error:"+e.toString());
        }
        
        
    }
}

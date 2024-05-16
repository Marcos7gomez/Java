
package Clases;

import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


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
        public void mostrarUsuarios (JTable tablaTotalUsuarios){
            
            Clases.CConexion objetoConexion = new Clases.CConexion();
            
            DefaultTableModel modelo = new DefaultTableModel();
            
            String sql = "";
            
            modelo.addColumn("id");
            modelo.addColumn("Nombres");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Sexo");
            modelo.addColumn("Edad");
            modelo.addColumn("Fnacimiento");
            modelo.addColumn("Foto");
            
            
            tablaTotalUsuarios.setModel(modelo);
            
            sql= "select usuarios.id, usuarios.nombres, usuarios.apellidos, sexo.sexo, usuarios.edad, usuarios.fnacimiento, usuarios.foto from usuarios INNER JOIN sexo ON usuarios.fksexo = sexo.id;";
            
            try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);
            
                while (rs.next()) {
                String id = rs.getString("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String sexo = rs.getString("sexo");
                String edad = rs.getString("edad");
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Para acomodar la fecha a como gusta verla
                java.sql.Date fechaSQL = rs.getDate("fnacimiento");
                String nuevaFecha = sdf.format(fechaSQL);
                
                byte [] imageBytes = rs.getBytes("foto");
                Image foto = null;
                
                    if(imageBytes !=null){
                        try {
                            ImageIcon imageIcon = new ImageIcon(imageBytes);
                            foto = imageIcon.getImage();
                        } catch (Exception e) {
                            
                            JOptionPane.showMessageDialog(null, "Error: "+e.toString());
                        }
                        modelo.addRow(new Object[]{id, nombres, apellidos, sexo, edad, nuevaFecha, foto});
                    }
                    tablaTotalUsuarios.setModel(modelo);
                }
            } catch (Exception e) {
                
                JOptionPane.showMessageDialog(null, "Error al mostrar usuarios, error: "+e.toString());
            }
                finally{
                    objetoConexion.cerrarConexion();
                }
        }
        
        
        public void Seleccionar (JTable totalUsuarios, JTextField id, JTextField nombres, JTextField apellidos, JComboBox sexo, JTextField edad, JDateChooser fnacimiento, JLabel foto){
            
            int fila = totalUsuarios.getSelectedRow();
            
            if (fila >=0) {
                
                id.setText(totalUsuarios.getValueAt(fila, 0).toString());
                nombres.setText(totalUsuarios.getValueAt(fila, 1).toString());
                apellidos.setText(totalUsuarios.getValueAt(fila, 2).toString());
                
                sexo.setSelectedItem(totalUsuarios.getValueAt(fila, 3).toString());
                
                edad.setText(totalUsuarios.getValueAt(fila, 4).toString());
                
                String fechaString = totalUsuarios.getValueAt(fila, 5).toString();
                
                Image imagen = (Image) totalUsuarios.getValueAt(fila, 6);
                
                ImageIcon originalIcon = new ImageIcon(imagen);
                int lblanchura = foto.getWidth();
                int lblaltura = foto.getHeight();
                
                Image ImagenEscalada = originalIcon.getImage().getScaledInstance(lblanchura, lblaltura, Image.SCALE_SMOOTH);
                foto.setIcon(new ImageIcon(ImagenEscalada));
                
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechadate = sdf.parse(fechaString);
                    
                    fnacimiento.setDate(fechadate);
                } catch (Exception e) {
                    
                    JOptionPane.showMessageDialog(null, "Error al seleccionar, error:"+e.toString());
                    
                }

            }
        }
            public void ModificarUsuario(JTextField id,JTextField nombres, JTextField apellidos, JComboBox combosexo, JTextField edad, JDateChooser fnacimiento, File foto){
                
                CConexion objetoConexion = new CConexion();
                
                String consulta = "UPDATE usuarios SET usuarios.nombres=?, usuarios.apellidos=?, usuarios.fksexo=?, usuarios.edad=?, usuarios.fnacimiento=?, usuarios.foto=? Where usuarios.id=?";
                
                try {
                    
                    FileInputStream fis = new FileInputStream(foto);
                    CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
                    
                    cs.setString(1, nombres.getText());
                    cs.setString(2, apellidos.getText());
                    
                    int idSexo = (int) combosexo.getClientProperty(combosexo.getSelectedItem());
                    
                    cs.setInt(3, idSexo);
                    
                    cs.setInt(4, Integer.parseInt(edad.getText()));
                    
                    Date fechaSeleccionada = fnacimiento.getDate();
                    
                    java.sql.Date fechaSQL = new java.sql.Date (fechaSeleccionada.getTime());
                    
                    cs.setDate(5, fechaSQL);
                    
                    cs.setBinaryStream(6, fis, (int)foto.length());
                    
                    cs.setInt(7, Integer.parseInt(id.getText()));
                    
                    cs.execute();
                    
                    JOptionPane.showMessageDialog(null, "Se modificó correctamente");
                    
                } catch (Exception e) {
                    
                    JOptionPane.showMessageDialog(null, "No se modificó correctamente, error:"+e.toString());
                }
                
                finally{
                    objetoConexion.cerrarConexion();
                }
                
            }
            
            public void EliminarUsuario(JTextField id){
                
                CConexion objetoConexion = new CConexion();
                
                
                String consulta = "DELETE FROM usuarios WHERE usuarios.id=?";
                
                try {
                    CallableStatement cs = objetoConexion.estableceConexion().prepareCall(consulta);
                    
                    cs.setInt(1, Integer.parseInt(id.getText()));
                    
                    cs.execute();
                    
                    JOptionPane.showMessageDialog(null, "Se eliminó correctamente");
                } catch (Exception e) {
                    
                    JOptionPane.showMessageDialog(null, "No se eliminó correctamente, error:"+e.toString());
                }
                
                finally{
                    objetoConexion.cerrarConexion();
                }
            }
            
            public void LimpiarCampos(JTextField id,JTextField nombres, JTextField apellidos, JTextField edad, JDateChooser fnacimiento,JTextField rutaimagen,  JLabel imagencontenido){
                
                id.setText("");
                nombres.setText("");
                apellidos.setText("");
                edad.setText("");
                
                Calendar calendario = Calendar.getInstance();
                
                fnacimiento.setDate(calendario.getTime());
                
                rutaimagen.setText("");
                
                imagencontenido.setIcon(null);
                
            }
            
            
}

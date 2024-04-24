
package com.mycompany.crud;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class CPersonas {
    
    int codigo;
    String nombrePersona;
    String apellidoPersona;
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getApellidoPersona() {
        return apellidoPersona;
    }

    public void setApellidoPersona(String apellidoPersona) {
        this.apellidoPersona = apellidoPersona;
    }
    
    public void mostrarPersonas (JTable paramTablaTotalPersonas){
    
        CConexion objetoConexion = new CConexion();
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        String sql = "";
        modelo.addColumn("ID");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        paramTablaTotalPersonas.setModel(modelo);
        
        sql ="select * from Personas;";
        
        String [] datos = new String[3];
        Statement st;
        try {
            st = objetoConexion.establecerConexion().createStatement();
            
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                
                modelo.addRow(datos);
                
            }
            paramTablaTotalPersonas.setModel(modelo);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"ERROR:"+e.toString());
            
        }
    }
   
    public void insertarPersona(JTextField paramNombres, JTextField paramApellidos ){
        
        setNombrePersona(paramNombres.getText());
        setApellidoPersona(paramApellidos.getText());
        
        CConexion objetoConexion = new CConexion();
        
        String consulta = "insert into Personas (nombres,apellidos) values (?,?);";
        
        try {
            
            CallableStatement cs = objetoConexion.establecerConexion().prepareCall(consulta);
            cs.setString(1, getNombrePersona());
            cs.setString(2, getApellidoPersona());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se insertó correctamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR"+e.toString());

        }
    }
    
    
    public void seleccionarPersona(JTable paramTablePersona, JTextField paramCodigo, JTextField paramNombres, JTextField paramApellidos){
        
        try {
            
            int fila = paramTablePersona.getSelectedRow();
            
            if (fila>=0){
                
                paramCodigo.setText(paramTablePersona.getValueAt(fila, 0).toString());
                paramNombres.setText(paramTablePersona.getValueAt(fila, 1).toString());
                paramApellidos.setText(paramTablePersona.getValueAt(fila, 2).toString());
                
            }else{
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR"+e.toString());
        }
    }
    
    public void modificarPersona(JTextField paramCodigo,JTextField paramNombres, JTextField paramApellidos ){
        
        setCodigo(Integer.parseInt(paramCodigo.getText()));
        setNombrePersona(paramNombres.getText());
        setApellidoPersona(paramApellidos.getText());
        
        CConexion objetoConexion = new CConexion();
        
        String consulta = "UPDATE Personas SET nombres=?, apellidos=? WHERE Personas.id=?;";
        
        try {
            
            CallableStatement cs = objetoConexion.establecerConexion().prepareCall(consulta);
            cs.setString(1, getNombrePersona());
            cs.setString(2, getApellidoPersona());
            cs.setInt(3, getCodigo());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se modificó correctamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR"+e.toString());

        }
    }
    
    public void eliminarPersona(JTextField paramCodigo){
        
        setCodigo(Integer.parseInt(paramCodigo.getText()));
        
        
        CConexion objetoConexion = new CConexion();
        
        String consulta = "DELETE FROM Personas WHERE Personas.id=?;";
        
        try {
            
            CallableStatement cs = objetoConexion.establecerConexion().prepareCall(consulta);
            cs.setInt(1, getCodigo());
            
            cs.execute();
            
            JOptionPane.showMessageDialog(null, "Se eliminó correctamente");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR"+e.toString());

        }
    }
}
    
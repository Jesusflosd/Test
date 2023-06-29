/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Administrador;
import Modelo.MetodosAdministrador;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import Modelo.CSV;

/**
 *
 * @author mario
 */
public class frmAdministrador extends javax.swing.JFrame {

    // Variables de instancia
    private String rutaArchivo = "Administradores.txt";
    Administrador administrador;
    MetodosAdministrador metodosAdministrador;

    public void inicializarTablaAdministradores() {
        DefaultTableModel titulos = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int filas, int columnas) {
                return false;
            }
        };
        titulos.addColumn("ID Administrador");
        titulos.addColumn("Nombre");
        titulos.addColumn("Apellido Materno");
        titulos.addColumn("Apellido Paterno");
        titulos.addColumn("Sueldo");
        titulos.addColumn("Usuario");
        titulos.addColumn("Contraseña");
        Object fila[] = new Object[titulos.getColumnCount()];
        for (int i = 0; i < metodosAdministrador.cantidadAdministradorRegistrados(); i++) {
            administrador = metodosAdministrador.obtenerDatosAdministrador(i);
            fila[0] = administrador.getIdAdministrador();
            fila[1] = administrador.getNombre();
            fila[2] = administrador.getApellidoPaterno();
            fila[3] = administrador.getApellidoMaterno();
            fila[4] = administrador.getSueldo();
            fila[5] = administrador.getUsuario();
            fila[6] = administrador.getContrasenia();
            titulos.addRow(fila);
        }
        tabla_consultaAdministradores.setModel(titulos);
        tabla_consultaAdministradores.setRowHeight(60);
    }

    // Metodos para leer los campos ingresados por registrar
    public int leerIDTextField() {
        try {
            int idAdministrador = Integer.parseInt(texto_ID.getText().trim());
            return idAdministrador;
        } catch (Exception exception) {
            return -666;
        }
    }

    public String leerNombreTextField() {
        try {
            String nombre = texto_nombre.getText().trim().replace(" ", " ");
            return nombre;
        } catch (Exception exception) {
            return null;
        }
    }

    public String leerApellidoPaternoTextField() {
        try {
            String apellidoPaterno = texto_apellidoP.getText().trim().replace(" ", " ");
            return apellidoPaterno;
        } catch (Exception exception) {
            return null;
        }
    }

    public String leerApellidoMaternoTextField() {
        try {
            String apellidoMaterno = texto_apellidoM.getText().trim().replace(" ", " ");
            return apellidoMaterno;
        } catch (Exception exception) {
            return null;
        }
    }

    public double leerSueldoTextField() {
        try {
            double sueldo = Double.parseDouble(texto_sueldo.getText().trim());
            return sueldo;
        } catch (Exception exception) {
            return -666;
        }
    }

    public String leerUsuarioTextField() {
        try {
            String usuarioTxt = usuario.getText().trim();
            return usuarioTxt;
        } catch (Exception exception) {
            return null;
        }
    }

    private String leerContraseniaTextField() {
        try {
            String contraseniaTxt = contrasenia.getText().trim();
            return contraseniaTxt;
        } catch (Exception exception) {
            return null;
        }
    }

    // Metodos para poder leer y guardar el contenido del archivo de texto
    public void verContenidoTXT() {
        try {
            FileInputStream file = new FileInputStream(rutaArchivo);
            ObjectInputStream input = new ObjectInputStream(file);
            if (input != null) {
                metodosAdministrador = (MetodosAdministrador) input.readObject();
                input.close();
            }
        } catch (Exception exception) {
            System.out.println(exception);
            JOptionPane.showMessageDialog(null, "Error al leer el archivo");
        }
    }

    public void guardarContenidoTXT() {
        try {
            FileOutputStream file = new FileOutputStream(rutaArchivo);
            ObjectOutputStream output = new ObjectOutputStream(file);
            if (output != null) {
                output.writeObject(metodosAdministrador);
                output.close();
            }
        } catch (Exception exception) {
            System.out.println(exception.getStackTrace());
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo");
        }
    }

    // Metodo para quitar el contenido de las celdas al guardar
    public void limpiarCeldas(JPanel jPanel) {
        for (int i = 0; jPanel.getComponents().length > i; i++) {
            if (jPanel.getComponents()[i] instanceof JTextField) {
                ((JTextField) jPanel.getComponents()[i]).setText("");
            } else if (jPanel.getComponents()[i] instanceof JPasswordField) {
                ((JPasswordField) jPanel.getComponents()[i]).setText("");
            }
        }
    }
    CSV csv = new CSV();
    String fileName = "Administradores.csv";
    public void guardarAdministrador() {
        try {
            // que es -666
            //Cambiar el manejo de errores
            if (leerIDTextField() == -666) {
                JOptionPane.showMessageDialog(null, "Ingrese un numero entero");
            } else if (leerNombreTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre");
            } else if (leerApellidoPaternoTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el apellido paterno");
            } else if (leerApellidoMaternoTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el apellido materno");
            } else if (leerSueldoTextField() == -666) {
                JOptionPane.showMessageDialog(null, "Ingrese el sueldo");
            } else if (leerUsuarioTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el usuario");
            } else if (leerContraseniaTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese la contraseña");
            } else {
                administrador = new Administrador(leerNombreTextField(), leerApellidoPaternoTextField(), leerApellidoMaternoTextField(), leerSueldoTextField(), leerUsuarioTextField(), leerIDTextField(), leerContraseniaTextField());
                if ((metodosAdministrador.compararExistenteID((int) administrador.getIdAdministrador())) != (-1)) {
                    JOptionPane.showMessageDialog(null, "Este ID ya ha sido asginado");
                } else {
                    metodosAdministrador.agregarDatosAdministrador(administrador);
                    csv.agregarFilaDatos(fileName, administrador);
                    guardarContenidoTXT();
                    inicializarTablaAdministradores();
                    limpiarCeldas(panelRegistro);
                }

            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Error al guardar administrador");
        }
    }

    public void modificarAdministrador() {
        try {
            if (leerIDTextField() == -666) {
                JOptionPane.showMessageDialog(null, "Ingrese un numero entero");
            } else if (leerNombreTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre");
            } else if (leerApellidoPaternoTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el apellido paterno");
            } else if (leerApellidoMaternoTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el apellido materno");
            } else if (leerSueldoTextField() == -666) {
                JOptionPane.showMessageDialog(null, "Ingrese el sueldo");
            } else if (leerUsuarioTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre de usuario");
            } else if (leerContraseniaTextField().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese la contraseña");
            } else {
                administrador = new Administrador(leerNombreTextField(), leerApellidoPaternoTextField(), leerApellidoMaternoTextField(), leerSueldoTextField(), leerUsuarioTextField(), leerIDTextField(), leerContraseniaTextField());
                int idAdministrador = metodosAdministrador.compararExistenteID((int) leerIDTextField());
                if (idAdministrador == -1) {
                    metodosAdministrador.agregarDatosAdministrador(administrador);
                } else {
                    metodosAdministrador.modificarDatosAdministrador((int) idAdministrador, administrador);
                    csv.modificarFilaDatos(fileName,idAdministrador, administrador);
                }
                guardarContenidoTXT();
                inicializarTablaAdministradores();
                limpiarCeldas(panelRegistro);
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Error al modificar administrador");
        }
    }

    public void eliminarAdministrador() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar al administrador?", "Aviso", 0);
            if (confirmacion == 0) {
                metodosAdministrador.eliminarDatosAdministrador(administrador);
                csv.eliminarFilaDatos(fileName, administrador.getIdAdministrador());
                guardarContenidoTXT();
                inicializarTablaAdministradores();
                limpiarCeldas(panelRegistro);
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Error al eliminar administrador");
        }
    }

    /**
     * Creates new form
     */
    public frmAdministrador() {
        initComponents();
        setLocationRelativeTo(null);
        metodosAdministrador = new MetodosAdministrador();
        try {
            verContenidoTXT();
            inicializarTablaAdministradores();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "El archivo de texto no existe");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        boton_guardar = new javax.swing.JButton();
        boton_modificar = new javax.swing.JButton();
        boton_eliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_consultaAdministradores = new javax.swing.JTable();
        panelRegistro = new javax.swing.JPanel();
        label_apellidoP = new javax.swing.JLabel();
        label_apellidoM = new javax.swing.JLabel();
        label_sueldo = new javax.swing.JLabel();
        label_ID = new javax.swing.JLabel();
        texto_ID = new javax.swing.JTextField();
        texto_nombre = new javax.swing.JTextField();
        texto_apellidoP = new javax.swing.JTextField();
        texto_apellidoM = new javax.swing.JTextField();
        texto_sueldo = new javax.swing.JTextField();
        label_nombre = new javax.swing.JLabel();
        label_titulo = new javax.swing.JLabel();
        usuarioLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usuario = new javax.swing.JTextField();
        contrasenia = new javax.swing.JTextField();
        btnRegresarAMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        boton_guardar.setBackground(new java.awt.Color(224, 255, 255));
        boton_guardar.setForeground(new java.awt.Color(1, 1, 1));
        boton_guardar.setText("Guardar");
        boton_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_guardarActionPerformed(evt);
            }
        });

        boton_modificar.setBackground(new java.awt.Color(224, 255, 255));
        boton_modificar.setForeground(new java.awt.Color(1, 1, 1));
        boton_modificar.setText("Modificar");
        boton_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_modificarActionPerformed(evt);
            }
        });

        boton_eliminar.setBackground(new java.awt.Color(224, 255, 255));
        boton_eliminar.setForeground(new java.awt.Color(1, 1, 1));
        boton_eliminar.setText("Eliminar");
        boton_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_eliminarActionPerformed(evt);
            }
        });

        tabla_consultaAdministradores.setBackground(new java.awt.Color(224, 255, 255));
        tabla_consultaAdministradores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla_consultaAdministradores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_consultaAdministradoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla_consultaAdministradores);

        label_apellidoP.setText("Apellido Paterno:");

        label_apellidoM.setText("Apellido Materno:");

        label_sueldo.setText("Sueldo:");

        label_ID.setText("ID Administrador:");

        texto_ID.setBackground(new java.awt.Color(224, 255, 255));
        texto_ID.setForeground(new java.awt.Color(1, 1, 1));
        texto_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texto_IDActionPerformed(evt);
            }
        });

        texto_nombre.setBackground(new java.awt.Color(224, 255, 255));
        texto_nombre.setForeground(new java.awt.Color(1, 1, 1));

        texto_apellidoP.setBackground(new java.awt.Color(224, 255, 255));
        texto_apellidoP.setForeground(new java.awt.Color(1, 1, 1));

        texto_apellidoM.setBackground(new java.awt.Color(224, 255, 255));
        texto_apellidoM.setForeground(new java.awt.Color(1, 1, 1));

        texto_sueldo.setBackground(new java.awt.Color(224, 255, 255));
        texto_sueldo.setForeground(new java.awt.Color(1, 1, 1));

        label_nombre.setText("Nombre:");

        label_titulo.setFont(new java.awt.Font("Jamrul", 1, 18)); // NOI18N
        label_titulo.setText("Registro Administrador");

        usuarioLabel.setText("Usuario:");

        jLabel2.setText("Contraseña:");

        usuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRegistroLayout = new javax.swing.GroupLayout(panelRegistro);
        panelRegistro.setLayout(panelRegistroLayout);
        panelRegistroLayout.setHorizontalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(label_titulo, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegistroLayout.createSequentialGroup()
                            .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRegistroLayout.createSequentialGroup()
                                    .addComponent(label_ID)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(texto_ID))
                                .addGroup(panelRegistroLayout.createSequentialGroup()
                                    .addComponent(label_nombre)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(texto_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelRegistroLayout.createSequentialGroup()
                                    .addComponent(label_apellidoP)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(texto_apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(63, 63, 63)
                            .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegistroLayout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(contrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelRegistroLayout.createSequentialGroup()
                                    .addComponent(usuarioLabel)
                                    .addGap(32, 32, 32)
                                    .addComponent(usuario)))
                            .addGap(21, 21, 21)))
                    .addGroup(panelRegistroLayout.createSequentialGroup()
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_apellidoM)
                            .addComponent(label_sueldo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(texto_sueldo)
                            .addComponent(texto_apellidoM, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))))
                .addGap(6, 6, 6))
        );
        panelRegistroLayout.setVerticalGroup(
            panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRegistroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_ID)
                    .addComponent(texto_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usuarioLabel)
                    .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_nombre)
                    .addComponent(texto_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(contrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_apellidoP)
                    .addComponent(texto_apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(texto_apellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_apellidoM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(texto_sueldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_sueldo))
                .addContainerGap())
        );

        btnRegresarAMenu.setText("Regresar");
        btnRegresarAMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarAMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnRegresarAMenu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(boton_modificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(boton_eliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(boton_guardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(76, 76, 76))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(boton_guardar)
                        .addGap(30, 30, 30)
                        .addComponent(boton_modificar)
                        .addGap(30, 30, 30)
                        .addComponent(boton_eliminar))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegresarAMenu)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_guardarActionPerformed
        // TODO add your handling code here:
        this.guardarAdministrador();
    }//GEN-LAST:event_boton_guardarActionPerformed

    private void boton_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_modificarActionPerformed
        // TODO add your handling code here:
        this.modificarAdministrador();
    }//GEN-LAST:event_boton_modificarActionPerformed

    private void boton_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_eliminarActionPerformed
        // TODO add your handling code here:
        this.eliminarAdministrador();
    }//GEN-LAST:event_boton_eliminarActionPerformed

    private void tabla_consultaAdministradoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_consultaAdministradoresMouseClicked
        // TODO add your handling code here:
        int mouseClick = tabla_consultaAdministradores.rowAtPoint(evt.getPoint());

        int idAdministrador = (int) tabla_consultaAdministradores.getValueAt(mouseClick, 0);
        String nombre = "" + tabla_consultaAdministradores.getValueAt(mouseClick, 1);
        String apellidoPaterno = "" + tabla_consultaAdministradores.getValueAt(mouseClick, 2);
        String apellidoMaterno = "" + tabla_consultaAdministradores.getValueAt(mouseClick, 3);
        double sueldo = (double) tabla_consultaAdministradores.getValueAt(mouseClick, 4);
        String texto_usuario = "" + tabla_consultaAdministradores.getValueAt(mouseClick, 5);
        String texto_contrasenia = "" + tabla_consultaAdministradores.getValueAt(mouseClick, 6);

        texto_ID.setText(String.valueOf(idAdministrador));
        texto_nombre.setText(nombre);
        texto_apellidoP.setText(apellidoPaterno);
        texto_apellidoM.setText(apellidoMaterno);
        texto_sueldo.setText(String.valueOf(sueldo));
        usuario.setText(String.valueOf(texto_usuario));
        contrasenia.setText(String.valueOf(texto_contrasenia));
    }//GEN-LAST:event_tabla_consultaAdministradoresMouseClicked

    private void usuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usuarioActionPerformed

    private void texto_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texto_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texto_IDActionPerformed

    private void btnRegresarAMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarAMenuActionPerformed
        // TODO add your handling code here:
        frmMenu menu = new frmMenu();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarAMenuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdministrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton boton_eliminar;
    private javax.swing.JButton boton_guardar;
    private javax.swing.JButton boton_modificar;
    private javax.swing.JButton btnRegresarAMenu;
    private javax.swing.JTextField contrasenia;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label_ID;
    private javax.swing.JLabel label_apellidoM;
    private javax.swing.JLabel label_apellidoP;
    private javax.swing.JLabel label_nombre;
    private javax.swing.JLabel label_sueldo;
    private javax.swing.JLabel label_titulo;
    private javax.swing.JPanel panelRegistro;
    private javax.swing.JTable tabla_consultaAdministradores;
    private javax.swing.JTextField texto_ID;
    private javax.swing.JTextField texto_apellidoM;
    private javax.swing.JTextField texto_apellidoP;
    private javax.swing.JTextField texto_nombre;
    private javax.swing.JTextField texto_sueldo;
    private javax.swing.JTextField usuario;
    private javax.swing.JLabel usuarioLabel;
    // End of variables declaration//GEN-END:variables
}

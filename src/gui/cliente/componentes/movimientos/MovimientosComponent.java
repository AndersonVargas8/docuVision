package gui.cliente.componentes.movimientos;

import datos.Documento;
import datos.Movimiento;
import estructuras.listas.ListaEncadenadaSimple;
import gui.servicios.serviciosGraficos.RecursosService;
import gui.servicios.serviciosLogicos.DocumentosService;
import gui.servicios.serviciosLogicos.MovimientosService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;

public class MovimientosComponent implements MouseListener, ActionListener, FocusListener {
    private MovimientosTemplate movimientosTemplate;
    private MovimientosService sMovimientos;

    //Objetos de apoyo
    private Movimiento movimiento;
    private String[] placeholders = {"Fecha..."};
    public MovimientosComponent(){
        this.movimientosTemplate = new MovimientosTemplate(this);
        sMovimientos = MovimientosService.getServicio();

        mostrarRegistrosTabla();
    }

    public MovimientosTemplate getMovimientosTemplate() {
        return movimientosTemplate;
    }

    //ActionListener
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    //FocusListener
    @Override
    public void focusGained(FocusEvent e) {
        JTextField textField = (JTextField) e.getSource();
        textField.setBorder(RecursosService.getServicio().getBordeTextField());

        if(textField.getText().equals(placeholders[0]))
            textField.setText("");
    }

    @Override
    public void focusLost(FocusEvent e) {
        JTextField textField = ((JTextField) e.getSource());
        textField.setBorder(null);
    }

    //MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == movimientosTemplate.getbFiltrar())
            filtrarRegistrosTabla();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource() instanceof JButton){
            JButton boton = ((JButton) e.getSource());
            boton.setBackground(RecursosService.getServicio().getColorPrincipalOscuro());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(e.getSource() instanceof JButton){
            JButton boton = ((JButton) e.getSource());
            boton.setBackground(RecursosService.getServicio().getColorPrincipal());
        }
    }

    //MÉTODOS PARA EL CONTROL DE INFORMACIÓN EN LA TABLA
    public void mostrarRegistrosTabla(){
        eliminarRegistros();
        agregarRegistros(sMovimientos.imprimirTodo());
    }

    public void agregarRegistros(Iterable<Movimiento> datos){
        for(Movimiento mov: datos)
            agregarRegistro(mov);
    }
    public void eliminarRegistros(){
        DefaultTableModel dm = movimientosTemplate.getModelo();
        int c = dm.getRowCount();

        for (int i = c -1 ; i >= 0; i--)
            dm.removeRow(i);
    }
    public void agregarRegistro(Movimiento movimiento){
        movimientosTemplate.getModelo().addRow(
                new Object[]{
                        movimiento.getIdDocumento(),
                        movimiento.getTipoDocumento(),
                        movimiento.getNombreDocumento(),
                        movimiento.getUbicacionDocumento(),
                        movimiento.getTipoMovimiento(),
                        movimiento.getFecha().toString(),
                        movimiento.getFecha().getHoraCompleta(),
                        movimiento.getUsuario()
                }
        );

    }

    public void actualizarValores(){
        mostrarRegistrosTabla();
    }

    public void filtrarRegistrosTabla(){
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(movimientosTemplate.getModelo());
        movimientosTemplate.getTabla().setRowSorter(trs);
        ListaEncadenadaSimple<RowFilter<RowFilter,Integer>> lista = new ListaEncadenadaSimple<>();

        if(movimientosTemplate.getCbUsuario().getSelectedIndex() != 0)
            lista.insertarAlInicio(RowFilter.regexFilter(
                    movimientosTemplate.getCbUsuario().getSelectedItem().toString(),7)
            );

        if(movimientosTemplate.getCbTipo().getSelectedIndex() != 0)
            lista.insertarAlInicio(RowFilter.regexFilter(
                    movimientosTemplate.getCbTipo().getSelectedItem().toString(),4
            ));

        if(!movimientosTemplate.gettFecha().getText().isEmpty() &&
                !movimientosTemplate.gettFecha().getText().equals(placeholders[0]))
            lista.insertarAlInicio(RowFilter.regexFilter(
                    movimientosTemplate.gettFecha().getText(),5
            ));

        RowFilter filter = RowFilter.andFilter(lista);

        trs.setRowFilter(filter);

    }
}

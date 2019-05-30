/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Percentage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Redes
 */
public class PercentagesView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JPanel searchPane;
    private JPanel searchPaneFlowLayout;
    private JPanel searchPaneFlowLayoutGridLayout;
    private JPanel bodyBoxLayout;
    private JPanel bodyRegisterPanelGridBag;
    private JPanel bodyUpdatePanelGridBag;
    private JPanel bodyDeletePanelGridBag;
    private JPanel bodyRegisterPanelFlow;
    private JLabel idSearchLabel;
    private JButton idSearchButton;
    private JButton buttonShowAllPercentages;
    private JFormattedTextField idTextField;

    private JLabel labelRegisterPercentage;
    private JFormattedTextField textFieldPercentageValue;
    private JButton addPercentageButton;
    
    private JLabel labelUpdatePercentage;
    private JFormattedTextField updateTextFieldPercentageValue;
    private JButton updatePercentageButton;
    
    private JButton deletePercentageButton;

    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Nivel", "Porcentaje"};
    private static final String[][] EMPTY_TABLE = new String[0][0];

    public PercentagesView() throws ParseException {
        initComponents();
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                percentageSearchById(e);
            }
        });
        buttonShowAllPercentages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllPercentages(e);
            }
        });
        addPercentageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!addNewPercentage(e)) {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar el porcentaje.");
                } else {
                    JOptionPane.showMessageDialog(null, "Registro exitoso!");
                }
            }
        });
        updatePercentageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!updatePercentage(e)) {
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar el porcentaje.");
                } else {
                    JOptionPane.showMessageDialog(null, "Actualización exitosa!");
                }
            }
        });
        deletePercentageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!deletePercentage(e)) {
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar el porcentaje.");
                } else {
                    JOptionPane.showMessageDialog(null, "Eliminación exitosa!");
                }
            }
        });
    }

    public boolean addNewPercentage(MouseEvent e) {
        Double percentage = (Double) textFieldPercentageValue.getValue();
        if (percentage == null) {
            JOptionPane.showMessageDialog(null, "El campo Porcentaje está vacío o el valor digitado es inválido.");
            return false;
        }
        
        textFieldPercentageValue.setValue(null);
        textFieldPercentageValue.setText("");

        return DatabaseConnection.getPercentageDao().addPercentage(percentage);
    }
    
    public boolean updatePercentage(MouseEvent e) {
        if (table.getSelectedRow()==-1) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un porcentaje a modificar.");
            return false;
        }
        Double per = (Double)updateTextFieldPercentageValue.getValue();
        if(per==null){
            JOptionPane.showMessageDialog(null, "Debe digitar un porcentaje.");
            return false;
        }
        updateTextFieldPercentageValue.setValue(null);
        updateTextFieldPercentageValue.setText("");
        
        Long id = (Long) table.getValueAt(table.getSelectedRow(), 0);
        Percentage p = new Percentage(id, per);
        
        return DatabaseConnection.getPercentageDao().updatePercentage(p);
    }
    
    public boolean deletePercentage(MouseEvent e) {

        return DatabaseConnection.getPercentageDao().removePercentage();
    }

    public void percentageSearchById(MouseEvent e) {
        Long id = (Long) idTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Percentage percentage = DatabaseConnection.getPercentageDao().getPercentageById(id);
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);

        if (percentage != null) {
           tableModel.addRow(new Object[]{percentage.getId(),
                percentage.getPercentage()});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro el producto con el código especificado.");
        }

    }

    public void showAllPercentages(MouseEvent e) {

        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<Percentage> percentages = DatabaseConnection.getPercentageDao().getAllPercentages();

        if (percentages != null && !percentages.isEmpty()) {
            percentages.forEach((percentage) -> {
                tableModel.addRow(new Object[]{percentage.getId(),
                    percentage.getPercentage()});
            });
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void initComponents() throws ParseException {
        this.setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(TABLE_MODEL_IDENTIFIERS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        table = new JTable(tableModel);
        table.setDragEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Lista Niveles Porcentajes"));

        EmptyBorder eb = new EmptyBorder(new Insets(20, 20, 20, 20));
        searchPane = new JPanel(new BorderLayout());
        searchPane.setBorder(eb);

        idSearchLabel = new JLabel("Nivel del porcentaje: ");

        Dimension preferredTextFieldsSize = new Dimension(200, 30);

        idTextField = new JFormattedTextField(0L);
        idTextField.setPreferredSize(preferredTextFieldsSize);
        idTextField.setValue(null);

        idSearchButton = new JButton("Buscar");

        buttonShowAllPercentages = new JButton("Mostrar todos");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        searchPaneFlowLayoutGridLayout = new JPanel(new GridLayout(2, 3, 10, 10));

        searchPaneFlowLayoutGridLayout.add(idSearchLabel);
        searchPaneFlowLayoutGridLayout.add(idTextField);
        searchPaneFlowLayoutGridLayout.add(idSearchButton);

        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        searchPaneFlowLayoutGridLayout.add(buttonShowAllPercentages);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());

        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout);

        bodyBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(bodyBoxLayout, BoxLayout.X_AXIS);
        bodyBoxLayout.setLayout(b);

        this.labelRegisterPercentage = new JLabel("<html>Porcentaje (Utilice coma (,)<br> en vez de punto decimal): </html>");

        this.textFieldPercentageValue = new JFormattedTextField(0D);
        this.textFieldPercentageValue.setValue(null);
        this.textFieldPercentageValue.setText("");
        this.textFieldPercentageValue.setPreferredSize(preferredTextFieldsSize);

        this.addPercentageButton = new JButton("Agregar un nivel");
        
        this.labelUpdatePercentage = new JLabel("<html>Porcentaje (Utilice coma (,)<br> en vez de punto decimal): </html>");

        this.updateTextFieldPercentageValue = new JFormattedTextField(0D);
        this.updateTextFieldPercentageValue.setValue(null);
        this.updateTextFieldPercentageValue.setText("");
        this.updateTextFieldPercentageValue.setPreferredSize(preferredTextFieldsSize);

        this.updatePercentageButton = new JButton("Actualizar");

        this.deletePercentageButton = new JButton("Eliminar");

        this.bodyRegisterPanelFlow = new JPanel();
        BoxLayout bl = new BoxLayout(this.bodyRegisterPanelFlow, BoxLayout.Y_AXIS);
        this.bodyRegisterPanelFlow.setLayout(bl);

        this.bodyRegisterPanelGridBag = new JPanel(new GridBagLayout());
        this.bodyRegisterPanelGridBag.setBorder(BorderFactory.createTitledBorder("Nuevo Porcentaje"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHEAST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyRegisterPanelGridBag.add(this.labelRegisterPercentage, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyRegisterPanelGridBag.add(this.textFieldPercentageValue, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyRegisterPanelGridBag.add(this.addPercentageButton, gbc);
        
        this.bodyUpdatePanelGridBag = new JPanel(new GridBagLayout());
        this.bodyUpdatePanelGridBag.setBorder(BorderFactory.createTitledBorder("Actualizar Selección"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHEAST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyUpdatePanelGridBag.add(this.labelUpdatePercentage, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyUpdatePanelGridBag.add(this.updateTextFieldPercentageValue, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyUpdatePanelGridBag.add(this.updatePercentageButton, gbc);
        
        this.bodyDeletePanelGridBag = new JPanel(new GridBagLayout());
        this.bodyDeletePanelGridBag.setBorder(BorderFactory.createTitledBorder("Eliminar Último Nivel"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHEAST;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyDeletePanelGridBag.add(this.deletePercentageButton, gbc);

        this.bodyRegisterPanelFlow.add(this.bodyRegisterPanelGridBag);
        this.bodyRegisterPanelFlow.add(this.bodyUpdatePanelGridBag);
        this.bodyRegisterPanelFlow.add(this.bodyDeletePanelGridBag);

        this.bodyBoxLayout.add(this.bodyRegisterPanelFlow);
        this.bodyBoxLayout.add(this.tableScroll);

        this.add(this.bodyBoxLayout, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);

    }
}

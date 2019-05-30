/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Category;
import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Redes
 */
public class CategoriesView extends JPanel {

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
    private JLabel nameSearchLabel;
    private JButton nameSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAllCategories;
    private JFormattedTextField idTextField;
    private JTextField nameTextField;

    private JLabel categoryName;
    private JTextField categoryNameValue;
    private JButton addCategoryButton;

    private JLabel updateCategoryName;
    private JTextField updateCategoryNameValue;
    private JButton updateCategoryButton;

    private JButton deleteCategoryButton;
    
    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Código", "Nombre"};
    private static final String[][] EMPTY_TABLE = new String[0][0];
    
    private UsersView usersView;

    public CategoriesView(UsersView userView1) throws ParseException {
        initComponents();
        this.usersView = userView1;
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                categorySearchById(e);
            }
        });
        nameSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                categoriesSearchByName(e);
            }
        });
        buttonShowAllCategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllProducts(e);
            }
        });
        addCategoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!addNewCategory(e)){
                    JOptionPane.showMessageDialog(null, "No se pudo registrar la categoría.");
                }else{
                    JOptionPane.showMessageDialog(null, "Registro exitoso!");
                    usersView.setDataUI();
                }
            }
        });
        updateCategoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!updateCategory(e)){
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar la categoría.");
                }else{
                    JOptionPane.showMessageDialog(null, "Actualización exitosa!");
                    usersView.setDataUI();
                }
            }
        });
        deleteCategoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!deleteCategory(e)){
                    JOptionPane.showMessageDialog(null, "No se pudo eliminar la categoría.");
                }else{
                    JOptionPane.showMessageDialog(null, "Eliminación exitosa!");
                    usersView.setDataUI();
                }
            }
        });
    }
    
    public boolean addNewCategory(MouseEvent e){
        String name = categoryNameValue.getText();
        if(name==null || name.equals("")){
            JOptionPane.showMessageDialog(null, "El campo Nombre está vacío.");
            return false;
        }
        
        categoryNameValue.setText("");
        
        Category c = new Category(null, name);
        return DatabaseConnection.getCategoryDao().addCategory(c);
    }
    
    public boolean updateCategory(MouseEvent e){
        String name = updateCategoryNameValue.getText();
        if(name==null || name.equals("")){
            JOptionPane.showMessageDialog(null, "El campo Nombre está vacío.");
            return false;
        }
        if(table.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría!");
            return false;
        }
        
        updateCategoryNameValue.setText("");
        
        Long id = (Long)table.getValueAt(table.getSelectedRow(), 0);
        Category c = new Category(id, name);
        return DatabaseConnection.getCategoryDao().updateCategory(c);
    }
    
    public boolean deleteCategory(MouseEvent e){
        if(table.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar una categoría!");
            return false;
        }
        Long id = (Long)table.getValueAt(table.getSelectedRow(), 0);
        
        return DatabaseConnection.getCategoryDao().removeCategoryById(id);
    }

    public void categorySearchById(MouseEvent e) {
        Long id = (Long) idTextField.getValue();
        if(id == null){
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }
        
        idTextField.setText("");
        idTextField.setValue(null);
        
        Category category = DatabaseConnection.getCategoryDao().getCategoryById(id);
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);

        if (category != null) {
            tableModel.addRow(new Object[]{category.getId(), category.getName()});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro la categoría con el código especificado.");
        }

    }
    
    public void categoriesSearchByName(MouseEvent e){
        String name = nameTextField.getText();
        if(name.equals("")){
            JOptionPane.showMessageDialog(this, "El campo no puede estar vacío.");
            return;
        }
        
        nameTextField.setText("");
        
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<Category> categories = DatabaseConnection.getCategoryDao().getCategoriesByName(name);
        
        if (categories != null && !categories.isEmpty()) {
            categories.forEach((category) -> {
                tableModel.addRow(new Object[]{category.getId(), category.getName()});
            });
        }else{
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }
    
    public void showAllProducts(MouseEvent e){
        
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<Category> categories = DatabaseConnection.getCategoryDao().getAllCategories();
        
        if (categories != null && !categories.isEmpty()) {
            categories.forEach((category) -> {
                tableModel.addRow(new Object[]{category.getId(), category.getName()});
            });
        }else{
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
        tableScroll.setBorder(BorderFactory.createTitledBorder("Lista Categorías"));
        
        EmptyBorder eb = new EmptyBorder(new Insets(20, 20, 20, 20));
        searchPane = new JPanel(new BorderLayout());
        searchPane.setBorder(eb);

        idSearchLabel = new JLabel("Código de la categoría: ");
        idSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        nameSearchLabel = new JLabel("Nombre de la categoría: ");
        nameSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        Dimension preferredTextFieldsSize = new Dimension(200, 30);
        
        idTextField = new JFormattedTextField(0L);
        idTextField.setPreferredSize(preferredTextFieldsSize);
        idTextField.setValue(null);
        
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(preferredTextFieldsSize);
        nameTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        idSearchButton = new JButton("Buscar");
        
        nameSearchButton = new JButton("Buscar");
        
        buttonShowAllCategories = new JButton("Mostrar todo");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        searchPaneFlowLayoutGridLayout = new JPanel(new GridLayout(3, 3, 10, 10));

        searchPaneFlowLayoutGridLayout.add(idSearchLabel);
        searchPaneFlowLayoutGridLayout.add(idTextField);
        searchPaneFlowLayoutGridLayout.add(idSearchButton);
        
        searchPaneFlowLayoutGridLayout.add(nameSearchLabel);
        searchPaneFlowLayoutGridLayout.add(nameTextField);
        searchPaneFlowLayoutGridLayout.add(nameSearchButton);
        
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        searchPaneFlowLayoutGridLayout.add(buttonShowAllCategories);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());

        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout);

        bodyBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(bodyBoxLayout, BoxLayout.X_AXIS);
        bodyBoxLayout.setLayout(b);
        
        this.categoryName = new JLabel("Nombre: ");
        
        this.categoryNameValue = new JTextField(20);
        
        this.addCategoryButton = new JButton("Registrar categoría");
        
        this.updateCategoryName = new JLabel("Nombre: ");
        
        this.updateCategoryNameValue = new JTextField(20);
        
        this.updateCategoryButton = new JButton("Actualizar categoría");
        
        this.deleteCategoryButton = new JButton("Eliminar categoría");
        
        
        this.bodyRegisterPanelFlow = new JPanel();
        BoxLayout bl = new BoxLayout(this.bodyRegisterPanelFlow, BoxLayout.Y_AXIS);
        this.bodyRegisterPanelFlow.setLayout(bl);
        
        this.bodyRegisterPanelGridBag = new JPanel(new GridBagLayout());
        this.bodyRegisterPanelGridBag.setBorder(BorderFactory.createTitledBorder("Nueva Categoría"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyRegisterPanelGridBag.add(this.categoryName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyRegisterPanelGridBag.add(this.categoryNameValue, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyRegisterPanelGridBag.add(this.addCategoryButton, gbc);
        
        this.bodyUpdatePanelGridBag = new JPanel(new GridBagLayout());
        this.bodyUpdatePanelGridBag.setBorder(BorderFactory.createTitledBorder("Actualizar Categoría Seleccionada"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyUpdatePanelGridBag.add(this.updateCategoryName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyUpdatePanelGridBag.add(this.updateCategoryNameValue, gbc);
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyUpdatePanelGridBag.add(this.updateCategoryButton, gbc);
        
        this.bodyDeletePanelGridBag = new JPanel(new GridBagLayout());
        this.bodyDeletePanelGridBag.setBorder(BorderFactory.createTitledBorder("Eliminar Categoría Seleccionada"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.NORTHEAST;
        
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyDeletePanelGridBag.add(this.deleteCategoryButton, gbc);
        
        this.bodyRegisterPanelFlow.add(this.bodyRegisterPanelGridBag);
        this.bodyRegisterPanelFlow.add(this.bodyUpdatePanelGridBag);
        this.bodyRegisterPanelFlow.add(this.bodyDeletePanelGridBag);
        
        this.bodyBoxLayout.add(this.bodyRegisterPanelFlow);
        this.bodyBoxLayout.add(this.tableScroll);
        
        this.add(this.bodyBoxLayout, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);

    }
}

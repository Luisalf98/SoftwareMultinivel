/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Product;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Redes
 */
public class ProductsView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JPanel searchPane;
    private JPanel searchPaneFlowLayout;
    private JPanel searchPaneFlowLayoutGridLayout;
    private JPanel bodyBoxLayout;
    private JPanel bodyRegisterPanelGridBag;
    private JPanel manageProducts;
    private JPanel deleteProducts;
    private JPanel panelRadioButtons;
    private JRadioButton radioButtonRegister;
    private JRadioButton radioButtonUpdate;
    private ButtonGroup buttonGroup;
    private JLabel idSearchLabel;
    private JLabel nameSearchLabel;
    private JButton nameSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAllProducts;
    private JFormattedTextField idTextField;
    private JTextField nameTextField;

    private JLabel productName;
    private JLabel productDescription;
    private JLabel productPrice;
    private JTextField productNameValue;
    private JTextArea productDescriptionValue;
    private JFormattedTextField productPriceValue;
    private JButton manageProductButton;
    private JButton deleteProductButton;

    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Código", "Nombre", "Descripción",
        "Precio"};
    private static final String[][] EMPTY_TABLE = new String[0][0];

    public ProductsView() throws ParseException {
        initComponents();
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                productSearchById(e);
            }
        });
        nameSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                productsSearchByName(e);
            }
        });
        buttonShowAllProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllProducts(e);
            }
        });
        manageProductButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!manageProduct(e)) {
                    JOptionPane.showMessageDialog(null, "No se pudo realizar la operación.");
                } else {
                    JOptionPane.showMessageDialog(null, "Operación exitosa!");
                }
            }
        });
        deleteProductButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!deleteProduct(e)) {
                    JOptionPane.showMessageDialog(null, "No se pudo realizar la operación.");
                } else {
                    JOptionPane.showMessageDialog(null, "Operación exitosa!");
                }
            }
        });
        this.radioButtonRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                productDescriptionValue.setText("");
                productNameValue.setText("");
                productPriceValue.setValue(null);
                productPriceValue.setText("");
            }
        });
        this.radioButtonUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    Long cod = (Long) table.getValueAt(table.getSelectedRow(), 0);
                    Product p = DatabaseConnection.getProductDao().getProductById(cod);
                    productDescriptionValue.setText(p.getDescription());
                    productNameValue.setText(p.getName());
                    productPriceValue.setValue(p.getPrice());
                } else {
                    productDescriptionValue.setText("");
                    productNameValue.setText("");
                    productPriceValue.setValue(null);
                    productPriceValue.setText("");
                }
            }
        });
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1 && radioButtonUpdate.isSelected()) {
                    Long cod = (Long) table.getValueAt(table.getSelectedRow(), 0);
                    Product p = DatabaseConnection.getProductDao().getProductById(cod);
                    productDescriptionValue.setText(p.getDescription());
                    productNameValue.setText(p.getName());
                    productPriceValue.setValue(p.getPrice());
                }
            }
        });
    }
    
    public boolean deleteProduct(MouseEvent e){
        if(table.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "No seleccionó ningún producto.");
            return false;
        }
        
        Long id = (Long)table.getValueAt(table.getSelectedRow(), 0);
        return DatabaseConnection.getProductDao().removeProductById(id);
    }

    public boolean manageProduct(MouseEvent e) {
        if(table.getSelectedRow()==-1 && radioButtonUpdate.isSelected()){
            JOptionPane.showMessageDialog(null, "Ningún producto seleccionado.");
            return false;
        }
        
        Double price = (Double) productPriceValue.getValue();
        if (price == null) {
            JOptionPane.showMessageDialog(null, "El campo Precio está vacío o el valor digitado es inválido.");
            return false;
        }
        String name = productNameValue.getText();
        if (name == null || name.equals("")) {
            JOptionPane.showMessageDialog(null, "El campo Nombre está vacío.");
            return false;
        }
        String description = productDescriptionValue.getText();
        if (description == null || description.equals("")) {
            JOptionPane.showMessageDialog(null, "El campo Precio está vacío o el valor digitado es inválido.");
            return false;
        }

        this.productDescriptionValue.setText("");
        this.productNameValue.setText("");
        this.productPriceValue.setText("");
        this.productPriceValue.setValue(null);

        if (radioButtonRegister.isSelected()) {
            Product p = new Product(null, name, description, price);
            return DatabaseConnection.getProductDao().addProduct(p);
        }
        
        Long cod = (Long)table.getValueAt(table.getSelectedRow(), 0);
        Product p = new Product(cod, name, description, price);
        return DatabaseConnection.getProductDao().updateProduct(p);
    }

    public void productSearchById(MouseEvent e) {
        Long id = (Long) idTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Product product = DatabaseConnection.getProductDao().getProductById(id);
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);

        if (product != null) {
            tableModel.addRow(new Object[]{product.getId(), product.getName(),
                product.getDescription(), ApplicationStarter.CURRENCY_FORMAT.format(product.getPrice())});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro el producto con el código especificado.");
        }

    }

    public void productsSearchByName(MouseEvent e) {
        String name = nameTextField.getText().toUpperCase();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "El campo no puede estar vacío.");
            return;
        }

        nameTextField.setText("");

        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<Product> products = DatabaseConnection.getProductDao().getProductByName(name);

        if (products != null && !products.isEmpty()) {
            products.forEach((product) -> {
                tableModel.addRow(new Object[]{product.getId(), product.getName(),
                    product.getDescription(), ApplicationStarter.CURRENCY_FORMAT.format(product.getPrice())});
            });
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void showAllProducts(MouseEvent e) {

        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<Product> products = DatabaseConnection.getProductDao().getAllProducts();

        if (products != null && !products.isEmpty()) {
            products.forEach((product) -> {
                tableModel.addRow(new Object[]{product.getId(), product.getName(),
                    product.getDescription(), ApplicationStarter.CURRENCY_FORMAT.format(product.getPrice())});
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
        tableScroll.setBorder(BorderFactory.createTitledBorder("Lista Productos"));

        EmptyBorder eb = new EmptyBorder(new Insets(20, 20, 20, 20));
        searchPane = new JPanel(new BorderLayout());
        searchPane.setBorder(eb);

        idSearchLabel = new JLabel("Código del producto: ");
        idSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        nameSearchLabel = new JLabel("Nombre del producto: ");
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

        buttonShowAllProducts = new JButton("Mostrar todos");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        searchPaneFlowLayoutGridLayout = new JPanel(new GridLayout(3, 3, 10, 10));

        searchPaneFlowLayoutGridLayout.add(idSearchLabel);
        searchPaneFlowLayoutGridLayout.add(idTextField);
        searchPaneFlowLayoutGridLayout.add(idSearchButton);

        searchPaneFlowLayoutGridLayout.add(nameSearchLabel);
        searchPaneFlowLayoutGridLayout.add(nameTextField);
        searchPaneFlowLayoutGridLayout.add(nameSearchButton);

        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        searchPaneFlowLayoutGridLayout.add(buttonShowAllProducts);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());

        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout);

        bodyBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(bodyBoxLayout, BoxLayout.X_AXIS);
        bodyBoxLayout.setLayout(b);

        this.productName = new JLabel("Nombre: ");
        this.productDescription = new JLabel("Descripción: ");
        this.productPrice = new JLabel("Precio: ");

        this.productNameValue = new JTextField();
        this.productDescriptionValue = new JTextArea(10, 20);
        this.productDescriptionValue.setLineWrap(true);
        this.productDescriptionValue.setDragEnabled(false);
        this.productDescriptionValue.setTabSize(500);
        this.productDescriptionValue.setMaximumSize(new Dimension(300, 300));
        this.productPriceValue = new JFormattedTextField(0D);
        this.productPriceValue.setValue(null);
        this.productPriceValue.setText("");

        this.manageProductButton = new JButton("Enviar");

        this.bodyRegisterPanelGridBag = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHEAST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyRegisterPanelGridBag.add(this.productName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyRegisterPanelGridBag.add(this.productNameValue, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyRegisterPanelGridBag.add(this.productDescription, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyRegisterPanelGridBag.add(this.productDescriptionValue, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        this.bodyRegisterPanelGridBag.add(this.productPrice, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        this.bodyRegisterPanelGridBag.add(this.productPriceValue, gbc);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.bodyRegisterPanelGridBag.add(this.manageProductButton, gbc);

        this.manageProducts = new JPanel(new BorderLayout(10, 10));
        this.manageProducts.setBorder(BorderFactory.createTitledBorder("Gestionar Productos"));

        this.panelRadioButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        this.radioButtonRegister = new JRadioButton("Nuevo Producto", true);
        this.radioButtonUpdate = new JRadioButton("Actualizar Producto");

        this.buttonGroup = new ButtonGroup();
        this.buttonGroup.add(this.radioButtonRegister);
        this.buttonGroup.add(this.radioButtonUpdate);

        this.panelRadioButtons.add(this.radioButtonRegister);
        this.panelRadioButtons.add(this.radioButtonUpdate);

        this.deleteProducts = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        this.deleteProductButton = new JButton("Eliminar Producto");
        
        this.deleteProducts.add(this.deleteProductButton);
        
        this.manageProducts.add(this.bodyRegisterPanelGridBag, BorderLayout.CENTER);
        this.manageProducts.add(this.panelRadioButtons, BorderLayout.NORTH);
        this.manageProducts.add(this.deleteProducts, BorderLayout.SOUTH);

        this.bodyBoxLayout.add(this.manageProducts);
        this.bodyBoxLayout.add(this.tableScroll);

        this.add(this.bodyBoxLayout, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);

    }
}

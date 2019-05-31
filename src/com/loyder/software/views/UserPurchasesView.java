/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Sale;
import com.loyder.software.model.entities.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Perez
 */
public class UserPurchasesView extends JPanel{
    
    private JLabel customerId;
    private JLabel customerUserId;
    private JLabel customerName;
    private JLabel customerCategory;
    private JLabel customerIdValue;
    private JLabel customerUserIdValue;
    private JLabel customerNameValue;
    private JLabel customerCategoryValue;
    
    private JPanel customerPanel;
    private JPanel infoPanel;
    private JPanel dividerPanel;
    private JPanel panelDetailsButton;
    
    private JButton buttonSeeDetails;
    
    private DefaultTableModel purchasesTableModel;
    private JTable purchasesTable;
    private JScrollPane purchasesTableScroll;
    
    private JPanel topBarPanel;
    private JButton goBackButton;
    
    private JPanel searchPaneFlowLayoutGridLayout;
    
    private JFormattedTextField idTextField;
    private JFormattedTextField date1TextField;
    private JFormattedTextField date2TextField;
    
    private JLabel idSearchLabel;
    private JLabel dateSearchLabel;
    
    private JButton dateSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAll;
    
    private static final String[] PURCHASES_HEADER = {"Código", "Fecha", "Total", "Tipo de Venta", "Estado"};
    private static final String[][] EMPTY_TABLE = {};
    
    private final JPanel panelParent;
    
    private final JPanel salesPanel;
    private final SaleInfoView saleInfoView;
    private final JTabbedPane tabbedPane;
    
    private Long infoCustomerId;
    
    public UserPurchasesView(JPanel panelParent1, JPanel salesPanel1, SaleInfoView saleInfoView1, JTabbedPane tabbedPane1){
        initComponents();
        this.panelParent = panelParent1;
        this.salesPanel = salesPanel1;
        this.saleInfoView = saleInfoView1;
        this.tabbedPane = tabbedPane1;
        
        this.goBackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)panelParent.getLayout()).show(panelParent, UsersView.class.getName());
            }
        });
        
        this.buttonShowAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllPurchases();
            }
        });
        this.idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                purchaseSearchById((Long)idTextField.getValue());
            }
        });
        this.dateSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                purchasesSearchInDateRange((Date)date1TextField.getValue(), (Date)date2TextField.getValue());
            }
        });
        this.buttonSeeDetails.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(purchasesTable.getSelectedRow()==-1){
                    JOptionPane.showMessageDialog(null, "Debe seleccionar una compra!");
                }else{
                    Long saleId = (Long) purchasesTable.getValueAt(purchasesTable.getSelectedRow(), 0);
                    saleInfoView.setData(saleId);
                    ((CardLayout)salesPanel.getLayout()).show(salesPanel, SaleInfoView.class.getName());
                    tabbedPane.setSelectedComponent(salesPanel);
                }
            }
        });
        
        
    }
    
    public void purchaseSearchById(Long id){
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Sale sale = DatabaseConnection.getSaleDao().getSaleByIdAndUserId(id, infoCustomerId);
        this.purchasesTableModel.setDataVector(EMPTY_TABLE, PURCHASES_HEADER);

        if (sale != null) {
            purchasesTableModel.addRow(new Object[]{sale.getId(), 
                ApplicationStarter.formatDate(new Date(sale.getSaleDate())),
                ApplicationStarter.CURRENCY_FORMAT.format(sale.getTotal()),
                sale.getType(), sale.getState()});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro la compra con el código especificado.");
        }
    }
    
    public void purchasesSearchInDateRange(Date d1, Date d2){
        if (d1 == null || d2 == null) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.");
            return;
        }

        date1TextField.setText("");
        date1TextField.setValue(null);
        date2TextField.setText("");
        date2TextField.setValue(null);

        ArrayList<Sale> sales = DatabaseConnection.getSaleDao().getSalesInDateRangeByUserId(infoCustomerId, d1, d2);
        this.purchasesTableModel.setDataVector(EMPTY_TABLE, PURCHASES_HEADER);

        if (sales != null && !sales.isEmpty()) {
            sales.forEach((sale)->{
                purchasesTableModel.addRow(new Object[]{sale.getId(), 
                    ApplicationStarter.formatDate(new Date(sale.getSaleDate())),
                ApplicationStarter.CURRENCY_FORMAT.format(sale.getTotal()),
                sale.getType(), sale.getState()});
            });
            
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }
    
    public void showAllPurchases(){

        ArrayList<Sale> sales = DatabaseConnection.getSaleDao().getAllSalesByUserId(infoCustomerId);
        this.purchasesTableModel.setDataVector(EMPTY_TABLE, PURCHASES_HEADER);

        if (sales != null && !sales.isEmpty()) {
            sales.forEach((sale)->{
                purchasesTableModel.addRow(new Object[]{sale.getId(), 
                    ApplicationStarter.formatDate(new Date(sale.getSaleDate())),
                ApplicationStarter.CURRENCY_FORMAT.format(sale.getTotal()), 
                sale.getType(), sale.getState()});
            });
            
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }
    
    public void setUIData(Long infoCustomerId){
        this.infoCustomerId = infoCustomerId;
        User user = DatabaseConnection.getUserDao().getUserById(infoCustomerId);
        
        this.customerIdValue.setText(user.getId().toString());
        this.customerUserIdValue.setText(user.getUserId().toString());
        this.customerNameValue.setText(user.getName()+" "+user.getLastName());
        this.customerCategoryValue.setText(user.getCategoryId().toString());
        
    }
    
    public void initComponents(){
        this.setLayout(new BorderLayout(10, 10));
        
        this.customerId = new JLabel("Código: ");
        this.customerIdValue = new JLabel("");
        this.customerUserId = new JLabel("Cédula: ");
        this.customerUserIdValue = new JLabel("");
        this.customerName = new JLabel("Nombre: ");
        this.customerNameValue = new JLabel("");
        this.customerCategory = new JLabel("Categoría: ");
        this.customerCategoryValue = new JLabel("");
        
        this.customerPanel = new JPanel(new GridBagLayout());
        this.customerPanel.setBorder(BorderFactory.createTitledBorder("Cliente"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx=0;
        gbc.gridy=0;
        this.customerPanel.add(this.customerId, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=1;
        gbc.gridy=0;
        this.customerPanel.add(this.customerIdValue, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx=0;
        gbc.gridy=1;
        this.customerPanel.add(this.customerUserId, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=1;
        gbc.gridy=1;
        this.customerPanel.add(this.customerUserIdValue, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx=0;
        gbc.gridy=2;
        this.customerPanel.add(this.customerName, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=1;
        gbc.gridy=2;
        this.customerPanel.add(this.customerNameValue, gbc);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx=0;
        gbc.gridy=3;
        this.customerPanel.add(this.customerCategory, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=1;
        gbc.gridy=3;
        this.customerPanel.add(this.customerCategoryValue, gbc);
        
        searchPaneFlowLayoutGridLayout = new JPanel(new GridBagLayout());
        searchPaneFlowLayoutGridLayout.setBorder(BorderFactory.createTitledBorder("Búsqueda de compras"));
        
        idSearchLabel = new JLabel("Código de la compra: ");
        idTextField = new JFormattedTextField(0L);
        idTextField.setValue(null);
        idTextField.setColumns(20);
        idSearchButton = new JButton("Buscar");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date1TextField = new JFormattedTextField(sdf);
        date2TextField = new JFormattedTextField(sdf);
        dateSearchButton = new JButton("Buscar");
        dateSearchLabel = new JLabel("Fecha Inicial (dd/mm/yyyy) - Fecha Final (dd/mm/yyyy): ");
        
        buttonShowAll = new JButton("Mostrar todo");
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idSearchLabel, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idTextField,gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 2;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idSearchButton,gbc);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(dateSearchLabel,gbc);
        JPanel datesPane = new JPanel();
        BoxLayout bl = new BoxLayout(datesPane, BoxLayout.X_AXIS);
        datesPane.setLayout(bl);
        datesPane.add(date1TextField);
        datesPane.add(date2TextField);
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(datesPane, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 2;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(dateSearchButton,gbc);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        searchPaneFlowLayoutGridLayout.add(buttonShowAll, gbc);
        
        this.panelDetailsButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        this.buttonSeeDetails = new JButton("Ver detalles de compra");
        
        this.panelDetailsButton.add(this.buttonSeeDetails);
        
        this.infoPanel = new JPanel();
        BoxLayout bol = new BoxLayout(this.infoPanel, BoxLayout.X_AXIS);
        this.infoPanel.setLayout(bol);
        this.infoPanel.add(this.customerPanel);
        this.infoPanel.add(this.searchPaneFlowLayoutGridLayout);
        this.infoPanel.add(this.panelDetailsButton);
        
        this.purchasesTableModel = new DefaultTableModel(EMPTY_TABLE, PURCHASES_HEADER);
        
        this.purchasesTable = new JTable(this.purchasesTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        purchasesTable.setDragEnabled(false);
        purchasesTable.getTableHeader().setReorderingAllowed(false);
        purchasesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.purchasesTableScroll = new JScrollPane(this.purchasesTable);
        this.purchasesTableScroll.setBorder(BorderFactory.createTitledBorder("Compras"));
        
        this.dividerPanel = new JPanel();
        BoxLayout dbl = new BoxLayout(this.dividerPanel, BoxLayout.Y_AXIS);
        this.dividerPanel.setLayout(dbl);
        
        this.dividerPanel.add(this.infoPanel);
        this.dividerPanel.add(this.purchasesTableScroll);
        
        this.goBackButton = new JButton("Atrás");
        
        this.topBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        this.topBarPanel.add(this.goBackButton);
        
        this.add(this.dividerPanel, BorderLayout.CENTER);
        this.add(this.topBarPanel, BorderLayout.NORTH);
    }
    
}

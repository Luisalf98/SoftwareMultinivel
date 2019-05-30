/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConfig.SaleState;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Bonus;
import com.loyder.software.model.entities.Category;
import com.loyder.software.model.entities.Percentage;
import com.loyder.software.model.entities.Product;
import com.loyder.software.model.entities.Sale;
import com.loyder.software.model.entities.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Perez
 */
public class SaleInfoView extends JPanel{
    
    private JLabel saleId;
    private JLabel saleDate;
    private JLabel saleTotal;
    private JLabel saleType;
    private JLabel saleState;
    private JLabel saleIdValue;
    private JLabel saleDateValue;
    private JLabel saleTotalValue;
    private JLabel saleTypeValue;
    private JLabel saleStateValue;
    
    private JLabel customerId;
    private JLabel customerUserId;
    private JLabel customerName;
    private JLabel customerCategory;
    private JLabel customerIdValue;
    private JLabel customerUserIdValue;
    private JLabel customerNameValue;
    private JLabel customerCategoryValue;
    
    private JPanel salePanel;
    private JPanel customerPanel;
    private JPanel infoPanel;
    private JPanel headerPanel;
    private JPanel dividerPanel;
    
    private DefaultTableModel bonusesTableModel;
    private JTable bonusesTable;
    private JScrollPane bonusesTableScroll;
    private DefaultTableModel detailsTableModel;
    private JTable detailsTable;
    private JScrollPane detailsTableScroll;
    
    private JPanel topBarPanel;
    private JPanel goBackPanel;
    private JPanel changeSaleStatusPanel;
    private JButton goBackButton;
    private JButton changeSaleStatusButton;
    private JComboBox changeSaleStatusComboBox;
    private DefaultComboBoxModel changeSaleStatusComboBoxModel;
    
    public static final String[] BONUSES_HEADER = {"Código", "Nivel Procentaje", "Porcentaje", "Valor", "Fecha", 
                            "Código Usuario", "Cédula Usuario", "Nombre Usuario"};
    private static final String[] DETAILS_HEADER = {"Código", "Nombre", "Descripción", "Valor Unidad", "Unidades", "Total"};
    private static final String[][] EMPTY_TABLE = {};
    
    private final JPanel panelParent;
    
    private Long dataSaleId;
    
    public SaleInfoView(JPanel panelParent1){
        initComponents();
        this.panelParent = panelParent1;
        
        this.changeSaleStatusComboBox.setSelectedIndex(-1);
        
        
        
        this.changeSaleStatusButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(changeSaleStatus()){
                    setData(dataSaleId);
                }else{
                    JOptionPane.showMessageDialog(null, "No se pudo cambiar el estado de la venta.");
                }
            }
        });
        this.goBackButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout)panelParent.getLayout()).show(panelParent, SalesView.class.getName());
            }
        });
    }
    
    public void setData(Long id){
        this.dataSaleId = id;
        
        this.changeSaleStatusComboBox.setSelectedIndex(-1);
        
        Sale sale = DatabaseConnection.getSaleDao().getSaleById(this.dataSaleId);
        this.saleIdValue.setText(sale.getId().toString());
        this.saleDateValue.setText(ApplicationStarter.formatDate(new Date(sale.getSaleDate())));
        this.saleTotalValue.setText(sale.getTotal().toString());
        this.saleStateValue.setText(sale.getState());
        this.saleTypeValue.setText(sale.getType());
        
        User buyer = DatabaseConnection.getUserDao().getUserById(sale.getBuyerId());
        Category category = DatabaseConnection.getCategoryDao().getCategoryById(buyer.getCategoryId());
        
        this.customerIdValue.setText(buyer.getId().toString());
        this.customerNameValue.setText(buyer.getName()+" "+buyer.getLastName());
        this.customerUserIdValue.setText(buyer.getUserId().toString());
        this.customerCategoryValue.setText(category.getName());
        
        this.bonusesTableModel.setDataVector(EMPTY_TABLE, BONUSES_HEADER);
        this.detailsTableModel.setDataVector(EMPTY_TABLE, DETAILS_HEADER);
        
        ArrayList<Sale.Detail> details = DatabaseConnection.getSaleDao().getSaleDetailsBySaleId(sale.getId());
        details.forEach((detail)->{
            Product p = DatabaseConnection.getProductDao().getProductById(detail.getProductId());
            detailsTableModel.addRow(new Object[]{
                p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), detail.getQuantity(), detail.getQuantity()*p.getPrice()
            });
        });
        
        ArrayList<Bonus> bonuses = DatabaseConnection.getBonusDao().getBonusesBySaleId(this.dataSaleId);
        
        bonuses.forEach(bonus -> {
            Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonus.getPercentageId());
            User u = DatabaseConnection.getUserDao().getUserById(bonus.getUserId());
            bonusesTableModel.addRow(new Object[]{
                bonus.getId(), p.getId(), p.getPercentage(),
                bonus.getBonus(), ApplicationStarter.formatDate(new Date(bonus.getDate())), u.getId(),
                u.getUserId(), u.getName()+" "+u.getLastName()
            });
        });
    }
    
    public boolean changeSaleStatus(){
        if(this.changeSaleStatusComboBox.getSelectedIndex()==-1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar una opción.");
            return false;
        }
        SaleState status = (SaleState)this.changeSaleStatusComboBox.getSelectedItem();
        
        return DatabaseConnection.getSaleDao().updateSaleState(dataSaleId, status);
    }
    
    
    public void initComponents(){
        this.setLayout(new BorderLayout(10, 10));
        
        this.saleId = new JLabel("Código: ");
        this.saleDate = new JLabel("Fecha: ");
        this.saleTotal = new JLabel("Total: ");
        this.saleType = new JLabel("Tipo: ");
        this.saleTypeValue = new JLabel("");
        this.saleState = new JLabel("Estado: ");
        this.saleStateValue = new JLabel("");
        this.saleIdValue = new JLabel("");
        this.saleDateValue = new JLabel("");
        this.saleTotalValue = new JLabel("");
        
        this.salePanel = new JPanel(new GridLayout(5,2));
        this.salePanel.setBorder(BorderFactory.createTitledBorder("Detalles Venta"));
        this.salePanel.add(this.saleId);
        this.salePanel.add(this.saleIdValue);
        this.salePanel.add(this.saleDate);
        this.salePanel.add(this.saleDateValue);
        this.salePanel.add(this.saleType);
        this.salePanel.add(this.saleTypeValue);
        this.salePanel.add(this.saleState);
        this.salePanel.add(this.saleStateValue);
        this.salePanel.add(this.saleTotal);
        this.salePanel.add(this.saleTotalValue);
        
        this.customerId = new JLabel("Código: ");
        this.customerIdValue = new JLabel("");
        this.customerUserId = new JLabel("Cédula: ");
        this.customerUserIdValue = new JLabel("");
        this.customerName = new JLabel("Nombre: ");
        this.customerNameValue = new JLabel("");
        this.customerCategory = new JLabel("Categoría: ");
        this.customerCategoryValue = new JLabel("");
        
        this.customerPanel = new JPanel(new GridLayout(4,2));
        this.customerPanel.setBorder(BorderFactory.createTitledBorder("Cliente"));
        this.customerPanel.add(this.customerId);
        this.customerPanel.add(this.customerIdValue);
        this.customerPanel.add(this.customerUserId);
        this.customerPanel.add(this.customerUserIdValue);
        this.customerPanel.add(this.customerName);
        this.customerPanel.add(this.customerNameValue);
        this.customerPanel.add(this.customerCategory);
        this.customerPanel.add(this.customerCategoryValue);
        
        this.infoPanel = new JPanel();
        BoxLayout bl = new BoxLayout(this.infoPanel, BoxLayout.Y_AXIS);
        this.infoPanel.setLayout(bl);
        this.infoPanel.add(this.salePanel);
        this.infoPanel.add(this.customerPanel);
        
        this.detailsTableModel = new DefaultTableModel(EMPTY_TABLE, DETAILS_HEADER);
        
        this.detailsTable = new JTable(this.detailsTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        detailsTable.setDragEnabled(false);
        detailsTable.getTableHeader().setReorderingAllowed(false);
        detailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.detailsTableScroll = new JScrollPane(this.detailsTable);
        this.detailsTableScroll.setBorder(BorderFactory.createTitledBorder("Productos"));
        
        this.headerPanel = new JPanel();
        BoxLayout hbl = new BoxLayout(this.headerPanel, BoxLayout.X_AXIS);
        this.headerPanel.setLayout(hbl);
        
        this.headerPanel.add(this.infoPanel);
        this.headerPanel.add(this.detailsTableScroll);
        
        this.dividerPanel = new JPanel();
        BoxLayout dbl = new BoxLayout(this.dividerPanel, BoxLayout.Y_AXIS);
        this.dividerPanel.setLayout(dbl);
        
        this.bonusesTableModel = new DefaultTableModel(EMPTY_TABLE, BONUSES_HEADER);
        
        this.bonusesTable = new JTable(this.bonusesTableModel){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        bonusesTable.setDragEnabled(false);
        bonusesTable.getTableHeader().setReorderingAllowed(false);
        bonusesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        this.bonusesTableScroll = new JScrollPane(this.bonusesTable);
        this.bonusesTableScroll.setBorder(BorderFactory.createTitledBorder("Bonificaciones"));
        
        this.dividerPanel.add(this.headerPanel);
        this.dividerPanel.add(this.bonusesTableScroll);
        
        this.goBackButton = new JButton("Atrás");
        
        this.topBarPanel = new JPanel(new GridLayout(1,2));
        this.goBackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.changeSaleStatusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        //this.changeSaleStatusPanel.setBorder(BorderFactory.createTitledBorder("Cambiar Estado de Venta"));
        
        this.goBackPanel.add(this.goBackButton);
        
        this.topBarPanel.add(this.goBackPanel);
        this.topBarPanel.add(this.changeSaleStatusPanel);
        
        this.changeSaleStatusComboBoxModel = new DefaultComboBoxModel(new Object[]{SaleState.PAGADA, SaleState.NO_PAGADA});
        this.changeSaleStatusComboBox = new JComboBox(changeSaleStatusComboBoxModel);
        this.changeSaleStatusButton = new JButton("Cambiar Estado de Venta");
        
        this.changeSaleStatusPanel.add(this.changeSaleStatusComboBox);
        this.changeSaleStatusPanel.add(this.changeSaleStatusButton);
        
        this.add(this.dividerPanel, BorderLayout.CENTER);
        this.add(this.topBarPanel, BorderLayout.NORTH);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Income;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class IncomesView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JPanel searchPane;
    private JPanel searchPaneFlowLayout;
    private JPanel searchPaneFlowLayoutGridLayout;
    private JPanel footer;
    private JPanel footerBoxLayout;
    private JPanel footerFlowLayoutSales;
    private JPanel footerFlowLayoutBonuses;
    private JPanel footerFlowLayoutIncomes;
    private JLabel idSearchLabel;
    private JLabel dateSearchLabel;
    private JButton dateSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAll;
    private JFormattedTextField idTextField;
    private JFormattedTextField date1TextField;
    private JFormattedTextField date2TextField;
    private JPanel datesPane;

    private JLabel totalIncomesLabel;
    private JLabel totalBonusesLabel;
    private JLabel totalSalesLabel;
    private JLabel totalIncomes;
    private JLabel totalBonuses;
    private JLabel totalSales;
    
    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Código", "Código Venta", "Total Venta", "Total Bonificación",
            "Total Ingreso"};
    private static final String[][] EMPTY_TABLE = new String[0][0];

    public IncomesView() throws ParseException {
        initComponents();
        dateSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                incomeSearchByDateRange(e);
            }
        });
        buttonShowAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllIncomes(e);
            }
        });
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                incomeSearchById(e);
            }
        });
    }

    public void incomeSearchById(MouseEvent e) {
        Long id = (Long) idTextField.getValue();
        if(id == null){
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }
        
        idTextField.setText("");
        idTextField.setValue(null);
        
        Income income = DatabaseConnection.getIncomeDao().getIncomeById(id);
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);

        if (income != null) {
            tableModel.addRow(new Object[]{income.getId(), income.getSaleId(),
                ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalSale()),
                ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalBonus()),
                ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalIncome())});
            
            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalBonus()));
            totalIncomes.setText(ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalIncome()));
            totalSales.setText(ApplicationStarter.CURRENCY_FORMAT.format(income.getTotalSale()));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro el ingreso especificado.");
            totalBonuses.setText("");
            totalIncomes.setText("");
            totalSales.setText("");
        }

    }

    public void incomeSearchByDateRange(MouseEvent e) {
        Date date1 = ((Date) date1TextField.getValue());
        Date date2 = ((Date) date2TextField.getValue());
        if(date1 == null || date2 == null){
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.");
            return;
        }
        
        date1TextField.setValue(null);
        date2TextField.setValue(null);
        date1TextField.setText("");
        date2TextField.setText("");
        
        Double totalSalesSum = 0d;
        Double totalBonusesSum = 0d;
        Double totalIncomesSum = 0d;
        ArrayList<Income> incomes = DatabaseConnection.getIncomeDao().getIncomesInDateRange(date1, date2);
        Object[][] data = new Object[incomes.size()][5];
        if (incomes != null && !incomes.isEmpty()) {
            for (int i = 0; i < incomes.size(); i++) {
                data[i][0] = incomes.get(i).getId();
                data[i][1] = incomes.get(i).getSaleId();
                data[i][2] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalSale());
                data[i][3] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalBonus());
                data[i][4] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalIncome());
                
                totalBonusesSum+=incomes.get(i).getTotalBonus();
                totalIncomesSum+=incomes.get(i).getTotalIncome();
                totalSalesSum+=incomes.get(i).getTotalSale();
            }
            
            totalIncomes.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalIncomesSum));
            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
            totalSales.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalSalesSum));
        }else{
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            totalIncomes.setText("");
            totalBonuses.setText("");
            totalSales.setText("");
        }
        tableModel.setDataVector(data, TABLE_MODEL_IDENTIFIERS);

    }
    
    public void showAllIncomes(MouseEvent e) {
        
        Double totalSalesSum = 0d;
        Double totalBonusesSum = 0d;
        Double totalIncomesSum = 0d;
        ArrayList<Income> incomes = DatabaseConnection.getIncomeDao().getAllIncomes();
        Object[][] data = new Object[incomes.size()][5];
        if (incomes != null && !incomes.isEmpty()) {
            for (int i = 0; i < incomes.size(); i++) {
                data[i][0] = incomes.get(i).getId();
                data[i][1] = incomes.get(i).getSaleId();
                data[i][2] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalSale());
                data[i][3] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalBonus());
                data[i][4] = ApplicationStarter.CURRENCY_FORMAT.format(incomes.get(i).getTotalIncome());
                
                totalBonusesSum+=incomes.get(i).getTotalBonus();
                totalIncomesSum+=incomes.get(i).getTotalIncome();
                totalSalesSum+=incomes.get(i).getTotalSale();
            }
            
            totalIncomes.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalIncomesSum));
            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
            totalSales.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalSalesSum));
        }else{
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            totalIncomes.setText("");
            totalBonuses.setText("");
            totalSales.setText("");
        }
        tableModel.setDataVector(data, TABLE_MODEL_IDENTIFIERS);

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
        EmptyBorder eb = new EmptyBorder(new Insets(20, 20, 20, 20));
        searchPane = new JPanel(new BorderLayout());
        searchPane.setBorder(eb);
        footer = new JPanel(new BorderLayout());
        footer.setBorder(eb);
        
        Dimension preferredSize = new Dimension(250, 30);
        idSearchLabel = new JLabel("Código del Ingreso: ");
        idTextField = new JFormattedTextField(0L);
        idTextField.setValue(null);
        idTextField.setPreferredSize(preferredSize);
        idSearchButton = new JButton("Buscar");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date1TextField = new JFormattedTextField(sdf);
        date2TextField = new JFormattedTextField(sdf);
        dateSearchButton = new JButton("Buscar");
        dateSearchLabel = new JLabel("Fecha Inicial (dd/mm/yyyy) - Fecha Final (dd/mm/yyyy): ");
        
        buttonShowAll = new JButton("Mostrar todo");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPaneFlowLayoutGridLayout = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        
        gbc.anchor = GridBagConstraints.EAST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idSearchLabel,gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idTextField,gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        searchPaneFlowLayoutGridLayout.add(idSearchButton,gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(dateSearchLabel,gbc);
        datesPane = new JPanel();
        BoxLayout bl = new BoxLayout(datesPane, BoxLayout.X_AXIS);
        datesPane.setLayout(bl);
        datesPane.add(date1TextField);
        datesPane.add(date2TextField);
        datesPane.setPreferredSize(preferredSize);
        gbc.gridx = 1;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(datesPane,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        searchPaneFlowLayoutGridLayout.add(dateSearchButton,gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        searchPaneFlowLayoutGridLayout.add(buttonShowAll, gbc);
        
        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout);

        totalBonuses = new JLabel();
        totalBonusesLabel = new JLabel("Total Bonificaciones: ");
        totalIncomes = new JLabel();
        totalIncomesLabel = new JLabel("Total Ingresos: ");
        totalSales = new JLabel();
        totalSalesLabel = new JLabel("Total Ventas: ");

        footerBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(footerBoxLayout, BoxLayout.X_AXIS);
        footerBoxLayout.setLayout(b);
        footerFlowLayoutBonuses = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerFlowLayoutSales = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerFlowLayoutIncomes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        footerFlowLayoutSales.add(totalSalesLabel);
        footerFlowLayoutSales.add(totalSales);

        footerFlowLayoutIncomes.add(totalIncomesLabel);
        footerFlowLayoutIncomes.add(totalIncomes);

        footerFlowLayoutBonuses.add(totalBonusesLabel);
        footerFlowLayoutBonuses.add(totalBonuses);

        footerBoxLayout.add(footerFlowLayoutSales);
        footerBoxLayout.add(footerFlowLayoutBonuses);
        footerBoxLayout.add(footerFlowLayoutIncomes);

        footer.add(footerBoxLayout);

        this.add(tableScroll, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);
        this.add(footer, BorderLayout.SOUTH);

    }
}

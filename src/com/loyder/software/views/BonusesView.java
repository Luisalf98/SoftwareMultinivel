/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Bonus;
import com.loyder.software.model.entities.Percentage;
import com.loyder.software.model.entities.User;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class BonusesView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JPanel searchPane;
    private JPanel searchPaneFlowLayout;
    private JPanel searchPaneFlowLayoutGridLayout;
    private JPanel footer;
    private JPanel footerBoxLayout;
    private JPanel footerFlowLayoutBonuses;
    private JLabel idSearchLabel;
    private JLabel dateSearchLabel;
    private JButton dateSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAll;
    private JFormattedTextField idTextField;
    private JFormattedTextField date1TextField;
    private JFormattedTextField date2TextField;
    private JPanel datesPane;

    private JLabel totalBonusesLabel;
    private JLabel totalBonuses;

    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Código", "Código Venta", "Código Usuario", "Nombre Usuario", "Nivel Porcentaje", "Procentaje", "Fecha",
        "Valor Bonificación"};
    private static final String[][] EMPTY_TABLE = new String[0][0];

    public BonusesView() throws ParseException {
        initComponents();
        dateSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bonusSearchByDateRange(e);
            }
        });
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                bonusSearchById(e);
            }
        });
        this.buttonShowAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllBonuses(e);
            }
        });
        
    }

    public void bonusSearchById(MouseEvent e) {
        Long id = (Long) idTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Bonus bonus = DatabaseConnection.getBonusDao().getBonusById(id);
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);

        if (bonus != null) {
            User user = DatabaseConnection.getUserDao().getUserById(bonus.getUserId());
            Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonus.getPercentageId());
            tableModel.addRow(new Object[]{bonus.getId(), bonus.getSaleId(), bonus.getUserId(),
                user.getName() + " " + user.getLastName(),
                p.getId(), ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage()),
                ApplicationStarter.formatDate(new Date(bonus.getDate())), 
                ApplicationStarter.CURRENCY_FORMAT.format(bonus.getBonus())});
            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(bonus.getBonus()));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro la bonificación especificada.");
            totalBonuses.setText("");
        }

    }

    public void bonusSearchByDateRange(MouseEvent e) {
        Date date1 = ((Date) date1TextField.getValue());
        Date date2 = ((Date) date2TextField.getValue());
        if (date1 == null || date2 == null) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.");
            return;
        }

        date1TextField.setValue(null);
        date2TextField.setValue(null);
        date1TextField.setText("");
        date2TextField.setText("");

        Double totalBonusesSum = 0d;
        ArrayList<Bonus> bonuses = DatabaseConnection.getBonusDao().getBonusesInDateRange(date1, date2);
        Object[][] data = new Object[bonuses.size()][8];
        if (bonuses != null && !bonuses.isEmpty()) {
            for (int i = 0; i < bonuses.size(); i++) {
                User user = DatabaseConnection.getUserDao().getUserById(bonuses.get(i).getUserId());
                Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonuses.get(i).getPercentageId());
            
                data[i][0] = bonuses.get(i).getId();
                data[i][1] = bonuses.get(i).getSaleId();
                data[i][2] = user.getId();
                data[i][3] = user.getName()+" "+user.getLastName();
                data[i][4] = p.getId();
                data[i][5] = ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage());
                data[i][6] = ApplicationStarter.formatDate(new Date(bonuses.get(i).getDate()));
                data[i][7] = ApplicationStarter.CURRENCY_FORMAT.format(bonuses.get(i).getBonus());
                
                totalBonusesSum += bonuses.get(i).getBonus();
                
            }

            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            totalBonuses.setText("");
        }
        tableModel.setDataVector(data, TABLE_MODEL_IDENTIFIERS);

    }
    
    public void showAllBonuses(MouseEvent e) {

        Double totalBonusesSum = 0d;
        ArrayList<Bonus> bonuses = DatabaseConnection.getBonusDao().getAllBonuses();
        Object[][] data = new Object[bonuses.size()][8];
        if (bonuses != null && !bonuses.isEmpty()) {
            for (int i = 0; i < bonuses.size(); i++) {
                User user = DatabaseConnection.getUserDao().getUserById(bonuses.get(i).getUserId());
                Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonuses.get(i).getPercentageId());
                
                data[i][0] = bonuses.get(i).getId();
                data[i][1] = bonuses.get(i).getSaleId();
                data[i][2] = user.getId();
                data[i][3] = user.getName()+" "+user.getLastName();
                data[i][4] = p.getId();
                data[i][5] = ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage());
                data[i][6] = ApplicationStarter.formatDate(new Date(bonuses.get(i).getDate()));
                data[i][7] = ApplicationStarter.CURRENCY_FORMAT.format(bonuses.get(i).getBonus());
                
                totalBonusesSum += bonuses.get(i).getBonus();
                
            }

            totalBonuses.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            totalBonuses.setText("");
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

        idSearchLabel = new JLabel("Código de la bonificación: ");
        idTextField = new JFormattedTextField(0L);
        idTextField.setValue(null);
        idSearchButton = new JButton("Buscar");

//        DateFormatter dateFormatter = new DateFormatter();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date1TextField = new JFormattedTextField(sdf);
        date2TextField = new JFormattedTextField(sdf);
        dateSearchButton = new JButton("Buscar");
        dateSearchLabel = new JLabel("Fecha Inicial (dd/mm/yyyy) - Fecha Final (dd/mm/yyyy): ");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPaneFlowLayoutGridLayout = new JPanel(new GridLayout(3, 3, 10, 10));

        
        searchPaneFlowLayoutGridLayout.add(idSearchLabel);
        searchPaneFlowLayoutGridLayout.add(idTextField);
        searchPaneFlowLayoutGridLayout.add(idSearchButton);

        searchPaneFlowLayoutGridLayout.add(dateSearchLabel);
        datesPane = new JPanel();
        BoxLayout bl = new BoxLayout(datesPane, BoxLayout.X_AXIS);
        datesPane.setLayout(bl);
        datesPane.add(date1TextField);
        datesPane.add(date2TextField);
        
        searchPaneFlowLayoutGridLayout.add(datesPane);
        searchPaneFlowLayoutGridLayout.add(dateSearchButton);
        
        this.buttonShowAll = new JButton("Mostrar todo");
        
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        searchPaneFlowLayoutGridLayout.add(this.buttonShowAll);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());

        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout);

        totalBonuses = new JLabel();
        totalBonusesLabel = new JLabel("Total Bonificaciones: ");

        footerBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(footerBoxLayout, BoxLayout.X_AXIS);
        footerBoxLayout.setLayout(b);
        footerFlowLayoutBonuses = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        footerFlowLayoutBonuses.add(totalBonusesLabel);
        footerFlowLayoutBonuses.add(totalBonuses);

        footerBoxLayout.add(footerFlowLayoutBonuses);

        footer.add(footerBoxLayout);

        this.add(tableScroll, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);
        this.add(footer, BorderLayout.SOUTH);

    }
}

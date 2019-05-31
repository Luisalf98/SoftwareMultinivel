/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Bonus;
import com.loyder.software.model.entities.Category;
import com.loyder.software.model.entities.Percentage;
import com.loyder.software.model.entities.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Luis Perez
 */
public class UserInfoView extends JPanel {

    private JPanel recommendersInfoPanel;
    private JPanel backArrowPanel;
    private JPanel headerPanel;
    private JPanel recommenderFlowLayout;
    private JPanel recommenderGridLayout;
    private JPanel recommenderAdderFlowLayout;
    private JPanel recommenderAdderGridLayout;
    private JPanel tablesPanel;
    private JPanel tablesPanelBoxLayout;
    private JScrollPane addedTableScroll;
    private JScrollPane bonusesTableScroll;

    //Recommender Info
    private JLabel recommenderId;
    private JLabel recommenderUserId;
    private JLabel recommenderName;
    private JLabel recommenderLastName;
    private JLabel recommenderAddress;
    private JLabel recommenderTel;
    private JLabel recommenderCel;
    private JLabel recommenderCategory;
    private JLabel recommenderEntranceDate;
    private JLabel recommenderIdValue;
    private JFormattedTextField recommenderUserIdValue;
    private JTextField recommenderNameValue;
    private JTextField recommenderLastNameValue;
    private JTextField recommenderAddressValue;
    private JTextField recommenderTelValue;
    private JTextField recommenderCelValue;
    private JComboBox<Category> recommenderCategoryValue;
    private DefaultComboBoxModel<Category> categoriesModel;
    private JLabel recommenderEntranceDateValue;

    private JButton buttonSave;
    private JToggleButton buttonUpdate;
    private JPanel panelManageUser;

    //Recommender Adder Info
    private JLabel recommenderAdderId;
    private JLabel recommenderAdderUserId;
    private JLabel recommenderAdderName;
    private JLabel recommenderAdderAddress;
    private JLabel recommenderAdderTel;
    private JLabel recommenderAdderCel;
    private JLabel recommenderAdderIdValue;
    private JLabel recommenderAdderUserIdValue;
    private JLabel recommenderAdderNameValue;
    private JLabel recommenderAdderAddressValue;
    private JLabel recommenderAdderTelValue;
    private JLabel recommenderAdderCelValue;

    //Tables
    private DefaultTableModel addedTableModel;
    private DefaultTableModel bonusesTableModel;
    private JTable addedTable;
    private JTable bonusesTable;

    private JButton goBack;

    private JPanel panelRecommendersAdded;
    private JPanel panelRecommendersAddedForm;
    private JScrollPane panelRecommendersAddedScrollPane;
    private JPanel panelBonusesGotten;
    private JPanel panelBonusesGottenForm;
    private JScrollPane panelBonusesGottenScrollPane;

    //Added Recommenders Form
    private JLabel idSearchRecommendersLabel;
    private JLabel userIdSearchRecommendersLabel;
    private JLabel nameSearchRecommendersLabel;

    private JFormattedTextField idRecommendersTextField;
    private JFormattedTextField userIdRecommendersTextField;
    private JTextField nameRecommendersTextField;

    private JButton userIdSearchUserButton;
    private JButton nameSearchUsersButton;
    private JButton idSearchUserButton;

    private JButton buttonShowAllAddedUsers;

    //Gotten Bonuses Form
    private JLabel idSearchBonusesLabel;
    private JLabel dateSearchBonusesLabel;
    private JButton dateSearchBonusesButton;
    private JButton idSearchBonusesButton;
    private JButton buttonShowAllBonuses;
    private JFormattedTextField idBonusesTextField;
    private JFormattedTextField date1BonusesTextField;
    private JFormattedTextField date2BonusesTextField;

    private JLabel labelTotalBonuses;
    private JLabel labelTotalBonusesValue;
    private JPanel panelTotalBonuses;
    private JPanel panelBonusesContainer;

    private static final String[] ADDED_MODEL = {"Código", "Cédula", "Nombre", "Categoría", "Fecha de Ingreso"};
    private static final String[] BONUSES_MODEL = {"Código", "Nivel Porcentaje", "Porcentaje", "Bonificación",
        "Código venta", "Fecha"};
    private static final String[][] EMPTY_TABLE = {};

    private final JPanel panelParent;

    private Long dataRecommenderId;

    public UserInfoView(JPanel panelParent1) {
        initComponents();
        this.panelParent = panelParent1;

        this.goBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((CardLayout) panelParent.getLayout()).show(panelParent, UsersView.class.getName());
            }
        });

        //recommenders
        this.buttonShowAllAddedUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllAddedUsers(e);
            }
        });
        this.idSearchUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAddedUserById(e);
            }
        });
        this.userIdSearchUserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAddedRecommendersByUserId(e);
            }
        });
        this.nameSearchUsersButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAddedRecommendersByName(e);
            }
        });

        //bonuses
        this.buttonShowAllBonuses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllBonuses(e);
            }
        });
        this.idSearchBonusesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBonusById(e);
            }
        });
        this.dateSearchBonusesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBonusesInDateRange(e);
            }
        });
        this.buttonUpdate.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (buttonUpdate.isSelected()) {
                    buttonUpdate.setText("Modo Lectura");
                    recommenderUserIdValue.setEnabled(true);
                    recommenderNameValue.setEnabled(true);
                    recommenderLastNameValue.setEnabled(true);
                    recommenderAddressValue.setEnabled(true);
                    recommenderTelValue.setEnabled(true);
                    recommenderCelValue.setEnabled(true);
                    recommenderCategoryValue.setEnabled(true);
                    buttonSave.setEnabled(true);
                } else {
                    buttonUpdate.setText("Modo Edición");
                    recommenderUserIdValue.setEnabled(false);
                    recommenderNameValue.setEnabled(false);
                    recommenderLastNameValue.setEnabled(false);
                    recommenderAddressValue.setEnabled(false);
                    recommenderTelValue.setEnabled(false);
                    recommenderCelValue.setEnabled(false);
                    recommenderCategoryValue.setEnabled(false);
                    buttonSave.setEnabled(false);
                }
            }

        });
        this.buttonSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (buttonSave.isEnabled()) {
                    if (updateUser(e)) {
                        JOptionPane.showMessageDialog(null, "Operación exitosa!");
                        setData(dataRecommenderId);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar los datos del usuario.");
                    }
                }
            }
        });

    }

    private boolean updateUser(MouseEvent e) {
        Long userId = (Long) recommenderUserIdValue.getValue();
        if (userId == null) {
            JOptionPane.showMessageDialog(null, "Campo cédula incorrecto o vacío");
            return false;
        }
        String name = recommenderNameValue.getText();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo nombre vacío");
            return false;
        }
        String lastName = recommenderLastNameValue.getText();
        if (lastName.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo apellido vacío");
            return false;
        }
        String address = recommenderAddressValue.getText();
        if (address.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo dirección vacío");
            return false;
        }
        String tel = recommenderTelValue.getText();
        if (tel.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo teléfono vacío");
            return false;
        }
        String cel = recommenderCelValue.getText();
        if (cel.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo celular vacío");
            return false;
        }
        Long categoryId;
        if (recommenderCategoryValue.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "No seleccionó categoría.");
            return false;
        }

        Category c = (Category) recommenderCategoryValue.getSelectedItem();
        categoryId = c.getId();

        return DatabaseConnection.getUserDao().updateUser(new User(dataRecommenderId, userId, name, lastName, address, tel,
                cel, null, null, categoryId));
    }

    public void showBonusById(MouseEvent e) {
        Long id = (Long) idBonusesTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idBonusesTextField.setText("");
        idBonusesTextField.setValue(null);

        Bonus bonus = DatabaseConnection.getBonusDao().getBonusByIdAndUserId(id, dataRecommenderId);
        bonusesTableModel.setDataVector(EMPTY_TABLE, BONUSES_MODEL);

        if (bonus != null) {
            Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonus.getPercentageId());
            bonusesTableModel.addRow(new Object[]{bonus.getId(), p.getId(),
                ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage()),
                ApplicationStarter.CURRENCY_FORMAT.format(bonus.getBonus()),
                bonus.getSaleId(),
                ApplicationStarter.formatDate(new Date(bonus.getDate()))});
            labelTotalBonusesValue.setText(ApplicationStarter.CURRENCY_FORMAT.format(bonus.getBonus()));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro la bonificación especificada.");
            labelTotalBonusesValue.setText("");
        }
    }

    public void showBonusesInDateRange(MouseEvent e) {
        Date date1 = ((Date) date1BonusesTextField.getValue());
        Date date2 = ((Date) date2BonusesTextField.getValue());
        if (date1 == null || date2 == null) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.");
            return;
        }

        date1BonusesTextField.setValue(null);
        date2BonusesTextField.setValue(null);
        date1BonusesTextField.setText("");
        date2BonusesTextField.setText("");

        Double totalBonusesSum = 0d;
        ArrayList<Bonus> bonuses = DatabaseConnection.getBonusDao().getBonusesInDateRangeByUserId(date1, date2, dataRecommenderId);
        Object[][] data = new Object[bonuses.size()][7];
        if (bonuses != null && !bonuses.isEmpty()) {
            for (int i = 0; i < bonuses.size(); i++) {
                Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonuses.get(i).getPercentageId());
                data[i][0] = bonuses.get(i).getId();
                data[i][1] = p.getId();
                data[i][2] = ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage());
                data[i][3] = ApplicationStarter.CURRENCY_FORMAT.format(bonuses.get(i).getBonus());
                data[i][4] = bonuses.get(i).getSaleId();
                data[i][5] = ApplicationStarter.formatDate(new Date(bonuses.get(i).getDate()));

                totalBonusesSum += bonuses.get(i).getBonus();

            }

            labelTotalBonusesValue.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            labelTotalBonusesValue.setText("");
        }
        bonusesTableModel.setDataVector(data, BONUSES_MODEL);
    }

    public void showAllBonuses(MouseEvent e) {

        Double totalBonusesSum = 0d;
        ArrayList<Bonus> bonuses = DatabaseConnection.getBonusDao().getBonusesByUserId(dataRecommenderId);
        Object[][] data = new Object[bonuses.size()][7];
        if (bonuses != null && !bonuses.isEmpty()) {
            for (int i = 0; i < bonuses.size(); i++) {
                Percentage p = DatabaseConnection.getPercentageDao().getPercentageById(bonuses.get(i).getPercentageId());
                data[i][0] = bonuses.get(i).getId();
                data[i][1] = p.getId();
                data[i][2] = ApplicationStarter.PERCENTAGE_FORMAT.format(p.getPercentage());
                data[i][3] = ApplicationStarter.CURRENCY_FORMAT.format(bonuses.get(i).getBonus());
                data[i][4] = bonuses.get(i).getSaleId();
                data[i][5] = ApplicationStarter.formatDate(new Date(bonuses.get(i).getDate()));

                totalBonusesSum += bonuses.get(i).getBonus();

            }

            labelTotalBonusesValue.setText(ApplicationStarter.CURRENCY_FORMAT.format(totalBonusesSum));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
            labelTotalBonusesValue.setText("");
        }
        bonusesTableModel.setDataVector(data, BONUSES_MODEL);
    }

    public void showAllAddedUsers(MouseEvent e) {
        ArrayList<User> users = DatabaseConnection.getUserDao().getUserChildsById(dataRecommenderId);

        Object[][] data = new Object[users.size()][7];
        if (users != null && !users.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                data[i][0] = users.get(i).getId();
                data[i][1] = users.get(i).getUserId();
                data[i][2] = users.get(i).getName() + " " + users.get(i).getLastName();
                data[i][3] = users.get(i).getCategoryId();
                data[i][4] = ApplicationStarter.formatDate(new Date(users.get(i).getEntranceDate()));

            }

        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
        addedTableModel.setDataVector(data, ADDED_MODEL);
    }

    public void showAddedUserById(MouseEvent e) {
        Long id = (Long) this.idRecommendersTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(null, "Código incorrecto");
            return;
        }

        idRecommendersTextField.setValue(null);
        idRecommendersTextField.setText("");

        addedTableModel.setDataVector(EMPTY_TABLE, ADDED_MODEL);

        User user = DatabaseConnection.getUserDao().getUserChildByIdAndChildId(dataRecommenderId, id);

        if (user != null) {
            addedTableModel.addRow(new Object[]{
                user.getId(),
                user.getUserId(),
                user.getName() + " " + user.getLastName(),
                user.getCategoryId(),
                ApplicationStarter.formatDate(new Date(user.getEntranceDate())),});

        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void showAddedRecommendersByUserId(MouseEvent e) {
        Long id = (Long) this.userIdRecommendersTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(null, "Código incorrecto");
            return;
        }

        userIdRecommendersTextField.setValue(null);
        userIdRecommendersTextField.setText("");

        addedTableModel.setDataVector(EMPTY_TABLE, ADDED_MODEL);

        User user = DatabaseConnection.getUserDao().getUserChildByIdAndChildUserId(dataRecommenderId, id);

        if (user != null) {
            addedTableModel.addRow(new Object[]{
                user.getId(),
                user.getUserId(),
                user.getName() + " " + user.getLastName(),
                user.getCategoryId(),
                ApplicationStarter.formatDate(new Date(user.getEntranceDate())),});

        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void showAddedRecommendersByName(MouseEvent e) {
        String name = nameRecommendersTextField.getText();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Campo Nombre vacío!");
            return;
        }

        nameRecommendersTextField.setText("");

        ArrayList<User> users = DatabaseConnection.getUserDao().getUserChildsByIdAndChildName(dataRecommenderId, name);

        Object[][] data = new Object[users.size()][7];
        if (users != null && !users.isEmpty()) {
            for (int i = 0; i < users.size(); i++) {
                data[i][0] = users.get(i).getId();
                data[i][1] = users.get(i).getUserId();
                data[i][2] = users.get(i).getName() + " " + users.get(i).getLastName();
                data[i][3] = users.get(i).getCategoryId();
                data[i][4] = ApplicationStarter.formatDate(new Date(users.get(i).getEntranceDate()));

            }

        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
        addedTableModel.setDataVector(data, ADDED_MODEL);
    }

    public void setData(Long id) {
        this.dataRecommenderId = id;

        User user = DatabaseConnection.getUserDao().getUserById(id);
        User parent;
        if (user.getAdderId() == -1) {
            parent = ApplicationStarter.COMPANY_ROOT;
        } else {
            parent = DatabaseConnection.getUserDao().getUserById(user.getAdderId());
        }

        recommenderIdValue.setText(user.getId().toString());
        recommenderUserIdValue.setValue(user.getUserId());
        recommenderNameValue.setText(user.getName());
        recommenderLastNameValue.setText(user.getLastName());
        recommenderAddressValue.setText(user.getAddress());
        recommenderTelValue.setText(user.getTel());
        recommenderCelValue.setText(user.getCel());
        ArrayList<Category> categories = DatabaseConnection.getCategoryDao().getAllCategories();
        this.categoriesModel.removeAllElements();
        categories.forEach(c -> {
            categoriesModel.addElement(c);
        });
        recommenderCategoryValue.setSelectedItem(new Category(user.getCategoryId(), ""));
        recommenderEntranceDateValue.setText(ApplicationStarter.formatDate(new Date(user.getEntranceDate())));

        recommenderUserIdValue.setEnabled(false);
        recommenderNameValue.setEnabled(false);
        recommenderLastNameValue.setEnabled(false);
        recommenderAddressValue.setEnabled(false);
        recommenderTelValue.setEnabled(false);
        recommenderCelValue.setEnabled(false);
        recommenderCategoryValue.setEnabled(false);
        buttonSave.setEnabled(false);

        buttonUpdate.setSelected(false);
        buttonUpdate.setText("Modo Edición");

        recommenderAdderIdValue.setText(parent.getId().toString());
        recommenderAdderUserIdValue.setText(parent.getUserId().toString());
        recommenderAdderNameValue.setText(parent.getName() + " " + parent.getLastName());
        recommenderAdderAddressValue.setText(parent.getAddress());
        recommenderAdderTelValue.setText(parent.getTel());
        recommenderAdderCelValue.setText(parent.getCel());

        showAllBonuses(null);
        showAllAddedUsers(null);
    }

    public void initComponents() {
        this.setLayout(new BorderLayout(10, 10));

        //Recommenders panel
        this.recommendersInfoPanel = new JPanel();
        BoxLayout b1 = new BoxLayout(this.recommendersInfoPanel, BoxLayout.X_AXIS);
        this.recommendersInfoPanel.setLayout(b1);
        //Tables panel
        this.tablesPanel = new JPanel();
        BoxLayout b2 = new BoxLayout(this.tablesPanel, BoxLayout.X_AXIS);
        this.tablesPanel.setLayout(b2);

        //Recommender Info
        this.recommenderId = new JLabel("Código: ");
        this.recommenderUserId = new JLabel("Cédula: ");
        this.recommenderName = new JLabel("Nombre: ");
        this.recommenderLastName = new JLabel("Nombre: ");
        this.recommenderAddress = new JLabel("Dirección: ");
        this.recommenderTel = new JLabel("Teléfono: ");
        this.recommenderCel = new JLabel("Celular: ");
        this.recommenderCategory = new JLabel("Categoría: ");
        this.recommenderEntranceDate = new JLabel("Fecha de ingreso: ");
        this.recommenderIdValue = new JLabel("");
        this.recommenderUserIdValue = new JFormattedTextField(0L);
        this.recommenderNameValue = new JTextField("");
        this.recommenderLastNameValue = new JTextField("");
        this.recommenderAddressValue = new JTextField("");
        this.recommenderTelValue = new JTextField("");
        this.recommenderCelValue = new JTextField("");
        this.recommenderCategoryValue = new JComboBox<>();
        this.categoriesModel = new DefaultComboBoxModel<>();
        this.recommenderCategoryValue.setModel(categoriesModel);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.recommenderEntranceDateValue = new JLabel("");

        this.recommenderGridLayout = new JPanel(new GridBagLayout());
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.EAST;
        gbc1.insets = new Insets(5, 5, 5, 5);

        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderId, gbc1);
        gbc1.gridx = 1;
        gbc1.gridy = 0;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderIdValue, gbc1);
        gbc1.gridx = 2;
        gbc1.gridy = 0;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderUserId, gbc1);
        gbc1.gridx = 3;
        gbc1.gridy = 0;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderUserIdValue, gbc1);
        gbc1.gridx = 0;
        gbc1.gridy = 1;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderName, gbc1);
        gbc1.gridx = 1;
        gbc1.gridy = 1;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderNameValue, gbc1);
        gbc1.gridx = 2;
        gbc1.gridy = 1;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderLastName, gbc1);
        gbc1.gridx = 3;
        gbc1.gridy = 1;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderLastNameValue, gbc1);
        gbc1.gridx = 0;
        gbc1.gridy = 2;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderAddress, gbc1);
        gbc1.gridx = 1;
        gbc1.gridy = 2;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderAddressValue, gbc1);
        gbc1.gridx = 2;
        gbc1.gridy = 2;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderTel, gbc1);
        gbc1.gridx = 3;
        gbc1.gridy = 2;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderTelValue, gbc1);
        gbc1.gridx = 0;
        gbc1.gridy = 3;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderCel, gbc1);
        gbc1.gridx = 1;
        gbc1.gridy = 3;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderCelValue, gbc1);
        gbc1.gridx = 2;
        gbc1.gridy = 3;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderCategory, gbc1);
        gbc1.gridx = 3;
        gbc1.gridy = 3;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderCategoryValue, gbc1);
        gbc1.gridx = 0;
        gbc1.gridy = 4;
        gbc1.fill = GridBagConstraints.NONE;
        this.recommenderGridLayout.add(this.recommenderEntranceDate, gbc1);
        gbc1.gridx = 1;
        gbc1.gridy = 4;
        gbc1.fill = GridBagConstraints.BOTH;
        this.recommenderGridLayout.add(this.recommenderEntranceDateValue, gbc1);

        this.recommenderFlowLayout = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.recommenderFlowLayout.setBorder(BorderFactory.createTitledBorder("Usuario"));

        this.panelManageUser = new JPanel(new GridLayout(2, 1, 5, 5));

        this.buttonUpdate = new JToggleButton("Modo Edición", false);

        this.buttonSave = new JButton("Guardar");

        panelManageUser.add(buttonUpdate);
        panelManageUser.add(buttonSave);

        this.recommenderFlowLayout.add(this.recommenderGridLayout);
        this.recommenderFlowLayout.add(this.panelManageUser);

        //Adder info
        this.recommenderAdderId = new JLabel("Código: ");
        this.recommenderAdderUserId = new JLabel("Cédula: ");
        this.recommenderAdderName = new JLabel("Nombre: ");
        this.recommenderAdderAddress = new JLabel("Dirección: ");
        this.recommenderAdderTel = new JLabel("Teléfono: ");
        this.recommenderAdderCel = new JLabel("Celular: ");
        this.recommenderAdderIdValue = new JLabel("");
        this.recommenderAdderUserIdValue = new JLabel("");
        this.recommenderAdderNameValue = new JLabel("");
        this.recommenderAdderAddressValue = new JLabel("");
        this.recommenderAdderTelValue = new JLabel("");
        this.recommenderAdderCelValue = new JLabel("");

        this.recommenderAdderGridLayout = new JPanel(new GridLayout(6, 2, 5, 5));

        this.recommenderAdderGridLayout.add(this.recommenderAdderId);
        this.recommenderAdderGridLayout.add(this.recommenderAdderIdValue);
        this.recommenderAdderGridLayout.add(this.recommenderAdderUserId);
        this.recommenderAdderGridLayout.add(this.recommenderAdderUserIdValue);
        this.recommenderAdderGridLayout.add(this.recommenderAdderName);
        this.recommenderAdderGridLayout.add(this.recommenderAdderNameValue);
        this.recommenderAdderGridLayout.add(this.recommenderAdderAddress);
        this.recommenderAdderGridLayout.add(this.recommenderAdderAddressValue);
        this.recommenderAdderGridLayout.add(this.recommenderAdderTel);
        this.recommenderAdderGridLayout.add(this.recommenderAdderTelValue);
        this.recommenderAdderGridLayout.add(this.recommenderAdderCel);
        this.recommenderAdderGridLayout.add(this.recommenderAdderCelValue);

        this.recommenderAdderFlowLayout = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.recommenderAdderFlowLayout.setBorder(BorderFactory.createTitledBorder("Recomendador"));

        this.recommenderAdderFlowLayout.add(this.recommenderAdderGridLayout);

        this.recommendersInfoPanel.add(this.recommenderFlowLayout);
        this.recommendersInfoPanel.add(this.recommenderAdderFlowLayout);

        //Back arrow
        this.goBack = new JButton("Atrás");

        this.backArrowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        this.backArrowPanel.add(this.goBack);

        this.headerPanel = new JPanel(new BorderLayout(5, 5));

        this.headerPanel.add(recommendersInfoPanel, BorderLayout.CENTER);
        this.headerPanel.add(this.backArrowPanel, BorderLayout.NORTH);

        //Tables
        this.addedTableModel = new DefaultTableModel();
        this.addedTableModel.setColumnIdentifiers(ADDED_MODEL);
        this.addedTable = new JTable(this.addedTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        addedTable.setDragEnabled(false);
        addedTable.getTableHeader().setReorderingAllowed(false);
        addedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.addedTableScroll = new JScrollPane(this.addedTable);

        this.bonusesTableModel = new DefaultTableModel();
        this.bonusesTableModel.setColumnIdentifiers(BONUSES_MODEL);
        this.bonusesTable = new JTable(this.bonusesTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        bonusesTable.setDragEnabled(false);
        bonusesTable.getTableHeader().setReorderingAllowed(false);
        bonusesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.bonusesTableScroll = new JScrollPane(this.bonusesTable);

        this.tablesPanelBoxLayout = new JPanel();
        BoxLayout bl = new BoxLayout(this.tablesPanelBoxLayout, BoxLayout.X_AXIS);
        this.tablesPanelBoxLayout.setLayout(bl);

        //Added Recommenders Form
        idSearchRecommendersLabel = new JLabel("Código: ");

        userIdSearchRecommendersLabel = new JLabel("Cédula: ");

        nameSearchRecommendersLabel = new JLabel("Nombre: ");

        Dimension preferredTextFieldsSize = new Dimension(200, 30);

        idRecommendersTextField = new JFormattedTextField(0L);
        idRecommendersTextField.setPreferredSize(preferredTextFieldsSize);
        idRecommendersTextField.setValue(null);

        userIdRecommendersTextField = new JFormattedTextField(0L);
        userIdRecommendersTextField.setPreferredSize(preferredTextFieldsSize);
        userIdRecommendersTextField.setValue(null);

        nameRecommendersTextField = new JTextField();
        nameRecommendersTextField.setPreferredSize(preferredTextFieldsSize);

        idSearchUserButton = new JButton("Buscar");

        userIdSearchUserButton = new JButton("Buscar");

        nameSearchUsersButton = new JButton("Buscar");

        buttonShowAllAddedUsers = new JButton("Mostrar todos");

        panelRecommendersAddedForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelRecommendersAddedForm.add(idSearchRecommendersLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelRecommendersAddedForm.add(idRecommendersTextField, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 0;
        panelRecommendersAddedForm.add(idSearchUserButton, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelRecommendersAddedForm.add(userIdSearchRecommendersLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelRecommendersAddedForm.add(userIdRecommendersTextField, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 1;
        panelRecommendersAddedForm.add(userIdSearchUserButton, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelRecommendersAddedForm.add(nameSearchRecommendersLabel, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 1;
        gbc.gridy = 2;
        panelRecommendersAddedForm.add(nameRecommendersTextField, gbc);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 2;
        gbc.gridy = 2;
        panelRecommendersAddedForm.add(nameSearchUsersButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 3;
        panelRecommendersAddedForm.add(buttonShowAllAddedUsers, gbc);

        this.panelRecommendersAdded = new JPanel(new BorderLayout());
        this.panelRecommendersAdded.add(this.panelRecommendersAddedForm, BorderLayout.NORTH);
        this.panelRecommendersAdded.add(this.addedTableScroll, BorderLayout.CENTER);

        this.panelRecommendersAddedScrollPane = new JScrollPane(this.panelRecommendersAdded);
        this.panelRecommendersAddedScrollPane.setBorder(BorderFactory.createTitledBorder("Agregados"));

        this.tablesPanel.add(this.panelRecommendersAddedScrollPane);

        //Bonuses Form
        idSearchBonusesLabel = new JLabel("Código de la bonificación: ");
        idBonusesTextField = new JFormattedTextField(0L);
        idBonusesTextField.setValue(null);
        idBonusesTextField.setPreferredSize(new Dimension(200, 30));
        idSearchBonusesButton = new JButton("Buscar");

        date1BonusesTextField = new JFormattedTextField(sdf);
        date2BonusesTextField = new JFormattedTextField(sdf);
        dateSearchBonusesButton = new JButton("Buscar");
        dateSearchBonusesLabel = new JLabel("Fecha Inicial(dd/mm/yyyy)-Fecha Final(dd/mm/yyyy): ");

        this.buttonShowAllBonuses = new JButton("Mostrar todos");

        this.panelBonusesGottenForm = new JPanel(new GridBagLayout());
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        this.panelBonusesGottenForm.add(this.idSearchBonusesLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.idBonusesTextField, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.idSearchBonusesButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        this.panelBonusesGottenForm.add(this.dateSearchBonusesLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.date1BonusesTextField, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.date2BonusesTextField, gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.dateSearchBonusesButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        this.panelBonusesGottenForm.add(this.buttonShowAllBonuses, gbc);

        this.panelBonusesGotten = new JPanel(new BorderLayout());

        this.panelBonusesGotten.add(this.panelBonusesGottenForm, BorderLayout.NORTH);
        this.panelBonusesGotten.add(this.bonusesTableScroll, BorderLayout.CENTER);

        this.panelBonusesGottenScrollPane = new JScrollPane(this.panelBonusesGotten);

        this.panelBonusesContainer = new JPanel(new BorderLayout());
        this.panelBonusesContainer.setBorder(BorderFactory.createTitledBorder("Bonificaciones"));

        this.labelTotalBonuses = new JLabel("Total bonificaciones: ");
        this.labelTotalBonusesValue = new JLabel("");

        this.panelTotalBonuses = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.panelTotalBonuses.add(this.labelTotalBonuses);
        this.panelTotalBonuses.add(this.labelTotalBonusesValue);

        this.panelBonusesContainer.add(this.panelBonusesGottenScrollPane, BorderLayout.CENTER);
        this.panelBonusesContainer.add(this.panelTotalBonuses, BorderLayout.SOUTH);

        this.tablesPanel.add(this.panelBonusesContainer);

        this.add(this.headerPanel, BorderLayout.NORTH);
        this.add(this.tablesPanel, BorderLayout.CENTER);

    }

}

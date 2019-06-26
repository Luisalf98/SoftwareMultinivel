/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.views;

import com.loyder.software.main.ApplicationStarter;
import com.loyder.software.model.dao.config.DatabaseConnection;
import com.loyder.software.model.entities.Category;
import com.loyder.software.model.entities.DeletedUserLogRegister;
import com.loyder.software.model.entities.User;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Box;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Redes
 */
public class UsersView extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScroll;
    private JPanel searchPane;
    private JPanel searchPaneFlowLayout;
    private JPanel searchPaneFlowLayoutGridLayout;
    private JPanel panelHeaderLeft;
    private JPanel drawTreePane;
    private JPanel panelAddDeleteUser;
    private JPanel panelDeleteUserFromNetwork;
    private JPanel panelRestoreUserInNetwork;
    private JPanel footer;
    private JPanel footerBoxLayout;
    private JLabel idSearchLabel;
    private JLabel userIdSearchLabel;
    private JLabel nameSearchLabel;
    private JLabel labelCategoryFilter;
    private JComboBox comboBoxCategoryFilter;
    private DefaultComboBoxModel comboBoxCategoryFilterModel;
    private JButton drawTreeButton;
    private JButton buttonDeleteUserFromNetwork;
    private JButton buttonRestoreUserInNetwork;
    private JButton userIdSearchButton;
    private JButton nameSearchButton;
    private JButton idSearchButton;
    private JButton buttonShowAllRecommenders;
    private JFormattedTextField idTextField;
    private JFormattedTextField userIdTextField;
    private JTextField nameTextField;

    private JButton seeDetailsButton;
    private JButton addNewButton;
    private JButton seePurchasesButton;

    private static final String[] TABLE_MODEL_IDENTIFIERS = {"Código", "Cédula", "Nombre",
        "Código Categoría", "Fecha de Ingreso", "Código Recomendador"};
    private static final String[][] EMPTY_TABLE = new String[0][0];

    private final JPanel panelParent;

    private UserInfoView recommenderInfoView;
    private UserRegisterView registerRecommenderView;
    private UserPurchasesView userPurchasesView;
    private MultiLevelNetworkDrawer multiLevelNetworkDrawer;
    
    private ArrayList<DeletedUserLogRegister> deletedUserLogRegisters;

    public UsersView(JPanel panelParent1) throws ParseException {
        initComponents();
        this.panelParent = panelParent1;
        
        deletedUserLogRegisters = DatabaseConnection.getDeletedUserLogDao().getDeletedUserLogRegisters();
        
        if(deletedUserLogRegisters==null || deletedUserLogRegisters.isEmpty()){
            buttonRestoreUserInNetwork.setEnabled(false);
        }

        userIdSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userSearchByUserId(e);
            }
        });
        idSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userSearchById(e);
            }
        });
        nameSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                usersSearchByName(e);
            }
        });
        buttonShowAllRecommenders.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllRecommenders(e);
            }
        });
        buttonDeleteUserFromNetwork.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteUserFromNetwork(e);
            }
        });
        buttonRestoreUserInNetwork.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!buttonRestoreUserInNetwork.isEnabled()){
                    return;
                }
                restoreUserInNetwork(e);
            }
        });

        this.drawTreeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    ((CardLayout) panelParent.getLayout()).show(panelParent, MultiLevelNetworkDrawer.class.getName());
                    multiLevelNetworkDrawer.setData((Long) table.getValueAt(table.getSelectedRow(), 0));
                } else {
                    ((CardLayout) panelParent.getLayout()).show(panelParent, MultiLevelNetworkDrawer.class.getName());
                    multiLevelNetworkDrawer.setData(ApplicationStarter.COMPANY_ROOT.getId());
                }
            }
        });
        this.addNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                registerRecommenderView.setUIData();
                ((CardLayout) panelParent.getLayout()).show(panelParent, UserRegisterView.class.getName());
            }
        });
        this.seeDetailsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    recommenderInfoView.setData((Long) table.getValueAt(table.getSelectedRow(), 0));
                    ((CardLayout) panelParent.getLayout()).show(panelParent, UserInfoView.class.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un recomendador.");
                }
            }
        });
        this.seePurchasesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    Long customerId = (Long) table.getValueAt(table.getSelectedRow(), 0);
                    userPurchasesView.setUIData(customerId);
                    ((CardLayout) panelParent.getLayout()).show(panelParent, UserPurchasesView.class.getName());

                } else {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un cliente.");
                }
            }
        });
    }

    public UserPurchasesView getUserPurchasesView() {
        return userPurchasesView;
    }

    public void setUserPurchasesView(UserPurchasesView userPurchasesView) {
        this.userPurchasesView = userPurchasesView;
    }

    public UserInfoView getRecommenderInfoView() {
        return recommenderInfoView;
    }

    public void setRecommenderInfoView(UserInfoView recommenderInfoView) {
        this.recommenderInfoView = recommenderInfoView;
    }

    public UserRegisterView getRegisterRecommenderView() {
        return registerRecommenderView;
    }

    public void setRegisterRecommenderView(UserRegisterView registerRecommenderView) {
        this.registerRecommenderView = registerRecommenderView;
    }

    public MultiLevelNetworkDrawer getMultiLevelNetworkDrawer() {
        return multiLevelNetworkDrawer;
    }

    public void setMultiLevelNetworkDrawer(MultiLevelNetworkDrawer multiLevelNetworkDrawer) {
        this.multiLevelNetworkDrawer = multiLevelNetworkDrawer;
    }
    
    public void deleteUserFromNetwork(MouseEvent e){
        if(table.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario!");
            return;
        }
        Long userId = (Long)table.getValueAt(table.getSelectedRow(), 0);
        User user = DatabaseConnection.getUserDao().getUserById(userId);
        ArrayList<User> childs = DatabaseConnection.getUserDao().getUsersByParentId(userId);
        if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, 
                "¿Está seguro de querer eliminar al usuario "+user.getName()+" "+user.getLastName()+
                        " con Número de identificación "+ user.getUserId()+" de la red?", 
                "Confirmación de Eliminación", JOptionPane.OK_CANCEL_OPTION)){
            if(DatabaseConnection.getUserDao().removeUserById(userId)){
                if(childs==null || childs.isEmpty()){
                    DatabaseConnection.getDeletedUserLogDao().addRegister(new DeletedUserLogRegister(
                                null, user.getId(), -1L, user.getAdderId()
                        ));
                }else{
                    for(User child : childs){
                        DatabaseConnection.getDeletedUserLogDao().addRegister(new DeletedUserLogRegister(
                                null, user.getId(), child.getId(), user.getAdderId()
                        ));
                    }
                }
                JOptionPane.showMessageDialog(null, "Operación exitosa!");
                deletedUserLogRegisters = DatabaseConnection.getDeletedUserLogDao().getDeletedUserLogRegisters();
                buttonRestoreUserInNetwork.setEnabled(true);
            }else{
                JOptionPane.showMessageDialog(null, "Error!");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Operación Cancelada!");
        }
    }
    
    public void restoreUserInNetwork(MouseEvent e){
        if(deletedUserLogRegisters == null || deletedUserLogRegisters.isEmpty()){
            JOptionPane.showMessageDialog(null, "No hay usuario por restablecer!");
            return;
        }
        
        User u = DatabaseConnection.getUserDao().getUserById(deletedUserLogRegisters.get(0).getUserId());
        if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, 
                "¿Está seguro de querer restablecer al usuario "+u.getName()+" "+u.getLastName()+
                        " con Número de identificación "+ u.getUserId()+" en la red?", 
                "Confirmación de Restablecimiento de Usuario", JOptionPane.OK_CANCEL_OPTION)){
            
            DatabaseConnection.getUserDao().setUserAdder(deletedUserLogRegisters.get(0).getUserId(), deletedUserLogRegisters.get(0).getParentId());
            if(deletedUserLogRegisters.get(0).getChildId() != -1L){
                for(int i=0; i<deletedUserLogRegisters.size(); i++){
                    DatabaseConnection.getUserDao().setUserAdder(deletedUserLogRegisters.get(i).getChildId(), deletedUserLogRegisters.get(i).getUserId());
                }
            }
            
            JOptionPane.showMessageDialog(null, "Operación exitosa!");
            buttonRestoreUserInNetwork.setEnabled(false);
            DatabaseConnection.getDeletedUserLogDao().deleteAllRegisters();
            
        }else{
            JOptionPane.showMessageDialog(null, "Operación Cancelada!");
        }
    }

    public void userSearchById(MouseEvent e) {
        
        Long id = (Long) idTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Código digitado inválido.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Category c = (Category) comboBoxCategoryFilter.getSelectedItem();
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        User user;
        if (!c.getId().equals(-1L)) {
            user = DatabaseConnection.getUserDao().getUserByIdAndCategoryId(id, c.getId());
        } else {
            user = DatabaseConnection.getUserDao().getUserById(id);
        }

        if (user != null) {
            tableModel.addRow(new Object[]{user.getId(), user.getUserId(),
                user.getName() + " " + user.getLastName(), user.getCategoryId(),
                ApplicationStarter.formatDate(new Date(user.getEntranceDate())), user.getAdderId()});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro el cliente con el ID especificado.");
        }

    }

    public void userSearchByUserId(MouseEvent e) {
        Long id = (Long) userIdTextField.getValue();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Cédula no válida.");
            return;
        }

        idTextField.setText("");
        idTextField.setValue(null);

        Category c = (Category) comboBoxCategoryFilter.getSelectedItem();
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        User user;
        if (!c.getId().equals(-1L)) {
            user = DatabaseConnection.getUserDao().getUserByUserIdAndCategoryId(id, c.getId());
        } else {
            user = DatabaseConnection.getUserDao().getUserByUserId(id);
        }

        if (user != null) {
            tableModel.addRow(new Object[]{user.getId(), user.getUserId(),
                user.getName() + " " + user.getLastName(), user.getCategoryId(),
                ApplicationStarter.formatDate(new Date(user.getEntranceDate())), user.getAdderId()});
        } else {
            JOptionPane.showMessageDialog(this, "No se encontro el cliente con la cédula especificada.");
        }
    }

    public void usersSearchByName(MouseEvent e) {
        String name = nameTextField.getText().toUpperCase();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "El campo no puede estar vacío.");
            return;
        }

        nameTextField.setText("");

        Category c = (Category) comboBoxCategoryFilter.getSelectedItem();
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<User> users;
        if (!c.getId().equals(-1L)) {
            users = DatabaseConnection.getUserDao().getUsersByNameAndCategoryId(name, c.getId());
        } else {
            users = DatabaseConnection.getUserDao().getUsersByName(name);
        }
        
        if (users != null && !users.isEmpty()) {
            users.forEach((user) -> {
                tableModel.addRow(new Object[]{user.getId(), user.getUserId(),
                    user.getName() + " " + user.getLastName(), user.getCategoryId(),
                    ApplicationStarter.formatDate(new Date(user.getEntranceDate())), user.getAdderId()});
            });
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void showAllRecommenders(MouseEvent e) {
        Category c = (Category) comboBoxCategoryFilter.getSelectedItem();
        tableModel.setDataVector(EMPTY_TABLE, TABLE_MODEL_IDENTIFIERS);
        ArrayList<User> users;
        if (!c.getId().equals(-1L)) {
            users = DatabaseConnection.getUserDao().getAllUsersByCategoryId(c.getId());
        } else {
            users = DatabaseConnection.getUserDao().getAllUsers();
        }

        if (users != null && !users.isEmpty()) {
            users.forEach((user) -> {
                tableModel.addRow(new Object[]{user.getId(), user.getUserId(),
                    user.getName() + " " + user.getLastName(), user.getCategoryId(),
                    ApplicationStarter.formatDate(new Date(user.getEntranceDate())), user.getAdderId()});
            });
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
        }
    }

    public void setDataUI() {
        comboBoxCategoryFilterModel.removeAllElements();
        ArrayList<Category> categories = DatabaseConnection.getCategoryDao().getAllCategories();
        comboBoxCategoryFilterModel.addElement(new Category(-1L, "NINGUNA"));
        categories.forEach(category -> {
            comboBoxCategoryFilterModel.addElement(category);
        });

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

        idSearchLabel = new JLabel("Código: ");
        idSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userIdSearchLabel = new JLabel("Cédula: ");
        userIdSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        nameSearchLabel = new JLabel("Nombre: ");
        nameSearchLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        Dimension preferredTextFieldsSize = new Dimension(200, 30);

        idTextField = new JFormattedTextField(0L);
        idTextField.setPreferredSize(preferredTextFieldsSize);
        idTextField.setValue(null);

        userIdTextField = new JFormattedTextField(0L);
        userIdTextField.setPreferredSize(preferredTextFieldsSize);
        userIdTextField.setValue(null);

        nameTextField = new JTextField();
        nameTextField.setPreferredSize(preferredTextFieldsSize);
        nameTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);

        idSearchButton = new JButton("Buscar");

        userIdSearchButton = new JButton("Buscar");

        nameSearchButton = new JButton("Buscar");

        buttonShowAllRecommenders = new JButton("Mostrar todos");

        searchPaneFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        searchPaneFlowLayoutGridLayout = new JPanel(new GridLayout(5, 3, 10, 10));

        labelCategoryFilter = new JLabel("Filtrar por categoría: ");
        comboBoxCategoryFilterModel = new DefaultComboBoxModel();
        comboBoxCategoryFilter = new JComboBox(comboBoxCategoryFilterModel);

        setDataUI();

        searchPaneFlowLayoutGridLayout.add(labelCategoryFilter);
        searchPaneFlowLayoutGridLayout.add(comboBoxCategoryFilter);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());

        searchPaneFlowLayoutGridLayout.add(idSearchLabel);
        searchPaneFlowLayoutGridLayout.add(idTextField);
        searchPaneFlowLayoutGridLayout.add(idSearchButton);

        searchPaneFlowLayoutGridLayout.add(userIdSearchLabel);
        searchPaneFlowLayoutGridLayout.add(userIdTextField);
        searchPaneFlowLayoutGridLayout.add(userIdSearchButton);

        searchPaneFlowLayoutGridLayout.add(nameSearchLabel);
        searchPaneFlowLayoutGridLayout.add(nameTextField);
        searchPaneFlowLayoutGridLayout.add(nameSearchButton);

        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        searchPaneFlowLayoutGridLayout.add(buttonShowAllRecommenders);
        searchPaneFlowLayoutGridLayout.add(Box.createGlue());
        
        panelHeaderLeft = new JPanel(new BorderLayout());
        panelDeleteUserFromNetwork = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        buttonDeleteUserFromNetwork = new JButton("Eliminar Usuario de la Red");
        
        panelDeleteUserFromNetwork.add(buttonDeleteUserFromNetwork);
        
        panelRestoreUserInNetwork = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        buttonRestoreUserInNetwork = new JButton("Restablecer Usuario Eliminado");
        
        panelRestoreUserInNetwork.add(buttonRestoreUserInNetwork);

        drawTreePane = new JPanel(new FlowLayout(FlowLayout.CENTER));

        drawTreeButton = new JButton("Dibujar Red Multinivel");

        drawTreePane.add(drawTreeButton);
        
        panelAddDeleteUser = new JPanel(new GridLayout(1,2));
        panelAddDeleteUser.add(panelDeleteUserFromNetwork);
        panelAddDeleteUser.add(panelRestoreUserInNetwork);
        
        panelHeaderLeft.add(drawTreePane, BorderLayout.NORTH);
        panelHeaderLeft.add(panelAddDeleteUser, BorderLayout.SOUTH);

        searchPaneFlowLayout.add(searchPaneFlowLayoutGridLayout);
        searchPane.add(searchPaneFlowLayout, BorderLayout.CENTER);
        searchPane.add(panelHeaderLeft, BorderLayout.WEST);

        seeDetailsButton = new JButton("Más información");

        addNewButton = new JButton("Agregar Nuevo");

        seePurchasesButton = new JButton("Ver compras");

        footerBoxLayout = new JPanel();
        BoxLayout b = new BoxLayout(footerBoxLayout, BoxLayout.X_AXIS);
        footerBoxLayout.setLayout(b);

        footerBoxLayout.add(seeDetailsButton);
        footerBoxLayout.add(addNewButton);
        footerBoxLayout.add(seePurchasesButton);

        JPanel jp = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));

        jp.add(footerBoxLayout);

        footer.add(jp);

        this.add(tableScroll, BorderLayout.CENTER);
        this.add(searchPane, BorderLayout.NORTH);
        this.add(footer, BorderLayout.SOUTH);

    }
}

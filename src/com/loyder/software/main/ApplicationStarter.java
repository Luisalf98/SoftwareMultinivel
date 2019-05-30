/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.main;

import com.loyder.software.model.dao.config.DatabaseConfig;
import com.loyder.software.model.entities.User;
import com.loyder.software.views.MainWindow;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Luis Perez
 */
public class ApplicationStarter {

    /**
     */
    
    public static final User COMPANY_ROOT = new User(-1L, 9005370698L, "SANDYL", "", "Calle 60 # 27 - 46", "320 0550", "318 721 4616", -2L, 0L, -1L);
    public static final Locale LOCALE = new Locale("es", "CO");
    public static final NumberFormat PERCENTAGE_FORMAT = NumberFormat.getPercentInstance(LOCALE);
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(LOCALE);
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(LOCALE);
    
    static{
        PERCENTAGE_FORMAT.setMinimumFractionDigits(0);
        PERCENTAGE_FORMAT.setMaximumFractionDigits(2);
        NUMBER_FORMAT.setMaximumFractionDigits(2);
        NUMBER_FORMAT.setMinimumFractionDigits(0);
    }
    
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            boolean laf = false;
            for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
                if (lafi.getName().equals("Nimbus")) {
                    UIManager.setLookAndFeel(lafi.getClassName());
                    laf = true;
                    break;
                }
            }
            if (!laf) {
                for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
                    if (lafi.getName().equals("Windows")) {
                        UIManager.setLookAndFeel(lafi.getClassName());
                        laf = true;
                        break;
                    }
                }
            }
            if (!laf) {
                for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels()) {
                    if (lafi.getName().equals("GTK+")) {
                        UIManager.setLookAndFeel(lafi.getClassName());
                        laf = true;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ApplicationStarter.class.getName() + "::main(): " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "No se encontraron temas instalados para la aplicación. Se usará el tema del sistema operativo por defecto.");
        }

        if (DatabaseConfig.initDatabase()) {
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    new MainWindow().setVisible(true);
                } catch (ParseException ex) {
                    Logger.getLogger(ApplicationStarter.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            JOptionPane.showMessageDialog(null, ApplicationStarter.class.getName() + "::main(): No se pudo iniciar la base de datos.");
        }
        
        for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++) {
            System.out.println(UIManager.getInstalledLookAndFeels()[i].getName());
        }
    }
    
    private static SimpleDateFormat sdf;
    
    public static String formatDate(Date date){
        if(sdf==null){
            sdf = new SimpleDateFormat("dd/MM/yyyy");
        }
        return sdf.format(date);
    }

    public static String getSHA(String input) {

        try {

            // Static getInstance method is called with hashing SHA 
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value 
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "<html>Exception thrown"
                    + " for incorrect algorithm: " + e + "</html>");

            return null;
        }
    }

}

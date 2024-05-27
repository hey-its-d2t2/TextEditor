package com.txtEdit;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import javax.swing.*;

public class Register extends JPanel implements ActionListener {

    JLabel userL = new JLabel("User name : ");
    JTextField userTF = new JTextField();
    JLabel passL = new JLabel("Password : ");
    JPasswordField passTF = new JPasswordField();
    
    JLabel passC = new JLabel("Confirm Pass : ");
    JPasswordField passCTF = new JPasswordField();
    
    JButton register = new JButton("Register");
    JButton back = new JButton("Back");
    
    public Register() {
        
        JPanel loginP = new JPanel();
        loginP.setLayout(new GridLayout(4, 2));
        loginP.add(userL);
        loginP.add(userTF);
        loginP.add(passL);
        loginP.add(passTF);
        loginP.add(passC);
        loginP.add(passCTF);
        loginP.add(register);
        loginP.add(back);
        register.addActionListener(this);
        back.addActionListener(this);
        add(loginP);
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {
       
        
        if (e.getSource() == register && passTF.getPassword().length > 0 && userTF.getText().length() > 0) {
            
            String pass = new String(passTF.getPassword());
            String cnfPass = new String(passCTF.getPassword());
            if (pass.equals(cnfPass)) {
                try {
                    BufferedReader input = new BufferedReader(new FileReader("password.txt"));
                    String line = input.readLine();
                    while (line != null) {
                        StringTokenizer st = new StringTokenizer(line);
                        if (userTF.getText().equals(st.nextToken())) {

                        	JOptionPane.showMessageDialog(this, "User already exists...");
                            input.close();
                            return;
                        }
                        line = input.readLine();
                    }
                    input.close();
                    
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    md.update(pass.getBytes());
                    byte byteData[] = md.digest();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < byteData.length; i++) {
                        sb.append(String.format("%02x", byteData[i]));
                    }
                    BufferedWriter output = new BufferedWriter(new FileWriter("password.txt", true));
                    output.write(userTF.getText() + " " + sb.toString() + "\n");
                    output.close();
                    
                    Login login = (Login) getParent();
                    login.cl.show(login, "Login");
                    
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (NoSuchAlgorithmException e3) {
                    e3.printStackTrace();
                }
            }
        }
        if (e.getSource() == back) {
            Login login = (Login) getParent();
            login.cl.show(login, "Login");
        }
    }
}

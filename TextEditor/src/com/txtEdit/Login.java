package com.txtEdit;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import javax.swing.*;

public class Login extends JPanel implements ActionListener {

    JLabel userL = new JLabel("Username: ");
    JTextField userTF = new JTextField();

    JLabel passL = new JLabel("Password: ");
    JPasswordField passTF = new JPasswordField();

    JPanel loginP = new JPanel(new GridLayout(3, 2));
    JPanel panel = new JPanel();

    JButton login = new JButton("Login");
    JButton register = new JButton("Register");

    CardLayout cl;

    Login() {
        setLayout(new CardLayout());
        loginP.add(userL);
        loginP.add(userTF);
        loginP.add(passL);
        loginP.add(passTF);
        login.addActionListener(this);
        register.addActionListener(this);
        loginP.add(login);
        loginP.add(register);
        panel.add(loginP);
        add(panel, "Login");
        cl = (CardLayout) getLayout();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            handleLogin();
        } else if (e.getSource() == register) {
            add(new Register(), "register");
            cl.show(this, "register");
        }
    }

    private void handleLogin() {
        try {
            BufferedReader input = new BufferedReader(new FileReader("password.txt"));
            String pass = null;
            String line = input.readLine();
            while (line != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (userTF.getText().equals(st.nextToken())) {
                    pass = st.nextToken();
                }
                line = input.readLine();
            }
            input.close();

            if (pass != null) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(new String(passTF.getPassword()).getBytes());
                byte byteData[] = md.digest();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < byteData.length; i++) {
                    sb.append(String.format("%02x", byteData[i]));
                }

                if (pass.equals(sb.toString())) {
                    //System.out.println("You have successfully logged in...");
                    add(new FileBrowser(userTF.getText()),"fb");
                    cl.show(this,"fb");
                } else {
                 
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                }
            } else {
            	JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
            

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        Login login = new Login();
        frame.add(login);
        frame.setVisible(true);
    }

	
}

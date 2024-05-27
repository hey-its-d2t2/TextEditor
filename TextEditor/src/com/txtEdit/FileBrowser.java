package com.txtEdit;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class FileBrowser extends JPanel implements ActionListener {

    JLabel label = new JLabel("File List");
    JButton newFile = new JButton("New File");
    JButton open = new JButton("Open");
    JTextField newFileTF = new JTextField(10);
    ButtonGroup bg;
    File directory;
    JPanel fileList;

    public FileBrowser(String dir) {
        directory = new File(dir);
        directory.mkdir();
        fileList = new JPanel(new GridLayout(0, 1));
        fileList.add(label);
        bg = new ButtonGroup();
        listFiles();
        JPanel newP = new JPanel();
        newP.add(newFileTF);
        newP.add(newFile);
        fileList.add(open);
        fileList.add(newP);
        add(fileList);

        newFile.addActionListener(this);
        open.addActionListener(this);
    }

    private void listFiles() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                JRadioButton radio = new JRadioButton(file.getName());
                radio.setActionCommand(file.getName());
                bg.add(radio);
                fileList.add(radio);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Login login = (Login) getParent();
        if (e.getSource() == open) {
            if (bg.getSelection() != null) {
                String selectedFile = directory.getName() + "\\" + bg.getSelection().getActionCommand();
                login.add(new Editor(selectedFile), "editor");
                login.cl.show(login, "editor");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a file to open.");
            }
        }
        if (e.getSource() == newFile) {
            String newFileName = newFileTF.getText().trim();
            if (newFileName.length() > 0) {
                String file = directory.getName() + "\\" + newFileName + ".txt";
                File newFile = new File(file);
                if (!newFile.exists()) {
                    try {
                        if (newFile.createNewFile()) {
                            login.add(new Editor(file), "editor");
                            login.cl.show(login, "editor");
                            refreshFileList();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error creating new file.");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error creating new file.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "File already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a file name.");
            }
        }
    }

    private void refreshFileList() {
        fileList.removeAll();
        fileList.add(label);
        bg = new ButtonGroup();
        listFiles();
        JPanel newP = new JPanel();
        newP.add(newFileTF);
        newP.add(newFile);
        fileList.add(open);
        fileList.add(newP);
        fileList.revalidate();
        fileList.repaint();
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("File Browser");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500, 400);
//        FileBrowser browser = new FileBrowser("files");
//        frame.add(browser);
//        frame.setVisible(true);
//    }
}

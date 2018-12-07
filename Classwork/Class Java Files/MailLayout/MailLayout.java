/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maillayout;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

class MailLayout extends JFrame {

    private JFrame project = new JFrame();
    private String[] contentNames = {
        String.format("  %15s ", "To:"),
        String.format("  %15s ", "Cc:"),
        String.format("  %14s ", "Bcc:"),
        String.format("  %11s ", "Subject:")};
    private MyPanel[] contentPanels = new MyPanel[4];
    private Dimension size = new Dimension(600, 300);
    private JTextArea writing = new JTextArea();
    private JScrollPane scroll = new JScrollPane(writing);
    TopPanel top;

    public MailLayout() {
        for (int i = 0; i < contentPanels.length; i++) {
            contentPanels[i] = new MyPanel(contentNames[i]);
        }
        top = new TopPanel();
        project.setTitle("New Message");
        project.add(top, BorderLayout.NORTH);
        project.add(scroll, BorderLayout.CENTER);
        project.setSize(size);
        project.setLocationRelativeTo(null);
        project.setVisible(true);
        project.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        MailLayout layout = new MailLayout();
    }

    class TopPanel extends JPanel {

        TopPanel() {
            //modify to take array of panels
            setLayout(new GridLayout(5, 1));
            for (int i = 0; i < contentPanels.length; i++) {
                add(contentPanels[i]);
            }
            add(new EmailPanel());
        }

        class EmailPanel extends JPanel {

            EmailPanel() {
                String[] emails
                        = {"emailOne@gmail.com",
                            "emailTwo@gmail.com",
                            "emailThree@gmail.com"};
                setLayout(new BorderLayout());
                JPanel stuff = new JPanel();
                stuff.setLayout(new BorderLayout());
                JButton send = new JButton("SEND");
                send.addActionListener(new ButtonListener());
                stuff.add(send, BorderLayout.WEST);
                stuff.add(new JLabel(" From: "), BorderLayout.CENTER);
                add(stuff, BorderLayout.WEST);
                JComboBox<String> email = new JComboBox<String>(emails);
                add(email, BorderLayout.CENTER);

            }

            class ButtonListener implements ActionListener {

                public void actionPerformed(ActionEvent e) {
                    try {
                        printFile();
                    } catch (FileNotFoundException ex) {
                    } catch (IOException io) {
                    }

                }

            }

            public void printFile() throws FileNotFoundException, IOException {
                //doesn't need full path
                PrintWriter w = new PrintWriter("/nfs/home/s/h/sheppardsage/unit6/outbox.txt");
                writing.write(w);
                w.close();
            }
        }
    }

    class WritingPanel extends JTextField {

        WritingPanel() {
            addKeyListener(new TypeListener());

        }

        public String toString() {
            return this.getText();
        }

        class TypeListener extends KeyAdapter {

            public void keyReleased(KeyEvent e) {
                String title;
                title = e.getSource().toString();
                project.setTitle(title);
            }
        }
    }

    class MyPanel extends JPanel {

        MyPanel(String name) {
            setLayout(new BorderLayout());
            JLabel label = new JLabel(name);
            add(label, BorderLayout.WEST);
            if (!name.equals(contentNames[3])) {
                add(new JTextField(), BorderLayout.CENTER);
            } else {
                add(new WritingPanel(), BorderLayout.CENTER);
            }
        }

    }

}

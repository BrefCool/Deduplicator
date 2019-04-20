package edu.bu.ec504.group9;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class Dedup extends JFrame {

/** define GUI components */
    private JLabel Message;

    private JTextField lockerName;
    private JTextField addFileName;
    private JTextField retrieveFileName;
    private JButton submitButton;
    private JButton addButton;
    private JButton retrieveButton;
    private JTextArea sysMessage;
    private JTextField filePath;
    private JLabel pathMessage;
    private JButton submitButton2;
    private JLabel filepathMsg;
    private JButton button1;
    private JScrollPane jsp;
    private PipedInputStream pin = new PipedInputStream();

    /** initialize GUI */
    public Dedup() {

        FileIO.initialize();
        createView();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400,350);
        setLocationRelativeTo(null);
        setResizable(false);
    }
        private void createView(){
            JFrame f = new JFrame("Deduplicator");
            JPanel panel = new JPanel();
            getContentPane().add(panel);


            /** locker section */
            JLabel label = new JLabel("Enter your locker name: (set locker to default if leave blank)");
            panel.add(label);
            lockerName = new JTextField();
            lockerName.setPreferredSize(new Dimension(150,30));
            panel.add(lockerName);
            String locker;
//            Locker myLocker = LockerFactory.getLocker(locker, LockerFactory.CHUNKING.FIXEDSIZE);
            submitButton = new JButton("Exchange");
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String locker = lockerName.getText();
                    if (locker.isEmpty()){
                        Message.setText("Locker name: default");
                    }else{
                        Message.setText("Locker name:  "+locker);
                    }
                }
            });
            panel.add(submitButton);
            Message = new JLabel("Enter locker name! ");
            panel.add(Message);

            /** add file section */
            addFileName = new JTextField();
            addFileName.setPreferredSize(new Dimension(250,30));
            panel.add(addFileName);
            addButton = new JButton("   Add   ");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String locker = lockerName.getText();
                    if (locker.isEmpty()){
                        locker = "default";
                    }
                    Locker myLocker = LockerFactory.getLocker(locker, LockerFactory.CHUNKING.FIXEDSIZE);
                    String name = addFileName.getText();
                    if (name.isEmpty()){
                        sysMessage.append("Name can't be empty!\n");
                    }else{
                        myLocker.addFile(name, "");
                        PrintStream sysOut = System.out;
                        System.setOut(new PrintStream(new OutputStream() {
                            @Override
                            public void write(int b) throws IOException {
                                sysMessage.append(String.valueOf((char) b));
                                sysOut.write(b);
                            }
                        }));
//                        else {
//
//                        sysMessage.append("File Added!             \n");}
                    }
                }
            });
            panel.add(addButton);

            /** define file path section */
            JLabel pathMessage = new JLabel("Enter your file path: (create new folder if leave blank)");
            panel.add(pathMessage);
            filePath = new JTextField();
            filePath.setPreferredSize(new Dimension(250,30));
            panel.add(filePath);
            submitButton2 = new JButton("Submit");
            submitButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = filePath.getText();
                    if (name.isEmpty()){
                        File dir = new File("retrieveFile");
                        dir.mkdir();
                        filepathMsg.setText("  Alert! Created new folder in current path for retrieve!");
                    }else{

                        filepathMsg.setText("   File path: "+name);
                    }
                }
            });
            panel.add(submitButton2);
            filepathMsg = new JLabel("   Enter file path above or click submit to create a new folder! ");
            filepathMsg.setPreferredSize(new Dimension(400,30));
            panel.add(filepathMsg);

            /** retrieve file section */
            retrieveFileName = new JTextField();
            retrieveFileName.setPreferredSize(new Dimension(250,30));
            panel.add(retrieveFileName);
            retrieveButton = new JButton("Retrieve");
            retrieveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String path = filePath.getText();
                    String locker = lockerName.getText();
                    if (locker.isEmpty()){
                        locker = "default";
                    }
                    Locker myLocker = LockerFactory.getLocker(locker, LockerFactory.CHUNKING.FIXEDSIZE);
                    String name = retrieveFileName.getText();
                    if (name.isEmpty()){
                        sysMessage.append("Name can't be empty!\n");
                    }else{
                        if (path.isEmpty()) {
                            myLocker.retrieveFile(name, "./retrieveFile");
                            PrintStream sysOut = System.out;
                            System.setOut(new PrintStream(new OutputStream() {
                                @Override
                                public void write(int b) throws IOException {
                                    sysMessage.append(String.valueOf((char) b));
                                    sysOut.write(b);
                                }
                            }));
//                            sysMessage.append("File Retrieved!          \n");
                        }
                        else {
                            myLocker.retrieveFile(name, path);
                            PrintStream sysOut = System.out;
                            System.setOut(new PrintStream(new OutputStream() {
                                @Override
                                public void write(int b) throws IOException {
                                    sysMessage.append(String.valueOf((char) b));
                                    sysOut.write(b);
                                }
                            }));
//                            sysMessage.append("File Retrieved!          \n");
                        }
                    }
                }
            });
            panel.add(retrieveButton);
            sysMessage = new JTextArea("Enter file name for adding or retrieving! \n");

            panel.add(sysMessage,BorderLayout.CENTER);

            jsp = new JScrollPane(sysMessage,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            panel.add(jsp);
        }
    /** run */
        public static void main (String[]args){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Dedup().setVisible(true);
                }
            });
        }
    }




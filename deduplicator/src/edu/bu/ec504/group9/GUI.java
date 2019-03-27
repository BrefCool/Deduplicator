package edu.bu.ec504.group9;
import java.io.File;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    public  GUI() {
        FileIO.initialize();
        JFrame frame=new JFrame("DeDuplicator");
        Box b1=Box.createHorizontalBox();  // create box
        frame.add(b1);  // add box
        b1.add(Box.createVerticalStrut(300));  //set vertical structure
        JButton AFB = new JButton("Add File");  // add addfile button
        b1.add(AFB);  // add button to box
        b1.add(Box.createHorizontalStrut(200));
        b1.add(Box.createHorizontalGlue());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set exit function
        AFB.addActionListener(ActionEvent-> {       // choose file
            Locker myLocker = LockerFactory.getLocker("myLocker", LockerFactory.CHUNKING.FIXEDSIZE);
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jfc.showDialog(new JLabel(), "Choose");
            File file = jfc.getSelectedFile();
            if (null != file) {
                myLocker.addFile(file.getAbsolutePath());
            }
        });

        frame.setBounds(100,100,400,200);
        frame.setVisible(true);
    }
}
package edu.bu.ec504.group9;
import java.io.File;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    public  GUI() {
        FileIO.initialize();
        Locker myLocker = LockerFactory.getLocker("myLocker", LockerFactory.CHUNKING.FIXEDSIZE);

        JFrame frame=new JFrame("DeDuplicator");
        Box b1=Box.createHorizontalBox();  // create box
        frame.add(b1);  // add box
        b1.add(Box.createVerticalStrut(300));  //set vertical structure

        JButton AFB = new JButton("Add File");  // add addfile button
        JButton RB = new JButton("Retrieve File");
        JButton RFI = new JButton("Retrieve FileInfo");

        b1.add(AFB);  // add button to box
        b1.add(RB);
        b1.add(RFI);
        b1.add(Box.createHorizontalStrut(200));
        b1.add(Box.createHorizontalGlue());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set exit function

        AFB.addActionListener(ActionEvent-> {       // choose file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jfc.showDialog(new JLabel(), "Choose");
            File file = jfc.getSelectedFile();
            if (null != file) {
                myLocker.addFile(file.getAbsolutePath());
            }
        });

        RB.addActionListener(ActionEvent-> {       // retrieve file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jfc.showDialog(new JLabel(), "Choose");
            File file = jfc.getSelectedFile();
            if (null != file) {
                myLocker.retrieveFile("TestFile00.txt",file.getAbsolutePath());
            }
        });

        RFI.addActionListener(ActionEvent-> {       // retrieve file
            System.out.println(myLocker.retrieveFileInfo());
        });
        frame.setBounds(100,100,400,200);
        frame.setVisible(true);
    }
}
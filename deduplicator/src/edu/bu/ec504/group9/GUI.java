package edu.bu.ec504.group9;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;
import javax.swing.*;



public class GUI extends JFrame {
    JFrame frame = new JFrame("DeDuplicator");
    JButton AFB = new JButton("Add File");  // add addfile button
    JTextField jt = new JTextField();
    JButton ALB = new JButton("Add Locker");
    JPopupMenu menu = new JPopupMenu();
    JButton RFI = new JButton("Retrieve FileInfo");
    Locker myLocker = LockerFactory.getLocker("default", LockerFactory.CHUNKING.ROLLING);
    JList<String> jl = new JList<>();
    JComboBox<String> jc = new JComboBox<>();
    String lockerDir = new StringJoiner(File.separator).add(System.getProperty("user.home")).add(".dedupStore")
            .add("lockers").toString();
    JScrollPane sp = new JScrollPane();

    public GUI() {
        FileIO.initialize();
        init();

    }
    public void init() {

        JButton RB = new JButton("SubString Search");
        RB.setBounds(160, 110, 120, 50);
        RB.addActionListener(ActionEvent -> {       // retrieve file
            myLocker = LockerFactory.getLocker((String)jc.getSelectedItem(), LockerFactory.CHUNKING.FIXEDSIZE);
            System.out.println(myLocker.SSS(jt.getText()));
        });
        jcInit();
        initJMenu();
        AFBInit();
        ALBInit();
        jlInit();
        jtInit();
        RFIInit();
        spInit();
        framInit();
        frame.add(RB);

    }

    public void addFileDirElements(LockerMeta meta, DefaultListModel dlm, String filename) {
        HashMap<String, FileInfo> files = meta.files;
        HashMap<String, ArrayList<String>> directories = meta.directorys;

        if (!directories.containsKey(filename)) {
            if (!files.containsKey(filename)) {
                System.out.println("file not exists?");
                return;
            }
            dlm.addElement(filename);
        } else {
            dlm.addElement(filename);
            for (String subFile : directories.get(filename))
                addFileDirElements(meta, dlm, subFile);
        }
    }

    public void JlistSetElement(Locker locker) {
        DefaultListModel dlm = new DefaultListModel();
        LockerMeta meta = locker.getMetaData();
        jl.removeAll();

        for (String subFile : meta.directorys.get("/"))
            addFileDirElements(meta, dlm, subFile);

        jl.setModel(dlm);
    }

    public void jcInit(){
        jc.removeAllItems();
        jc.setBounds(10,60,150,30);
        File file = new File(lockerDir);
        File[] fs = file.listFiles();
        if (fs!=null) {
            for (File f : fs) {
                String fname = f.getName();
                String suffix = fname.substring(fname.lastIndexOf(".") + 1);
                if (!f.isDirectory() && !suffix.equals("DS_Store"))
                    jc.addItem(f.getName());
            }
        }
        jc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String locker = (String) jc.getSelectedItem();
                if (locker != null && !locker.equals(""))
                    myLocker = LockerFactory.getLocker(locker,LockerFactory.CHUNKING.ROLLING);
                JlistSetElement(myLocker);
            }
        });
    }

    public void jcReset(){
        jc.removeAllItems();
        File file = new File(lockerDir);
        File[] fs = file.listFiles();
        if (fs!=null) {
            for (File f : fs) {
                String fname = f.getName();
                String suffix = fname.substring(fname.lastIndexOf(".") + 1);
                if (!f.isDirectory() && !suffix.equals("DS_Store"))
                    jc.addItem(f.getName());
            }
        }
        JlistSetElement(myLocker);

    }

    public void initJMenu() {
        JMenuItem retrieve = new JMenuItem("Retrieve");
        JMenuItem deletion = new JMenuItem("Delete");
        retrieve.addActionListener(ActionEvent->{
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = jfc.showDialog(new JLabel(), "Choose");
            File file = jfc.getSelectedFile();
            if (result == jfc.APPROVE_OPTION) {
                if (jl.getSelectedValue() != null) {
                    myLocker.retrieveFile(jl.getSelectedValue(), file.getAbsolutePath());
                    JOptionPane.showInternalMessageDialog(null, "Retrieve File Successfully", "DeDuplicator",
                            JOptionPane.INFORMATION_MESSAGE);
                } else
                    JOptionPane.showInternalMessageDialog(null, "File does not exist", "DeDuplicator",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        });
        deletion.addActionListener(ActionEvent->{
            if (null != jl.getSelectedValue()){
                myLocker.deleteFile(jl.getSelectedValue());
                JlistSetElement(myLocker);
                //System.out.println("123");
            }
        });
        menu.add(retrieve);
        menu.add(deletion);
    }

    public void framInit(){
        frame.setBounds(700, 300, 300, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set exit function
        frame.add(AFB);  // add button to box
        frame.add(ALB);
        frame.add(RFI);
        frame.add(jt);
//        frame.add(jl);
        frame.add(sp);
        frame.add(jc);
        frame.setVisible(true);

    }
    public void AFBInit(){
        AFB.setBounds(160, 60, 120, 50);
        AFB.addActionListener(ActionEvent -> {       // choose file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = jfc.showDialog(new JLabel(), "Choose");
            if (result == jfc.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                if (myLocker.containsFile("/"+file.getName()))
                    JOptionPane.showInternalMessageDialog(null, "Filename already exists", "DeDuplicator",
                            JOptionPane.INFORMATION_MESSAGE);
                else  {
                    myLocker.addFile(file.getAbsolutePath(), "");
                    JOptionPane.showInternalMessageDialog(null, "Add file successfully", "DeDuplicator",
                            JOptionPane.INFORMATION_MESSAGE);
                    JlistSetElement(myLocker);

                }
            }
        });
    }

    public void jtInit(){
        jt.setText("default");
        jt.setBounds(10, 10, 150, 50);
        jt.setFont(new Font("default", Font.BOLD, 18));
    }

    public void ALBInit(){
        ALB.setBounds(160, 10, 120, 50);
        ALB.addActionListener(ActionEvent -> {
            System.out.println("text:" + jt.getText());
            if (!jt.getText().equals(""))
                myLocker = LockerFactory.getLocker(jt.getText(), LockerFactory.CHUNKING.ROLLING);
            jcReset();
        });
    }

    public void RFIInit(){
        RFI.setBounds(160, 160, 120, 50);
        RFI.addActionListener(ActionEvent -> {       // retrieve file
            System.out.println(myLocker.getMetaData().files.keySet());
        });
    }

    public void jlInit(){
//        jl.setBounds(10, 90, 150, 150);
        myLocker = LockerFactory.getLocker((String)jc.getSelectedItem(), LockerFactory.CHUNKING.FIXEDSIZE);
        jl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    menu.show(e.getComponent(),e.getX(),e.getY());

                }
            }
        });
        JlistSetElement(myLocker);
    }

    public void spInit(){
        sp.setBounds(10, 90, 150, 150);
        sp.setViewportView(jl);

    }


}
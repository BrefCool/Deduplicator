package edu.bu.ec504.group9;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.*;



public class GUI extends JFrame {
    JFrame frame = new JFrame("DeDuplicator"); //add frame
    JButton AFB = new JButton("Add File");  // add addfile button
    JTextField jt = new JTextField(); // add locker text
    JButton ALB = new JButton("Add Locker"); // add locker button
    JPopupMenu menu = new JPopupMenu(); //right click menu
    JTextField jt2 = new JTextField("search"); // button for searching
    Locker myLocker = LockerFactory.getLocker("default", LockerFactory.CHUNKING.ROLLING);//set default locker
    JList<String> jl = new JList<>();  // list for files
    JComboBox<String> jc = new JComboBox<>(); // ComboBox for locker selection
    String lockerDir = new StringJoiner(File.separator).add(System.getProperty("user.home")).add(".dedupStore")
            .add("lockers").toString();
    JScrollPane sp1 = new JScrollPane();
    JButton ExportButton = new JButton("Export");
    JButton ImportButton = new JButton("Import");
    JButton RB = new JButton("Search");
    JTextArea jta = new JTextArea();
    List<String> result = new ArrayList<>();
    JScrollPane sp2 = new JScrollPane();

    public GUI() {
        FileIO.initialize();
        init(); //GUI init

    }
    public void init() {
        jcInit();
        initJMenu();
        AFBInit();
        ALBInit();
        jlInit();
        jtInit();
        jt2Init();
        sp1Init();
        sp2Init();
        RBInit();
        jtaInit();
        framInit();
        ImportExportBtnInit();

    }
    //Fuction for Jlist Elements
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
    //Reset Jlist Elements
    public void JlistSetElement(Locker locker) {
        DefaultListModel dlm = new DefaultListModel();
        LockerMeta meta = locker.getMetaData();
        jl.removeAll();

        for (String subFile : meta.directorys.get("/"))
            addFileDirElements(meta, dlm, subFile);

        jl.setModel(dlm);
    }
    //ComboList init
    public void jcInit(){
        jc.removeAllItems();
        jc.setBounds(10,60,150,30);
        File file = new File(lockerDir);
        File[] fs = file.listFiles();
        if (fs!=null) {
            for (File f : fs) {
                String fname = f.getName(); //get file names from locker
                String suffix = fname.substring(fname.lastIndexOf(".") + 1); //get last name
                if (!f.isDirectory() && !suffix.equals("DS_Store"))  // ignore DS_Store
                    jc.addItem(f.getName());
            }
        }
        jc.addActionListener(new ActionListener() {//change locker when select
            @Override
            public void actionPerformed(ActionEvent e) {
                String locker = (String) jc.getSelectedItem();
                if (locker != null && !locker.equals(""))
                    myLocker = LockerFactory.getLocker(locker,LockerFactory.CHUNKING.ROLLING);
                JlistSetElement(myLocker);
            }
        });
    }
    //reset combolist
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
        retrieve.addActionListener(ActionEvent->{//connect retrieve function
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = jfc.showDialog(new JLabel(), "Choose");
            File file = jfc.getSelectedFile();
            if (result == jfc.APPROVE_OPTION) {
                if (jl.getSelectedValue() != null) {
                    myLocker.retrieveFile(jl.getSelectedValue(), file.getAbsolutePath());
                    jta.setText("Retrieve File Successfully");
                } else
                    jta.setText("File does not exist");
            }
        });
        deletion.addActionListener(ActionEvent->{//connect deletion function
            if (null != jl.getSelectedValue()){
                myLocker.deleteFile(jl.getSelectedValue());
                JlistSetElement(myLocker);
                jta.setText("Delete file successfully");
                //System.out.println("123");
            }
        });
        menu.add(retrieve);
        menu.add(deletion);
    }

    public void framInit(){
        frame.setBounds(700, 300, 300, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // set exit function
        frame.add(AFB);  // add button to box
        frame.add(ALB);
        frame.add(jt2);
        frame.add(jt);
        frame.add(RB);
//        frame.add(jl);
        frame.add(sp1);
        frame.add(sp2);
        frame.add(jc);
        frame.add(ExportButton);
        frame.add(ImportButton);
//        frame.add(jta);
        frame.setVisible(true);

    }
    public void AFBInit(){ // add file button
        AFB.setBounds(160, 60, 120, 50);
        AFB.addActionListener(ActionEvent -> {       // choose file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = jfc.showDialog(new JLabel(), "Choose");
            if (result == jfc.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                if (myLocker.containsFile("/"+file.getName()))
                    jta.setText("Filename already exists");
                else  {
                    myLocker.addFile(file.getAbsolutePath(), "");
                    jta.setText("Add file successfully");
                    JlistSetElement(myLocker);

                }
            }
        });
    }

    public void jtInit(){ // locker text
        jt.setText("default");
        jt.setBounds(10, 10, 150, 50);
        jt.setFont(new Font("default", Font.BOLD, 18));
    }

    public void ALBInit(){ // add locker button
        ALB.setBounds(160, 10, 120, 50);
        ALB.addActionListener(ActionEvent -> {
            System.out.println("text:" + jt.getText());
            if (!jt.getText().equals(""))
                myLocker = LockerFactory.getLocker(jt.getText(), LockerFactory.CHUNKING.ROLLING);
            jcReset();
        });
    }

    public void jt2Init(){ // searching text
        jt2.setBounds(160, 110, 120, 50);
    }

    public void jlInit(){ // file list
//        jl.setBounds(10, 90, 150, 150);
        myLocker = LockerFactory.getLocker((String)jc.getSelectedItem(), LockerFactory.CHUNKING.ROLLING);
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

    public void sp1Init(){ // ScrollPane for file list
        sp1.setBounds(10, 90, 150, 150);
        sp1.setViewportView(jl);

    }

    public void ImportExportBtnInit() {
        ExportButton.setBounds(10, 250, 120, 50);
        ImportButton.setBounds(160, 250, 120, 50);
        ExportButton.addActionListener(ActionEvent -> {       // choose file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = jfc.showDialog(new JLabel(), "Choose");
            if (result == jfc.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                String lockerName = (String)jc.getSelectedItem();
                FileIO.exportLocker(lockerName, file.getAbsolutePath());
            }
        });
        ImportButton.addActionListener(ActionEvent -> {       // choose file
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = jfc.showDialog(new JLabel(), "Choose");
            if (result == jfc.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                FileIO.importLocker(file.getAbsolutePath());
                jcReset();
            }
        });
    }

    public void RBInit(){ // SubString Search button
        RB.setBounds(160, 160, 120, 50);
        RB.addActionListener(ActionEvent -> {       // retrieve file
            myLocker = LockerFactory.getLocker((String)jc.getSelectedItem(), LockerFactory.CHUNKING.ROLLING);
            result = myLocker.SSS(jt2.getText());
            StringJoiner sj = new StringJoiner("\n");
            sj.add("Substring found in:");
            for (String filename:result){
                sj.add(filename+";");
            }
            jta.setText(sj.toString());
        });
    }

    public void jtaInit(){  // info text area
//        jta.setBounds(10,320,270,150);
        jta.setEditable(false);
        jta.setColumns(20);
        jta.setLineWrap(true);
    }

    public void sp2Init(){ // ScrollPane for info text area
        sp2.setBounds(10,320,270,150);
        sp2.setViewportView(jta);
    }

}
package Main;

import Engine.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.List;
import java.util.Arrays;

public class Clicker implements ActionListener{
    private JFrame frame;
    private JButton gain;
    private JButton quit;
    private JButton reset;
    private JButton autoCount;
    private JPanel topPanel;
    private JLabel label;
    private JTextField textPane;
    private JTextArea textArea;
    private JList list;
    private JMenuBar mainMenu;
    private JMenu menu;
    private JMenu subMenu1;
    private JMenu subMenu2;
    private FileCipher fc;

    private boolean autoCounter = false;
    private int score = 0;

// This handles the view (Gui)
    public Clicker(){
        fc = new FileCipher("DES");
        frame = new JFrame();
        quit = new JButton("Quit");
        gain = new JButton("Gain");
        autoCount = new JButton("Auto");
        autoCount.setBackground(new Color(255, 0, 0));
        reset = new JButton("Reset");
        mainMenu = new JMenuBar();
        menu = new JMenu("Menu");
        subMenu1 = new JMenu("Save");
        subMenu2 = new JMenu("Load");
        mainMenu.add(menu);
        menu.add(subMenu1);
        menu.add(subMenu2);
        JMenuItem saveFile1 = new JMenuItem("Save File 1");
        JMenuItem saveFile2 = new JMenuItem("Save File 2");
        JMenuItem loadFile1 = new JMenuItem("Load File 1");
        JMenuItem loadFile2 = new JMenuItem("Load File 2");
        subMenu1.add(saveFile1);
        subMenu1.add(saveFile2);
        subMenu2.add(loadFile1);
        subMenu2.add(loadFile2);
        saveFile1.addActionListener(this);
        saveFile2.addActionListener(this);
        loadFile1.addActionListener(this);
        loadFile2.addActionListener(this);
        quit.addActionListener(this);
        gain.addActionListener(this);
        autoCount.addActionListener(this);
        reset.addActionListener(this);

        topPanel = new JPanel();
        label = new JLabel("Score: ");
        textPane = new JTextField(15);
        textPane.setText(score+"");
        label.setLabelFor(textPane);
        topPanel.add(mainMenu, BorderLayout.LINE_START);
        topPanel.add(label, BorderLayout.CENTER);
        topPanel.add(textPane, BorderLayout.LINE_END);
        frame.add(topPanel, BorderLayout.PAGE_START);

        textArea = new JTextArea("Text",30,50);
        String[] listData = {"Increase 1","Increase 10","Increase 100","Increase 1000",
        "","Decrease 1","Decrease 10","Decrease 100","Decrease 1000"};
        list = new JList<String>(listData);
        list.setSelectedIndex(0);
        frame.add(textArea, BorderLayout.CENTER);
        frame.add(list, BorderLayout.LINE_END);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(gain);
        bottomPanel.add(autoCount);
        bottomPanel.add(reset);
        bottomPanel.add(quit);
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Clicker");
        frame.pack();
        frame.setSize(300,500);
        frame.setVisible(true);
    }

    public int getCount(){
        return score;
    }

    public void setCount(int c){
        score = c;
    }

    public void togCount(){
        if(autoCounter){
            autoCount.setBackground(new Color(255, 0, 0));
        }else{
            autoCount.setBackground(new Color(0, 255, 0));
        }
        autoCounter = !autoCounter;
    }

    public void update(){
        textPane.setText(score+"");
    }
// This handles both save and load (Module)
    private void accessFile(String function, String file){
        ReadWriteTextFileJDK7 saveWrite = new ReadWriteTextFileJDK7();
        try{
            if (function.equals("save")){
                List<String> saveData = Arrays.asList(
                  //new String(fc.encrypt(String.valueOf(getCount())),"UTF-8"),
                  //new String(fc.encrypt(textArea.getText()),"UTF-8")
                  String.valueOf(getCount()),
                  textArea.getText()
                );
                saveWrite.writeSmallTextFile(saveData,"Saves\\"+file+".ClickerGame");
            }else if(function.equals("load")){
                List<String> loadData = saveWrite.readSmallTextFile("Saves\\"+file+".ClickerGame");
                StringBuilder string = new StringBuilder("");
                //setCount(Integer.parseInt(fc.decrypt( (loadData.get(0)).getBytes("UTF-8") )));
                setCount(Integer.parseInt(loadData.get(0)));
                boolean first = true;
                for(String i: loadData){
                    if(first) {
                        first = false;
                        continue;
                    }
                    //string.append(fc.decrypt(i.getBytes("UTF-8"))+"\n");
                    string.append(i+"\n");
                }
                textArea.setText(String.valueOf(string));
            }
        }catch(Exception IO){
            System.out.println("Access: "+IO);
        }
    }
    public static void main(String[] args) {
        Clicker clicker = new Clicker();
        Timer timer = new Timer(1000,clicker);
        timer.start();
    }
// This handles the button actions (Controller)
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() != null){
            String action = e.getActionCommand();
            System.out.println(action);
            if(action.equals("Quit")){
                System.exit(0);
            }else if(action.equals("Auto")){
                togCount();
            }else if(action.equals("Reset")){
                setCount(0);
                textArea.setText("Text");
            }else if(action.equals("Gain")){
                int i = list.getSelectedIndex();
                if(i==1){
                    setCount(getCount()+10);
                }else if(i==2){
                    setCount(getCount()+100);
                }else if(i==3){
                    setCount(getCount()+1000);
                }else if(i==5){
                    setCount(getCount()-1);
                }else if(i==6){
                    setCount(getCount()-10);
                }else if(i==7){
                    setCount(getCount()-100);
                }else if(i==8){
                    setCount(getCount()-1000);
                }else{
                    setCount(getCount()+1);
                }
            }else if(action.equals("Save File 1")){
                accessFile("save","save1");
            }else if(action.equals("Save File 2")){
                accessFile("save","save2");
            }else if(action.equals("Load File 1")){
                accessFile("load","save1");
            }else if(action.equals("Load File 2")){
                accessFile("load","save2");
            }
        }
        if (autoCounter){
            setCount(getCount()+1);
        }
        update();
    }
}
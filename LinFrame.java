package wuziqi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//窗口
public class LinFrame extends JFrame {

    private JPanel toolBar;//工具栏按钮
    private JButton starButton,backButton,exitButton;
    private DrawPanel drawPanel;//棋盘面板

    private JMenuBar menuBar;
    private JMenu sysMenu;
    private JMenuItem starMenuItem,backMenuItem,exitMenuItem;
    private MyListener listener= new MyListener();
    public LinFrame(){

    }
    public void init(){
        listener = new MyListener();
        this.setTitle("五子棋");
        toolBar = new JPanel();
        starButton = new JButton("开始");
        starButton.addActionListener(listener);

        backButton = new JButton("悔棋");
        backButton.addActionListener(listener);

        exitButton = new JButton("退出");
        exitButton.addActionListener(listener);

        drawPanel =new DrawPanel();

        menuBar = new JMenuBar();
        sysMenu = new JMenu("启动");
        starMenuItem = new JMenuItem("开始");
        starMenuItem.addActionListener(listener);

        backMenuItem = new JMenuItem("悔棋");
        backMenuItem.addActionListener(listener);

        exitMenuItem= new JMenuItem("退出");
        exitMenuItem.addActionListener(listener);

        this.setJMenuBar(menuBar);//设置窗口菜单栏
        menuBar.add(sysMenu);
        sysMenu.add(starMenuItem);
        sysMenu.add(backMenuItem);
        sysMenu.add(exitMenuItem);

        toolBar.add(starButton);
        toolBar.add(backButton);
        toolBar.add(exitButton);

        this.setLayout(new BorderLayout());
        this.add(toolBar,BorderLayout.NORTH);
        this.add(drawPanel,BorderLayout.CENTER);

        this.setDefaultCloseOperation(3);
        this.setResizable(false);//能否允许改变窗口大小

        //this.setSize(WIDTH,HEIGHT);
        //窗口出现位置
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-500,(Toolkit.getDefaultToolkit().getScreenSize().height/2)-400);
        pack();
        this.setVisible(true);

    }

    //把功能加入到启动项里面，使其生效
    private class MyListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){
            if (e.getSource()==starButton||e.getSource()==starMenuItem){
                drawPanel.restartGame();
            }
            if (e.getSource()==backButton||e.getSource()==backMenuItem){
                drawPanel.goBack();
            }
            if (e.getSource()==exitButton||e.getSource()==exitMenuItem){
                System.exit(0);
            }
        }
    }

}

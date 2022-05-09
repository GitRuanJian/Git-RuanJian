package wuziqi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public  class DrawPanel extends JPanel implements MouseListener{
    public static final int MARGIN=30;//边距
    public static final int GRID_SPAN=35;//网格间距
    public static final int ROWS=15;
    public static final int COLS=15;

    private int x_index,y_index;

    private boolean isBlack=true;

    private final Chess[] chessList = new Chess[(ROWS+1)*(COLS+1)]; //装棋子的数组

    private  int chessCount=0;//计算棋子的个数

    //游戏是否结束的提示
    private boolean gameOver=false;

    public DrawPanel(){
        super();
        this.setBackground(Color.orange);
        this.addMouseListener(this);
    }
    public void paint(Graphics g){
        super.paint(g);
        //画棋盘
        for (int i = 0; i <=ROWS; i++) {
            //横线，
            g.drawLine(MARGIN,MARGIN+i*GRID_SPAN,MARGIN+COLS*GRID_SPAN,MARGIN+i*GRID_SPAN);
        }

        for (int i = 0; i <=COLS; i++) {
            //竖线
            g.drawLine(MARGIN+i*GRID_SPAN,MARGIN,MARGIN+i*GRID_SPAN,MARGIN+COLS*GRID_SPAN);
        }
        //画棋子
        for (int i = 0; i < chessCount; i++) {
            int xPos = chessList[i].getX()*GRID_SPAN+MARGIN;
            int yPos = chessList[i].getY()*GRID_SPAN+MARGIN;
            g.setColor(chessList[i].getColor());
            g.fillOval(xPos-Chess.DIAMETER/2,yPos-Chess.DIAMETER/2,Chess.DIAMETER,Chess.DIAMETER);
            //落棋后当前棋子出现红色方框
            if (i==chessCount-1){
                g.setColor(Color.red);
                g.drawRect(xPos-Chess.DIAMETER/2,yPos-Chess.DIAMETER/2,Chess.DIAMETER,Chess.DIAMETER);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MARGIN*2+GRID_SPAN*ROWS,MARGIN*2+GRID_SPAN*COLS);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x_index=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
        y_index=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
        System.out.println("("+x_index+","+y_index+")");
        //判断棋子是否可用
        //1.游戏结束不能下子
        if (gameOver){
            return;
        }
        //2.棋如果落在棋盘外则不能下
        if (x_index<0||x_index>COLS||y_index<0||y_index>ROWS){
            return;
        }
        //3.位置上已有棋子，则无法继续下子
        if (FindChess(x_index,y_index)){
            return;
        }

        Chess ch = new Chess(x_index,y_index,isBlack?Color.black:Color.white);

        chessList[chessCount++]=ch;
        System.out.println("棋子的个数：" + chessCount);
        this.repaint();

        //判断赢棋
        if (isWin()){
            String msg= String.format("恭喜您，%s赢了",isBlack?"黑棋":"白棋");
            JOptionPane.showMessageDialog(this,msg);
            gameOver=true;
        }
        isBlack=!isBlack;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //位置上是否有棋子
    private boolean FindChess(int x,int y ){
        for (Chess c :
             chessList) {
            if (c!=null && c.getX()==x && c.getY()==y){
                return true;
            }
        }
        return false;
    }

    //得到棋盘上的棋子
    private Chess getChess(int x,int y,Color color){
        for (Chess c :
             chessList) {
            if (c!=null && c.getX()==x && c.getY()==y && c.getColor()==color){
                return c;
            }
        }
        return null;
    }

    //赢棋：   四个方向
    // search1() 斜向 西南-东北
    // search2() 水平方向
    // search3() 斜向 西北-东南
    // search4() 垂直方向
    private boolean isWin(){
        return search1()||search2()||search3()||search4();
    }
    //斜向 西南-东北
    private boolean search1(){
        int continueCount=1;//连续棋子的个数，初始值为1；
        //斜向上寻找
        for (int x = x_index+1,y=y_index-1; x <=COLS &&y >=0 ; x++,y--) {
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y,c)!=null){
                continueCount++;
            }else{
                break;
            }
        }

        //斜向下寻找
        for (int x= x_index-1,y = y_index+1;x>=0 &&y<=ROWS;x--,y++){
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y,c)!=null){
                continueCount++;
            }else{
                break;
            }
        }


        //五子连珠
        if (continueCount>=5){
            return true;
        }else{
            continueCount=1;
        }
        return false;
    }
    //水平方向，西东
    private boolean search2(){
        int continueCount=1;
        //寻找 西方向
        for (int x = x_index-1; x >=0 ; x--) {
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y_index,c)!=null){
                continueCount++;
            }else {
                break;
            }
        }
        //寻找 东方向
        for (int x = x_index+1; x <=COLS ; x++) {
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y_index,c)!=null){
                continueCount++;
            }else {
                break;
            }
        }
        //五子连珠
        if (continueCount>=5){
            return true;
        }else{
            continueCount=1;
        }
        return false;
      }
      //斜向 西北-东南
    private boolean search3(){
        int continueCount=1;//连续棋子的个数，初始值为1；
        //斜向上寻找
        for (int x = x_index-1,y=y_index-1; x >=0 &&y >=0 ; x--,y--) {
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y,c)!=null){
                continueCount++;
            }else{
                break;
            }
        }

        //斜向下寻找
        for (int x= x_index+1,y = y_index+1;x<=COLS &&y<=ROWS;x++,y++){
            Color c = isBlack?Color.black:Color.white;
            if (getChess(x,y,c)!=null){
                continueCount++;
            }else{
                break;
            }
        }


        //五子连珠
        if (continueCount>=5){
            return true;
        }else{
            continueCount=1;
        }
        return false;
    }

        //垂直方向，上下
        private boolean search4(){
            int continueCount=1;
            //寻找 上
            for (int y = y_index-1; y >=0 ; y--) {
                Color c = isBlack?Color.black:Color.white;
                if (getChess(x_index,y,c)!=null){
                    continueCount++;
                }else {
                    break;
                }
            }
            //寻找 下方向
            for (int y = y_index+1; y <=ROWS ; y++) {
                Color c = isBlack?Color.black:Color.white;
                if (getChess(x_index,y,c)!=null){
                    continueCount++;
                }else {
                    break;
                }
            }
            //五子连珠
            if (continueCount>=5){
                return true;
            }else{
                continueCount=1;
            }
            return false;
    }
    //重新开始游戏
    public void restartGame(){
        //重置界面
        for (int i = 0; i < chessList.length; i++) {
            chessList[i]=null;
        }
        //恢复游戏相关的变量
        isBlack=true;
        gameOver=false;
        chessCount=0;

        this.repaint();
    }
    //悔棋
    public void goBack(){
        //棋盘中没棋子时不能悔棋
        if (chessCount==0){
            return;
        }
        chessList[chessCount-1]=null;
        chessCount--;

        if (chessCount>0){
            x_index=chessList[chessCount-1].getX();
            y_index=chessList[chessCount-1].getY();
        }
        isBlack=!isBlack;

        this.repaint();
    }
}




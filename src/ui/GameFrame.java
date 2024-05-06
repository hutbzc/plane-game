package ui;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/12
 * Time: 18:23
 **/
public class GameFrame extends JFrame{
    public GameFrame(){ 
        //设置窗体标题
        setTitle("飞机大战");
        //设置窗体大小
        setSize(512,768);
        //设置位置居中（null表示相对左上角居中）
        setLocationRelativeTo(null);
        //设置不允许玩家改变窗体大小
        setResizable(false);
        //设置窗口关闭时自动停止程序
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //主函数
    public static void main(String[] args){
        //创建窗体
        GameFrame frame=new GameFrame();

        //创建面板
        GamePanel panel=new GamePanel(frame);

        //将面板加入到窗体中
        frame.add(panel);

        panel.action();

        //设置面板的可见性
        frame.setVisible(true);
    }
}

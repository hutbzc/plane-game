package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Zengc
 * Date: 2020/11/12
 * Time: 18:44
 **/
//面板类
public class GamePanel extends JPanel {
    BufferedImage bg;                        //背景
    Hero hero=new Hero();
    /**
     *  使用集合代表 敌机大本营
     *  不用数组，是因为不能提前知道大本营中敌机的数量
     *  若用数组，还需要扩容
     * 创建的敌机都会放到这个集合中
     * 绘图时遍历此集合即可
     */
    List<Ep> eps=new ArrayList<Ep>();
    /**
     * 英雄机的弹药库
     * 与敌机大本营类似
     */
    ArrayList<Fire> fs=new ArrayList<Fire>();
    int  score;                             //得分
    int power=1;                            //火力
    boolean gameover=false;                 //游戏开关

    //创建面板
    public GamePanel(GameFrame frame){

        //设置背景颜色
        setBackground(Color.black);

        //App是工具类，专门用来获取图片的，参数是相对路径
        bg=App.getImg("/img/bg1.jpg");

        /**
         * 鼠标监听事件
         *      鼠标移动事件mouseMoved(MouseEvent e){
         *         相对应函数
         *      }
         *      其他监听事件的格式类似
         *          鼠标单击事件mouseClicked()
         *          鼠标按下去的事件mousePressed()
         *          鼠标移入游戏界面的事件mouseEntered()
         *          鼠标移出游戏界面的事件mouseExited()
         */
        MouseAdapter adapter=new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                //获取鼠标的位置
                int mx=e.getX();
                int my=e.getY();

                //游戏未结束时，调用函数，将英雄机移动到鼠标位置
                if(!gameover)
                    hero.moveToMouse(mx,my);

                //重画，刷新一下
                repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {

                /**
                 * 游戏结束时，鼠标点击画面，重新开始新游戏
                 * 恢复hp
                 * 游戏结束标志置假
                 * 得分清零
                 * 敌机大本营清零
                 * 弹药库清零
                 * 刷新，重画
                 */
                if(gameover){
                    hero = new Hero();
                    gameover=false;
                    score=0;
                    eps.clear();
                    fs.clear();
                    power=1;
                    repaint();
                }
            }
        };

        //将适配器加入到监听器中
        addMouseListener(adapter);
        addMouseMotionListener(adapter);

        //键盘监听事件
        KeyAdapter kd=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                /**
                 * 游戏未结束时，才可以调用相应函数移动
                 * getKeyCode()函数获得按键的编号
                 * KeyEvent.VK_UP、KeyEvent.VK_DOWN、KeyEvent.VK_LEFT、KeyEvent.VK_RIGHT
                 * 分别表示上下左右键的编号
                 * 移动之后需要重画，刷新页面
                 */
                if(!gameover){
                    if(e.getKeyCode()==KeyEvent.VK_UP){
                        hero.moveUp();
                    }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                        hero.moveDown();
                    }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                        hero.moveLeft();
                    }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                        hero.moveRight();
                    }
                }
                repaint();
            }
        };

        //同样，需要将适配器加入到监听器中
        frame.addKeyListener(kd);
    }

    public void action(){
        /**
         * 创建并启动一个线程，控制游戏场景中活动的物体
         * 固定格式
         * new Thread(){public void run(){..线程需要做的事..}}.start();
         */
        new Thread(){
            public void run(){
                while(true){
                    if(!gameover){
                        epEnter();      //敌机入场
                        epMove();       //敌机向下移动
                        shoot();        //英雄机发射子弹
                        firemove();     //子弹移动
                        shootEp();      //
                        hit();
                        repaint();      //刷新
                    }
                    try {
                        Thread.sleep(10);           //暂停10ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //英雄机被敌机击中
    private void hit() {
        /**
         * 遍历敌机大本营
         * 若敌机与英雄机相撞
         * 则将相撞的英雄机删除，
         *       英雄机hp-1
         *       得分-10(最低为0)
         *       火力回归最初水平
         *       若血量为0，则游戏结束
         */
        for (int i = 0; i < eps.size(); i++) {
            Ep ep = eps.get(i);
            if(ep.hitBy(hero)){
                eps.remove(ep);
                hero.hp--;
                score-=10;
                if(score<0)
                    score=0;
                power=1;
                if(hero.hp<=0)
                    gameover=true;
            }
        }
    }

    //遍历敌机大本营
    private void shootEp() {
        for (int i = 0; i < fs.size(); i++) {
            Fire fire=fs.get(i);
            bang(fire);
        }
    }

    //敌机被击中后
    private void bang(Fire fire){
        for (int i = 0; i < eps.size(); i++) {
            Ep ep=eps.get(i);

            //如果击中目标
            if(ep.shootBy(fire)){

                //敌机血量下降
                ep.hp--;

                //如果敌机的血量为零，则将此敌机从大本营删除，并加分
                if(ep.hp==0){

                    //如果被击毁的是14号敌机，那么英雄机可以加血或者升级武器
                    if(ep.type==14){
                        if(hero.hp<=5)
                            hero.hp++;
                        else if(power<=3)
                            power++;
                    }

                    eps.remove(ep);
                    score+=10;
                }
                //将发生碰撞的子弹从子弹库中删去
                fs.remove(fire);
            }
        }
    }

    //用于控制子弹发射频率
    int fireindex;
    //英雄机发射子弹
    private void shoot() {

        //GamePanel类action方法中的while循环10次，发射一次子弹
        if(fireindex%5==0){

            //火力为1，英雄机正中间发射子弹
            if(power==1) {
                Fire fire2 = new Fire(hero.x + 33, hero.y - 20);       //中间弹道子弹的起始位置应在靠前方一点
                fs.add(fire2);
            }else if(power==2){

             //火力为2，英雄机两侧发射子弹
                Fire fire1=new Fire(hero.x+11,hero.y);
                fs.add(fire1);
                Fire fire3=new Fire(hero.x+58,hero.y);
                fs.add(fire3);
            }else{

            //火力为3，三弹道一起发射子弹
                Fire fire1=new Fire(hero.x+11,hero.y);
                fs.add(fire1);
                Fire fire2=new Fire(hero.x+33,hero.y-20);
                fs.add(fire2);
                Fire fire3=new Fire(hero.x+58,hero.y);
                fs.add(fire3);
            }
        }
        fireindex++;
    }

    //子弹移动
    private void firemove(){

        //获取子弹库中每发子弹，并调用move函数，进行向下移动
        for (int i = 0; i < fs.size(); i++) {
            Fire fire=fs.get(i);
            fire.move();
        }
    }

    //飞机移动（与子弹移动类似）
    private void epMove() {
        for (int i = 0; i < eps.size(); i++) {
            Ep ep=eps.get(i);
            ep.move();
        }
    }

    int index;      //用于控制敌机进场频率（与子弹发射频率类似）

    //飞机进场
    private void epEnter(){
        if(index%20==0){
            Ep ep=new Ep();
            eps.add(ep);
        }
        index++;
    }

    /**
     * 绘图
     *  画笔Graphics
     *      super.paint(g);
     */
    @Override
    public void paint(Graphics g) {
        //Graphics g 画笔
        super.paint(g);

        //用画笔画图g.drawImage(图片,位置（横坐标）,位置（纵坐标）,null)

        //画背景及英雄机
        g.drawImage(bg,0,0,null);
        g.drawImage(hero.img,hero.x,hero.y,hero.h,hero.w,null);

        //设置字体颜色、大小等
        g.setColor(Color.WHITE);
        g.setFont(new Font("楷体",Font.BOLD,20));

        //用画笔写字
        g.drawString("分数："+score,10,30);
        g.drawString("血量：",240,30);

        //绘画英雄机
        for(int i = 0; i < hero.hp; i++) {
            g.drawImage(hero.img,300+35*i,5,30,30,null);
        }

        //绘画敌机
        for(int i=0;i<eps.size();i++){
            Ep ep = eps.get(i);
            g.drawImage(ep.img,ep.x,ep.y,ep.h,ep.w,null);
        }

        //绘画子弹
        for(int i=0;i<fs.size();i++) {
            Fire fire = fs.get(i);
            g.drawImage(fire.img, fire.x, fire.y, fire.h, fire.w, null);
        }

        //游戏结束时，绘画结束界面
        if(gameover){
            g.setColor(Color.RED);
            g.setFont(new Font("楷体",Font.BOLD,40));
            g.drawString("我大意了啊~",150,300);
            g.setColor(Color.CYAN);
            g.setFont(new Font("楷体",Font.BOLD,20));
            g.drawString("单击任意位置，即可重新开始游戏。",80,350);
        }
    }
}

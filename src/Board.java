import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener{
int height=400;
int width=400;
int maxdots=1600;
int dotsize=10;
int dots;
int[] x=new int[maxdots];
int[] y=new int[maxdots];
int applex;
int appley;
Image body,head,apple;
int delay=150;
Timer timer;
boolean left=true;
boolean right=false;
boolean up=false;
boolean down=false;
boolean ingame=true;
Board(){
    TAdpter tadpter=new TAdpter();
    addKeyListener(tadpter);
    setFocusable(true);
    setPreferredSize(new Dimension(width,height));
    setBackground(Color.BLACK);
    initgame();
    loadImages();
}
//intializing the snake's position
public void initgame(){
    dots=3;
    x[0]=50;
    y[0]=50;
    for(int i=1;i<dots;i++){
        x[i]=x[0]+dotsize*i;
        y[i]=y[0];
    }
    //intializing the apple's position
    locateapple();
    timer=new Timer(delay,this);
    timer.start();
}
//loading images from resources to icons
public void loadImages(){
    ImageIcon headicon=new ImageIcon("src/resources/head.png");
    head=headicon.getImage();
    ImageIcon doticon=new ImageIcon("src/resources/dot.png");
    body=doticon.getImage();
    ImageIcon appelicon=new ImageIcon("src/resources/apple.png");
    apple=appelicon.getImage();
}
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    public void doDrawing(Graphics g){
    if(ingame){
        g.drawImage(apple,applex,appley,this);
        for(int i=0;i<dots;i++){
            if(i==0){
                g.drawImage(head,x[0],y[0],this);
            }
            else{
                g.drawImage(body,x[i],y[i],this);
            }
        }
    }
    else {
        gameover(g);
        timer.stop();
    }
    }
    public void locateapple(){
        applex=((int)(Math.random()*39))*10;
        appley=((int)(Math.random()*39))*10;
    }
    @Override
    public void actionPerformed(ActionEvent ev){
        if(ingame){
            checkeating();
            checkcollision();
            move();
        }
        repaint();
    }
    public void move(){
        for(int i=dots-1;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        if(left){
            x[0]-=dotsize;
        }
        if(right){
            x[0]+=dotsize;
        }
        if(up){
            y[0]-=dotsize;
        }
        if(down){
            y[0]+=dotsize;
        }
    }
    private class TAdpter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ev){
            int key=ev.getKeyCode();
            if(key==KeyEvent.VK_LEFT&&!right){
                left=true;
                up=false;
                down=false;
            }
            if(key==KeyEvent.VK_RIGHT&&!left){
                right=true;
                up=false;
                down=false;
            }
            if(key==KeyEvent.VK_UP&&!down){
                left=false;
                up=true;
                right=false;
            }
            if(key==KeyEvent.VK_DOWN&&!up){
                left=false;
                right=false;
                down=true;
            }
        }
    }
    public void checkeating(){
        if(applex==x[0]&&appley==y[0]){
            dots++;
            locateapple();
        }
    }
    public void checkcollision(){
        if(x[0]<0||x[0]>=width||y[0]<0||y[0]>=height){
            ingame=false;
        }
        for(int i=1;i<dots;i++){
            if(x[0]==x[i]&&y[0]==y[i]){
                ingame=false;
            }
        }
    }
    public void gameover(Graphics g){
        String msg="Game Over";
        int score=(dots-3)*10;
        String scoremsg="Score:"+Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, (height / 2)-10);
        g.drawString(scoremsg, (width - metr.stringWidth(scoremsg)) / 2,(height / 2)+10 );
    }
}

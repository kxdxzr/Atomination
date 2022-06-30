import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

public class AtominationGUI extends PApplet {

    public AtominationGUI() {
        
    }

    public void mouseClicked(MouseEvent event) {
        int x=event.getX();
        int y=event.getY();
        x=x/64;
        y=y/64;
        int[] buf={x,y};
        Atomination.place(buf,false);
    }

    public void setup() {
        frameRate(60);
        int[] buf={2,10,6};
        Atomination.start(buf,false);
    }

    public void settings() {
        /// DO NOT MODIFY SETTINGS
        size(640, 384);
    }

    public void draw() {
      PImage title=loadImage("./assets/tile.png");
      PImage blue1=loadImage("./assets/blue1.png");
      PImage blue2=loadImage("./assets/blue2.png");
      PImage blue3=loadImage("./assets/blue3.png");
      PImage green1=loadImage("./assets/green1.png");
      PImage green2=loadImage("./assets/green2.png");
      PImage green3=loadImage("./assets/green3.png");
      PImage purple1=loadImage("./assets/purple1.png");
      PImage purple2=loadImage("./assets/purple2.png");
      PImage purple3=loadImage("./assets/purple3.png");
      PImage red1=loadImage("./assets/red1.png");
      PImage red2=loadImage("./assets/red2.png");
      PImage red3=loadImage("./assets/red3.png");
      PImage[] reds={red1,red2,red3};
      PImage[] blues={blue1,blue2,blue3};
      PImage[] greens={green1,green2,green3};
      PImage[] purples={purple1,purple2,purple3};
      PImage[][] all={reds,greens,purples,blues};
      background(0,0,255);
      for (int y=0;y<6;y++){
        for (int x=0;x<10;x++){
          int excX=x*64;
          int excY=y*64;
          image(title,excX,excY,64,64);
        }
      }
      for (int y=0;y<6;y++){
        Grid[] buf=Atomination.gameBoard[y];
        for (int x=0;x<10;x++){
          Grid cur=buf[x];
          Player curPla=cur.getOwner();
          if (curPla==null){
            continue;
          }
          String curcol=curPla.getColour();
          int gridNumber=cur.getAtom();
          int excX=x*64;
          int excY=y*64;
          int bufff=0;
          for (int i=0;i<4;i++){
            String curp=Atomination.players[i];
            if (curp.equalsIgnoreCase(curcol)){
              bufff=i;
              break;
            }
          }
          PImage target=all[bufff][gridNumber-1];
          image(target,excX,excY,64,64);
        }
      }
    }

    public static void go() {
        AtominationGUI.main("AtominationGUI");
    }
    public static void main(String[] args){
        go();    
    }

}

import java.util.Scanner;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;
import java.io.*;

interface commandZero {
	void apply();
}
interface commandFile{
	void apply(String fileName);
}

public class Atomination {

////////////////////////////DATA//////////////////////////////////////////////
	private static boolean checkneg=true;
	private static boolean started=false;
	public static int playerNumber=0; //for the use in Grid.java
	public static final String[] players={"Red", "Green", "Purple", "Blue"};
	public static Player[] playerType;
	public static Grid[][] gameBoard; //for the use in Grid.java
	private static int curPlayer=0;
	public static int width=0;
	public static int height;
	private static final String Help=
		"HELP	displays this help message\n"+
		"QUIT	quits the current game\n"+
		"\n"+
		"DISPLAY	draws the game board in terminal\n"+
		"START	<number of players> <width> <height> starts the game\n"+	
		"PLACE	<x> <y> places an atom in a grid space\n"+
		"UNDO	undoes the last move made\n"+
		"STAT	displays game statistics\n"+
		"SAVE	<filename> saves the state of the game\n"+
		"LOAD	<filename> loads a save file";
	public static boolean gameStop=false;
	private static String topBottom="+";
	private static ArrayList<int[]> undoList=new ArrayList<>();
	public static boolean firstRound=true;
	public static int turn=-1;
////////////////////////////DATA//////////////////////////////////////////////

////////////////////////////PRINT/////////////////////////////////////////////
	private static void invalidCommand(){
		System.out.println("Invalid Command");
	}
	private static void tooManyArguments() {
		System.out.println("Too Many Arguments");
	}
	private static void missingArgument() {
		System.out.println("Missing Argument");
	}
	private static void invalidcommandarguments(){
		System.out.println("Invalid command arguments");
	}
	private static void gameNotInProgress(){
		System.out.println("Game Not In Progress");
	}
	private static void printcurplayer(){
		System.out.println(players[curPlayer]+"'s Turn");
	}
	private static void invalidCoordinates(){
		System.out.println("Invalid Coordinates");
	}
	private static void cannotPlaceAtomHere(){
		System.out.println("Cannot Place Atom Here");
	}
	private static void  cannotUndo(){
		System.out.println("Cannot Undo");
	}
	private static void fileAlreadyExists(){
		System.out.println("File Already Exists");
	}
	private static void gameSaved() {
		System.out.println("Game Saved");
	}
	private static void cannotLoadSave() {
		System.out.println("Cannot Load Save");
	}
	private static void restartApplicationToLoadSave(){
		System.out.println("Restart Application To Load Save");
	}
	private static void gameLoaded() {
		System.out.println("Game Loaded");
	}
////////////////////////////PRINT/////////////////////////////////////////////

////////////////////////////START/////////////////////////////////////////////
	public static void startChecker(String[] com){
		if (started){
			invalidCommand();
			return;
		}
		else if (com.length<4){
			missingArgument();
			return;
		}
		else if (com.length>4){
			tooManyArguments();
			return;
		}
		int[] val=genVal(com,true);
		if (!checkneg){
			invalidcommandarguments();
		}
		else if (val[0]<2 || val[0]>4) {
			invalidcommandarguments();
		} 
		else {
			start(val,false);
		}
	}
	
	public static void start(int[] val, boolean load){
		playerNumber=val[0];
		width=val[1];
		height=val[2];
		/*
		**generate gameboard
		*/
		gameBoard=new Grid[height][width];
		for (int i=0;i<height;i++){
			for (int j=0;j<width;j++){
				Grid cur = new Grid(j,i);
				if (i==0){
					cur.minLimit();
				}
				if (j==0){
					cur.minLimit();
				}
				if (i==height-1){
					cur.minLimit();
				}
				if (j==width-1){
					cur.minLimit();
				}
				gameBoard[i][j]=cur;
			}
		}
		/*
		**generate players
		*/
		playerType=new Player[playerNumber];
		for (int i=0;i<playerNumber;i++){
			Player cur=new Player(players[i]);
			playerType[i]=cur;
		}
		/*
		**generate top&bottom of the eage of the game board
		*/
		for (int i=0;i<3*width-1;i++){
			topBottom=topBottom+"-";
		}
		topBottom=topBottom+"+";
		if (!load){
			System.out.println("Game Ready");
			printcurplayer();
		}
		started=true;
	}
	
	public static int[] genVal(String[] com, boolean start){
		int j=com.length;
		int[] val=new int[j-1];
		for (int i=1;i<j;i++){
			int cur;
			try {
				cur=Integer.parseInt(com[i]);
				val[i-1]=cur;
			} catch (NumberFormatException e){
				checkneg=false;
				return null;
			}
			if (start) {
				checkneg=cur>1;
			} else {
				checkneg=cur>-1;
			}
			if (! checkneg) {
				return null;
			}
		}
		return val;
	}
////////////////////////////START/////////////////////////////////////////////

////////////////////////////PLACE/////////////////////////////////////////////
	public static void placeChecker(String[] com){
		if (!started){
			gameNotInProgress();
			return;
		}
		int[] val=genVal(com,false);
		boolean checkCo = checkCoordinates(val);
		if (!checkneg || !checkCo) {
			invalidCoordinates();
		} else {
			place(val,false);
		}
	}
	
	public static boolean checkCoordinates(int[] val){
		int x=val[0];
		int y=val[1];
		if (x<width && y<height){
			return true;
		}
		return false;
	}
	
	public static void place(int[] val,boolean undoing) {
		int x=val[0];
		int y=val[1];
		Grid curGrid=gameBoard[y][x];
		Player cur=playerType[curPlayer];
		if (curGrid.getOwner() ==cur || curGrid.getOwner() ==null) {
			int[] bufStep={x,y};
			undoList.add(bufStep);
			curGrid.addAtom(cur);
			if (gameStop){
				System.out.println(players[curPlayer]+" Wins!");
				return;
			}
			curPlayer++;
			if (curPlayer==-1) {
				curPlayer=playerNumber;
			}
			if (curPlayer==playerNumber){
				curPlayer=0;
				firstRound=false;
			}
			if (playerType[curPlayer].getGrids()==0 && !firstRound) {
				curPlayer++;
			}
			turn++;
			if (!undoing){
				printcurplayer();
			}
		} else {
			cannotPlaceAtomHere();
		}
	}
////////////////////////////PLACE/////////////////////////////////////////////

////////////////////////////UNDO//////////////////////////////////////////////
	public static void undo(int turnTo){
		if (turn == -1){
			cannotUndo();
			return;
		}
		turn=-1;
		gameStop=false;
		firstRound=true;
		curPlayer=0;
		ArrayList<int[]> undoListBuff=new ArrayList<>();
		for (int[] cur : undoList){
			undoListBuff.add(cur);
		}
		undoList.clear();
		for (Grid[] buf : gameBoard) {
			for (Grid cur : buf) {
				cur.undoStarter();
			}
		}
		for (Player cur : playerType) {
			cur.undoBegineer();
		}
		for (int i=0;i<turnTo+1;i++){
			int[] curStep=undoListBuff.get(i);
			place(curStep,true);
		}
		printcurplayer();
	}
////////////////////////////UNDO//////////////////////////////////////////////

////////////////////////////STAT//////////////////////////////////////////////
	private static void stat(){
		if (!started){
			gameNotInProgress();
			return;
		}
		for (int i=0;i<playerNumber;i++){
			Player cur=playerType[i];
			String colour=cur.getColour();
			int gridsOwned=cur.getGrids();
			System.out.println("Player "+colour+":");
			if (gridsOwned==0 && !firstRound){
				System.out.println("Lost");
			} else{
				System.out.println("Grid Count: "+gridsOwned);
			}
			if (i<playerNumber-1) {
				System.out.println();
			}
		}
	}
////////////////////////////STAT//////////////////////////////////////////////

///////////////////////////DISPLAY////////////////////////////////////////////
	public static void display(){
		System.out.println();
		System.out.println(topBottom);
		for (Grid[] buf : gameBoard){
			for (Grid cur : buf){
				System.out.print("|");
				Player curGamer=cur.getOwner();
				if (curGamer != null){
					int curAtoms=cur.getAtom();
					String playerColour=curGamer.getColour();
					System.out.print(playerColour.charAt(0));
					System.out.print(curAtoms);
				} else {
					System.out.print("  ");
				}
			}
			System.out.println("|");
		}
		System.out.println(topBottom);
	}
///////////////////////////DISPLAY////////////////////////////////////////////
	
////////////////////////////SAVE//////////////////////////////////////////////
	private static void save(String fileName) {
		if (!started){
			gameNotInProgress();
			return;
		}
		File f = new File(fileName);
		boolean exists = f.exists();
		if (exists){
			fileAlreadyExists();
			return;
		}
		try {
			FileOutputStream output = new FileOutputStream(f);
			int[] buff={width,height,playerNumber};
			byte zero = (byte) 0;
			for (int cur : buff) {
				byte buffer = (byte)cur;
				output.write(buffer);
			}
			for (int[] cur : undoList) {
				byte buffer = (byte)cur[0];
				output.write(buffer);
				buffer = (byte)cur[1];
				output.write(buffer);
				output.write(zero);
				output.write(zero);
			}
			output.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameSaved();
	}
////////////////////////////SAVE//////////////////////////////////////////////

////////////////////////////LOAD//////////////////////////////////////////////
	private static void load(String fileName){
		if (started) {
			restartApplicationToLoadSave();
		}
		try {
			FileInputStream f = new FileInputStream(fileName);
			int bufWidth=f.read();
			int bufHeight=f.read();
			int bufPlayerNumber=f.read();
			int[] buf = {bufPlayerNumber,bufWidth,bufHeight};
			start(buf,true);
			while (f.available()>0) {
				int x=f.read();
				int y=f.read();
				int[] val={x,y};
				f.read();
				f.read();
				place(val,true);
			}
			f.close();
		} catch (FileNotFoundException e) {
			cannotLoadSave();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameLoaded();
	}
	
////////////////////////////LOAD//////////////////////////////////////////////

////////////////////////////QUIT//////////////////////////////////////////////
	public static void quit(){
		checkneg=true;
		started=false;
		playerNumber=0;
		curPlayer=0;
		width=0;
		height=0;
		gameStop=false;
		topBottom="+";
		undoList=new ArrayList<>();
		firstRound=true;
		turn=-1;
		System.out.println("Bye!");
	}
////////////////////////////QUIT//////////////////////////////////////////////

////////////////////////////main//////////////////////////////////////////////
////////////////////////////main//////////////////////////////////////////////
////////////////////////////main//////////////////////////////////////////////
	public static void main(String[] args) {
		//Your game starts here!
		Scanner keyboard=new Scanner(System.in);
		HashMap<String, commandZero> operationsZero = new HashMap<>();
		HashMap<String, commandFile> operationsFile = new HashMap<>();
		operationsZero.put("HELP", () -> System.out.println(Help));
		operationsZero.put("DISPLAY", () -> display());
		operationsZero.put("STAT", () -> stat());//google method reference;
		operationsFile.put("SAVE", (String fileName) -> save(fileName));
		operationsFile.put("LOAD", (String fileName) -> load(fileName));
		while(true){
			if (gameStop){
				break;
			}
			String comm=keyboard.nextLine();
			String[] com=comm.split(" ");
			String com0=com[0];
			String com1=com0.toUpperCase();
			if (com0.equalsIgnoreCase("QUIT")) {
				quit();
				break;
			}
			if (com0.equalsIgnoreCase("PLACE")) {
				placeChecker(com);
				if (gameStop){
					break;
				}
			} else if (com0.equalsIgnoreCase("START")) {
				startChecker(com);
			} else if(com0.equalsIgnoreCase("UNDO")){
				undo(turn-1);
			} else if (com.length==1) {
				try {
					operationsZero.get(com1).apply();
				} catch (NullPointerException e){
					invalidCommand();
				}
			} else if (com.length==2) {
				String filename=com[1];
				try {
					operationsFile.get(com1).apply(filename);
				} catch (NullPointerException e) {
					invalidCommand();
				}
			} 
			System.out.println();
		}
	}
}

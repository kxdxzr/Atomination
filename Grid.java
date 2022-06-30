public class Grid {
	private Player owner=null;
	private int atomCount=0;
	private int atomLimit=4;
	private int x;
	private int y;
	
	public Grid(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public void undoStarter(){
		this.owner=null;
		this.atomCount=0;
	}
	
	public void setLimit(int atomLimit){
		this.atomLimit=atomLimit;
	}
	public void minLimit(){
		atomLimit--;
	}
	private void setOwner(Player newOwner){
		if (owner==null){
			newOwner.addGrids();
		} else if (owner != null && newOwner == null) {
			this.owner.minGrids();
		} else if (owner != null && newOwner != null) {
			this.owner.minGrids();
			newOwner.addGrids();
		}
		this.owner=newOwner;
	}
	public Player getOwner() {
		return owner;
	}
	
	private void expend(int y, int x, Player newOwner){
		Atomination.gameBoard[y][x].setOwner(newOwner);
		Atomination.gameBoard[y][x].addAtom(newOwner);
	}
	private void checkStop(){
		if (Atomination.firstRound) {
			return;
		}
		int gamerCounter=0;
		Player buf=new Player(null);
		for (int i=0;i<Atomination.playerNumber;i++){
			if (Atomination.playerType[i].getGrids() != 0) {
				gamerCounter++;
			}
		}
		if (gamerCounter==1) {
			Atomination.gameStop=true;
		}
	}
	public void addAtom(Player newOwner) {
		checkStop();
		if (Atomination.gameStop){
			return;
		}
		atomCount++;
		setOwner(newOwner);
		if (atomCount==atomLimit){
			atomCount=0;
			setOwner(null);
			//addAtom to surroundings
			if (y-1 > -1) {
				expend(y-1,x,newOwner);
			}
			if (Atomination.gameStop){
				return;
			}
			if (x+1 < Atomination.width) {
				expend(y,x+1,newOwner);
			}
			if (Atomination.gameStop){
				return;
			}
			if (y+1 < Atomination.height) {
				expend(y+1,x,newOwner);
			}
			if (Atomination.gameStop){
				return;
			}
			if (x-1 > -1) {
				expend(y,x-1,newOwner);
			}
		}
	}
	public int getAtom(){
		return atomCount;
	}
}

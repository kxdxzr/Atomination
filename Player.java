public class Player {
	private String colour;
	private int gridsOwned;
	
	public void undoBegineer() {
		this.gridsOwned=0;
	}
	
	public Player(String colour){
		this.colour=colour;
	}
	
	public String getColour(){
		return colour;
	}
	public int getGrids(){
		return gridsOwned;
	}
	public void addGrids(){
		gridsOwned++;
	}
	public void minGrids(){
		gridsOwned--;
	}
}

import static org.junit.Assert.*;
import org.junit.Test;

public class AtominationTest {
	@Test
	public void testGenVal() {
		String[] com={"command","2","2","2"};
		int[] val1=Atomination.genVal(com, true);
		int[] buf={2,2,2};
		for (int i=0;i<3;i++){
			assertEquals(val1[i],buf[i]);
		}
		com[0]="command";
		com[1]="2";
		com[2]="0";
		com[3]="2";
		int[] val2=Atomination.genVal(com, true);
		assertEquals(null,val2);
		Atomination.quit();
	}

	@Test
	public void testCheckCoordinates(){
		String[] buff={"Start","2","3","4"};
		Atomination.startChecker(buff);
		int[] val1={2,2};
		boolean buf=Atomination.checkCoordinates(val1);
		assertTrue(buf);
		int[] val2={3,2};
		buf=Atomination.checkCoordinates(val2);
		assertEquals(false,buf);
		Atomination.quit();
	}
	
	@Test
	public void  testConstruction() {
		String[] buf={"Start","2","3","4"};
		Atomination.startChecker(buf);
		assertEquals(2,Atomination.playerNumber);
		assertEquals(3,Atomination.width);
		assertEquals(4,Atomination.height);
		assertEquals(4,Atomination.gameBoard.length);
		assertEquals(3,Atomination.gameBoard[0].length);
		Atomination.quit();
	}
	@Test
	public void testPlace() {
		String[] buf={"Start","2","3","3"};
		Atomination.startChecker(buf);
		String[] com1={"command","0","0"};
		Atomination.placeChecker(com1);
		assertEquals(1,Atomination.gameBoard[0][0].getAtom());
		assertEquals("Red",Atomination.gameBoard[0][0].getOwner().getColour());
		assertEquals(1,Atomination.gameBoard[0][0].getOwner().getGrids());
		String[] com2={"command","1","1"};
		Atomination.placeChecker(com2);
		assertEquals(Atomination.gameBoard[1][1].getAtom(),1);
		assertEquals(Atomination.gameBoard[1][1].getOwner().getColour(),"Green");
		assertEquals(Atomination.gameBoard[1][1].getOwner().getGrids(),1);
		Atomination.placeChecker(com1);
		assertTrue(Atomination.gameBoard[0][0].getAtom()==0);
		assertTrue(Atomination.gameBoard[0][0].getOwner()==null);
		assertTrue(Atomination.gameBoard[0][1].getAtom()==1);
		assertTrue(Atomination.gameBoard[1][0].getAtom()==1);
		assertTrue(Atomination.gameBoard[0][1].getOwner().getColour()=="Red");
		assertTrue(Atomination.gameBoard[1][0].getOwner().getColour()=="Red");
		assertTrue(Atomination.gameBoard[1][0].getOwner().getGrids()==2);
		Atomination.quit();
	}
	@Test
	public void testUndo() {
		String[] buf={"Start","2","3","3"};
		Atomination.startChecker(buf);
		String[] com1={"command","0","0"};
		Atomination.placeChecker(com1);
		Atomination.undo(Atomination.turn-1);
		assertTrue(Atomination.gameBoard[0][0].getAtom()==0);
		assertTrue(Atomination.gameBoard[0][0].getOwner()==null);
		Atomination.placeChecker(com1);
		String[] com2={"command","1","1"};
		Atomination.placeChecker(com2);
		Atomination.placeChecker(com1);
		Atomination.undo(Atomination.turn-1);
		assertEquals(1,Atomination.gameBoard[0][0].getAtom());
		assertEquals("Red",Atomination.gameBoard[0][0].getOwner().getColour());
		assertEquals(1,Atomination.gameBoard[0][0].getOwner().getGrids());
		assertEquals(0,Atomination.gameBoard[0][1].getAtom());
		assertEquals(null,Atomination.gameBoard[0][1].getOwner());
		assertEquals(0,Atomination.gameBoard[1][0].getAtom(),0);
		assertEquals(null,Atomination.gameBoard[1][0].getOwner());
		Atomination.quit();
	}
}

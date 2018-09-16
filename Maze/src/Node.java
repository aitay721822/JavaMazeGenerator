
public class Node {
	private boolean[] wall;
	public boolean isVisted;
	private int X,Y;
	public Node(int x,int y) {
		wall = new boolean[]{true,true,true,true}; //array[0] is up side, array[1] is down side, array[2] is left side, array[3] is right side
		isVisted= false;
		X=x; Y=y;
	}
	
	public int[] getXY() {
		return new int[] {X,Y};
	}
	
	public void setWall(int side,boolean status) {
		wall[side % wall.length] = status;
	}
	
	public void unWall(int side) {
		wall[side % wall.length] = false;
	}
	
	public boolean[] getWall() {
		return wall;
	}
}

import java.util.Stack;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class Generator {
	List<Integer> RandomDirection;
	Node[][] MazePanel;
	int NowPosX,NowPosY,GoalPosX,GoalPosY;
    Point[] Shift = new Point[] {new Point(0,-1),new Point(0,1),new Point(-1,0),new Point(1,0)};
    String[] Str_Shift = new String[] {"top","down","left","right"};
    
    int randindex = 0;
    List<Node> CanVisited = new ArrayList<Node>();
    
	public Generator(int width,int height,int RandomSeed) {
		MazePanel = new Node[width][height];
		Integer[] Sha = getSHA512(String.valueOf(RandomSeed));
		RandomDirection = new LinkedList<>(Arrays.asList(Sha));
		ArrInitial(); /*將MazePanel初始化*/
		setPosition(RandomDirection.get(0),RandomDirection.get(1),height,width);
		Stack<Node> current  = new Stack<Node>();
		MazePanel[NowPosX][NowPosY].setWall(0, false); //將牆上打穿
		current.push(MazePanel[NowPosX][NowPosY]); //推入初始狀態
		
		while(!current.isEmpty()) {
			Node status = current.peek(); //移出初始狀態
			List<Node> getnb = getNeighbor(status,height,width);//取得鄰居
			if(CanVisited.isEmpty()) break;//當MazePanel無節點沒走貴就跳出
			if(getnb.isEmpty()) {
				current.pop();
				MazePanel[status.getXY()[0]][status.getXY()[1]].isVisted = true;
				CanVisited.remove(status);
				continue; //當沒有鄰居時跳過
			}
			
			int index = RandomDirection.get(randindex++ % RandomDirection.size());

			Node RandNeighbor = getnb.get(index % getnb.size());
			
			for(int i=0;i<Shift.length;i++) {
				if( RandNeighbor.getXY()[0]==status.getXY()[0]+Shift[i].X &&
					RandNeighbor.getXY()[1]==status.getXY()[1]+Shift[i].Y) {
					String Direction = Str_Shift[i];
					switch(Direction) {
					case "top":
						status.unWall(0);
						RandNeighbor.unWall(1);
						break;
					case "down":
						status.unWall(1);
						RandNeighbor.unWall(0);
						break;
					case "left":
						status.unWall(2);
						RandNeighbor.unWall(3);
						break;
					case "right":
						status.unWall(3);
						RandNeighbor.unWall(2);
						break;
					}
				}
			}
			
			
			status.isVisted = true;
			CanVisited.remove(status);
			MazePanel[status.getXY()[0]][status.getXY()[1]] = status; //將目前節點標記為走過
			current.push(RandNeighbor);
		}
		MazePanel[GoalPosX][GoalPosY].unWall(1);
	}
	
	public Node[][] returnMaze(){
		if(MazePanel!=null)return MazePanel;
		else return null;
	}
	
	public boolean[][] getMazeResult() {
		if(MazePanel!=null) {
			boolean[][] result = new boolean[2*MazePanel.length + 1][2*MazePanel[0].length + 1 ];
			for(int i=0;i<result.length;i++)
				Arrays.fill(result[i], true);
			
			for(int x=0;x<MazePanel.length;x++) {
				for(int y=0;y<MazePanel[0].length;y++) {
					final Point[][] Moving = new Point[][] {
						{new Point(-1,-1),new Point(0,-1),new Point(1,-1)},
						{new Point(-1,0),new Point(0,0),new Point(1,0)},
						{new Point(-1,1),new Point(0,1),new Point(1,1)},
					};
					Node current = MazePanel[x][y];
					boolean[] wall = current.getWall();
					
					int currentX = 1 + x * 2;
					int currentY = 1 + y * 2;
					
					for(int px=0;px<Moving.length;px++) {
						for(int py=0;py<Moving[0].length;py++) {
							if(Moving[px][py].X !=0 && Moving[px][py].Y != 0) {
								int movingX = currentX + Moving[px][py].X;
								int movingY = currentY + Moving[px][py].Y;
								if(movingX >=0 && movingX < result.length &&
									movingY >=0 && movingY < result[0].length) {
									result[movingX][movingY] = true;
								}
							}
							else result[currentX][currentY] = false;
						}
					}
					
					for(int i=0;i<wall.length;i++) {
						if(!wall[i]) {
							switch(i) {
							case 0:
								result[currentX][currentY - 1] = false;
								break;
							case 1:
								result[currentX][currentY + 1] = false;
								break;
							case 2:
								result[currentX - 1][currentY] = false;
								break;
							case 3:
								result[currentX + 1][currentY] = false;
								break;
							}
						}
					}
					
				}
			}
			return result;
		}
		else return null;
	}
	public List<Node> getNeighbor(Node x,int height,int width) {
		List<Node> result = new ArrayList<Node>();
		int currentX = x.getXY()[0];
		int currentY = x.getXY()[1];
		for(Point xy : Shift) {
			if( currentX+xy.X>=0 && currentX+xy.X < width &&
				currentY+xy.Y>=0 && currentY+xy.Y < height) {
				int neighborX = currentX+xy.X;
				int neighborY = currentY+xy.Y;
				if(!MazePanel[neighborX][neighborY].isVisted) result.add(MazePanel[neighborX][neighborY]);
			}
		}
		return result;
	}
	
	public void setPosition(int rand0,int rand1 ,int height,int width) {
		NowPosX = (rand0 % width); 
		NowPosY = 0;
		GoalPosX = (rand1 % width);
		GoalPosY = height - 1;
	}
	
	public Integer[] getSHA512(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes());	
			byte[] hash = md.digest();
			StringBuffer strHexString = new StringBuffer();
			for(int i=0;i<hash.length;i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length()==1) {
					strHexString.append('0');
				}
				strHexString.append(hex);
			}
	        char[] c = strHexString.toString().toCharArray();
	        return ConvIntArray(c);
	      }
	      catch (NoSuchAlgorithmException e)
	      {
	        e.printStackTrace();
	        return null;
	      }
	}
	
	public Integer[] ConvIntArray(char[] Arr) {
		Integer[] array = new Integer[Arr.length];
		for(int i=0;i<Arr.length;i++) {
			int temp = Arr[i];
			array[i] = temp;
		}
		return array;
	}
	
	public void ArrInitial() {
		try {
			for(int x=0;x<MazePanel.length;x++) {
				for(int y=0;y<MazePanel[x].length;y++) {
					MazePanel[x][y] = new Node(x,y);
					MazePanel[x][y].setWall(0, true);
					MazePanel[x][y].setWall(1, true);
					MazePanel[x][y].setWall(2, true);
					MazePanel[x][y].setWall(3, true);
					CanVisited.add(MazePanel[x][y]);
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}

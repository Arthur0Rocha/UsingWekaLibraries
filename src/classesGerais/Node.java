package classesGerais;


public class Node{

	private int index;
	private double rank;
	
	public Node(int index , double rank) {
		this.index = index;
		this.rank = rank;
	}
	
	public double getRank(){
		
		return this.rank;
		
	}
	
	public int getIndex(){
		
		return this.index;
		
	}

}

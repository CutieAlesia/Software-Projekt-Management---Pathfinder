
public class Main {

	public static void main(String[] args) {
		
		AStar alg = new AStar();
		alg.generateField(10);
		alg.placeStartAndEndNode();
		alg.printField();
		System.out.println();
		alg.start();
		alg.printField();
		
	}
	
}

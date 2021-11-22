import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AStar {
	
	private Node[][] field;
	private Node start;
	private Node end;
	private ArrayList<Node> relevantNodes;
	private ArrayList<Node> visited;
	
	public AStar() {
		this.relevantNodes = new ArrayList<Node>();
		this.visited = new ArrayList<Node>();
	}
	
	public void generateField(int width, int height) {
		Node[][] field = new Node[height][width];
		
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				field[i][j] = new Node(i, j);
			}
		}
		
		this.field = field;
	}
	
	public void generateField(int dimension) {
		generateField(dimension, dimension);
	}
	
	public void placeStartAndEndNode() {
		this.start = getRandomNode();
		this.start.setType(NodeType.START);
		this.end = getRandomNode();
		while(this.end.getType() == NodeType.START) {
			this.end = getRandomNode();
		}
		this.end.setType(NodeType.END);
	}

	public Node getRandomNode() {
		return field[(int) (Math.random() * field.length)][(int) (Math.random() * field[0].length)];
	}
	
	public void start() {
		boolean found = updateNeighbours(this.start);
		if(found) {
			Node node = end;
			while(node != null) {
				node.setTest(true);
				node = node.getPrev();
			}
		} else {
			System.out.println("Es wurde leider kein Weg gefunden!");
		}
	}
	
	private boolean updateNeighbours(Node node) {
		this.visited.add(node);
		boolean found = false;
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				boolean leftInBound = node.getHorIndex() + j >= 0;
				boolean rightInBound = node.getHorIndex() + j < field[0].length;
				boolean topInBound = node.getVertIndex() + i >= 0;
				boolean bottomInBound = node.getVertIndex() + i < field.length;
				
//				System.out.println("i: " + (node.getVertIndex() + i) + ", j: " + (node.getHorIndex() + j));
//				System.out.println("top: " + topInBound);
//				System.out.println("bottom: " + bottomInBound);
//				System.out.println("left: " + leftInBound);
//				System.out.println("right: " + rightInBound);
//				System.out.println();
				
				if(leftInBound && rightInBound && topInBound && bottomInBound) {
					Node neighbour = field[node.getVertIndex() + i][node.getHorIndex() + j];
					int costs = getDistance(node, neighbour) + (node.getCosts() != -1 ? node.getCosts() : 0);
					int estimatedCosts = getDistance(neighbour, end);
					
					if(neighbour.getType() == NodeType.START)
						continue;
					
					if(costs + estimatedCosts < neighbour.getTotalCosts() || neighbour.getCosts() == -1) {
						neighbour.setCosts(costs);
						neighbour.setEstimatedCosts(estimatedCosts);
						neighbour.setPrev(node);
						
						if(neighbour.getType() == NodeType.END)
							found = true;
					}
					
					if(!relevantNodes.contains(neighbour)) {
						relevantNodes.add(neighbour);
					}
				}
			}
		}
		
		printField();
		sortRelevantNodes();
		Node nextToUpdate = getNextNode();
		return found ? true : updateNeighbours(nextToUpdate);
	}
	
	private Node getNextNode() {
		Node node = this.relevantNodes.get(0);
		int idx = 0;
		while(this.visited.contains(node)) {
			node = this.relevantNodes.get(++idx);
		}
		
		return node;
	}
	
	private void sortRelevantNodes() {
		Collections.sort(relevantNodes, new Comparator<Node>() {
		    @Override
		    public int compare(Node node1, Node node2) {
		        return ((Integer) node1.getTotalCosts()).compareTo(node2.getTotalCosts());
		    }
		});
	}
	
	private int getDistance(Node node1, Node node2) {
		int vertDiff = Math.abs(node1.getVertIndex() - node2.getVertIndex());
		int horDiff = Math.abs(node1.getHorIndex() - node2.getHorIndex());
		int diagonales = Math.min(vertDiff, horDiff);
		int straightLines = Math.max(vertDiff, horDiff) - diagonales;
		
		return diagonales * 14 + straightLines * 10;
	}
	
	public void printField() {
		for(int i=0; i<field.length; i++) {
			for(int j=0; j<field[0].length; j++) {
				Node node = field[i][j];
				String ob = Colors.ANSI_BLACK + "[";
				String cb = Colors.ANSI_BLACK + "]";
				String cc = node.getType() == NodeType.NORMAL ? "" : node.getType() == NodeType.START ? Colors.ANSI_GREEN : Colors.ANSI_RED;
				String formCosts = String.format("%03d", node.getCosts());
				String formEstCosts = String.format("%03d", node.getEstimatedCosts());
				String values = node.getCosts() == -1 ? "###, ###" : formCosts + ", " + formEstCosts;
				String content = ob + cc + values + cb;
				String prefix = field[i][j].getTest() ? Colors.ANSI_BLUE_BACKGROUND : Colors.ANSI_WHITE_BACKGROUND;
				System.out.print(prefix + content);
			}
			System.out.println("");
		}
		System.out.println();
	}
}

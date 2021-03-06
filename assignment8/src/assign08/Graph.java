package assign08;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
//import java.util.Queue;

/**
 * 
 * @author Daniel Kopta, SETH POLEVOI and MIKE PHELPS This Graph class acts as a
 *         starting point for your maze path finder. Add to this class as
 *         needed.
 */
public class Graph {

	// The graph itself is just a 2D array of nodes
	private Node[][] nodes;

	// The node to start the path finding from
	private Node start;
	private Node goal;

	// The size of the maze
	private int width;
	private int height;

	private LinkedList<Node> queue = new LinkedList<Node>();

	/**
	 * Constructs a maze graph from the given text file.
	 * 
	 * @param filename - the file containing the maze
	 * @throws Exception
	 */
	public Graph(String filename) throws Exception {
		BufferedReader input;
		input = new BufferedReader(new FileReader(filename));

		if (!input.ready()) {
			input.close();
			throw new FileNotFoundException();
		}

		// read the maze size from the file
		String[] dimensions = input.readLine().split(" ");
		height = Integer.parseInt(dimensions[0]);
		width = Integer.parseInt(dimensions[1]);

		// instantiate and populate the nodes
		nodes = new Node[height][width];
		for (int i = 0; i < height; i++) {
			String row = input.readLine().trim();

			for (int j = 0; j < row.length(); j++)
				switch (row.charAt(j)) {
				case 'X':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isWall = true;
					break;
				case ' ':
					nodes[i][j] = new Node(i, j);
					break;
				case 'S':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isStart = true;
					start = nodes[i][j];
					break;
				case 'G':
					nodes[i][j] = new Node(i, j);
					nodes[i][j].isGoal = true;
					break;
				default:
					throw new IllegalArgumentException("maze contains unknown character: \'" + row.charAt(j) + "\'");
				}
		}
		input.close();
	}

	/**
	 * Outputs this graph to the specified file. Use this method after you have
	 * found a path to one of the goals. Before using this method, for the nodes on
	 * the path, you will need to set their isOnPath value to true.
	 * 
	 * @param filename - the file to write to
	 */
	public void printGraph(String filename) {
		try {
			PrintWriter output = new PrintWriter(new FileWriter(filename));
			output.println(height + " " + width);
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					output.print(nodes[i][j]);
				}
				output.println();
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Traverse the graph with BFS (shortest path to closest goal) A side-effect of
	 * this method should be that the nodes on the path have had their isOnPath
	 * member set to true.
	 * 
	 * @return - the length of the path
	 */
	public int CalculateShortestPath() {
		CalculateShortest(start);

		// if no goal was found
		if (goal == null) {
			return 0;
		}
		return countPath(goal);
	}

	/**
	 * Calculates the shortest path to a goal, given a starting node;
	 * 
	 * @param start Starting node of a graph to find a path to a goal from
	 */
	private void CalculateShortest(Node start) {
		start.visited = true;
		queue.offer(start);

		while (!queue.isEmpty()) {
			// removes current node from the queue
			Node curr = queue.poll();

			// if the current node is the goal, mark it and exit function
			if (curr.isGoal) {
				goal = curr;
				return;
			}

			// update instance variables with non wall neighbors
			makeNeighbors(curr);

			// add all unvisited neighbors to the queue
			for (Node n : curr.neighbors) {
				if(!n.visited) {
					n.visited = true;
					n.cameFrom = curr;
					queue.offer(n);
				}
			}
		}
	}// end CalculateShortest method

	/**
	 * Traverse the graph with DFS (any path to any goal) A side-effect of this
	 * method should be that the nodes on the path have had their isOnPath member
	 * set to true.
	 * 
	 * @return - the length of the path
	 */
	public int CalculateAPath() {
		// call recursive method to find the Goal
		CalculateAPathRecursive(start);

		// if goal wasn't found in last method
		if (goal == null) {
			return 0;
		}
		return countPath(goal);
	}

	/**
	 * Calculates path to the goal from an input node.
	 * 
	 * @param currentNode
	 */
	private void CalculateAPathRecursive(Node currentNode) {
		currentNode.visited = true;

		// if currentNode is the goal, exit this method
		if (currentNode.isGoal) {
			goal = currentNode;
			return;
		}

		// call a method to make an array list of the node's non-wall neighbors
		makeNeighbors(currentNode);

		// check the nodes neighbors for paths
		for (Node next : currentNode.neighbors) {
			if (!next.visited) {
				// tell the next node which node it came from
				next.cameFrom = currentNode;
				// call recursive method on the next node
				CalculateAPathRecursive(next);
			}
		}
	}

	/**
	 * Updates an instance variable of the node's non-wall neighbors
	 * 
	 * @param node node to find the neighbors of
	 */
	private void makeNeighbors(Node node) {
//		// add node below current neighbors if it's not a wall
//		if (!nodes[node.x + 1][node.y].isWall)
//			node.neighbors.add(nodes[node.x + 1][node.y]);
//		// add node above current neighbors if it's not a wall
//		if (!nodes[node.x - 1][node.y].isWall)
//			node.neighbors.add(nodes[node.x - 1][node.y]);
//		// add node right of current neighbors if it's not a wall
//		if (!nodes[node.x][node.y + 1].isWall)
//			node.neighbors.add(nodes[node.x][node.y + 1]);
//		// add node left of current neighbors if it's not a wall
//		if (!nodes[node.x][node.y - 1].isWall)
//			node.neighbors.add(nodes[node.x][node.y - 1]);

		// alternate order
		// add node right of current neighbors if it's not a wall
		if (!nodes[node.x][node.y + 1].isWall)
			node.neighbors.add(nodes[node.x][node.y + 1]);
		// add node left of current neighbors if it's not a wall
		if (!nodes[node.x][node.y - 1].isWall)
			node.neighbors.add(nodes[node.x][node.y - 1]);
		// add node below current neighbors if it's not a wall
		if (!nodes[node.x + 1][node.y].isWall)
			node.neighbors.add(nodes[node.x + 1][node.y]);
		// add node above current neighbors if it's not a wall
		if (!nodes[node.x - 1][node.y].isWall)
			node.neighbors.add(nodes[node.x - 1][node.y]);
	}

	/**
	 * Given a node, this method counts back to the starting node. Also sets the
	 * nodes isOnPath instance variable to true.
	 * 
	 * @param currentNode node to find the path back to startt of
	 * @return int of how many nodes the input node traveled through
	 */
	private int countPath(Node currentNode) {
		currentNode.isOnPath = true;
		// if node is the start of the path
		if (currentNode.cameFrom == null) {
			return 0;
		}
		return 1 + countPath(currentNode.cameFrom);
	}

	/**
	 * @author Daniel Kopta and SETH POLEVOI and MIKE PHELPS A node class to assist
	 *         in the implementation of the graph. You will need to add additional
	 *         functionality to this class.
	 */
	private static class Node {
		// The node's position in the maze
		private int x, y;// rows, columns

		// The type of the node
		private boolean isStart;
		private boolean isGoal;
		private boolean isOnPath;
		private boolean isWall;
		// Data about node's location
		private boolean visited;
		private Node cameFrom;
		private ArrayList<Node> neighbors = new ArrayList<Node>(4);

		public Node(int _x, int _y) {
			isStart = false;
			isGoal = false;
			isOnPath = false;
			x = _x;
			y = _y;
		}

		@Override
		public String toString() {
			if (isWall)
				return "X";
			if (isStart)
				return "S";
			if (isGoal)
				return "G";
			if (isOnPath)
				return ".";
			return " ";
		}
	}

//	public static void main(String[] args) {
//// 		PathFinder.solveMaze("src/assign08/randomSmall.txt",  "src/assign08/solvedGraph.txt",true);
//		PathFinder.solveMaze("src/assign08/bigMaze_multigoal.txt", "src/assign08/solvedGraph.txt", false);
//	}

}

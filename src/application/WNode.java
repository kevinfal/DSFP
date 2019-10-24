package application;

import java.util.ArrayList;

/**
 * This is a modified node class
 * points to an array of nodes as its next
 * 
 * I think I could have actually done this with regular nodes that just point to nodes with lists, but it's too late for that
 * 
 * @author kevin
 *
 * @param <E>
 */
public class WNode<E> {

	private E data;
	private ArrayList<WNode<E>> nextList;


	/**
	 * Constructor
	 */
	public WNode() {
		this.data = null;
		this.nextList = null;
	}
	/**
	 * Constructor with parameter for data
	 * @param data - data
	 */
	public WNode(E data) {
		this.data = data;
		this.nextList = null;
	}
	/**
	 * 
	 * @param data -- value of the node
	 * @param next -- what node it links to
	 */
	public WNode(E data, ArrayList<WNode<E>> next) {
		this.data = data;
		this.nextList = next;
	}
	
	/**
	 * 
	 * @return current data
	 */
	public E getData() {
		return data;
	}
	/**
	 * 
	 * @return the list of nodes that it links to
	 */
	public ArrayList<WNode<E>> getNext(){
		return nextList;
	}

	
	
	/**
	 * 
	 * @param new data to change
	 */
	public void setData(E data) {
		this.data = data;
	}
	/**
	 * 
	 * @param next -- changes what a node links to
	 */
	public void setNext(ArrayList<WNode<E>> next) {
		this.nextList = next;
	}

	
	/**
	 * honestly this is only for debugging purposes
	 * @return node in string form
	 */
	public String toString() {
		/*
		if(next == null) {
		return "[ "+data+" |" +next+"]";
		}
		else {
			return "[ "+data+" |" +next.getData()+"]";
		}
		*/
		return "["+data.toString()+"]";
	}
	


}

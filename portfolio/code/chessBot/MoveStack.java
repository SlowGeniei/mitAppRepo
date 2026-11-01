package ChessGame;


import java.util.*;
/**
 * Custom stack object, implemented using an object array
 * @author Galen Heinze
 * @version 1.0
 * @see StackListener
 */
public class MoveStack {
	Move[] stack;
	int stackSize;
	int stackTop;
	
	List<StackListener> listener = new ArrayList<StackListener>();
	
	public void addListener(StackListener s) {
		listener.add(s);
	}
	
	private void notifyListener() {
		for (StackListener whatever :  listener) {
			whatever.StackModified();
		}
	}
	
	/**
	 * Constructor
	 * @param size The initial size of the stack
	 */
	public MoveStack(int size) {
		stackSize = size;
		stack = new Move[stackSize];
		stackTop = -1;
	}
	
	/**
	 * Push an object onto the stack
	 * @param obj The object to push onto the stack
	 */
	public void push(Move move) {
		if (stackTop == stackSize - 1) {
			for (int i = 0; i < stackSize - 1; i++) {
				stack[i] = stack[i + 1];
			}
			stack[stackTop] = move;
		} else {
			stack[++stackTop] = move;
		}
		System.out.println("\tNotified Stack Listeners");
		notifyListener();
	}
	
	/**
	 * Pop an object off the stack
	 * @return the object on the top of the stack
	 */
	public Move pop() {
		Move move = stack[stackTop];
		stack[stackTop--] = null;
		notifyListener();
		return move;
	}
	
	/**
	 * Returns the value at the top of the stack
	 * @return Object on the top of the stack
	 */
	public Move peek() {
		return stack[stackTop];
	}
	
	public Move peek(int indx) {
		return stack[indx];
	}
	/**
	 * Deletes all objects from the stack
	 */
	public void clear() {
		for (int i = 0; i < stackTop; i++) {
			stack[i] = null;
		}
		stackTop = -1;
		notifyListener();
	}
	public int getStackTop() {
		return stackTop;
	}
}
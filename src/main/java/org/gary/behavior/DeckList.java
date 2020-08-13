package org.gary.behavior;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * this implementation includes a discard pile that can be accessed.
 *
 */

public class DeckList<T>
{
	private final ArrayList<T> list;
	private int cursor;

	public DeckList(List<T> asList) {
		this(asList,0);
	}
	public DeckList(List<T> asList, int cursor) {
		this.list = new ArrayList<>(asList);
		this.cursor = cursor;
	}


	public boolean canDraw(){
		return cursor < list.size();
	}

	@NotNull
	public T draw(){
		return list.get(cursor++);
	}

	@NotNull
	public T peek(){
		return list.get(cursor);
	}

	public List<T> getDeck(){
		return list.subList(cursor, list.size());
	}

	public List<T> getDiscard(){
		return list.subList(0, cursor);
	}

	public List<T> getAll(){
		return list;
	}
}

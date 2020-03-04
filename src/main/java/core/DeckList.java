package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jetbrains.annotations.NotNull;

/**
 * this implementation includes a discard pile that can be accessed.
 *
 */

public class DeckList<T> implements List<T>
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

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@NotNull
	@Override
	public <T1> T1[] toArray(@NotNull T1[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(T t) {
		return list.add(t);
	}

	@Override
	public boolean remove(Object o) {
		if(list.indexOf(o)<cursor){
			cursor--;
		}

		return list.remove(o);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends T> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index, @NotNull Collection<? extends T> c) {
		if(index<=cursor){
			cursor += c.size();
		}
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		int size = list.size();
		c.forEach(this::remove);
		return size != list.size();
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		throw new UnsupportedOperationException("the fuck does this do?");
	}

	@Override
	public void clear() {
		cursor = 0;
		list.clear();
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public T set(int index, T element) {
		return list.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		if(index<=cursor){
			cursor ++;
		}
		list.add(index, element);
	}

	@Override
	public T remove(int index) {
		if(index<=cursor){
			cursor --;
		}
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.indexOf(o);
	}

	@NotNull
	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@NotNull
	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@NotNull
	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	public boolean canDraw(){
		return cursor < size();
	}

	public T draw(){
		return list.get(cursor++);
	}

	public T peek(){
		return list.get(cursor);
	}

	public List<T> getDeck(){
		return list.subList(cursor, list.size());
	}

	public List<T> getDiscard(){
		return list.subList(0, cursor);
	}
}

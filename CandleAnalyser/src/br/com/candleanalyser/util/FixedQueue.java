package br.com.candleanalyser.util;

import java.util.Iterator;

public class FixedQueue<T> implements Iterable<T> {

	private int n;
	private Object[] elements;

	public FixedQueue(int maxSize) {
		elements = new Object[maxSize];
	}

	public void add(T obj) {
		if (n < elements.length) {
			n++;
		} else {
			//put new sample in tail
			for (int i = 0; i < elements.length-1; i++) {
				elements[i] = elements[i+1];
			}
		}
		elements[n-1] = obj;
	}
	
	public T get(int i) {
		if((i+1)>n) throw new IllegalStateException("Cannot get element at position " + i + ". Number of elements=" + getSize());
		return (T)elements[i];
	}
	
	public int getSize() {
		return n;
	}

	public int getMaxSize() {
		return elements.length;
	}
	
	@Override
	public Iterator<T> iterator() {
		Iterator<T> t = new Iterator<T>(){
			int next = 0;
			@Override
			public boolean hasNext() {
				return next<n;
			}
			@Override
			public T next() {
				if(hasNext()) {
					return get(next++);
				} else {
					throw new IndexOutOfBoundsException("No more elements available");
				}
			}
			@Override
			public void remove() {
				throw new RuntimeException("remove() not implemented for this list");
			}};
		return t;
	}

	public boolean isFull() {
		return getSize()==getMaxSize();
	}

}

package br.com.candleanalyser.util;

import junit.framework.TestCase;

public class UtilTest extends TestCase {

	public void testFixedList() {
		FixedQueue<Integer> fl = new FixedQueue<Integer>(5);
		assertEquals(0, fl.getSize());
		fl.add(1);
		fl.add(2);
		assertEquals(2, fl.getSize());
		fl.add(3);
		fl.add(4);
		fl.add(5);
		assertEquals(5, fl.getSize());
		fl.add(6);
		fl.add(7);
		assertEquals(5, fl.getSize());
		assertEquals(3, fl.get(0).intValue());
		assertEquals(7, fl.get(4).intValue());
	}
	
}

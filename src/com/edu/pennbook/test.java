package com.edu.pennbook;

import java.util.LinkedList;
import java.util.List;

import com.edu.pennbook.server.PennbookSQL;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PennbookSQL psql = new PennbookSQL();
		try {
			List<Integer> l = new LinkedList<Integer>();
			l.add(3);l.add(4);l.add(5);
			System.out.println(l.size());
			l.remove(1);
			System.out.println(l.get(1));
			
//			System.out.println("Work");
//			psql.startup();
//			System.out.println("maybe");
//			System.out.println(psql.getFirstName(1));
//			psql.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

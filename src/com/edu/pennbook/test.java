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
			psql.startup();
			System.out.println(psql.getFirstName(psql.getMsgReciever(4)));
			psql.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

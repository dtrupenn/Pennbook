package com.edu.pennbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
			System.out.println(psql.getBDay(1));
			psql.updateBDay(1, "1991-12-12 00:00:00");
			System.out.println(psql.getBDay(1));
			psql.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

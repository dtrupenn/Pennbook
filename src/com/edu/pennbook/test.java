package com.edu.pennbook;

import java.sql.ResultSet;

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
			psql.updateAffiliation(0, "Penn");
			System.out.println(psql.getAffiliation(0));
			psql.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

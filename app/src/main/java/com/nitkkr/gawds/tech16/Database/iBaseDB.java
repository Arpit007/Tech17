package com.nitkkr.gawds.tech16.Database;

/**
 * Created by Home Laptop on 20-Dec-16.
 */

public interface iBaseDB
{
	void resetTable();
	long getRowCount();
	String getTableName();
	void deleteTable();
}

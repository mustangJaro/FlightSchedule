package com.flightSchedule.core;

import com.db.Database;
import com.flightSchedule.preferencePages.ConfigurationPreference;

public abstract class SQLServerBase {
	private String serverName;
	private String DBName;
	
	public SQLServerBase() {
		ConfigurationPreference config = new ConfigurationPreference();
		serverName = config.getServerName();
		DBName = config.getDBName();
	}
	
	public Database getDatabase(){
		Database db = new Database(serverName, DBName);
		db.connect();
		return db;
	}
	
	//TODO provide ability to get SQL statements from resource file

}

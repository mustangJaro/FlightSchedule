package com.flightSchedule.core;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.Database;
import com.flightSchedule.baseObjects.Aircraft;

public class SQLServerAircraft extends SQLServerBase {

	public List<Aircraft> getAircraft(){
		List<Aircraft> ac = new ArrayList<Aircraft>();
		Database db = getDatabase();
		
		try{
			ResultSet rs = db.executeQuery("select EI_SN from EIACFT where DEL_FLAG = 0");
			
			while(rs.next()){
				Aircraft acft = new Aircraft();
				acft.setTailNumber(rs.getString("EI_SN"));
				ac.add(acft);
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		
		return ac;
	}
	public Map<BigInteger, String> getAircraftMap(){
		Map<BigInteger, String> ac = new HashMap<BigInteger, String>();
		Database db = getDatabase();
		ac.put(BigInteger.ZERO, "");
		
		try{
			ResultSet rs = db.executeQuery("select EI_SN from EIACFT where DEL_FLAG = 0");
			
			while(rs.next()){
				ac.put(new BigInteger(rs.getString("EI_SN")), rs.getString("EI_SN"));
			}
		}catch(SQLException e){
			ExceptionHandler.logException(e);
		}finally {
			db.close();
		}
		return ac;
	}
}

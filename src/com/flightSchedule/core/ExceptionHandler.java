package com.flightSchedule.core;

public class ExceptionHandler {

	public static void logException(String customMessage, Exception e){
		logException(null, e, 4);
	}
	public static void logException(Exception e){
		logException(null, e, 4);
	}
	
	private static void logException(String customMessage, Exception e, int depth){
		System.err.println(getClassAndMethodName(depth) + ": " + 
				(customMessage != null ? customMessage + ": " : "") 
				+ e.getMessage());		
	}
	
	public static String getClassAndMethodName(final int depth) {
	    final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
	    return ste[depth].getClassName() + "." + ste[depth].getMethodName();
	}
	
}

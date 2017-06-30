package com.flightSchedule.baseObjects;

public enum Rank {
	PFC("PFC"),
	SPC("SPC"),
	SGT("SGT"),
	SSG("SSG"),
	SFC("SFC"),
	MSG("MSG"),
	OSG("1SG"),
	SGM("SGM"),
	CSM("CSM"),
	WO1("WO1"),
	CW2("CW2"),
	CW3("CW3"),
	CW4("CW4"),
	CW5("CW5"),
	TLT("2LT"),
	OLT("1LT"),
	CPT("CPT"),
	MAJ("MAJ"),
	LTC("LTC"),
	COL("COL")
	;
	
	private final String text;
	
	private Rank(final String text){
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
}

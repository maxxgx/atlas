package atlas.utils;

public enum Units {
	MIN_SEC(60, "min/sec"), 
	HOUR_SEC(3600, "hour/sec"), 
	DAY_SEC(86400, "day/sec"), 
	WEEK_SEC(DAY_SEC.getValue() * 7, "week/sec"),
	MONTH_SEC(DAY_SEC.getValue() * 30, "month/sec"),
	YEAR_SEC(31536000, "year/sec");
	
	private int number;
	private String name;
	
	private Units(int u, String name) {
		this.number = u;
		this.name = name;
	}
	
	public int getValue() {
		return this.number;
	}
	
	public String getName() {
		return this.name;
	}

}

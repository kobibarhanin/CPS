package CarPark;

public abstract class ParkDetails {
	public static enum ParkPos {
		EMPTY,OCCUPIED,CORRUPT
	};
	public static enum type {
		POSITIONS,STATISTICS, PERIOD,PERFORMANCE
	};
}

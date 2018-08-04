package CarPark;

public abstract class Struct {
	public static enum table {
		USERS,ORDERS,CARS,CARPARKS,STATISTICS,CPSSCHEDULER,COMPLAINTS
	};

	public static enum report {
		ORDER,COMPLAINT,CORRUPTED,EXCEEDING,IMAGE_STATE, PERIOD
	};

	public static enum CorruptPeriod {
		WEEKLY,MONTHLY
	};
}

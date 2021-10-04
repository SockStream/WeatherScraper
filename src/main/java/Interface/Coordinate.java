package Interface;

public class Coordinate {

	private double _latitude, _longitude;

	public Coordinate(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public double getLongitude() {
		return _longitude;
	}

	public void setLongitude(double longitude) {
		this._longitude = longitude;
	}

	public double getLatitude() {
		return _latitude;
	}

	public void setLatitude(double latitude) {
		this._latitude = latitude;
	}
}

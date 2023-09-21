package Interface;

public class Coordinate {

	private double _latitude, _longitude;
	private String _nom;

	public Coordinate(double latitude, double longitude, String nom) {
		setLatitude(latitude);
		setLongitude(longitude);
		set_nom(nom);
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

	public String get_nom() {
		return _nom;
	}

	public void set_nom(String _nom) {
		this._nom = _nom;
	}
}

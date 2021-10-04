package Interface;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import datas.LieuData;

public abstract class InterfaceRecuperation {
	protected List<LieuData> listeLieuxData = new ArrayList<LieuData>();	
	
	public List<LieuData> getListeLieuxData ()
	{
		return listeLieuxData;
	}

	public abstract void ScraperDonneesLieu(Coordinate lieu) throws IOException;
}

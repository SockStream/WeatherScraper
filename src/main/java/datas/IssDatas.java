package datas;

public class IssDatas {
	
	private String start;
	private String max;
	private String end;
	private String magnitude;
	private Boolean hasDatas;
	
	public IssDatas()
	{
		hasDatas = false;
	}
	
	public void parse(String content)
	{
		content = content.replaceAll("<strong>", "");
		content = content.replaceAll("</strong>", "");
		
		String[] splitted = content.split("<br />");
		start = splitted[0];
		max = splitted[1];
		end = splitted[2];
		magnitude = splitted[3];
		hasDatas = true;
	}
	
	public String getDatas()
	{
		return start + "\n" + max + "\n" + end + "\n" + magnitude;
	}

	public boolean hasDatas() {
		return hasDatas;
	}
}

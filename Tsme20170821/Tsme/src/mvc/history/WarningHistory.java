package mvc.history;

import java.util.ArrayList;
import java.util.List;

public class WarningHistory {

	private String warningTime;
	
	private List<WarningGroup> warningGroupList = new ArrayList<WarningGroup>();

	public String getWarningTime() {
		return warningTime;
	}

	public void setWarningTime(String warningTime) {
		this.warningTime = warningTime;
	}

	public List<WarningGroup> getWarningGroupList() {
		return warningGroupList;
	}

	public void setWarningGroupList(List<WarningGroup> warningGroupList) {
		this.warningGroupList = warningGroupList;
	}
	
}

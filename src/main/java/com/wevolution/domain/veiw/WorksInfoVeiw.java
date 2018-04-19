package com.wevolution.domain.veiw;

import java.io.Serializable;
import java.util.List;

import com.wevolution.domain.WorksInfo;

public class WorksInfoVeiw implements Serializable{
	/** 
	* @Fields serialVersionUID :  
	*/ 
	private static final long serialVersionUID = -982734731860587124L;
	protected List<WorksInfo> worksInfos;

	public List<WorksInfo> getWorksInfos() {
		return worksInfos;
	}

	public void setWorksInfos(List<WorksInfo> worksInfos) {
		this.worksInfos = worksInfos;
	}
	
}

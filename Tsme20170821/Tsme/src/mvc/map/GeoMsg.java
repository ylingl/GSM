package mvc.map;

//设备及基站地理信息查询结果
public class GeoMsg {
	
	private String BSIC;
	
	private String name;
	
	private String model;

	//设备编号
	private String device_num;
	
	//设备型号
	private String device_type;
	
	//简介
	private String introduction;
	
	//经度
	private float LNG;
	
	//纬度
	private float LAT;
	
	private String ip;
	
	//代表设备或基站
	private String point_type;
	
	private String km_stone;
	
	private String state;
	
	public String getPoint_type() {
		return point_type;
	}

	public void setPoint_type(String point_type) {
		this.point_type = point_type;
	}

	public String getIntroduction() {
		return introduction;
	}
	
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public float getLNG() {
		return LNG;
	}
	
	public void setLNG(float LNG) {
		this.LNG = LNG;
	}
	
	public float getLAT() {
		return LAT;
	}
	
	public void setLAT(float LAT) {
		this.LAT = LAT;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getDevice_num() {
		return device_num;
	}

	public void setDevice_num(String device_num) {
		this.device_num = device_num;
	}

	public String getKm_stone() {
		return km_stone;
	}

	public void setKm_stone(String km_stone) {
		this.km_stone = km_stone;
	}

	public String getBSIC() {
		return BSIC;
	}

	public void setBSIC(String bSIC) {
		BSIC = bSIC;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

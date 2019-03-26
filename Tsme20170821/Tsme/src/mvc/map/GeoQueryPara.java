package mvc.map;

//设备、基站信息查询需要参数类
public class GeoQueryPara {
	
	private float minX;
	
	private float minY;
	
	private float maxX;
	
	private float maxY;
	
	public float getMinX() {
		return minX;
	}
	
	public void setMinX(float minX) {
		this.minX = minX;
	}
	
	public float getMinY() {
		return minY;
	}
	
	public void setMinY(float minY) {
		this.minY = minY;
	}
	
	public float getMaxX() {
		return maxX;
	}
	
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}
	
	public float getMaxY() {
		return maxY;
	}
	
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
}

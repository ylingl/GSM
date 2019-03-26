package utils;

import java.util.List;

public class PageView<T> {
	
	//当前页
	private int curPage = 1;
	
	//单页数据条目数
	private int pageSize = 20;
	
	//总页数
	private long totalPagesNum = 1;
	
	//总条目数
	private long totalItemsNum = 0;
	
	//排序列
	private String sortColumn = null;
	
	//排列顺序
	private ESortOrder sortOrder = ESortOrder.ASC;
	
	//当前页中的数据
	private List<T> items;
	
	//默认构造空页
	public PageView(int pageSize){
		this(pageSize, 0, null);
	}
	
	public PageView(int pageSize, long totalItems){
		this(pageSize, totalItems, null);
	}
	
	public PageView(int curPage, int pageSize) {
		this.curPage = curPage;
		this.pageSize = pageSize;
	}
	
	public PageView(int pageSize, long totalItems, List<T> items){
		this.pageSize = pageSize;
		setTotalItemsNum(totalItems);
		this.items = items;
	}
	
	public enum ESortOrder {
		ASC("ASC"),
		DESC("DESC");
		
		private String name;
		
		ESortOrder(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPagesNum() {
		return totalPagesNum;
	}

	public void setTotalPagesNum(long totalPagesNum) {
		this.totalPagesNum = totalPagesNum;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public ESortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(ESortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
	
	/**
	 * SQL分页查询的首条数目相对第一条数目的偏移量
	 * <p>
	 * 注：SQL分页语法： LIMIT [offset],rows.
	 * @return
	 */
	public long getOffset(){
		long offset = (this.curPage - 1) * this.pageSize;
		return offset < 0 ? 0 : offset;
		
	}
	
	/**
	 * SQL一次分页查询的行数
	 * @return
	 */
	public long getRows(){
		return this.pageSize;
	}
	
	/**
	 * SQL一次分页查询的最后一条记录行号
	 * @return
	 */
	public long getFinishRow(){
		return getOffset() + getRows();
	}

	public long getTotalItemsNum() {
		return totalItemsNum;
	}

	public void setTotalItemsNum(long totalItemsNum) {
		this.totalItemsNum = totalItemsNum;
		if(pageSize != 0){
			if(totalItemsNum % pageSize == 0){
				this.totalPagesNum = totalItemsNum / pageSize;
			} else {
				this.totalPagesNum = (long) (Math.floor(totalItemsNum / pageSize) + 1);
			}
		}
	}
}

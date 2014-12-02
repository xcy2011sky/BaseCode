package com.xcy.xpassword.model;



import java.io.InputStream;
import java.util.List;




public interface CategoryItemParser {
	/**
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public List<CategoryItem> parse(InputStream is) throws Exception;
	
	/**
	 *
	 * @param passwords
	 * @return
	 * @throws Exception
	 */
	public String serialize(List<CategoryItem> passwords) throws Exception;
}

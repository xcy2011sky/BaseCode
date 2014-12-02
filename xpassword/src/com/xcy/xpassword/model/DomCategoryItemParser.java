package com.xcy.xpassword.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xcy.xpassword.app.XDebug;
import com.xcy.xpassword.ui.XPasswordActivity;

import android.content.Context;

public class DomCategoryItemParser implements CategoryItemParser {
	
	
    private XDebug debug=new XDebug("DomCategoryItem",false);
    
	
	private Context mContext;
	public DomCategoryItemParser(Context context)
	{
		this.mContext=context;
	}
	@Override
	public List<CategoryItem> parse(InputStream is) throws Exception {
		List<CategoryItem>categorys=new ArrayList<CategoryItem>();
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		builder = factory.newDocumentBuilder();
		Document document = null;
		try {
		document = builder.parse(is);
		} catch (SAXException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
			return null;
		}
		Element rootElement = document.getDocumentElement();

		NodeList list = rootElement.getElementsByTagName("category");
		if (list.getLength() < 1)
		 return null;
		for(int i=0;i<list.getLength();i++)
		{
			debug.LogE("category.getLength()="+list.getLength());
			
			CategoryItem categoryItem=new CategoryItem(mContext);
			List<PasswordItem>passwordItems=new ArrayList<PasswordItem>();
			
			Element element = (Element) list.item(i);
			int id = Integer.parseInt(element.getAttribute("id"));
		
			debug.LogE("category.id="+id);
			NodeList itemList = element.getElementsByTagName("item");
			debug.LogE("itemList.getLength()="+itemList.getLength());
			for (int j = 0; j < itemList.getLength(); j++)
			{
				
				
				 	Node item = itemList.item(j);  
				 	
		            NodeList properties = item.getChildNodes();
		            
		            Password password=new Password();
		        	debug.LogE("properties.getLength()="+properties.getLength());
		            for (int k = 0; k < properties.getLength(); k++)
		            {  
		            	
		            
		                Node property = properties.item(k);  
		                String nodeName = property.getNodeName();  
		              	debug.LogE("properties.nodeName="+nodeName);
		              	
		                if (nodeName.equals("index")) {  
		                	
		                	int index=Integer.parseInt(property.getFirstChild().getNodeValue());
		                	debug.LogE("properties.index="+index);
		                	password.setCategory(XPasswordActivity.gCategorys.get(index));
		                     
		                } else if (nodeName.equals("name")) {  
		                	debug.LogE("properties.name="+property.getFirstChild().getNodeValue());
		                	password.setTitle(property.getFirstChild().getNodeValue()); 
		                } else if (nodeName.equals("url")) {  
			                	
			                	password.setUrl(property.getFirstChild().getNodeValue());
		                
		                } //else if (nodeName.equals("descript")) { 
		               // 	if(property.getFirstChild().getNodeValue()!=null)
		               // 	password.setNote(property.getFirstChild().getNodeValue());  
		               // }  
		            }  
		            PasswordItem passwordItem=new PasswordItem(mContext, password);
		            passwordItems.add(passwordItem);
			}
			categoryItem.setCategory(XPasswordActivity.gCategorys.get(id));
			categoryItem.setPassworditems(passwordItems);
			categorys.add(categoryItem);
		}
		
		
		
		return categorys;
	}

	@Override
	public String serialize(List<CategoryItem> passwords) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

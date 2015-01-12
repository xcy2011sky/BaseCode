package com.xcy.datastract;

import com.xcy.datastract.EnumTest.XResType;

class PutOut<E>{
	public PutOut(){
		
	}
	public  void print(E array[],String type){
		System.out.println("name="+type);
		for(E i:array){
			System.out.print(" "+i);
		}
		System.out.println("");
	}
	public  void print(int array[],String type){
		System.out.println("name="+type);
		for(int i:array){
			System.out.print(" "+i);
		}
		System.out.println("");
	}
}



public class SearchUtil {
	private static int mArray[]={1,12,13,34,233,45,2,54,34,53,245,542,90,78,1234};

	
	public  int BinarySearch(int[] mArray2,int start,int end,int value){
		
		System.out.println("digui implment");
		if(start>end)return -1;
		if(value==mArray2[end])return end;
		
		int mid=(start+end)/2;
		
		if(value==mArray2[mid])
			{
				return mid;
			}
		else if(value>mArray2[mid]){
			return BinarySearch(mArray2,mid,end,value);
		}else{
			return BinarySearch(mArray2,start,mid,value);
		}
	}

	public  int BinarySearch(int[] mArray2,int value){
		
		System.out.println("while implment");
		int start=0,end=mArray2.length-1;
		if(value==mArray2[end])return end;
		
		while(start<end){
		
			int mid=(start+end)/2;
			System.out.println("mid="+mid);
			if(value==mArray2[mid]) 
				return mid;
			else if(value>mArray2[mid]){
				start=mid;
			}else{
				end=mid;
			}
		}
		return -1;
		
	}
	
	
	
	public  static void main (String []args){

		PutOut<Integer> put=new PutOut<Integer>();
		SearchUtil searchUtil=new SearchUtil();
		SortUtil sortUtil=new SortUtil();
		put.print(mArray, "Init:");
		sortUtil.BuSort(mArray);
		put.print(mArray, "after sort ");
		System.out.println("leng="+mArray.length);
		int index=searchUtil.BinarySearch(mArray,1234);

		System.out.println("index="+index);
		
		
	
		
	}
	

	
}

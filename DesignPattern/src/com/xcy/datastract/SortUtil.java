package com.xcy.datastract;

public class SortUtil {
  
	class Print<E>{
		public void PrintData(String type,E[] array){
			System.out.println(type);
			for( E e : array){
				System.out.print(" "+e);
			}
			System.out.println();
		}
	}
	//插入排序
		public static void InSort(int []array){
		
			int lenght=array.length;
			int i; //插入的下标
			int j;//插入值ｉ的下标之前的值
			int key;//需要插入的值
			for (i=1;i<lenght;i++){
				key=array[i];
				for(j=i;j>0;j--){
					if(key<array[j-1]){
						array[j]=array[j-1];
						array[j-1]=key;
					}
				}
			}

		}
		//冒泡排序
	public static void BuSort(int array[]){
			int []list=array;
			
			int tmp;
			for(int i=0;i<list.length;i++){
				for(int j=0;j<list.length-1;j++){
					if(list[j+1]<list[j]){
						tmp=list[j];
						list[j]=list[j+1];
						list[j+1]=tmp;
					}
				}
			}
		
		}
		//快速排序
	public static void QuickSort(int array[]){
	
			
			quickSort(array,0,array.length-1);
			
		
		}
		private static int  getMid(int a[],int left,int right){
			int tmp=a[left];
			
			while(left<right){
				while(left<right && a[right]>tmp){
					right=right-1;
				}
			    a[left]=a[right];
				while(left<right&&a[left]<=tmp){
					left++;
				}
		       a[right]=a[left];
				
			}
			a[left]=tmp;
			return left;
			
		}
		private static void quickSort(int a[],int low,int hight){
			if(low<hight){
				int mid_index=getMid(a,low,hight);
				System.out.println("mid="+mid_index);
				quickSort(a,low,mid_index-1);
				quickSort(a,mid_index+1,hight);
			}
		}
	//选择排序
	  public static void ChoiceSort(int  array[]){

		for(int i=0;i<array.length;i++){
			int min=i;
			for(int j=i;j<array.length;j++){
				if(array[min]>array[j]){
					min=j;
				}
			}
			if(min!=i){
				int tmp;
				tmp=array[min];
				array[min]=array[i];
				array[i]=tmp;
				
			}
		}
	;
	}
	// Shell　排序
	  public static void ShellSort(int[] data) {  
	
	      // 计算出最大的h值  
	      int h = 1;  
	      while (h <= data.length / 3) {  
	          h = h * 3 + 1;  
	      }  
	      while (h > 0) {  
	          for (int i = h; i < data.length; i += h) {  
	              if (data[i] < data[i - h]) {  
	                  int tmp = data[i];  
	                  int j = i - h;  
	                  while (j >= 0 && data[j] > tmp) {  
	                      data[j + h] = data[j];  
	                      j -= h;  
	                  }  
	                  data[j + h] = tmp;  
	               //   print(data);  
	              }  
	          }  
	          // 计算出下一个h值  
	          h = (h - 1) / 3;  
	      }  
	
	  }  
		
		

	
}

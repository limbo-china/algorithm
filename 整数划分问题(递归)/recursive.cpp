 #include <stdio.h>
 
 int count(int n,int m){
 	if(n==1){
		return 1;
	}
	else if(m==1){
			return 1;
	}
	else if(n<m){
		return count(n,n);
	}
	else if(n==m){
		return 1+count(n,m-1);
	}
	else{
		return count(n-m,m)+count(n,m-1);
	}
 }
 
 
 int main(){
 	int num;
 	scanf("%d",&num);
 	printf("%d\n",count(num,num));
 	return 0;
}

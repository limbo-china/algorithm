 #include "stdio.h"
 #include "string.h"
 #include "time.h"
 
 #define random(x) (rand()%x)
 #define N 101
 #define P 4
 int Matrix[N][N];
 int NumMat[N][N];
 
 int main(){
 	memset(Matrix, 0, sizeof(Matrix));
 	memset(NumMat, 0, sizeof(NumMat));
 	srand((int)time(0));
 	int i,j,num=0,sum=0;
	
	//构建地雷数组
 	for(i=0;i<N;i++){
 		for(j=0;j<N;j++){
 			if(random(P)==0){
 				Matrix[i][j]=1;
 				num++;
 			}
 			printf("%d ",Matrix[i][j]);
		}
		printf("\n");
	}
	printf("\n");
	
	//构建数字数组
	for(i=0;i<N;i++){
 		for(j=0;j<N;j++){
 			if(i==0&&j==0){
 				NumMat[i][j]=Matrix[i][j]+Matrix[i][j+1]+Matrix[i+1][j]+Matrix[i+1][j+1];
 				continue;
 			}
 			if(i==0&&j==N-1){
 				NumMat[i][j]=Matrix[i][j]+Matrix[i][j-1]+Matrix[i+1][j]+Matrix[i+1][j-1];
 				continue;
 			}
 			if(i==N-1&&j==0){
 				NumMat[i][j]=Matrix[i][j]+Matrix[i][j+1]+Matrix[i-1][j]+Matrix[i-1][j+1];
 				continue;
 			}
 			if(i==N-1&&j==N-1){
 				NumMat[i][j]=Matrix[i][j]+Matrix[i][j-1]+Matrix[i-1][j]+Matrix[i-1][j-1];
 				continue;
 			}
 			if(i==0){
 				NumMat[i][j]=Matrix[i][j-1]+Matrix[i][j]+Matrix[i][j+1]+Matrix[i+1][j-1]+Matrix[i+1][j]+Matrix[i+1][j+1];
 				continue;
 			}
 			if(i==N-1){
 				NumMat[i][j]=Matrix[i][j-1]+Matrix[i][j]+Matrix[i][j+1]+Matrix[i-1][j-1]+Matrix[i-1][j]+Matrix[i-1][j+1];
 				continue;
 			}
 			if(j==0){
 				NumMat[i][j]=Matrix[i-1][j]+Matrix[i][j]+Matrix[i+1][j]+Matrix[i-1][j+1]+Matrix[i][j+1]+Matrix[i+1][j+1];
 				continue;
 			}
 			if(j==N-1){
 				NumMat[i][j]=Matrix[i-1][j]+Matrix[i][j]+Matrix[i+1][j]+Matrix[i-1][j-1]+Matrix[i][j-1]+Matrix[i+1][j-1];
 				continue;
 			}
 			NumMat[i][j]=Matrix[i-1][j-1]+Matrix[i-1][j]+Matrix[i-1][j+1]+Matrix[i][j-1]+Matrix[i][j]+Matrix[i][j+1]+Matrix[i+1][j-1]+Matrix[i+1][j]+Matrix[i+1][j+1];
		}
	}
 	for(i=0;i<N;i++){
 		for(j=0;j<N;j++){
 			printf("%d ",NumMat[i][j]);
		}
		printf("\n");
	}	

	//计算地雷总数量	
	if(N%3==0){
		for(i=1;i<N;i=i+3)
			for(j=1;j<N;j=j+3)
				sum=sum+NumMat[i][j];		
	}
	else{
		for(i=0;i<N;i=i+3)
			for(j=0;j<N;j=j+3)
				sum=sum+NumMat[i][j];
	} 	
	printf("num:%d\n",num);
	printf("sum:%d\n",sum);
	return 0;			
 }

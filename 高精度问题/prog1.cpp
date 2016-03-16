#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define N 40000

int a[N]={0};

int main(){
	
	FILE *fp;
	char FNAME[20]="prog1_"; char TIME[20]="";
	
	int n,i,j,num=1;
	scanf("%d",&n);
	
	int start=clock();
	
	a[0]=1;
	for(i=2;i<=n;i++)
	{
		int c=0;
		for(j=0;j<=N;j++)
		{
			int s=a[j]*i+c;
			a[j]=s%10;
			c=s/10;
		}
	}
	j=N-1;
	while(!a[j]) 
		j--;
		
	
	int end=clock();
	int time=(end-start)*1000/CLOCKS_PER_SEC;
	itoa(time,TIME,10);
	strcat(TIME,"ms.txt");
	strcat(FNAME,TIME);
	if((fp=fopen(FNAME,"w"))==NULL)
		printf("not open !\n");
		
		
	while(j>=0){
		fprintf(fp,"%d",a[j]);
		j--;
	}
	return 0;	
}


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int a[40000];
int main()
{
	FILE *fp;
	char FNAME[20]="prog2_"; char TIME[20]="";
	
 int N,i,j,pre;
 
 scanf("%d",&N);
 
 int start=clock();
 
  a[0]=1;a[1]=1;
  
  for(i=2;i<=N;i++)
  {
   pre=0;
   for(j=1;j<=a[0];j++)
   {
    a[j]*=i;
    a[j]+=pre;//���Ͻ�λ
    pre=a[j]/10;//��λֵ
    a[j]%=10;//��λ
   }
   while(pre)//��ĩβ�Ľ�λ���д洢
   {
    a[j]=pre%10;
    a[0]=j;
    pre/=10;
    j++;
   }
  }
  
  
  int end=clock();
	int time=(end-start)*1000/CLOCKS_PER_SEC;
	itoa(time,TIME,10);
	strcat(TIME,"ms.txt");
	strcat(FNAME,TIME);
	if((fp=fopen(FNAME,"w"))==NULL)
		printf("not open !\n");
		
  
  for(i=a[0];i>=1;i--)//���
  		fprintf(fp,"%d",a[i]);
 return 0;
}

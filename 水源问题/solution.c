#include "stdio.h"
#include "string.h"

#define N 5
#define LEFT 0
#define UP 1
#define RIGHT 0
#define DOWN 1
int r[N][N-1]={{3,2,4,8},{6,3,5,3},{4,6,3,5},{1,1,3,5},{9,3,9,4}};
int c[N-1][N]={{7,8,1,9,4},{2,5,1,6,2},{2,4,2,5,2},{3,7,4,3,7}};
int s1[N][N],s2[N][N];
int A1[N][N],A2[N][N];
int B1[2*N-2],B2[2*N-2];

int main(){
	memset(s,0,sizeof(s));
	int a1i,a1j,a2i,a2j,b1i,b1j,b2i,b2j,aij,bij;
	scanf("%d%d%d%d%d%d%d%d",&a1i,&a1j,&a2i,&a2j,&bi,&bj);
	aij=ai+aj;bij=bi+bj;
	int i,j,ij;
	for(ij=aij+1;ij<=bij;ij++)
		for(i=ai;i<=bi;i++){
			j=ij-i;
			if(j<=bj){
				if(i==ai)
					s[i][j]=s[i][j-1]+r[i][j-1],A[i][j]=LEFT;
				else if(j==aj)
					s[i][j]=s[i-1][j]+c[i-1][j],A[i][j]=UP;
				else{	
					if(s[i][j-1]+r[i][j-1]<=s[i-1][j]+c[i-1][j])
						s[i][j]=s[i][j-1]+r[i][j-1],A[i][j]=LEFT;
					else
						s[i][j]=s[i-1][j]+c[i-1][j],A[i][j]=UP;
				}
			}			
		}
	int n;
	i=bi;j=bj;
	for(n=bij-aij-1;n>=0;n--)
		if(A[i][j]==LEFT)
			B[n]=RIGHT,j=j-1;
		else
			B[n]=DOWN,i=i-1;
	for(i=0;i<bij-aij;i++)
		printf("%s ",B[i]==0?"RIGHT":"DOWN");		
	printf("\nsum:%d\n",s[bi][bj]);
}

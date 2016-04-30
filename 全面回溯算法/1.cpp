//bool finished = FALSE; /* 是否获得全部解? */
#include "stdio.h"

int N[4]={0,4,6,7};
void backtrack(int a[], int k, int input);
int is_a_solution(int a[],int k, int input);
void construct_candidates(int a[],int k, int input, int c[],int *ncandidates);
void process_solution(int a[],int k,int input);
void generate_subsets(int n);

void backtrack(int a[], int k, int input)
{
        int c[2]; /*这次搜索的候选 */
        int ncandidates; /* 候选数目 */
        int i; /* counter */
        if (is_a_solution(a,k,input))
                process_solution(a,k,input);
        else {
                k = k+1;
                construct_candidates(a,k,input,c,&ncandidates);
                for (i=0; i<ncandidates; i++) {
                        a[k] = c[i];
                        backtrack(a,k,input);
                        //if (finished) return; /* 如果符合终止条件就提前退出 */
                }
        }
}

int is_a_solution(int a[],int k, int input)
{
        return k==input;
}

void construct_candidates(int a[],int k, int input, int c[],int *ncandidates)
{
        c[0] = 1;
        c[1] = 0;
        *ncandidates = 2;
}

void process_solution(int a[],int k,int input)
{
        int i;
        printf("{");
        for(i=1; i<=k; i++)
                if(a[i])
                        printf(" %d",N[i]);
        printf(" }\n");
}
void generate_subsets(int n){
        int a[3];
        backtrack(a,0,n);
}
int main(){
        generate_subsets(3);
}

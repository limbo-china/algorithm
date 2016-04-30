
//kmp algorithm, more introduction in clrs.
#include <iostream>
#include <string>
#include <vector>

using namespace std;
class Solution {
public:
    int strStr(string haystack, string needle) {
        vector<int> PI;
        CalcPI(PI,needle);
        vector<int>::iterator it; 
        for(it=PI.begin();it!=PI.end();it++)
            cout<<*it;
        return 0;
    }
    
    void CalcPI(vector<int>& PI, string &needle){
        int m=needle.size();
        int i,j;
        PI.push_back(0);
        PI.push_back(0);
        i=0;
        for(j=2;j<m;j++){
            while(i>0 && needle[i+1]!=needle[j])
                i=PI[i];
            if(needle[i+1]==needle[j])
                i=i+1;
            PI.push_back(i);
        }
    }
};
int main(){
	string a="";
	string b="ababaca";
	Solution s;
	s.strStr(a,b);
}

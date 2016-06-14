
/*朴素贝叶斯的思想基础是这样的：
     对于给出的待分类项，求解在此项出现的条件下各个类别出现的概率，
      哪个最大，就认为此待分类项属于哪个类别*/
//设x={a1,a2,...am}为一个待分类项，而每个a为x的一个特征属性。
//有类别集合C={y1,y2..yn}
//计算P(y1|x),P(y2|x)...P(yn|x)

/*
  情景假设：
  通过朴素贝叶斯分类来将社区中所有账号在真实账号和不真实账号两个类别上进行分类
 1、确定特征属性及划分
      这一步要找出可以帮助我们区分真实账号与不真实账号的特征属性，
      在实际应用中，特征属性的数量是很多的，划分也会比较细致，
      但这里为了简单起见，我们用少量的特征属性以及较粗的划分，并对数据做了修改。
      我们选择三个特征属性：a1：日志数量/注册天数，a2：好友数量/注册天数，a3：是否使用真实头像。
      这三项都是可以直接从数据库里得到或计算出来的。
      下面给出划分：a1：{a<=0.05, 0.05<a<0.2, a>=0.2}，
      a1：{a<=0.1, 0.1<a<0.8, a>=0.8}，
      a3：{a=0（不是）,a=1（是）}。
 */
public class NaiveBayesin {
	//待分类项目的属性
	public double a1,a2;
	int a3;
	public int c;
	
	public int defineClass(BaseData bd)
	{
		double[] pc=new double[2];
		int fa1,fa2,fa3;	//设标志位
		pc[0]=1.0;
		pc[1]=1.0;
		if(a1<=0.05)
		{
			fa1=0;
		}
		else if(a1>0.05 && a1<0.2)
		{
			fa1=1;
		}
		else
		{
			fa1=2;
		}
		if(a2<=0.1)
		{
			fa2=0;
		}
		else if(a2>0.1 && a2<0.8)
		{
			fa2=1;
		}
		else
		{
			fa2=2;
		}
		if(a3==0)
		{
			fa3=0;
		}
		else
		{
			fa3=1;
		}
		for(int i=0;i<2;i++)
		{
			pc[i]=bd.pa1c[fa1][i]*bd.pa2c[fa2][i]*bd.pa3c[fa3][i]*bd.pc[i];
		}
		int max=0;
		for(int i=0;i<2;i++)
		{
			if(pc[i]>pc[max])
				max=i;	
		}
		this.c=max;
		return max;	
	}
	
}


public class Clarans {
	private int numlocal;	//表示抽样次数
	private int maxneighbor;	//表示一个节点可以与任意特定邻居进行比较的数目

	
	//CLARANS采样得到代价最小的节点的序号
	public int Clcompare(int[] G){
		int i=1;		//i表示已经选样的次数
		int mincost=65535;
		int bestnode=-1;
		for(i=1;i<numlocal;i++)
		{
			int current=(int)(Math.random()*G.length);	//设置当前结点current为G中任意一个节点
			for(int j=1;j<=maxneighbor;j++)	//j表示已经与current进行比较的邻居的个数
			{
				int rnd=0;
				for(int k=0;k<Math.random();k++)
				{
					rnd*=-1;		//取rnd为一个-1或1的随机数
				}
				if(current+rnd<0 || current+rnd>=G.length)
					continue;
				else if(G[current]>G[current+rnd])
				{
					current=current+rnd;
					j=1;
				}
			}
			if(G[current]<mincost)
			{
				mincost=G[current];
				bestnode=current;
			}
		}
		return bestnode;
	}

	public int getNumlocal() {
		return numlocal;
	}

	public void setNumlocal(int numlocal) {
		this.numlocal = numlocal;
	}

	public int getMaxneighbor() {
		return maxneighbor;
	}

	public void setMaxneighbor(int maxneighbor) {
		this.maxneighbor = maxneighbor;
	}

	
}

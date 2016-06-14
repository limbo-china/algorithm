
public class Clarans {
	private int numlocal;	//��ʾ��������
	private int maxneighbor;	//��ʾһ���ڵ�����������ض��ھӽ��бȽϵ���Ŀ

	
	//CLARANS�����õ�������С�Ľڵ�����
	public int Clcompare(int[] G){
		int i=1;		//i��ʾ�Ѿ�ѡ���Ĵ���
		int mincost=65535;
		int bestnode=-1;
		for(i=1;i<numlocal;i++)
		{
			int current=(int)(Math.random()*G.length);	//���õ�ǰ���currentΪG������һ���ڵ�
			for(int j=1;j<=maxneighbor;j++)	//j��ʾ�Ѿ���current���бȽϵ��ھӵĸ���
			{
				int rnd=0;
				for(int k=0;k<Math.random();k++)
				{
					rnd*=-1;		//ȡrndΪһ��-1��1�������
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

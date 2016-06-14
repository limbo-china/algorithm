import java.io.IOException;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO 自动生成的方法存根
		Clarans cla=new Clarans();
		System.out.println("输入抽样的次数:");
		Scanner input=new Scanner(System.in);
		cla.setNumlocal(input.nextInt());
		System.out.println("一个节点可以与任意特定邻居进行比较的数目:");
		cla.setMaxneighbor(input.nextInt());
		System.out.println("输入比较的数组大小：");
		int length=input.nextInt();
		System.out.println("输入数组");
		int[] G=new int[length];
		for(int i=0;i<length;i++)
		{
			G[i]=input.nextInt();
		}
		int bestnode=cla.Clcompare(G);
		System.out.println(G[bestnode]);
		
	}

}

import java.io.IOException;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO �Զ����ɵķ������
		Clarans cla=new Clarans();
		System.out.println("��������Ĵ���:");
		Scanner input=new Scanner(System.in);
		cla.setNumlocal(input.nextInt());
		System.out.println("һ���ڵ�����������ض��ھӽ��бȽϵ���Ŀ:");
		cla.setMaxneighbor(input.nextInt());
		System.out.println("����Ƚϵ������С��");
		int length=input.nextInt();
		System.out.println("��������");
		int[] G=new int[length];
		for(int i=0;i<length;i++)
		{
			G[i]=input.nextInt();
		}
		int bestnode=cla.Clcompare(G);
		System.out.println(G[bestnode]);
		
	}

}

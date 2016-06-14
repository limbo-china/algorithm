package meanshift_java;
import java.util.Random;
import java.util.Scanner;

public class MeanShift {

	private final static Double MAX = 100.0;
	private final static Double MIN = 0.0;
	private final static Double Radius = 20.0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("��������ά������");
		Scanner sc = new Scanner(System.in);
		System.out.println("����������ɵĵ������");
		int dataCount = 0;
		dataCount = Integer.parseInt(sc.nextLine());
		int dimensionCount = 0;
		System.out.println("��������ά������");
		dimensionCount = Integer.parseInt(sc.nextLine());
		sc.close();
		double[][] originalData = new double[dataCount][dimensionCount];
		double[] currentCenter = new double[dimensionCount];
		// �����������
		Random random = new Random();
		for (int i = 0; i < dataCount; i++) {
			for (int j = 0; j < dimensionCount; j++) {
				originalData[i][j] = random.nextDouble() * (MAX - MIN) + MIN;
			}
		}

		for (int i = 0; i < dataCount; i++) {
			for (int j = 0; j < dimensionCount; j++) {
				System.out.print(originalData[i][j] + " ");
			}
			System.out.print("\n");
		}

		int randomIndex = random.nextInt(10) % dataCount;
		for (int i = 0; i < dimensionCount; i++) {
			currentCenter[i] = originalData[randomIndex][i];
		}
		while (true) {
			// ��ʼ������
			double[] vector = new double[dimensionCount];
			for (int i = 0; i < dimensionCount; i++) {
				vector[i] = 0.0;
			}
			// �������Բ�ڣ������������
			for (int i = 0; i < dataCount; i++) {
				double sumOfDiff = 0.0;
				for (int j = 0; j < dimensionCount; j++) {
					sumOfDiff = sumOfDiff
							+ (currentCenter[j] - originalData[i][j])
							* (currentCenter[j] - originalData[i][j]);
				}
				// ������ڸ�ά���������������
				if (Math.sqrt(sumOfDiff) < Radius) {
					for (int k = 0; k < dimensionCount; k++) {
						vector[k] = vector[k] + originalData[i][k]
								- currentCenter[k];
					}
				}
			}
			// ��������
			double sumOfVarDiff = 0.0;
			for (int i = 0; i < dimensionCount; i++) {
				sumOfVarDiff = sumOfVarDiff + vector[i] * vector[i];
			}
			// �������Χ�����˳�
			if (Math.sqrt(sumOfVarDiff) < 1) {
				break;
			}
			// ������������
			for (int i = 0; i < dimensionCount; i++) {
				currentCenter[i] = currentCenter[i] + vector[i];
			}
		}

		System.out.println("�����");
		for (int i = 0; i < dimensionCount; i++) {
			System.out.print(currentCenter[i] + " ");
		}

	}

}

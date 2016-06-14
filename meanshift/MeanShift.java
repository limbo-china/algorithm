package meanshift_java;
import java.util.Random;
import java.util.Scanner;

public class MeanShift {

	private final static Double MAX = 100.0;
	private final static Double MIN = 0.0;
	private final static Double Radius = 20.0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("输入数据维度数量");
		Scanner sc = new Scanner(System.in);
		System.out.println("输入随机生成的点的数量");
		int dataCount = 0;
		dataCount = Integer.parseInt(sc.nextLine());
		int dimensionCount = 0;
		System.out.println("输入数据维度数量");
		dimensionCount = Integer.parseInt(sc.nextLine());
		sc.close();
		double[][] originalData = new double[dataCount][dimensionCount];
		double[] currentCenter = new double[dimensionCount];
		// 生成随机数据
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
			// 初始化向量
			double[] vector = new double[dimensionCount];
			for (int i = 0; i < dimensionCount; i++) {
				vector[i] = 0.0;
			}
			// 如果点在圆内，则加上向量差
			for (int i = 0; i < dataCount; i++) {
				double sumOfDiff = 0.0;
				for (int j = 0; j < dimensionCount; j++) {
					sumOfDiff = sumOfDiff
							+ (currentCenter[j] - originalData[i][j])
							* (currentCenter[j] - originalData[i][j]);
				}
				// 如果点在高维球内则加上向量差
				if (Math.sqrt(sumOfDiff) < Radius) {
					for (int k = 0; k < dimensionCount; k++) {
						vector[k] = vector[k] + originalData[i][k]
								- currentCenter[k];
					}
				}
			}
			// 计算误差和
			double sumOfVarDiff = 0.0;
			for (int i = 0; i < dimensionCount; i++) {
				sumOfVarDiff = sumOfVarDiff + vector[i] * vector[i];
			}
			// 如果在误差范围内则退出
			if (Math.sqrt(sumOfVarDiff) < 1) {
				break;
			}
			// 更新中心向量
			for (int i = 0; i < dimensionCount; i++) {
				currentCenter[i] = currentCenter[i] + vector[i];
			}
		}

		System.out.println("结果：");
		for (int i = 0; i < dimensionCount; i++) {
			System.out.print(currentCenter[i] + " ");
		}

	}

}

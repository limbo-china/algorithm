import java.util.Scanner;
import java.util.StringTokenizer;

public class KMeans {
    public static void main(String[] args) {        
        Scanner sc = new Scanner(System.in);
        StringTokenizer st;
        int featureCount = 0, dataCount = 0;
        System.out.print("Enter the number of fearures: ");
        featureCount = Integer.parseInt(sc.nextLine());
        System.out.print("Enter the number of datasets: ");
        dataCount = Integer.parseInt(sc.nextLine());
        double dataSet[][] = new double[dataCount][featureCount];
        System.out.println("Input data(<feature><space><feature>....<space><feature>): ");
        for(int i = 0; i < dataCount; ++i) {
            st = new StringTokenizer(sc.nextLine());
            for(int j = 0; j < featureCount; ++j) {
                dataSet[i][j] = Double.parseDouble(st.nextToken());
            }
        }
        System.out.print("Enter the value of k: ");
        int k = 0;
        k = Integer.parseInt(sc.nextLine());
        double centroid[][] = new double[k][featureCount];
        //consider first k rows for calculating initial centroids
        for(int  i = 0; i < k; ++i) {
            for(int j = 0; j < featureCount; ++j) {
                centroid[i][j] = dataSet[i][j];
            }
        }
        byte[][] finalResult = new byte[k][dataCount];
        boolean flag;
        while(true) {
            flag = true;
            //calculating pairwise distance from the centroids
            double tempDataSet[][] = new double[k][dataCount];
            for(int i = 0; i < k; ++i) {
                for(int j = 0; j < dataCount; ++j) {
                    double sum = 0.0;
                    for(int m = 0; m < featureCount; ++m) {
                        sum += Math.pow((centroid[i][m] - dataSet[j][m]) , 2.0);
                    }
                    tempDataSet[i][j] = Math.sqrt(sum);
                    //System.out.println("temp "+ tempDataSet[i][j]);
                }
            }
            byte groupData[][] = new byte[k][dataCount];
            for(int i = 0; i < dataCount; ++i) {
                double min = 9999999;
                int r = 0, c = 0;
                for(int j = 0; j < k ; ++j) {
                    if(tempDataSet[j][i] < min) {
                        min = tempDataSet[j][i];
                        r = j;
                        c = i;
                    }
                }
                //groupdata[i][j]=1 则将j数据划分到i中
                groupData[r][c] = 1; 
                //System.out.println("min: " + min +" row: "+r+ "col: "+c);
            }
//            for(int i = 0; i < k; ++i){
//                for(int j = 0; j < dataCount; ++j){
//                    System.out.print(groupData[i][j] + "  ");
//                }
//                System.out.println("\n");
//            }
            //checking whether the groupData is same as the previous finalResult
            for(int i = 0; i < k; ++i ) {
                for(int j = 0; j < dataCount; ++j) {
                    if(groupData[i][j] != finalResult[i][j]) {
                        flag = false;
                        break;
                    }
                }
            }
            //updating the finalResult by new groupData
            finalResult = groupData;
            if(flag)
                break;
            //否则计算每个group的新中心
            for(int i = 0; i < k; ++i) {
                int count = 0;
                double temp[] = new double[featureCount];
                for(int j = 0; j < dataCount; ++j) {
                    if(groupData[i][j] == 1) {
                        count++;
                        //某个维度的数据总和
                        for(int m = 0; m < featureCount; ++m) {
                            temp[m] += dataSet[j][m];
                        }
                    }
                }
                //updating centroids
                for(int n = 0; n < featureCount; ++n) {
                    centroid[i][n] = temp[n] / count;
                    //System.out.println(centroid[i][n]);
                }
                temp = null;
            }
            tempDataSet = null;
            groupData = null;
        }
        for(int i = 0; i < k; ++i){
            System.out.print("Cluster " + (i + 1) + " contains: ");
            for(int j = 0; j < dataCount; ++j) {
                if(finalResult[i][j] == 1) {
                    System.out.print((j + 1) + " ");
                }
                //System.out.print(finalResult[i][j] + "  ");
            }
            System.out.println("\n");
        }
    }
}

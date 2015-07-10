package cluster.kmeans;

/**
 * K-means（K均值）算法调用类
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/cluster/kmeans/input.txt";
		
		int classNum = 3;
		
		KMeansTool tool = new KMeansTool(filePath, classNum);
		tool.kMeansClustering();
	}
}

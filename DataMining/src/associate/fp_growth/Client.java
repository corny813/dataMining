package associate.fp_growth;

/**
 * FPTree频繁模式树算法
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/associate/fp_growth/input.txt";
		//最小支持度阈值
		int minSupportCount = 2;
		
		FPTreeTool tool = new FPTreeTool(filePath, minSupportCount);
		tool.startBuildingTree();
	}
}

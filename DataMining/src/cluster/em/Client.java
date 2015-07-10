package cluster.em;

/**
 * EM期望最大化算法场景调用类
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/cluster/em/input.txt";
		
		EMTool tool = new EMTool(filePath);
		
		tool.readDataFile();
		tool.exceptMaxStep();
	}
}

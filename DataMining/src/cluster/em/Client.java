package cluster.em;

/**
 * EM��������㷨����������
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

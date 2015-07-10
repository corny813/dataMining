package classify.id3_c45;

/**
 * ID3决策树分类算法测试场景类
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "src/classify/id3_c45/input.txt";
		
		ID3C45Tool tool = new ID3C45Tool(filePath);
		tool.startBuildingTree(false);
	}
}

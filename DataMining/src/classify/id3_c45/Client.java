package classify.id3_c45;

/**
 * ID3�����������㷨���Գ�����
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

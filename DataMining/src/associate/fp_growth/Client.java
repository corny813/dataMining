package associate.fp_growth;

/**
 * FPTreeƵ��ģʽ���㷨
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/associate/fp_growth/input.txt";
		//��С֧�ֶ���ֵ
		int minSupportCount = 2;
		
		FPTreeTool tool = new FPTreeTool(filePath, minSupportCount);
		tool.startBuildingTree();
	}
}

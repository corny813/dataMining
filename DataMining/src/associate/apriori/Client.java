package associate.apriori;

/**
 * Apriori���������ھ��㷨������
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		String filePath = "src/associate/apriori/input.txt";
		
		AprioriTool tool = new AprioriTool(filePath, 2);
		tool.printAttachRule(0.7);
	}
}

package associate.apriori;

/**
 * Apriori关联规则挖掘算法调用类
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

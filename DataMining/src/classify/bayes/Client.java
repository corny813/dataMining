package classify.bayes;

/**
 * ���ر�Ҷ˹�㷨����������
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/classify/bayes/input.txt";
		
		String testData = "Youth Medium Yes Fair";
		String test2 = "Youth High No Fair";
		
		NaiveBayesTool tool = new NaiveBayesTool(filePath);
		
		System.out.println(testData + " ���ݵķ���Ϊ:" + tool.naiveBayesClassify(testData));
		System.out.println(test2 + " ���ݵķ���Ϊ:" + tool.naiveBayesClassify(test2));
	}
}

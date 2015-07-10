package classify.bayes;

/**
 * 朴素贝叶斯算法场景调用类
 * @author corny
 *
 */
public class Client {
	public static void main(String[] args){
		
		String filePath = "src/classify/bayes/input.txt";
		
		String testData = "Youth Medium Yes Fair";
		String test2 = "Youth High No Fair";
		
		NaiveBayesTool tool = new NaiveBayesTool(filePath);
		
		System.out.println(testData + " 数据的分类为:" + tool.naiveBayesClassify(testData));
		System.out.println(test2 + " 数据的分类为:" + tool.naiveBayesClassify(test2));
	}
}

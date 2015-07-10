package classify.bayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ���ر�Ҷ˹�㷨������
 * 
 * @author corny
 * 
 */
public class NaiveBayesTool {

	private String YES = "Yes"; // ���Ƿ��������Ϊ2�࣬YES��NO
	private String NO = "No";
	private String filePath; // �ѷ���ѵ�����ݼ��ļ�·��

	private String[] attrNames; // ������������
	private String[][] data; // ѵ�����ݼ�
	private HashMap<String, ArrayList<String>> attrValue; // ÿ�����Ե�ֵ��������

	public NaiveBayesTool(String filePath) {
		this.filePath = filePath;
		readDataFile();
		initAttrValue();
	}

	/**
	 * ���ļ��ж�ȡ����
	 */
	private void readDataFile() {
		File file = new File(filePath);
		ArrayList<String[]> dataArray = new ArrayList<String[]>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			String[] tempArray;
			while ((str = in.readLine()) != null) {
				tempArray = str.split(" ");
				dataArray.add(tempArray);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		data = new String[dataArray.size()][];
		dataArray.toArray(data);
		attrNames = data[0];
	}

	/**
	 * ���ȳ�ʼ��ÿ�����Ե�ֵ���������ͣ����ں���������صļ���ʱ��
	 */
	private void initAttrValue() {
		attrValue = new HashMap<>();
		ArrayList<String> tempValues;

		for (int j = 1; j < attrNames.length; j++) {
			tempValues = new ArrayList<>();
			for (int i = 1; i < data.length; i++) {
				if (!tempValues.contains(data[i][j])) {
					tempValues.add(data[i][j]);
				}
			}
			attrValue.put(data[0][j], tempValues);
		}
	}

	/**
	 * ��classType������£�����condition�����ĸ���
	 * 
	 * @param condition
	 *            ��������
	 * @param classType
	 *            ���������
	 * @return
	 */
	private double computeConditionProbaility(String condition, String classType) {
		
		int count = 0;		// ����������
		int attrIndex = 1;	// �������Ե�������
		
		ArrayList<String[]> yClassData = new ArrayList<>();	// yes���Ƿ�����
		ArrayList<String[]> nClassData = new ArrayList<>();	// no���Ƿ�����
		ArrayList<String[]> classData;

		for (int i = 1; i < data.length; i++) {
			if (data[i][attrNames.length - 1].equals(YES)) {
				yClassData.add(data[i]);
			} else {
				nClassData.add(data[i]);
			}
		}

		if (classType.equals(YES)) {
			classData = yClassData;
		} else {
			classData = nClassData;
		}

		// ���û�����������򣬼�����Ǵ�������¼�����
		if (condition == null) {
			return 1.0 * classData.size() / (data.length - 1);
		}

		// Ѱ�Ҵ�������������
		attrIndex = getConditionAttrName(condition);

		for (String[] s : classData) {
			if (s[attrIndex].equals(condition)) {
				count++;
			}
		}

		return 1.0 * count / classData.size();
	}

	/**
	 * ��������ֵ���������������Ե���ֵ
	 * @param condition
	 * @return
	 */
	private int getConditionAttrName(String condition) {
		
		String attrName = "";		// ��������������
		int attrIndex = 1;			// ������������������
	
		ArrayList<String[]> valueTypes; 	// ��ʱ����ֵ����
		
		for (Map.Entry entry : attrValue.entrySet()) {
			valueTypes = (ArrayList<String[]>) entry.getValue();
			if (valueTypes.contains(condition)
					&& !((String) entry.getKey()).equals("BuysComputer")) {
				attrName = (String) entry.getKey();
			}
		}

		for (int i = 0; i < attrNames.length - 1; i++) {
			if (attrNames[i].equals(attrName)) {
				attrIndex = i;
				break;
			}
		}

		return attrIndex;
	}

	/**
	 * �������ر�Ҷ˹����
	 * @param data
	 */
	public String naiveBayesClassify(String data) {
		
		String[] dataFeatures;		// �������ݵ�����ֵ����
		
		double xWhenYes = 1.0;		// ��yes�������£�x�¼������ĸ���
		double xWhenNo = 1.0;		// ��no�������£�x�¼������ĸ���
		double pYes = 1;			// yes��no������ܸ��ʣ���P(X|Ci)*P(Ci)�Ĺ�ʽ����
		double pNo = 1;

		dataFeatures = data.split(" ");
		for (int i = 0; i < dataFeatures.length; i++) {
			// ��Ϊ���ر�Ҷ˹�㷨�������������ģ����Կ��Խ����ۻ��ļ���
			xWhenYes *= computeConditionProbaility(dataFeatures[i], YES);
			xWhenNo *= computeConditionProbaility(dataFeatures[i], NO);
		}

		pYes = xWhenYes * computeConditionProbaility(null, YES);
		pNo = xWhenNo * computeConditionProbaility(null, NO);

		return (pYes > pNo ? YES : NO);
	}

}

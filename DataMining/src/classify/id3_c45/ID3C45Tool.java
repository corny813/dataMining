package classify.id3_c45;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * �㷨ʵ���࣬��ѡ��ʹ��ID3����C45��
 * C4.5�㷨�����˸Ľ����õ�����Ϣ����������,�˷�������Ϣ����ѡ������ʱƫ��ѡ��ȡֵ������ԵĲ���
 * @author corny
 *
 */
public class ID3C45Tool {

	private final String YES = "Yes"; // ���ŵ�ֵ����
	private final String NO = "No";
	private int attrNum; // ��������,��dataԴ���ݵ�����
	private String filePath;

	private String[][] data; // ��ʼԴ����
	private String[] attrNames; // ������

	private HashMap<String, ArrayList<String>> attrValue; // ÿ�����Ե�ֵ��������

	public ID3C45Tool(String filePath) {
		this.filePath = filePath;
		attrValue = new HashMap<>();
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
		attrNum = data[0].length;
		attrNames = data[0];
	}

	/**
	 * ���ȳ�ʼ��ÿ�����Ե�ֵ���������ͣ����ں���������صļ���ʱ��
	 */
	private void initAttrValue() {
		ArrayList<String> tempValues;

		for (int j = 1; j < attrNum; j++) {
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
	 * �������ݰ��ղ�ͬ��ʽ���ֵ���
	 * @param remainData    ʣ�������
	 * @param attrName      �����ֵ����ԣ�������Ϣ�����ʱ���ʹ�õ�
	 * @param attrValue     ���ֵ�������ֵ
	 * @param isParent     �Ƿ�������Ի��ֻ���ԭ������Ļ���
	 */
	private double computeEntropy(String[][] remainData, String attrName,
			String value, boolean isParent) {
		
		int total = 0;		// ʵ������
		int posNum = 0;		// ��ʵ����
		int negNum = 0;		// ��ʵ����

		for (int j = 1; j < attrNames.length; j++) {
			if (attrName.equals(attrNames[j])) {
				for (int i = 1; i < remainData.length; i++) {
					boolean flag = !isParent && remainData[i][j].equals(value);
					if (isParent|| flag ) {
						if (remainData[i][attrNames.length - 1].equals(YES)) {
							posNum++;
						} else {
							negNum++;
						}
					}
				}
			}
		}

		total = posNum + negNum;
		double posProbobly = (double) posNum / total;
		double negProbobly = (double) negNum / total;

		if (posProbobly == 1 || posProbobly == 0) {
			return 0;
		}

		double entropyValue = -posProbobly * Math.log(posProbobly)
				/ Math.log(2.0) - negProbobly * Math.log(negProbobly)
				/ Math.log(2.0);

		return entropyValue;
	}

	/**
	 * Ϊĳ�����Լ�����Ϣ����
	 * @param remainData   ʣ�������
	 * @param value       �����ֵ���������
	 * @return
	 */
	private double computeGain(String[][] remainData, String value) {
		
		double gainValue = 0;
		double entropyOri = 0;		// Դ�صĴ�С���������Ի��ֺ���бȽ�
		double childEntropySum = 0;	// �ӻ����غ�
		int childValueNum = 0;				// ���������͵ĸ���
		
		ArrayList<String> attrTypes = attrValue.get(value);	// ����ֵ������
		HashMap<String, Integer> ratioValues = new HashMap<>();	// �����Զ�Ӧ��Ȩ�ر�

		for (int i = 0; i < attrTypes.size(); i++) {
			// ���ȶ�ͳһ����Ϊ0
			ratioValues.put(attrTypes.get(i), 0);
		}

		for (int j = 1; j < attrNames.length; j++) {
			if (value.equals(attrNames[j])) {
				for (int i = 1; i <= remainData.length - 1; i++) {
					childValueNum = ratioValues.get(remainData[i][j]);
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
			}
		}

		// ����ԭ�صĴ�С
		entropyOri = computeEntropy(remainData, value, null, true);
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			childEntropySum += ratio
					* computeEntropy(remainData, value, attrTypes.get(i), false);
		}

		// ���������������Ϣ����
		gainValue = entropyOri - childEntropySum;
		return gainValue;
	}

	/**
	 * ������Ϣ�����
	 * @param remainData    ʣ������
	 * @param value        ����������
	 * @return
	 */
	private double computeGainRatio(String[][] remainData, String value) {
		
		double gain = 0;
		double spiltInfo = 0;
		int childValueNum = 0;
		
		ArrayList<String> attrTypes = attrValue.get(value);		// ����ֵ������
		HashMap<String, Integer> ratioValues = new HashMap<>();		// �����Զ�Ӧ��Ȩ�ر�

		for (int i = 0; i < attrTypes.size(); i++) {
			ratioValues.put(attrTypes.get(i), 0);
		}

		for (int j = 1; j < attrNames.length; j++) {
			if (value.equals(attrNames[j])) {
				for (int i = 1; i <= remainData.length - 1; i++) {
					childValueNum = ratioValues.get(remainData[i][j]);
					childValueNum++;
					ratioValues.put(remainData[i][j], childValueNum);
				}
			}
		}

		// ������Ϣ����
		gain = computeGain(remainData, value);
		
		// ���������Ϣ��������Ϣ����������Ϊ(������Ϣ�����������Է������ݵĹ�Ⱥ;���)��
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			spiltInfo += -ratio * Math.log(ratio) / Math.log(2.0);
		}

		// �������Ϣ������
		return gain / spiltInfo;
	}

	/**
	 * ����Դ���ݹ��������
	 * ID3�㷨���õ��ǰ�����Ϣ�����ֵ����
	 * C4.5�㷨�����˸Ľ����õ�����Ϣ����������,�˷�������Ϣ����ѡ������ʱƫ��ѡ��ȡֵ������ԵĲ���
	 */
	private void buildDecisionTree(AttrNode node, String parentAttrValue,
			String[][] remainData, ArrayList<String> remainAttr, boolean isID3) {
		node.setParentAttrValue(parentAttrValue);

		String attrName = "";
		double gainValue = 0;
		double tempValue = 0;

		// ���ֻ��1��������ֱ�ӷ���
		if (remainAttr.size() == 1) {
			System.out.println("attr null");
			return;
		}

		// ѡ��ʣ����������Ϣ����������Ϊ��һ�����������
		for (int i = 0; i < remainAttr.size(); i++) {
			if (isID3) {
				tempValue = computeGain(remainData, remainAttr.get(i));
			} else {
				tempValue = computeGainRatio(remainData, remainAttr.get(i));
			}

			if (tempValue > gainValue) {
				gainValue = tempValue;
				attrName = remainAttr.get(i);
			}
		}

		node.setAttrName(attrName);
		ArrayList<String> valueTypes = attrValue.get(attrName);
		remainAttr.remove(attrName);

		AttrNode[] childNode = new AttrNode[valueTypes.size()];
		String[][] rData;
		for (int i = 0; i < valueTypes.size(); i++) {
			// �Ƴ��Ǵ�ֵ���͵�����
			rData = removeData(remainData, attrName, valueTypes.get(i));

			childNode[i] = new AttrNode();
			boolean sameClass = true;
			ArrayList<String> indexArray = new ArrayList<>();
			for (int k = 1; k < rData.length; k++) {
				indexArray.add(rData[k][0]);
				// �ж��Ƿ�Ϊͬһ���
				if (!rData[k][attrNames.length - 1]
						.equals(rData[1][attrNames.length - 1])) {
					// ֻҪ��1������ȣ��Ͳ���ͬ���͵�
					sameClass = false;
					break;
				}
			}

			if (!sameClass) {
				// �����µĶ������ԣ������ͬ�����û����
				ArrayList<String> rAttr = new ArrayList<>();
				for (String str : remainAttr) {
					rAttr.add(str);
				}

				buildDecisionTree(childNode[i], valueTypes.get(i), rData,
						rAttr, isID3);
			} else {
				// �����ͬ�����ͣ���ֱ��Ϊ���ݽڵ�
				childNode[i].setParentAttrValue(valueTypes.get(i));
				childNode[i].setChildDataIndex(indexArray);
			}

		}
		node.setChildAttrNode(childNode);
	}

	/**
	 * ���Ի�����ϣ��������ݵ��Ƴ�
	 * @param srcData
	 * @param attrName
	 * @param valueType
	 * @return
	 */
	private String[][] removeData(String[][] srcData, String attrName,
			String valueType) {
		String[][] desDataArray;
		ArrayList<String[]> desData = new ArrayList<>();
		// ��ɾ������
		ArrayList<String[]> selectData = new ArrayList<>();
		selectData.add(attrNames);

		// ��������ת�����б��У������Ƴ�
		for (int i = 0; i < srcData.length; i++) {
			desData.add(srcData[i]);
		}

		for (int j = 1; j < attrNames.length; j++) {
			if (attrNames[j].equals(attrName)) {
				for (int i = 1; i < desData.size(); i++) {
					if (desData.get(i)[j].equals(valueType)) {
						selectData.add(desData.get(i));
					}
				}
			}
		}

		desDataArray = new String[selectData.size()][];
		selectData.toArray(desDataArray);

		return desDataArray;
	}

	/**
	 * ����������
	 * @param isID3  ����ID3(true)����C45(false)
	 */
	public void startBuildingTree(boolean isID3) {
		readDataFile();
		initAttrValue();

		ArrayList<String> remainAttr = new ArrayList<>();
		// ������ԣ��������һ����������
		for (int i = 1; i < attrNames.length - 1; i++) {
			remainAttr.add(attrNames[i]);
		}

		AttrNode rootNode = new AttrNode();
		buildDecisionTree(rootNode, "", data, remainAttr, isID3);
		showDecisionTree(rootNode, 0);
	}

	/**
	 * ��ʾ������
	 * @param node ��ʾ�Ľڵ�
	 * @param blankNum  �ո����������ʾ���ͽṹ
	 */
	private void showDecisionTree(AttrNode node, int blankNum) {
		System.out.println();
		 for (int i = 0; i < blankNum; i++) {
			 System.out.print("\t");
		 }
		System.out.print("--");
		
		String value = node.getParentAttrValue();
		if (value != null && value.length() > 0) {
			System.out.print(value);
		}
		System.out.print("--");

		ArrayList<String> datas = node.getChildDataIndex();
		if (datas != null && datas.size() > 0) {
			String i = (String) datas.get(0);
			System.out.print(data[Integer.parseInt(i)][attrNames.length - 1]);
			System.out.print("[");
			
			for(int j=0;j<datas.size()-1;j++){
				System.out.print(datas.get(j)+", ");
			}
			System.out.print(datas.get(datas.size()-1));
			System.out.print("]");
		} else {
			// �ݹ���ʾ�ӽڵ�
			System.out.print("��" + node.getAttrName() + "��");
			for (AttrNode childNode : node.getChildAttrNode()) {
				showDecisionTree(childNode, 2 * blankNum+1);
			}
		}

	}

}

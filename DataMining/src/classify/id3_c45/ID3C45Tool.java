package classify.id3_c45;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 算法实现类，可选择使用ID3还是C45。
 * C4.5算法进行了改进，用的是信息增益率来比,克服了用信息增益选择属性时偏向选择取值多的属性的不足
 * @author corny
 *
 */
public class ID3C45Tool {

	private final String YES = "Yes"; // 类标号的值类型
	private final String NO = "No";
	private int attrNum; // 属性数量,即data源数据的列数
	private String filePath;

	private String[][] data; // 初始源数据
	private String[] attrNames; // 属性名

	private HashMap<String, ArrayList<String>> attrValue; // 每个属性的值所有类型

	public ID3C45Tool(String filePath) {
		this.filePath = filePath;
		attrValue = new HashMap<>();
	}

	/**
	 * 从文件中读取数据
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
	 * 首先初始化每种属性的值的所有类型，用于后面的子类熵的计算时用
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
	 * 计算数据按照不同方式划分的熵
	 * @param remainData    剩余的数据
	 * @param attrName      待划分的属性，在算信息增益的时候会使用到
	 * @param attrValue     划分的子属性值
	 * @param isParent     是否分子属性划分还是原来不变的划分
	 */
	private double computeEntropy(String[][] remainData, String attrName,
			String value, boolean isParent) {
		
		int total = 0;		// 实例总数
		int posNum = 0;		// 正实例数
		int negNum = 0;		// 负实例数

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
	 * 为某个属性计算信息增益
	 * @param remainData   剩余的数据
	 * @param value       待划分的属性名称
	 * @return
	 */
	private double computeGain(String[][] remainData, String value) {
		
		double gainValue = 0;
		double entropyOri = 0;		// 源熵的大小将会与属性划分后进行比较
		double childEntropySum = 0;	// 子划分熵和
		int childValueNum = 0;				// 属性子类型的个数
		
		ArrayList<String> attrTypes = attrValue.get(value);	// 属性值的种数
		HashMap<String, Integer> ratioValues = new HashMap<>();	// 子属性对应的权重比

		for (int i = 0; i < attrTypes.size(); i++) {
			// 首先都统一计数为0
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

		// 计算原熵的大小
		entropyOri = computeEntropy(remainData, value, null, true);
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			childEntropySum += ratio
					* computeEntropy(remainData, value, attrTypes.get(i), false);
		}

		// 二者熵相减就是信息增益
		gainValue = entropyOri - childEntropySum;
		return gainValue;
	}

	/**
	 * 计算信息增益比
	 * @param remainData    剩余数据
	 * @param value        待划分属性
	 * @return
	 */
	private double computeGainRatio(String[][] remainData, String value) {
		
		double gain = 0;
		double spiltInfo = 0;
		int childValueNum = 0;
		
		ArrayList<String> attrTypes = attrValue.get(value);		// 属性值的种数
		HashMap<String, Integer> ratioValues = new HashMap<>();		// 子属性对应的权重比

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

		// 计算信息增益
		gain = computeGain(remainData, value);
		
		// 计算分裂信息，分裂信息度量被定义为(分裂信息用来衡量属性分裂数据的广度和均匀)：
		for (int i = 0; i < attrTypes.size(); i++) {
			double ratio = (double) ratioValues.get(attrTypes.get(i))
					/ (remainData.length - 1);
			spiltInfo += -ratio * Math.log(ratio) / Math.log(2.0);
		}

		// 计算机信息增益率
		return gain / spiltInfo;
	}

	/**
	 * 利用源数据构造决策树
	 * ID3算法采用的是按照信息增益的值来比
	 * C4.5算法进行了改进，用的是信息增益率来比,克服了用信息增益选择属性时偏向选择取值多的属性的不足
	 */
	private void buildDecisionTree(AttrNode node, String parentAttrValue,
			String[][] remainData, ArrayList<String> remainAttr, boolean isID3) {
		node.setParentAttrValue(parentAttrValue);

		String attrName = "";
		double gainValue = 0;
		double tempValue = 0;

		// 如果只有1个属性则直接返回
		if (remainAttr.size() == 1) {
			System.out.println("attr null");
			return;
		}

		// 选择剩余属性中信息增益最大的作为下一个分类的属性
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
			// 移除非此值类型的数据
			rData = removeData(remainData, attrName, valueTypes.get(i));

			childNode[i] = new AttrNode();
			boolean sameClass = true;
			ArrayList<String> indexArray = new ArrayList<>();
			for (int k = 1; k < rData.length; k++) {
				indexArray.add(rData[k][0]);
				// 判断是否为同一类的
				if (!rData[k][attrNames.length - 1]
						.equals(rData[1][attrNames.length - 1])) {
					// 只要有1个不相等，就不是同类型的
					sameClass = false;
					break;
				}
			}

			if (!sameClass) {
				// 创建新的对象属性，对象的同个引用会出错
				ArrayList<String> rAttr = new ArrayList<>();
				for (String str : remainAttr) {
					rAttr.add(str);
				}

				buildDecisionTree(childNode[i], valueTypes.get(i), rData,
						rAttr, isID3);
			} else {
				// 如果是同种类型，则直接为数据节点
				childNode[i].setParentAttrValue(valueTypes.get(i));
				childNode[i].setChildDataIndex(indexArray);
			}

		}
		node.setChildAttrNode(childNode);
	}

	/**
	 * 属性划分完毕，进行数据的移除
	 * @param srcData
	 * @param attrName
	 * @param valueType
	 * @return
	 */
	private String[][] removeData(String[][] srcData, String attrName,
			String valueType) {
		String[][] desDataArray;
		ArrayList<String[]> desData = new ArrayList<>();
		// 待删除数据
		ArrayList<String[]> selectData = new ArrayList<>();
		selectData.add(attrNames);

		// 数组数据转化到列表中，方便移除
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
	 * 建立决策树
	 * @param isID3  采用ID3(true)还是C45(false)
	 */
	public void startBuildingTree(boolean isID3) {
		readDataFile();
		initAttrValue();

		ArrayList<String> remainAttr = new ArrayList<>();
		// 添加属性，除了最后一个类标号属性
		for (int i = 1; i < attrNames.length - 1; i++) {
			remainAttr.add(attrNames[i]);
		}

		AttrNode rootNode = new AttrNode();
		buildDecisionTree(rootNode, "", data, remainAttr, isID3);
		showDecisionTree(rootNode, 0);
	}

	/**
	 * 显示决策树
	 * @param node 显示的节点
	 * @param blankNum  空格符，用于显示树型结构
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
			// 递归显示子节点
			System.out.print("【" + node.getAttrName() + "】");
			for (AttrNode childNode : node.getChildAttrNode()) {
				showDecisionTree(childNode, 2 * blankNum+1);
			}
		}

	}

}

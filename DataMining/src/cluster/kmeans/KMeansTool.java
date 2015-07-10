package cluster.kmeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;

/**
 * KMeans算法工具类
 * @author corny
 *
 */
public class KMeansTool {
	
	private String filePath;		// 输入数据文件地址
	private int classNum;		// 分类类别个数
	
	private ArrayList<String> classNames;		// 类名称
	private ArrayList<Point> classPoints;		// 聚类坐标点
	private ArrayList<Point> totalPoints;		// 所有的数据左边点

	public KMeansTool(String filePath, int classNum) {
		this.filePath = filePath;
		this.classNum = classNum;
		readDataFile();
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

		classPoints = new ArrayList<>();
		totalPoints = new ArrayList<>();
		classNames = new ArrayList<>();
		for (int i = 0, j = 1; i < dataArray.size(); i++) {
			if (j <= classNum) {
				classPoints.add(new Point(dataArray.get(i)[0],
						dataArray.get(i)[1], j + ""));
				classNames.add(i + "");
				j++;
			}
			totalPoints
					.add(new Point(dataArray.get(i)[0], dataArray.get(i)[1]));
		}
	}

	/**
	 * K均值聚类算法实现
	 */
	public void kMeansClustering() {
		double tempX = 0;
		double tempY = 0;
		int count = 0;
		double error = Integer.MAX_VALUE;
		Point temp;

		while (error > 0.01 * classNum) {
			for (Point p1 : totalPoints) {
				for (Point p2 : classPoints) {
					p2.computerDistance(p1);
				}
				Collections.sort(classPoints);

				p1.setClassName(classPoints.get(0).getClassName());
			}

			error = 0;
			for (Point p1 : classPoints) {
				count = 0;
				tempX = 0;
				tempY = 0;
				for (Point p : totalPoints) {
					if (p.getClassName().equals(p1.getClassName())) {
						count++;
						tempX += p.getX();
						tempY += p.getY();
					}
				}
				tempX /= count;
				tempY /= count;

				error += Math.abs((tempX - p1.getX()));
				error += Math.abs((tempY - p1.getY()));
				// 计算均值
				p1.setX(tempX);
				p1.setY(tempY);

			}
			
			for (int i = 0; i < classPoints.size(); i++) {
				temp = classPoints.get(i);
				System.out.println(MessageFormat.format("聚类中心点{0}，x={1},y={2}",
						(i + 1), temp.getX(), temp.getY()));
			}
			System.out.println("----------");
		}

		System.out.println("结果值收敛");
		for (int i = 0; i < classPoints.size(); i++) {
			temp = classPoints.get(i);
			System.out.println(MessageFormat.format("聚类中心点{0}，x={1},y={2}",
					(i + 1), temp.getX(), temp.getY()));
		}

	}

}

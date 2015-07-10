package associate.apriori;

/**
 * Ƶ���
 * 
 * @author corny
 * 
 */
public class FrequentItem implements Comparable<FrequentItem>{
	
	private String[] idArray;		// Ƶ����ļ���ID
	
	private int count;		// Ƶ�����֧�ֶȼ���
	private int length;		//Ƶ����ĳ��ȣ�1�����2��������3�
	
	public FrequentItem(String[] idArray, int count){
		this.idArray = idArray;
		this.count = count;
		length = idArray.length;
	}

	public String[] getIdArray() {
		return idArray;
	}

	public void setIdArray(String[] idArray) {
		this.idArray = idArray;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public int compareTo(FrequentItem o) {
		Integer int1 = Integer.parseInt(this.getIdArray()[0]);
		Integer int2 = Integer.parseInt(o.getIdArray()[0]);
		
		return int1.compareTo(int2);
	}
	
}

package classify.id3_c45;

import java.util.ArrayList;

/**
 * ���Խڵ�
 * @author corny
 *
 */
public class AttrNode {
	
	private String attrName;	//��ǰ���Ե�����
	private String parentAttrValue;	//���ڵ�ķ�������ֵ
	private AttrNode[] childAttrNode;	//�����ӽڵ�
	private ArrayList<String> childDataIndex;	//����Ҷ�ӽڵ�

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public AttrNode[] getChildAttrNode() {
		return childAttrNode;
	}

	public void setChildAttrNode(AttrNode[] childAttrNode) {
		this.childAttrNode = childAttrNode;
	}

	public String getParentAttrValue() {
		return parentAttrValue;
	}

	public void setParentAttrValue(String parentAttrValue) {
		this.parentAttrValue = parentAttrValue;
	}

	public ArrayList<String> getChildDataIndex() {
		return childDataIndex;
	}

	public void setChildDataIndex(ArrayList<String> childDataIndex) {
		this.childDataIndex = childDataIndex;
	}
}

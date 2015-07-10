package classify.id3_c45;

import java.util.ArrayList;

/**
 * 属性节点
 * @author corny
 *
 */
public class AttrNode {
	
	private String attrName;	//当前属性的名字
	private String parentAttrValue;	//父节点的分类属性值
	private AttrNode[] childAttrNode;	//属性子节点
	private ArrayList<String> childDataIndex;	//孩子叶子节点

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

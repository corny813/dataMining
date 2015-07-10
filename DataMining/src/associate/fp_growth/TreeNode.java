package associate.fp_growth;

import java.util.ArrayList;

/**
 * FP���ڵ�
 * @author corny
 *
 */
public class TreeNode implements Comparable<TreeNode>, Cloneable{
	
	private String name;		// �ڵ��������
	private Integer count;		// ��������
	private TreeNode parentNode;	// ���׽ڵ�
	
	private ArrayList<TreeNode> childNodes;		// ���ӽڵ㣬����Ϊ���
	
	public TreeNode(String name, int count){
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public ArrayList<TreeNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(ArrayList<TreeNode> childNodes) {
		this.childNodes = childNodes;
	}

	@Override
	public int compareTo(TreeNode o) {
		// TODO Auto-generated method stub
		return o.getCount().compareTo(this.getCount());
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		//��Ϊ�����ڲ������ã���Ҫ�������
		TreeNode node = (TreeNode)super.clone(); 
		if(this.getParentNode() != null){
			node.setParentNode((TreeNode) this.getParentNode().clone());
		}
		
		if(this.getChildNodes() != null){
			node.setChildNodes((ArrayList<TreeNode>) this.getChildNodes().clone());
		}
		
		return node;
	}
	
}
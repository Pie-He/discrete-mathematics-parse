package parse;

import graph.ShowTree;

import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	final static String LETTER_REG = "[A-Z]+(_\\{\\d+\\})?";
	final static String CONNECTIVE_REG = "\\\\(and|or|not|imply|eq)";
	final static String PARENTHESE_REG = "\\(|\\)";
	final static String LEFT_PARENTHESE_REG = "\\(";
	final static String RIGHT_PARENTHESE_REG = "\\)";
	final static String ALLREG = "(" + LETTER_REG + ")|(" + CONNECTIVE_REG
			+ ")|(" + PARENTHESE_REG + ")";
	final static String REG = "((" + LETTER_REG + ")|(" + CONNECTIVE_REG
			+ ")|(" + PARENTHESE_REG + ")|(\\s))+";

	LinkedList<Symbol> symbols = new LinkedList<Symbol>();
	LinkedList<Node> tree = new LinkedList<Node>();

	public void parse(String str) {
		boolean flag = true;
		Pattern pattern = Pattern.compile(REG);
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			System.out.println("not well defined");
			flag = false;
			return;
		}
		pattern = Pattern.compile(ALLREG);
		matcher = pattern.matcher(str);
		int index = 0;
		while (matcher.find()) {
			String s = matcher.group();
			index = str.indexOf(s, index);
			//System.out.println("index1:"+index); 
			index += s.length();
			//System.out.println(s);
			//System.out.println("index:"+index);
			if (Pattern.matches(LETTER_REG, s)) {
				symbols.add(new Symbol(s, Type.LETTER));
			} else if (Pattern.matches(CONNECTIVE_REG, s)) {
				try {
					if (str.charAt(index) != ' ') {
						//System.out.println(index);
						System.out.println("not well defined");
						return;
					}
					if ((!s.equals("\\not"))&&str.charAt(index-s.length()-1) != ' ') {
						//System.out.println(s);
						System.out.println("not well defined");
						return;
					}
				} catch (IndexOutOfBoundsException e) {
					//System.out.println("exception");
				}
				//System.out.println("exceptionwww");
				symbols.add(new Symbol(s, Type.CONNECTIVE));
			} else if (Pattern.matches(LEFT_PARENTHESE_REG, s)) {
				symbols.add(new Symbol(s, Type.LEFT_PARENTHESE));
			} else if (Pattern.matches(RIGHT_PARENTHESE_REG, s)) {
				symbols.add(new Symbol(s, Type.RIGHT_PARENTHESE));
			} else {
				System.out.println("reg error");
			}
		}
		/*
		 * symbols.stream().forEach(i->{ System.out });
		 */
		if (!check()) {
			flag = false;
		}
		Node node = new Node(0, symbols.size());
		tree.add(node);
		if (!constructTree()) {
			flag = false;
		}
		if (!flag) {
			System.out.println("not well defined!");
			return;
		}
		// tree.stream().forEach(System.out::println);
		ShowTree.show(tree);
	}

	private boolean check() {
		Stack<Symbol> stack = new Stack<Symbol>();
		for (int i = 0; i < symbols.size(); i++) {
			Symbol s = symbols.get(i);
			s.index = i;
			if (s.str.equals("(")) {
				stack.push(s);
			} else if (s.str.equals(")")) {
				try {
					Symbol left = stack.pop();
					left.partner = i;
					s.partner = left.index;
				} catch (Exception e) {
					return false;
				}
			} else if (s.type == Type.LETTER) {
				s.partner = s.index;
			}
		}
		if (!stack.isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean constructTree() {
		boolean flag = true;
		while (!tree.stream().filter(i -> i.isLeaf())
				.allMatch(i -> i.isLetter())) {
			Node node = tree.stream()
					.filter(i -> (i.isLeaf() && !i.isLetter())).findFirst()
					.orElse(null);
			int i = node.leftIndex;
			int j = node.rightIndex;
			if (symbols.get(i).type != Type.LEFT_PARENTHESE) {
				flag = false;
				break;
			}
			if (symbols.get(i + 1).str.equals("\\not")) {
				int pindex = symbols.get(i + 2).partner;
				Node left = new Node(i + 2, pindex + 1);
				node.left = left;
				left.parent = node;
				// System.out.println(node);
				tree.add(left);
				if (j - pindex != 2)
					flag = false;
			} else {
				int index = symbols.get(i + 1).partner;
				// System.out.println(index);
				index++;
				Node left = new Node(i + 1, index);
				if (symbols.get(index).type != Type.CONNECTIVE)
					flag = false;
				index++;
				int pindex = symbols.get(index).partner;
				Node right = new Node(index, pindex + 1);
				node.left = left;
				node.right = right;
				left.parent = right.parent = node;
				tree.add(left);
				tree.add(right);
				if (j - pindex != 2)
					flag = false;
			}
		}
		return flag;
	}

	/*
	 * public boolean parse() { if (tree.stream().filter(i ->
	 * i.isLeaf).allMatch(i -> isLetter(i.pro))) return true; Node node =
	 * tree.stream().filter(i -> i.isLeaf && !isLetter(i.pro))
	 * .findFirst().orElse(null); return false; }
	 */

	public class Node {
		int leftIndex;
		int rightIndex;
		public Node left;
		public Node right;
		public Node parent;
		public final int height;
		public final int width;
		public final String text;

		Node(int left, int right) {
			leftIndex = left;
			rightIndex = right;
			text = this.getMessage();
			width = text.length() * 10;
			height = 30;
		}

		boolean isLeaf() {
			return left == null && right == null;
		}

		boolean isLetter() {
			return rightIndex - leftIndex == 1
					&& symbols.get(leftIndex).type == Type.LETTER;
		}

		private String getMessage() {
			StringBuilder sb = new StringBuilder();
			symbols.subList(leftIndex, rightIndex)
					.stream()
					.forEach(
							i -> {
								sb.append(i.str);
								if (i.type != Type.LEFT_PARENTHESE
										&& i.type != Type.RIGHT_PARENTHESE)
									sb.append(" ");
							});
			return sb.toString();
		}
	}

	private class Symbol {
		String str;
		Type type;
		int index;
		int partner;

		Symbol(String str, Type type) {
			this.str = str;
			this.type = type;
		}
	}
}

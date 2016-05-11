package parse;

import static org.junit.Assert.*;

public class Test {

	@org.junit.Test
	public void test() {
		Parse p=new Parse();
		p.parse("(\\not ((A_{1} \\and A_{2}) \\imply B))");
		//fail("Not yet implemented");
	}

}

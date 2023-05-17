package st;



import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class Task2_3_FunctionalTest {
	
	private Parser parser;
	private OptionMap optmap;
	
	@Before
	public void setUp() {
		parser = new Parser();
		optmap = new OptionMap();
	}
	
	@Test
	public void optionParse() {
		Option op = new Option("op", Type.BOOLEAN);
		op.setName("name");
		op.setType(Type.STRING);
		parser.addOption(op);
		parser.parse("--name value");
		assertEquals("value", parser.getString("name"));
	}
	
	@Test
	public void OptionToString() {
		Option toStringOp = new Option("toStringOp", Type.STRING);
		assertEquals("Option[name:toStringOp, value:, type:STRING]", toStringOp.toString());
	}
	
	@Test
	public void SameOptionEquality() {
		Option sameOp = new Option("sameOp", Type.STRING);
		assertEquals(true, sameOp.equals(sameOp));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void storeOptionMapNOTYPE() {
		optmap.store(new Option("name", Type.NOTYPE), null);
	}
	
	@Test
	public void storeSameName() {
		optmap.store(new Option("name", Type.BOOLEAN), "sh");
		optmap.store(new Option("name", Type.STRING), "sh");
		assertEquals(new Option("name", Type.STRING), optmap.getOption("name"));
	}
	
	@Test(expected = RuntimeException.class)
	public void getOptionException() {
		optmap.getOption(null);
	}

//TODO

}

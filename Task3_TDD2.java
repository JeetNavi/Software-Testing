package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD2 {

private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	@Test
	public void OneStringOptionExists() {
		parser.addAll("OneStrOption","oneStrOp","string");
		assertEquals(true, parser.optionExists("OneStrOption"));
	}
	
	@Test
	public void OneBooleanOptionExists() {
		parser.addAll("OneBoolOption","oneBoolOp","boolean");
		assertEquals(true, parser.optionExists("OneBoolOption"));
	}
	
	@Test
	public void OneIntegerOptionExists() {
		parser.addAll("OneIntOption","oneIntOp","integer");
		assertEquals(true, parser.optionExists("OneIntOption"));
	}
	
	@Test
	public void OneCharacterOptionExists() {
		parser.addAll("OneCharOption","oneCharOp","character");
		assertEquals(true, parser.optionExists("OneCharOption"));
	}
	
	@Test
	public void CorrectOptionTypeandShortcutStoredString() {
		parser.addAll("StringOp","strOp","string");
		assertEquals("Options Map: \n"
				+ "{StringOp=Option[name:StringOp, value:, type:STRING]}\n"
				+ "Shortcuts Map:\n"
				+ "{strOp=Option[name:StringOp, value:, type:STRING]}", parser.toString());
	}
	
	@Test
	public void CorrectOptionTypeAndShortcutStoredInteger() {
		parser.addAll("IntegerOp","intOp","integer");
		assertEquals("Options Map: \n"
				+ "{IntegerOp=Option[name:IntegerOp, value:, type:INTEGER]}\n"
				+ "Shortcuts Map:\n"
				+ "{intOp=Option[name:IntegerOp, value:, type:INTEGER]}", parser.toString());
	}
	
	@Test
	public void MultipleOptionExists() {
		parser.addAll("MultiOption1 MultiOption2","MO1 MO2","string boolean");
		assertEquals(true, parser.optionExists("MultiOption2"));
	}
	
	@Test
	public void WhiteSpaceTestOption() {
		parser.addAll("WhiteSpaceTest1       WhiteSpaceTest2", "WST1  WST2", "string    character");
		assertEquals(true, parser.optionExists("WhiteSpaceTest2"));
	}
	
	@Test
	public void WhiteSpaceTestShortcut() {
		parser.addAll("WhiteSpaceTest1       WhiteSpaceTest2", "WST1   WST2", "string    character");
		assertEquals(true, parser.shortcutExists("WST2"));
	}
	
	@Test
	public void InvalidNamedOptionIgnore1() {
		parser.addAll("1nvalidName", "inval", "string");
		assertEquals(false, parser.optionExists("1nvalidName"));
	}
	
	@Test
	public void InvalidNamedShortcutIgnore1() {
		parser.addAll("invalidShortcut", "invalid!", "string");
		assertEquals(false, parser.shortcutExists("invalid!"));
	}
	
	@Test
	public void ShortcutArgOptional() {
		parser.addAll("opt", "string");
		assertEquals(true, parser.optionExists("opt"));
	}
	
	@Test
	public void MoreOptionsThanShortcut() {
		parser.addAll("moreOps1 moreOps2 moreOps3", "short1", "string boolean character");
		//Then we can check by calling parser.toString(), that moreOps2 and 3 have no shortcut
	}
	
	@Test
	public void LessOptionsThanShortcut() {
		parser.addAll("op1", "sh1 sh2", "string");
		assertEquals(false, parser.shortcutExists("sh2"));
	}
	
	@Test
	public void LessTypes() {
		parser.addAll("o1 o2", "s1 s2", "string");
		assertEquals(Type.STRING, parser.getType("o2"));
	}
	
	@Test
	public void extraShortcutOmitted() {
		parser.addAll("o1", "s1, s2, s3", "string");
		assertEquals(false, parser.optionExists("s2"));
	}
	
	@Test
	public void caseInsensitiveType() {
		parser.addAll("o1", "sTrIng");
		assertEquals(Type.STRING, parser.getType("o1"));
	}
	
	@Test
	public void lastTypeUsed() {
		parser.addAll("o1 o2 o3 o4", "integer string");
		assertEquals(Type.STRING, parser.getType("o4"));
	}
	
	@Test
	public void invalidOptGroupIgnoreType() {
		parser.addAll("o1-p3 o2", "sh1 sh2", "string integer");
		assertEquals(Type.INTEGER, parser.getType("o2"));
		//so we know o1-p3 gets ignored along with string
	}
	
	@Test
	public void invalidOptGroupIgnoreShc() {
		parser.addAll("o1-p3 o2", "sh1 sh2", "string integer");
		assertEquals(false, parser.shortcutExists("sh1"));
		//so we know o1-p3 gets ignored along with sh1 and string 
	}
	
	@Test
	public void invalidShcGroupReplaceWithNoShortcut() {
		parser.addAll("o1 o2", "sh1-sh3 sh2", "string integer");
		assertEquals(true, parser.optionExists("o1"));
	}
	
	@Test
	public void typeAppliesToEntireGroup() {
		parser.addAll("o1-3 o4", "integer string");
		assertEquals(Type.STRING, parser.getType("o2"));
		//This is wrong implementation. My code assigns first type to first member of group, second
		//type to second member of group, and so on...
	}
	
	@Test
	public void testGroupDoubleDigit() {
		parser.addAll("o129-11", "integer");
		assertEquals(true, parser.optionExists("o1211"));
	}
	
	@Test
	public void decreasingRange() {
		parser.addAll("o5-4", "integer");
		assertEquals(true, parser.optionExists("o5") & parser.optionExists("o4"));
	}
	
	@Test
	public void multipleOptGroupToOneShcGroup() {
		parser.addAll("o1-2 o3-4", "o1-4", "string");
		assertEquals("Options Map: \n"
				+ "{o1=Option[name:o1, value:, type:STRING], o2=Option[name:o2, value:, type:STRING], o3=Option[name:o3, value:, type:STRING], o4=Option[name:o4, value:, type:STRING]}\n"
				+ "Shortcuts Map:\n"
				+ "{o1=Option[name:o1, value:, type:STRING], o2=Option[name:o2, value:, type:STRING], o3=Option[name:o3, value:, type:STRING], o4=Option[name:o4, value:, type:STRING]}" 
				, parser.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void exceptionFromInvalidType() {
		parser.addAll("o1", "notype");
	}
	
}
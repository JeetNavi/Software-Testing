package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task1_1_FunctionalTest {

	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}

	//Bug #1 is caught several times due to no shortcut being specified.
	
	@Test
	public void addOptionWithLongShortcut() { 
		parser.addOption(new Option("optionWithLongShortcut", Type.STRING), "longShortcuttttttttttttttttttttttt");
		assertEquals(0, parser.parse("--optionWithLongShortcut test"));
	}
	
	@Test
	public void addOptionWithLongName() { 
		parser.addOption(new Option("optionWithLongNameeeeeeeeeeeeeeeeeeeeeeeeeee", Type.STRING));
		assertEquals(0, parser.parse("--optionWithLongNameeeeeeeeeeeeeeeeeeeeeeeeeee test"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addOptionWithInvalidName() {
		Option addOptionWithInvalidName = new Option("invalid#name", Type.STRING);
		parser.addOption(addOptionWithInvalidName);
	}
	
	@Test
	public void newOptionWithSameName() {
		Option newOptionWithSameName1 = new Option("newOptionWithSameName", Type.STRING);
		Option newOptionWithSameName2 = new Option("newOptionWithSameName", Type.INTEGER);
		parser.addOption(newOptionWithSameName1);
		parser.addOption(newOptionWithSameName2);
		assertEquals(Type.INTEGER, newOptionWithSameName1.getType());
	}
	
	@Test
	public void parseBlankCommandLine() {
		assertNotEquals(-2, parser.parse(" "));
	}
	
	@Test
	public void parseComplexSingleQuote() {
		Option parseComplexSingleQuote = new Option("parseComplexSingleQuote", Type.STRING);
		parser.addOption(parseComplexSingleQuote);
		parser.parse("--parseComplexSingleQuote=' value='ab' '");
		assertEquals(" value='ab", parser.getString("parseComplexSingleQuote"));
	}
	
	@Test
	public void parseComplexDoubleQuote() {
		Option parseComplexDoubleQuote = new Option("parseComplexDoubleQuote", Type.STRING);
		parser.addOption(parseComplexDoubleQuote);
		parser.parse("--parseComplexDoubleQuote=\" parseComplexDoubleQuote=complex-value\"");
		assertEquals(" parseComplexDoubleQuote=complex-value", parser.getString("parseComplexDoubleQuote"));
	}
	
	@Test(expected = RuntimeException.class)
	public void getStringOfNullOption () {
		parser.getString(null);
	}
	
	@Test
	public void getCharacterOfEmptyStringValue() {
		parser.addOption(new Option("getCharacterOfEmptyValue", Type.STRING));
		assertEquals('\0', parser.getCharacter("getCharacterOfEmptyValue"));
	}
	
	@Test
	public void getBooleanOfEmptyStringValue() {
		parser.addOption(new Option ("getBooleanOfEmptyValue", Type.STRING));
		assertEquals(false, parser.getBoolean("getBooleanOfEmptyValue"));
	}
	
	@Test
	public void getNegativeInteger() {
		parser.addOption(new Option("getNegativeInteger", Type.STRING));
		parser.parse("--getNegativeInteger '-1'");
		assertEquals(-1, parser.getInteger("getNegativeInteger"));
	}
	
	@Test
	public void getIntegerFromStringOptionWithValue5() {
		parser.addOption(new Option("getIntFromEachDigit", Type.STRING));
		parser.parse("--getIntFromEachDigit 5");
		assertEquals(5, parser.getInteger("getIntFromEachDigit"));
		//5 is the only digit (0-9) that fails
	}
	
	@Test
	public void getBooleanNonBooleanTest() {
		parser.addOption(new Option("GBNB", Type.BOOLEAN));
		parser.parse("--GBNB notABool");
		assertEquals(1, parser.getInteger("GBNB"));
	}

		@Test
	public void replaceOptionUsingShortcutDash() {
		Option o1 = new Option("o1", Type.STRING);
		o1.setValue("o1");
		parser.addOption(o1, "one");
		parser.replace("-one", "o1", "o1Updated");
		assertEquals("o1Updated", o1.getValue());
	}
	
	@Test
	public void replaceWhiteSpaceVariableTest() {
		Option op1 = new Option("op1", Type.STRING);
		Option op2 = new Option("op2", Type.STRING);
		op1.setValue("op1");
		op2.setValue("op2");
		parser.addOption(op1);
		parser.addOption(op2);
		parser.replace("op1       op2", "op2", "op2Updated");
		assertEquals("op2Updated", op2.getValue());
	}
	
	@Test
	public void optionEqualityWithDifferentNames(){
		Option OEWDN = new Option("name", Type.STRING);
		Option OEWDN2 = new Option("differentName", Type.STRING);
		assertEquals(false, OEWDN.equals(OEWDN2));
	}
	
	@Test
	public void getIntegerStringBig() {
		Option op = new Option("op", Type.STRING);
		op.setValue("99999999999999999999999");
		parser.addOption(op);
		assertEquals(0, parser.getInteger("op"));
	}

}


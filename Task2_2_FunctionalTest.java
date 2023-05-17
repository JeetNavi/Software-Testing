package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	
	private Parser parser;
	private OptionMap optmap;
	
	@Before
	public void setUp() {
		parser = new Parser();
		optmap = new OptionMap();
	}

	
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
	public void getIntegerOfLongNumberString() {
		Option getIntegerOfBigString = new Option("getIntegerOfBigString", Type.STRING);
		getIntegerOfBigString.setValue("999999999999999999999");
		parser.addOption(getIntegerOfBigString);
		assertEquals(0,parser.getInteger("getIntegerOfBigString"));
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
	
	//Above are all the tests from task 1 (i.e. the one that caught bugs in JAR)
	
	@Test
	public void setOptionName() {
		Option SEN = new Option ("SEN", Type.STRING);
		SEN.setName("newName");
		assertEquals("newName", SEN.getName());
	}
	
	@Test
	public void OptionEquality() {
		Option OptionEquality1 = new Option("OptionEquality", Type.STRING);
		Option OptionEquality2 = new Option("OptionEquality", Type.STRING);
		assertEquals(true, OptionEquality1.equals(OptionEquality2));
	}
	
	@Test
	public void NullOptionEquality() {
		Option NullOptionEquality = new Option("NullOptionEquality", Type.STRING);
		assertEquals(false, NullOptionEquality.equals(null));
	}
	
	@Test
	public void DiffTypeOptionEquality() {
		Option DiffTypeOptionEquality1 = new Option("DiffType1", Type.BOOLEAN);
		Option DiffTypeOptionEquality2 = new Option("DiffType2", Type.STRING);
		assertEquals(false, DiffTypeOptionEquality1.equals(DiffTypeOptionEquality2));
	}
	
	@Test
	public void NullNameOptionEquality1() {
		Option NullNameOptionEquality1 = new Option("name", Type.STRING);
		Option NullNameOptionEquality2 = new Option(null, Type.STRING);
		assertEquals(false, NullNameOptionEquality2.equals(NullNameOptionEquality1));
	}
	
	@Test(expected = NullPointerException.class)
	public void NullNameOptionEquality2() {
		Option NullNameOptionEquality1 = new Option(null, Type.STRING);
		Option NullNameOptionEquality2 = new Option(null, Type.STRING);
		NullNameOptionEquality1.equals(NullNameOptionEquality2);
	}
	
	@Test
	public void SameOptionEquality() {
		Option sameOp = new Option("sameOp", Type.STRING);
		assertEquals(true, sameOp.equals(sameOp));
	}
	
	@Test
	public void OptionToString() {
		Option toStringOp = new Option("toStringOp", Type.STRING);
		assertEquals("Option[name:toStringOp, value:, type:STRING]", toStringOp.toString());
	}
	
	@Test
	public void OptionExists() {
		assertEquals(false, parser.optionExists(null));
	}
	
	@Test
	public void ShortcutExists() {
		assertEquals(false, parser.shortcutExists(null));
	}
	
	@Test
	public void OptionOrShortcutExists() {
		assertEquals(false, parser.optionOrShortcutExists(null));
	}
	
	@Test
	public void IntegerOptionParseIntException() {
		Option parseIntException = new Option("parseIntExc", Type.INTEGER);
		parseIntException.setValue("999999999999999999");
		parser.addOption(parseIntException);
		assertEquals(0, parser.getInteger("parseIntExc"));
	}
	
	@Test
	public void BigIntegerException() {
		Option BigIntegerException = new Option("BigIntegerException", Type.INTEGER);
		BigIntegerException.setValue("roar");
		parser.addOption(BigIntegerException);
		assertEquals(0, parser.getInteger("BigIntegerException"));
	}
	
	@Test
	public void GetIntegerOfTrueBooleanOp() {
		Option GetIntegerOfTrueBooleanOp = new Option("GetIntegerOfTrueBooleanOp", Type.BOOLEAN);
		GetIntegerOfTrueBooleanOp.setValue("1");
		parser.addOption(GetIntegerOfTrueBooleanOp);
		assertEquals(1, parser.getInteger("GetIntegerOfTrueBooleanOp"));
	}
	
	@Test
	public void GetIntegerOfFalseBooleanOp() {
		Option GetIntegerOfFalseBooleanOp = new Option("GetIntegerOfFalseBooleanOp", Type.BOOLEAN);
		GetIntegerOfFalseBooleanOp.setValue("0");
		parser.addOption(GetIntegerOfFalseBooleanOp);
		assertEquals(0, parser.getInteger("GetIntegerOfFalseBooleanOp"));
	}
	
	@Test
	public void getIntegerOfCharacterOp() {
		Option charOp = new Option ("charOp", Type.CHARACTER);
		parser.addOption(charOp);
		assertEquals(0, parser.getInteger("charOp"));
	}
	
	@Test
	public void getBooleanCaseFalse() {
		Option GBCFalse = new Option ("GBCFalse", Type.STRING);
		GBCFalse.setValue("false");
		parser.addOption(GBCFalse);
		assertEquals(false, parser.getBoolean("GBCFalse"));
	}
	
	@Test
	public void getCharacterCaseEmpty() {
		Option GCCEmpty = new Option ("GCCEmpty", Type.STRING);
		parser.addOption(GCCEmpty);
		assertEquals('\0', parser.getCharacter("GCCEmpty"));
	}
	
	@Test
	public void getCharacterCaseNonEmpty() {
		Option GCCNonEmpty = new Option ("GCCNonEmpty", Type.STRING);
		GCCNonEmpty.setValue("test");
		parser.addOption(GCCNonEmpty);
		assertEquals('t', parser.getCharacter("GCCNonEmpty"));
	}
	
	@Test
	public void setShortcut() {
		Option setShortcut = new Option("setShortcut", Type.STRING);
		parser.addOption(setShortcut);
		parser.setShortcut("setShortcut", "SS");
		assertEquals(true, parser.shortcutExists("SS"));
	}
	
	@Test
	public void replaceExistingVarNameStartTwoDash(){
		Option REVNSTD = new Option("REVNSTD", Type.STRING);
		REVNSTD.setValue("test");
		parser.addOption(REVNSTD);
		parser.replace("--REVNSTD", "test", "testUpdate");
		assertEquals("testUpdate", REVNSTD.getValue());
	}
	
	@Test
	public void replaceWithShortcutName() {
		Option RWSN = new Option("RWSN", Type.STRING);
		RWSN.setValue("test");
		parser.addOption(RWSN, "SN");
		parser.replace("SN", "test", "testUpdate");
		assertEquals("testUpdate", RWSN.getValue());
	}
	
	@Test
	public void parseNull() {
		assertEquals(-1, parser.parse(null));
	}
	
	@Test
	public void parseEmpty() {
		assertEquals(-2, parser.parse(""));
	}
	
	@Test
	public void isKeyOptionFalse() {
		Option keyOptionF = new Option("keyOptionF", Type.STRING);
		parser.addOption(keyOptionF, "KOF");
		parser.parse("-KOF hi");
		assertEquals("hi", keyOptionF.getValue());
	}
	
	@Test
	public void entrySplitConditionFalse1() {
		Option entrySplitCond1 = new Option("esc1", Type.STRING);
		parser.addOption(entrySplitCond1);
		assertEquals(0, parser.parse("--esc1 "));
	}
	
	@Test
	public void entrySplitConditionFalse2() {
		Option entrySplitCond2 = new Option("esc2", Type.STRING);
		parser.addOption(entrySplitCond2);
		assertEquals(0,parser.parse("--esc2"));
	}
	
	@Test
	public void parseBooleanFalse() {
		Option parseBooleanFalse = new Option("pbf", Type.BOOLEAN);
		parser.addOption(parseBooleanFalse);
		assertEquals(0,parser.parse("--pbf false"));
	}
	
	@Test
	public void parseBooleanZero() {
		Option parseBooleanZero = new Option("pbz", Type.BOOLEAN);
		parser.addOption(parseBooleanZero);
		assertEquals(0,parser.parse("--pbz 0"));
	}
	
	@Test
	public void parseBooleanTrue() {
		Option parseBooleanTrue = new Option("pnbt", Type.BOOLEAN);
		parser.addOption(parseBooleanTrue);
		assertEquals(0,parser.parse("--pnbt notfalse"));
	}
	
	@Test
	public void parseComplexQuote() {
		Option parseSingleQuoteStart = new Option("psqs", Type.STRING);
		parser.addOption(parseSingleQuoteStart);
		assertEquals(0,parser.parse("psqs=\'h\""));
	}
	
	@Test
	public void parseComplexQuote2() {
		Option parseSingleQuoteEnd = new Option("psqe", Type.STRING);
		parser.addOption(parseSingleQuoteEnd);
		assertEquals(0,parser.parse("psqe=\"h\'"));
	}
	
	@Test
	public void parserToString() {
		assertEquals("Options Map: \n{}\nShortcuts Map:\n{}", parser.toString());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void noTypeException() {
		parser.addOption(new Option("noType", Type.NOTYPE));
	}
	
	@Test
	public void addOptionSameNameNewShortcut() {
		Option oldOp = new Option("sameName", Type.STRING);
		Option newOp = new Option("sameName", Type.STRING);
		parser.addOption(oldOp);
		parser.addOption(newOp, "newSHC");
		assertEquals(parser.parse("--sameName hi"), parser.parse("-newSHC hi"));
	}
	
	@Test(expected = RuntimeException.class)
	public void getOptionException() {
		optmap.getOption(null);
	}
	
	@Test(expected = RuntimeException.class)
	public void getShortcutException() {
		optmap.getShortcut(null);
	}
	
	@Test(expected = RuntimeException.class)
	public void getOptionByNSException() {
		optmap.getOptionByNameOrShortcut("test");
	}
	
	@Test
	public void optOrShcExistsTrue1() {
		optmap.store(new Option("test", Type.STRING), "hi");
		assertEquals(true, optmap.optionOrShortcutExists("test"));
	}
	
	@Test
	public void optOrShcExistsTrue2() {
		optmap.store(new Option("hi", Type.STRING), "test");
		assertEquals(true, optmap.optionOrShortcutExists("test"));
	}
	
	@Test
	public void setSHCForNullOption() {
		optmap.setShortcut(null, "test");
		assertEquals(false, optmap.shortcutExists("test"));
	}
	
	@Test
	public void setValueFromNullOption() {
		optmap.setValueWithOptionName(null, "test");
		//No assertion here, just making sure it doesn't cause exception.
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void isOptionValidNullName() {
		optmap.store(new Option(null, Type.STRING), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void isOptionValidEmptyName() {
		optmap.store(new Option("", Type.STRING), "shc");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void isOptionValidInvalidShortcut() {
		optmap.store(new Option("name", Type.STRING), "test!");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void isOptionValidNullSHCValidName() {
		optmap.store(new Option("name", Type.STRING), null);
	}
	
}

package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private OptionMap optionMap;
	
	public Parser() {
		optionMap = new OptionMap();
	}
	
	public void addAll(String options, String types) {
		addAll(options, "", types);
	}
	
	public void addAll(String options, String shortcuts, String types) {
		options = options.replaceAll("\\s+", " ");
		shortcuts = shortcuts.replaceAll("\\s+", " ");
		types = types.replaceAll("\\s+", " ");
		types = types.toLowerCase();
		List<String> typesList = new ArrayList<String>(Arrays.asList(types.split(" ")));
		List<String> optsList = new ArrayList<String>(Arrays.asList(options.split(" ")));
		List<String> shctsList = new ArrayList<String>(Arrays.asList(shortcuts.split(" ")));
		List<Boolean> toIgnoreList = new ArrayList<Boolean>();
		int counter = 0;
		
		//Check for invalid types
		for (String type:typesList) {
			if (!(type.equals("string") || type.equals("boolean") || type.equals("integer") || type.equals("character"))){
				throw new IllegalArgumentException("Types must be valid");
			}
		}
		
		//Expands all valid groups for options
		for (ListIterator<String> iterator = optsList.listIterator(); iterator.hasNext();) {
			String option = iterator.next();
			if (option.contains("-") && this.isValidGroup(option)) {
				iterator.remove();
				String[] groupMembers = this.getMembersOfGroup(option);
				for (String member : groupMembers) {
					iterator.add(member);
					//optsList.add(counter+miniCounter, member);
				}
				//iterator.remove();
				//optsList.remove(counter);
			}
			counter++;
		}
		
		counter = 0;
		
		//Delete all valid groups that have been expanded
		
		//Populate toIgnoreList for options
		for (String option : optsList) {
			if (!option.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				//toIgnoreList.set(counter, true);
				toIgnoreList.add(true);
			} else {
				//toIgnoreList.set(counter, false);
				toIgnoreList.add(false);
			}
			counter++;
		}
		
		counter = 0;
		
		//Expands all valid groups for shortcuts
		for (ListIterator<String> iterator = shctsList.listIterator(); iterator.hasNext();) {
			String shortcut = iterator.next();
			if (shortcut.contains("-") && this.isValidGroup(shortcut)) {
				iterator.remove();
				String[] groupMembers = this.getMembersOfGroup(shortcut);
				for (String member : groupMembers) {
					iterator.add(member);
				}
			}
			counter++;
		}
		
		counter = 0;
		
		for (Boolean toIgnore : toIgnoreList) {
			if (toIgnore) {
				optsList.remove(counter);
				if (shctsList.size() > counter) {
					shctsList.remove(counter);
				}
				if (typesList.size() > counter) {
					typesList.remove(counter);
				}
			}
			counter++;
		}
		
		counter = 0;
		
		//Replace all invalid shortcuts with ""
		for (String shortcut : shctsList) {
			if (!shortcut.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
				shctsList.set(counter, "");
			} 
			counter++;
		}
		
		Type lastType = null;
		counter = 0;
		
		for (String option : optsList) {
			
			if (typesList.size() > counter && shctsList.size() > counter) {
				
				if (typesList.get(counter).equals("string")) {
					optionMap.store(new Option(option, Type.STRING), shctsList.get(counter));
					lastType = Type.STRING;
					
				} else if (typesList.get(counter).equals("integer")) {
					optionMap.store(new Option(option, Type.INTEGER), shctsList.get(counter));
					lastType = Type.INTEGER;
					
				} else if (typesList.get(counter).equals("boolean")) {
					optionMap.store(new Option(option, Type.BOOLEAN), shctsList.get(counter));
					lastType = Type.BOOLEAN;
					
				} else if (typesList.get(counter).equals("character")) {
					optionMap.store(new Option(option, Type.CHARACTER), shctsList.get(counter));
					lastType = Type.CHARACTER;
					
				} //else we ignore invalid option types completely along with their
				//corresponding option name and shortcut.
				
				counter++;
				
			} else if (!(typesList.size() > counter) && shctsList.size() > counter) {
				optionMap.store(new Option(option, lastType), shctsList.get(counter));
				counter++;
				
			} else if (!(typesList.size() > counter) && !(shctsList.size() > counter)) {
				optionMap.store(new Option(option, lastType), "");
				counter++;
				
			} else if (typesList.size() > counter && !(shctsList.size() > counter)) {
				
				if (typesList.get(counter).equals("string")) {
					optionMap.store(new Option(option, Type.STRING), "");
					lastType = Type.STRING;
					
				} else if (typesList.get(counter).equals("integer")) {
					optionMap.store(new Option(option, Type.INTEGER), "");
					lastType = Type.INTEGER;
					
				} else if (typesList.get(counter).equals("boolean")) {
					optionMap.store(new Option(option, Type.BOOLEAN), "");
					lastType = Type.BOOLEAN;
					
				} else if (typesList.get(counter).equals("character")) {
					optionMap.store(new Option(option, Type.CHARACTER), "");
					lastType = Type.CHARACTER;
					
				} //else we ignore invalid option types completely along with their
				//corresponding option name and shortcut.
				
				counter++;
			}
		}
		
	}
	
	public boolean isValidGroup(String group) {
		Pattern patternLowerString = Pattern.compile("[a-z]-[a-z]*$");
		Pattern patternUpperString = Pattern.compile("[A-Z]-[A-Z]*$");
		Pattern patternInteger = Pattern.compile("[0-9]-[0-9]*$");
		
		Matcher matcherLowerString = patternLowerString.matcher(group);
		Matcher matcherUpperString = patternUpperString.matcher(group);
		Matcher matcherInteger = patternInteger.matcher(group);
		
		return (matcherLowerString.find() || matcherUpperString.find() || matcherInteger.find());
	}
	
	public String[] getMembersOfGroup(String group) {
		
		ArrayList<String> members = new ArrayList<String>();
		
		Pattern patternString = Pattern.compile("[a-zA-Z]-[a-zA-Z]$");
		Pattern patternInteger = Pattern.compile("[0-9]-[0-9]*$");
		
		Pattern PatternGroupName = Pattern.compile("^([a-zA-Z]|[0-9])*-");
		
		Matcher matcherString = patternString.matcher(group);
		Matcher matcherInteger = patternInteger.matcher(group);
		
		Matcher matcherGroupName = PatternGroupName.matcher(group);
		
		String regex=null;
		String groupName=null;
		Boolean strMatch = false;
		Boolean intMatch = false;
		
		if (matcherString.find()) {
			strMatch = true;
			regex = (matcherString.group(0));
		} else {
			matcherInteger.find();
			intMatch = true;
			regex = (matcherInteger.group(0));
		}
		
		if (matcherGroupName.find()) {
			groupName = (matcherGroupName.group(0));
		}
		groupName = groupName.substring(0, groupName.length()-2);
		
		String[] regArray = regex.split("-");
		
		if (strMatch) {
			Character start = regArray[0].charAt(0);
			Character end = regArray[1].charAt(0);
			for (int i = 0; i <= Math.abs((int)start - (int)end); i++) {
				if ((int)start < (int)end) {
					members.add(groupName + (char)((int)start+i));
				} else if ((int)start > (int)end){
					members.add(groupName + (char)((int)start-i));
				}
			}
		} else if (intMatch) {
			int startInt = Integer.parseInt(regArray[0]);
			int endInt = Integer.parseInt(regArray[1]);
			for (int i = 0; i <= Math.abs(startInt - endInt); i++) {
				if (startInt < endInt) {
					members.add(groupName + Integer.toString(startInt+i));
				} else if (startInt > endInt) {
					members.add(groupName + Integer.toString(startInt-i));
				}
			}
		}
		
		
		String[] membersArray = members.toArray(new String[members.size()]);
		
		return membersArray;
	}
	
	public void addOption(Option option, String shortcut) {
		optionMap.store(option, shortcut);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}

	
	public Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}
}
package hr.fer.zemris.java.hw06.shell.util;

public interface NameBuilder {

	void execute(FilterResult result, StringBuilder sb);
	
	default NameBuilder text(String text) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(text);
		};
	}
	
	default NameBuilder group(int groupNum) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(r.group(groupNum));
		};
	}
	
	default NameBuilder group(int groupNum, char padding, int minWidth) {
		return (r, sb) -> {
			execute(r, sb);
			sb.append(String.format("%" + Math.max(minWidth, r.group(groupNum).length()) + "s", 
					r.group(groupNum)).replaceAll(" ", String.valueOf(padding)));
		};
	}
	
}

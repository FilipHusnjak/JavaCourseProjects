package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.webserver.RequestContext;

public class SmartScriptEngineDemo {

	public static void main(String[] args) {
		Path path = Paths.get("./webroot/scripts/brojPoziva.smscr");
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			Map<String, String> persistentParameters = new HashMap<String, String>();
			List<RequestContext.RCCookie> cookies = new ArrayList<>();
			String docBody = Files.readString(path);
			RequestContext rc = new RequestContext(
					System.out, 
					parameters, 
					persistentParameters,
					cookies);
			new SmartScriptEngine(
					new SmartScriptParser(docBody).getDocumentNode(),
					rc).execute();
		} catch (IOException e) {
			System.out.println("Given file cannot be read!");
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
		}
	}

}

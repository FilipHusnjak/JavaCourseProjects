package hr.fer.zemris.java.custom.scripting.examples.demo;

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
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class BrojPozivaDemo {

	public static void main(String[] args) {
		Path path = Paths.get("./webroot/scripts/brojPoziva.smscr");
		try {
			String docBody = Files.readString(path);
			Map<String,String> parameters = new HashMap<String, String>();
			Map<String,String> persistentParameters = new HashMap<String, String>();
			List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
			persistentParameters.put("brojPoziva", "3");
			RequestContext rc = new RequestContext(System.out, parameters, persistentParameters,
			cookies);
			new SmartScriptEngine(
			new SmartScriptParser(docBody).getDocumentNode(), rc).execute();
			System.out.println("Vrijednost u mapi: "+rc.getPersistentParameter("brojPoziva"));
		} catch (IOException e) {
			System.out.println("Given file cannot be read!");
		} catch (SmartScriptParserException e) {
			System.out.println(e.getMessage());
		}
	}

}

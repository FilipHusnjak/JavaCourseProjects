package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		StringBuilder sb = new StringBuilder();
        sb.append("<html>\r\n");
        sb.append("		<head>\r\n");
        sb.append("			<title>Your parameters</title>\r\n");
        sb.append("		</head>\r\n");
        sb.append("		<body>\r\n");
        sb.append("			<h1>Tablica parametara:</h1>\r\n");
        sb.append("			<table>\r\n");
        sb.append("				<thead>\r\n");
        sb.append("					<tr>\r\n");
        sb.append("					<th>Name</th>\r\n");
        sb.append("					<th>Value</th>\r\n");
        sb.append("					</tr>\r\n");
        sb.append("				</thead>\r\n");
        sb.append("				<tbody>\r\n");
        for (String name : context.getParameterNames()) {
            sb.append("				<tr>\r\n");
            sb.append("				<td>\r\n");
            sb.append(name);
            sb.append("				</td>\r\n");
            sb.append("				<td>\r\n");
            sb.append(context.getParameter(name));
            sb.append("				</td>\r\n");
            sb.append("				</tr>\r\n");
        }
        sb.append("				</tbody>\r\n");
        sb.append("			</table>\r\n");
        sb.append("		</body>\r\n");
        sb.append("</html>\r\n");
        context.write(sb.toString());
	}

}

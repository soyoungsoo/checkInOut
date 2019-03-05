package com.sys.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Message {

	public void sendMsg(HttpServletResponse response, String msg, String location) throws IOException {
		PrintWriter out = response.getWriter();
        out.println("<script>alert('" + msg + "'); location.href='" + location + "';</script>");
        out.flush();             
	}
}

package com.surekam.modules.sys.utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.surekam.common.mapper.JsonMapper;


/**
 * Servlet implementation class WxHttpServlet
 */
public class WxHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WxHttpServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Sign sign = new Sign();
		String jsapi_ticket = sign.getJsApiTicket();
		String url = request.getHeader("Referer");
		Map<String, String> map = Sign.sign(jsapi_ticket, url);
		JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
		response.getWriter().println(jsonMapper.toJson(map));
		
	}

}

package org.edu.ptu.studentmanager.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers","Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
		response.setHeader("Access-Control-Allow-Credentials", "true"); // 是否支持cookie跨域        
		response.setHeader("XDomainRequestAllowed", "1");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}

	static int x = 0, y = 0, a = 0, b = 0;
	public static void main(String[] args) throws Exception {
		Thread one = new Thread(() -> {
			int t = b;
			a = 1;
			int s = 0;
			for (int i = 0; i < 1000000000; i++) {
				s = i;
			}
			System.out.println(t);
			x = b;
		});
		Thread other = new Thread(() -> {
			b = 1;
			y = a;
		});
		one.start(); other.start();
		one.join(); other.join();
		System.out.println("x=" + x + ", y=" + y);
	}
}

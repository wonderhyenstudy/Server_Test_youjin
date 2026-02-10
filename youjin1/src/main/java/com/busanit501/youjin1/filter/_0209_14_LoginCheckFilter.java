package com.busanit501.youjin1.filter;

import com.busanit501.youjin1.dto._0209_18_MemberDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
// 톰캣서버의 todo/하위경로 만 적용대상.
@WebFilter(urlPatterns = {"/todo/*"})
public class _0209_14_LoginCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        log.info("자동 로그인 체크 필터 1, 테스트");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();

        if(session.isNew()){
            log.info("JSESSIONID 쿠키가 새롭게 만들어진 사용자");
            resp.sendRedirect("/login_0209");
        }
        if(session.getAttribute("loginInfo") != null) {
            log.info("로그인 정보가 없는 사용자입니다.");
//            resp.sendRedirect("/login_0209");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie cookie = findCookie(req.getCookies(), "remember-me");

        if(cookie == null) {
            resp.sendRedirect("/login_0209");
            return;
        }
        String uuid = cookie.getValue();
        try {
            _0209_18_MemberDTO memberDTO = _0209_21_MemberService.INSTANCE.getByUUID(uuid);
            log.info("필터 검사에서, 자동로그인 체크후, uuid 로 유저 검색 결과 memberDTO 확인 : " + memberDTO);
            if (memberDTO == null) {
                throw new Exception("쿠키 값 uuid 값이 유효하지 않아요!!");
            }

            session.setAttribute("loginInfo", memberDTO);
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("/login_0209");
        }
    }
    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie targetCookie = null;
        if(cookies != null && cookies.length > 0) {
            for(Cookie ck: cookies) {
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }
        return targetCookie;
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

package com.busanit501.jsp_server_project1._0209_todo.controller;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
@WebServlet(name="_0209_16_LogoutController", urlPatterns = "/logout_0209")
public class _0209_16_LogoutController extends HttpServlet {
    // 로그 아웃 처리.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그아웃 처리를 담당하느 doPost 입니다. ");

        // 임시 로그아웃 효과,
        HttpSession session = req.getSession();
        // 세션의 loginInfo 라는 키를 삭제하면 됩니다.
        session.removeAttribute("loginInfo");
        // 적용하기.
        session.invalidate();
        // 리다이렉트
        resp.sendRedirect("/login_0209");

    }
}

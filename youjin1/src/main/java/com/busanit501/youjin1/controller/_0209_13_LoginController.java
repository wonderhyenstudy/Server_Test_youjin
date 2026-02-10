package com.busanit501.youjin1.controller;

import com.busanit501.jsp_server_project1._0209_todo.dto._0209_18_MemberDTO;
import com.busanit501.jsp_server_project1._0209_todo.service._0209_21_MemberService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@Log4j2
//      / -> http://localhost:8080, 톰캣 서버의 루트 폴더, 줄여서, 루트 경로라고 부름.
@WebServlet(name="_0209_13_LoginController", urlPatterns = "/login_0209")
public class _0209_13_LoginController extends HttpServlet {
    // 앞에 만들었던, 멤버서비스의 기능을 의존하고, 부탁하고, 용역주기.
    _0209_21_MemberService memberService = _0209_21_MemberService.INSTANCE;

    // 로그인 화면 필요.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그인 화면을 제공하는 컨트롤러입니다.");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req,resp);
    }
    // 로그인 처리 필요.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("로그인 처리를 담당하느 doPost 입니다. ");

        // 화면에서, 전달받은 mid, mpw 정보를 가져오기. 무조건 문자열이다.
        String mid = req.getParameter("mid");
        String mpw = req.getParameter("mpw");

        //===================================================================
        // 자동로그인 기능 추가 0209, 순서1 , 화면에서, 체크를 한 경우, 결과 문자열 : "on"
        String auto = req.getParameter("auto");
        // 자동로그인 체크 여부의 상태 변수.
        boolean rememberMe = auto != null && auto.equals("on");
        try {
            //
            _0209_18_MemberDTO memberDTO = memberService.login(mid, mpw);
            if (rememberMe) {
                //임시로 UUID 클래스를 이용해서, 랜덤한 문자열 생성.
                String uuid = UUID.randomUUID().toString();
                log.info("생성된 uuid 값 확인: " + uuid);
                // 서비스의 도움의 받아서, DB 에 해당 유저의 uuid 컬럼 부분을 업데이트 하기.
                memberService.updateUuid(mid,uuid);
                // 멤버 테이블에 업데이트가 된 uuid를 현재 로그인한 유저 상태에도 똑같이 업데이트
                memberDTO.setUuid(uuid);

                //쿠키 전달.  서버 : 쿠키 생성, 서버가 웹브라우저에게 쿠키를 전달함.
                Cookie rememberCookie = new Cookie("remember-me", uuid);
                // 유효기간 : 7일
                rememberCookie.setMaxAge(60 * 60 * 24 * 7);
                rememberCookie.setPath("/");
                // 서버가 전달하기.
                resp.addCookie(rememberCookie);

            }
            // 세션에 기록하기.
            HttpSession session = req.getSession();
            // 세션 : 서버에 저장하는 임시 메모리 공간,
            // setAttribute -> 저장
            // 키 : loginInfo(라벨이름)
            // 값 : memberDTO
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("/todo/list_0209");
        } catch (Exception e) {
            // 로그인 실패하면, try 에서 예외가 발생하면, 여기  catch 구문으로 던진다.
            // /login_0209 , 화면으로 (doGet), 첨부 : result=error , url 무조건 문자열.
           resp.sendRedirect("/login_0209?result=error");
        }

        //===================================================================
    } //doPost
}

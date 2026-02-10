package com.busanit501.youjin1.controller;

import com.busanit501.jsp_server_project1._0209_todo.dto._0209_6_TodoDTO;
import com.busanit501.jsp_server_project1._0209_todo.service._0209_2_TodoService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet(name="_0209_9_TodoReadController", urlPatterns = "/todo/read_0209")
public class _0209_9_TodoReadController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 현재, 작업하는 파일 어디인지 , 서버 콘솔에 출력
        log.info("/todo/read_0206, get으로 요청 처리함. ");
        // 임시로 하나 조회할 더미 데이터 지정. (더미 데이터 임의로 100번 지정)
        // 웹브라우저에서, 주소 요청 : http://localhost:8080/todo/read_0206?tno=1
        // 서버는 , 웹에서 전달한 데이터를 가져오기.
//        req.getParameter("tno") => 문자열이기 때문에, 우리는 파싱해서, 다시 숫자 형태로 변경.
        //  Long.parseLong(문자열) -> 문자열 -> 숫자 변경.
        try {
            Long tno = Long.parseLong(req.getParameter("tno"));
            // 서비스에서 만들었던, get 메서드에, tno 번호를 전달해서, 임시 데이터를 가져오기.
            _0209_6_TodoDTO todoDTO  = _0209_2_TodoService.INSTANCE.get(tno);
            // 데이터를 전달 준비1
            req.setAttribute("dto", todoDTO); // 화면에 담았다.

            // =_0209_순서1==========================================================================
            // 웹 브라우저에서, 1) 쿠키를 찾고, 2)없으면, 새로 생성, 3)있으면, 유지하고 기록하기.
            // findCookie 메서드를 생성. 매개변수 1) 전체 쿠키 목록 2) 찾고자하는 쿠키의 이름 : viewTodos
            Cookie viewTodosCookie = findCookie(req.getCookies(),"viewTodos");
            String todoListStr = viewTodosCookie.getValue();
            // 상태변수 , todoListStr 문자열 , 예시 : "25-3-",  tno : 25, todo를 하나조회하고,
            // tno : 3, todo를 하나조회하고,
            // todoListStr 안의 값의 존재 여부를 상태변수로 기록중.
            boolean exist = false;

            if(todoListStr != null && todoListStr.indexOf(tno+"-") >= 0) {
                exist = true;
            }
            log.info("exist : " + exist);

            // 만약에 기록이 없다면, 새로 문자열에 내용을 추가하기. 예시, "25-3-"
            if(!exist) {
                todoListStr += tno+"-";
                viewTodosCookie.setValue(todoListStr);
                viewTodosCookie.setMaxAge(60 * 60 * 24);
                viewTodosCookie.setPath("/");
                // 응답 객체에 담아서, 웹브라우저에게 쿠키 전달하기.
                resp.addCookie(viewTodosCookie);
            }
            // ===========================================================================
            // 화면에 전달
            req.getRequestDispatcher("/WEB-INF/_0209_todo/_3_todoRead.jsp").forward(req,resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ServletException("read error");
        }

    } //doGet

    // _0209_순서2, 2) 찾고자하는 쿠키의 이름 : viewTodos
    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        // 찾고자 하는 쿠키를 담을 임시 변수 선언,
        Cookie targetCookie = null;

        // 전체 목록의 기본 유효성 체크, null 아니고, 길이가 0 초과 : 쿠키가 있는 경우.
        if(cookies != null && cookies.length > 0) {
            for(Cookie ck: cookies) {
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                } //if
            } //for
        } // if

        // 찾고자 하는 쿠키가 없는 경우, 새로 쿠키를 생성해서, 반환.
        if(targetCookie == null) {
            targetCookie = new Cookie(cookieName,"");
            targetCookie.setPath("/");
            targetCookie.setMaxAge(60 * 60 * 24); // 24 시간 유효시간. 임의로
        }
        return targetCookie;
    }


}

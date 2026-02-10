package com.busanit501.youjin1.controller;

import com.busanit501.jsp_server_project1._0209_todo.dto._0209_6_TodoDTO;
import com.busanit501.jsp_server_project1._0209_todo.service._0209_2_TodoService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "_0209_1_TodoListController", urlPatterns = "/todo/list_0209")
@Log4j2 // 로그를 중요도에 따라서, 기록을 하는 방법을 다르게한다.
public class _0209_1_TodoListController extends HttpServlet {

    // 컨트롤러에서,  목록조회하는 기능이 없음,
    // 1) 여기서 또 새로 만들거나 , 2) 이미 만들어진 기능을 가져와서 재사용.
    // 정답 : 2) 이용.

    // 서비스의 기능을 가지고 있는 클래스 이용 : _0204_4_TodoService
    private _0209_2_TodoService todoService = _0209_2_TodoService.INSTANCE;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        System.out.println("package com.busanit501.jsp_server_project1._0204_todo.controller; " +
//                "_0204_7_TodoListController, 시간 260204:141800 기존 콘솔에서 확인하는 방법. ");
//        log.info("log4j2를 이용해서, 기록하기. ");
        log.info("===전체 목록 조회=====");
        try {
            //0206, 서블릿 컨텍스트 이용 1
            // 1. ServletContext 객체 얻기
            ServletContext context = getServletContext();

            // 2. web.xml에 설정된 파라미터 읽기
            String adminEmail = context.getInitParameter("adminEmail");

            // 3. 데이터 저장 (공유 목적)
            context.setAttribute("globalMessage", "안녕하세요, 모두와 공유하는 메시지입니다.");

            // 서버에서, 객체에 담아서, 결과 화면에 전달.(직접 전달.)
            req.setAttribute("adminEmailDirect", adminEmail);


            // 서비스의 도움을 받아서, DB로 데이터를 가져오기.
            List<_0209_6_TodoDTO> dtoList = todoService.listAll();
            // 서버에서, 객체에 담아서, 결과 화면에 전달.
            req.setAttribute("dtoList", dtoList);
            // 결과 화면으로 안내.
            req.getRequestDispatcher("/WEB-INF/_0209_todo/list.jsp")
                    .forward(req,resp);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServletException("list error");
        }


    }
}

package com.busanit501.youjin1.controller;

import com.busanit501.jsp_server_project1._0209_todo.dto._0209_6_TodoDTO;
import com.busanit501.jsp_server_project1._0209_todo.service._0209_2_TodoService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
@WebServlet(name="_0209_10_TodoRegController", urlPatterns = "/todo/register_0209")
public class _0209_10_TodoRegController extends HttpServlet {
    // 2가지 기능을 제공
    // 1) 화면 제공, 2) 글쓰기 작업 수행.

    // 컨트롤러에서,  글쓰기하는 서비스 기능이 없음,
    // 1) 여기서 또 새로 만들거나 , 2) 이미 만들어진 기능을 가져와서 재사용.
    // 정답 : 2) 이용.

    // 서비스의 기능을 가지고 있는 클래스 이용 : _0204_4_TodoService
    private _0209_2_TodoService todoService = _0209_2_TodoService.INSTANCE;
    // 시간의 포맷 형태를 변경하는 기능을 추가. HH : 24시간제 표기 , a(오전/오후) hh : 1 ~12시 나타냄.
//    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss");
    private final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // 글쓰기 화면 제공
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("/todo/register, 글작성 폼 임시화면 get으로 요청 처리함. ");

//        //0206 , 글쓰기 화면에 접근하기 전에, 세션에서 로그인 유저를 체크를해서,
//        // JSESSIONID 를 체크. 있으면 진행. 없으면 로그인 페이지로 이동시키자.
//
//        // 세션를 이용하기 위해서, 도구가 필요함. 객체를 이용하기.
//        HttpSession session = req.getSession();
//
//        // 기존에 접근 이용자인지? 새로운 이용자 인지? 여부 확인을 JSESSIONID
//        if(session.isNew()){
//            // 새로운 이용자이니, 서버 : JSESSIONID 생성해서, 서버 메모리에 가지고 있고,
//            // 그리고, 웹 브라우저에게, 쿠키에 담아서, 보내기.
//            log.info("JSESSIONID 쿠키가 새롭게 만들어진 사용자");
//            // 새로 오셨네요? , 로그인 페이지로 가서, 로그인 해주세요. -> 아직 없음, 곧 만들 예정.
//            resp.sendRedirect("/login");
//        }
//
//        // 임시 로그인 처리라서,
//        // JSESSIONID 있어요, 해당 서버에 완전 최초 접속자는 아니예요.
//        // 세션이라는 공간에 loginInfo 라는 이름으로 저장을 할예정, 로그인한 유저를 .
//        if(session.getAttribute("loginInfo") == null) {
//            log.info("로그인 정보가 없는 사용자입니다.");
//            resp.sendRedirect("/login");
//            return;
//        }

        // JSESSIONID 있고, loginInfo(명단), 로그인한 유저 정보가 있어요, -> 로그인 되었다고 가정.
        // 정상적으로 글쓰기 페이지로 안내.
        req.getRequestDispatcher("/WEB-INF/_0209_todo/todoReg.jsp").forward(req,resp);

    }

    //순서4, 글쓰기 로직 처리.
    // post 처리 담당.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 받을 때 한글 설정,
//        req.setCharacterEncoding("UTF-8");

        log.info("todo/register 글쓰기 로직 처리하는 곳입니다.");
        log.info("PRG 패턴으로 글쓰기 post 작업 후, 리다이렉트 목록 화면으로 이동하기.");

        // 화면으로부터 전달받은 데이터를, -> DTO 객체 담아서, -> 서비스에 전달.
        _0209_6_TodoDTO todoDTO = _0209_6_TodoDTO.builder()
                .title(req.getParameter("title"))
//                .dueDate(req.getParameter("dueDate"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATEFORMATTER))
                .build();
        //서비스에 전달.
        try {
            // 보낼 때 한글 설정,
            // 나중에, 서버에 한번만 설정해서, 따로 설정 없이, 이용만 하면됨.
//            resp.setContentType("text/html;charset=UTF-8");
//            resp.setCharacterEncoding("UTF-8");
            todoService.register(todoDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.sendRedirect("/todo/list_0209");

    }

}

package com.busanit501.youjin1.service;


import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum _0209_2_TodoService {

    INSTANCE; // static final 생략이 되어있다.

    private _0209_3_TodoDAO dao;

    private ModelMapper modelMapper;

    _0209_2_TodoService(){
        dao = new _0209_3_TodoDAO();
        modelMapper = _0209_4_MapperUtil.INSTANCE.get();
    }

    // 기능구현,
    // 글쓰기
    public void register(_0209_6_TodoDTO todoDTO) throws Exception {
        _0209_5_TodoVO todoVO = modelMapper.map(todoDTO, _0209_5_TodoVO.class);
//
//        System.out.println("_0204_4_TodoService에서 register 작업중, 변환 결과 확인 todoVO : " + todoVO);

        log.info("_0204_4_TodoService에서 register 작업중, 변환 결과 확인 todoVO : \" + todoVO");

        dao.insert(todoDTO);
    }
    // 목록조회
    public List<_0209_6_TodoDTO> ListAll() throws Exception {

        List<_0209_5_TodoVO> list = dao.selectAll();
        log.info("voLIST확인 " + list);
        List<_0209_6_TodoDTO>  DTOList = list.stream()
                .map(todoVO -> modelMapper.map(todoVO, _0209_6_TodoDTO.class))
                .collect(Collectors.toList());

        return DTOList;
    } //getList 닫기

    // 0205_모델클래스_서비스_컨트롤러를_이용한_로직 처리_순서5
    //Todo 조회
    public _0209_6_TodoDTO get(Long tno) throws Exception {

        _0209_5_TodoVO todoVO = dao.selectOne(tno);

        _0209_6_TodoDTO todoDTO = modelMapper.map(todoVO, _0209_6_TodoDTO.class);

        return todoDTO;
    }

    public void modify(_0209_6_TodoDTO todoDTO) throws Exception {
        log.info("todoDTO : " + todoDTO );
        // dto -> vo 타입으로 변경.
        _0209_5_TodoVO todoVO = modelMapper.map(todoDTO , _0209_5_TodoVO.class);
        // dao 의 도움을 받아서, DB 서버에게 일 시키기
        dao.updateOne(todoVO);
    }

    // 삭제하기
    public void remove(Long tno) throws Exception {
        log.info("삭제할 tno 번호 : " + tno);
        dao.deleteOne(tno);
    }

}


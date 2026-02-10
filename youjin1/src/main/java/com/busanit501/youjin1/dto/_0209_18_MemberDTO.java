package com.busanit501.youjin1.dto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class _0209_18_MemberDTO {
    // 데이터베이스의 컬럼과 동일하게 작업.
    private String mid;
    private String mpw;
    private String mname;
    //자동 로그인 추가
    private String uuid;
}

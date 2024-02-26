package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    /*
     * 회원 등록
     * */
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){  // JSON으로 온 Body를 Member 데이터에 mapping
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 2번째 : DTO를 사용하자
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }


    /*
     * 회원 수정
     * */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
        @PathVariable("id") Long id,
        @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());    // 커맨드와
        Member findMember = memberService.findOne(id);  // 쿼리를 분류하는 코드가 좋다. (유지보수에 좋기 위해)
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());   // 아니면, 그냥 id 정도만 반환해주도록 짜거나...
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }


    /*
     * 회원 조회
     * */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());   //  스트림의 처리 결과를 리스트 형태로 수집

        return new Result(collect.size(), collect);  // Result 클래스를 통해 List<MemberDto>를 반환
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;  // List<MemberDto> 타입
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }


}

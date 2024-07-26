package com.bookrentalsystem.bks.service;

import com.bookrentalsystem.bks.dto.member.MemberRequest;
import com.bookrentalsystem.bks.dto.member.MemberResponse;
import com.bookrentalsystem.bks.model.Member;

import java.util.List;

public interface MemberService {

    String addMember(MemberRequest memberRequest);
    Member memberRequestToEtity(MemberRequest memberRequest);
    MemberResponse entityToMemberResponse(Member member);
    Member findMemberById(Long id);
    MemberResponse findMemberResponseFromId(Long id);
    List<Member > allMemberEntity();
    List<MemberResponse> allMemberResponse();
    List<MemberResponse> allMemberResponseDTo(List<Member> members);
    String deleteMemberById(Long id);

}

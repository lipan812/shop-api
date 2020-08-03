package com.fh.member.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

import java.util.Map;

public interface MemberService {
    ServerResponse addMember(Member member);
    ServerResponse validateMemberName(String name);

    ServerResponse checkMemberByPhone(String phone);

    ServerResponse redister(Member member);

    ServerResponse login(Member member);

    Map queryListOrder();
}

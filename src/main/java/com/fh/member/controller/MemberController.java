package com.fh.member.controller;

import com.fh.annotation.Ignore;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //注册会员 没用
    @RequestMapping("addMember")
    public ServerResponse addMember(Member member) {
        return memberService.addMember(member);
    }

    //验证账号唯一
    @RequestMapping("checkMemberByName")
    @Ignore
    public ServerResponse validateMemberName(String name){
        return memberService.validateMemberName(name);
    }
    //验证手机唯一
    @RequestMapping("checkMemberByPhone")
    @Ignore
    public ServerResponse checkMemberByPhone(String phone){
        return memberService.checkMemberByPhone(phone);
    }
    //注册会员
    @RequestMapping("redister")
    @Ignore
    public ServerResponse redister(Member member){
        return memberService.redister(member);
    }
    //登录
    @RequestMapping("login")
    @Ignore
    public ServerResponse login(Member member){
        return memberService.login(member);
    }


    @RequestMapping("checjLogin")
    //验证用户是否已经登录
    public ServerResponse checjLogin(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if (member == null){
            return ServerResponse.error();
        }
        return ServerResponse.success();
    }

    @RequestMapping("out")
    @Ignore
    public ServerResponse out(HttpServletRequest request){
       //让token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);
        request.getSession().removeAttribute(SystemConstant.TOKEN_KEY);
        //清除session的用户信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return ServerResponse.success();
    }

    //查询收货地址
    @RequestMapping("queryListOrder")
    public Map queryListOrder(){
        return memberService.queryListOrder();
    }
}


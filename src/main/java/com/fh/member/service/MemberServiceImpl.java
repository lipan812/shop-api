package com.fh.member.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ResponseEnum;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.member.mapper.MemberMapper;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public ServerResponse addMember(Member member) {
        String name = member.getName();
        String password = member.getPassword();
        if(StringUtils.isEmpty(name)  || StringUtils.isEmpty(password)){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_NULL);
        }
        //判断账号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Member member1 = memberMapper.selectOne(queryWrapper);
        if (member1 !=null){
            return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_MEMBERNAME);
        }
        //注册会员
        memberMapper.addMember(member);
        return ServerResponse.success();
    }

    //验证名称
    @Override
    public ServerResponse validateMemberName(String name) {

        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Member member1 = memberMapper.selectOne(queryWrapper);
        if (member1 == null){
            return ServerResponse.success();
        }
        return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_MEMBERNAME);
    }

    @Override
    public ServerResponse checkMemberByPhone(String phone) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member member1 = memberMapper.selectOne(queryWrapper);
        if (member1 ==null){
            return ServerResponse.success();
        }
        return ServerResponse.error(ResponseEnum.REG_MEMBER_IS_PHONE);
    }

    @Override
    public ServerResponse redister(Member member) {
        /*Member memberDB =  memberMapper.getUserByName(member.getName());
        if(memberDB!=null){
            return ServerResponse.error("用户已存在");
        }*/
        /*String encodePassword =  Md5Util.md5(Md5Util.md5(member.getPassword()));
        member.setPassword(encodePassword);*/

        String redisCode = RedisUtil.get(member.getPhone());
        if (redisCode == null){
            return ServerResponse.error(ResponseEnum.REDISCODE_NULL_NAME);
        }
        if (!redisCode.equals(member.getCode())){
            return ServerResponse.error(ResponseEnum.REDISCODE_ERROR);
        }
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(Member member) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",member.getName());
        queryWrapper.or();
        queryWrapper.eq("phone",member.getName());
        Member memberDB = memberMapper.selectOne(queryWrapper);
        if (memberDB == null){
            return ServerResponse.error("用户名或手机号不存在！！！");
        }
        //判断密码是否正确
        if (!member.getPassword().equals(memberDB.getPassword())){
            return ServerResponse.error("密码错误");
        }
        /*String md5Password = Md5Util.md5(Md5Util.md5(memberDB.getPassword()));
        if(!md5Password.equals(memberDB.getPassword())){
            //密码错误
            return ServerResponse.error("密码错误");
        }*/
        //密码正确
        String token ="";
        try {
            String jsonString = JSONObject.toJSONString(memberDB);
            String encodeJson = URLEncoder.encode(jsonString, "utf-8");
            token = JwtUtil.sign(encodeJson);
            //给token设置过期时间  
            RedisUtil.setEx(SystemConstant.TOKEN_KEY+token,token, SystemConstant.TOKEN_EXPIRE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ServerResponse.success(token);
    }

    @Override
    public Map queryListOrder() {
        Map map = new HashMap();
        List<Member> mm = new ArrayList<>();
        List<Member> list = memberMapper.selectList(null);
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getIsup()==1){
                mm.add(list.get(i));
            }
        }
        map.put("code",200);
        map.put("data",list);
        map.put("mm",mm);
        return map;
    }

}

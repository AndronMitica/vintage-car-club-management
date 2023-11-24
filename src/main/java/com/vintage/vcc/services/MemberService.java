package com.vintage.vcc.services;

import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.model.dtos.VehicleDTO;

import java.util.List;

public interface MemberService {

    MemberDTO createMember(MemberDTO memberDTO);
    List<MemberDTO> getAllMembers();
    MemberDTO updateMemberById(Long id, MemberDTO memberDTO);
    MemberDTO deleteMemberById(Long id);
    MemberDTO getMemberById(Long id);
}
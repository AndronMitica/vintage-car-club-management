package com.vintage.vcc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.model.entities.Member;
import com.vintage.vcc.repositories.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    public MemberServiceImpl(MemberRepository memberRepository, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        Member memberEntity = objectMapper.convertValue(memberDTO, Member.class);

        Member memberResponseEntity = memberRepository.save(memberEntity);
        log.info("Member with id: {} was created", memberResponseEntity.getMemberId());

        return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        List<Member> membersEntityList = memberRepository.findAll(Sort.by("memberId"));
        log.info("The list of members was retrieved, count {}", membersEntityList.size());

        return membersEntityList.stream()
                .map(memberEntity -> objectMapper.convertValue(memberEntity, MemberDTO.class))
                .toList();
    }

    @Override
    public MemberDTO updateMemberById(Long id, MemberDTO memberDTO) {

        if(memberRepository.findById(id).isPresent()) {
            memberDTO.setMemberId(id);
            Member memberEntity = objectMapper.convertValue(memberDTO, Member.class);
            Member memberResponseEntity = memberRepository.save(memberEntity);
            log.info("Member with id: {} was updated", memberResponseEntity.getMemberId());
            return objectMapper.convertValue(memberResponseEntity, MemberDTO.class);
        }
            log.info("Member with id: {} was not found", id);
        return null;
    }

    @Override
    public MemberDTO deleteMemberById(Long id) {
        Optional<Member> memberEntity = memberRepository.findById(id);
        if(memberEntity.isPresent()){
            MemberDTO memberDTO = objectMapper.convertValue(memberEntity, MemberDTO.class);
            memberRepository.deleteById(id);
            log.info("Member with id: {} was deleted", id);
            return memberDTO;
        }
        log.info("Member with id: {} was not founded", id);
        return null;
    }
}
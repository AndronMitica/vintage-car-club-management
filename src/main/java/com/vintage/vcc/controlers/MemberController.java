package com.vintage.vcc.controlers;

import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/vintage")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        System.out.println("Reached createMember endpoint");

        return ResponseEntity.ok(memberService.createMember(memberDTO));
    }

    @GetMapping("/members")
    public List<MemberDTO> getAllMembers() {
        return memberService.getAllMembers();
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberDTO> updateMemberById(@PathVariable @Valid Long id, @RequestBody @Valid MemberDTO memberDTO) {
        Optional<MemberDTO> memberDTOResponse = Optional.ofNullable(memberService.updateMemberById(id, memberDTO));
        return memberDTOResponse
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberDTO> deleteMemberById(@PathVariable @Valid Long id) {
        Optional<MemberDTO> memberDTOResponse = Optional.ofNullable(memberService.deleteMemberById(id));
        return memberDTOResponse
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
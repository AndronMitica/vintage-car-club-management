package com.vintage.vcc.controlers;

import com.vintage.vcc.model.dtos.MemberDTO;
import com.vintage.vcc.repositories.VehicleRepository;
import com.vintage.vcc.services.MemberService;
import com.vintage.vcc.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final VehicleService vehicleService;
    private final VehicleRepository vehicleRepository;

    public MemberController(MemberService memberService, VehicleService vehicleService, VehicleRepository vehicleRepository) {
        this.memberService = memberService;
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDTO> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.createMember(memberDTO));
    }

    @GetMapping("/members")
    public List<MemberDTO> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PatchMapping("/members/{id}")
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

    @PutMapping("/members/{id}/vehicles/{licensePlate}")
    public ResponseEntity<String> assignVehicleToMember(
            @PathVariable Long id,
            @PathVariable String licensePlate
    ) {
        memberService.assignVehicleToMember(id, licensePlate);
        return ResponseEntity.ok("Vehicle with licence plate " + licensePlate + " added to list of user with id " + id);
    }
}
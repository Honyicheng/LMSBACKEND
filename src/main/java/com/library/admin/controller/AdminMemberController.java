package com.library.admin.controller;

import com.library.admin.payload.MemberUpdateRequest;
import com.library.admin.service.AdminMemberService;
import com.library.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService memberService;

    @GetMapping
    public List<User> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/search")
    public List<User> searchMembers(@RequestParam String keyword) {
        return memberService.searchMembers(keyword);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequest req) {
        return ResponseEntity.ok(memberService.updateMember(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<User> renewMembership(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.renewMembership(id));
    }
}

package com.library.admin.service;

import com.library.admin.payload.MemberUpdateRequest;
import com.library.common.entity.User;

import java.util.List;

public interface AdminMemberService {
    List<User> getAllMembers();
    List<User> searchMembers(String keyword);
    User updateMember(Long id, MemberUpdateRequest request);
    void deleteMember(Long id);
    User renewMembership(Long id);
}

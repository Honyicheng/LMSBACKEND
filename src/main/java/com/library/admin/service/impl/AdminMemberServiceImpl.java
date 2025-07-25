package com.library.admin.service.impl;

import com.library.admin.payload.MemberUpdateRequest;
import com.library.admin.service.AdminMemberService;
import com.library.common.entity.User;
import com.library.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final UserRepository userRepo;

    @Override
    public List<User> getAllMembers() {
        return userRepo.findAll(); // 可扩展筛选角色
    }

    @Override
    public List<User> searchMembers(String keyword) {
        return userRepo.findByUsernameContainingIgnoreCase(keyword);
    }

    @Override
    public User updateMember(Long id, MemberUpdateRequest request) {
        User user = userRepo.findById(id).orElseThrow();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return userRepo.save(user);
    }

    @Override
    public void deleteMember(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User renewMembership(Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setMembershipDate(LocalDateTime.now()); // 假设有该字段
        return userRepo.save(user);
    }
}

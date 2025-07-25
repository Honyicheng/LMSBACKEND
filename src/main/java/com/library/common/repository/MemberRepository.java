//package com.library.common.repository;
//
//
//import com.library.common.entity.Member;
//import com.library.common.entity.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface MemberRepository extends JpaRepository<User, Long> {
//    List<User> findByRoles_Name(String roleName);
//
//    Optional<Member> findByEmail(String email);
//
//    @Query("SELECT m FROM Member m WHERE " +
//            "LOWER(m.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
//            "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    Page<Member> searchByName(@Param("name") String name, Pageable pageable);
//
//    @Query("SELECT m FROM Member m WHERE m.membershipEnd < :date")
//    List<Member> findExpiredMemberships(@Param("date") LocalDate date);
//
//    @Query("SELECT m FROM Member m WHERE m.membershipEnd BETWEEN :start AND :end")
//    List<Member> findMembershipsExpiringBetween(
//            @Param("start") LocalDate start,
//            @Param("end") LocalDate end);
//
//    @Query("SELECT m FROM Member m WHERE m.user.id = :userId")
//    Optional<Member> findByUserId(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(m) FROM Member m WHERE m.membershipEnd >= CURRENT_DATE")
//    long countActiveMembers();
//}
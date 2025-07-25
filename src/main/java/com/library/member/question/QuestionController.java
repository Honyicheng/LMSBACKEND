package com.library.member.question;

import com.library.common.entity.AskQuestionRequest;
import com.library.common.entity.Question;
import com.library.common.entity.User;
import com.library.common.repository.QuestionRepository;
import com.library.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
@RequestMapping("/api/member/questions")
@RequiredArgsConstructor
@Slf4j

public class QuestionController {

    private final QuestionRepository questionRepo;
    private final UserRepository userRepo;

    @PostMapping
    @Transactional
    public void ask(@RequestBody AskQuestionRequest req, Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        Question q = new Question();
        q.setUser(user);
        q.setAskedAt(LocalDate.now());
        q.setMessage(req.message());
        log.info("User {} asked: {}", user.getUsername(), req.message());
        questionRepo.save(q);
    }

    @GetMapping("/my")
    public List<Question> getMyQuestions(Principal principal) {
        User user = userRepo.findByUsername(principal.getName()).orElseThrow();
        return questionRepo.findByUserId(user.getId());
    }
}

package com.library.admin.controller;

import com.library.common.entity.Question;
import com.library.common.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionRepository questionRepo;

    // 查看所有提问
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    // 回复某条提问
    @PutMapping("/{id}/reply")
    @Transactional
    public ResponseEntity<Question> reply(@PathVariable Long id, @RequestBody String replyContent) {
        Question question = questionRepo.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
        question.setReply(replyContent);
        question.setRepliedAt(LocalDate.now());
        return ResponseEntity.ok(questionRepo.save(question));
    }
}

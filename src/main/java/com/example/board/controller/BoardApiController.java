package com.example.board.controller;

import com.example.board.model.Board;
import com.example.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BoardApiController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    List<Board> list(@RequestParam(required = false, defaultValue = "") String title, @RequestParam(required = false, defaultValue = "")
    String content) {

        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(title)) {
            return boardRepository.findAll();
        }
        else {
            return boardRepository.findByTitleOrContent(title, content);
        }

    }

    @PostMapping("/boards")
    Board board(@RequestBody Board board) {
        return boardRepository.save(board);
    }

    @GetMapping("/boards/{id}")
    Board one(@PathVariable Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}")
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return boardRepository.findById(id)
            .map(board -> {
                board.setTitle(newBoard.getTitle());
                board.setContent(newBoard.getContent());
                return boardRepository.save(board);
            })
            .orElseGet(()-> {
                newBoard.setId(id);
                return boardRepository.save(newBoard);
            });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        boardRepository.deleteById(id);
    }
}

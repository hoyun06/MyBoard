package com.example.board.controller;

import com.example.board.model.Board;
import com.example.board.repository.BoardRepository;
import com.example.board.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model) {

        List<Board> board = boardRepository.findAll();
        model.addAttribute("board", board);
        return "board/list";
    }
    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false, name = "id") Long id) {
        if(id == null)
            model.addAttribute("board", new Board());
        else
            model.addAttribute("board", boardRepository.findById(id).orElse(null));
        return "board/form";
    }

    @PostMapping("/form")
    public String write(@Valid Board board, BindingResult bindingResult) {

        boardValidator.validate(board,bindingResult);

        if(bindingResult.hasErrors())
            return "board/form";

        boardRepository.save(board);
        return "redirect:/board/list";
    }
}

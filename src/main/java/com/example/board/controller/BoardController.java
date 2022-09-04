package com.example.board.controller;

import com.example.board.model.Board;
import com.example.board.repository.BoardRepository;
import com.example.board.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

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
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText) {

        Page<Board> board = boardRepository.findByTitleContainingOrContentContaining(searchText,searchText,pageable);

        boolean check = false;
        int startPage, endPage;

        if(board.isEmpty()) {

            startPage = endPage = 0;
            check = true;
        }
        else {
            startPage = Math.max(1, board.getPageable().getPageNumber() - 4);
            endPage = Math.min(board.getTotalPages(), board.getPageable().getPageNumber() + 4);
        }

        model.addAttribute("board", board);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("check", check);
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

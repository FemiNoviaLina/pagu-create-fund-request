package com.kelompok4.pagu.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, RedirectAttributes redAttr) {
        redAttr.addFlashAttribute("error", "Pengajuan Tidak Ditemukan");
        return "redirect:/dashboard";
    }
}

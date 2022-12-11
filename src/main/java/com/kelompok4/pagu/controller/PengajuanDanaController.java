package com.kelompok4.pagu.controller;

import com.kelompok4.pagu.dto.NewPengajuanDto;
import com.kelompok4.pagu.model.PengajuanDana;
import com.kelompok4.pagu.model.User;
import com.kelompok4.pagu.service.PengajuanDanaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class PengajuanDanaController {
    @Autowired
    private PengajuanDanaService pengajuanDanaService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PengajuanDana> pengajuanDana = pengajuanDanaService.getAllPengajuanDana(user);
        model.addAttribute("pengajuanDana", pengajuanDana);
        return "dashboard";
    }

    @GetMapping("/new/pengajuan")
    public String showNewPengajuanForm(Model model){
        NewPengajuanDto pengajuanDana = new NewPengajuanDto();
        model.addAttribute("pengajuanDana", pengajuanDana);
        return "new-pengajuan";
    }

    @PostMapping("/new/pengajuan")
    public String createNewPengajuan(@Valid @ModelAttribute("pengajuanDana") NewPengajuanDto newPengajuanDto,
                               BindingResult result,
                               Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        pengajuanDanaService.createPengajuanDana(newPengajuanDto, user);
        return "redirect:/dashboard";
    }

    @GetMapping("/pengajuan/{nomor}")
    public String showDetailPengajuan(@PathVariable Long nomor,
                                      Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PengajuanDana pengajuanDana = pengajuanDanaService.getPengajuanDana(nomor);
        if(pengajuanDana.getPengajuId().getId() != user.getId()) {
            throw new NoSuchElementException();
        }

        model.addAttribute("pengajuanDana", pengajuanDana);
        return "detail";
    }

    @GetMapping("/proposal/{fileName:.+}")
    public ResponseEntity<Resource> getProposal(@PathVariable String fileName) throws IOException {
        Resource resource = pengajuanDanaService.getProposal(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<Resource> response = new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
        return response;
    }
}
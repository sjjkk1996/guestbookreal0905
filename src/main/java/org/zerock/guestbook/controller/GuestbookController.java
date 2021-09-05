package org.zerock.guestbook.controller;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor //자동 주입이 되도록한다
public class GuestbookController {

  private final GuestbookService service;

  @GetMapping({"/",""})
  public String index() {
    return "redirect:/guestbook/list";
  }

  @GetMapping({"/list"})
  public void list(PageRequestDTO pageRequestDTO, Model model) {
    log.info("list......." + pageRequestDTO);
    model.addAttribute("result", service.getList(pageRequestDTO));
  }

  @GetMapping("/register")
  private void register() {  }

  @PostMapping("/register")
  public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
    log.info("registerPost...");
    Long gno = service.register(dto);
    redirectAttributes.addFlashAttribute("msg",gno);
    redirectAttributes.addFlashAttribute("noti", "등록");
    return "redirect:/guestbook/list";
  }

  @GetMapping({"/read","/modify"})
  public void read(Long gno, Model model,
                   @ModelAttribute("requestDTO") PageRequestDTO requestDTO) {
     GuestbookDTO dto = service.read(gno);
     model.addAttribute("dto", dto);
     
  }
  @PostMapping("/modify")
  public String modify(GuestbookDTO dto, RedirectAttributes redirectAttributes,
                       @ModelAttribute("requestDTO")PageRequestDTO requestDTO) {
    log.info("post modify..................");
    log.info("dto: " + dto);
    service.modify(dto);
    redirectAttributes.addAttribute("page", requestDTO.getPage());
    redirectAttributes.addAttribute("type", requestDTO.getType());
    redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
    redirectAttributes.addAttribute("gno", dto.getGno());
    //addAttribute 기록이남음 ex)page번호
    return "redirect:/guestbook/read";
  }

  //래퍼클래스인 경우
  @PostMapping("/remove")
  public String remove(Long gno, RedirectAttributes redirectAttributes){
    service.remove(gno);
    redirectAttributes.addFlashAttribute("msg", gno);
    redirectAttributes.addFlashAttribute("noti", "삭제");
    //addFlashAttribute는 일회성 한번보여주고 사라짐
    return "redirect:/guestbook/list";
    //redirect 주소
  }

}

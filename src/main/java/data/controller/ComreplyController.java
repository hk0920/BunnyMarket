package data.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import data.dto.AdreplyDTO;
import data.dto.ComReplyDTO;
import data.service.ComreplyService;

@Controller
@RequestMapping("/community")
public class ComreplyController {
	@Autowired
	ComreplyService service;
	
	@PostMapping("/reinsert")
	public String insert(
			@ModelAttribute ComReplyDTO dto,
			HttpSession session,
			@RequestParam(value = "currentPage", required = false) String currentPage
			) 
	{
		//세션에 로그인한 아이디 얻기

		//아이디에 대한 작성자 얻기
		
		//dto에 넣기
		
		//insert
		service.insertReply(dto);
		return "redirect:detail?idx="+dto.getNum()+"&currentPage="+currentPage;
	}
	
	
	@GetMapping("/relist")
	public @ResponseBody List<ComReplyDTO> relist(@RequestParam int num,
			@RequestParam(value = "currentPage", required = false) String currentPage) 
	{
		List<ComReplyDTO> list = service.getReplyList(num);
		
		ComReplyDTO dto=new ComReplyDTO();
		
		dto.setNum("num");
		dto.setRegroup(Integer.parseInt("regroup"));
		dto.setRelevel(Integer.parseInt("relevel"));
		dto.setRestep(Integer.parseInt("restep"));
		
		list.add(dto);
		
		return service.getReplyList(num);
	}
	
	@GetMapping("/redata")
	public ComReplyDTO data(int idx) {
		return service.getReply(idx);
	}
	
	@PostMapping("/reupdate")
	public void aupdate(@ModelAttribute ComReplyDTO dto) {
		service.updateReply(dto);
	}
	
	@ResponseBody
	@GetMapping("/redelete")
	public void delete(int idx) {
		
		service.deleteReply(idx);

	}
}
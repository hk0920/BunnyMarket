package data.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import data.dto.MemberDTO;
import data.service.MemberService;

@Controller()
@RequestMapping("/join")
public class MemberController {
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	MemberService service;
	
	@GetMapping("/main")
	public String joinHome() {
		return "/join/main";
	}

	@GetMapping("/addForm")
	public String rightForm(
		@RequestParam String type,
		Model model
		) 
	{
		model.addAttribute("joinType", type);
		return "/join/joinForm";
	}	
	
	@PostMapping("/CheckMail")
	public @ResponseBody HashMap<String, String> sendMail(
		String mail
		)
	{
		Random random = new Random();
		String key="";
		for(int i=0; i<3; i++) {
			int index = random.nextInt(25)+65;
			key+=(char)index;
		}
		int numIndex = random.nextInt(9999)+1000;
		key+=numIndex;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("key", key);
		
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			message.setSubject("BunnyMarket 인증번호 입력을 위한 메일 전송");
			message.addRecipient(RecipientType.TO, new InternetAddress(mail, "포크", "UTF-8"));
			
			String mailForm = "<div style='width:960px; margin:0 auto;'>";
			mailForm += "<table  cellpadding='0' cellspacing='0' border='0' width='960' align='left' valign='middle' style='margin:0 auto; padding:0; min-width:960px; border:1px solid #ebebeb; font-family:'맑은 고딕', 'Malgun Gothic', '돋움', Dotum, sans-serif; font-size:18px; color:#666666; letter-spacing:-1.3px; line-height:1.8;'>";
			mailForm += "<tbody>";
			mailForm += "<tr>";
			mailForm += "<td width='70' height='80'></td>";
			mailForm += "<td width='820' height='80'></td>";
			mailForm += "<td width='70' height='80'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td height='30'></td>";
			mailForm += "<td height='30' align='right'>";
			mailForm += "<a href='http://localhost:9002/'><img src='http://localhost:9002/image/logo2.png' alt='바니마켓 로고' style='height:30px;'/></a>";
			mailForm += "</td>";
			mailForm += "<td height='30'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td height='30'></td>";
			mailForm += "<td height='30'></td>";
			mailForm += "<td height='30'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td></td>";
			mailForm += "<td>";
			mailForm += "<p style='font-size:40px; color:#333333; font-weight:300; line-height:1; letter-spacing:-4px;'>인증번호 안내</p>";
			mailForm += "</td>";
			mailForm += "<td></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td height='30'></td>";
			mailForm += "<td height='30'></td>";
			mailForm += "<td height='30'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td></td>";
			mailForm += "<td>";
			mailForm += "<p style='font-size:16px;'>안녕하세요.고객님<br/>바니마켓 회원가입을 위한 인증번호를 안내해드립니다.<br/> 인증번호를 입력해주세요.</p>";
			mailForm += "</td>";
			mailForm += "<td></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td height='15'></td>";
			mailForm += "<td height='15'></td>";
			mailForm += "<td height='15'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td></td>";
			mailForm += "<td style='padding:30px; background:#f5f5f5; text-align:center;'>";
			mailForm += "<p style='font-size:25px; font-weight:500; color:#333333;'><span style='margin-right:20px; color:#666666;'>인증번호</span>" + key + "</p>";
			mailForm += "</td>";
			mailForm += "<td></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td height='15'></td>";
			mailForm += "<td height='15'></td>";
			mailForm += "<td height='15'></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td></td>";
			mailForm += "<td>";
			mailForm += "<p style='font-size:16px;'>감사합니다.</p>";
			mailForm += "</td>";
			mailForm += "<td></td>";
			mailForm += "</tr>";
			mailForm += "<tr>";
			mailForm += "<td width='70' height='80'></td>";
			mailForm += "<td width='820' height='80'></td>";
			mailForm += "<td width='70' height='80'></td>";
			mailForm += "</tr>";
			mailForm += "</tbody>";
			mailForm += "</table>";
			mailForm += "</div>";
			message.setText(mailForm, "UTF-8", "html");
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			// TODO: handle exception
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
		}
		return map;
	}
	
	@PostMapping("/insert") 
	public String insertMember(
		@RequestParam String type,
		@RequestParam String email1,
		@RequestParam String email2,
		@RequestParam String hp1,
		@RequestParam String hp2,
		@RequestParam String hp3,
		@RequestParam String addrLocal,
		@RequestParam String zonecode,
		@RequestParam String addr1,
		@RequestParam String addr2,
		MemberDTO dto
		) 
	{	
		dto.setType(type);
		dto.setPw(encoder.encode(dto.getPw()));
		dto.setEmail(email1 + "@" + email2);
		dto.setHp(hp1 + "-" + hp2 + "-" + hp3);
		dto.setLocal(addrLocal);
		dto.setAddr(addr1 + "," + addr2);
		dto.setZonecode(zonecode);
		dto.setCurrent_local(addrLocal);
		service.insertMember(dto);
		return "redirect:complete";
	}
	
	@GetMapping("/complete")
	public ModelAndView completeJoin(
		Principal principal
		) 
	{
		ModelAndView mview = new ModelAndView();
		String userId = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		if(principal != null) {
			userId = principal.getName();
			local = service.getLocal(principal);
			currentLocal = service.currentLocal(userId);
			localArr = local.split(",");
		}
		
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("currentLocal", currentLocal);
		mview.setViewName("/join/complete");
		return mview;
	}
	
	@PostMapping("/pwCheck")
	public @ResponseBody HashMap<String, Integer> getPwCheck(
		@RequestParam String pw,
		Principal principal
		)
	{
		String originPw = service.getPw(principal.getName());
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if(encoder.matches(pw, originPw)) {
			map.put("result", 1);
		}else {
			map.put("result", 0);
		}
		
		return map;
	}
	
	@GetMapping("/idCheck")
	public @ResponseBody HashMap<String, Integer> getIdCheck(
		@RequestParam String id
		) 
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("id", service.getIdCheck(id));
		return map;
	} 
	
	@GetMapping("/nickCheck")
	public @ResponseBody HashMap<String, Integer> getNickCheck(
		@RequestParam String nick
		) 
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("nick", service.getNickCheck(nick));
		return map;
	} 
}

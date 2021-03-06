package data.controller;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import data.service.MemberService;

@Controller
@RequestMapping("/local")
public class LocalController {
	@Autowired
	MemberService memberService;
	
	@GetMapping("/auth/add")
	public ModelAndView addLocal(
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
			local = memberService.getLocal(principal);
			currentLocal = memberService.currentLocal(userId);
			localArr = local.split(",");
		}
		
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("currentLocal", currentLocal);
		mview.setViewName("/local/addLocal");
		return mview;
	}
	
	@PostMapping("/auth/update")
	public @ResponseBody void updateLocal(
		@RequestParam String local,
		Principal principal
		) 
	{
		HashMap<String, String> localMap = new HashMap<String, String>();
		String userId = principal.getName();
		localMap.put("id", userId);
		localMap.put("local", local);
		memberService.updateLocal(localMap);
	}
	
	@GetMapping("/auth/getlocal")
	public @ResponseBody HashMap<String, String> getLocal(
		@RequestParam String local,
		Principal principal
		)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		System.out.println("local=>"+local);
		map.put("local", local);
		map.put("id", principal.getName());
		String[] localArr = memberService.getLocal(principal).split(",");
		String orginalLocal = localArr[0];
		System.out.println("orginalLocal=>"+orginalLocal);
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("orginalLocal", orginalLocal);
		return resultMap;
	}
	

	@GetMapping("/auth/updatecurrentlocal")
	public @ResponseBody void updateCurrentLocal(
		@RequestParam String local,
		Principal principal
		) 
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", principal.getName());
		map.put("current_local", local);
		memberService.updateCurrentLocal(map);
	}
}

package data.controller;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import data.dto.AdvertiseDTO;
import data.dto.FaqDTO;
import data.dto.FollowDTO;
import data.dto.MemberDTO;
import data.dto.ProductDTO;
import data.dto.ReviewDTO;
import data.service.FollowService;
import data.service.MemberService;
import data.service.ProductLikeService;
import data.service.ProductService;
import data.service.ReviewService;


@Controller
@RequestMapping("/mypage/auth")
public class MypageController {
	@Autowired
	MemberService memService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	ProductService pservice;
	
	@Autowired
	FollowService followService;
	
	@Autowired
	ProductLikeService plservice;
	
	@Autowired
	ReviewService revservice;
	
	@GetMapping("/detail")
	public ModelAndView detail(
			HttpServletRequest request,
			Principal principal
		) 
	{
		ModelAndView mview=new ModelAndView();
		String id = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		if(principal != null) {
			id = principal.getName();
			local = memService.getLocal(principal);
			currentLocal = memService.currentLocal(id);
			localArr = local.split(",");
		}
		
		String nick=memService.getNick(principal.getName());
		String profile = memService.getMemberId(principal.getName()).getProfile();
		
		mview.addObject("nick", nick);
		mview.addObject("profile", profile);
		mview.addObject("myId", id);
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("currentLocal", currentLocal);
		
		mview.setViewName("/mypage/detail");
		return mview;
	}
	
	@GetMapping("/profileupdateform")
	public ModelAndView pupdateform(
			HttpServletRequest request,
			Principal principal
		) 
	{
		ModelAndView mview=new ModelAndView();
		
		String userId = "no";
		String userNickName = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		String profile = memService.getMemberId(principal.getName()).getProfile();
		
		if(principal != null) {
			userId = principal.getName();
			userNickName = memService.currentUserNickName(principal);
			local = memService.getLocal(principal);
			currentLocal = memService.currentLocal(userId);
			localArr = local.split(",");
		}		
		mview.addObject("userId", userId);
		mview.addObject("userNickName", userNickName);		
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("currentLocal", currentLocal);
		mview.addObject("profile", profile);
		
		mview.setViewName("/mypage/profile_updateForm");
		return mview;
	}
	
	@PostMapping("/profileupdate")
	public String pudate(
			@RequestParam String nickname,
			MultipartFile profile,
			HttpSession session,
			Principal principal
		) 
	{
		String userId = "no";
		String local = "";
		String[] localArr = {};
		
		if(principal != null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			localArr = local.split(",");
		}
		UUID uuid = UUID.randomUUID();
		
		String path = session.getServletContext().getRealPath("/photo");
		System.out.println(path);

		String photo = "no";
		if(!profile.getOriginalFilename().equals("")) {
			//이전 사진 삭제
			String ufile=memService.getMemberId(principal.getName()).getProfile();
			File file=new File(path+"\\"+ufile);
			file.delete();		
			photo = uuid.toString() + "_" + profile.getOriginalFilename();
			
			try {
				profile.transferTo(new File(path + "\\" + photo));
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HashMap<String, String> profileMap = new HashMap<String, String>();
			profileMap.put("profile", photo);
			profileMap.put("id", userId);
			memService.updateProfile(profileMap);
		}
		
		HashMap<String, String> map=new HashMap<String, String>();
		map.put("nickname",nickname);
		map.put("id", userId);
		memService.updateNickName(map);
		
		return "redirect:/mypage/auth/detail";
	}
	
	@PostMapping("/changepw")
	public @ResponseBody void changePw(
		@RequestParam String pw,
		Principal principal
		) 
	{
		String changePw = encoder.encode(pw);
		String userId = principal.getName();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pw", changePw);
		map.put("id", userId);
		memService.updatePw(map);
	} 
	
	@GetMapping("/member/updateform")
	public ModelAndView updateForm(
		Principal principal
		) 
	{
		ModelAndView mview = new ModelAndView();
		String userId = "no";
		String local="";
		String[] localArr = {};
		String currentLocal = "";
		if(principal != null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			currentLocal = memService.currentLocal(userId);
			localArr = local.split(",");
		}
		MemberDTO dto = memService.getMemberId(userId);
		
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("currentLocal", currentLocal);
		mview.addObject("dto", dto);
		mview.setViewName("/mypage/updateMemberForm");
		return mview;
	}
	
	@PostMapping("/member/update")
	public String updateMember(
		@RequestParam String email1,
		@RequestParam String email2,
		@RequestParam String hp1,
		@RequestParam String hp2,
		@RequestParam String hp3,
		@RequestParam String addrLocal,
		@RequestParam String zonecode,
		@RequestParam String addr1,
		@RequestParam String addr2,
		MemberDTO dto,
		Principal principal
		) 
	{	
		String[] localArr = memService.getLocal(principal).split(",");
		String local = memService.getLocal(principal);
		for(var i=0; i<localArr.length; i++) {
			if(localArr.length == 2) {
				local = addrLocal + "," + localArr[1] + ",";	
			}else {
				local = addrLocal + ",";
			}
		}
		local = local.substring(0, local.length() - 1);
		if(localArr.length == 1) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("current_local", local);
			map.put("id", principal.getName());
			memService.updateCurrentLocal(map);
		}
			
		dto.setEmail(email1 + "@" + email2);
		dto.setHp(hp1 + "-" + hp2 + "-" + hp3);
		dto.setLocal(local);
		dto.setAddr(addr1 + "," + addr2);
		dto.setZonecode(zonecode);
		memService.updateMember(dto);
		return "redirect:/mypage/auth/detail";
	}
	
	@GetMapping("/member/deleteform")
	public ModelAndView deleteForm(
		Principal principal
		) 
	{
		ModelAndView mview = new ModelAndView();
		String userId = "no";
		String userNickName = "";
		String local="";
		String[] localArr = {};
		String currentLocal = "";
		if(principal != null) {
			userId = principal.getName();
			userNickName = memService.currentUserNickName(principal);
			local = memService.getLocal(principal);
			currentLocal = memService.currentLocal(userId);
			localArr = local.split(",");
		}
		
		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.addObject("userNickName", userNickName);
		mview.addObject("currentLocal", currentLocal);
		mview.setViewName("/mypage/deleteMemberForm");
		return mview;
	}
	
	@GetMapping("/member/deletemember")
	public String deleteMember(
		Principal principal,
		HttpServletRequest request
		)
	{
		memService.deleteMember(principal.getName());
		request.getSession().removeAttribute("isLogin");
		return "redirect:/logout";
	}
	
	@GetMapping("/productlike")
	public ModelAndView productLikeList(
		@RequestParam (defaultValue = "1") int currentPage, 
		Principal principal
		) 
	{ 
		ModelAndView mview = new ModelAndView();
		//지역 가져오기
		String userId = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		if(principal != null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			localArr = local.split(",");
			currentLocal = memService.currentLocal(userId);
		}
		mview.addObject("localCnt",localArr.length);
		
		mview.addObject("localArr",localArr);
		String id = principal.getName();
		int totalCount = plservice.getTotalCount(id);
		
		//페이징 처리에 필요한 변수 선언
		int perPage = 20;
		int totalPage;
		int start;
		int perBlock = 5;
		int startPage;
		int endPage;
		
		//총 페이지 갯수 구하기
		totalPage = totalCount/perPage+(totalCount%perPage==0?0:1);
		//각 블럭의 시작 페이지
		startPage = (currentPage-1)/perBlock*perBlock +1;
		//각 블럭의 마지막 페이지
		endPage = startPage + perBlock -1;
		
		if(endPage > totalPage) {
			endPage = totalPage;
		}
		
		//각 페이지에서 불러올 시작번호
		start = (currentPage-1)*perPage;
		
		List<ProductDTO> list = plservice.getList(start, perPage, id);
		
		//각 페이지에 출력할 시작번호
		int no = totalCount-(currentPage-1)*perPage;
		
		//닉네임 가져오기
		String nick=memService.getNick(principal.getName());		
		mview.addObject("nick", nick);
		//출력에 필요한 변수들을 request에 저장
		mview.addObject("list",list);
		mview.addObject("startPage", startPage);
		mview.addObject("endPage", endPage);
		mview.addObject("totalPage", totalPage);
		mview.addObject("no", no);
		mview.addObject("currentPage", currentPage);
		mview.addObject("totalCount", totalCount);
		mview.addObject("currentLocal", currentLocal);
		
		mview.setViewName("/mypage/productLikeList");
			  
		return mview; 
	}
	
	@GetMapping("/selllist")
	public @ResponseBody ModelAndView sellList(
		@RequestParam(defaultValue = "no") String sellstatus,
		@RequestParam(defaultValue = "1") int currentPage,
		Principal principal) throws Exception
	{
		ModelAndView mview = new ModelAndView();
		
		//지역 가져오기
		String userId = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		
		if(principal != null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			localArr = local.split(",");
			currentLocal = memService.currentLocal(userId);
		}
		
		int totalCount = pservice.getStatusCount(userId, sellstatus);
		int perPage = 10; 
		int totalPage;
		int start; 
		int perBlock=5; 
		int startPage; 
		int endPage;
		
		totalPage = totalCount/perPage + (totalCount%perPage==0?0:1);
		startPage = (currentPage-1)/perBlock * perBlock +1; 
		endPage = startPage + perBlock-1;
		start = (currentPage-1) * perPage; 
		if(endPage>totalPage){ 
			endPage = totalPage; 
			}
		System.out.println("아이디"+userId);
		List<ProductDTO> list = pservice.getStatusList(userId, sellstatus);
		System.out.println("사이즈"+list.size());
		
		
		
		//출력에 필요한 변수들을 request에 저장
		mview.addObject("list",list);
		mview.addObject("startPage",startPage);
		mview.addObject("endPage",endPage);
		mview.addObject("totalPage",totalPage);
		mview.addObject("totalCount", totalCount);
		mview.addObject("currentPage",currentPage);
		mview.addObject("localCnt",localArr.length);
		mview.addObject("localArr",localArr);
		mview.addObject("currentLocal",currentLocal);
		
		mview.setViewName("/mypage/sellList");
		
		return mview;
	}

	@GetMapping("/getListByStatus")
	public @ResponseBody Map<String, Object> getListByStatus(
			@RequestParam(defaultValue = "no") String sellstatus,
			@RequestParam(defaultValue = "1") int currentPage,
			Principal principal) 
		{
		
		//지역 가져오기
		String userId = "no";
		
		if(principal != null) {
			userId = principal.getName();
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		//System.out.println("currentPage="+currentPage);
		int totalCount = pservice.getStatusCount(userId, sellstatus);
		int perPage=10;
		int totalPage;
		int start;
		int perBlock=5;
		int startPage;
		int endPage;
		
		totalPage=totalCount/perPage+(totalCount%perPage == 0?0:1);
		startPage=(currentPage-1)/perBlock*perBlock+1;
		endPage=startPage+perBlock-1;
		
		if(endPage>totalPage) {
			endPage=totalPage;
		}
		
		start=(currentPage-1)*perPage;
		

		List<ProductDTO> list = pservice.getStatusList(userId, sellstatus);
		System.out.println("size:"+list.size());
		System.out.println("status"+sellstatus);
		
		result.put("list", list);
		result.put("sellstatus", sellstatus);
		result.put("startPage", startPage);
		result.put("endPage", endPage);
		result.put("totalPage", totalPage);
		result.put("currentPage", currentPage);
		result.put("totalCount", totalCount);

		return result;
	}
	
	@GetMapping("/followlist")
	public @ResponseBody ModelAndView followlist(
		@RequestParam(defaultValue = "1") int currentPage,
		Principal principal) throws Exception
	{
		ModelAndView mview = new ModelAndView();
		
		//지역 가져오기
		String userId = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		
		if(principal != null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			localArr = local.split(",");
			currentLocal = memService.currentLocal(userId);
		}
		
		int totalCount = followService.followeeCount(userId);
		int perPage = 15; 
		int totalPage;
		int start; 
		int perBlock=5; 
		int startPage; 
		int endPage;
		
		System.out.println(totalCount);
		
		//총 페이지 개수
		totalPage=totalCount/perPage+(totalCount%perPage==0?0:1);
		//각 블럭의 시작페이지
		startPage=(currentPage-1)/perBlock*perBlock+1;
		endPage=startPage+perBlock-1;
		if(endPage>totalPage) {
			endPage=totalPage;
		}
		//각 페이지에서 불러올 시작번호
		start=(currentPage-1)*perPage;
		
		//팔로우 가져오기
		List<FollowDTO> followList=followService.followeeList(userId);
		mview.addObject("followList", followList);
		
		//출력에 필요한 변수들을 request에 저장
		mview.addObject("startPage",startPage);
		mview.addObject("endPage",endPage);
		mview.addObject("totalPage",totalPage);
		mview.addObject("currentPage",currentPage);
		mview.addObject("totalCount", totalCount);
		
		mview.addObject("localCnt",localArr.length);
		mview.addObject("localArr",localArr);
		mview.addObject("currentLocal",currentLocal);
		
		mview.setViewName("/mypage/follow_list");
		
		return mview;
	}
	

	@GetMapping("/reviewlist")
	public ModelAndView reviewList(
			@RequestParam(defaultValue = "1") int currentPage,
			Principal principal
			)
	{
		ModelAndView mview = new ModelAndView();
		
		//지역 가져오기
		String userId = "no";
		String local = "";
		String[] localArr = {};
		String currentLocal = "";
		if(principal !=null) {
			userId = principal.getName();
			local = memService.getLocal(principal);
			localArr = local.split(",");
			currentLocal = memService.currentLocal(userId);
		}
		mview.addObject("localCnt",localArr.length);
		mview.addObject("localArr",localArr);
		mview.addObject("currentLocal",currentLocal);
		
		int totalCount = revservice.getTotalCount();
		int perPage = 10;
		int totalPage;
		int start;
		int perBlock=5;
		int startPage;
		int endPage;
		
		totalPage = totalCount/perPage + (totalCount%perPage==0?0:1);
		startPage = (currentPage-1)/perBlock * perBlock +1;
		endPage = startPage + perBlock-1;
		if(endPage>totalPage) {
			endPage = totalPage;
		}
		start = (currentPage-1) * perPage;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("reviewer", userId);
		map.put("start", start);
		map.put("perpage", perPage);
		
		System.out.println("userId=>" +userId);
		System.out.println("start=>" +start);
		System.out.println("perpage=>" +perPage);
		
		List<ReviewDTO> myrelist = revservice.getMyReviewList(map);
		System.out.println("1:"+myrelist.size());
		mview.addObject("myrelist",myrelist);
		
		
		/*
		 * List<ReviewDTO> otherrelist =revservice.getOtherReviewList(userId,
		 * Integer.toString(start), Integer.toString(perPage));
		 * mview.addObject("otherrelist",otherrelist);
		 */
		
		mview.addObject("startPage",startPage);
		mview.addObject("endPage",endPage);
		mview.addObject("totalPage",totalPage);
		mview.addObject("currentPage",currentPage);
		mview.addObject("totalCount", totalCount);
		
		mview.addObject("localCnt",localArr.length);
		mview.addObject("localArr",localArr);
		mview.addObject("currentLocal",currentLocal);
		
		mview.setViewName("/mypage/myReview");
		return mview;
	}

}

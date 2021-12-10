package data.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import data.dto.ChatDTO;
import data.dto.ProductDTO;
import data.service.ChatService;
import data.dto.ReviewDTO;
import data.service.FollowService;
import data.service.MemberService;
import data.service.ProductLikeService;
import data.service.ProductService;
import data.service.ReviewService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService service;

	@Autowired
	ProductLikeService plservice;

	@Autowired
	FollowService flservice;

	@Autowired
	MemberService mservice;
	
	@Autowired
	ChatService cservice;
	
	@Autowired 
	ReviewService rservice;

	@ResponseBody
	@GetMapping("/list")
	public ModelAndView productList(
			@RequestParam (defaultValue = "1") int currentPage,
			@RequestParam (defaultValue = "전체") String category,
			@RequestParam (required = false) String keyword,
			Principal principal) { 
		ModelAndView mview = new ModelAndView();

		int totalCount = service.getTotalCount(category);

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

		List<ProductDTO> list = service.getList(start, perPage, category, keyword);

		//각 페이지에 출력할 시작번호
		int no = totalCount-(currentPage-1)*perPage;

		//지역가져오기
		String userId="no";
		String local="";
		String []localArr = {};
		//로그인 되어있을 경우,
		if(principal != null) {
			userId= principal.getName();
			local = mservice.getLocal(principal);
			localArr=local.split(",");
		}

		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);

		//출력에 필요한 변수들을 request에 저장
		mview.addObject("list",list);
		mview.addObject("startPage", startPage);
		mview.addObject("endPage", endPage);
		mview.addObject("totalPage", totalPage);
		mview.addObject("no", no);
		mview.addObject("currentPage", currentPage);
		mview.addObject("totalCount", totalCount);

		mview.addObject("category", category);

		mview.setViewName("/product/list");

		return mview; 
	}


	@GetMapping("/auth/updateForm")
	public ModelAndView updateForm(@RequestParam String idx, Principal principal) {
		ModelAndView mview = new ModelAndView();

		ProductDTO dto = service.getData(idx);
		
		//지역가져오기
		String userId="no";
		String local="";
		String []localArr = {};
		//로그인 되어있을 경우,
		if(principal != null) {
			userId= principal.getName();
			local = mservice.getLocal(principal);
			localArr=local.split(",");
		}

		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);

		mview.addObject("dto", dto);
		mview.setViewName("/product/updateForm");

		return mview;
	}

	@GetMapping("/auth/insertForm")
	public ModelAndView insertForm(Principal principal) {
		ModelAndView mview = new ModelAndView();
		
		//지역가져오기
		String local=mservice.getLocal(principal);
		String []localArr = local.split(",");

		mview.addObject("localCnt", localArr.length);
		mview.addObject("localArr", localArr);
		mview.setViewName("/product/insertForm");
		
		return mview;
	}
	
	@ResponseBody
	@PostMapping("/auth/insert")
	public HashMap<String, Integer> insertData(HttpServletRequest request, Principal principal,
			MultipartHttpServletRequest multiRequest, HttpSession session) throws Exception 
	{
		//System.out.println("fileList =>" + multiRequest.getFile("uploadFile"));
		List<MultipartFile> fileList = multiRequest.getFiles("uploadFile");
		String title = multiRequest.getParameter("title");
		String content = multiRequest.getParameter("content");
		String price = multiRequest.getParameter("price");
		String category = multiRequest.getParameter("category");
		String sellstatus = multiRequest.getParameter("sellstatus");
		String local = multiRequest.getParameter("local");
		
		String photoname = "";
		
		if(fileList != null) {
			String path = session.getServletContext().getRealPath("/photo");
			
			System.out.println(path);

			File dir = new File(path);
			if(!dir.isDirectory()) {
				dir.mkdirs();
			}
			
			if(!fileList.isEmpty()) {
				for(int i=0; i<fileList.size(); i++) {
					String random = UUID.randomUUID().toString();
					String originalFileName = fileList.get(i).getOriginalFilename();
					String saveFileName = random + "_" + originalFileName;
					String savePath = path + "\\" + saveFileName;
					fileList.get(i).transferTo(new File(savePath));		
					
					photoname += saveFileName + ",";
				}
				
				photoname = photoname.substring(0, photoname.length()-1);
			}
		}
		
		ProductDTO dto =  new ProductDTO();
		
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPrice(price);
		dto.setCategory(category);
		dto.setUploadfile(photoname);
		dto.setSellstatus(sellstatus);
		dto.setLocal(local);
		
		//로그인된 아이디
		String id = principal.getName();
		dto.setId(id);
		
		service.insertData(dto);
		
		HashMap<String , Integer> map = new HashMap<String, Integer>();
		
		map.put("idx", service.getMaxIdx());
		
		return map;
	}
	
	@PostMapping("/auth/update")
	public String updateData(@ModelAttribute ProductDTO dto, HttpServletRequest request, HttpSession session, Principal principal,String idx) {
		//업로드된 파일 리스트
		List<MultipartFile> mf = dto.getUpload(); 
		
		// 파일 업로드 안했을 경우,
		if (mf.get(0).getOriginalFilename().equals("")) {
			//기존 사진 그대로
			String uploadfile = dto.getUploadfile();
			// 파일 업로드 했을 경우,
		} else{
			// 저장할 폴더 지정
			String path = session.getServletContext().getRealPath("/photo");
			String fileplus="";
			
			for(int i=0; i<mf.size(); i++) {
				// uuid 생성
				UUID uuid = UUID.randomUUID();
				// uuid 활용해 파일이름 지정 
				String uploadfile = uuid.toString() + "_" + mf.get(i).getOriginalFilename();
				// 실제 업로드
				try {
					mf.get(i).transferTo(new File(path + "\\" + uploadfile));
				} catch (IllegalStateException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileplus += uploadfile+",";
			}
			//마지막 컴마 제거
			fileplus = fileplus.substring(0,fileplus.length()-1);
			
			dto.setUploadfile(fileplus);
		}
		//세션에서 아이디 얻어서 dto에 저장
		String id = principal.getName();
		dto.setId(id);
		
		service.updateData(dto);
		
		return "redirect:../detail?idx="+dto.getIdx();
	}

	@GetMapping("/detail")
	public String content(@RequestParam String idx,
			@RequestParam (defaultValue = "1") int currentPage, 
			@RequestParam (required = false) String key,
			Model model, HttpServletRequest request, Principal principal,@ModelAttribute ProductDTO pdto,ReviewDTO rdto) {
		//由ъ뒪�듃�뿉�꽌 �뵒�뀒�씪�럹�씠吏�媛�硫� 議고쉶�닔 �삱�씪媛�寃�
		if(key!=null) {
			service.updateReadcount(idx);
		}


		//해당 idx의 데이터 가져오기
		ProductDTO dto = service.getData(idx);
		//�궗吏� ,濡� split(���몴 �씠誘몄�)
		String []photo = dto.getUploadfile().split(",");


		//닉네임 가져오기
		String nick = mservice.getNick(dto.getId());

		//같은 카테고리 연관제품 보여주기
		String category = dto.getCategory();
		List<ProductDTO> list = service.getRelateList(category,idx);

		//로그인 여부
		String isLogin = "N";
		isLogin = (String)request.getSession().getAttribute("isLogin");
		
		System.out.println("isLogin =>" + isLogin);

		//로그인 되어 있을 경우,

		if(isLogin!=null) {
			//濡쒓렇�씤 �븘�씠�뵒 媛��졇�삤湲�
			String id = principal.getName();
			model.addAttribute("myId", id);

			//지역가져오기
			String userId="no";
			String local="";
			String []localArr = {};

			local = mservice.getLocal(principal);
			localArr=local.split(",");
			

			model.addAttribute("localCnt", localArr.length);
			model.addAttribute("localArr", localArr);


			//하트 버튼 클릭여부
			int likeCheck = plservice.plikeCheck(id,idx);
			model.addAttribute("likeCheck", likeCheck);

			//팔로우 여부

			int followCheck = flservice.followCheck(dto.getId(), id);
			System.out.println("follow?"+followCheck);
			model.addAttribute("followCheck", followCheck);

			if(id.equals(dto.getId())) {
				//�뙋留ㅼ긽�깭
				String sellstatus = dto.getSellstatus();
				if(sellstatus.equals("판매중")) {
					dto.setSellstatus("selling");
				} else if(sellstatus.equals("예약중")) {
					dto.setSellstatus("reserved");
				} else {
					dto.setSellstatus("finished");
				}
			}
		}
		//팝업창 관련
		
		//팝업창 choose
		List<ChatDTO> poplist=rservice.getList(idx);
		model.addAttribute("poplist", poplist);
		model.addAttribute("idx", idx);
		
		//팝업창 insert
		
		String seller=principal.getName();
		model.addAttribute("seller", seller);
		
		model.addAttribute("dto", dto);
		model.addAttribute("list", list);
		model.addAttribute("isLogin", isLogin);
		model.addAttribute("nick", nick);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("photo", photo);

		return "/product/detail";
	}

	@GetMapping("/auth/delete")
	public String deleteData(@RequestParam String idx, @RequestParam String currentPage) {
		service.deleteData(idx);
		
		String state ="삭제됨";
		
		//채팅 테이블 state 바꾸기
		cservice.updateChatState(idx, state);

		return "redirect:../list?currentPage="+currentPage;
	}

	@ResponseBody
	@PostMapping("/updateLikecount")
	public int updateLikecount(@RequestParam String idx, Principal principal) {
		String id = principal.getName();
		//product�쓽 likecount+1
		service.updateLikecount(idx);
		//product_like�쓽 �뜲�씠�꽣 異붽�
		plservice.insertPlike(id,idx);

		//like 수 리턴
		return service.getLikeCount(idx);
	}

	@ResponseBody
	@PostMapping("/updateLikeMinuscount")
	public int updateLikeMinuscount(@RequestParam String idx, Principal principal) {
		String id = principal.getName();
		//product�쓽 likecount-1
		service.updateLikeMinuscount(idx);


		//product_like의 데이터 삭제
		plservice.deletePlike(id,idx);

		//like 수 리턴

		return service.getLikeCount(idx);
	}

	@ResponseBody
	@PostMapping("/updateStatus")
	public void updateStatus(@RequestParam String idx, @RequestParam String status) {
		if(status.equals("selling")) {
			status = "판매중";
		} else if(status.equals("reserved")) {
			status = "예약중";
		} else if(status.equals("finished")) {
			status = "판매완료";
		}
		service.updateStatus(idx, status);
	}
	
	
	


	
	@PostMapping("/popinsert")
	public String insert(@ModelAttribute ReviewDTO rdto)
	{	
		rservice.ReviewInsert(rdto);
		
		return "/product/list";
	}
	

}

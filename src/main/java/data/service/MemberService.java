package data.service;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import data.dto.MemberDTO;
import data.mapper.MemberMapper;

@Service
public class MemberService implements UserDetailsService {
	
	@Autowired
	MemberMapper mapper;
	
	String userId = "";
	
	
	public void insertMember(MemberDTO dto) {
		mapper.insertMember(dto);
	}
	
	public int getIdCheck(String id) {
		return mapper.getIdCheck(id);
	}
	
	public int getNickCheck(String nick) {
		return mapper.getNickCheck(nick);
	}
	
	public String getPw(String id) {
		return mapper.getPw(id);
	}
	
	public String getIdFindMember(HashMap<String, String> map) {
		return mapper.getIdFindMember(map);
	}
	
	public MemberDTO getMemberId(String id) {
		return mapper.getMemberId(id);
	}
	
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {		
		MemberDTO member = mapper.getMemberId(id);
		
		if(member == null) throw new UsernameNotFoundException("Not Fount account");
		
		return member;
	}
	
	public String currentUserId(Principal principal) {
		userId = principal.getName();
		return principal.getName();
	}
	
	public String currentUserNickName(String id) {
		return "";
	}
}
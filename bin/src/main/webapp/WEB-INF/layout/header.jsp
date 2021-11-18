<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="root" value="<%=request.getContextPath() %>"></c:set>
<header id="header">
	<div class="util">
		<div class="inner">
			<a href="javascript" class="">로그인</a>
			<a href="javascript" class="">회원가입</a>
		</div>
	</div>
	<div class="header-div">
		<div class="inner">
			<h1 class="logo">
				<a href="javascript:">바니마켓 로고</a>
			</h1> 
			<nav class="menu">
				<button type="button" class="menu-btn">
					<strong>메뉴 열기</strong>
					<span></span>
					<span></span>
					<span></span>
				</button>
				<ul class="gnb">
					<li>
						<a href="javascript:" class="one-depth">기업정보</a>
						<ul class="two-depth">
							<a href="javascript:">소개</a>
						</ul>
					</li>
					<li>
						<a href="javascript:" class="one-depth">동네이야기</a>
						<ul class="two-depth">
							<a href="javascript:">동네거래</a>
							<a href="javascript:">동네생활</a>
							<a href="javascript:">동네소식</a>
							<a href="javascript:">동네행사</a>
						</ul>
					</li>
					<li>
						<a href="javascript:" class="one-depth">기타</a>
						<ul class="two-depth">
							<a href="javascript:">공지사항</a>
							<a href="javascript:">자주 묻는 질문</a>
							<a href="javascript:">이용약관</a>
							<a href="javascript:">개인정보처리방침</a>
						</ul>
					</li>
				</ul>
			</nav>
			<div class="local-div">
				<button type="button" class="local-btn">지역1</button>
				<ul class="local-option">
					<li>지역1</li>
					<li>지역2</li>
					<li>내 동네 설정하기</li>
				</ul>
			</div>
		</div>
	</div>
</header>
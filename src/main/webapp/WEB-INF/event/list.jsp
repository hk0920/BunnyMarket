<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" type="text/css" href="/css/event_style.css">
<div class="event-div">
	<div class="tab-btn">
		<div class="inner">
			<a href="list?currentPage=1&status=process" class="${status == 'process'?'active':''}">진행중인 이벤트</a>
			<a href="list?currentPage=1&status=complete" class="${status == 'complete'?'active':''}">종료된 이벤트</a>
		</div>
	</div>
	<div class="category-div">
		<div class="inner">
			<a href="list?currentPage=1&status=${status}" class="${selectCategory == '전체'?'active':''}">전체</a>
			<c:forEach var="category" items="${categoryArr}">
				<a href="list?currentPage=1&category=${category}&status=${status}" class="${selectCategory == category?'active':''}">${category}</a>
			</c:forEach>
		</div>
	</div>
	<div class="inner">
		<c:if test="${userType == 'admin'}">
			<div class="top-div btn-wrap">
				<a href="./auth/insertform" class="btn-add">글쓰기</a>
			</div>
		</c:if>
		<c:if test="${totalCount==0}">
			<div class="nodata">
				<p class="icon">
					<img alt="" src="/image/nodata-icon.png">
				</p>
				<p>등록된 데이터가 없습니다.</p>
			</div>		
		</c:if>
		<c:if test="${totalCount>0}">
			<ul class="event-list">
				<c:forEach var="edto" items="${eventList}">
					<li>
						<a href="detail?idx=${edto.idx}&currentPage=${currentPage}&key=list">
							<span class="label">${edto.category}</span>
							<div class="img-div">
								<img alt="" src="/photo/${fn:split(edto.photo,',')[0]}">
							</div>
							<div class="txt-div">
								<p class="tit">${edto.title}</p>
								<p class="date">
									<fmt:formatDate value="${edto.event_start}" pattern="yyyy.MM.dd" />
									~
									<fmt:formatDate value="${edto.event_end}" pattern="yyyy.MM.dd" />
								</p>
								<div class="etc">
									<p class="reply-count">${edto.replycount}</p>
									<p class="read-count">${edto.readcount}</p>
								</div>
							</div>
						</a>
					</li>
				</c:forEach>
			</ul>
			
			<div class="paging">
				<c:if test="${startPage>1}">
					<a href="list?currentPage=${startPage-1}" class="prev"><span>이전</span></a>
				</c:if>
				<c:forEach var="pp" begin="${startPage}" end="${endPage}" >
					<a href="list?currentPage=${pp}"  ${currentPage == pp ? 'class="active"' : ''} >${pp}</a>
				</c:forEach>
				
				<!-- 다음 -->
				<c:if test="${endPage<totalPage}">
					<a href="list?currentPage=${endPage+1}" class="next"><span>다음</span></a>
				</c:if>
			</div>
		</c:if>
	</div>
</div>
<script type="text/javascript" src="/js/event_script.js"></script>

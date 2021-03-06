<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<link rel="stylesheet" type="text/css" href="/css/notice_style.css">
<!-- 작성글 없을 떄 -->
<c:if test="${totalCount==0}">
	<div class="inner">
		<div class="inner">
			<div class="nodata">
				<p class="icon">
					<img alt="" src="/image/nodata-icon.png">
				</p>

				<p>등록된 데이터가 없습니다.</p>
			</div>
		</div>
		<div class="btn-wrap">
			<button class="btn-add" type="button"
				onclick="location.href='writeform'" >글쓰기</button>
		</div>
	</div>


</c:if>
<!-- 작성글 없을 떄 -->
<div class="inner">




	<table class="table">


		<thead>

			<tr>

				<!-- 관리자가 로그인 했을 떄만 체크박스로 변경 -->

				<c:choose>

					<c:when test="${admin == 'admin'}">
						<th class="num"><input type="checkbox" value="selectall"
							onclick="selectAll(this)" name="del"></th>
					</c:when>
					<c:otherwise>
						<th class="num"></th>
					</c:otherwise>

				</c:choose>



				<th class="title">제목</th>
				<th class="writeday">작성일</th>
				<th class="readcount">조회수</th>
			</tr>
		</thead>



		<c:if test="${totalCount>0}">
			<tbody>



				<c:forEach var="n" items="${list}">
					<tr>

						<c:choose>


							<c:when test="${admin == 'admin'}">

								<td><input type="checkbox" name="del" id="${n.idx}"></td>
							</c:when>
							<c:otherwise>

								<td>${num}</td>
								<c:set var="num" value="${num-1}" />

							</c:otherwise>
						</c:choose>

						<td><a
							href="content?idx=${n.idx}&currentPage=${currentPage}&key=list">${n.title}</a>
						</td>
						<td><fmt:formatDate value="${n.writeday}" pattern="yy.MM.dd" /></td>
						<td>${n.readcount}</td>
					</tr>
				</c:forEach>











			</tbody>
		</c:if>
	</table>

	<!-- 관리자가 로그인 했을 떄만 버튼 나타남 -->
	<c:if test="${admin == 'admin'}">
		<div class="btn-wrap">
			<button class="btn-add" type="button"
				onclick="location.href='writeform'">글쓰기</button>
			<button class="btn-delete" type="button" id="delete">글삭제</button>
		</div>

	</c:if>

</div>




<!-- 페이징처리 -->
<c:if test="${totalCount>0}">
	<div class="inner">

		<div class="paging">

			<!-- 이전 -->
			<c:if test="${startPage>1}">
				<a href="list?currentPage=${startPage-1}" class="prev"><span>이전</span></a>
			</c:if>

			<c:forEach var="pp" begin="${startPage}" end="${endPage}">
				<c:if test="${currentPage==pp}">
					<a href="list?currentPage=${pp}" class="active">${pp}</a>
				</c:if>
				<c:if test="${currentPage!=pp}">
					<a href="list?currentPage=${pp}">${pp}</a>
				</c:if>
			</c:forEach>

			<!-- 다음 -->
			<c:if test="${endPage<totalPage}">
				<a href="list?currentPage=${endPage+1}" class="next">다음</a>
			</c:if>

		</div>

	</div>
</c:if>
<script type="text/javascript" src="/js/notice_script.js"></script>













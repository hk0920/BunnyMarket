<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="/css/swiper.min.css">
<link rel="stylesheet" type="text/css" href="/css/product_style.css">
<link rel="stylesheet" type="text/css" href="/css/ad_style.css">
<div class="inner">
<div class="infoAll">
	<input type="hidden" name="current-page" value="${currentPage}">
		<div class="img group">
			<div class="detail-swiper">
				<div class="bigImgDiv swiper-wrapper">
					<c:forEach items="${dto.photo}" var="dbimg">				
						<div class="swiper-slide bigImg fix">
							<c:if test="${dto.photo=='no'}">
								<img class="bigImg" alt="thumbnail" src="/image/list-noimg.gif">
							</c:if>
							<c:if test="${dto.photo!='no'}">
								<img class="bigImg" alt="thumbnail" src="../photo/${dbimg}">
							</c:if>
						</div>
					</c:forEach>
				</div>
				<div class="swiper-button-next"></div>
	   			<div class="swiper-button-prev"></div>
				<div class="swiper-pagination"></div>
			</div>
			<div class="smImgDiv">
				<c:forEach items="${dbimg}" var="dbimg">			
					<div class="smImg child">
						<c:if test="${dto.photo=='no'}">
							<img alt="smallImage" src="/image/list-noimg.gif" class="smImg">
						</c:if>
						<c:if test="${dto.photo!='no'}">
							<img alt="smallImage" src="../photo/${dbimg}" class="smImg">
						</c:if>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<div class="info">
		<table class="table table-default">
			<tr>			
				<td class="ad-tit">
					${dto.title}
				</td>
			</tr>
			<tr>
				<td rowspan="2" class="profile">
					<img alt="profile" src="/image/profile-icon.png" class="profileImg">	
				</td>
				<td class="nick tit verticalBottom">
					닉네임
				</td>
				<td rowspan="2" class="detailBtn ad-dbtn">
					<button class="btn-list" id="follow">
						<span class="glyphicon glyphicon-plus"></span>
					팔로우</button>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="tit-sm">
					작성일 <fmt:formatDate value="${dto.writeday}" pattern="yy.MM.dd HH:mm"/>
				</td>
			</tr>
			<tr class="counts">
				<td colspan="3" class="tit-sm">
				공감 <span id="likecount">${dto.goodcount}</span>&nbsp;&nbsp;&nbsp;조회수 ${dto.readcount}
				</td>
			</tr>
			<tr class="lineNeed">
				<td class="marginZero">
					<button type="button" id="dibs" onclick="dibsClicked()"><img src="/image/stopheart-icon.gif" alt="dibsButton" id="dibsBtnImg"></button>
				<!-- 로그인중(작성자) -->
				<c:if test="true">
					<td colspan="2" class="detailBtn">
						<button type="button" class="btn-update" onclick="location.href='updateform?idx=${dto.idx}&currentPage=${currentPage}'">수정</button>
						<button type="button" id="deleteBtn" class="btn-delete" value="${dto.idx}">삭제</button>
					</td>
				</c:if>
				</td>
			</tr>
		</table>
		</div>
	</div>

	<div class="detailContentDiv">
		<pre class="detailContent">${dto.content}</pre>
	</div>
	<div class="detailbtn">
		<!-- 로그인 안했을경우 -->
		<c:if test="false">
			<button type="button" class="btn-list delist"
			onclick="location.href='list'">목록</button>
		</c:if>
		
		<!-- 로그인 했을경우 -->
		<c:if test="true">
			<button type="button" class="btn-list delist"
				onclick="location.href='list'">목록</button>
			<button type="button" class="btn-add gdcount">공감</button>
		</c:if>
	</div>
	<!-- 댓글 -->
	<div class="reform tit">
		댓글 ${recount}
	</div>
	<!-- 댓글작성 -->
	<div class="reply">
		<div class="re-content">
			<!-- <input type="text" class="retext" id="recontent" placeholder="댓글을 입력하세요."> -->
			<textarea name="re-content" class="re-textinput" placeholder="댓글을 입력해주세요."
						required="required"></textarea>
		</div>
		<div class="re-items">
			<div class="re-addbtn">
				<button type="button" class="btn-add btn-sm" id="re-addbtn">등록</button>
			</div>
			<div class="text-count">
				<span class="text-plus">0</span><span>/100</span>
			</div>
		</div>
	</div>	
	<!-- 댓글목록 -->
	<div class="re-list">
		<c:if test="${recount==0}">
			<div class="nodata">
				<p class="icon">
					<img alt="" src="/image/nodata-icon.png">
				</p>
				<p>등록된 댓글이 없습니다.</p>
			</div>
		</c:if>
	</div>
	<div class="re-list">
		<c:if test="${recount>0}">
		    <c:forEach var="ardto" items="${relist}">
		    	<div class="re-info">
		    		<img alt="" src="../image/profile-icon.png" class="re-profileimg">
		    	</div>
	            <div class="re-info">
		            <span class="re-writer">${ardto.id}</span>
		            <span class="re-day">
		                <fmt:formatDate value="${ardto.writeday}" pattern="yy.MM.dd"/>
		            </span>
		        </div>
	            <div class="re-detail">		    
	                <!-- relevel 만큼 공백 -->
	                <c:forEach var="sp" begin="1" end="${ardto.relevel}">
	                    <div class="re-blank"></div>
	                </c:forEach>
	                <!-- 답글인 경우에만 re 이미지 출력 -->
	                <c:if test="${ardto.relevel>0}">
	                    <!-- <img src="../photo/re.png"> -->
	                    <div>👉</div>
	                </c:if>
	                <!-- 댓글내용 -->
	                <div class="">
	                	<span>${ardto.content}</span>
	                </div>
		        <div class="re-rebtn">
		        	<a>답글쓰기</a>
		        </div>
	            </div>
		    </c:forEach>
		</c:if>
	</div>
</div>

<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<!-- Initialize Swiper -->
<script>
setTimeout(() => {
	 var swiper = new Swiper(".mySwiper", {
		    navigation: {
		      nextEl: ".swiper-button-next",
		      prevEl: ".swiper-button-prev",
		    },
		    pagination: {
		      el: ".swiper-pagination",
		    },
		  });
}, 20);

//미리보기 이미지 클릭시,
$(document).on("click",".smallImg", function(e) {
	var src = $(this).attr("src");
	$(".bigImage").attr("src",src);
});

//미리보기 이미지 호버시,
/* $(document).ready(function() {
	var original = $(".bigImg").attr("src");
	$(".smallImg").hover(function() {
		var src = $(this).attr("src");
		$(".bigImage").attr("src",src);
	}, function() {
		var src = $(this).attr("src");
		$(".bigImage").attr("src",original);
	});
}); */

//찜버튼 클릭시
function dibsClicked(){
	//찜 DB에 올라가기
	
	//버튼 이미지 변경
	if($("#dibsBtnImg").attr("src")=="/image/stopheart-icon.gif"){
		$("#dibsBtnImg").attr("src","/image/movingheart-icon.gif");
	} else{
		$("#dibsBtnImg").attr("src","/image/stopheart-icon.gif");
	}
}

//삭제 버튼 alert
$("#deleteBtn").click(function() {
	var idx =  $(this).val();
	var currentPage= $("input[name='current-page']").val();
	
	var n = confirm("정말 게시물을 삭제하시겠습니까?");
	if(n){
		location.href="delete?idx="+idx+"&currentPage="+currentPage;
	} else{
		return;			
	}
	
});

//댓글 글자수 제한
$(document).ready(function() {
	$(".re-textinput").keyup(function() {
		var inputlength=$(this).val().length;
		var remain=+inputlength;
		$(".text-plus").html(remain);
		if(remain>=90){
			$(".text-plus").css('color','red');
		}else{
			$(".text-plus").css('color','black');
		}
	});
	
	$(".re-textinput").keyup(function() {
		var inputlength=$(this).val().length;
		var remain=+inputlength;
		$(".text-plus").html(remain);
		if(remain>=101){
			alert("100자를 초과했습니다.")
		}else{
			return;
		}
	});
});

//댓글

</script>
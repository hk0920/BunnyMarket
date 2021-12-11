<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/css/community_style.css">
<div class="write-form commu-div update">
	<div class="inner">
		<form action="update" method="post" enctype="multipart/form-data">
		<input type="hidden" name="idx" value="${dto.idx}">
		<input type="hidden" name="currentPage" value="${currentPage}"> 
			<div class="group">
				<div class="child tit">
					이미지<span class="must">*</span>
				</div>
				<div class="child img-upload">
					<div class="form-group upload-img">
						<label for="chooseFile"> 
						<img src="/image/imageadd.jpg">
						</label> 
						<input type="file" class="img-input" name="photo-upload"" id="chooseFile" multiple="multiple"/>
					</div>
					<div class="preview-area"></div>
				</div>
			</div>
			<div class="group">
				<div class="child tit">
					제목<span class="must">*</span>
				</div>
				<div class="child">
					<input type="text" name="title" class="sub-input"
						required="required" value="${dto.title}" maxlength="30">
				</div>
			</div>
			<div class="group">
				<div class="child tit">
					내용<span class="must">*</span>
				</div>
				<div class="child">
					<textarea class="text-input" name="content" 
					required="required" maxlength="500">${dto.content}</textarea>
				</div>
			</div>
			<div class="btn-wrap">
				<button type="button" class="btn-add event-update-btn">수정</button>
				<button type="button" class="btn-list"
					onclick="location.href='/community/detail?idx='+${dto.idx}+'&currentPage='+${currentPage}+'&key=list'">취소</button>
			</div>
		</form>
	</div>
</div>


<script type="text/javascript" src="/js/community_script.js"></script>
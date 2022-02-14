$(function(){	
	var fileListArr = new Array();
	var keyNum = 0;
	var fileCnt = 0;
	
	$("#chooseFile").on("change", function(e){
		var files = $(this)[0].files;
		var fileArr = new Array();
		fileArr = fileListArr;
		
		for(var i=0; i<files.length; i++){
			fileCnt++;
			if(!files[i].type.match("image.*")){
				alert("확장자는 이미지 확장자만 가능합니다.");
				return false;
			}
		}
		
		if(fileCnt > 4){
			alert("파일은 최대 4개까지만 가능합니다.");
			fileCnt = fileCnt - files.length;
			return false;
		}

		for(var i=0; i<files.length; i++){
			var reader = new FileReader();
			reader.onload = function(e){
				var tag = "<div class='preview-img' idx='"+ keyNum +"'><img src='" + e.target.result +"' alt='''><span class='close'></span></div>";
				$(".preview-area").append(tag);
				keyNum++;
			}
			reader.readAsDataURL(files[i]);
			
			reader.onloadend = function(e){
				$(".preview-img").each(function(){
					$(this).height($(this).width());
					if($(this).width() > $(this).find("img").height()){
						$(this).find("img").width("auto");
						$(this).find("img").height("100%");
					}else{
						$(this).find("img").width("100%");
						$(this).find("img").height("auto");
					}
				});
			}
			fileArr.push(files[i]);
		}
				
		fileListArr = new Array();
		fileListArr = fileArr;
		console.log(fileListArr.length);
	});
	
	var deleteFile = "";
	$(document).on("click", ".ad-div .preview-img .close", function(e){
		var delIdx = $(this).parent(".preview-img").attr("idx");
		
		if($(this).parents(".ad-div").hasClass("update")){
			var fileTxt = $(e.target).prev("img").attr("src");
			deleteFile += $(e.target).prev("img").attr("src").substring(7,fileTxt.length)  + ",";
		}
		
		var fileArr = fileListArr;
		fileArr.splice(delIdx,1);
		fileCnt--;
		
		$(this).parent(".preview-img").remove();
	});
	
	$(".ad-div .ad-add-btn").click(function(){
		var title = $(".ad-div.write-form input[name='title']").val();
		var content = $(".ad-div.write-form textarea[name='content']").val();
		var formData = new FormData();
		if(title == '' || content == '' || fileCnt == 0){
			alert("필수 입력을 입력해주세요.");
			return;
		}
		formData.append("title", title);
		formData.append("content", content);
		
		for(var i=0; i<fileListArr.length; i++){
			console.log(fileListArr[i]);
			formData.append("photoupload", fileListArr[i]);
		}
		
		$.ajax({
			type:"POST",
			enctype: "multipart/form-data",
			url:"/advertise/auth/insert",
			data:formData,
			processData:false,
			contentType:false,
			success:function(){
				console.log("성공");
				location.href = "/advertise/list";
			},
			error:function(e){
				console.log("에러" + e);
			}
		});
	});
	
	//글 수정
	$(".ad-div .ad-update-btn").click(function(){
		var idx = $(".ad-div.write-form.update input[name='idx']").val();
		var title = $(".ad-div.write-form input[name='title']").val();
		var content = $(".ad-div.write-form textarea[name='content']").val();
		var photo = $(".ad-div input[name='photo']").val().split(",");
		var deletePhoto = deleteFile.split(",");
		
		var formData = new FormData();
		if(title == '' || content == ''){
			alert("필수 입력을 입력해주세요.");
			return;
		}
		formData.append("idx", idx);
		formData.append("title", title);
		formData.append("content", content);
		formData.append("deleteFile",deleteFile);
		
		var updatePhoto = "";
		var updateOrigin = "";
		for(var i=0; i<deletePhoto.length; i++){
			for(var j=0; j<photo.length; j++){
				if(deletePhoto[i]==photo[j]){
					photo.splice(j,1);
					console.log(deletePhoto[i] +"삭제" + photo[j])
				}
			}
		}
		
		for(var i=0; i<photo.length; i++){
			updatePhoto += photo[i] + ",";
			var photoSplit = photo[i].split("_")[1];
			updateOrigin += photoSplit + ","; 
		}
		
		formData.append("updatePhoto", updatePhoto.substring(0,updatePhoto.length));
		formData.append("updateOrigin", updateOrigin.substring(0,updatePhoto.length));
		
		for(var i=0; i<fileListArr.length; i++){
			console.log(fileListArr[i]);
			formData.append("uploadFile", fileListArr[i]);
		}
		
		$.ajax({
			type:"POST",
			enctype: "multipart/form-data",
			url:"/advertise/auth/update",
			data:formData,
			processData:false,
			contentType:false,
			success:function(){
				console.log("성공");
				location.href="/advertise/list";
			},
			error:function(e){
				console.log("에러" + e);
			}
		});
	});
	
	//글 삭제
	$(".detailbtn .btn-delete").click(function() {
		var idx =  $(this).attr("idx");
		var currentPage= $(".ad-div input[name='current-page']").val();
		
		var n = confirm("정말 게시물을 삭제하시겠습니까?");
		if(n){
			location.href="./auth/delete?idx="+idx+"&currentPage="+currentPage;
		} else{
			return;			
		}
	});
	
	$(".ad-div .reply .re-div .btn-add").click(function(){
		var num = $(this).parents(".reply").find("input[name='num']").val();
		var content = $(this).parents(".re-div").find("textarea[name='re-content']").val();
		var regroup = $(this).parents(".reply").find("input[name='regroup']").val();
		var checkStep ="no";
		console.log("num : "+num, "content : "+content, "regroup : "+regroup)
		
		$.ajax({
			type:"post",
			url:"auth/reply/insert",
			data:{
				"num":num,
				"content":content,
				"regroup":regroup,
				"checkStep":checkStep
			},
			success:function(){
				$(this).parents(".re-div").find("textarea[name='re-content']").val("");
				location.reload();
			}
		});
	});
	
	$(".ad-div .re-list .reply-btn").click(function(){
		if(!$(this).hasClass("active")){
			$(".re-list .reply-btn").removeClass("active");
			$(".re-list .re-div").hide();
			$(this).addClass("active");
			$(this).parents("li").find(".re-div").show();
		}else{
			$(this).removeClass("active");
			$(this).parents("li").find(".re-div").hide();
		}
	});
	
	$(".ad-div .re-list li.bg").each(function(){
		var level = $(this).find("input[name='relevel']").val();
		if($(window).width() > 768) {
			$(this).css("padding-left",(level*50) + "px");		
		}else{
			$(this).css("padding-left",(level*25) + "px");
		}
	})
	
	//대댓글
	$(".ad-div .re-list .re-div").find(".btn-add").click(function(){
		var regroup = $(this).parents("li").find("input[name='regroup']").val();
		var restep = $(this).parents("li").find("input[name='restep']").val();
		var relevel = $(this).parents("li").find("input[name='relevel']").val();
		var num = $(".ad-div").find(".reply input[name='num']").val();
		var content = $(this).parents("li").find(".re-div textarea[name='re-content']").val();
		var checkStep = "yes"; 
		console.log(num);
		$.ajax({
			type:"post",
			url:"auth/reply/insert",
			data:{
				"num":num,
				"content":content,
				"regroup":regroup,
				"restep":restep,
				"relevel":relevel,
				"checkStep":checkStep
			},
			success:function(){
				$(this).parents(".re-div").find("textarea[name='re-content']").val("");
				location.reload();
			}
		});
	});
	
	//댓글 글자수 제한
	$(".ad-div .re-content textarea").keyup(function() {
		var inputlength=$(this).val().length;
		var remain=+inputlength;
		$(".text-plus").html(remain);
		if(remain>=90){
			$(".event-div .text-plus").css('color','red');
		}else{
			$(".event-div .text-plus").css('color','black');
		}
		
		if(remain>=101){
			alert("100자를 초과했습니다.");
			$(this).val($(this).val().substring(0, 100));
            $(".event-div .text-plus").html("100");
		}
	});
	
	//댓글 삭제
	$(".ad-div .re-list .btn-delete").click(function() {
		var idx=$(this).attr("idx");
		console.log(idx);
		if(confirm("댓글을 삭제하시겠습니까?")){
			$.ajax({
				type:"get",
				dataType:"html",
				url:"auth/reply/delete",
				data:{"idx":idx},
				success:function(){
					alert("댓글을 삭제했습니다.");
					location.reload();
				}
			});
		}
	});
	
	//이미지 상세 보기
	$(".img-detail-view").click(function() {
		$(this).parents(".img-detail-div").siblings().find(".content-img").hide();
		$(this).parents(".img-detail-div").find(".content-img").toggle();
	});
})
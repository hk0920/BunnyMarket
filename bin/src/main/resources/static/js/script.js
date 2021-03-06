$(function(){
	$("header .menu-btn").click(function(){
		if(!$(this).hasClass("on")){
			$(this).addClass("on");
			$("body").css("overflow","hidden");
			$("header .menu").addClass("gnb-on");
			gsap.to(".gnb", 0.6, {opacity:1, right:0, ease:Power3.easeOut});
		}else{
			$(this).removeClass("on");
			$("header .menu").removeClass("gnb-on");   
			gsap.to(".gnb", 0.6, {opacity:0, right:"-50%", ease:Power3.easeOut});
			$("body").css("overflow","auto");
		}
	});
	
	$("header .local-div .local-btn").click(function(){
		if(!$(this).hasClass("on")){
			$(this).addClass("on");
			$("header .local-option").stop(true,true).slideDown(300);
		}else{
			$(this).removeClass("on");
			$("header .local-option").stop(true,true).slideUp(300);   
		}
	}); 

	$(".sub-body .search-btn").click(function(){
		if(!$(".sub-body .search-area").hasClass("on")){
			$(".sub-body .search-area").addClass("on");
		}else{
			$(".sub-body .search-area").removeClass("on");   
		}
	});   
   
	$(window).on("load",function(){
		//상세페이지 롤링
		if($(".detail-swiper").length > 0){
			// 상세 페이지 - 큰 이미지 롤링
			$(".infoAll .bigImg").each(function(){
				$(this).height($(this).width());
				if($(this).width() > $(this).find("img").height()){
					$(this).find("img").width("auto");
					$(this).find("img").height("100%");
				}else{
					$(this).find("img").width("100%");
					$(this).find("img").height("auto");
				}
			});
			
			$(".infoAll .smImg").each(function(){
				$(this).height($(this).width());
				if($(this).width() > $(this).find("img").height()){
					$(this).find("img").width("auto");
					$(this).find("img").height("100%");
				}else{
					$(this).find("img").width("100%");
					$(this).find("img").height("auto");
				}
			});
			
			var detailSwiper = new Swiper(".detail-swiper", {
			    navigation: {
			      nextEl: ".detail-swiper .swiper-button-next",
			      prevEl: ".detail-swiper .swiper-button-prev",
			    },
			    pagination: {
			      el: ".detail-swiper .swiper-pagination",
			    },
				observer:true,
				observerParents:true,
				updateOnImageReady:true
			});
		}
		if($(".thumbnailDiv").length > 0) {
			//리스트 이미지 정사각형
			$(".thumbnailDiv").each(function(){
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
	});
});

function loadFile(event){
	imgArr=new Array();
	for(var image of event.target.files){
		var reader = new FileReader();
      
		reader.onload = function(e){
			var img="<div class='preview-img'><img src='" +  e.target.result +  "' alt='' /><span class='close'>X</span></div>";
			imgArr.push(img);
			$("div.previewarea").append($(img));
		}; 
		reader.readAsDataURL(image);
	}
	setTimeout(function(){
		$(".preview-img").each(function(idx){
			$(this).attr("idx",idx);
			if($(this).width() > $(this).find("img").height()){
				$(this).find("img").width("auto");
				$(this).find("img").height("100%");
			}else{
				$(this).find("img").width("100%");
				$(this).find("img").height("auto");
			}
		});
	},10);
   
	setTimeout(function(){
		$(".preview-img").each(function(idx){
			$(this).attr("idx",idx);
			if($(this).width() > $(this).find("img").height()){
				$(this).find("img").width("auto");
				$(this).find("img").height("100%");
			}else{
				$(this).find("img").width("100%");
				$(this).find("img").height("auto");
			}
		});
	},10);
   
	$(document).on("click", ".preview-img .close", function(){
		var selectData = $(this).parents(".preview-img");
		console.log(imgArr.length + "개");
		var idx=$(this).parents(".preview-img").attr("idx");
		$(this).parents(".preview-img").remove(); 
		imgArr.splice(idx,1);
		console.log(imgArr.length);
	});
}

function popOpen(pop){
	$(pop).fadeIn(500);
	
	$(pop).find(".modal-close").click(function(){
		$(pop).fadeOut(500);	
	});
}

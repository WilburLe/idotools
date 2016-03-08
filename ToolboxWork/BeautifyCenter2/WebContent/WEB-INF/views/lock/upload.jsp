<%@page import="com.toolbox.common.AppEnum"%>
<%@page import="com.toolbox.common.AppMarketEnum"%>
<%@page import="com.toolbox.web.entity.AppTagEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<%=basePath %>static/jquery-2.2.0.min.js"></script>
<script src="<%=basePath %>static/jquery-ui.js"></script>
<script src="<%=basePath %>static/dropzone.js"></script>

<link rel="stylesheet" href="<%=basePath %>static/dropzone.css">
<link rel="stylesheet" href="<%=basePath %>static/dialog.css?<%=System.currentTimeMillis()%>">
<script type="text/javascript">
$(function() {
    $( "#dialog" ).dialog({
      autoOpen: false,
      show: {
        effect: "blind",
        duration: 1000
      },
      hide: {
        effect: "explode",
        duration: 1000
      }
    });
 
    
    $(".market").click(function() {
    	var tags = [];
    	 $('input[class=market]').each(function() {
			if ($(this).is(':checked') ==true) {
				//alert($(this).val());
				tags.push($(this).val());
			}
    	});
    	$("#market").val(JSON.stringify(tags));
    });
 });
  
	//Dropzone的初始化，myDropzone为form的id
	Dropzone.options.myDropzone = {
		//指定上传图片的路径
		url : "<%=basePath %>upload",
		//添加上传取消和删除预览图片的链接，默认不添加
		addRemoveLinks : true,
		//关闭自动上传功能，默认会true会自动上传//也就是添加一张图片向服务器发送一次请求
		autoProcessQueue : false,
		//允许上传多个照片
		uploadMultiple : true,
		//每次上传的最多文件数，经测试默认为2，坑啊
		//记得修改web.config 限制上传文件大小的节
		parallelUploads : 100,
		init : function() {
			var submitButton = document.querySelector("#submit-all")
			myDropzone = this; // closure
			//为上传按钮添加点击事件
			submitButton.addEventListener("click", function() {
				//手动上传所有图片
				myDropzone.processQueue();
			});
			//当添加图片后的事件，上传按钮恢复可用
			this.on("addedfile", function() {
				$("#submit-all").removeAttr("disabled");
			});
			//当上传完成后的事件，接受的数据为JSON格式
			this.on("complete", function(data) {
				//console.log(" >>> "+data.xhr.responseText)
				if (this.getUploadingFiles().length === 0 && this.getQueuedFiles().length === 0) {
					var res = eval('(' + data.xhr.responseText + ')');
					var msg = "成功上传" + res.data.successNum + "个文件！\n"
						+"失败"+res.data.failNum+"个，原因：\n"
					+res.data.msg;
					$(".dz-remove").remove();	
					$("#message").text(msg);
					$("#dialog").dialog("open");
				}
			});
			//删除图片的事件，当上传的图片为空时，使上传按钮不可用状态
			this.on("removedfile", function() {
				if (this.getAcceptedFiles().length === 0) {
					$("#submit-all").attr("disabled", true);
				}
			});
		}
	};
</script>

</head>
<body>
	上架范围
	<input type="checkbox" name="market" class="market" value="<%=AppMarketEnum.China.getCode() %>"><%=AppMarketEnum.China.getName() %>
	<input type="checkbox" name="market" class="market" value="<%=AppMarketEnum.GooglePlay.getCode() %>"><%=AppMarketEnum.GooglePlay.getName() %>
	
	<form action="/" class="dropzone" enctype="multipart/form-data" id="my-dropzone" method="post">
		<input type="hidden" name="tablename" value="<%=AppEnum.lockscreen.getCollection()%>">
		<input type="hidden" name="market" id="market">
	</form>
	<div>
		<!--上传按钮，提供多张图片一次性上传的功能-->
		<button type="submit" id="submit-all" disabled="disabled">上传</button>
	</div>
	
	<div id="dialog" title="上传通知">
		<p id="message"></p>
	</div>
</body>
</html>
<%@page import="com.toolbox.entity.AppTagEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.toolbox.framework.utils.WebUtility"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
    String basePath = WebUtility.getBasePath(request);
	String tablename = (String) request.getAttribute("tablename");
	List<AppTagEntity> apptags = ( List<AppTagEntity> ) request.getAttribute("apptags");
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
<link rel="stylesheet" href="<%=basePath %>static/dialog.css">
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
 
    
    $(".tag").click(function() {
    	var tags = [];
    	 $('input[class=tag]').each(function() {
			if ($(this).is(':checked') ==true) {
				//alert($(this).val());
				tags.push($(this).val());
			}
    	});
    	$("#tags").val(JSON.stringify(tags));
    });
 });
  
	//Dropzone的初始化，myDropzone为form的id
	Dropzone.options.myDropzone = {
		//指定上传图片的路径
		url : "<%=basePath %>/upload",
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
					var msg;
					if (res.data.statu) {
						msg = "恭喜，已成功上传" + res.data.count + "张照片！";
					} else {
						msg = "上传失败，失败的原因是：" + res.data.msg;
					}
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
选择分类：
<%
for(int i=0; i<apptags.size(); i++) {
    AppTagEntity tag = apptags.get(i);%>
 	 <input type="checkbox"  class="tag" value="<%=tag.getElementId()%>">
 	 <%=tag.getName().getString("zh_CN") %>-<%=tag.getName().getString("en_US") %>
<%}%>
	
	<form action="/" class="dropzone" enctype="multipart/form-data" id="my-dropzone" method="post">
		<input type="hidden" name="tablename" value="<%=tablename%>">
		<input type="hidden" name="tags" id="tags">
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
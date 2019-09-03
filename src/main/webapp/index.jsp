<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<meta charset="UTF-8">
<body>
<h2>tomcat1</h2>
<h2>Hello World!y</h2>
<h2>Hello World!y</h2>

springmvc上传文件
<form name="form1" action="/mmall2/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springmvc文件上传"/>

</form>
富文本上传文件
<form name="form2" action="/mmall2/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本文件上传"/>

</form>

</body>
</html>

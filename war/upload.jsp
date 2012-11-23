<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
 
<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>
<!DOCTYPE html>
<html xml:lang="zh-cn" lang="zh-cn" dir="ltr" xmlns="http://www.w3.org/1999/xhtml">
    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Upload Image</title>
        <link rel="stylesheet" type="text/css" href="static/css/upload.css" media="all" />
        <script type="text/javascript" src="static/js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="static/js/upload.js"></script>
    </head>
    <body>
    <div class="sel">
    <input id="byup" accesskey="l" type="radio" name="ulway" class="ulway" value="byup"/><label for="byup">本地上传</label>
    <input id="byurl" accesskey="u" type="radio" name="ulway" class="ulway" value="byurl" /><label for="byurl">URL上传</label>
    </div>
    <div class="add byup">
        <form action="<%= blobstoreService.createUploadUrl("/addImageByUpload") %>" method="post" enctype="multipart/form-data">
            图片标题: <input type="text" name="title">
            图片描述: <input type="text" name="desc">
            点击上传: <input type="file" name="myFile">
            <input type="hidden" name="width" value="230">
            <input type="submit" value="上传图片" class="submit">
        </form>
     </div>
     <div class="add byurl">
        <form action="/addImage" method="get">
            图片标题: <input type="text" name="title">
            图片描述: <input type="text" name="desc">
            URL地址: <input type="text" name="url">
            <input type="hidden" name="width" value="230">
            <input type="submit" value="上传图片" class="submit">
        </form>
        </div>
        <script type="text/javascript">/*<![CDATA[*/$(".sel").getUpload();//]]></script>
    </body>
</html>
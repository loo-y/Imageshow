<%@ 
page contentType="text/html;charset=UTF-8" language="java" 
%><%@ 
page import="java.util.List" 
%><%@ 
page import="javax.jdo.PersistenceManager" 
%><%@ 
page import="com.google.appengine.api.users.User" 
%><%@ 
page import="com.google.appengine.api.users.UserService" 
%><%@ 
page import="com.google.appengine.api.users.UserServiceFactory" 
%><%@ 
page import="imageshow.Myimage" 
%><%@ 
page import="imageshow.PMF" 
%><%@ 
page import="javax.jdo.Query"
%><%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    int RangeFrom=0;
    if(request.getParameter("ff")!=null){
    RangeFrom=Integer.parseInt(request.getParameter("ff"));
    }
    String descSerach = request.getParameter("desc");
    
    String querystring = "select from " + Myimage.class.getName();
    if(!(descSerach==null || descSerach.trim().length() == 0)){
    	querystring += " where description.matches('"+descSerach+".*')";
    }
    Query query = pm.newQuery(querystring);
    query.setRange(RangeFrom, RangeFrom+30);
    List<Myimage> images = (List<Myimage>) query.execute();
    if (images.isEmpty()==false) {    
		%>[<%
		int ilength=images.size();
        for (int i=0;i<ilength;i++) {
        Myimage ii=images.get(i);
			%>{iname:'<%=
			ii.getTitle()
			%>',id:'<%=
			ii.getDescription()
			%>',iu:'<%=
			ii.getPath()
			%>',isu:'<%=
			ii.getSmallImagePath()
			%>',en:'<%=
			ii.getExtName()
			%>',dk:'<%=
			ii.getDownKey()
			%>',ish:'<%=
			ii.getSmallImageHeight()
			%>',it:'<%=
			ii.getDate()
			%>'}<%
			if(i != ilength-1){
			%>,<%
			}
		}
		%>]<%
	}
    pm.close();
%>
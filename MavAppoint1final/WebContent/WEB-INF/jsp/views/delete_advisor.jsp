<jsp:include page='<%=(String) request.getAttribute("includeHeader")%>' />
<%@ page import="java.util.ArrayList"%>
<%@ page import="uta.mav.appoint.login.LoginUser"%>

<%

//String name = (String)request.getAttribute("name");
ArrayList<LoginUser> users = (ArrayList<LoginUser>)request.getAttribute("users"); 
%>
<style>
.resize {
width: 60%;
}
.resize-body {
width: 80%;
}
tbody tr{
	background:white;
}

</style>
<div class="container">
	<div class="btn-group">
		<form action="appointments" method="post" name="cancel">
			<input type=hidden name=cancel_button id="cancel_button"> <input
				type=hidden name=edit_button id="edit_button">
			<div class="row col-md-16  custyle">
				<table class="table table-striped custab">
					<thead>
						<tr style="color:white">
							<th>Advisor Name</th>
							<th>Advising Email</th>
							<th>Password</th> 
							<th class="text-center">Action</th>
						</tr>
					</thead>
					<!-- begin processing appointments  -->
					<% 
		    			if (users != null){%>
					<%for (int i=0;i<users.size();i++){ %>
					<tr>
						<td><%=users.get(i).getPname()%></td>
						<td><%=users.get(i).getEmail()%></td>
						<td>************</td>
						
						<td class="text-center"><button type="button" class="deletebtn" userid="<%=users.get(i).getUserId()%>">Delete</button></td>
						</tr>
				
					<%	}
		    			}
		    			%>
					<!-- end processing advisors -->
				</table>
		</form>
	</div>
</div>
<%@include file="templates/footer.jsp"%>
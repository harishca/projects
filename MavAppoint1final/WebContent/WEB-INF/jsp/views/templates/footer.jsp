<%@ page import ="uta.mav.appoint.login.LoginUser"%>
<script type="text/javascript" src="js/allInPage.js"></script>
<footer>
</footer>
<%
LoginUser usr = (LoginUser) session.getAttribute("user");
%>
<div id="myModal" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">User preference</h4>
      </div>
      <div class="modal-body">
       <%
      	if(usr!=null){
      		if(usr.getNotificationEmail()!=null && usr.getNotificationEmail().equalsIgnoreCase("true")){
      			%>
      	        <p><input type="checkbox" name="emailpref" value="true" checked="checked">&nbsp;&nbsp;&nbsp;&nbsp;Enable email<br></p>
      	      <%
      		}
          	else{
          		%>
          	 <p><input type="checkbox" name="emailpref" value="false">&nbsp;&nbsp;&nbsp;&nbsp;Enable email<br></p>
          		<%
          	}      			
      	}
      	
      	
      %>
  	<h4 id="message" style="color:green"></h4>
      </div>
      <div class="modal-footer">
        <button id="cancelchange" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button id="savechange" type="button" class="btn btn-primary">Save</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<script>
$(function(){
	$("#showpreference").click(function(){
		$("h4#message").text("").hide();
		$('#myModal').modal('show');	
	});
	
	$("#cancelchange").click(function(){
		$("h4#message").text("").hide();
	});
	$("#savechange").click(function(){
		var ischecked = $('input[name=emailpref]').is(':checked');
		//alert(ischecked);
		
		$.post("updatenotification", {value: ischecked}, function(result){
	        if(result){
	        	//alert("Preferences successfully updated");
	        	//$('#myModal').modal('hide');
	        	if(ischecked){
	        		$("h4#message").text("Preferences successfully updated!!!. Email enabled").show();
	        	}
	        	else{
	        		$("h4#message").text("Preferences successfully updated!!! Email disabled").show();
	        	}
	        	
	        }
	    });
	});
	
});
</script>
</body>

</html>

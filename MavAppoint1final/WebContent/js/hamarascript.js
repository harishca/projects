$(function(){
	$(".deletebtn").click(function(){
		var uid = $(this).attr('userid');
		if (confirm('Are you sure?')) {
			$.post("deleteadvisor",{
		        userid:uid 
		    },function(data){
		    	alert(data);
		    	window.location.reload();
		    });
			
		}
		
		
	});
});
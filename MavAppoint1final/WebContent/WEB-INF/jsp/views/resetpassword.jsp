<%@ include file="templates/header.jsp"%>
<%  String message = (String)session.getAttribute("message"); %>
<style>
.panel-heading {
	padding: 5px 15px;
}

.panel-footer {
	padding: 1px 15px;
	color: #A0A0A0;
}

.profile-img {
	width: 96px;
	height: 96px;
	margin: 0 auto 10px;
	display: block;
	-moz-border-radius: 50%;
	-webkit-border-radius: 50%;
	border-radius: 50%;
}
</style>

<div class="container" style="margin-top: 40px">
	<label for="message"><font color="#e67e22" size="4"><%=message%></label>
	<div class="row">
		<div class="col-sm-6 col-md-4 col-md-offset-4">
			<div class="panel panel-default">
				<div class="panel-heading">
					<strong>Enter your email to reset password</strong>
				</div>
				<div class="panel-body">
					<form role="form" action="#" method="POST">
						<fieldset>
							
							<div class="row">
								<div class="col-sm-12 col-md-10  col-md-offset-1 ">
									<div class="form-group">
										<div class="input-group">
											<span class="input-group-addon"> <i
												class="glyphicon glyphicon glyphicon-envelope"></i>
											</span> <input type="text" class="form-control" name=email
												placeholder="yourname@mavs.uta.edu">
										</div>
									</div>
									
									<div class="form-group">
										<input type="submit" class="btn btn-lg btn-primary btn-block"
											value="Reset">
									</div>
								</div>
							</div>
						</fieldset>
					</form>
				</div>
				<div class="panel-footer "></div>
			</div>
		</div>
	</div>
</div>

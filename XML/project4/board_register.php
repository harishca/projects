<html>
<head>
<h2>Registration Page</h2>
</head>
<body>
<?php
session_start();
echo "<form action='board_Register.php' method='post'>
<label>Username : <input type='text' name='username'></label>
<br>
<label>Password : <input type='password' name='password'></label>
<br>
<label>Fullname : <input type='text' name='fullname'></label>
<br>
<label>Email ID : <input type='text' name='emailid'></label>
<br>
<input type='submit' value='Register'>
<input type='reset' value='Reset'>";

if(isset($_POST['username'])&&isset($_POST['password'])&&isset($_POST['fullname'])&&isset($_POST['emailid'])){
$username= $_POST['username'];
$password= $_POST['password'];
$fullname= $_POST['fullname'];
$emailid =$_POST['emailid'];
try{
//$db = new pdo('mysql:unix_socket=/cloudsql/maximal-plate-93320:cloudsql'),'hari','hari',array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
$dbh = new PDO("mysql:host=127.0.0.1:3306;dbname=board","root","",array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
$dbh->exec('insert into users values("'.$username.'","' . md5("$password") . '","'.$fullname.'","'.$emailid.'")');
header("location:board.php");
}catch(PDOException $e){
if($e->getCode()==23000){
    echo "<br>";
    echo "<br>";
    echo "User Name already exists. Please try another one";
}
die();
}
}
?>
</body>
</html>

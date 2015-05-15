<html>
<head>
<h2>Board Messages</h2>
</head>
<body>

<?php
error_reporting(E_ALL & ~E_NOTICE);
session_start();
if(isset($_POST['replyMessage'])){
$reply=$_POST['replyMessage'];
}
echo "<form action='board_main.php' method='post' >";
echo "<input type='hidden' name='replyMessage' value=".$reply.">";
echo "<textarea rows='4' cols='50' name='message' placeholder='Please enter your message here...'>";
echo "</textarea>";
echo "<br><br>";
echo "<lable><input type='submit' value='Post'></label>";
echo "</form>";
echo "<br>";
if(isset($reply)){
if(isset($_POST['message'])){
$username=$_SESSION['username'];
$id=uniqid();
$message=$_POST['message'];
echo "<br>below is follows";
echo $reply;
echo "<br>";
try {
  $dbh = new PDO("mysql:host=127.0.0.1:3306;dbname=board","root","",array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
  $dbh->beginTransaction();
  $dbh->exec('insert into posts(id,postedby,follows,datetime,message) values("'.$id.'","'.$username.'","'.$reply.'",NOW(),"'.$message.'")')
          or die(print_r($dbh->errorInfo(), true));
  echo "Inserted in if part";
  $dbh->commit();
} catch (PDOException $e) {
  print "Error!: " . $e->getMessage() . "<br/>";
  die();
}
header("location:board_messages.php");
}
}else{

if(isset($_POST['message'])){
$username=$_SESSION['username'];
$id=uniqid();
$message=$_POST['message'];

try {
  $dbh = new PDO("mysql:host=127.0.0.1:3306;dbname=board","root","",array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
  $dbh->beginTransaction();
  $dbh->exec('insert into posts(id,postedby,datetime,message) values("'.$id.'","'.$username.'",NOW(),"'.$message.'")')
          or die(print_r($dbh->errorInfo(), true));
  echo "Inserted in else part";
  $dbh->commit();
} catch (PDOException $e) {
  print "Error!: " . $e->getMessage() . "<br/>";
  die();
}
header("location:board_messages.php");
}
}
?>

</body>
</html>

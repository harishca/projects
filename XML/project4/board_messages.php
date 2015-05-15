<html>
<head>
<h2>Board Messages</h2>
</head>
<body>
<?php
session_start();
echo "<form action='board_messages.php' method='post'>";
echo "<input type='hidden' name='logout' value='logout'>";
echo "<input type='submit' value='Logout'>";
echo "</form>";
echo "<form action='board_main.php' method='get'>";
echo "<label><input type='submit' value='New Message'></label>";
echo "</form>";
echo "<div>";
try {
  $dbh = new PDO("mysql:host=127.0.0.1:3306;dbname=board","root","",array(PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
  $dbh->beginTransaction();
  $stmt = $dbh->prepare("select * from posts") or die(print_r($db->errorInfo(), true));;
  $stmt->execute();
  print "<pre>";
  while ($row = $stmt->fetch()) {
        echo "<div>";
        $id=$row['id'];
        echo "Message ID:",$id;
        echo "<br>";
        echo "Posted By:",$row['postedby'];
        echo "<br>";
        if($row['follows']){
        echo "Follows:",$row['follows'];
        echo "<br>";
        }
        echo "Date & Time:",$row['datetime'];
        echo "<br>";
        echo "Message:",$row['message'];
        echo "<form action='board_main.php' method='post'>";
        echo "<input type='hidden' name=replyMessage value=".$id.">";
        echo "<input type='submit' value='Reply'>";
        echo "</form>";
        echo "</div>";
  }
  print "</pre>";  
 
} catch (PDOException $e) {
  print "Error!: " . $e->getMessage() . "<br/>";
  die();
}
echo "</div>";
if(isset($_POST['logout'])){
  session_destroy();
  echo "session destroyed";
  header("location:board.php");
 }
?>
</body>
</html>

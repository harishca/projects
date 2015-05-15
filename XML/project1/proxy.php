<?php
  // put your TMDb API key here:
  $api_key = "7991962ef09715df799931ae03bec180";

  header("Content-type: application/json\n\n");
  $method = $_GET['method'];
  $params = $_SERVER['QUERY_STRING'];
  $pos = strpos($params,'&');
  if ($pos === false) {
    $host = "https://api.themoviedb.org$method?api_key=$api_key";
  } else {
    $params = substr($params,$pos);
    $host = "https://api.themoviedb.org$method?api_key=$api_key$params";
  };
  $ch = curl_init($host);
  curl_exec($ch);
  curl_close($ch);
?>

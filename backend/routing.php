<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "storytelling";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
  die("Connection failed: " . $conn->connect_error);
}

function getList(){
  global $conn;
  $sql = "SELECT * FROM video ORDER BY mark DESC";
  $result = $conn->query($sql);

  return $result;
  $conn->close();
}

function update($list){
  global $conn;
  $sql = "UPDATE video SET email=". $list['email']. ", mark=". $list['mark']. ", videoName=". $list['videoName']. ", dateTimes=". $list['dateTimes']. ", comments=". $list['comments']. "WHERE videoID=". $list['videoID'];

  if ($conn->query($sql) === TRUE) {
    echo "Record updated successfully";
  } else {
    echo "Error updating record: " . $conn->error;
  }
}

function delete($id){
  global $conn;

  $sql = "DELETE FROM video WHERE id=".$id;

  if ($conn->query($sql) === TRUE) {
    echo "Record deleted successfully";
  } else {
    echo "Error deleting record: " . $conn->error;
  }
}
?>
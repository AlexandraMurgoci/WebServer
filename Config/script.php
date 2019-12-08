<?php
$username=$_GET['username'];
$password=$_GET['password'];
if ($username=="student" && $password=="123456") 
echo "Hello! Logged successfully!";
else echo "Error";

?>
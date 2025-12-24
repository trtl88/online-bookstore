<?php
// backend/logout.php
session_start();
session_destroy();
// Redirect back to the login page
header("Location: ../frontend/login.php");
exit();
?>
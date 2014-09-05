<?php
//creating connection to databases falcon_reserve_2 and bo.favista.in 

//Host Name, User Name & Password for database falcon_reserve_2
global $host1,$host2,$user1,$user2,$password1,$password2,$database1,$database2;
 $host1= "192.168.0.133";
 $user1= "root";
 $password1="falcon_dev";
 $database1="falcon_reserve_2" ;
 $con=mysqli_connect($host1,$user1,$password1,$database1);
 
///Host Name, User Name & Password for database falcon_reserve_2
 $host2 = "bo.favista.in" ;
 $user2= "poster";
 $password2=  "KKafahf@dsgad";
 $database2=  "favista";
 $con1=mysqli_connect($host2,$user2,$password2,$database2);
?>

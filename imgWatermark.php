<?php
///////////error reporting
error_reporting(E_ALL);
ini_set('display_errors','1');  
$font_path = "/var/www/test/arial.ttf"; // Font file
$font_size = 30; // in pixcels
$water_mark_text_2 = "9lessons"; // Watermark Text

function watermark_text($oldimage_name, $new_image_name)
{
echo "in funtion" ;

global $font_path, $font_size, $water_mark_text_2;
print $size1=getimagesize($oldimage_name);
list($owidth,$oheight) = getimagesize($oldimage_name);
$width = $height = 300;
$image = imagecreatetruecolor($width, $height);
$image_src = imagecreatefromjpeg($oldimage_name);
imagecopyresampled($image, $image_src, 0, 0, 0, 0, $width, $height, $owidth, $oheight);
$blue = imagecolorallocate($image, 79, 166, 185);
imagettftext($image, $font_size, 0, 68, 190, $blue, $font_path, $water_mark_text_2);
imagejpeg($image, $new_image_name, 100);
imagedestroy($image);
echo "image is water marked";
unlink($oldimage_name);
return true;
}
echo "In file imgWatermark";
//chdir('Random');
//echo getcwd();

$old="Random/index1.jpeg";
$new="Random/index1.jpeg";
echo "<br>";

//echo '<img src="Random/index.jpeg" width="350" height="250" />';
echo $img=watermark_text($old,$new);
?>

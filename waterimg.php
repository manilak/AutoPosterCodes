<?php
// Tell the server to treat this file as an image
header('Content-type: image/jpeg');
 
// Obtain the image file path
$img ="Pic (2).jpg";
 
//Set the output (watermarked) image quality
$q = '80';
 
// Create an object from the original image
$image = @imagecreatefromjpeg($img);
if (!$image) die();
 
//Get the size of the original image
$img_w = imagesx($image);
$img_h = imagesy($image);
 
// Limit watermarking to images larger than 301px wide
if ($img_w < "301") {//if image width is less then 301 pixels
    imagejpeg($image, null, $q); die();
} else {// create the watermarking
	// Create some colors
	$colour = imagecolorallocate($image, 255, 255, 0);
	$shadow = imagecolorallocate($image, 128, 0, 0);
 
	// Set watermark text
	$text = '©Phaethon.net';
 
	// Set font size
	$size = $img_w/25;
 
	// As we use a ttf file in the same directory,
	// tell GD where to look
	putenv('GDFONTPATH=' . realpath('.'));
 
	// Name the font to be used (NB no .ttf extension)
	$font = 'freestyle';
 
	// Get the coordinates of the box enclosing the text
	$box = @imageTTFBbox($size,0,$font,$text);
 
	// Calculate width of the text
	$w_w = abs($box[4] - $box[0]);
 
	// Inset watermark by width of text plus about 10%
	$dest_x = $img_w - $w_w*1.1;
 
	// Offset watermark from bottom by about 2% of image height
	$dest_y = $img_h*0.98;
 
	// Add the shadow watermark text offset by 2px lower and to right
	imagettftext($image, $size, 0, $dest_x+2, $dest_y+2, $shadow, $font, $text);
 
	// Add the watermark text
	imagettftext($image, $size, 0, $dest_x, $dest_y, $colour, $font, $text);
}
// create output (watermarked) image
imagejpeg($image, null, $q);
 
// clean-up
imagedestroy($image);
?>

<html>
<head><title>Buy Products</title></head>
<h1>Shopping Website</h1>
<body>
<?php
session_start();
error_reporting(E_ALL);
error_reporting(E_ERROR | E_PARSE);

#variable to calculate price
$price = 0;

if(isset($_REQUEST['delete'])){
	$dpid = $_REQUEST['delete'];
	$_SESSION['delpid'][] = $dpid ;
	unset($_REQUEST['delete']);
	for($i=0;$i<sizeof($_SESSION['pid1']);$i++){
		if($_SESSION['pid1'][$i]==$dpid){
		unset($_SESSION['pid1'][$i]);
		$_SESSION['pid1'] = array_values($_SESSION['pid1']);
		}
	}
}
if(isset($_REQUEST['buy'])){
$pid=$_REQUEST['buy'];
$_REQUEST['buy'] = null;
if(empty($_SESSION['pid1'])){
$_SESSION['pid1'][]=$pid;
        foreach($_SESSION['basket'] as $key=>$list){
        if($list['pid']==$pid){
        echo "<table border=1>";
        echo "<tr>";
        echo "<td><a href=".$list['OfferURL']."><img src=".$list['Img_URL']."></a></td>"; 
        echo "<td>".$list['name']."</td>";
        echo "<td>".$list['Min Price']."</td>";
		$price1=abs($list['Min Price']);
		echo "<td><a href='buy.php?delete=".$pid."'>Delete</a>";
        echo "</tr>";
        echo "</table>";
        }
		$price = $price1;
}        
}else{

if(!in_array($pid,$_SESSION['pid1'])){
if(!in_array($pid, $_SESSION['delpid'])){$_SESSION['pid1'][]=$pid;
		 echo "<table border=1>";
        foreach($_SESSION['pid1'] as $idd){
        foreach($_SESSION['basket'] as $key=>$list1){
        if($list1['pid']== $idd){
        echo "<tr>";
        echo "<td><a href=".$list1['OfferURL']."><img src=".$list1['Img_URL']."></a></td>"; 
        echo "<td>".$list1['name']."</td>";
        echo "<td>".$list1['Min Price']."</td>";
		$price1=abs($list1['Min Price']);
		echo "<td><a href='buy.php?delete=".$idd."'>Delete</a>";
        echo "</tr>";
		 $price+= $price1;
       } 
	  
}
}
echo "</table>";}
else{
		echo "<table border=1>";
        foreach($_SESSION['pid1'] as $idd){
        foreach($_SESSION['basket'] as $key=>$list1){
        if($list1['pid']== $idd){
        echo "<tr>";
        echo "<td><a href=".$list1['OfferURL']."><img src=".$list1['Img_URL']."></a></td>"; 
        echo "<td>".$list1['name']."</td>";
        echo "<td>".$list1['Min Price']."</td>";
		$price1=abs($list1['Min Price']);
		echo "<td><a href='buy.php?delete=".$idd."'>Delete</a>";
        echo "</tr>";
		$price+=$price1;
       } 
	   
}
}
}
}else{
echo "<table border=1>";
	foreach($_SESSION['pid1'] as $idd){
        foreach($_SESSION['basket'] as $key=>$list){
        if($list['pid']==$idd){
        
        echo "<tr>";
        echo "<td><a href=".$list['OfferURL']."><img src=".$list['Img_URL']."></a></td>"; 
        echo "<td>".$list['name']."</td>";
        echo "<td>".$list['Min Price']."</td>";
		$price1=abs($list['Min Price']);
		echo "<td><a href='buy.php?delete=".$pid."'>Delete</a>";
        echo "</tr>";
       $price+=$price1;
       } 
	   
}
}
 echo "</table>";
}
}
}
elseif(isset($_GET['clear'])){
unset($_SESSION['pid1']);
unset($_SESSION['basket']);
unset($_SESSION['delpid']);
$price = 0;
}
elseif(isset($_SESSION['pid1'])){
	echo "<table border=1>";
	foreach($_SESSION['pid1'] as $idd){
        foreach($_SESSION['basket'] as $key=>$list){
        if($list['pid']==$idd){
        echo "<tr>";
        echo "<td><a href=".$list['OfferURL']."><img src=".$list['Img_URL']."></a></td>"; 
        echo "<td>".$list['name']."</td>";
        echo "<td>".$list['Min Price']."</td>";
		$price1=abs($list['Min Price']);
		echo "<td><a href='buy.php?delete=".$idd."'>Delete</a>";
        echo "</tr>";
        $price+=$price1;
       } 	   
}
}
echo "</table>";
}

echo "Total Price:".$price."$";

echo"<form action='buy.php' method='get'>
<input type='hidden' name='clear' value='1'></input>
<input type='submit' value='Empty Basket'></input>
</form>";

ini_set('display_errors','On');
$xmlstr = file_get_contents('http://sandbox.api.ebaycommercenetwork.com/publisher/3.0/rest/CategoryTree?apiKey=78b0db8a-0ee1-4939-a2f9-d3cd95ec0fcc&visitorUserAgent&visitorIPAddress&trackingId=7000610&categoryId=72&showAllDescendants=true');
$xml = new SimpleXMLElement($xmlstr);
#header('Content-type: text/xml');
#echo "$xmlstr";

echo "<form action='buy.php' method='get'>";
echo "<fieldset>";
echo "<legend>Find Products:</legend>";
echo "<label>Category : ";
echo "<select name='category'>";
echo "<option value='72'>Computers</option>";
foreach($xml->category->categories->category as $category1){
        
        echo "<optgroup label='$category1->name'>";
        echo "<option value=".$category1->attributes()->id.">".$category1->name."</option>";
        foreach($category1->categories->category as $category2){
                echo "<option value=".$category2->attributes()->id.">".$category2->name."</option>";
}        
echo "</optgroup>";

}
echo "</select>";
echo "</label>";
echo "<label>Search Keywords:";
echo "<input type='text' name='search'>";
echo "<input type='submit' value='search'>";
echo "</label>";
echo "</fieldset>";
echo "</form>";
if(isset($_GET['category'])){
if($_GET['category']=='NULL'){
        echo "Please select a category";
}
else{
$products = file_get_contents('http://sandbox.api.ebaycommercenetwork.com/publisher/3.0/rest/GeneralSearch?apiKey=78b0db8a-0ee1-4939-a2f9-d3cd95ec0fcc&trackingId=7000610&categoryId='.$_GET['category'].'&keyword='.$_GET['search'].'&numItems=20');
$product = new SimpleXMLElement($products);
echo "<table border=1>";
foreach($product->categories->category->items->product as $item){
        $name=(string)$item->name;
        $product_id=(string)$item->attributes()->id;
        $minPrice=(string)$item->minPrice;
        $offerURLS=(string)$item->productOffersURL;
        $img_url=(string)$item->images->image->sourceURL;

		if (isset($_SESSION['basket']))
		{	if(!in_array($product_id,$_SESSION['basketpid'])){
			// http://php.net/manual/en/function.array-push.php
				array_push($_SESSION['basketpid'], $product_id);
					$_SESSION['basket'][]=array(
					'name'=>$name,
					'pid'=>$product_id,
					'Min Price'=>$minPrice,
					'OfferURL'=>$offerURLS,
					'Img_URL'=>$img_url
					);
				}
		}else{
			echo $name;
			$_SESSION['basket'][]=array(                            
			'name'=>$name,
			'pid'=>$product_id,
			'Min Price'=>$minPrice,
			'OfferURL'=>$offerURLS,
			'Img_URL'=>$img_url
			);
			
			$_SESSION['basketpid']=array($product_id);
		}
        echo "<tr>";
        echo "<td><a href='buy.php?buy=".$item->attributes()->id."'><img src=".$item->images->image->sourceURL."></a>";
        echo "</td>";
        echo "<td>".$item->name."</td>";
        echo "<td>".$item->minPrice."</td>";
        echo "<td>".$item->fullDescription."</td>";        
        echo "</tr>"; 
}
}
}
echo "</table>";
?>
</body>
</html>

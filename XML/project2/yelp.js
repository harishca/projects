// Declared global variables as they will be used throughout the program.
var map;
var markers = [];

// The below function is used to intialize the map with given default settings.

function initialize () {
var mycenter = new google.maps.LatLng(32.75, -97.13);
var mapProp = {
          center:mycenter,
          zoom:16,
          mapTypeId:google.maps.MapTypeId.ROADMAP
          };
map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
}

// The below function is used to send requests to the yelp and recieve results and display them.

function sendRequest () {
    var xhr = new XMLHttpRequest();
    var query = encodeURI(document.getElementById("search").value);

// Bounds variable is used to capture the latitude and longitude positions of the displayed map.

    var bounds = map.getBounds();
    var ne_lat = bounds.getNorthEast().lat();
    var ne_lon = bounds.getNorthEast().lng();
    var sw_lat = bounds.getSouthWest().lat();
    var sw_lon = bounds.getSouthWest().lng();
    xhr.open("GET", "proxy.php?term="+query+"&bounds="+sw_lat+","+sw_lon+"|"+ne_lat+","+ne_lon+"&limit=10");
    xhr.setRequestHeader("Accept","application/json");
    xhr.onreadystatechange = function () {
        if (this.readyState == 4)
        {
            var json = JSON.parse(this.responseText);
            var str = JSON.stringify(json,undefined,2);
            var mycenter = new google.maps.LatLng(32.75, -97.13);
            var i;
            var len=json.businesses.length;

// Ressting the markers in the map.

            if(markers.length>0){
                for(var m=0;m<markers.length;m++)
                {
                    markers[m].setMap(null);
                }
            }
            if(json.businesses.length>0)
            {
                
// Placing markers in the map.

                for(var j=0;j<json.businesses.length;j++)
                { 
                    var pos = new google.maps.LatLng(json.businesses[j].location.coordinate.latitude,json.businesses[j].location.coordinate.longitude);
                    var marker=new google.maps.Marker({
                    position:pos,
                    map: map
                    });
                    markers.push(marker);
                }

// Displaying the output onto the map.

                var out='<div class="border"><h3>Search Results</h3>'; 
                for(var k=0;k<json.businesses.length;k++)
                {
                    out+='<div class="inner">';
                    img_src=json.businesses[k].image_url;
                    out+='<div><img src=\''+img_src+'\'></div><div><a href='+json.businesses[k].url+'>'+json.businesses[k].name+'</a></div><div>'+json.businesses[k].snippet_text+'</div><div><img src=\''+json.businesses[k].rating_img_url+'\'></div>';
                    out+='</div>'
                }
                out+='</div>';
                document.getElementById("sample").innerHTML=out; 
            }
            else
            {
                document.getElementById("sample").innerHTML="There are no places found in this area.";
            } 
        }
    };
xhr.send(null);
}

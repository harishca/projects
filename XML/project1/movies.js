function initialize () {
}

function sendRequest () {
    var xhr = new XMLHttpRequest();
    var query = encodeURI(document.getElementById("form-input").value);
    xhr.open("GET", "proxy.php?method=/3/search/movie&query=" + query);
    xhr.setRequestHeader("Accept","application/json");
    xhr.onreadystatechange = function () {
    if (this.readyState == 4) {
    var json = JSON.parse(this.responseText);
    var str = JSON.stringify(json,undefined,2);
    var out='<table border="1">';
    var i;

//The above code is reading values from json object


//The below code creates a table for the json data and populates value into it.
    out+='<tr><th>Movies Title</th><th>Release Date</th></tr>';        
    for(i=0;i<json.results.length;i++){
        out += '<tr><td><a href="#" onClick="return loadDetails('+json.results[i].id+')">' + json.results[i].title + '</a></td><td>'+json.results[i].release_date.substring(0,4)+'</td></tr>';
    }
    out+='</table>'

        
//Diplays table onto the webpage.
    document.getElementById("filmList").innerHTML =out;
   }
   };
   xhr.send(null);
}

//The below function is used to get the details of the movie which has been selected.

function loadDetails(id) {
    var xhr = new XMLHttpRequest();
    var query = encodeURI(id);
    xhr.open("GET", "proxy.php?method=/3/movie/" + query);
    console.log(id);
    xhr.setRequestHeader("Accept","application/json");
    xhr.onreadystatechange = function () {
    if (this.readyState == 4) {
        var json = JSON.parse(this.responseText);
        var str = JSON.stringify(json,undefined,2);
        var out='<table border="1">';
        var img_src ="http://image.tmdb.org/t/p/w300"; 
        var fileName = json.poster_path;
        var img_Url = img_src.concat(fileName);
        var out='<table border="1">'; 
        var i;

//The below code creates a table for the json data and populates value into it.

        out+='<tr><th>Poster</th><th>Title</th><th>Summary</th><th>Genre</th></tr>';
        out += '<tr><td><img src=\''+img_Url+'\' width="300" height="500"></td><td><p class="title">' +json.title+ '</p></td><td><p class="summary"> '+json.overview+ '</p></td>';
        out+='<td>'
        for(i=0;i<json.genres.length;i++)
        {
            out+=''+json.genres[i].name+',';
        }
        out+='</td></tr></table>'
        document.getElementById("filmInfo").innerHTML =out;
    }
//The below function call is made to get the cast of the movie.
    loadCast(id);
    };
    xhr.send(null);
}

//The below code gets the data of cast for the movie selected and displays it.
function loadCast(id){
    var xhr = new XMLHttpRequest();
    var query = encodeURI(id);
    xhr.open("GET", "proxy.php?method=/3/movie/" + query+"/credits");
    xhr.setRequestHeader("Accept","application/json");
    xhr.onreadystatechange = function () {
    if (this.readyState == 4) {
        var json = JSON.parse(this.responseText);
        var str = JSON.stringify(json,undefined,2);
        var out='<table border="1">';
        var i;
        out+='<tr><th>Cast</th></tr>';
        for(i=0;i<5;i++){
            out+='<tr><td>'+json.cast[i].name+'</td></tr>';
        }
        out+='</table>'
        document.getElementById("castInfo").innerHTML=out;
    }
};
xhr.send(null);
}

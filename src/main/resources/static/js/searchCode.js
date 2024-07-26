const search=()=>{
    console.log("seaching");
    let query = $("#search-input").val();
    console.log(query);

    if(query == ""){
        $(".search-result").hide();  //it will hide the search box popup info if we have nothing in input
    }else {

        let url =   `http://localhost:8080/search/${query}`;

        fetch(url).then((response)=>{
            return response.json(); //all the response first come here and it will convert into JSON and return
        }).then((data)=>{
             //we get the return data from above function
            console.log(data);

            let text = `<div class="list-group">`

            data.forEach((book) =>{
                text+= `<a href="/book/view/${book.id}" class="list-group-item list-group-action">${book.name}</a>`
            });



            text+= `</div>`;

            $(".search-result").html(text);
            $(".search-result").show();

        })

        console.log(query);
        $(".search-result").show();   //it will show the search box popup info if we have nothing in input
    }
}
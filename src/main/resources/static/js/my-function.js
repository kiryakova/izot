/*function getCategories(){alert("aaa");
    fetch('/categories/fetch')
        .then((response) => response.json())
        .then((json) => {
            json.forEach((category) => $('#product-category').append(`<option value="${category.id}">${category.name}</option>`));
        })
        .catch((err) => console.log(err));
}

function getCategoriesAndSelect(){
    fetch('/categories/fetch')
        .then((response) => response.json())
        .then((json) => {
            let categories = [[${product.categories}]];
            console.log(categories);
            json.forEach((category) =>
                $('#product-category').append(
                    categories.includes(category.name)
                        ? `<option value="${category.id}" selected>${category.name}</option>`
                        : `<option value="${category.id}">${category.name}</option>`));
        })
        .catch((err) => console.log(err));
}
*/
/*const http = ( function() {
    const send = (url, method, body) =>
        fetch(url, {method, body})
            .then(response => response.json());

    const post = (url, body) => send(url, 'POST', body);
    const get = (url) => send(url, 'GET', null);

    return{
        send,
        post,
        get
    };

}());
*/
/*
function viewUser(id) {
    $.ajax({
        url: "/loginn/" + id,
        success: function(response) {
            $('#viewUserHolder').html(response);
            $('#viewUser').modal();
            $('#viewUser').modal('open');
        }
    });
}*/
/*function viewUser() {
    $.ajax({
        url: "/loginn",
        success: function(response) {
            $('#viewUserHolder').html(response);
            $('#viewUser').modal();
            $('#viewUser').modal('open');
        }
    });
}*/
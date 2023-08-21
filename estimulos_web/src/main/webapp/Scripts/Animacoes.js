$(document).ready(function () {
    $("#start").click(function () {
        $("#estimulo").animate({marginTop: 50, marginLeft: 300}, 1000);
        $("#estimulo").animate({marginTop: 300, marginLeft: 50}, 1000);
        $("#estimulo").animate({marginTop: 50, marginLeft: 150}, 1000);
        $("#estimulo").animate({marginTop: 150, marginLeft: 490}, 1000);
    });
    $("#stop").click(function () {
        $("#estimulo").clearQueue();
    });
});


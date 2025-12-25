// assets/js/main.js
document.addEventListener('DOMContentLoaded', function() {
    console.log("Bookstore Frontend Loaded");

    // Fake "Add to Cart" interaction
    const buttons = document.querySelectorAll('.btn-submit');
    
    buttons.forEach(btn => {
        btn.addEventListener('click', function(e) {
            // Only alert if it's an 'Add to Cart' button (not a form submit)
            if(this.innerText === "Add to Cart") {
                alert("Item added to cart! (Frontend Demo)");
            }
        });
    });
});
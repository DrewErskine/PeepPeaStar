$(document).ready(function() {
    $('.disc-highlight').hover(
        function() { // Mouse over function
            var parentWidth = $(this).parent().width(); // Get the width of the parent container
            var elementWidth = $(this).width(); // Get the width of the element
            var paddingAmount = (parentWidth - elementWidth) / 2; // Calculate padding needed to center

            $(this).animate({
                'padding-left': paddingAmount + 'px' // Apply the calculated padding
            }, 3000);
        },
        function() { // Mouse out function
            $(this).animate({
                'padding-left': '0px' // Reset padding when mouse leaves
            }, 3000);
        }
    );
});

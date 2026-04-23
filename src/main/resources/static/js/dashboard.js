// This function is called when user clicks any Delete button
// It receives the task title so the message is specific
function confirmDelete(taskTitle) {
    // window.confirm() shows a browser dialog with OK / Cancel
    // returns true if user clicks OK, false if Cancel
    return window.confirm("Are you sure you want to delete \"" + taskTitle + "\"?");
}

// Auto-hide success messages after 3 seconds
// This runs as soon as the page loads
document.addEventListener("DOMContentLoaded", function() {

    // find the success message element
    const successMsg = document.querySelector("[th\\:if]");
    const msg = document.getElementById("successMessage");

    if (msg) {
        // setTimeout(function, milliseconds)
        // runs the function after 3000ms = 3 seconds
        setTimeout(function() {
            // gradually fade out
            msg.style.transition = "opacity 1s";
            msg.style.opacity = "0";

            // after fade completes, fully hide it
            setTimeout(function() {
                msg.style.display = "none";
            }, 1000);
        }, 3000);
    }
});
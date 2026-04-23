// This whole file runs after the page loads

// document.getElementById("taskForm") finds the form element in HTML by its id
const form = document.getElementById("taskForm");

// .addEventListener("submit", function) means:
// "when this form is submitted, run this function first"
form.addEventListener("submit", function(event) {

    // assume no errors at start
    let isValid = true;

    // --- Validate Title ---
    const title = document.getElementById("title").value.trim();
    // .value gets what the user typed
    // .trim() removes spaces from start and end
    const titleError = document.getElementById("titleError");

    if (title === "") {
        titleError.textContent = "Title cannot be empty";
        isValid = false;
    } else if (title.length < 3) {
        titleError.textContent = "Title must be at least 3 characters";
        isValid = false;
    } else {
        titleError.textContent = ""; // clear error if valid
    }

    // --- Validate Due Date ---
    const dueDate = document.getElementById("dueDate").value;
    const dueDateError = document.getElementById("dueDateError");

    if (dueDate !== "") {
        // Date() creates a date object from a string
        const selectedDate = new Date(dueDate);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // reset time to midnight for fair comparison

        if (selectedDate < today) {
            dueDateError.textContent = "Due date cannot be in the past";
            isValid = false;
        } else {
            dueDateError.textContent = "";
        }
    }

    // if any validation failed, stop the form from submitting
    // event.preventDefault() cancels the form submission
    if (!isValid) {
        event.preventDefault();
    }
});
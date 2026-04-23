const form = document.getElementById("taskForm");

// Check if we are in edit mode
// document.title is the <title> tag content
// We set it to "Edit Task" in edit mode in our HTML
const isEditMode = document.title.includes("Edit Task");

form.addEventListener("submit", function(event) {

    let isValid = true;

    // --- Validate Title ---
    const title = document.getElementById("title").value.trim();
    const titleError = document.getElementById("titleError");

    if (title === "") {
        titleError.textContent = "Title cannot be empty";
        isValid = false;
    } else if (title.length < 3) {
        titleError.textContent = "Title must be at least 3 characters";
        isValid = false;
    } else {
        titleError.textContent = "";
    }

    // --- Validate Due Date (only for new tasks) ---
    // In edit mode, we allow keeping an existing past due date
    if (!isEditMode) {
        const dueDate = document.getElementById("dueDate").value;
        const dueDateError = document.getElementById("dueDateError");

        if (dueDate !== "") {
            const selectedDate = new Date(dueDate);
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            if (selectedDate < today) {
                dueDateError.textContent = "Due date cannot be in the past";
                isValid = false;
            } else {
                dueDateError.textContent = "";
            }
        }
    }

    if (!isValid) {
        event.preventDefault();
    }
});
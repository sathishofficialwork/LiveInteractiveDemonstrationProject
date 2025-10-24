// Get references to DOM elements
        const startButton = document.getElementById("startButton");
        const timerDisplay = document.getElementById("timerDisplay");
        const newButton = document.getElementById("result-check-btn");
		const reloadButton = document.getElementById("reload-btn");
		
        // Function to format seconds as mm:ss
        function formatTime(seconds) {
            const minutes = Math.floor(seconds / 60);
            const secs = seconds % 60;
            return minutes.toString().padStart(2, '0') + ':' + secs.toString().padStart(2, '0');
        }

        // Add click event listener to the start button
        startButton.addEventListener("click", function () {
			
			startButton.classList.add("active-run-btn");
			startButton.innerText="⨉";
            // Disable the start button to avoid multiple triggers
            startButton.disabled = true;

            // Set the 2-minute (120 seconds) total
            let totalTime = 120;

            // Display the initial countdown
            timerDisplay.textContent = "Time remaining: " + formatTime(totalTime);

            // Start the countdown using setInterval
            const intervalID = setInterval(() => {
                totalTime--;  // Decrement the timer by 1 second
                timerDisplay.textContent = "Time remaining: " + formatTime(totalTime);

                // When timer reaches 0, clear interval and enable the new button
                if (totalTime <= 0) {
                    clearInterval(intervalID);
                    timerDisplay.textContent = "Time's up!, Check the Result";
                    newButton.disabled = false;
                }
            }, 1000); // 1000ms = 1 second delay
        });
		
//		newButton.addEventListener("click", function(){
	//		reloadButton.disabled = false;
	//	});
	
	
		function triggerGithubWorkflow() {
			fetch('/api/startWorkflow')
			                .then(response => response.text())
			                .catch(error => console.error('Error:', error));
		}
		
		function reloadPage() {
			fetch('/api/updateLatestLog')
						                .then(response => response.text())
						                .catch(error => console.error('Error:', error));
		      location.reload(); // This reloads the current page
		    }

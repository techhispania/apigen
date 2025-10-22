document.addEventListener("DOMContentLoaded", function() {
	const form = document.getElementById("generateForm");

	form.addEventListener("submit", async function(e) {
		e.preventDefault();

		const data = {
			language: document.getElementById("language").value,
			database: document.getElementById("database").value
		};

		const response = await fetch("/api/generate", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(data)
		});

		const result = await response.json();
		console.log(result);

		if (result.redirectUrl) {
			window.location.href = result.redirectUrl;
		}
	});
});
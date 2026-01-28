console.log("Content script loaded");

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    console.log("Received message:", message);
    if (message.action === "getSelectedText") {
        const selectedText = window.getSelection().toString().trim();
        console.log("Selected text:", selectedText);
        sendResponse({text: selectedText});
    }
    return true;
});
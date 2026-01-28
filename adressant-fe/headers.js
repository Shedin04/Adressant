let currentLanguage = 'en';
const translations = {
    en: {
        emailAnalysis: "Email Analysis",
        threatDetection: "Threat Detection",
        showHeaders: "Show Headers",
        backToAdressant: "Back to Adressant",
        headerTitle: "Click the button to analyze the headers of the email you received.",
        headersHelp: "Don't know where to find it?",
        headerLink: "Check our guides",
        headersFound: "Headers information was successfully found:",
        expandView: "Expand View",
        collapseView: "Collapse View",
        orText: "or",
        addressDetailsTitle: "Address Details",
        transferDetailsTitle: "Transfer Details",
        messageDetailsTitle: "Message Details",
        contentDetailsTitle: "Content Details",
        authenticationDetailsTitle: "Authentication Details",
        xHeadersTitle: "X-Headers",
        ipWhoisTitle: "IP Whois Details",
        numberOfLinks: "Number of Links",
        unsortedHeadersTitle: "Unsorted Headers",
        totalMessageDelay: "Total message delay",
        errorInvalidService: "We're sorry, but this feature is only available on Gmail and Outlook.",
        errorNotFoundHeaders: "We're sorry, but the email headers cannot be found or are incorrect. Check our guides how to find them.",
        errorServiceDown: "We're sorry, but our service is down now.",
        errorDefault: "We're sorry, an error occurred."
    },
    uk: {
        emailAnalysis: "Аналіз електронної пошти",
        threatDetection: "Виявлення загроз",
        showHeaders: "Показати заголовки",
        backToAdressant: "Назад до Adressant",
        headerTitle: "Натисніть кнопку для аналізу заголовків отриманого листа.",
        headersHelp: "Не знаєте, як знайти?",
        headerLink: "Перегляньте наші інструкції",
        headersFound: "Інформацію про заголовки успішно знайдено:",
        expandView: "Розгорнути",
        collapseView: "Згорнути",
        orText: "або",
        addressDetailsTitle: "Деталі адреси",
        transferDetailsTitle: "Деталі передачі",
        messageDetailsTitle: "Деталі повідомлення",
        contentDetailsTitle: "Деталі вмісту",
        authenticationDetailsTitle: "Деталі автентифікації",
        xHeadersTitle: "X-Заголовки",
        ipWhoisTitle: "Деталі IP Whois",
        numberOfLinks: "Кількість посилань",
        unsortedHeadersTitle: "Несортовані заголовки",
        totalMessageDelay: "Загальна затримка повідомлення",
        errorInvalidService: "Вибачте, але ця функція доступна лише для Gmail та Outlook.",
        errorNotFoundHeaders: "Вибачте, але заголовки електронної пошти не вдалося знайти або вони неправильні. Перегляньте наші інструкції, як їх знайти.",
        errorServiceDown: "Вибачте, але наш сервіс зараз недоступний.",
        errorDefault: "Вибачте, сталася помилка."
    }
};

function updateLanguage(lang) {
    currentLanguage = lang;
    const translation = translations[lang];

    document.querySelector('[data-tab="email-analysis"]').innerHTML = `<i class="fas fa-envelope"></i> ${translation.emailAnalysis}`;
    document.querySelector('[data-tab="malicious-analysis"]').innerHTML = `<i class="fas fa-virus-slash"></i> ${translation.threatDetection}`;
    document.getElementById('showHeadersButton').innerHTML = `<i class="fas fa-list"></i> ${translation.showHeaders}`;
    document.getElementById('backButton').innerHTML = `<i class="fas fa-arrow-left"></i> ${translation.backToAdressant}`;
    document.getElementById('headerTitle').textContent = translation.headerTitle;
    document.getElementById('headersHelp').innerHTML = `<p><i class="fas fa-question-circle"></i> ${translation.headersHelp} <a href="https://web-adressant.azurewebsites.net/guides" target="_blank"> ${translation.headerLink}</a></p>`;
    document.getElementById('headersFoundButton').textContent = translation.headersFound;
    document.getElementById('orText').textContent = translation.orText;

    const popup = document.querySelector('.popup');
    const toggleExpandButton = document.getElementById('toggleExpandButton');
    toggleExpandButton.innerHTML = `<i class="fas fa-${popup.classList.contains('expanded') ? 'compress' : 'expand'}"></i> ${popup.classList.contains('expanded') ? translation.collapseView : translation.expandView}`;

    const languageButton = document.getElementById('languageSwitchButton');
    languageButton.innerHTML = `<i class="fas fa-globe"></i> ${lang === 'en' ? 'UA' : 'EN'}`;

    chrome.storage.sync.set({selectedLanguage: lang}, () => {
        console.log(`Language saved: ${lang}`);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.sync.get('selectedLanguage', (result) => {
        const savedLanguage = result.selectedLanguage || 'en';
        currentLanguage = savedLanguage;
        updateLanguage(savedLanguage);
    });

    document.getElementById('languageSwitchButton').addEventListener('click', () => {
        const newLanguage = currentLanguage === 'en' ? 'uk' : 'en';
        updateLanguage(newLanguage);
    });

    const tabs = document.querySelectorAll('.tab-button');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            const tabId = tab.dataset.tab;
            chrome.storage.local.set({pendingTabSwitch: tabId}, () => {
                window.location.href = "popup.html";
            });
        });
    });

    const backButton = document.getElementById("backButton");
    backButton.addEventListener("click", () => {
        window.location.href = "popup.html";
    });

    const toggleExpandButton = document.getElementById('toggleExpandButton');
    const popup = document.querySelector('.popup');
    const headersResult = document.getElementById('headersResult');
    const loader = document.getElementById('loader');
    const headersFoundButton = document.getElementById('headersFoundButton');

    toggleExpandButton.addEventListener('click', () => {
        headersResult.style.display = 'none';
        loader.style.display = 'block';
        toggleExpandButton.style.display = 'none';
        headersFoundButton.style.display = 'none';

        const translation = translations[currentLanguage];
        if (popup.classList.contains('expanded')) {
            popup.classList.remove('expanded');
            toggleExpandButton.innerHTML = `<i class="fas fa-expand"></i> ${translation.expandView}`;
        } else {
            popup.classList.add('expanded');
            toggleExpandButton.innerHTML = `<i class="fas fa-compress"></i> ${translation.collapseView}`;
        }
    });

    popup.addEventListener('transitionend', (event) => {
        if (event.propertyName === 'width' || event.propertyName === 'height') {
            headersResult.style.display = 'block';
            loader.style.display = 'none';
            headersFoundButton.style.display = 'block';
            toggleExpandButton.style.display = 'block';
        }
    });

    function getGmailHeaders() {
        const headers = document.evaluate("(//pre[@id='raw_message_text']/text())", document, null, XPathResult.STRING_TYPE, null).stringValue;
        return headers ? headers : null;
    }

    function getOutlookHeaders() {
        const headers = document.evaluate("//div[contains(@id,'Modal')]//div[contains(@class,'allowTextSelection')]", document, null, XPathResult.STRING_TYPE, null).stringValue;
        return headers ? headers : null;
    }

    const showHeadersButton = document.getElementById('showHeadersButton');
    showHeadersButton.addEventListener('click', function () {
        showHeadersButton.disabled = true;
        chrome.tabs.query({active: true, currentWindow: true}, function (tabs) {
            const activeTab = tabs[0];
            const headersRegex = /^((Delivered-To:|Received: from|From:|MIME-Version:|Date:)[\s\S]*)$/;
            switch (true) {
                case activeTab.url.includes("https://mail.google.com/"):
                    chrome.scripting.executeScript({
                        target: {tabId: activeTab.id},
                        function: getGmailHeaders
                    }, (result) => {
                        const headersData = result[0].result;
                        if (headersRegex.test(headersData)) {
                            sendHeaders("gmail", headersData);
                        } else {
                            const linkInHelp = document.querySelector('#headersHelp a');
                            linkInHelp.setAttribute('href', 'https://web-adressant.azurewebsites.net/guides?section=gmail-original-guide');
                            displayError('not-found-headers');
                        }
                    });
                    break;
                case activeTab.title.includes("Outlook"):
                    chrome.scripting.executeScript({
                        target: {tabId: activeTab.id},
                        function: getOutlookHeaders
                    }, (result) => {
                        const headersData = result[0].result;
                        if (headersRegex.test(headersData)) {
                            sendHeaders("outlook", headersData);
                        } else {
                            const linkInHelp = document.querySelector('#headersHelp a');
                            linkInHelp.setAttribute('href', 'https://web-adressant.azurewebsites.net/guides?section=outlook-original-guide');
                            displayError('not-found-headers');
                        }
                    });
                    break;
                default:
                    displayError('invalid-service');
                    break;
            }
        });
    });

    function sendHeaders(service, headersData) {
        const loader = document.getElementById('loader');
        const headerTitle = document.getElementById('headerTitle');
        const orText = document.getElementById('orText');
        const headersHelp = document.getElementById('headersHelp');
        const actionsContainer = document.getElementById('headersActionsContainer');
        const tabContainer = document.getElementById(`tab-container`);
        const languageButton = document.getElementById('languageSwitchButton');
        languageButton.style.display = 'none';
        actionsContainer.style.display = 'none';
        tabContainer.style.display = 'none';
        loader.style.display = 'block';
        backButton.style.display = 'none';
        orText.style.display = 'none';
        headersHelp.style.display = 'none';
        chrome.storage.sync.get('uuid', function (result) {
            const uuid = result.uuid;
            fetch('https://adressant.azurewebsites.net/api/v1/sender/process-headers', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Request-ID': uuid
                },
                body: JSON.stringify({service, headersData})
            })
                .then(response => {
                    loader.style.display = 'none';
                    headerTitle.style.display = 'none';
                    return checkResponse(response);
                })
                .then(displayDetails)
                .catch(() => {
                    loader.style.display = 'none';
                    displayError('service-down');
                });
        });

        function displayDetails(data) {
            const resultElement = document.getElementById(`headersResult`);
            resultElement.style.display = 'block';
            const detailsMap = {
                'addressDetails': displayAddressDetails,
                'transferDetails': displayTransferDetails,
                'authenticationDetails': displayAuthenticationDetails,
                'messageDetails': displayMessageDetails,
                'contentDetails': displayContentDetails,
                'xHeaders': displayXHeaders,
                'links': displayLinks,
                'ipWhois': displayIPWhois,
                'unsortedHeaders': displayUnsortedHeaders
            };
            Object.entries(data)
                .forEach(([key, value]) => detailsMap[key]?.(value));
        }
    }

    function checkResponse(response) {
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        return response.json();
    }

    function createLinkToWebsite(target, elementToAppend, color) {
        const linkElement = document.createElement('a');
        linkElement.href = target;
        linkElement.style.color = color;
        linkElement.style.fontWeight = 'bold';
        linkElement.textContent = '*';
        linkElement.target = '_blank';
        linkElement.style.textDecoration = 'none';
        elementToAppend.appendChild(linkElement);
    }

    function displayAddressDetails(headers) {
        const translation = translations[currentLanguage];
        const headersFoundButton = document.getElementById('headersFoundButton');
        const toggleExpandButton = document.getElementById('toggleExpandButton');
        headersFoundButton.style.display = 'block';
        toggleExpandButton.style.display = 'block';
        const addressDetailsDiv = document.getElementById('addressDetails');
        addressDetailsDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.addressDetailsTitle;
        addressDetailsDiv.appendChild(title);
        const table = document.createElement('table');
        table.classList.add('table');
        const tips = document.createElement('div');
        tips.classList.add('tips');
        addressDetailsDiv.appendChild(tips);
        for (const [key, value] of Object.entries(headers)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            switch (key) {
                case 'mailFrom':
                    if (headers.sender && !isTwoValuesContains(value, headers.sender)) {
                        applyCellStyle(keyCell, valueCell, '#fc6a03', 'normal');
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-sender-headers-guide', keyCell, '#fc6a03');
                    }
                    if (value !== headers.returnPath && headers.returnPath) {
                        const isIncludes = isTwoValuesContains(value, headers.returnPath);
                        const color = isIncludes ? '#fc6a03' : 'purple';
                        const fontWeight = isIncludes ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-return-path-headers-guide', keyCell, color);
                        if (!isIncludes) {
                            break;
                        }
                    }
                    if (value !== headers.replyTo && headers.replyTo) {
                        const isIncludes = isTwoValuesContains(value, headers.replyTo);
                        const color = isIncludes ? '#fc6a03' : 'purple';
                        const fontWeight = isIncludes ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-reply-to-headers-guide', keyCell, color);
                        if (!isIncludes) {
                            break;
                        }
                    }
                    break;
                case 'mailTo':
                    if (value !== headers.deliveredTo && headers.deliveredTo) {
                        const isIncludes = isTwoValuesContains(value, headers.deliveredTo);
                        const color = isIncludes ? '#fc6a03' : 'purple';
                        const fontWeight = isIncludes ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=mail-to-delivered-to-headers-guide', keyCell, '#fc6a03');
                    }
                    break;
                case 'sender':
                    if (headers.mailFrom && !isTwoValuesContains(value, headers.mailFrom)) {
                        applyCellStyle(keyCell, valueCell, '#fc6a03', 'normal');
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-sender-headers-guide', keyCell, '#fc6a03');
                    }
                    break;
                case 'replyTo':
                    if (headers.mailFrom && headers.mailFrom !== value) {
                        const isIncludes = isTwoValuesContains(value, headers.mailFrom);
                        const color = isIncludes ? '#fc6a03' : 'purple';
                        const fontWeight = isIncludes ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-reply-to-headers-guide', keyCell, color);
                    }
                    break;
                case 'deliveredTo':
                    if (value !== headers.mailTo && headers.mailTo) {
                        const isIncludes = isTwoValuesContains(value, headers.mailTo);
                        const color = (value === headers.blindCarbonCopy || isIncludes) ? '#fc6a03' : 'purple';
                        const fontWeight = (value === headers.blindCarbonCopy || isIncludes) ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=mail-to-delivered-to-headers-guide', keyCell, color);
                    }
                    break;
                case 'returnPath':
                    if (value !== headers.mailFrom && headers.mailFrom) {
                        const isIncludes = isTwoValuesContains(value, headers.mailFrom);
                        const color = isIncludes ? '#fc6a03' : 'purple';
                        const fontWeight = isIncludes ? 'normal' : 'bold';
                        applyCellStyle(keyCell, valueCell, color, fontWeight);
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=from-return-path-headers-guide', keyCell, color);
                    }
                    break;
                case 'carbonCopy':
                case 'resentCarbonCopy':
                    applyCellStyle(keyCell, valueCell, '#fc6a03');
                    createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=cc-headers-guide', keyCell, '#fc6a03');
                    break;
                case 'blindCarbonCopy':
                case 'resentBlindCarbonCopy':
                    applyCellStyle(keyCell, valueCell, 'purple', 'bold');
                    createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=bcc-headers-guide', keyCell, 'purple');
                    break;
            }
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        addressDetailsDiv.appendChild(table);
    }

    function displayTransferDetails(transferDetails) {
        const translation = translations[currentLanguage];
        const transferPoints = transferDetails.transferPoints;
        const transferDiv = document.getElementById('transferDetails');
        transferDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.transferDetailsTitle;
        transferDiv.appendChild(title);
        const table = document.createElement('table');
        table.classList.add('table');
        const headerRow = document.createElement('tr');
        const dateTimeHeader = document.createElement('th');
        dateTimeHeader.innerText = 'DateTime';
        dateTimeHeader.style.paddingRight = '15px';
        headerRow.appendChild(dateTimeHeader);
        const dateTimeToUTCHeader = document.createElement('th');
        dateTimeToUTCHeader.innerText = 'DateTime (UTC)';
        dateTimeToUTCHeader.style.paddingRight = '15px';
        headerRow.appendChild(dateTimeToUTCHeader);
        const delayHeader = document.createElement('th');
        delayHeader.innerText = 'Delay';
        headerRow.appendChild(delayHeader);
        const detailsHeader = document.createElement('th');
        detailsHeader.innerText = 'Details';
        headerRow.appendChild(detailsHeader);
        table.appendChild(headerRow);
        transferPoints.forEach((point) => {
            const row = document.createElement('tr');
            const dateTimeCell = document.createElement('td');
            dateTimeCell.innerText = point.dateTime;
            row.appendChild(dateTimeCell);
            const dateTimeToUTCCell = document.createElement('td');
            dateTimeToUTCCell.innerText = point.dateTimeToUTC;
            row.appendChild(dateTimeToUTCCell);
            const delayCell = document.createElement('td');
            delayCell.innerText = point.delay;
            row.appendChild(delayCell);
            const detailsCell = document.createElement('td');
            detailsCell.innerText = point.details;
            if (point.details.includes('[SKIPPED]')) {
                dateTimeCell.style.color = '#fc6a03';
                dateTimeToUTCCell.style.color = '#fc6a03';
                delayCell.style.color = '#fc6a03';
                detailsCell.style.color = '#fc6a03';
                detailsCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=skipped-point-guide', detailsCell, '#fc6a03');
            }
            row.appendChild(detailsCell);
            table.appendChild(row);
        });
        transferDiv.appendChild(table);
        if (transferDetails.totalMessageDelay !== "No delay") {
            const totalDelay = document.createElement('h3');
            let totalDelayColor;
            if (isDelayMoreThanHour(transferDetails.totalMessageDelay)) {
                totalDelayColor = 'purple';
            } else {
                totalDelayColor = '#fc6a03';
            }
            totalDelay.innerText = `${translation.totalMessageDelay}: ${transferDetails.totalMessageDelay}`;
            totalDelay.style.color = totalDelayColor;
            createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=total-delay-guide', totalDelay, totalDelayColor);
            transferDiv.appendChild(totalDelay);
        }
    }

    function isDelayMoreThanHour(delay) {
        const hourRegex = /\d+h/;
        const matches = delay.match(hourRegex);
        if (matches && matches.length > 0) {
            const hours = parseInt(matches[0]);
            return hours > 1;
        }
        return false;
    }

    function displayMessageDetails(msgDetails) {
        const translation = translations[currentLanguage];
        const messageDetails = document.getElementById('messageDetails');
        messageDetails.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.messageDetailsTitle;
        messageDetails.appendChild(title);
        const table = document.createElement('table');
        table.classList.add('table');
        for (const [key, value] of Object.entries(msgDetails)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            switch (key) {
                case 'messageId': {
                    if ((value !== msgDetails.inReplyTo && msgDetails.inReplyTo) || (value !== msgDetails.references && msgDetails.references)) {
                        applyCellStyle(keyCell, valueCell, '#fc6a03', 'normal');
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=message-ids-headers-guide', keyCell, '#fc6a03');
                    }
                    break;
                }
                case 'inReplyTo': {
                    if ((value !== msgDetails.messageId && msgDetails.messageId) || (value !== msgDetails.references && msgDetails.references)) {
                        applyCellStyle(keyCell, valueCell, '#fc6a03', 'normal');
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=message-ids-headers-guide', keyCell, '#fc6a03');
                    }
                    break;
                }
                case 'references': {
                    if ((value !== msgDetails.messageId && msgDetails.messageId) || (value !== msgDetails.inReplyTo && msgDetails.inReplyTo)) {
                        applyCellStyle(keyCell, valueCell, '#fc6a03', 'normal');
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=message-ids-headers-guide', keyCell, '#fc6a03');
                    }
                    break;
                }
            }
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        messageDetails.appendChild(table);
    }

    function displayContentDetails(contentDetails) {
        const translation = translations[currentLanguage];
        const contentDetailsDiv = document.getElementById('contentDetails');
        contentDetailsDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.contentDetailsTitle;
        contentDetailsDiv.appendChild(title);
        const table = document.createElement('table');
        table.classList.add('table');
        for (const [key, value] of Object.entries(contentDetails)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        contentDetailsDiv.appendChild(table);
    }

    function displayAuthenticationDetails(authenticationDetails) {
        const translation = translations[currentLanguage];
        const authenticationDetailsDiv = document.getElementById('authenticationDetails');
        authenticationDetailsDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.authenticationDetailsTitle;
        authenticationDetailsDiv.appendChild(title);
        const table = document.createElement('table');
        table.classList.add('table');
        for (const [key, value] of Object.entries(authenticationDetails)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            if (key === "receivedSpf" && !/^pass/i.test(value)) {
                keyCell.style.color = 'purple';
                valueCell.style.color = 'purple';
                valueCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=authentication-results-guide', keyCell, 'purple');
            }
            if ((key === "authenticationResults" || key === "arcAuthenticationResults") && [...value.matchAll(/(spf|dkim|dmarc)=(\S*)/g)].some(match => !match[2].includes("pass"))) {
                keyCell.style.color = 'purple';
                valueCell.style.color = 'purple';
                valueCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=authentication-results-guide', keyCell, 'purple');
            }
            if ((key === "authenticationResults" || key === "arcAuthenticationResults") && !(/(spf|dkim|dmarc)/.test(value))) {
                keyCell.style.color = 'purple';
                valueCell.style.color = 'purple';
                valueCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=authentication-results-guide', keyCell, 'purple');
            }
            if ((key === "arcSeal" || key === "arcMessageSignature" || key === "dkimSignature") && !((/rsa-sha256/.test(value)) || (/rsa-sha512/.test(value)))) {
                keyCell.style.color = '#fc6a03';
                valueCell.style.color = '#fc6a03';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=weak-algorithm-guide', keyCell, '#fc6a03');
            }
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        authenticationDetailsDiv.appendChild(table);
    }

    function displayXHeaders(XHeaders) {
        const translation = translations[currentLanguage];
        const XHeadersDiv = document.getElementById('XHeaders');
        XHeadersDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.xHeadersTitle;
        XHeadersDiv.appendChild(title);
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=x-headers-guide', title, 'black');
        const table = document.createElement('table');
        const headerRow = document.createElement('tr');
        const keyHeader = document.createElement('th');
        keyHeader.innerText = 'Header';
        headerRow.appendChild(keyHeader);
        const valueHeader = document.createElement('th');
        valueHeader.innerText = 'Value';
        headerRow.appendChild(valueHeader);
        table.appendChild(headerRow);
        table.classList.add('table');
        for (const [key, value] of Object.entries(XHeaders)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            if (key === "X-Mailer" && (value.startsWith("WPMailSMTP") || value.startsWith("PHPMailer") || value.startsWith("BotnetMailer") || value.startsWith("AnonymousSender"))) {
                keyCell.style.color = '#fc6a03';
                valueCell.style.color = '#fc6a03';
                valueCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=x-mailer-guide', keyCell, '#fc6a03');
            }
            if ((key === "X-Google-DKIM-Signature") && !/rsa-sha256/.test(value)) {
                keyCell.style.color = '#fc6a03';
                valueCell.style.color = '#fc6a03';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=weak-algorithm-guide', keyCell, '#fc6a03');
            }
            if ((key === "X-SID-Result") && !/PASS/i.test(value)) {
                keyCell.style.color = 'purple';
                valueCell.style.color = 'purple';
                valueCell.style.fontWeight = 'bold';
                createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=x-sid-result-guide', keyCell, 'purple');
            }
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        XHeadersDiv.appendChild(table);
    }

    function displayIPWhois(ipWhois) {
        const translation = translations[currentLanguage];
        const ipWhoisDiv = document.getElementById('ipWhois');
        ipWhoisDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.ipWhoisTitle;
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=whois-guide', title, 'black');
        ipWhoisDiv.appendChild(title);

        for (const [key, values] of Object.entries(ipWhois)) {
            if (values.length === 1) {
                const p = document.createElement('p');
                p.innerText = `${key} - ${values[0]}`;
                p.style.color = 'purple';
                ipWhoisDiv.appendChild(p);
            } else {
                const details = document.createElement('details');
                const summary = document.createElement('summary');
                summary.innerText = key;
                details.appendChild(summary);
                const ul = document.createElement('ul');
                for (const value of values) {
                    const li = document.createElement('li');
                    li.innerText = value;
                    ul.appendChild(li);
                }
                details.appendChild(ul);
                ipWhoisDiv.appendChild(details);
            }
        }
    }

    function displayLinks(links) {
        const translation = translations[currentLanguage];
        const linksDiv = document.getElementById('links');
        linksDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = `${translation.numberOfLinks}: ${links.length}`;
        title.style.marginBottom = '1px';
        linksDiv.appendChild(title);
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=links-guide', title, 'black');
        const container = document.createElement('div');
        container.classList.add('links-container');
        const list = document.createElement('ol');
        links.forEach((link) => {
            const linkItem = document.createElement('li');
            const linkAnchor = document.createElement('a');
            linkAnchor.href = link;
            linkAnchor.target = '_blank';
            linkAnchor.textContent = link.length > 120 ? link.substring(0, 120) + "..." : link;
            if (linkAnchor.textContent.startsWith("http:")) {
                linkAnchor.style.color = 'purple';
            }
            linkItem.appendChild(linkAnchor);
            list.appendChild(linkItem);
        });
        container.appendChild(list);
        if (links.length > 10) {
            container.style.height = '150px';
        }
        linksDiv.appendChild(container);
    }

    function displayUnsortedHeaders(unsortedHeaders) {
        const translation = translations[currentLanguage];
        const unsortedHeadersDiv = document.getElementById('unsortedHeaders');
        unsortedHeadersDiv.innerHTML = '';
        const title = document.createElement('h2');
        title.innerText = translation.unsortedHeadersTitle;
        unsortedHeadersDiv.appendChild(title);
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=unsorted-headers-guide', title, 'black');
        const table = document.createElement('table');
        const headerRow = document.createElement('tr');
        const keyHeader = document.createElement('th');
        keyHeader.innerText = 'Header';
        headerRow.appendChild(keyHeader);
        const valueHeader = document.createElement('th');
        valueHeader.innerText = 'Value';
        headerRow.appendChild(valueHeader);
        table.appendChild(headerRow);
        table.classList.add('table');
        for (const [key, value] of Object.entries(unsortedHeaders)) {
            const row = document.createElement('tr');
            const keyCell = document.createElement('td');
            keyCell.innerText = key;
            row.appendChild(keyCell);
            const valueCell = document.createElement('td');
            valueCell.innerText = value;
            if (unsortedHeaders.Precedence) {
                switch (value.toLowerCase()) {
                    case "bulk":
                    case "junk":
                        keyCell.style.color = 'purple';
                        valueCell.style.color = 'purple';
                        valueCell.style.fontWeight = 'bold';
                        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=precedence-header-guide', keyCell, 'purple');
                        break;
                    default:
                        keyCell.style.color = '#fc6a03';
                        valueCell.style.color = '#fc6a03';
                        break;
                }
            }
            row.appendChild(valueCell);
            table.appendChild(row);
        }
        unsortedHeadersDiv.appendChild(table);
    }

    function isTwoValuesContains(value1, value2) {
        return value1.includes(value2) || value2.includes(value1);
    }

    function applyCellStyle(keyCell, valueCell, color, fontWeight) {
        keyCell.style.color = color;
        valueCell.style.color = color;
        if (fontWeight) {
            valueCell.style.fontWeight = fontWeight;
        }
    }

    function displayError(errorType) {
        const translation = translations[currentLanguage];
        const emailErrorContainer = document.getElementById('emailErrorContainer');
        emailErrorContainer.style.display = 'block';
        switch (errorType) {
            case 'invalid-service':
                emailErrorContainer.innerHTML = `<p id='error-message'>${translation.errorInvalidService}</p>`;
                break;
            case 'not-found-headers':
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorNotFoundHeaders}</p>`;
                break;
            case 'service-down':
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorServiceDown}</p>`;
                break;
            default:
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorDefault}</p>`;
                break;
        }
    }
});
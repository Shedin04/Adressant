let currentLanguage = 'en';
const translations = {
    en: {
        emailAnalysis: "Email Analysis",
        threatDetection: "Threat Detection",
        analyzeDomain: "Analyze Domain",
        viewHeaders: "View Email Headers",
        setRating: "Set Rating",
        analyzeSelectedText: "Analyze Selected Text",
        moreInfo: "More Info",
        privacyPolicy: "Privacy Policy",
        selectSecurityOption: "Select a security analysis option:",
        analyzeThreats: "Analyze selected text for potential security threats:",
        maliciousInfoButtonSmall: "(Right-click highlighted text)",
        disclaimer: "By using this extension, you agree to allow us to analyze email data and selected text.",
        domainSuccess: "Domain information was successfully found:",
        maliciousSuccess: "Text analysis was successfully completed:",
        orText: "or",
        feedbackQuestion: "Is prediction valid?",
        feedbackSent: "Thank you for your feedback!",
        detailsHeader: "Click here to view results",
        emailLabel: "Email",
        domainLabel: "Domain",
        rateLabel: "Rate",
        numberOfScansLabel: "Number of scans",
        lastScanTimeLabel: "Last scan",
        isTemporaryLabel: "Is Temporary",
        isFreeLabel: "Is Free",
        ipAddressLabel: "IP address",
        mxRecordsLabel: "MX Records",
        whoisInfoLabel: "WHOIS info",
        rateThisDomain: "Rate this domain",
        errorInvalidService: "We're sorry, but these features are only available on Gmail and Outlook.",
        errorNotFound: "We're sorry, but the email address cannot be found or is incorrect.",
        errorServiceDown: "We're sorry, but our service is down now.",
        errorTemporaryRatingBlock: "You can only update the rating once per hour.",
        errorDefault: "We're sorry, an error occurred.",
        errorMaliciousServiceDown: "We're sorry, but our threat detection service is down now.",
        errorMaliciousDefault: "We're sorry, an error occurred with the threat detection.",
        maliciousTextLabel: "Text",
        isMaliciousLabel: "Is Malicious",
        maliciousYes: "Yes",
        maliciousNo: "No",
        confidenceLabel: "Confidence",
        maliciousTypeLabel: "Malicious Type",
        maliciousTypeNA: "N/A",
        maliciousTypePhishing: "Phishing",
        maliciousTypeFakeGiveaway: "Fake Giveaway",
        maliciousTypeSpam: "Spam",
        maliciousTypeSocialMediaScam: "Social Media Scam",
        maliciousTypeOther: "Other Malicious Content",
        maliciousTypeBenign: "Benign",
        sentimentLabel: "Sentiment",
        sentimentVeryNegative: "Very Negative",
        sentimentNegative: "Negative",
        sentimentNeutral: "Neutral",
        sentimentPositive: "Positive",
        sentimentVeryPositive: "Very Positive",
        emotionalTriggersLabel: "Emotional Triggers",
        emotionalTriggersNone: "None",
        emotionalTriggerUrgency: "Urgency",
        emotionalTriggerFear: "Fear",
        emotionalTriggerGreed: "Greed",
        emotionalTriggerExclusivity: "Exclusivity",
        emotionalTriggerCuriosity: "Curiosity",
        maliciousPopupTitle: "About this analysis:",
        maliciousPopupConfidence: "Confidence: Shows the likelihood of the assessment accuracy",
        maliciousPopupMaliciousType: "Malicious Type: Indicates the category of threat if detected",
        maliciousPopupSentiment: "Sentiment: Analyzes the emotional tone of the text",
        maliciousPopupEmotionalTriggers: "Emotional Triggers: Identifies psychological manipulation attempts"
    },
    uk: {
        emailAnalysis: "Аналіз електронної пошти",
        threatDetection: "Виявлення загроз",
        analyzeDomain: "Аналізувати домен",
        viewHeaders: "Переглянути заголовки",
        setRating: "Встановити оцінку",
        analyzeSelectedText: "Аналізувати вибраний текст",
        moreInfo: "Більше інформації",
        privacyPolicy: "Політика конфіденційності",
        selectSecurityOption: "Виберіть варіант аналізу безпеки:",
        analyzeThreats: "Аналізуйте виділений текст на наявність потенційних загроз безпеці:",
        maliciousInfoButtonSmall: "(Натисніть праву кнопку миші для виділеного тексту)",
        disclaimer: "Використовуючи це розширення, ви дозволяєте нам аналізувати дані електронної пошти та виділений текст.",
        domainSuccess: "Інформацію про домен успішно знайдено:",
        maliciousSuccess: "Аналіз тексту успішно завершено:",
        orText: "або",
        feedbackQuestion: "Чи є прогноз правильним?",
        feedbackSent: "Дякуємо за Ваш фідбек!",
        detailsHeader: "Натисніть для перегляду",
        emailLabel: "Пошта",
        domainLabel: "Домен",
        rateLabel: "Рейтинг",
        numberOfScansLabel: "Кількість сканувань",
        lastScanTimeLabel: "Останнє сканування",
        isTemporaryLabel: "Чи є тимчасовим",
        isFreeLabel: "Чи є безкоштовним",
        ipAddressLabel: "IP-адреса",
        mxRecordsLabel: "MX-записи",
        whoisInfoLabel: "Інформація WHOIS",
        rateThisDomain: "Оцініть цей домен",
        errorInvalidService: "Вибачте, але ці функції доступні лише для Gmail та Outlook.",
        errorNotFound: "Вибачте, але адресу електронної пошти не вдалося знайти або вона неправильна.",
        errorServiceDown: "Вибачте, але наш сервіс зараз недоступний.",
        errorTemporaryRatingBlock: "Ви можете оновлювати рейтинг лише раз на годину.",
        errorDefault: "Вибачте, сталася помилка.",
        errorMaliciousServiceDown: "Вибачте, але наш сервіс виявлення загроз зараз недоступний.",
        errorMaliciousDefault: "Вибачте, сталася помилка з виявленням загроз.",
        maliciousTextLabel: "Текст",
        isMaliciousLabel: "Чи є зловмисним",
        maliciousYes: "Так",
        maliciousNo: "Ні",
        confidenceLabel: "Впевненість",
        maliciousTypeLabel: "Тип зловмисності",
        maliciousTypeNA: "Н/В",
        maliciousTypePhishing: "Фішинг",
        maliciousTypeFakeGiveaway: "Фальшивий розіграш",
        maliciousTypeSpam: "Спам",
        maliciousTypeSocialMediaScam: "Шахрайство в соціальних мережах",
        maliciousTypeOther: "Інший тип зловмисності",
        maliciousTypeBenign: "Немає",
        sentimentLabel: "Настрій",
        sentimentVeryNegative: "Дуже негативний",
        sentimentNegative: "Негативний",
        sentimentNeutral: "Нейтральний",
        sentimentPositive: "Позитивний",
        sentimentVeryPositive: "Дуже позитивний",
        emotionalTriggersLabel: "Емоційні тригери",
        emotionalTriggersNone: "Немає",
        emotionalTriggerUrgency: "Терміновість",
        emotionalTriggerFear: "Страх",
        emotionalTriggerGreed: "Жадібність",
        emotionalTriggerExclusivity: "Ексклюзивність",
        emotionalTriggerCuriosity: "Цікавість",
        maliciousPopupTitle: "Про цей аналіз:",
        maliciousPopupConfidence: "Впевненість: показує ймовірність точності оцінки",
        maliciousPopupMaliciousType: "Тип загрози: вказує її категорію, якщо така виявлена",
        maliciousPopupSentiment: "Настрій: аналізує емоційний тон тексту",
        maliciousPopupEmotionalTriggers: "Емоційні тригери: виявляє психологічні маніпуляції"
    }
};

function getTranslationKey(field, value) {
    const translationMap = {
        malicious_type: {
            "Phishing": "maliciousTypePhishing",
            "Fake Giveaway": "maliciousTypeFakeGiveaway",
            "Spam": "maliciousTypeSpam",
            "Social Media Scam": "maliciousTypeSocialMediaScam",
            "Other Malicious Content": "maliciousTypeOther",
            "Benign": "maliciousTypeBenign"
        },
        sentiment: {
            "very negative": "sentimentVeryNegative",
            "negative": "sentimentNegative",
            "neutral": "sentimentNeutral",
            "positive": "sentimentPositive",
            "very positive": "sentimentVeryPositive"
        }
    };

    return translationMap[field][value] || (field === "malicious_type" ? "maliciousTypeNA" : "sentimentNeutral");
}

function updateLanguage(lang) {
    currentLanguage = lang;
    const translation = translations[lang];

    document.querySelector('[data-tab="email-analysis"]').innerHTML = `<i class="fas fa-envelope"></i> ${translation.emailAnalysis}`;
    document.querySelector('[data-tab="malicious-analysis"]').innerHTML = `<i class="fas fa-virus-slash"></i> ${translation.threatDetection}`;
    document.getElementById('ajaxButton').innerHTML = `<i class="fas fa-globe"></i> ${translation.analyzeDomain}`;
    document.getElementById('viewHeadersButton').innerHTML = `<i class="fas fa-code"></i> ${translation.viewHeaders}`;
    document.getElementById('setRatingButton').innerHTML = `<i class="fas fa-save"></i> ${translation.setRating}`;
    document.getElementById('maliciousInfoButton').innerHTML = `<i class="fas fa-search"></i> ${translation.analyzeSelectedText}<br><small>${translation.maliciousInfoButtonSmall}</small>`;
    document.getElementById('maliciousInfoResultButton').innerHTML = `<i class="fas fa-info-circle"></i> ${translation.moreInfo}`;
    document.querySelector('#disclaimer-container a').textContent = translation.privacyPolicy;
    document.getElementById('header-email').textContent = translation.selectSecurityOption;
    document.getElementById('header-malicious').textContent = translation.analyzeThreats;
    document.querySelector('#disclaimer-container p').innerHTML = `<i class="fas fa-info-circle"></i> ${translation.disclaimer} <a href="policy.html">${translation.privacyPolicy}</a>`;
    document.getElementById('successButton').textContent = translation.domainSuccess;
    document.getElementById('maliciousSuccessButton').textContent = translation.maliciousSuccess;
    document.getElementById('orText-email').textContent = translation.orText;
    document.getElementById('feedbackQuestion').textContent = translation.feedbackQuestion;

    const maliciousPopup = document.getElementById('maliciousInfoPopup');
    maliciousPopup.innerHTML = `
        <p><strong>${translation.maliciousPopupTitle}</strong></p>
        <ul>
            <li>${translation.maliciousPopupConfidence}</li>
            <li>${translation.maliciousPopupMaliciousType}</li>
            <li>${translation.maliciousPopupSentiment}</li>
            <li>${translation.maliciousPopupEmotionalTriggers}</li>
        </ul>
    `;

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

    chrome.storage.local.get('pendingTabSwitch', (result) => {
        if (result.pendingTabSwitch) {
            switchToTab(result.pendingTabSwitch);
            chrome.storage.local.remove('pendingTabSwitch');
        }
    });

    const tabs = document.querySelectorAll('.tab-button');
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            switchToTab(tab.dataset.tab);
        });
    });

    function switchToTab(tabId) {
        const tabs = document.querySelectorAll('.tab-button');
        const tabContents = document.querySelectorAll('.tab-content');

        tabs.forEach(t => t.classList.remove('active'));
        tabContents.forEach(content => content.classList.remove('active'));

        const targetTab = document.querySelector(`.tab-button[data-tab="${tabId}"]`);
        const targetContent = document.getElementById(tabId);

        if (targetTab && targetContent) {
            targetTab.classList.add('active');
            targetContent.classList.add('active');
        }
    }

    chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
        if (message.action === "switchTab") {
            switchToTab(message.tabId);
            sendResponse({success: true});
            return true;
        }
    });

    const ajaxButton = document.getElementById('ajaxButton');
    const viewHeadersButton = document.getElementById('viewHeadersButton');
    const slider = document.getElementById('emailRatingSlider');
    const sliderValue = document.getElementById('sliderValue');
    const setRatingButton = document.getElementById('setRatingButton');

    ajaxButton.addEventListener('click', () => {
        chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
            const activeTab = tabs[0];
            switch (true) {
                case activeTab.url.includes("https://mail.google.com/"):
                    fetchDataAndDisplayResult(activeTab, getEmailForGmail, 'email');
                    break;
                case activeTab.title.includes("Outlook"):
                    fetchDataAndDisplayResult(activeTab, getEmailForOutlook, 'email');
                    break;
                default:
                    displayError('invalid-service', 'email');
                    ajaxButton.disabled = true;
                    viewHeadersButton.disabled = true;
                    break;
            }
        });
    });

    viewHeadersButton.addEventListener('click', () => {
        chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
            const activeTab = tabs[0];
            if (activeTab.url.includes("https://mail.google.com/") || activeTab.title.includes("Outlook")) {
                window.location.href = "headers.html";
            } else {
                ajaxButton.disabled = true;
                viewHeadersButton.disabled = true;
                displayError('invalid-service', 'email');
            }
        });
    });

    slider.addEventListener('input', () => {
        sliderValue.textContent = slider.value;
    });

    setRatingButton.addEventListener('click', () => {
        setRatingButton.disabled = true;
        slider.disabled = true;
        chrome.storage.sync.get('uuid', (data) => {
            const uuid = data.uuid;
            chrome.storage.sync.get('lastUpdate', (last) => {
                const lastUpdate = last.lastUpdate;
                const currentTime = Date.now();
                if (lastUpdate && currentTime - lastUpdate < 60 * 60 * 1000) {
                    displayError('temporary-rating-block', 'email');
                } else {
                    const domainId = document.getElementById('result-email').dataset.domainId;
                    const domainRating = slider.value;
                    fetch('https://adressant.azurewebsites.net/api/v1/sender/rating', {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-Request-ID': uuid,
                        },
                        body: JSON.stringify({domainId, domainRating}),
                    })
                        .then(response => checkResponse(response))
                        .then(data => {
                            document.querySelector("#rating>#value").innerHTML = data.domainRating;
                            saveLastUpdate();
                            formatRating();
                        })
                        .catch(() => displayError('service-down', 'email'));
                }
            });
        });
    });

    const likeButton = document.getElementById('likeButton');
    const dislikeButton = document.getElementById('dislikeButton');

    likeButton.addEventListener('click', () => {
        sendFeedback(true);
        likeButton.disabled = true;
        dislikeButton.disabled = true;
    });

    dislikeButton.addEventListener('click', () => {
        sendFeedback(false);
        likeButton.disabled = true;
        dislikeButton.disabled = true;
    });

    function sendFeedback(isValid) {
        chrome.storage.sync.get('uuid', (result) => {
            const uuid = result.uuid;
            const resultDiv = document.getElementById('result-malicious');
            const analyzedText = resultDiv.querySelector('p span').textContent;
            const actualPrediction = resultDiv.querySelector('p:nth-child(2) span').textContent === translations[currentLanguage].maliciousYes;

            document.getElementById('loader-malicious').style.display = 'block';

            fetch('http://0.0.0.0:8080/ai-feedback', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Request-ID': uuid,
                },
                body: JSON.stringify({
                    analyzedText,
                    isValid,
                    actualPrediction
                }),
            })
                .then(response => {
                    document.getElementById('loader-malicious').style.display = 'none';

                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Network response was not ok');
                    }
                })
                .then(data => {
                    if (data.status === 'success') {
                        const feedbackSection = document.getElementById('feedbackSection');

                        const confirmationDiv = document.createElement('div');
                        confirmationDiv.classList.add('feedback-confirmation');
                        confirmationDiv.innerHTML = `
                    <p><i class="fas fa-check-circle" style="color: var(--primary); margin-right: 5px;"></i> 
                    ${translations[currentLanguage].feedbackSent}</p>
                `;

                        feedbackSection.parentNode.replaceChild(confirmationDiv, feedbackSection);
                    }
                })
                .catch(() => {
                    document.getElementById('loader-malicious').style.display = 'none';
                    displayError('service-down', 'malicious');
                });
        });
    }

    chrome.storage.local.get(['analysisResult', 'analysisError'], (result) => {
        const loader = document.getElementById('loader-malicious');
        const resultDiv = document.getElementById('result-malicious');
        const button = document.getElementById('maliciousInfoButton');
        const maliciousActionsContainer = document.getElementById('maliciousActionsContainer');

        loader.style.display = 'none';

        const translation = translations[currentLanguage];
        if (result.analysisResult) {
            hidePolicy();
            const {data, text} = result.analysisResult;
            resultDiv.style.display = 'block';
            resultDiv.innerHTML = `
            <p><strong>${translation.maliciousTextLabel}:</strong> <span>${text}</span></p>
            <p><strong>${translation.isMaliciousLabel}:</strong> <span style="color: ${data.is_malicious ? 'purple' : '#3e8e41'}">${data.is_malicious ? translation.maliciousYes : translation.maliciousNo}</span></p>
            <p><strong>${translation.confidenceLabel}:</strong> <span>${(data.confidence * 100).toFixed(2)}%</span></p>
            <p><strong>${translation.maliciousTypeLabel}:</strong> <span>${translation[getTranslationKey('malicious_type', data.malicious_type)]}</span></p>
            <p><strong>${translation.sentimentLabel}:</strong> <span>${translation[getTranslationKey('sentiment', data.sentiment)]}</span></p>
            <p><strong>${translation.emotionalTriggersLabel}:</strong> <span>${formatEmotionalTriggers(data.emotional_triggers)}</span></p>
        `;
            chrome.storage.local.remove('analysisResult');
            maliciousActionsContainer.style.display = 'none';
            button.style.display = 'none';
            const languageButton = document.getElementById('languageSwitchButton');
            languageButton.style.display = 'none';
            const tabContainer = document.getElementById(`tab-container`);
            tabContainer.style.display = 'none';

            const successButton = document.getElementById('maliciousSuccessButton');
            successButton.style.display = 'block';
            const infoButton = document.getElementById('maliciousInfoResultButton');
            infoButton.style.display = 'block';

            const feedbackSection = document.getElementById('feedbackSection');
            feedbackSection.style.display = 'block';

            const popup = document.getElementById('maliciousInfoPopup');
            const popupContainer = document.querySelector('.popup');

            infoButton.addEventListener('mouseover', () => {
                const buttonRect = infoButton.getBoundingClientRect();
                const containerRect = popupContainer.getBoundingClientRect();
                const popupRect = popup.getBoundingClientRect();

                let top = buttonRect.bottom - containerRect.top + 5;

                if (top + popupRect.height > containerRect.height) {
                    top = buttonRect.top - containerRect.top - popupRect.height - 5;
                }

                top = Math.max(5, top);

                popup.style.top = `${top}px`;
            });
        } else if (result.analysisError) {
            hidePolicy();
            resultDiv.style.display = 'none';
            maliciousActionsContainer.style.display = 'none';
            button.style.display = 'none';
            displayError('service-down', 'malicious');
            chrome.storage.local.remove('analysisError');
        }
    });
});

function fetchDataAndDisplayResult(activeTab, getEmailFunction, tab) {
    const button = document.getElementById('ajaxButton');
    const loader = document.getElementById(`loader-${tab}`);
    const toHeaderAnalyzerButton = document.getElementById('viewHeadersButton');
    const orText = document.getElementById(`orText-${tab}`);
    const languageButton = document.getElementById('languageSwitchButton');

    if (button.disabled) return;
    button.disabled = true;

    toHeaderAnalyzerButton.style.display = 'none';
    orText.style.display = 'none';
    languageButton.style.display = 'none';

    chrome.scripting.executeScript(
        {target: {tabId: activeTab.id}, function: getEmailFunction},
        (result) => {
            const email = result[0].result;
            const emailRegex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,8}$/;
            if (emailRegex.test(email)) {
                const actionsContainer = document.getElementById('emailActionsContainer');
                const tabContainer = document.getElementById(`tab-container`);
                actionsContainer.style.display = 'none';
                tabContainer.style.display = 'none';
                loader.style.display = 'block';
                hidePolicy();
                chrome.storage.sync.get('uuid', (result) => {
                    const uuid = result.uuid;
                    fetch(`https://adressant.azurewebsites.net/api/v1/sender`, {
                        headers: {'X-Request-ID': uuid, 'email': email}
                    })
                        .then(response => checkResponse(response))
                        .then(data => {
                            loader.style.display = 'none';
                            displayResult(data, email, tab);
                        })
                        .catch(() => {
                            loader.style.display = 'none';
                            displayError('service-down', tab);
                        });
                });
            } else {
                loader.style.display = 'none';
                displayError('not-found', tab);
                orText.style.display = 'block';
                toHeaderAnalyzerButton.style.display = 'block';
            }
        }
    );
}

function getEmailForGmail() {
    const emailElement = document.evaluate("(//div[@class='adn ads']//span[@email])[1]", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null)
        .singleNodeValue;
    return emailElement ? emailElement.getAttribute('email') : null;
}

function getEmailForOutlook() {
    const tempEmailElement = document.evaluate("//div[@aria-label='Email message']//span[@class='OZZZK']/text()[2]", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null)
        .singleNodeValue.textContent;
    const emailElement = tempEmailElement.match(/<(.+)>/)[1];
    return emailElement ? emailElement : null;
}

function checkResponse(response) {
    if (!response.ok) throw new Error(response.statusText);
    return response.json();
}

function setupSlider(data) {
    const slider = document.getElementById('emailRatingSlider');
    const sliderValue = document.getElementById('sliderValue');
    let rate = data.rate;
    if (rate === 0) rate = 5;
    slider.value = rate;
    sliderValue.textContent = rate;
}

function displayResult(data, email, tab) {
    const resultElement = document.getElementById(`result-${tab}`);
    resultElement.style.display = 'block';
    resultElement.classList.add('result-animated');
    resultElement.dataset.domainId = data.domainId;

    const whoisInfo = data.whoisInfo;

    const translation = translations[currentLanguage];

    let whoisHtml = whoisInfo.length > 1
        ? `<details><summary>${translation.detailsHeader}</summary><ul>${whoisInfo.map(info => `<li>${info}</li>`).join('')}</ul></details>`
        : `<ul><li>${whoisInfo[0]}</li></ul>`;

    resultElement.innerHTML = `
        <p id="email"><strong>${translation.emailLabel}:</strong> ${email}</p>
        <p id="domain"><strong>${translation.domainLabel}:</strong> ${data.domain}</p>
        <p id="rating"><strong>${translation.rateLabel}:</strong> <strong id="value">${data.rate}</strong></p>
        <p id="numberOfScans"><strong>${translation.numberOfScansLabel}:</strong> ${data.numberOfScans}</p>
        <p id="lastScanTime"><strong>${translation.lastScanTimeLabel}:</strong> ${data.lastScanTime} (UTC)</p>
        <p id="isTemporary"><strong>${translation.isTemporaryLabel}:</strong> <span id="value">${data.isTemporary}</span></p>
        <p id="isFree"><strong>${translation.isFreeLabel}:</strong> <span id="value">${data.isFree}</span></p>
        <p id="ipAddress"><strong>${translation.ipAddressLabel}:</strong> <span id="value">${data.ipAddress}</span></p>
        <div id="mxRecorcdsContainer"><p id="mxRecords"><strong>${translation.mxRecordsLabel}:</strong></p><ul>${data.mxRecords.map(record => `<li>${record}</li>`).join('')}</ul></div>
        <div id="whoisInfoContainer"><p id="whoisInfo"><strong>${translation.whoisInfoLabel}:</strong></p>${whoisHtml}</div>
    `;

    const header = document.getElementById(`header-${tab}`);
    const policy = document.getElementById('disclaimer-container');
    const successButton = document.getElementById('successButton');

    header.style.display = "none";
    policy.style.display = "none";
    successButton.style.display = "block";
    const emailRatingSection = document.getElementById('emailRatingSection');
    const setRatingButton = document.getElementById('setRatingButton');
    const ratingTextElement = emailRatingSection.querySelector('p strong');
    ratingTextElement.childNodes[0].textContent = `${translation.rateThisDomain}: `;
    emailRatingSection.classList.add('email-rating-animation');
    emailRatingSection.style.display = 'block';
    setRatingButton.style.display = 'block';

    setupSlider(data);
    formatRating();
    formatDomainRecords(data);
}

function formatRating() {
    const ratingElement = document.getElementById('rating');
    const ratingValueElement = ratingElement.querySelector('#value');
    const ratingValue = parseFloat(ratingValueElement.textContent);
    if (ratingValue < 3) {
        ratingValueElement.style.color = 'purple';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=rating-guide', ratingValueElement, 'purple');
    } else if (ratingValue < 6) {
        ratingValueElement.style.color = '#fc6a03';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=rating-guide', ratingValueElement, '#fc6a03');
    } else {
        ratingValueElement.style.color = '#3e8e41';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=rating-guide', ratingValueElement, '#3e8e41');
    }
}

function formatDomainRecords(data) {
    if (data.isFree) {
        const isFreeValueElement = document.getElementById('isFree').querySelector('#value');
        isFreeValueElement.style.color = 'purple';
        isFreeValueElement.style.fontWeight = 'bold';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=is-free-guide', isFreeValueElement, 'purple');
    }
    if (data.isTemporary) {
        const isTemporaryValueElement = document.getElementById('isTemporary').querySelector('#value');
        isTemporaryValueElement.style.color = 'purple';
        isTemporaryValueElement.style.fontWeight = 'bold';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=is-temp-guide', isTemporaryValueElement, 'purple');
    }
    if (data.ipAddress === "No IP found") {
        const ipAddressValueElement = document.getElementById('ipAddress').querySelector('#value');
        ipAddressValueElement.style.color = 'purple';
        ipAddressValueElement.style.fontWeight = 'bold';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=ip-guide', ipAddressValueElement, 'purple');
    }
    if (data.mxRecords[0] === "No MX records were found!") {
        const mxRecordsValueElement = document.getElementById('mxRecorcdsContainer').getElementsByTagName('li')[0];
        mxRecordsValueElement.style.color = 'purple';
        mxRecordsValueElement.style.fontWeight = 'bold';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=mx-records-guide', mxRecordsValueElement, 'purple');
    }
    if (data.whoisInfo[0] === "No WHOIS information was found!") {
        const whoisInfoValueElement = document.getElementById('whoisInfoContainer').getElementsByTagName('li')[0];
        whoisInfoValueElement.style.color = 'purple';
        whoisInfoValueElement.style.fontWeight = 'bold';
        createLinkToWebsite('https://web-adressant.azurewebsites.net/guides?section=whois-guide', whoisInfoValueElement, 'purple');
    }
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

function saveLastUpdate() {
    chrome.storage.sync.set({lastUpdate: Date.now()});
}

function displayError(errorType, tab) {
    const translation = translations[currentLanguage];
    if (tab === 'email') {
        const emailErrorContainer = document.getElementById('emailErrorContainer');
        emailErrorContainer.style.display = 'block';
        switch (errorType) {
            case 'invalid-service':
                emailErrorContainer.innerHTML = `<p id='error-message'>${translation.errorInvalidService}</p>`;
                break;
            case 'not-found':
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorNotFound}</p>`;
                break;
            case 'service-down':
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorServiceDown}</p>`;
                break;
            case 'temporary-rating-block':
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorTemporaryRatingBlock}</p>`;
                break;
            default:
                emailErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorDefault}</p>`;
                break;
        }
    } else if (tab === 'malicious') {
        const maliciousErrorContainer = document.getElementById('maliciousErrorContainer');
        maliciousErrorContainer.style.display = 'block';
        switch (errorType) {
            case 'service-down':
                maliciousErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorMaliciousServiceDown}</p>`;
                break;
            default:
                maliciousErrorContainer.innerHTML = `<p id='error-message' class='center'>${translation.errorMaliciousDefault}</p>`;
                break;
        }
    }
}

function hidePolicy() {
    const policy = document.getElementById('disclaimer-container');
    policy.style.opacity = '0';
    setTimeout(() => {
        policy.style.display = 'none';
    }, 500);
}

function formatEmotionalTriggers(triggers) {
    const translation = translations[currentLanguage];
    return triggers && typeof triggers === 'object'
        ? Object.entries(triggers)
        .filter(([_, value]) => value)
        .map(([key]) => translation[`emotionalTrigger${key.charAt(0).toUpperCase() + key.slice(1)}`])
        .join(', ') || translation.emotionalTriggersNone
        : translation.emotionalTriggersNone;
}
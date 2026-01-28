let currentLanguage = 'en';
const translations = {
    en: {
        privacyPolicy: "Privacy Policy",
        processingConfidentialInfo: "Processing Confidential Information",
        storageOfInfo: "Storage of Information",
        securityMeasures: "Security Measures",
        contactUs: "Contact Us",
        backToAdressant: "Back to Adressant",
        introText: "We are committed to protecting your and your sender's privacy and ensuring this information is processed safely and securely. Please take a moment to read through our",
        fullPolicyLink: "full privacy policy and terms of use",
        processingText: "By clicking on any of the buttons on our extension, you are permitting us to process confidential information, including the sender's email and headers. We take this responsibility seriously and want you to know that we do not store any information about you or the sender. All the data is processed in real-time and is provided to you immediately.",
        storageText: "We do not store any information about you (except temporary technical) or your sender. It means that your and your sender's data is safe with us and cannot be accessed by any third party.",
        securityText: "We take security seriously and have implemented various measures to protect your information. Our Chrome extension is encrypted, and we use TLS v1.3 encryption to keep your data safe.",
        contactText: "If you have any questions about this",
        contactLink: "Privacy Policy",
        contactEmail: "you can contact us by email:",
    },
    uk: {
        privacyPolicy: "Політика конфіденційності",
        processingConfidentialInfo: "Обробка конфіденційної інформації",
        storageOfInfo: "Зберігання інформації",
        securityMeasures: "Заходи безпеки",
        contactUs: "Зв'яжіться з нами",
        backToAdressant: "Назад до Adressant",
        introText: "Ми прагнемо захищати вашу конфіденційність та конфіденційність вашого відправника, а також гарантувати, що ця інформація обробляється безпечно. Будь ласка, приділіть хвилинку, щоб ознайомитися з нашою",
        fullPolicyLink: "повною політикою конфіденційності та умовами використання",
        processingText: "Натискаючи на будь-яку кнопку нашого розширення, ви дозволяєте нам обробляти конфіденційну інформацію, включно з електронною поштою відправника та заголовками. Ми серйозно ставимося до цієї відповідальності та хочемо, щоб ви знали, що ми не зберігаємо жодної інформації про вас чи відправника. Усі дані обробляються в реальному часі та надаються вам негайно.",
        storageText: "Ми не зберігаємо жодної інформації про вас (крім тимчасових технічних даних) чи вашого відправника. Це означає, що ваші дані та дані вашого відправника перебувають у безпеці з нами і не можуть бути доступні стороннім особам.",
        securityText: "Ми серйозно ставимося до безпеки та впровадили різноманітні заходи для захисту вашої інформації. Наше розширення для Chrome зашифроване, і ми використовуємо шифрування TLS v1.3, щоб забезпечити безпеку ваших даних.",
        contactText: "Якщо у вас є запитання щодо цієї",
        contactLink: "Політики конфіденційності",
        contactEmail: "ви можете зв’язатися з нами електронною поштою:",
    }
};

function updateLanguage(lang) {
    currentLanguage = lang;
    const translation = translations[lang];

    document.querySelector('h1').innerHTML = `<i class="fas fa-lock"></i> ${translation.privacyPolicy}`;
    document.querySelectorAll('h2')[0].innerHTML = `<i class="fas fa-shield-alt"></i> ${translation.processingConfidentialInfo}`;
    document.querySelectorAll('h2')[1].innerHTML = `<i class="fas fa-database"></i> ${translation.storageOfInfo}`;
    document.querySelectorAll('h2')[2].innerHTML = `<i class="fas fa-user-shield"></i> ${translation.securityMeasures}`;
    document.querySelectorAll('h2')[3].innerHTML = `<i class="fas fa-envelope"></i> ${translation.contactUs}`;

    document.querySelectorAll('p')[0].innerHTML = `${translation.introText} <a href="https://web-adressant.azurewebsites.net/policy" target="_blank">${translation.fullPolicyLink}</a>.`;
    document.querySelectorAll('p')[1].innerHTML = translation.processingText;
    document.querySelectorAll('p')[2].innerHTML = translation.storageText;
    document.querySelectorAll('p')[3].innerHTML = translation.securityText;
    document.querySelectorAll('p')[4].innerHTML = `${translation.contactText} <a href="mailto:adressant@proton.me">adressant@proton.me</a>.</p>`;

    document.getElementById('backFromPolicyButton').innerHTML = `<i class="fas fa-arrow-left"></i> ${translation.backToAdressant}`;
    document.getElementById('languageSwitchButton').innerHTML = `<i class="fas fa-globe"></i> ${lang === 'en' ? 'UA' : 'EN'}`;

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

    const backFromPolicyButton = document.getElementById("backFromPolicyButton");
    backFromPolicyButton.addEventListener("click", () => {
        window.location.href = "popup.html";
    });

    document.getElementById('languageSwitchButton').addEventListener('click', () => {
        const newLanguage = currentLanguage === 'en' ? 'uk' : 'en';
        updateLanguage(newLanguage);
    });
});
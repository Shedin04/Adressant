import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import precision_recall_fscore_support, accuracy_score
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from torch.nn.functional import softmax
from training import MaliciousContentDetector, load_training_data_from_csv
import torch


def evaluate_lexical_method(texts, labels, urgency_terms, cta_terms, reward_terms, threat_terms):
    """Evaluate lexical method based on keyword counts."""
    y_pred = []
    for text in texts:
        text_lower = text.lower()
        urgency_count = sum(1 for term in urgency_terms if term in text_lower)
        cta_count = sum(1 for term in cta_terms if term in text_lower)
        reward_count = sum(1 for term in reward_terms if term in text_lower)
        threat_count = sum(1 for term in threat_terms if term in text_lower)
        # Simple rule: classify as malicious if multiple trigger types are present
        score = urgency_count + cta_count + reward_count + threat_count
        y_pred.append(1 if score >= 2 else 0)
    precision, recall, f1, _ = precision_recall_fscore_support(labels, y_pred, average='binary')
    accuracy = accuracy_score(labels, y_pred)
    return {'Precision': precision, 'Recall': recall, 'F1-Score': f1, 'Accuracy': accuracy}


def evaluate_rf_method(texts, labels):
    """Evaluate Random Forest with TF-IDF features."""
    tfidf = TfidfVectorizer(max_features=1000, ngram_range=(1, 2))
    X = tfidf.fit_transform(texts)
    X_train, X_test, y_train, y_test = train_test_split(X, labels, test_size=0.2, random_state=42)
    clf = RandomForestClassifier(n_estimators=100, random_state=42)
    clf.fit(X_train, y_train)
    y_pred = clf.predict(X_test)
    precision, recall, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='binary')
    accuracy = accuracy_score(y_test, y_pred)
    return {'Precision': precision, 'Recall': recall, 'F1-Score': f1, 'Accuracy': accuracy}


def evaluate_bert_method(texts, labels):
    """Evaluate BERT-based sentiment analysis."""
    tokenizer = AutoTokenizer.from_pretrained("nlptown/bert-base-multilingual-uncased-sentiment")
    model = AutoModelForSequenceClassification.from_pretrained("nlptown/bert-base-multilingual-uncased-sentiment")
    y_pred = []
    for text in texts:
        encoded_text = tokenizer(text, return_tensors='pt', truncation=True, max_length=512)
        with torch.no_grad():
            output = model(**encoded_text)
        scores = softmax(output.logits, dim=1).numpy()[0]
        # Classify as malicious if very negative or very positive (common in manipulative texts)
        is_malicious = scores[0] > 0.4 or scores[4] > 0.4
        y_pred.append(1 if is_malicious else 0)
    precision, recall, f1, _ = precision_recall_fscore_support(labels, y_pred, average='binary')
    accuracy = accuracy_score(labels, y_pred)
    return {'Precision': precision, 'Recall': recall, 'F1-Score': f1, 'Accuracy': accuracy}


def evaluate_hybrid_method(texts, labels):
    """Evaluate hybrid method using MaliciousContentDetector."""
    detector = MaliciousContentDetector()
    X_train, X_test, y_train, y_test = train_test_split(texts, labels, test_size=0.2, random_state=42)
    detector.train(X_train, y_train)
    y_pred = [detector.predict(text)['is_malicious'] for text in X_test]
    precision, recall, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='binary')
    accuracy = accuracy_score(y_test, y_pred)
    return {'Precision': precision, 'Recall': recall, 'F1-Score': f1, 'Accuracy': accuracy}


if __name__ == "__main__":
    # Load data
    csv_file_path = 'training_data.csv'
    texts, labels = load_training_data_from_csv(csv_file_path)

    # Define lexical terms (same as in training.py)
    urgency_terms = ['терміново', 'негайно', 'важливо', 'поспішайте', 'обмежено', 'швидко',
                     'urgent', 'immediately', 'important', 'hurry', 'limited', 'quickly']
    cta_terms = ['натисніть', 'перейдіть', 'заповніть', 'реєструйтесь', 'введіть', 'поділіться',
                 'click', 'follow', 'fill', 'register', 'enter', 'share']
    reward_terms = ['виграш', 'приз', 'бонус', 'безкоштовно', 'шанс', 'ексклюзивно',
                    'win', 'prize', 'bonus', 'free', 'chance', 'exclusive']
    threat_terms = ['заблоковано', 'втрата', 'ризик', 'проблема', 'загроза', 'небезпека',
                    'blocked', 'loss', 'risk', 'problem', 'threat', 'danger']

    # Evaluate methods
    results = [
        {'Method': 'Lexical',
         **evaluate_lexical_method(texts, labels, urgency_terms, cta_terms, reward_terms, threat_terms)},
        {'Method': 'Random Forest', **evaluate_rf_method(texts, labels)},
        {'Method': 'BERT', **evaluate_bert_method(texts, labels)},
        {'Method': 'Hybrid', **evaluate_hybrid_method(texts, labels)}
    ]

    # Display results
    results_df = pd.DataFrame(results)
    print("\nComparative Analysis of Methods:")
    print(results_df)

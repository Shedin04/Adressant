import pandas as pd
import pickle
from detector import MaliciousContentDetector


# Function to load data from CSV
def load_training_data_from_csv(file_path):
    """Load text data and labels from a CSV file."""
    df = pd.read_csv(file_path)
    texts = df['text'].tolist()
    labels = df['label'].tolist()  # 1 for malicious, 0 for benign
    return texts, labels


def load_feedback_from_csv(feedback_file_path):
    """Load and validate feedback data from a CSV file."""
    try:
        df = pd.read_csv(feedback_file_path)
        required_columns = ['timestamp', 'analyzed_text', 'actual_prediction', 'is_valid', 'is_feedback_valid']

        # Check for required columns
        if not all(col in df.columns for col in required_columns):
            raise ValueError(f"Feedback CSV must contain columns: {required_columns}")

        # Initialize lists for valid feedback
        feedback_texts = []
        feedback_labels = []
        skipped_rows = []

        for index, row in df.iterrows():
            # Skip rows with invalid or missing data
            if pd.isna(row['analyzed_text']) or pd.isna(row['actual_prediction']):
                skipped_rows.append((index, "Missing text or prediction"))
                continue

            # Handle is_feedback_valid
            if row['is_feedback_valid'] == '[TO_UPDATE]':
                skipped_rows.append((index, "is_feedback_valid is [TO_UPDATE]"))
                continue

            # Convert is_feedback_valid and is_valid to boolean
            try:
                is_valid = bool(row['is_valid'])
                is_feedback_valid = row['is_feedback_valid']
                is_feedback_valid = is_feedback_valid == 'True' if isinstance(is_feedback_valid, str) \
                    else is_feedback_valid
                actual_prediction = bool(row['actual_prediction'])
            except ValueError:
                skipped_rows.append(
                    (index, "Invalid boolean value in is_feedback_valid or is_valid or actual_prediction"))
                continue

            if is_feedback_valid:
                if is_valid:
                    feedback_labels.append(1 if actual_prediction else 0)
                else:
                    feedback_labels.append(0 if actual_prediction else 1)
                feedback_texts.append(row['analyzed_text'])
            else:
                skipped_rows.append((index, "Invalid feedback (is_feedback_valid is False)"))

        # Log skipped rows
        if skipped_rows:
            print(f"Skipped {len(skipped_rows)} feedback rows:")
            for index, reason in skipped_rows:
                print(f"Row {index}: {reason}")

        return feedback_texts, feedback_labels

    except FileNotFoundError:
        print(f"Error: Could not find the feedback file {feedback_file_path}")
        return [], []
    except Exception as e:
        print(f"Error processing feedback file: {e}")
        return [], []


# Example of using the MaliciousContentDetector with CSV data
if __name__ == "__main__":
    # Create a detector instance
    detector = MaliciousContentDetector()

    # Load data from a CSV file
    csv_file_path = 'training_data.csv'
    feedback_file_path = 'feedback_data.csv'

    try:
        # Load training data
        training_texts, training_labels = load_training_data_from_csv(csv_file_path)
        print(f"Loaded {len(training_texts)} examples from {csv_file_path}")

        feedback_texts, feedback_labels = load_feedback_from_csv(feedback_file_path)
        print(f"Loaded {len(feedback_texts)} valid feedback examples from {feedback_file_path}")

        # Train the model
        print("Training model...")
        detector.train(training_texts, training_labels, feedback_texts, feedback_labels)
        print("Training complete!")

        # Save the trained model for future use
        model_path = 'malicious_content_detector.pkl'
        with open(model_path, 'wb') as f:
            pickle.dump(detector, f)
        print(f"Model saved to {model_path}")

        # Test the model with a few examples
        test_messages = [
            "Привіт. Як справи?",
            "ТЕРМІНОВО! Ваш рахунок заблоковано! Перейдіть за посиланням зараз!",
            "Передійть за посиланням щоб підтвердити бронювання",
            "Купуйте IPhone 16 Pro Max зі знижкою в Алло",
            "🎯 Зустрічай турнір Біржа Іксів від Dream Play 📈 🚀 Акція проводиться ексклюзивно на First Casino. 💰 Призовий фонд – 500 000 ₴. 💵 Мінімальна ставка – 5 ₴. 🐺 Біржові гонки стартують, тож го скоріше іксувати виграші. 🎰 Топ-5 слотів тижня 🌀 Девіз цього тижня – «Відвертість» 🤑 Грати з бонусом CasinoBonus"
        ]

        print("\nTesting model on example messages:")
        for msg in test_messages:
            result = detector.predict(msg)
            print(f"\nMessage: {msg}")
            print(f"Prediction: {'Malicious' if result['is_malicious'] else 'Benign'}")
            print(f"Confidence: {result['confidence']:.2f}")
            print(f"Type: {result['malicious_type']}")

    except FileNotFoundError:
        print(f"Error: Could not find the file {csv_file_path}")
        print("Please create a CSV file with 'text' and 'label' columns")
        print("Example CSV format:")
        print("text,label")
        print('"ТЕРМІНОВО! Ваш банківський рахунок заблоковано!",1')
        print('"Привіт! Як справи? Давно не бачились.",0')

    except Exception as e:
        print(f"An error occurred: {e}")

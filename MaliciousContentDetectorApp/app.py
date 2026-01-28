import os
from flask import Flask, request, jsonify
import pickle
from werkzeug.exceptions import BadRequest
import logging
from waitress import serve
import csv
from datetime import datetime
from detector import MaliciousContentDetector

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize Flask app
app = Flask(__name__)

# Load the trained model
MODEL_PATH = 'malicious_content_detector.pkl'
FEEDBACK_CSV_PATH = 'feedback_data.csv'

try:
    logger.info(f"Loading model from {MODEL_PATH}")
    with open(MODEL_PATH, 'rb') as f:
        detector = pickle.load(f)
    logger.info("Model loaded successfully")
except Exception as e:
    logger.error(f"Error loading model: {e}")
    detector = None

# Create or initialize feedback CSV file with headers if it doesn't exist
if not os.path.exists(FEEDBACK_CSV_PATH):
    with open(FEEDBACK_CSV_PATH, 'w', newline='') as f:
        writer = csv.writer(f)
        writer.writerow(['timestamp', 'analyzed_text', 'actual_prediction', 'is_valid', 'is_feedback_valid'])


@app.route('/health', methods=['GET'])
def health_check():
    """Endpoint for health checks"""
    if detector is None:
        return jsonify({"status": "error", "message": "Model not loaded"}), 500
    return jsonify({"status": "healthy"}), 200


@app.route('/score', methods=['POST'])
def analyze_text():
    """Endpoint to analyze text messages"""
    # Check if model is loaded
    if detector is None:
        return jsonify({
            "error": "Model not loaded. Please check server logs."
        }), 500

    # Get request data
    try:
        data = request.get_json()
        if not data or 'text' not in data:
            raise BadRequest("Request must include 'text' field")

        text = data['text']
        logger.info(f"Analyzing text: {text[:50]}...")

        # Analyze the text
        result = detector.predict(text)

        return jsonify(result), 200

    except BadRequest as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        logger.error(f"Error processing request: {e}")
        return jsonify({"error": "Internal server error"}), 500


@app.route('/ai-feedback', methods=['POST'])
def store_feedback():
    try:
        data = request.get_json()
        if not data or 'analyzedText' not in data or 'isValid' not in data or 'actualPrediction' not in data:
            raise BadRequest("Request must include 'analyzedText', 'isValid', and 'actualPrediction' fields")

        analyzed_text = data['analyzedText']
        actual_prediction = data['actualPrediction']
        is_valid = data['isValid']
        timestamp = datetime.now().isoformat()

        with open(FEEDBACK_CSV_PATH, 'a', newline='') as f:
            writer = csv.writer(f)
            writer.writerow([timestamp, analyzed_text, actual_prediction, is_valid, '[TO_UPDATE]'])

        logger.info(f"Feedback stored for text: {analyzed_text[:50]}...")
        return jsonify({"status": "success"}), 200

    except BadRequest as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        logger.error(f"Error storing feedback: {e}")
        return jsonify({"error": "Internal server error"}), 500


if __name__ == "__main__":
    # Get port from environment variable or use default
    port = int(os.environ.get('PORT', 8080))

    # Use waitress for production
    logger.info(f"Starting server on port {port}")
    serve(app, host="0.0.0.0", port=port)

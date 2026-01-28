import os
import pickle
from detector import MaliciousContentDetector

model = None


def init():
    global model
    model_path = os.path.join(os.getenv('AZUREML_MODEL_DIR', '.'), 'malicious_content_detector.pkl')
    with open(model_path, 'rb') as f:
        model = pickle.load(f)


def run(input_json):
    import json
    if isinstance(input_json, str):
        input_data = json.loads(input_json)
    else:
        input_data = input_json
    text = input_data.get("text", "")
    return model.predict(text)

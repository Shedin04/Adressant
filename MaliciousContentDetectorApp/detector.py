import emoji
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, confusion_matrix
from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
import numpy as np
from torch.nn.functional import softmax
import spacy
import pandas as pd

# Load multilingual language model (supports Ukrainian and other languages)
nlp = spacy.load("xx_ent_wiki_sm")
nlp.add_pipe("sentencizer")


class MaliciousContentDetector:
    # Hidden

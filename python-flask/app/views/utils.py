from flask import Response, flash, render_template, request, redirect, url_for, jsonify, send_from_directory, session, \
    send_file, Blueprint
import os
import importlib
import threading
import pandas as pd
from werkzeug.utils import secure_filename
from app.utils.model_utils import train_scheduler, training, predict
import traceback
import json

from config import Config


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in Config.ALLOWED_EXTENSIONS

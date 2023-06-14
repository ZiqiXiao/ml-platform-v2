const CURRENT_IP = import.meta.env.VITE_APP_CURRENT_IP;

export default {
  "python-flask": "http://"+CURRENT_IP+":5001",
  "java-springboot": {
    "root": "http://"+CURRENT_IP+":8080",
    "model": "http://"+CURRENT_IP+":8080/model",
    "train-data": "http://"+CURRENT_IP+":8080/train-data",
    "predict-data": "http://"+CURRENT_IP+":8080/predict-data",
  }

};
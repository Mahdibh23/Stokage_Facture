# Utiliser une image Python comme image de base
FROM python:3.9-slim

# Définir le répertoire de travail
WORKDIR /app

# Installer les dépendances
# Installer des outils système nécessaires pour la compilation de certaines dépendances (si besoin)
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential gcc \
    && rm -rf /var/lib/apt/lists/*

# Copier les fichiers requirements.txt et installer les dépendances
COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

# Copier le reste des fichiers de l'application
COPY . .

# Exposer le port 5000
EXPOSE 5000

# Démarrer l'application Flask (ou autre framework)
CMD ["python", "app.py"]

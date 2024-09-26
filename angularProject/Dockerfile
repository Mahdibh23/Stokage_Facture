# Étape 1: Construire l'application Angular
FROM node:20 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste de l'application
COPY . .

# Construire l'application pour production
RUN npm run build --prod

# Étape 2: Servir l'application avec NGINX
FROM nginx:1.21

# Copier l'application construite à partir de l'étape précédente
COPY --from=build /app/dist/[nom_du_projet] /usr/share/nginx/html

# Exposer le port 80
EXPOSE 80

# Démarrer le serveur NGINX
CMD ["nginx", "-g", "daemon off;"]

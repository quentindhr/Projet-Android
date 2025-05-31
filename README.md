# 🧾 Manuel Utilisateur – Application de Boutique en Ligne 

Bienvenue dans l'application de boutique en ligne développée en Kotlin. Cette application vous permet de naviguer dans un catalogue de produits, consulter les détails des articles, les ajouter à un panier, puis valider une commande.

# 📲 Installation de l’application

1. Pré-requis
    - Un smartphone Android ou un émulateur (API 33 ou supérieur)
    - Android Studio installé
1.  Installation
    - Cloner ou télécharger le projet via GitHub
    - Ouvrir le projet dans Android Studio
    - Connecter un téléphone ou lancer un émulateur
    - Lancer l’application avec le bouton ▶️ « Run »
# 🧭 Navigation dans l’application

L’application se compose de trois parties principales :

1. 🏠 Écran d’accueil <br>
Liste tous les articles disponibles issus de l'API https://fakestoreapi.com<br>
Cliquez sur un article pour voir ses détails
2. 🔍 Détails d’un article
Lorsque vous sélectionnez un article :

- Vous verrez :
    - Nom du produit<br>
    - Image<br>
    - Prix<br>
    - Catégorie<br>
    - Description<br>

Deux boutons flottants sont disponibles :<br>
    - 🛒 Ajouter au panier<br>
    - 🔙 Retour en haut à gauche<br>

3. 🛒 Panier<br>

Vous pouvez accéder au panier via l’icône dans l’écran des détails.
Fonctionnalités du panier :

- Voir tous les articles ajoutés
- Visualiser le prix total
- Supprimer un article (option dans CartManager)
- Bouton "Valider la commande" :
    - Affiche un message de confirmation<br>
    - Vide le panier<br>

Si le panier est vide, un message s'affiche : "Le panier est vide"
# 🔌 Connexion à Internet

L’application utilise une API distante. Une connexion Internet est nécessaire pour :

- Charger la liste des produits
- Afficher les images
# ❌ Problèmes fréquents

| Problème | Solutions |
|---|---|
| L’application ne charge aucun article | Vérifiez la connexion Internet |
| Le bouton « Ajouter au panier » ne fonctionne pas | Relancez l’application – il se peut que le panier ne soit pas encore initialisé |
| Crash au démarrage | Assurez-vous que l’API FakeStore est disponible et que l’appareil Android est configuré correctement |
# 📦 Données utilisées

Les produits sont récupérés depuis l’API publique :
🔗 https://fakestoreapi.com/products

Aucune donnée personnelle n’est collectée ou stockée.

# 📌 À savoir

Le panier est temporaire (non enregistré localement) <br>
L’application est un projet de démonstration (pas de paiement réel)

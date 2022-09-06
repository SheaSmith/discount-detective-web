# Navigate to the app folder
cd /var/app

# Pull latest changes
git pull origin master

# Navigate to web directory
cd web

# Compile kotlin and build
./gradlew jsBrowserDistribution

# Fix for bug https://github.com/JetBrains/compose-jb/issues/2273 as skiio files are not generated on first build
./gradlew jsBrowserDistribution

# Clear /var/www/html
rm -r /var/www/html/*

# Add new web files
cp -r multiplatform/build/distributions/* /var/www/html/
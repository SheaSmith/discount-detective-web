# Delete old web files
rm -r /var/app/web

# Copy the new web files to the appropriate place.
cp -r /vagrant/web /var/app/

# Convert Windows to Unix line endings.
find /var/app -type f -exec dos2unix {} \;

# Navigate to web directory
cd /var/app/web

# Compile kotlin and build
./gradlew jsBrowserDevelopmentExecutableDistribution

# Fix for bug https://github.com/JetBrains/compose-jb/issues/2273 as skiio files are not generated on first build
./gradlew jsBrowserDevelopmentExecutableDistribution

# Clear /var/www/html
rm -r /var/www/html/*

# Add new web files
cp -r multiplatform/build/developmentExecutable/* /var/www/html/
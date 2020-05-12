 #create the new directory will contain que October generated apk
  mkdir $HOME/buildApk/ 
  #copy generated apk from build folder to the folder just created
  cp -R app/build/outputs/apk/app-debug.apk $HOME/android/ 
  #go to home and git setup  
  cd $ HOME 
  git config --global user.email "helas@runbox.me" 
  git config --global user.name "Esselans" 
  # Clone the repository in the folder buildApk
  git clone --quiet --branch master = https://esselans:$GITHUB_API_KEY@github.com/Esselans/Edgee master > /dev/null 
  #go into directory and copy data we're interested
  cd master cp -Rf $HOME/android/*. 
  #add, commit and push files
  git add -f.
  git remote add origin https://esselans:$GITHUB_API_KEY@github.com/Esselans/Edgee.git
  . git add -f 
  git commit -m "Travis build $ TRAVIS_BUILD_NUMBER pushed [skip ci]" 
  git push origin master -fq > /dev/null 
  echo -e" Done \ n "

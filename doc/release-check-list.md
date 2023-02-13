# Create .apk

- [ ] Check current branch is master and working tree is clean

    git checkout master
    git pull
    git status

- [ ] Bump version numbers (versionCode and versionName):

    vi app/build.gradle

- [ ] Check translations are up to date

- [ ] Generate signed .apk

    if [ -f app/signing.gradle ] ; then
        ./gradlew assembleRelease
    else
        echo "app/signing.gradle does not exist"
    fi

- Smoke test

- Update CHANGELOG.md. Follow this format: <https://raw.githubusercontent.com/olivierlacan/keep-a-changelog/master/CHANGELOG.md>

    vi CHANGELOG.md

- [ ] Commit

    git add .
    git commit -m "Preparing $VERSION"

- [ ] Push

    git push

# Publish beta

- [ ] Upload to Google Play

- [ ] Wait for Google Play to be API

# Tag

- [ ] Tag

    git tag -a $VERSION -m "Release $VERSION"
    git push --tags

# Publish

- [ ] Take screenshots

- [ ] Publish beta version

- [ ] Write store changelog

- [ ] Create GitHub release

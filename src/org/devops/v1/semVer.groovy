package org.devops.v1

enum PatchLevel {
    MAJOR,
    MINOR,
    PATCH;
    public PatchLevel() {}
}

class SemVer implements Serializable {

    private int major, minor, patch

    SemVer(String version) {
        def versionParts = version.tokenize('.')
        // println versionParts
        if (versionParts.size() != 3) {
            throw new IllegalArgumentException("Wrong version format - expected MAJOR.MINOR.PATCH - got ${version}")
        }
        this.major = versionParts[0].toInteger()
        this.minor = versionParts[1].toInteger()
        this.patch = versionParts[2].toInteger()
    }

    SemVer(int major, int minor, int patch) {
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    SemVer bump(PatchLevel patchLevel) {
        switch (patchLevel) {
            case PatchLevel.MAJOR:
                return new SemVer(major + 1, 0, 0)
                break
            case PatchLevel.MINOR:
                return new SemVer(major, minor + 1, 0)
                break
            case PatchLevel.PATCH:
                return new SemVer(major, minor, patch + 1)
                break
        }
        return new SemVer()
    }

    String toString() {
        return "${major}.${minor}.${patch}"
    }

}

def getNextMinorVersion() {
    def version = sh returnStdout: true, script: 'git describe --tags `git rev-list --tags --max-count=1`'
    if (!(version =~ /\d+\.\d+\.\d+/)) {
        error('No git version tags with format MAJOR.MINOR.PATCH could be found in this project.')
    }

    def currentVersion = new SemVer(version)
    return currentVersion.bump(PatchLevel.MINOR).toString()
}

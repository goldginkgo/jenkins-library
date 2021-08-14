package org.devops.v1

// docker容器直接build
def DockerBuild(buildShell){
    sh """
        ${buildShell}
    """
}
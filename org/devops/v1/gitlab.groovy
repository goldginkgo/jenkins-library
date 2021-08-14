package org.devops.v1

//封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    def gitServer = "http://gitlab.com/api/v4"
    withCredentials([string(credentialsId: 'gitlab-token', variable: 'gitlabToken')]) {
      result = httpRequest customHeaders: [[maskValue: true, name: 'PRIVATE-TOKEN', value: "${gitlabToken}"]],
                httpMode: reqType,
                contentType: "APPLICATION_JSON",
                consoleLogResponseBody: true,
                ignoreSslErrors: true,
                requestBody: reqBody,
                url: "${gitServer}/${reqUrl}"
                //quiet: true
    }
    return result
}

//获取项目ID
def GetProjectID(projectName){
    projectApi = "projects?search=${projectName}"
    response = HttpReq('GET',projectApi,'')
    def result = readJSON text: """${response.content}"""

    for (repo in result){
       // println(repo['path_with_namespace'])
        if (repo['path'] == "${projectName}"){

            repoId = repo['id']
            println(repoId)
        }
    }
    return repoId
}


// 给仓库打tag
def TagGitlab(projectId,tag_name,tag_ref){
    def apiUrl = "projects/${projectId}/repository/tags"
    reqBody = """{"tag_name": "${tag_name}","ref": "${tag_ref}"}"""
    HttpReq('POST',apiUrl,reqBody)
}
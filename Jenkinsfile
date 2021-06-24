@Library('CISharedLibraries@rk-image-push') _

// Map gitConfig=[
//   git:[
//     domain:"github.com",
//     credentialId:"rmahimalur-github",
//     org:"CMS-SSO-TEST",
//     repo:"testing-codeql-runner",
//     buildBranch: "test"
//     ]
// ]

pipeline {
    agent {
        kubernetes {
            defaultContainer 'codeql-cli'
            yaml """
kind: Pod
metadata:
  name: kaniko
spec:
  containers:
  - name: codeql-cli
    image: artifactory.cms.gov/jenkins-core-docker/centos-codeql-cli:latest
    tty: true
    command: ["tail", "-f", "/dev/null"]
    imagePullPolicy: Always
"""
        }
    }
    stages {
      // stage('Git Checkout') {
      //   steps {
      //     container('codeql-cli'){
      //       gitCheckout(gitConfig)
      //     }
      //   }
      // }
      stage('codeql') {
        steps {
          container('codeql-cli'){
            sh "codeql --verisons"
            sh "echo env.GIT_COMMIT"
            sh "echo ${env.GIT_COMMIT}"
          }
        }
      }
    }
}

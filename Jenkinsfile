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
      stage('Creating CodeQL Database') {
        steps {
          container('codeql-cli'){
            //sh "codeql verison"
            sh "codeql database create /tmp/javadb --language=java"
          }
        }
      }
      stage('Analyzing CodeQL Database') {
        steps {
          container('codeql-cli'){
            //sh "codeql verison"
            sh "codeql database analyze /tmp/javadb /opt/codeql/qlpacks/codeql-java/codeql-suites/*.qls --format=sarif-latest --output=/tmp/gradle.sarif"
          }
        }
      }
    stage("Publishing CodeQL scanned results to github"){
      steps {
        container('codeql-cli'){
            withCredentials([usernamePassword(credentialsId: 'rmahimalur-github', usernameVariable: 'GitID', passwordVariable: 'GitPW')]){
              sh "echo $GitPW | codeql  github upload-results --verbose \
                      --repository=CMS-SSO-TEST/testing-codeql-runner --ref=refs/heads/${env.BRANCH_NAME} \
                      --commit=${env.GIT_COMMIT} --sarif=/tmp/gradle.sarif \
                      --github-auth-stdin --github-url=https://github.com/ --log-to-stderr"
              }
          }
        }
      }
      stage('Custom code to pass or fail the build') {
        steps {
          container('codeql-cli'){
            sh '''
                set +e
                ls -al /tmp/
                cat /tmp/gradle.sarif | jq -e '.runs[0].results | select(length > 0)'
                if [ "$?" -eq 0 ]
                then
                echo "Security vulnerabilities/errors identified in the code. Failing the action"
                exit 1
                fi
            '''
          }
        }
      }

    }
}

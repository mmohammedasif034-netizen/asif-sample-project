pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    timestamps()
    timeout(time: 30, unit: 'MINUTES')
  }
  stages {
    stage('Checkout') {
      steps {
        git url: 'https://github.com/mmohammedasif034-netizen/asif-sample-project.git', branch: 'main', credentialsId: 'github-http-credentials'
      }
    }

    stage('Build & Package') {
      steps {
        sh 'mvn -B -DskipTests package'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn -B test'
      }
    }

    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }
  post {
    success { echo 'SUCCESS: Pipeline completed' }
    failure { echo 'FAILURE: Pipeline failed' }
    always { cleanWs() }
  }
}
